package de.marvinleiers.banksystem.listeners;

import de.marvinleiers.banksystem.BankSystem;
import de.marvinleiers.banksystem.finance.FinanceManager;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import java.util.Random;

public class PlayerDeathListener implements Listener
{
    private final static Random random = new Random();

    @EventHandler
    public void onDeath(PlayerDeathEvent event)
    {
        Player player = event.getEntity().getPlayer();

        FinanceManager financeManager = FinanceManager.getInstance();
        double cash = financeManager.getCash(player);
        double percentage = (random.nextInt(35 - 15) + 15) / 100f;
        double lost = cash * percentage;
        double rounded = Math.round(lost * 100.0) / 100.0;

        if (lost > 0)
        {
            createMoneyDrop(rounded, player.getLocation());

            financeManager.setCash(player, financeManager.getCash(player) - rounded);

            player.sendMessage("§7Du hast §9" + BankSystem.getEconomy().format(rounded) + " §7verloren!");
        }
    }

    private void createMoneyDrop(double amount, Location location)
    {
        ItemStack itemStack = new ItemStack(Material.SUNFLOWER);
        ItemMeta meta = itemStack.getItemMeta();
        meta.setDisplayName("§6Cash: §7" + BankSystem.getEconomy().format(amount));
        itemStack.setItemMeta(meta);
        itemStack.addUnsafeEnchantment(Enchantment.DURABILITY, 1);

        Item item = location.getWorld().dropItem(location, itemStack);
        PersistentDataContainer persistentDataContainer = item.getPersistentDataContainer();
        persistentDataContainer.set(new NamespacedKey(BankSystem.getMPlugin(), "mplugin.cashdrop"),
                PersistentDataType.DOUBLE, amount);
    }
}
