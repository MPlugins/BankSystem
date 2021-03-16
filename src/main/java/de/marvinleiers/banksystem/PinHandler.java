package de.marvinleiers.banksystem;

import de.marvinleiers.banksystem.finance.FinanceManager;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class PinHandler implements Listener
{
    private final static Set<UUID> players = new HashSet<>();
    private static PinHandler instance;

    @EventHandler
    public void onChat(AsyncPlayerChatEvent event)
    {
        Player player = event.getPlayer();

        if (!players.contains(player.getUniqueId())) return;

        event.setCancelled(true);

        String msg = event.getMessage();

        if (!msg.split(" ")[0].matches("[0-9]{4}"))
        {
            player.sendMessage("§cFalsches PIN-Format! Deine PIN muss aus vier Ziffern bestehen!");
            return;
        }

        int pin = Integer.parseInt(msg.split(" ")[0]);

        player.sendMessage("§7Deine PIN ist §9" + pin + "§7, bitte schreibe dir diese auf oder merke sie dir sehr gut!");
        players.remove(player.getUniqueId());

        FinanceManager financeManager = FinanceManager.getInstance();
        financeManager.createBankAccount(player, String.valueOf(pin));

        new BukkitRunnable()
        {
            @Override
            public void run()
            {
                financeManager.setCash(player, financeManager.getCash(player) - 200);
            }
        }.runTask(BankSystem.getMPlugin());
    }

    private PinHandler()
    {
    }

    public void createBankAccountWithPin(Player player)
    {
        players.add(player.getUniqueId());

        player.sendMessage("§7Bitte gebe deine §9Wunsch-PIN §7in den Chat ein!");
    }

    public static PinHandler getInstance()
    {
        if (instance == null)
            instance = new PinHandler();

        return instance;
    }
}
