package br.alkazuz.tesouros;

import br.alkazuz.tesouros.commands.TesourosAdminCommand;
import br.alkazuz.tesouros.commands.TesourosCommand;
import br.alkazuz.tesouros.config.ArenasSettings;
import br.alkazuz.tesouros.config.Settings;
import br.alkazuz.tesouros.config.manager.ConfigManager;
import br.alkazuz.tesouros.gui.GuiEditTesouroItems;
import br.alkazuz.tesouros.gui.GuiShowTesouroItems;
import br.alkazuz.tesouros.gui.MenuConfirmOpen;
import br.alkazuz.tesouros.hooks.SunshineHook;
import br.alkazuz.tesouros.items.TesouroItemManager;
import br.alkazuz.tesouros.itens.TesouroItems;
import br.alkazuz.tesouros.listener.PlayerInteractListener;
import br.alkazuz.tesouros.listener.mcMMOListener;
import br.alkazuz.tesouros.util.EventWaiter;
import org.bukkit.Bukkit;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

public final class Tesouros extends JavaPlugin {
    private static Tesouros instance;
    public static EventWaiter eventWaiter;

    @Override
    public void onEnable() {
        instance = this;
        ConfigManager.createConfig("settings");
        getCommand("tesourosadmin").setExecutor(new TesourosAdminCommand());
        getCommand("tesouros").setExecutor(new TesourosCommand());

        TesouroItems.init();
        loadListeners();
        TesouroItemManager.load();
        SunshineHook.init();
        eventWaiter = new EventWaiter(this);
        eventWaiter.addEvents(AsyncPlayerChatEvent.class, InventoryClickEvent.class);

        Bukkit.getScheduler().scheduleSyncDelayedTask(this, ArenasSettings::load);
        Bukkit.getScheduler().scheduleSyncDelayedTask(this, Settings::load);
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
        pm.registerEvents(new GuiEditTesouroItems(), this);
        pm.registerEvents(new GuiShowTesouroItems(), this);
    }

    public static Tesouros getInstance() {
        return instance;
    }
}
