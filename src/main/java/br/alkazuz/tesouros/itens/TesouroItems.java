package br.alkazuz.tesouros.itens;

import br.alkazuz.tesouros.config.Settings;
import br.alkazuz.tesouros.util.ItemBuilder;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public class TesouroItems {

    public static ItemStack
            LEVEL_1,
        LEVEL_2,
        LEVEL_3,
        LEVEL_4,
        LEVEL_5,
        LEVEL_6,
        LEVEL_7,
        LEVEL_8,
        LEVEL_9,
        LEVEL_10,
        LEVEL_11,
        LEVEL_12;

    public static void init() {
        LEVEL_1 = new ItemBuilder(Material.BOOK).setName(Settings.TESOUROS_TITLE.get(1)).toItemStack();
        LEVEL_2 = new ItemBuilder(Material.BOOK).setName(Settings.TESOUROS_TITLE.get(2)).toItemStack();
        LEVEL_3 = new ItemBuilder(Material.BOOK).setName(Settings.TESOUROS_TITLE.get(3)).toItemStack();
        LEVEL_4 = new ItemBuilder(Material.BOOK).setName(Settings.TESOUROS_TITLE.get(4)).toItemStack();
        LEVEL_5 = new ItemBuilder(Material.BOOK).setName(Settings.TESOUROS_TITLE.get(5)).toItemStack();
        LEVEL_6 = new ItemBuilder(Material.BOOK).setName(Settings.TESOUROS_TITLE.get(6)).toItemStack();
        LEVEL_7 = new ItemBuilder(Material.BOOK).setName(Settings.TESOUROS_TITLE.get(7)).toItemStack();
        LEVEL_8 = new ItemBuilder(Material.BOOK).setName(Settings.TESOUROS_TITLE.get(8)).toItemStack();
        LEVEL_9 = new ItemBuilder(Material.BOOK).setName(Settings.TESOUROS_TITLE.get(9)).toItemStack();
        LEVEL_10 = new ItemBuilder(Material.BOOK).setName(Settings.TESOUROS_TITLE.get(10)).toItemStack();
        LEVEL_11 = new ItemBuilder(Material.BOOK).setName(Settings.TESOUROS_TITLE.get(11)).toItemStack();
        LEVEL_12 = new ItemBuilder(Material.BOOK).setName(Settings.TESOUROS_TITLE.get(12)).toItemStack();
    }

    public static ItemStack getTesouro(int level) {
        switch (level) {
            case 1:
                return LEVEL_1;
            case 2:
                return LEVEL_2;
            case 3:
                return LEVEL_3;
            case 4:
                return LEVEL_4;
            case 5:
                return LEVEL_5;
            case 6:
                return LEVEL_6;
            case 7:
                return LEVEL_7;
            case 8:
                return LEVEL_8;
            case 9:
                return LEVEL_9;
            case 10:
                return LEVEL_10;
            case 11:
                return LEVEL_11;
            case 12:
                return LEVEL_12;
        }
        return null;
    }


}
