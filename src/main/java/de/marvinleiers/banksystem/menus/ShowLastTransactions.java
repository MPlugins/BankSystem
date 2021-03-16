package de.marvinleiers.banksystem.menus;

import de.marvinleiers.banksystem.BankSystem;
import de.marvinleiers.banksystem.finance.Transaction;
import de.marvinleiers.banksystem.utils.TransactionType;
import de.marvinleiers.menuapi.Menu;
import de.marvinleiers.menuapi.MenuUserInformation;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.event.inventory.InventoryClickEvent;

import java.util.List;
import java.util.UUID;

public class ShowLastTransactions extends Menu
{
    private final List<Transaction> transactions;

    public ShowLastTransactions(List<Transaction> transactions, MenuUserInformation menuUserInformation)
    {
        super(menuUserInformation);

        this.transactions = transactions;
    }

    @Override
    public String getTitle()
    {
        return "Transaktionshistorie";
    }

    @Override
    public int getSlots()
    {
        return 27;
    }

    @Override
    public void setItems()
    {
        Economy eco = BankSystem.getEconomy();

        for (int i = 0; i < getSlots(); i++)
            inventory.setItem(i, makeItem(Material.BLACK_STAINED_GLASS_PANE, " "));

        try
        {
            inventory.setItem(11, makeItem(Material.PAPER, "§e" + Bukkit.getOfflinePlayer(UUID.fromString(transactions.get(0).getTo())).getName() +
                    " » " + (transactions.get(0).getType() == TransactionType.RECEIVE ? " §a§o+" + eco.format(transactions.get(0).getAmount())
                    : " §c§o-" + eco.format(transactions.get(0).getAmount()))));
        }
        catch (Exception e)
        {
            inventory.setItem(11, makeItem(Material.PAPER, "§e§oLeer"));
        }

        try
        {
            inventory.setItem(13, makeItem(Material.PAPER, "§e" + Bukkit.getOfflinePlayer(UUID.fromString(transactions.get(1).getTo())).getName() +
                    " » " + (transactions.get(1).getType() == TransactionType.RECEIVE ? " §a§o+" + eco.format(transactions.get(1).getAmount())
                    : " §c§o-" + eco.format(transactions.get(1).getAmount()))));
        }
        catch (Exception e)
        {
            inventory.setItem(13, makeItem(Material.PAPER, "§e§oLeer"));
        }

        try
        {
            inventory.setItem(15, makeItem(Material.PAPER, "§e" + Bukkit.getOfflinePlayer(UUID.fromString(transactions.get(2).getTo())).getName() +
                    " » " + (transactions.get(2).getType() == TransactionType.RECEIVE ? " §a§o+" + eco.format(transactions.get(2).getAmount())
                    : " §c§o-" + eco.format(transactions.get(2).getAmount()))));
        }
        catch (Exception e)
        {
            inventory.setItem(15, makeItem(Material.PAPER, "§e§oLeer"));
        }
    }

    @Override
    public void handleClickActions(InventoryClickEvent inventoryClickEvent)
    {

    }
}
