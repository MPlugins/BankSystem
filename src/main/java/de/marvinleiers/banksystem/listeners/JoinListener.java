package de.marvinleiers.banksystem.listeners;

import de.marvinleiers.banksystem.BankSystem;
import de.marvinleiers.banksystem.finance.FinanceManager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class JoinListener implements Listener
{
    @EventHandler
    public void onJoin(PlayerJoinEvent event)
    {
        FinanceManager financeManager = BankSystem.getFinanceManager();
        financeManager.createUser(event.getPlayer());
    }
}
