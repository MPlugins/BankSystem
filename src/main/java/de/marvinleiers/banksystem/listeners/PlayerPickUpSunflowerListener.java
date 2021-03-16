package de.marvinleiers.banksystem.listeners;

import de.marvinleiers.banksystem.BankSystem;
import de.marvinleiers.banksystem.finance.FinanceManager;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityPickupItemEvent;
import org.bukkit.persistence.PersistentDataType;

public class PlayerPickUpSunflowerListener implements Listener
{
    @EventHandler
    public void onPickUp(EntityPickupItemEvent event)
    {
        if (!(event.getEntity() instanceof Player)) return;

        Player player = (Player) event.getEntity();
        FinanceManager financeManager = FinanceManager.getInstance();
        Item item = event.getItem();

        if (item.getPersistentDataContainer().has(new NamespacedKey(BankSystem.getMPlugin(), "mplugin.cashdrop"),
                PersistentDataType.DOUBLE))
        {
            double amount = item.getPersistentDataContainer().get(new NamespacedKey(BankSystem.getMPlugin(), "mplugin.cashdrop"),
                    PersistentDataType.DOUBLE);

            event.setCancelled(true);
            item.remove();

            financeManager.setCash(player, financeManager.getCash(player) + amount);
            player.spigot().sendMessage(ChatMessageType.ACTION_BAR,
                    TextComponent.fromLegacyText("§7Du hast §9" + BankSystem.getEconomy().format(amount) + " §7gefunden!"));
        }
    }
}
