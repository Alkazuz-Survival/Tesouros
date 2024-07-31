package br.alkazuz.tesouros.config;

import br.alkazuz.tesouros.config.manager.ConfigManager;
import br.alkazuz.tesouros.engines.ArenaTesouro;
import br.alkazuz.tesouros.engines.ArenasTesouroManager;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;

public class ArenasSettings {


    public static void load() {
        try {
            FileConfiguration config = ConfigManager
                    .getConfig("arenas");
            if (!config.contains("arenas")) {
                return;
            }
            for (String key : config.getConfigurationSection("arenas").getKeys(false)) {
                String path = "arenas." + key + ".";
                String world = config.getString(path + "world");
                int x = config.getInt(path + "x");
                int y = config.getInt(path + "y");
                int z = config.getInt(path + "z");

                ArenasTesouroManager.getInstance().addArena(
                        new ArenaTesouro(new Location(Bukkit.getWorld(world), x, y, z)));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void save(ArenaTesouro arenaTesouro) {
        try {
            FileConfiguration config = ConfigManager
                    .getConfig("arenas");
            String path = "arenas." + ArenasTesouroManager.getInstance().getArenas().size() + ".";
            config.set(path + "world", arenaTesouro.getLocation().getWorld().getName());
            config.set(path + "x", arenaTesouro.getLocation().getBlockX());
            config.set(path + "y", arenaTesouro.getLocation().getBlockY());
            config.set(path + "z", arenaTesouro.getLocation().getBlockZ());
            ConfigManager.saveConfig(config, "arenas");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}
