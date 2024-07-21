package br.alkazuz.tesouros.commands;

import br.alkazuz.tesouros.config.Settings;
import br.alkazuz.tesouros.gui.GuiShowTesouroItems;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Map;

public class TesourosCommand implements CommandExecutor {


    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        if (!(commandSender instanceof Player)) {
            return true;
        }

        if (strings.length == 0) {
            commandSender.sendMessage("§a\n§a§lTesouros disponíveis:\n");
            for (int i = 1; i <= 12; i++) {
                int minLevel = 0;
                Map<Integer, Float> probability = Settings.TESOUROS_PROBABILITY.get(i);
                for (Map.Entry<Integer, Float> entry : probability.entrySet()) {
                    if (entry.getValue() > 0) {
                        minLevel = entry.getKey();
                        break;
                    }
                }
                if (minLevel == 0) {
                    continue;
                }
                String title = String.format(" %sTesouro nível %d §8- §7Necessário nível §a%d §7em alguma skill", getColorLevel(i), i, minLevel);
                if (i == 12) {
                    title = String.format(" %sTesouro nível %d §8- §7Necessário nível §a%d §7em todas as skills", getColorLevel(i), i, minLevel);
                }
                commandSender.sendMessage(title);

            }
            commandSender.sendMessage("§7 Use §f/tesouros itens <level> §7para abrir o menu de itens do tesouro\n§c");
            return true;
        } else if (strings[0].equalsIgnoreCase("itens")) {
            if (strings.length == 1) {
                commandSender.sendMessage("§cUtilize /tesouros itens <level>");
                return true;
            }
            int level = 0;

            try {
                level = Integer.parseInt(strings[1]);
            } catch (NumberFormatException e) {
                commandSender.sendMessage("§cO level deve ser um número!");
                return true;
            }

            if (level < 1 || level > 12) {
                commandSender.sendMessage("§cO level deve ser entre 1 e 12!");
                return true;
            }

            GuiShowTesouroItems.open((Player) commandSender, level);
        } else {
            commandSender.sendMessage("§c\n§a§l Comandos disponíveis:\n\n" +
                    "§7/tesouros itens <level> §8- §fAbre o menu de edição de itens do tesouro\n" +
                    "§7/tesouros §8- §fMostra todos os níveis de tesouros disponíveis\n§c");
        }


        return false;
    }

    private String getColorLevel(int level) {
        String title = Settings.TESOUROS_TITLE.get(level);
        if (title.contains("§")) {
            return title.substring(title.indexOf("§"), title.indexOf("§") + 2);
        }
        return "§f";
    }
}
