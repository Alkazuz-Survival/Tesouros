package br.alkazuz.tesouros.config;

import br.alkazuz.tesouros.config.manager.ConfigManager;
import br.alkazuz.tesouros.util.Serializer;
import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.HashMap;
import java.util.TreeMap;

public class Settings {

    public static HashMap<Integer, String> TESOUROS_TITLE = new HashMap<>();
    public static HashMap<Integer, TreeMap<Integer, Float>> TESOUROS_PROBABILITY = new HashMap<>();
    public static HashMap<Integer, TreeMap<String, Integer>> TESOUROS_MOBS_LEVELS = new HashMap<>();
    public static Location spawnLocation;

    public static void load() {
        try {
            FileConfiguration config = ConfigManager
                    .getConfig("settings");
            for (String key : config.getConfigurationSection("books-title").getKeys(false)) {
                TESOUROS_TITLE.put(Integer.parseInt(key), config.getString("books-title." + key).replace("&", "§"));
            }

            if (config.contains("spawn-location")) {
                spawnLocation = Serializer.getLocation(config.getString("spawn-location"));
            }

            for (String level : config.getConfigurationSection("books-probability").getKeys(false)) {
                String path = "books-probability." + level;
                TreeMap<Integer, Float> probabilityMap = new TreeMap<>();
                for (String range : config.getConfigurationSection(path).getKeys(false)) {
                    float value = (float) config.getDouble(path + "." + range);
                    if (range.contains("+")) {
                        int start = Integer.parseInt(range.replace("+", ""));
                        probabilityMap.put(start, value);
                    } else if (range.contains("-")) {
                        String[] parts = range.split("-");
                        int start = Integer.parseInt(parts[0]);
                        int end = Integer.parseInt(parts[1]);
                        for (int i = start; i <= end; i++) {
                            probabilityMap.put(i, value);
                        }
                    } else {
                        int index = Integer.parseInt(range);
                        probabilityMap.put(index, value);
                    }
                }
                TESOUROS_PROBABILITY.put(Integer.parseInt(level), probabilityMap);
            }

            for (String level : config.getConfigurationSection("stages").getKeys(false)) {
                String path = "stages." + level;
                TreeMap<String, Integer> mobsMap = new TreeMap<>();
                for (String mob : config.getConfigurationSection(path).getKeys(false)) {
                    int value = config.getInt(path + "." + mob);
                    mobsMap.put(mob, value);
                }
                TESOUROS_MOBS_LEVELS.put(Integer.parseInt(level), mobsMap);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}
