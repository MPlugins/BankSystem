package de.marvinleiers.banksystem.listeners;

import de.marvinleiers.banksystem.events.PlayerMoneyChangeEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class PlayerMoneyChangeListener implements Listener
{
    @EventHandler
    public void onMoneyChange(PlayerMoneyChangeEvent event)
    {
        System.out.println("new balance of " + event.getWho().getName() + " is " + event.getNewBalance());
    }
}
