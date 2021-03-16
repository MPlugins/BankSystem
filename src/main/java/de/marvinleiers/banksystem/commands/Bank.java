package de.marvinleiers.banksystem.commands;

import de.marvinleiers.banksystem.menus.CreateBankAccountMenu;
import de.marvinleiers.banksystem.menus.ShowBankMenu;
import de.marvinleiers.menuapi.MenuAPI;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Bank extends BankCommand
{
    public Bank(String name)
    {
        super(name, "befehl.bank");
    }

    @Override
    public void onPlayerExecute(Player player, String[] strings)
    {
        if (!financeManager.hasAccount(player))
            new CreateBankAccountMenu(MenuAPI.getMenuUserInformation(player)).open();
        else
            new ShowBankMenu(MenuAPI.getMenuUserInformation(player)).open();
    }

    @Override
    public void onExecute(CommandSender commandSender, String[] strings)
    {

    }
}
