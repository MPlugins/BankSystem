package de.marvinleiers.banksystem.menus;

import de.marvinleiers.banksystem.BankSystem;
import de.marvinleiers.banksystem.PinHandler;
import de.marvinleiers.banksystem.commands.Bank;
import de.marvinleiers.banksystem.finance.FinanceManager;
import de.marvinleiers.menuapi.Menu;
import de.marvinleiers.menuapi.MenuUserInformation;
import org.bukkit.Material;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class CreateBankAccountMenu extends Menu
{
    public CreateBankAccountMenu(MenuUserInformation menuUserInformation)
    {
        super(menuUserInformation);
    }

    @Override
    public String getTitle()
    {
        return "§9Konto erstellen";
    }

    @Override
    public int getSlots()
    {
        return 54;
    }

    @Override
    public void setItems()
    {
        List<Integer> bluePlaceholder = Arrays.asList(1, 2, 3, 4, 5, 6, 7, 9, 17, 26, 35, 53, 9, 18, 27, 45, 37, 38, 39, 40, 41, 42, 43, 48, 50);

        for (int i = 0; i < getSlots(); i++)
            inventory.setItem(i, makeItem(bluePlaceholder.contains(i) ? Material.BLUE_STAINED_GLASS_PANE :
                    Material.BLACK_STAINED_GLASS_PANE, " "));

        inventory.setItem(22, makeItem(Material.CHEST, "§eKonto eröffnen",
                Collections.singletonList("§7Kosten: §a§o" + BankSystem.getEconomy().format(200))));
        inventory.setItem(49, makeItem(Material.BARRIER, "§cSchließen"));
    }

    @Override
    public void handleClickActions(InventoryClickEvent inventoryClickEvent)
    {
        ItemStack item = inventoryClickEvent.getCurrentItem();

        switch (item.getType())
        {
            case CHEST:
                if (FinanceManager.getInstance().getCash(player) >= 200)
                    PinHandler.getInstance().createBankAccountWithPin(player);
                else
                    player.sendMessage("§cSo viel Geld hast du nicht!");

                player.closeInventory();
                break;
            case BARRIER:
                player.closeInventory();
                break;
        }
    }
}
