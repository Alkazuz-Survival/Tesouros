package br.alkazuz.tesouros.items;

import br.alkazuz.tesouros.commands.TesourosCommand;
import br.alkazuz.tesouros.config.manager.ConfigManager;
import br.alkazuz.tesouros.util.Serializer;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class TesouroItemManager {

    private static final HashMap<Integer, List<TesouroItem>> tesouros = new HashMap<>();

    public static void load() {
        FileConfiguration file = ConfigManager.getConfig("itens");

        for (int i = 1; i <= 12; i++) {
            tesouros.put(i, new ArrayList<>());
            for (String key : file.getConfigurationSection(String.valueOf(i)).getKeys(false)) {
                int id = Integer.parseInt(key);
                float chance = (float) file.getDouble(i + "." + key + ".chance");
                int level = file.getInt(i + "." + key + ".level");
                tesouros.get(i).add(new TesouroItem(id,
                        chance,
                        Serializer.deserializeItemStack(file.getString(i + "." + key + ".item")),
                        level));
            }
        }

    }

    public static List<TesouroItem> getTesouroItems(int level) {
        return tesouros.get(level);
    }

    public static TesouroItem getTesouroItemById(int level, int id) {
        for (TesouroItem tesouroItem : tesouros.get(level)) {
            if (tesouroItem.getId() == id) {
                return tesouroItem;
            }
        }
        return null;
    }

}
