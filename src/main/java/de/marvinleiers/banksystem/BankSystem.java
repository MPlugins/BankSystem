package de.marvinleiers.banksystem;

import de.marvinleiers.banksystem.commands.Bank;
import de.marvinleiers.banksystem.commands.Pay;
import de.marvinleiers.banksystem.commands.ShowBalance;
import de.marvinleiers.banksystem.economy.VaultEconomy;
import de.marvinleiers.banksystem.finance.FinanceManager;
import de.marvinleiers.banksystem.listeners.JoinListener;
import de.marvinleiers.banksystem.listeners.PlayerDeathListener;
import de.marvinleiers.banksystem.listeners.PlayerMoneyChangeListener;
import de.marvinleiers.banksystem.listeners.PlayerPickUpSunflowerListener;
import de.marvinleiers.banksystem.utils.MySQL;
import de.marvinleiers.menuapi.MenuAPI;
import de.marvinleiers.mplugin.MPlugin;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.plugin.ServicePriority;

public final class BankSystem extends MPlugin
{
    //TODO: Einzahlen, Auszahlen.
    //TODO: Aufträge anzeigen.

    private static Economy economy;
    private static MySQL mySQL;

    @Override
    protected void onStart()
    {
        MenuAPI.setUp(this);

        tryVaultHook();
        prepareMySQL();

        getServer().getPluginManager().registerEvents(new JoinListener(), this);
        getServer().getPluginManager().registerEvents(PinHandler.getInstance(), this);
        getServer().getPluginManager().registerEvents(new PlayerDeathListener(), this);
        getServer().getPluginManager().registerEvents(FinanceManager.getInstance(), this);
        getServer().getPluginManager().registerEvents(new PlayerMoneyChangeListener(), this);
        getServer().getPluginManager().registerEvents(new PlayerPickUpSunflowerListener(), this);

        new ShowBalance("money");
        new Bank("bank");
        new Pay("pay");
    }

    @Override
    protected void onShutdown()
    {
        mySQL.close();
    }

    private void prepareMySQL()
    {
        mySQL = new MySQL(getConfig().getString("mysql-host"), getConfig().getString("mysql-database"),
                getConfig().getString("mysql-user"), getConfig().getString("mysql-password"));

        try
        {
            mySQL.connect();
            mySQL.createTable("balance", "(id INT AUTO_INCREMENT PRIMARY KEY, uuid TEXT(38), balance DOUBLE, pin TEXT(32))");
            mySQL.createTable("cash", "(id INT AUTO_INCREMENT PRIMARY KEY, uuid TEXT(38), amount DOUBLE)");
            mySQL.createTable("transactions", "(id INT AUTO_INCREMENT PRIMARY KEY, initiator TEXT(38), recipient TEXT(38), amount DOUBLE, type TEXT(38), created LONG)");
            mySQL.createTable("vaults", "(id INT AUTO_INCREMENT PRIMARY KEY, uuid Text(38), content Text)");
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    private void tryVaultHook()
    {
        if (getServer().getPluginManager().getPlugin("Vault") == null)
        {
            log("§4Für diese Plugin wird Vault benötigt! Deaktiviere...");
            getServer().getPluginManager().disablePlugin(this);

            return;
        }

        if (economy == null)
            economy = new VaultEconomy();

        Bukkit.getServicesManager().register(Economy.class, economy, this, ServicePriority.Highest);
        log("Service registered!");
    }

    public static MySQL getMySQL()
    {
        return mySQL;
    }

    public static FinanceManager getFinanceManager()
    {
        return FinanceManager.getInstance();
    }

    public static Economy getEconomy()
    {
        return economy;
    }
}
