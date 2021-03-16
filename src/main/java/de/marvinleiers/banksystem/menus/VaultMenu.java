package de.marvinleiers.banksystem.menus;

import de.marvinleiers.banksystem.BankSystem;
import de.marvinleiers.banksystem.finance.FinanceManager;
import de.marvinleiers.banksystem.utils.Base64Util;
import de.marvinleiers.banksystem.utils.MySQL;
import de.marvinleiers.menuapi.Menu;
import de.marvinleiers.menuapi.MenuUserInformation;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.ItemStack;

public class VaultMenu extends Menu
{
    public VaultMenu(MenuUserInformation menuUserInformation)
    {
        super(true, menuUserInformation);
    }

    @Override
    public String getTitle()
    {
        return "§7Vault von: §9" + player.getName();
    }

    @Override
    public int getSlots()
    {
        return 27;
    }

    @Override
    public void setItems()
    {
        ItemStack[] contents = FinanceManager.getInstance().getVaultContents(player);

        if (contents == null) return;

        for (int i = 0; i < getSlots(); i++)
        {
            if (contents[i] == null) continue;

            inventory.setItem(i, contents[i]);
        }
    }

    @Override
    public void handleClickActions(InventoryClickEvent inventoryClickEvent)
    {

    }

    @Override
    public void onClose(InventoryCloseEvent event)
    {
        MySQL mysql = BankSystem.getMySQL();

        mysql.update("DELETE FROM vaults WHERE uuid = '" + player.getUniqueId().toString() + "';");

        String content = Base64Util.saveItems(inventory.getContents());

        mysql.update("INSERT INTO vaults (uuid, content) VALUES ('" + player.getUniqueId().toString() + "', '" + content + "');");
    }
}
