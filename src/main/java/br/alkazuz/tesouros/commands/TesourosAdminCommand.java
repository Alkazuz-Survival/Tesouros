package br.alkazuz.tesouros.commands;

import br.alkazuz.tesouros.config.ArenasSettings;
import br.alkazuz.tesouros.config.Settings;
import br.alkazuz.tesouros.config.manager.ConfigManager;
import br.alkazuz.tesouros.engines.ArenaTesouro;
import br.alkazuz.tesouros.engines.ArenasTesouroManager;
import br.alkazuz.tesouros.gui.GuiEditTesouroItems;
import br.alkazuz.tesouros.items.TesouroItem;
import br.alkazuz.tesouros.itens.TesouroItems;
import br.alkazuz.tesouros.util.Serializer;
import br.alkazuz.tesouros.util.TesouroItemGenerator;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import java.util.List;

public class TesourosAdminCommand implements CommandExecutor {


    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        if (!(commandSender.hasPermission("tesouros.admin"))) {
            return true;
        }

        if (!(commandSender instanceof Player)) {
            return true;
        }

        if (strings.length == 0) {
            return true;
        }

        Player player = (Player) commandSender;

        if (strings[0].equalsIgnoreCase("addarena")) {
            ArenaTesouro arenaTesouro = new ArenaTesouro(player.getLocation());
            ArenasTesouroManager.getInstance().addArena(arenaTesouro);
            ArenasSettings.save(arenaTesouro);
            player.sendMessage("§aArena adicionada com sucesso!");
            return true;
        }

        if (strings[0].equalsIgnoreCase("itens")) {
            if (strings.length == 1) {
                player.sendMessage("§cUtilize /tesouros itens <level>");
                return true;
            }

            int level = 0;

            try {
                level = Integer.parseInt(strings[1]);
            } catch (NumberFormatException e) {
                player.sendMessage("§cO level deve ser um número!");
                return true;
            }

            if (level < 1 || level > 12) {
                player.sendMessage("§cO level deve ser entre 1 e 12!");
                return true;
            }

            GuiEditTesouroItems.open(player, level);

            return true;
        }

        if (strings[0].equalsIgnoreCase("setspawn")) {
            Settings.spawnLocation = player.getLocation();
            FileConfiguration config = ConfigManager
                    .getConfig("settings");
            config.set("spawn-location", Serializer.getStringLocation(player.getLocation()));
            ConfigManager.saveConfig(config, "settings");
            player.sendMessage("§aSpawn setado com sucesso!");
            return true;
        }

        if (strings[0].equalsIgnoreCase("testitens")) {
            int level = Integer.parseInt(strings[1]);
            List<TesouroItem> tesouroItems = TesouroItemGenerator.generateTesouroItems(level);
            for  (TesouroItem tesouroItem : tesouroItems) {
                player.getInventory().addItem(tesouroItem.getItemStack());
            }
            return true;
        }

        if (strings[0].equalsIgnoreCase("addlivro")) {
            if (strings.length == 1) {
                player.sendMessage("§cUtilize /tesouros addlivro <level>");
                return true;
            }

            int level = 0;

            try {
                level = Integer.parseInt(strings[1]);
            } catch (NumberFormatException e) {
                player.sendMessage("§cO level deve ser um número!");
                return true;
            }

            if (level < 1 || level > 12) {
                player.sendMessage("§cO level deve ser entre 1 e 12!");
                return true;
            }

            player.getInventory().addItem(TesouroItems.getTesouro(level));
            player.sendMessage("§aLivro adicionado com sucesso!");
            return true;
        }

        player.sendMessage("§cComandos disponíveis:");
        player.sendMessage("§7/tesouros addarena");
        player.sendMessage("§7/tesouros itens <level>");
        player.sendMessage("§7/tesouros setspawn");
        player.sendMessage("§7/tesouros testitens <level>");
        player.sendMessage("§7/tesouros addlivro <level>");

        return false;
    }
}
