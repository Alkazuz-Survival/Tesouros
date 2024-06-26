package br.alkazuz.tesouros;

import br.alkazuz.tesouros.commands.TesourosCommand;
import br.alkazuz.tesouros.config.ArenasSettings;
import br.alkazuz.tesouros.config.Settings;
import br.alkazuz.tesouros.config.manager.ConfigManager;
import br.alkazuz.tesouros.engines.ArenasTesouroManager;
import br.alkazuz.tesouros.gui.MenuConfirmOpen;
import br.alkazuz.tesouros.itens.TesouroItems;
import br.alkazuz.tesouros.listener.PlayerInteractListener;
import br.alkazuz.tesouros.listener.mcMMOListener;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

public final class Tesouros extends JavaPlugin {
    private static Tesouros instance;
    @Override
    public void onEnable() {
        instance = this;
        ConfigManager.createConfig("settings");
        getCommand("tesouros").setExecutor(new TesourosCommand());
        Settings.load();
        ArenasSettings.load();
        TesouroItems.init();
        loadListeners();
    }

    @Override
    public void onDisable() {
    }

    private void loadListeners() {
        PluginManager pm = getServer().getPluginManager();
        if (pm.getPlugin("mcMMO") != null) {
            pm.registerEvents(new mcMMOListener(), this);
        }
        pm.registerEvents(new PlayerInteractListener(), this);
        pm.registerEvents(new MenuConfirmOpen(), this);
    }

    public static Tesouros getInstance() {
        return instance;
    }
}
