package br.alkazuz.tesouros.items;

import br.alkazuz.tesouros.config.manager.ConfigManager;
import br.alkazuz.tesouros.config.manager.DataManager;
import br.alkazuz.tesouros.util.ItemBuilder;
import br.alkazuz.tesouros.util.Serializer;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.inventory.ItemStack;

public class TesouroItem {
    private Integer id;
    private float chance;
    private ItemStack itemStack;
    private int level;

    public TesouroItem(Integer id, float chance, ItemStack itemStack, int level) {
        this.chance = chance;
        this.itemStack = itemStack;
        this.level = level;
        this.id = id;
    }

    public Integer getId() {
        if (this.id == null) {
            FileConfiguration config = ConfigManager.getConfig("itens");
            String key = String.valueOf(level);
            config.getKeys(false).stream().filter(k -> k.equals(key)).forEach(k -> {
                this.id = Integer.parseInt(k);
            });
            ++this.id;
        }
        return this.id;
    }

    public float getChance() {
        return this.chance;
    }

    public ItemStack getItemStack() {
        return this.itemStack;
    }

    public int getLevel() {
        return this.level;
    }

    public void setChance(float chance) {
        this.chance = chance;
    }

    public void setItemStack(ItemStack itemStack) {
        this.itemStack = itemStack;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public ItemStack getDisplayItem() {
        ItemStack clone = this.itemStack.clone();
        return new ItemBuilder(clone).setLore("§7Chance: §f" + this.chance + "%",
                "§8" + this.id).build();
    }

    public void save() {
        try {
            FileConfiguration config = ConfigManager.getConfig("itens");
            String path = String.valueOf(this.level) + "." + this.getId() + ".";
            config.set(path + "chance", this.chance);
            config.set(path + "item", Serializer.serializeItemStack(this.itemStack));
            config.set(path + "level", this.level);
            ConfigManager.saveConfig(config, "itens");

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

}
