package de.marvinleiers.banksystem.commands;

import de.marvinleiers.banksystem.utils.TransactionType;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.HashSet;
import java.util.Set;

public class Pay extends BankCommand
{
    private static Set<String> confirm = new HashSet<>();

    public Pay(String name)
    {
        super(name, "befehl.money", true);
    }

    @Override
    public void onPlayerExecute(Player player, String[] args)
    {
        if (args.length == 2)
        {
            OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(args[0]);

            if (!financeManager.hasAccount(offlinePlayer))
            {
                player.sendMessage("§cDer Spieler §e" + args[0] + " §chat noch kein Konto!");
                return;
            }

            if (offlinePlayer.getUniqueId().equals(player.getUniqueId()))
            {
                player.sendMessage("§cDu kannst dir selbst kein Geld überweisen!");
                return;
            }

            double amount = 0;

            try
            {
                amount = Double.parseDouble(args[1].replace(",", "."));
            }
            catch (NumberFormatException e)
            {
                player.sendMessage("§e" + args[1] + " §cist kein gültiger Wert!");
                return;
            }

            if (amount <= 0)
            {
                player.sendMessage("§e" + args[1] + " §cist kein gültiger Wert!");
                return;
            }

            if (!economy.has(player, amount))
            {
                player.sendMessage("§cSo viel Geld hast du nicht!");
                return;
            }

            if (amount >= 2500)
            {
                confirm.add(player.getUniqueId().toString());
                player.sendMessage("§cBestätige die Transaktion von §e" + economy.format(amount) + " §can §e" + offlinePlayer.getName()
                        + " §cmit §e/pay " + offlinePlayer.getName() + " " + economy.format(amount).split(" ")[0]
                        .replace(".", "") + " confirm");
            }
            else
            {
                economy.withdrawPlayer(player, amount);
                economy.depositPlayer(offlinePlayer, amount);
                financeManager.addTransaction(player, offlinePlayer, TransactionType.SEND, amount);
                financeManager.addTransaction(offlinePlayer, player, TransactionType.RECEIVE, amount);

                player.sendMessage("§7Du hast §9" + economy.format(amount) + " §7an §9" + offlinePlayer.getName() + " §7überwiesen!");

                if (offlinePlayer.isOnline())
                    ((Player) offlinePlayer).sendMessage("§9" + player.getName() + " §7hat dir §9" + economy.format(amount) + " §7überwiesen!");
            }
        }
        else if (args.length == 3)
        {
            if (!args[2].equalsIgnoreCase("confirm"))
            {
                player.sendMessage("§cBenutze: /pay <player> <amount>");
                return;
            }

            OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(args[0]);

            if (!financeManager.hasAccount(offlinePlayer))
            {
                player.sendMessage("§cDer Spieler §e" + args[0] + " §chat noch kein Konto!");
                return;
            }

            if (offlinePlayer.getUniqueId().equals(player.getUniqueId()))
            {
                player.sendMessage("§cDu kannst dir selbst kein Geld überweisen!");
                return;
            }

            double amount = 0;

            try
            {
                amount = Double.parseDouble(args[1].replace(",", "."));
            }
            catch (NumberFormatException e)
            {
                player.sendMessage("§e" + args[1] + " §cist kein gültiger Wert!");
                return;
            }

            if (amount <= 0)
            {
                player.sendMessage("§e" + args[1] + " §cist kein gültiger Wert!");
                return;
            }

            if (!economy.has(player, amount))
            {
                player.sendMessage("§cSo viel Geld hast du nicht!");
                return;
            }

            if (!confirm.contains(player.getUniqueId().toString()))
            {
                confirm.add(player.getUniqueId().toString());

                player.sendMessage("§cBestätige die Transaktion von §e" + economy.format(amount) + " §can §e" + offlinePlayer.getName()
                        + " §cmit §e/pay " + offlinePlayer.getName() + " " + economy.format(amount).split(" ")[0]
                        .replace(".", "") + " confirm");

                return;
            }

            confirm.remove(player.getUniqueId().toString());

            economy.withdrawPlayer(player, amount);
            economy.depositPlayer(offlinePlayer, amount);
            financeManager.addTransaction(player, offlinePlayer, TransactionType.SEND, amount);
            financeManager.addTransaction(offlinePlayer, player, TransactionType.RECEIVE, amount);

            player.sendMessage("§7Du hast §9" + economy.format(amount) + " §7an §9" + offlinePlayer.getName() + " §7überwiesen!");

            if (offlinePlayer.isOnline())
                ((Player) offlinePlayer).sendMessage("§9" + player.getName() + " §7hat dir §9" + economy.format(amount) + " §7überwiesen!");

        }
        else
        {
            player.sendMessage("§cBenutze: /pay <player> <amount>");
        }
    }

    @Override
    public void onExecute(CommandSender commandSender, String[] args)
    {

    }
}
