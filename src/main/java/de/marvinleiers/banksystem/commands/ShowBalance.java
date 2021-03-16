package de.marvinleiers.banksystem.commands;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class ShowBalance extends BankCommand
{
    public ShowBalance(String name)
    {
        super(name, "befehl.money", true);
    }

    @Override
    public void onPlayerExecute(Player player, String[] args)
    {
        player.sendMessage("§7Du hast §9" + economy.format(economy.getBalance(player)) + " §7auf deinem Konto und §9"
                + economy.format(financeManager.getCash(player)) + " §7Bargeld");
    }

    @Override
    public void onExecute(CommandSender commandSender, String[] args)
    {
        commandSender.sendMessage("/money <player>");
    }
}
