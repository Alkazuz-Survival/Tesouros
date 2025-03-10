package br.alkazuz.tesouros.items;

import br.alkazuz.tesouros.config.manager.ConfigManager;
import br.alkazuz.tesouros.util.ItemBuilder;
import br.alkazuz.tesouros.util.Serializer;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;

import java.util.Map;

public class TesouroItem {
    public Integer id;
    private float chance;
    private ItemStack itemStack;
    private int level;
    private String concurrence;

    public TesouroItem(Integer id, float chance, ItemStack itemStack, int level) {
        this.chance = chance;
        this.itemStack = itemStack;
        this.level = level;
        this.id = id;
    }

    public TesouroItem(Integer id, float chance, ItemStack itemStack, int level, String concurrence) {
        this.chance = chance;
        this.itemStack = itemStack;
        this.level = level;
        this.id = id;
        this.concurrence = concurrence;
    }

    public String getConcurrence() {
        return this.concurrence;
    }

    public void setConcurrence(String concurrence) {
        this.concurrence = concurrence;
    }

    public Integer getId() {
        if (this.id == null) {
            this.id = TesouroItemManager.getNextId(this.level);
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
            if (this.itemStack.hasItemMeta() && this.itemStack.getItemMeta().hasLore()) {
                if (this.itemStack.getItemMeta().getLore().stream()
                        .anyMatch(s -> s.contains("§7Chance: §f") && s.contains("%"))) {
                    this.itemStack = new ItemBuilder(this.itemStack).clearLore().build();
                }
            }
            FileConfiguration config = ConfigManager.getConfig("itens");
            Integer nId = this.getId();
            String path = String.valueOf(this.level) + "." + nId + ".";
            config.set(path + "chance", this.chance);
            config.set(path + "item", Serializer.serializeItemStack(this.itemStack));
            config.set(path + "level", this.level);
            config.set(path + "material", this.itemStack.getType().name());
            config.set(path + "data", this.itemStack.getData().getData());
            if (itemStack.getEnchantments() != null && !itemStack.getEnchantments().isEmpty()) {
                for (Map.Entry<Enchantment, Integer> entry : itemStack.getEnchantments().entrySet()) {
                    config.set(path + "enchantments." + entry.getKey().getName(), entry.getValue());
                }
            }
            if (concurrence != null && !this.concurrence.isEmpty()) {
                config.set(path + "concurrence", this.concurrence);
            }
            this.id = nId;
            ConfigManager.saveConfig(config, "itens");

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof TesouroItem) {
            TesouroItem tesouroItem = (TesouroItem) obj;
            if (tesouroItem.id != null && this.id != null && tesouroItem.id.equals(this.id)) {
                return true;
            }
        }
        return false;
    }

}
