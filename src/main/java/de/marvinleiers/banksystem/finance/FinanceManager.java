package de.marvinleiers.banksystem.finance;

import de.marvinleiers.banksystem.BankSystem;
import de.marvinleiers.banksystem.commands.Bank;
import de.marvinleiers.banksystem.events.PlayerMoneyChangeEvent;
import de.marvinleiers.banksystem.utils.Base64Util;
import de.marvinleiers.banksystem.utils.MoneyType;
import de.marvinleiers.banksystem.utils.MySQL;
import de.marvinleiers.banksystem.utils.TransactionType;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

public class FinanceManager implements Listener
{
    private static Set<UUID> depositToAccount = new HashSet<>();
    private static Set<UUID> withdrawFromAccount = new HashSet<>();
    private static FinanceManager instance;
    private static MySQL mySQL;

    private FinanceManager()
    {
    }

    @EventHandler
    public void onChat(AsyncPlayerChatEvent event)
    {
        Player player = event.getPlayer();
        String msg = event.getMessage();

        if (depositToAccount.contains(player.getUniqueId()))
        {
            event.setCancelled(true);

            if (msg.equalsIgnoreCase("#"))
            {
                depositToAccount.remove(player.getUniqueId());
                return;
            }

            String firstArgument = msg.split(" ")[0].replace(",", ".");

            if (firstArgument.matches("[0-9]+(.[0-9]{1,2})?"))
            {
                double amount = Double.parseDouble(firstArgument);

                if (getCash(player) < amount)
                {
                    player.sendMessage("§cSo viel Geld hast du nicht! Versuche es erneut.");
                    return;
                }

                new BukkitRunnable()
                {
                    @Override
                    public void run()
                    {
                        setCash(player, getCash(player) - amount);
                        setBalance(player, getBankBalance(player) + amount);
                    }
                }.runTask(BankSystem.getMPlugin());

                player.sendMessage("§7Du hast §9" + BankSystem.getEconomy().format(amount) + " §7eingezahlt!");

                depositToAccount.remove(player.getUniqueId());
            }
            else
            {
                player.sendMessage("§cFalsches Format! Bitte gebe eine Zahl ein.");
            }

            return;
        }
        else if (withdrawFromAccount.contains(player.getUniqueId()))
        {
            event.setCancelled(true);

            if (msg.equalsIgnoreCase("#"))
            {
                withdrawFromAccount.remove(player.getUniqueId());
                return;
            }

            String firstArgument = msg.split(" ")[0].replace(",", ".");

            if (firstArgument.matches("[0-9]+(.[0-9]{1,2})?"))
            {
                double amount = Double.parseDouble(firstArgument);

                if (getBankBalance(player) < amount)
                {
                    player.sendMessage("§cSo viel Geld hast du nicht! Versuche es erneut.");
                    return;
                }

                new BukkitRunnable()
                {
                    @Override
                    public void run()
                    {
                        setCash(player, getCash(player) + amount);
                        setBalance(player, getBankBalance(player) - amount);
                    }
                }.runTask(BankSystem.getMPlugin());

                player.sendMessage("§7Du hast §9" + BankSystem.getEconomy().format(amount) + " §7abgehoben!");

                withdrawFromAccount.remove(player.getUniqueId());
            }
            else
            {
                player.sendMessage("§cFalsches Format! Bitte gebe eine Zahl ein.");
            }
        }
    }

    public void depositToBankAccount(Player player)
    {
        depositToAccount.add(player.getUniqueId());

        player.sendMessage("§7Schreibe den §9Betrag in den Chat§7, den du gerne einzahlen möchtest oder schreibe §c#§7, um abzubrechen!");
    }

    public void withdrawFromAccount(Player player)
    {
        withdrawFromAccount.add(player.getUniqueId());

        player.sendMessage("§7Schreibe den §9Betrag in den Chat§7, den du gerne abheben möchtest oder schreibe §c#§7, um abzubrechen!");
    }

    public ItemStack[] getVaultContents(OfflinePlayer player)
    {
        if (!exists(player, "vaults")) return null;

        String query = "SELECT content FROM vaults WHERE uuid = '" + player.getUniqueId().toString() + "';";

        try
        {
            Connection connection = mySQL.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next())
            {
                String content = resultSet.getString("content");

                List<ItemStack> items = Base64Util.encode(content);
                ItemStack[] contents = new ItemStack[items.size()];

                for (int i = 0; i < 27; i++)
                    contents[i] = items.get(i);

                resultSet.close();
                preparedStatement.close();
                connection.close();
                return contents;
            }

            resultSet.close();
            preparedStatement.close();
            connection.close();
        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }

