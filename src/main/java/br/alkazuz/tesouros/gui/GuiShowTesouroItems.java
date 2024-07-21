package br.alkazuz.tesouros.gui;

import br.alkazuz.tesouros.Tesouros;
import br.alkazuz.tesouros.config.manager.ConfigManager;
import br.alkazuz.tesouros.items.TesouroItem;
import br.alkazuz.tesouros.items.TesouroItemManager;
import br.alkazuz.tesouros.util.GuiHolder;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class GuiShowTesouroItems implements Listener {

    public static void open(Player player, int level) {
        HashMap<String, Object> properties = new HashMap<>();
        properties.put("level", level);
        Inventory inventory = player.getServer().createInventory(
                new GuiHolder(99, properties), 54, "Itens do level " + level);

        int slot = 0;

        for (TesouroItem tesouroItem : TesouroItemManager.getTesouroItems(level)) {
            if (slot >= 54) {
                break;
            }
            ItemStack item = tesouroItem.getDisplayItem().clone();
            inventory.setItem(slot, item);
            slot++;
        }

        player.openInventory(inventory);
    }

    @EventHandler
    public void onRightClickInInvententorySlot(InventoryClickEvent event) {
        if (!(event.getWhoClicked() instanceof Player)) {
            return;
        }

        if (!(event.getInventory().getHolder() instanceof GuiHolder)) {
            return;
        }

        GuiHolder holder = (GuiHolder) event.getInventory().getHolder();

        if (holder.getId() != 99) {
            return;
        }

        event.setCancelled(true);
    }

}
