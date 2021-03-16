package de.marvinleiers.banksystem.menus;

import de.marvinleiers.banksystem.finance.FinanceManager;
import de.marvinleiers.banksystem.finance.Transaction;
import de.marvinleiers.menuapi.Menu;
import de.marvinleiers.menuapi.MenuAPI;
import de.marvinleiers.menuapi.MenuUserInformation;
import org.bukkit.Material;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class ShowBankMenu extends Menu
{
    public ShowBankMenu(MenuUserInformation menuUserInformation)
    {
        super(menuUserInformation);
    }

    @Override
    public String getTitle()
    {
        return "§7Konto von: §9" + player.getName();
    }

    @Override
    public int getSlots()
    {
        return 54;
    }

    @Override
    public void setItems()
    {
        List<Integer> bluePlaceholder = Arrays.asList(1, 2, 3, 4, 5, 6, 7, 9, 17, 26, 35, 53, 9, 18, 27, 45, 37, 38, 39, 40, 41, 42, 43);

        for (int i = 0; i < getSlots(); i++)
            inventory.setItem(i, makeItem(bluePlaceholder.contains(i) ? Material.BLUE_STAINED_GLASS_PANE :
                    Material.BLACK_STAINED_GLASS_PANE, " "));

        inventory.setItem(20, makeItem(Material.CHEST, "§7» §eEinzahlen",
                Collections.singletonList("§7§oZahle Geld auf dein Konto ein.")));
        inventory.setItem(22, makeItem(Material.DISPENSER, "§7» §eAuszahlen",
                Collections.singletonList("§7§oBuche Geld von deinem Konto ab.")));
        inventory.setItem(24, makeItem(Material.MAP, "§7» §eAufträge"));

        inventory.setItem(48, makeItem(Material.TRIPWIRE_HOOK, "§7» §eSchließfach",
                Collections.singletonList("§7§oÖffne dein Schließfach.")));
        inventory.setItem(49, makeItem(Material.BARRIER, "§cSchließen"));
        inventory.setItem(52, makeItem(Material.GOLD_BLOCK, "§7» §eUpgrade",
                Collections.singletonList("§7§oComing soon...")));
    }

    @Override
    public void handleClickActions(InventoryClickEvent inventoryClickEvent)
    {
        ItemStack item = inventoryClickEvent.getCurrentItem();
        FinanceManager financeManager = FinanceManager.getInstance();

        switch (item.getType())
        {
            case TRIPWIRE_HOOK:
                new VaultMenu(MenuAPI.getMenuUserInformation(player)).open();
                break;
            case CHEST:
                financeManager.depositToBankAccount(player);
                player.closeInventory();
                break;
            case DISPENSER:
                financeManager.withdrawFromAccount(player);
                player.closeInventory();
                break;
            case MAP:
                List<Transaction> transactions = financeManager.getLastTransactions(player);
                new ShowLastTransactions(transactions, MenuAPI.getMenuUserInformation(player)).open();
                break;
            case BARRIER:
                player.closeInventory();
                break;
        }
    }
}
