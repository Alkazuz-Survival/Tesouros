package br.alkazuz.tesouros.gui;

import br.alkazuz.tesouros.engines.ArenaTesouro;
import br.alkazuz.tesouros.engines.ArenasTesouroManager;
import br.alkazuz.tesouros.engines.TesouroOpening;
import br.alkazuz.tesouros.engines.TesouroOpeningManager;
import br.alkazuz.tesouros.util.GuiHolder;
import br.alkazuz.tesouros.util.ItemBuilder;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;

public class MenuConfirmOpen implements Listener {

    public static void open(Player player, int level, ItemStack item) {
        HashMap<String, Object> properties = new HashMap<>();
        properties.put("level", level);
        properties.put("item", item);
        Inventory inventory = player.getServer().createInventory(new GuiHolder(1, properties), 45, "Confirmar abertura");
        ItemStack clone = item.clone();
        clone.setAmount(1);
        inventory.setItem(13, clone);

        inventory.setItem(29, new ItemBuilder(Material.WOOL, 1, (short) 5).setName("§aConfirmar").toItemStack());

        inventory.setItem(33, new ItemBuilder(Material.WOOL, 1, (short) 14).setName("§cCancelar").toItemStack());

        player.openInventory(inventory);
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if (!(event.getInventory().getHolder() instanceof GuiHolder)) {
            return;
        }

        GuiHolder holder = (GuiHolder) event.getInventory().getHolder();
        if (holder.getId() != 1) {
            return;
        }

        event.setCancelled(true);

        Player player = (Player) event.getWhoClicked();

        if (event.getSlot() == 29) {
            int level = (int) holder.getProperty("level");
            ItemStack item = (ItemStack) holder.getProperty("item");
            ItemStack itemInHand = event.getWhoClicked().getItemInHand();
            if (itemInHand == null || !itemInHand.isSimilar(item)) {
                player.sendMessage("§cVocê não está segurando o item correto.");
                player.closeInventory();
                return;
            }

            ArenaTesouro arenaTesouro = ArenasTesouroManager.getInstance().getFreeArena();

            if (arenaTesouro == null) {
                player.sendMessage("§cNão há arenas disponíveis no momento.");
                player.closeInventory();
                return;
            }
            removeItem(itemInHand);
            TesouroOpening tesouroOpening = new TesouroOpening(player, arenaTesouro, level);
            TesouroOpeningManager.getInstance().addTesouro(tesouroOpening);
            tesouroOpening.start();

        } else if (event.getSlot() == 33) {
            event.getWhoClicked().closeInventory();
        }
    }

    private void removeItem(ItemStack item) {
        if (item.getAmount() == 1) {
            item.setType(Material.AIR);
        } else {
            item.setAmount(item.getAmount() - 1);
        }
    }

}
