package br.alkazuz.tesouros.hooks;

import org.bukkit.Bukkit;

public class SunshineHook {
    public static boolean enabled = false;

    public static void init() {
        enabled = Bukkit.getPluginManager().getPlugin("SunshineAntiHack") != null;
    }
}
