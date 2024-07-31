package br.alkazuz.tesouros.engines;

import br.alkazuz.correio.object.CorreioItem;
import br.alkazuz.correio.object.CorreioItemManager;
import br.alkazuz.tesouros.Tesouros;
import br.alkazuz.tesouros.config.Settings;
import br.alkazuz.tesouros.entities.CustomEntities;
import br.alkazuz.tesouros.event.TesouroFinishEvent;
import br.alkazuz.tesouros.items.TesouroItem;
import br.alkazuz.tesouros.itens.TesouroItems;
import br.alkazuz.tesouros.util.TesouroItemGenerator;
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
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntitySpawnEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.*;

public class TesouroOpening implements Listener {
    private final Player player;
    private final UUID uuid = UUID.randomUUID();
    private final ArenaTesouro arenaTesouro;
    private int currentLevel;
    private final int maxLevel;
    private long interactLong;
    private Integer taskTimeout;

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
        player.sendMessage("§aVocê tem §l10 minutos §apara derrubar todos os monstros.");

        if (taskTimeout != null) Bukkit.getScheduler().cancelTask(taskTimeout);

        this.taskTimeout = Bukkit.getScheduler().scheduleSyncDelayedTask(Tesouros.getInstance(), () -> {
            player.sendMessage("§cVocê não conseguiu derrubar todos os monstros a tempo.");
            finish();
        }, 20 * 60 * 10);
    }

    public boolean hasNextLevel() {
        return currentLevel < maxLevel;
    }

    public void finish() {
        HandlerList.unregisterAll(this);
        TesouroOpeningManager.getInstance().removeTesouro(this);
        for (Entity entity : getEntities()) {
            entity.remove();
        }
        if (player.isOnline() && Settings.spawnLocation != null)
            player.teleport(Settings.spawnLocation, PlayerTeleportEvent.TeleportCause.PLUGIN);
    }

    public void handleWin() {
        if (taskTimeout != null) Bukkit.getScheduler().cancelTask(taskTimeout);
        List<TesouroItem> tesouroItems = TesouroItemGenerator.generateTesouroItems(maxLevel);
        for (TesouroItem tesouroItem : tesouroItems) {
            CorreioItem correioItem = new CorreioItem(tesouroItem.getItemStack(), "CoreMC", player.getName());
            CorreioItemManager.addCorreioItem(player.getName(), correioItem);
            correioItem.save();
        }

        player.sendMessage("§aVocê venceu o tesouro e recebeu os itens no correio.");
        finish();

        TesouroFinishEvent tesouroFinishEvent = new TesouroFinishEvent(player, this);
        Bukkit.getPluginManager().callEvent(tesouroFinishEvent);
    }

    public void spawnMobs() {
        TreeMap<String, Integer> mobsCountMap = Settings.TESOUROS_MOBS_LEVELS.get(currentLevel);
        if (mobsCountMap == null) {
            return;
        }
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

    private List<LivingEntity> getEntities() {
        List<LivingEntity> list = new ArrayList<>();

        for (LivingEntity entity : arenaTesouro.getLocation().getWorld().getLivingEntities()) {
            if (entity instanceof Player || entity.isDead()) {
                continue;
            }

            if (!entity.hasMetadata("tesouroid")) {
                continue;
            }

            if (entity.getMetadata("tesouroid").get(0).asString().equals(uuid.toString())) {
                list.add(entity);
            }
        }
        return list;
    }

    public boolean hasMobsAlive() {
        return !getEntities().isEmpty();
    }

    public void start() {
        player.teleport(arenaTesouro.getLocation(), PlayerTeleportEvent.TeleportCause.PLUGIN);
        Bukkit.broadcastMessage("§3[Tesouros] §a" + player.getDisplayName() + " §7está abrindo um Tesouro Nível §a" + maxLevel + "§7.");
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        if (event.getPlayer() != player) return;
        CorreioItem correioItem = new CorreioItem(TesouroItems.getTesouro(maxLevel), "CoreMC", player.getName());
        CorreioItemManager.addCorreioItem(player.getName(), correioItem);
        correioItem.save();
        finish();
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

            } else {
                handleWin();
                player.sendMessage("§aVocê abriu todos os níveis do tesouro.");
            }
        }
    }

    @EventHandler
    public void onDamage(EntityDamageEvent event) {
        Entity entity = event.getEntity();
        if (entity instanceof LivingEntity) {
            LivingEntity livingEntity = (LivingEntity) entity;
            if (!livingEntity.hasMetadata("tesouroid")) {
                return;
            }
            if (event.getCause() != EntityDamageEvent.DamageCause.ENTITY_ATTACK) {
                event.setCancelled(true);
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
                double amplifer = currentLevel * 0.4;
                if (amplifer < 2.5) {
                    amplifer = 2.5;
                }
                livingEntity.setMaxHealth((int) (livingEntity.getMaxHealth() * amplifer));
                livingEntity.setHealth(livingEntity.getMaxHealth());
                livingEntity.addPotionEffect(
                        new PotionEffect(PotionEffectType.INCREASE_DAMAGE, 999999, 1));
            }, 1L);
        }
    }
}
