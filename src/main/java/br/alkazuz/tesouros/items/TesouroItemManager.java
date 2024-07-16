package br.alkazuz.tesouros.items;

import br.alkazuz.tesouros.commands.TesourosCommand;
import br.alkazuz.tesouros.config.manager.ConfigManager;
import br.alkazuz.tesouros.util.Serializer;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.inventory.ItemStack;

import java.util.*;

public class TesouroItemManager {

    private static final HashMap<Integer, List<TesouroItem>> tesouros = new HashMap<>();

    public static void load() {
        ConfigManager.createConfig("itens");
        FileConfiguration file = ConfigManager.getConfig("itens");

        for (int i = 1; i <= 12; i++) {
            tesouros.put(i, new ArrayList<>());
            if (!file.contains(String.valueOf(i))) {
                continue;
            }
            for (String key : file.getConfigurationSection(String.valueOf(i)).getKeys(false)) {
                int id = Integer.parseInt(key);
                float chance = (float) file.getDouble(i + "." + key + ".chance");
                int level = file.getInt(i + "." + key + ".level");
                String concurrence = null;
                if (file.contains(i + "." + key + ".concurrence")) {
                    concurrence = file.getString(i + "." + key + ".concurrence");
                }
                TesouroItem tesouroItem = new TesouroItem(id,
                        chance,
                        Serializer.deserializeItemStack(file.getString(i + "." + key + ".item")),
                        level,
                        concurrence);

                tesouros.get(i).add(tesouroItem);
            }
        }

    }

    public static void clearLevel(int level) {
        tesouros.get(level).clear();
    }

    public static void upsertTesouroItem(int level, TesouroItem tesouroItem) {
        Iterator<TesouroItem> iterator = tesouros.get(level).iterator();
        while (iterator.hasNext()) {
            TesouroItem item = iterator.next();
            if (item.id != null && tesouroItem.id != null && tesouroItem.equals(item)) {
                iterator.remove();
            }
        }

        tesouros.get(level).add(tesouroItem);
    }

    public static void setItens(int level, List<TesouroItem> tesouroItems) {
        tesouros.get(level).clear();
        tesouros.get(level).addAll(tesouroItems);
    }

    public static int getNextId(int level) {
        int id = 1;
        for (TesouroItem tesouroItem : tesouros.get(level)) {
            if (tesouroItem.getId() >= id) {
                id = tesouroItem.getId() + 1;
            }
        }
        return id;
    }

    public static TesouroItem getTesouroByItemStack(int level, ItemStack itemStack) {
        for (TesouroItem tesouroItem : tesouros.get(level)) {
            if (tesouroItem.getItemStack().isSimilar(itemStack)) {
                return tesouroItem;
            }
        }
        return null;
    }

    public static void addTesouroItem(int level, TesouroItem tesouroItem) {
        tesouros.get(level).add(tesouroItem);
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
