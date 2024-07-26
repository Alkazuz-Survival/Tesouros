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

public class GuiEditTesouroItems implements Listener {

    public static void open(Player player, int level, int targetLevel) {
        HashMap<String, Object> properties = new HashMap<>();
        properties.put("level", level);
        Inventory inventory = player.getServer().createInventory(
                new GuiHolder(2, properties), 54, "Itens do level " + level);

        int slot = 0;

        for (TesouroItem tesouroItem : TesouroItemManager.getTesouroItems(targetLevel)) {
            if (slot >= 54) {
                break;
            }
            ItemStack item;
            if (level != targetLevel) {
                item = tesouroItem.getItemStack().clone();
            } else {
                item = tesouroItem.getDisplayItem().clone();
            }
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

        Player player = (Player) event.getWhoClicked();

        if (!(event.getInventory().getHolder() instanceof GuiHolder)) {
            return;
        }

        GuiHolder holder = (GuiHolder) event.getInventory().getHolder();

        if (holder.getId() != 2) {
            return;
        }

        ItemStack item = event.getCurrentItem();

        if (item == null || item.getType() == Material.AIR) {
            return;
        }

        if (!event.isRightClick()) return;
        event.setCancelled(true);

        int level = (int) holder.getProperty("level");

        String lastLoreLine = ChatColor.stripColor(item.getItemMeta().getLore().get(item.getItemMeta().getLore().size() - 1));
        int id = Integer.parseInt(lastLoreLine);

        player.sendMessage("§eDigite a nova chance do item:");
        player.closeInventory();

        Tesouros.eventWaiter.waitForEvent(
                AsyncPlayerChatEvent.class,
                EventPriority.LOW,
                e -> e.getPlayer().equals(player) && isFloat(e.getMessage()),
                e -> {
                    float chance = Float.parseFloat(e.getMessage());

                    TesouroItem tesouroItem = TesouroItemManager.getTesouroItemById(level, id);

                    if (tesouroItem == null) {
                        player.sendMessage("§cItem não encontrado.");
                        return;
                    }

                    tesouroItem.setChance(chance);
                    tesouroItem.save();
                    event.setCancelled(true);
                    player.sendMessage("§aChance do item alterada com sucesso.");
                    open(player, level, level);


                },
                10 * 20,
                () -> player.sendMessage("§cTempo esgotado.")
        );
    }

    private boolean isFloat(String s) {
        try {
            Float.parseFloat(s);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    @EventHandler
    public void onInventoryClose(InventoryCloseEvent event) {
        if (!(event.getPlayer() instanceof Player)) {
            return;
        }

        Player player = (Player) event.getPlayer();
        if (!(event.getInventory().getHolder() instanceof GuiHolder)) {
            return;
        }

        GuiHolder holder = (GuiHolder) event.getInventory().getHolder();

        if (holder.getId() != 2) return;

        int level = (int) holder.getProperty("level");

        FileConfiguration itens = ConfigManager.getConfig("itens");

        try {
            itens.set(String.valueOf(level), null);
            ConfigManager.saveConfig(itens, "itens");
        } catch (Exception e) {
            e.printStackTrace();
        }

        List<TesouroItem> tesouroItems = new ArrayList<>();

        for (int i = 0; i < 54; i++) {
            ItemStack item = event.getInventory().getItem(i);
            if (item == null || item.getType() == Material.AIR) {
                continue;
            }

            TesouroItem tesouroItem = new TesouroItem(null, 0, item, level);

            float chance = 0.0f;
            Integer id = null;

            if (item.hasItemMeta()) {
                if (item.getItemMeta().hasLore()) {
                    for (String lore : item.getItemMeta().getLore()) {
                        if (lore.contains("Chance:")) {
                            String[] split = lore.split(" ");
                            chance = Float.parseFloat(ChatColor.stripColor(split[1].replace("%", "")));
                        }

                        if (lore.startsWith("§8")) {
                            id = Integer.parseInt(lore.replace("§8", ""));
                        }
                    }
                }
            }

            if (id != null && TesouroItemManager.getTesouroItemById(level, id) != null)
                tesouroItem = TesouroItemManager.getTesouroItemById(level, id);


            if (id == null) {
                tesouroItem.setItemStack(item);
                tesouroItem.setChance(chance);
                tesouroItem.setId(i + 1);
            }

            tesouroItem.save();

            tesouroItems.add(tesouroItem);

        }

        TesouroItemManager.setItens(level, tesouroItems);

        player.sendMessage("§aItens salvos com sucesso!");
    }

    private boolean isInt(String s) {
        try {
            Integer.parseInt(s);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

}
