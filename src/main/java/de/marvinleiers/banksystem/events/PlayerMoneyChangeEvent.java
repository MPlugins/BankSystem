package de.marvinleiers.banksystem.events;

import de.marvinleiers.banksystem.utils.MoneyType;
import org.bukkit.OfflinePlayer;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class PlayerMoneyChangeEvent extends Event
{
    private static final HandlerList handlers = new HandlerList();
    private final OfflinePlayer who;
    private final double newBalance;
    private final MoneyType type;

    public PlayerMoneyChangeEvent(OfflinePlayer who, double newBalance, MoneyType type)
    {
        this.who = who;
        this.newBalance = newBalance;
        this.type = type;
    }

    public MoneyType getType()
    {
        return type;
    }

    public double getNewBalance()
    {
        return newBalance;
    }

    public OfflinePlayer getWho()
    {
        return who;
    }

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }
}