        return null;
    }

    public void addTransaction(OfflinePlayer initator, OfflinePlayer recipient, TransactionType transactionType, double amount)
    {
        mySQL.update("INSERT INTO transactions (initiator, recipient, amount, type, created) VALUES (" +
                "'" + initator.getUniqueId().toString() + "'," +
                "'" + recipient.getUniqueId().toString() + "'," +
                "" + amount + "," +
                "'" + transactionType.toString() + "'," +
                "'" + System.currentTimeMillis() + "');");
    }

    public void setBalance(OfflinePlayer offlinePlayer, double newBalance)
    {
        mySQL.update("UPDATE balance SET balance='" + newBalance + "' WHERE uuid='"
                + offlinePlayer.getUniqueId().toString() + "';");

        Bukkit.getPluginManager().callEvent(new PlayerMoneyChangeEvent(offlinePlayer, newBalance, MoneyType.BALANCE));
    }

    public void setCash(OfflinePlayer offlinePlayer, double newBalance)
    {
        double rounded = Math.round(newBalance * 100.0) / 100.0;

        mySQL.update("UPDATE cash SET amount='" + rounded + "' WHERE uuid='"
                + offlinePlayer.getUniqueId().toString() + "';");

        Bukkit.getPluginManager().callEvent(new PlayerMoneyChangeEvent(offlinePlayer, rounded, MoneyType.CASH));
    }

    public double getCash(OfflinePlayer offlinePlayer)
    {
        String query = "SELECT amount FROM cash WHERE uuid = '" + offlinePlayer.getUniqueId().toString() + "';";

        try
        {
            Connection connection = mySQL.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next())
            {
                double d = resultSet.getDouble("amount");

                resultSet.close();
                preparedStatement.close();
                connection.close();
                return d;
            }

            resultSet.close();
            preparedStatement.close();
            connection.close();
        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }

        return 0d;
    }

    public double getBankBalance(OfflinePlayer offlinePlayer)
    {
        String query = "SELECT balance FROM balance WHERE UUID='" + offlinePlayer.getUniqueId().toString() + "';";

        try
        {
            Connection connection = mySQL.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next())
            {
                double d = resultSet.getDouble("balance");

                resultSet.close();
                preparedStatement.close();
                connection.close();
                return d;
            }

            resultSet.close();
            preparedStatement.close();
            connection.close();
        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }

        return 0d;
    }

    public boolean hasAccount(OfflinePlayer offlinePlayer)
    {
        return exists(offlinePlayer, "balance");
    }

    public List<Transaction> getLastTransactions(Player player)
    {
        String query = "SELECT recipient, amount, type FROM `transactions` WHERE initiator='" + player.getUniqueId().toString() + "' ORDER BY created DESC LIMIT 3";
        List<Transaction> transactions = new ArrayList<>();

        try
        {
            Connection connection = mySQL.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(query);

            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next())
            {
                double amount = resultSet.getDouble("amount");
                String to = resultSet.getString("recipient");
                TransactionType type = TransactionType.valueOf(resultSet.getString("type"));

                transactions.add(new Transaction(to, type, amount));
            }

            resultSet.close();
            preparedStatement.close();
            connection.close();
        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }

        return transactions;
    }

    private boolean exists(OfflinePlayer player, String table)
    {
        try
        {
            Connection connection = mySQL.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT ID FROM " + table + " WHERE uuid='"
                    + player.getUniqueId().toString() + "';");

            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next())
            {
                boolean bool = resultSet.getInt("id") != 0;
                resultSet.close();
                preparedStatement.close();
                connection.close();
                return bool;
            }

            resultSet.close();
            preparedStatement.close();
            connection.close();
        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }

        return false;
    }

    public void createBankAccount(Player player, String pin)
    {
        if (!exists(player, "balance"))
        {
            mySQL.update("INSERT INTO balance (uuid, balance, pin) VALUES ('" + player.getUniqueId().toString() + "', " +
                    "'0', '" + pin + "')");

            BankSystem.getMPlugin().log("Added new account for " + player.getName());
        }
    }

    public void createUser(Player player)
    {
        if (!exists(player, "cash"))
        {
            mySQL.update("INSERT INTO cash (uuid, amount) VALUES ('" + player.getUniqueId().toString() + "', '5000')");
            BankSystem.getMPlugin().log("Added new account for " + player.getName());
        }
    }

    public static FinanceManager getInstance()
    {
        if (instance == null)
            instance = new FinanceManager();

        mySQL = BankSystem.getMySQL();
        return instance;
    }
}
