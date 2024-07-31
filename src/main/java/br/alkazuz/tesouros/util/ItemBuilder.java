package br.alkazuz.tesouros.util;

import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.LeatherArmorMeta;
import org.bukkit.inventory.meta.SkullMeta;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;

public class ItemBuilder {

    private ItemStack is;

    public ItemBuilder(Material m) {
        this(m, 1);
    }

    public ItemBuilder(ItemStack is) {
        this.is = is;
    }

    public ItemBuilder(Material m, int quantia) {
        is = new ItemStack(m, quantia);
    }

    public ItemBuilder(Material m, int quantia, byte durabilidade) {
        is = new ItemStack(m, quantia, durabilidade);
    }

    public ItemBuilder(Material m, int quantia, int durabilidade) {
        is = new ItemStack(m, quantia, (short) durabilidade);
    }

    public ItemBuilder clone() {
        return new ItemBuilder(is);
    }

    public ItemBuilder setAmount(int amount) {
        is.setAmount(amount);
        ItemMeta im = is.getItemMeta();
        is.setItemMeta(im);
        return this;
    }

    public ItemBuilder setName(String nome) {
        ItemMeta im = is.getItemMeta();
        im.setDisplayName(nome);
        is.setItemMeta(im);
        return this;
    }

    public ItemBuilder name(String nome) {
        ItemMeta im = is.getItemMeta();
        im.setDisplayName(nome);
        is.setItemMeta(im);
        return this;
    }

    public ItemBuilder addLoreLine(String s) {
        ItemMeta itemMeta = this.is.getItemMeta();
        ArrayList<String> lore = new ArrayList<String>();
        if (itemMeta.hasLore()) {
            lore = new ArrayList<String>(itemMeta.getLore());
        }
        lore.add(s);
        itemMeta.setLore(lore);
        this.is.setItemMeta(itemMeta);
        return this;
    }

    public ItemBuilder addUnsafeEnchantment(Enchantment ench, int level) {
        is.addUnsafeEnchantment(ench, level);
        return this;
    }

    public ItemBuilder setSkullOwner(String dono) {
        try {
            SkullMeta im = (SkullMeta) is.getItemMeta();
            im.setOwner(dono);
            is.setItemMeta(im);
        } catch (ClassCastException expected) {
        }
        return this;
    }

    public ItemBuilder setLore(String... lore) {
        ItemMeta im = is.getItemMeta();
        im.setLore(Arrays.asList(lore));
        is.setItemMeta(im);
        return this;
    }

    public ItemBuilder setLore(List<String> lore) {
        ItemMeta im = is.getItemMeta();
        im.setLore(lore);
        is.setItemMeta(im);
        return this;
    }

    public ItemBuilder listLore(List<String> lista, String... lore) {
        ItemMeta im = is.getItemMeta();
        im.setLore(Arrays.asList(lore));
        im.getLore().addAll(lista);
        is.setItemMeta(im);
        return this;
    }

    public ItemBuilder listLore(String... lore) {
        ItemMeta im = is.getItemMeta();
        im.setLore(Arrays.asList(lore));
        is.setItemMeta(im);
        return this;
    }

    public ItemBuilder listLore(List<String> lore) {
        ItemMeta im = is.getItemMeta();
        im.setLore(lore);
        is.setItemMeta(im);
        return this;
    }

    public ItemBuilder setLore(String string1, String string2, String string3, String string4, String string5, String string6, String string7, String string8, String string9, List<String> lista1, String string10, String string11, String string12, String string13, String string14, List<String> lista2) {
        ItemMeta im = is.getItemMeta();
        List<String> l = new ArrayList<>();
        l.add(string1);
        l.add(string2);
        l.add(string3);
        l.add(string4);
        l.add(string5);
        l.add(string6);
        l.add(string7);
        l.add(string8);
        l.add(string9);
        l.addAll(lista1);
        l.add(string10);
        l.add(string11);
        l.add(string12);
        l.add(string13);
        l.add(string14);
        l.addAll(lista2);
        im.setLore(l);
        is.setItemMeta(im);
        return this;
    }

    public ItemBuilder setLeatherArmorColor(Color cor) {
        try {
            LeatherArmorMeta im = (LeatherArmorMeta) is.getItemMeta();
            im.setColor(cor);
            is.setItemMeta(im);
        } catch (ClassCastException expected) {
        }
        return this;
    }

    @SuppressWarnings("deprecation")
    public ItemBuilder setDyeColor(DyeColor dyeColor) {
        this.is.setDurability((short) dyeColor.getData());
        return this;
    }

    public ItemStack toItemStack() {
        return is;
    }

    public static boolean RefSet(final Class<?> sourceClass, final Object instance, final String fieldName, final Object value) {
        try {
            final Field field = sourceClass.getDeclaredField(fieldName);
            final Field modifiersField = Field.class.getDeclaredField("modifiers");
            final int modifiers = modifiersField.getModifiers();
            if (!field.isAccessible()) {
                field.setAccessible(true);
            }
            if ((modifiers & 0x10) == 0x10) {
                modifiersField.setAccessible(true);
                modifiersField.setInt(field, modifiers & 0xFFFFFFEF);
            }
            try {
                field.set(instance, value);
            } finally {
                if ((modifiers & 0x10) == 0x10) {
                    modifiersField.setInt(field, modifiers | 0x10);
                }
                if (!field.isAccessible()) {
                    field.setAccessible(false);
                }
            }
            if ((modifiers & 0x10) == 0x10) {
                modifiersField.setInt(field, modifiers | 0x10);
            }
            if (!field.isAccessible()) {
                field.setAccessible(false);
            }
            if ((modifiers & 0x10) == 0x10) {
                modifiersField.setInt(field, modifiers | 0x10);
            }
            if (!field.isAccessible()) {
                field.setAccessible(false);
            }
            return true;
        } catch (Exception var11) {
            Bukkit.getLogger().log(Level.WARNING, "Unable to inject Gameprofile", var11);
            return false;
        }
    }


    public ItemStack build() {
        return this.is;
    }

    public ItemBuilder durability(final int dur) {
        is.setDurability((short) dur);
        return this;
    }

    public ItemBuilder owner(final String owner) {
        try {
            final SkullMeta im = (SkullMeta) this.is.getItemMeta();
            im.setOwner(owner);
            this.is.setItemMeta((ItemMeta) im);
        } catch (ClassCastException ex) {
        }
        return this;
    }

    public void clearLore() {
        final ItemMeta meta = this.is.getItemMeta();
        meta.setLore(new ArrayList<String>());
        this.is.setItemMeta(meta);
    }
}
