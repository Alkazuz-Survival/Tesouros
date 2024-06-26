package br.alkazuz.tesouros.config.manager;

import br.alkazuz.tesouros.Tesouros;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;

public class ConfigManager {

	public static void createConfig(String file) {
		if (!new File(Tesouros.getInstance().getDataFolder(), file + ".yml").exists()) {
			Tesouros.getInstance().saveResource(file + ".yml", false);
		}
	}
	
	public static FileConfiguration getConfig(String file) {
      	try {
      		File arquivo = new File(Tesouros.getInstance().getDataFolder() + File.separator + file + ".yml");
			FileConfiguration config = YamlConfiguration.loadConfiguration(arquivo);
			return config;
		} catch (Throwable e) {
			e.printStackTrace();
		} 
      	return null;
	}

	public static void saveConfig(FileConfiguration config, String file) {
		try {
			config.save(new File(Tesouros.getInstance().getDataFolder() + File.separator + file + ".yml"));
		} catch (Throwable e) {
			e.printStackTrace();
		}
	}
}