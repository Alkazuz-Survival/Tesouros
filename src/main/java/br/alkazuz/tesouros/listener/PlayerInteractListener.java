package br.alkazuz.tesouros.listener;

import br.alkazuz.sunshine.anticheat.data.AntiCheatDataManager;
import br.alkazuz.tesouros.Tesouros;
import br.alkazuz.tesouros.gui.MenuConfirmOpen;
import br.alkazuz.tesouros.hooks.SunshineHook;
import br.alkazuz.tesouros.itens.TesouroItems;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntitySpawnEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

public class PlayerInteractListener implements Listener {

    @EventHandler
    public void onEntityItensSpawn(EntitySpawnEvent event) {
        if (!event.getLocation().getWorld().getName().equals("tesouros")) {
            return;
        }

        Entity entity = event.getEntity();

        if (entity instanceof LivingEntity) {
            return;
        }

        Bukkit.getScheduler().runTaskLater(Tesouros.getInstance(), () -> {
            event.getEntity().remove();
        }, 20L * 8L);
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        if (event.getAction() != Action.RIGHT_CLICK_AIR && event.getAction() != Action.RIGHT_CLICK_BLOCK) {
            return;
        }

        ItemStack item = event.getItem();

        if (item == null) {
            return;
        }

        if (item.getType() != Material.BOOK) {
            return;
        }

        if (!item.hasItemMeta()) {
            return;
        }

        if (!item.getItemMeta().hasDisplayName()) {
            return;
        }


        for (int i = 1; i <= 12; i++) {
            if (item.isSimilar(TesouroItems.getTesouro(i))) {
                event.setCancelled(true);
                if (SunshineHook.enabled) {
                    if (!AntiCheatDataManager.isValidAntiCheatData(event.getPlayer().getName())) {
                        event.getPlayer().sendMessage("§cVocê só pode abrir tesouros com o antihack instalado");
                        return;
                    }
                }
                MenuConfirmOpen.open(event.getPlayer(), i, item);
                return;
            }
        }
    }

}
