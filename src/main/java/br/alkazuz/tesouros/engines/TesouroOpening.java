package br.alkazuz.tesouros.engines;

import br.alkazuz.tesouros.Tesouros;
import br.alkazuz.tesouros.config.Settings;
import br.alkazuz.tesouros.entities.CustomEntities;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntitySpawnEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerTeleportEvent;

import java.util.Map;
import java.util.TreeMap;
import java.util.UUID;

public class TesouroOpening implements Listener {
    private final Player player;
    private final UUID uuid = UUID.randomUUID();
    private final ArenaTesouro arenaTesouro;
    private int currentLevel;
    private final int maxLevel;
    private long interactLong;
    private long mobSpawnLong;

    public TesouroOpening(Player player, ArenaTesouro arenaTesouro, int maxLevel) {
        this.player = player;
        this.arenaTesouro = arenaTesouro;
        this.maxLevel = maxLevel;
        this.currentLevel = 0;
        Bukkit.getPluginManager().registerEvents(this, Tesouros.getInstance());
    }

    public UUID getUuid() {
        return uuid;
    }

    public ArenaTesouro getArenaTesouro() {
        return arenaTesouro;
    }

    public int getCurrentLevel() {
        return currentLevel;
    }

    public void setCurrentLevel(int currentLevel) {
        this.currentLevel = currentLevel;
    }

    public int getMaxLevel() {
        return maxLevel;
    }

    public void nextLevel() {
        currentLevel++;
    }

    public boolean hasNextLevel() {
        return currentLevel < maxLevel;
    }

    public void finish() {
        HandlerList.unregisterAll(this);
        TesouroOpeningManager.getInstance().removeTesouro(this);
    }

    public void spawnMobs() {
        TreeMap<String, Integer> mobsCountMap = Settings.TESOUROS_MOBS_LEVELS.get(currentLevel);
        if (mobsCountMap == null) {
            return;
        }
        mobSpawnLong = System.currentTimeMillis();
        for (Map.Entry<String, Integer> entry : mobsCountMap.entrySet()) {
            String mobName = entry.getKey();
            int mobCount = entry.getValue();
            switch (mobName) {
                case "witches":
                    for (int i = 0; i < mobCount; i++) {
                        Location location = arenaTesouro.getRandomSpawnableLocation();
                        CustomEntities.spawnWitch(location, uuid);
                    }
                    break;
                case "blazes":
                    for (int i = 0; i < mobCount; i++) {
                        Location location = arenaTesouro.getRandomSpawnableLocation();
                        CustomEntities.spawnBlaze(location, uuid);
                    }
                    break;
                case "skeletons":
                    for (int i = 0; i < mobCount; i++) {
                        Location location = arenaTesouro.getRandomSpawnableLocation();
                        CustomEntities.spawnSkeleton(location, uuid);
                    }
                    break;
                case "zombies":
                    for (int i = 0; i < mobCount; i++) {
                        Location location = arenaTesouro.getRandomSpawnableLocation();
                        CustomEntities.spawnZombie(location, uuid);
                    }
                    break;
                case "slimes":
                    for (int i = 0; i < mobCount; i++) {
                        Location location = arenaTesouro.getRandomSpawnableLocation();
                        CustomEntities.spawnSlime(location, uuid);
                    }
                    break;
                case "pigzombies":
                    for (int i = 0; i < mobCount; i++) {
                        Location location = arenaTesouro.getRandomSpawnableLocation();
                        CustomEntities.spawnZombiePigman(location, uuid);
                    }
                    break;
            }
        }
    }

    public boolean hasMobsAlive() {
        for (LivingEntity entity : arenaTesouro.getLocation().getWorld().getLivingEntities()) {
            if (entity instanceof Player || entity.isDead()) {
                continue;
            }

            if (!entity.hasMetadata("tesouroid")) {
                continue;
            }

            if (entity.getMetadata("tesouroid").get(0).asString().equals(uuid.toString())) {
                return true;
            }
        }

        return false;
    }

    public void start() {
        player.teleport(arenaTesouro.getLocation(), PlayerTeleportEvent.TeleportCause.PLUGIN);
        Bukkit.broadcastMessage("§3[Tesouros] §a" + player.getDisplayName() + " §7está abrindo um tesouro nível §a" + currentLevel + "§7.");
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onInteractRightClickInChest(PlayerInteractEvent event) {
        if (event.getAction() != Action.RIGHT_CLICK_BLOCK) {
            return;
        }

        if (event.getClickedBlock().getType() != Material.CHEST) {
            return;
        }

        if (event.getClickedBlock().getLocation().equals(arenaTesouro.getChest().getLocation())) {
            event.setCancelled(true);
            Block block = event.getClickedBlock();
            if (System.currentTimeMillis() - interactLong < 1000) {
                return;
            }
            interactLong = System.currentTimeMillis();
            if (hasMobsAlive()) {
                player.sendMessage("§cVocê não pode abrir o tesouro enquanto houverem mobs vivos.");
                return;
            }
            if (hasNextLevel()) {
                nextLevel();
                spawnMobs();
                block.getLocation().getWorld().strikeLightningEffect(block.getLocation());
                player.sendMessage("");
                player.sendMessage("§3[Tesouros] §eOs monstros vieram defender o tesouro nível §a" + currentLevel + "§e. Cuidado!");
                player.sendMessage("");
                player.sendMessage("§aVocê tem §l10 minutos §apara derrubar todos os monstros.");
            } else {
                player.sendMessage("§aVocê abriu todos os níveis do tesouro.");
            }
        }
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onSpawn(EntitySpawnEvent event) {
        Entity entity = event.getEntity();
        if (entity instanceof LivingEntity) {
            Bukkit.getScheduler().runTaskLater(Tesouros.getInstance(), () -> {
                LivingEntity livingEntity = (LivingEntity) entity;
                if (!livingEntity.hasMetadata("tesouroid")) {
                    return;
                }
                livingEntity.setMaxHealth(livingEntity.getMaxHealth() * 3);
                livingEntity.setHealth(livingEntity.getMaxHealth());
            }, 1L);
        }
    }
}
