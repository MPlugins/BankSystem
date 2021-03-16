package de.marvinleiers.banksystem.economy;

import de.marvinleiers.banksystem.BankSystem;
import de.marvinleiers.banksystem.finance.FinanceManager;
import net.milkbowl.vault.economy.Economy;
import net.milkbowl.vault.economy.EconomyResponse;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;

import java.text.DecimalFormat;
import java.util.List;

public class VaultEconomy implements Economy
{
    private static final DecimalFormat decimalFormat = new DecimalFormat("#,##0.00");
    private static final BankSystem plugin = BankSystem.getPlugin(BankSystem.class);
    private static final FinanceManager financeManager = FinanceManager.getInstance();

    @Override
    public boolean isEnabled()
    {
        return BankSystem.getMPlugin().isEnabled();
    }

    @Override
    public String getName()
    {
        return BankSystem.getMPlugin().getName();
    }

    @Override
    public boolean hasBankSupport()
    {
        return false;
    }

    @Override
    public int fractionalDigits()
    {
        return 2;
    }

    @Override
    public String format(double amount)
    {
        return decimalFormat.format(amount) + " " + ((amount == 1) ? currencyNameSingular() : currencyNamePlural());
    }

    @Override
    public String currencyNamePlural()
    {
        return plugin.getConfig().getString("currency-plural");
    }

    @Override
    public String currencyNameSingular()
    {
        return plugin.getConfig().getString("currency-singular");
    }

    @Override
    public boolean hasAccount(String playerName)
    {
        //TODO
        return false;
    }

    @Override
    public boolean hasAccount(OfflinePlayer player)
    {
        //TODO
        return false;
    }

    @Override
    public boolean hasAccount(String playerName, String worldName)
    {
        //TODO
        return false;
    }

    @Override
    public boolean hasAccount(OfflinePlayer player, String worldName)
    {
        //TODO
        return false;
    }

    @Override
    public double getBalance(String playerName)
    {
        return getBalance(Bukkit.getOfflinePlayer(playerName));
    }

    @Override
    public double getBalance(OfflinePlayer player)
    {
        plugin.log(player.getName() + "'s balance requested.");

        return financeManager.getBankBalance(player);
    }

    @Override
    public double getBalance(String playerName, String world)
    {
        return getBalance(playerName);
    }

    @Override
    public double getBalance(OfflinePlayer player, String world)
    {
       return getBalance(player);
    }

    @Override
    public boolean has(String playerName, double amount)
    {
        return getBalance(playerName) >= amount;
    }

    @Override
    public boolean has(OfflinePlayer player, double amount)
    {
        return getBalance(player) >= amount;
    }

    @Override
    public boolean has(String playerName, String worldName, double amount)
    {
        return getBalance(playerName) >= amount;
    }

    @Override
    public boolean has(OfflinePlayer player, String worldName, double amount)
    {
        return getBalance(player) >= amount;
    }

    @Override
    public EconomyResponse withdrawPlayer(String playerName, double amount)
    {
        return withdrawPlayer(Bukkit.getOfflinePlayer(playerName), amount);
    }

    @Override
    public EconomyResponse withdrawPlayer(OfflinePlayer player, double amount)
    {
        financeManager.setBalance(player, getBalance(player) - amount);

        return new EconomyResponse(amount, getBalance(player), EconomyResponse.ResponseType.SUCCESS, "OK");
    }

    @Override
    public EconomyResponse withdrawPlayer(String playerName, String worldName, double amount)
    {
        return withdrawPlayer(Bukkit.getOfflinePlayer(playerName), amount);
    }

    @Override
    public EconomyResponse withdrawPlayer(OfflinePlayer player, String worldName, double amount)
    {
        return withdrawPlayer(player, amount);
    }

    @Override
    public EconomyResponse depositPlayer(String playerName, double amount)
    {
        return depositPlayer(Bukkit.getOfflinePlayer(playerName), amount);
    }

    @Override
    public EconomyResponse depositPlayer(OfflinePlayer player, double amount)
    {
        financeManager.setBalance(player, getBalance(player) + amount);

        return new EconomyResponse(amount, getBalance(player), EconomyResponse.ResponseType.SUCCESS, "OK");
    }

    @Override
    public EconomyResponse depositPlayer(String playerName, String worldName, double amount)
    {
        return depositPlayer(playerName, amount);
    }

    @Override
    public EconomyResponse depositPlayer(OfflinePlayer player, String worldName, double amount)
    {
        return depositPlayer(player, amount);
    }

    @Override
    public EconomyResponse createBank(String name, String player)
    {
        return null;
    }

    @Override
    public EconomyResponse createBank(String name, OfflinePlayer player)
    {
        return null;
    }

    @Override
    public EconomyResponse deleteBank(String name)
    {
        return null;
    }

    @Override
    public EconomyResponse bankBalance(String name)
    {
        return null;
    }

    @Override
    public EconomyResponse bankHas(String name, double amount)
    {
        return null;
    }

    @Override
    public EconomyResponse bankWithdraw(String name, double amount)
    {
        return null;
    }

    @Override
    public EconomyResponse bankDeposit(String name, double amount)
    {
        return null;
    }

    @Override
    public EconomyResponse isBankOwner(String name, String playerName)
    {
        return null;
    }

    @Override
    public EconomyResponse isBankOwner(String name, OfflinePlayer player)
    {
        return null;
    }

    @Override
    public EconomyResponse isBankMember(String name, String playerName)
    {
        return null;
    }

    @Override
    public EconomyResponse isBankMember(String name, OfflinePlayer player)
    {
        return null;
    }

    @Override
    public List<String> getBanks()
    {
        return null;
    }

    @Override
    public boolean createPlayerAccount(String playerName)
    {
        return false;
    }

    @Override
    public boolean createPlayerAccount(OfflinePlayer player)
    {
        return false;
    }

    @Override
    public boolean createPlayerAccount(String playerName, String worldName)
    {
        return false;
    }

    @Override
    public boolean createPlayerAccount(OfflinePlayer player, String worldName)
    {
        return false;
    }
}
