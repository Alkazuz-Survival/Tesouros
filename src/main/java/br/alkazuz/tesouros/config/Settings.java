package br.alkazuz.tesouros.config;

import br.alkazuz.tesouros.config.manager.ConfigManager;
import br.alkazuz.tesouros.util.Serializer;
import com.gmail.nossr50.datatypes.skills.SkillType;
import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.HashMap;
import java.util.TreeMap;

public class Settings {

    public static HashMap<Integer, String> TESOUROS_TITLE = new HashMap<>();
    public static HashMap<Integer, TreeMap<Integer, Float>> TESOUROS_PROBABILITY = new HashMap<>();
    public static HashMap<Integer, TreeMap<String, Integer>> TESOUROS_MOBS_LEVELS = new HashMap<>();
    public static HashMap<SkillType, TreeMap<Integer, Float>> SKILLS_MODIFIERS = new HashMap<>();
    public static HashMap<Integer, Float> TESOUROS_VIP = new HashMap<>();
    public static Location spawnLocation;

    public static void load() {
        try {
            FileConfiguration config = ConfigManager
                    .getConfig("settings");
            TESOUROS_PROBABILITY.clear();
            TESOUROS_TITLE.clear();
            TESOUROS_MOBS_LEVELS.clear();
            SKILLS_MODIFIERS.clear();
            TESOUROS_VIP.clear();

            if (config.contains("tesouro-vip")) {
                for (String level : config.getConfigurationSection("tesouro-vip").getKeys(false)) {
                    TESOUROS_VIP.put(Integer.parseInt(level), (float) config.getDouble("tesouro-vip." + level));
                }
            }

            if (config.contains("modifier-skills")) {
                for (String skill : config.getConfigurationSection("modifier-skills").getKeys(false)) {
                    SkillType skillType = SkillType.valueOf(skill.toUpperCase());
                    TreeMap<Integer, Float> probabilityMap = new TreeMap<>();
                    for (String levelStr : config.getConfigurationSection("modifier-skills." + skill).getKeys(false)) {
                        int level = Integer.parseInt(levelStr);
                        float value = (float) config.getDouble("modifier-skills." + skill + "." + levelStr);
                        probabilityMap.put(level, value);
                    }
                    SKILLS_MODIFIERS.put(skillType, probabilityMap);
                }
            }

            for (String key : config.getConfigurationSection("books-title").getKeys(false)) {
                TESOUROS_TITLE.put(Integer.parseInt(key), config.getString("books-title." + key).replace("&", "§"));
            }

            if (config.get("spawn-location") != null || config.contains("spawn-location")) {
                spawnLocation = Serializer.getLocation(config.getString("spawn-location"));
                System.out.println("Spawn location loaded: " + spawnLocation);
            } else {
                System.out.println("Spawn location not found");
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
