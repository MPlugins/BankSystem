package de.marvinleiers.banksystem.commands;

import de.marvinleiers.banksystem.BankSystem;
import de.marvinleiers.banksystem.finance.FinanceManager;
import de.marvinleiers.mplugin.commands.Command;
import net.milkbowl.vault.economy.Economy;

public abstract class BankCommand extends Command
{
    protected static final Economy economy = BankSystem.getEconomy();
    protected static final FinanceManager financeManager = FinanceManager.getInstance();

    public BankCommand(String name)
    {
        super(name);
    }

    public BankCommand(String name, String permission)
    {
        super(name, permission);
    }

    public BankCommand(String name, boolean allowConsole)
    {
        super(name, allowConsole);
    }

    public BankCommand(String name, String permission, boolean allowConsole)
    {
        super(name, permission, allowConsole);
    }
}
