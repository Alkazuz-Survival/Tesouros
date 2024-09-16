package br.alkazuz.tesouros.util;

import br.alkazuz.tesouros.config.Settings;
import com.gmail.nossr50.datatypes.player.PlayerProfile;
import com.gmail.nossr50.datatypes.skills.SkillType;
import com.gmail.nossr50.util.player.UserManager;
import org.bukkit.entity.Player;

import java.util.Map;
import java.util.TreeMap;

public class RandomTesourFish {
    private static Long lastTesouro = 0L;

    private static boolean hasAllSkillsInLevel(Player player, int level) {
        for (SkillType skill : SkillType.values()) {
            if (!player.hasPermission("mcmmo.skills." + skill.toString().toLowerCase())) continue;
            PlayerProfile profile = UserManager.getPlayer(player).getProfile();
            if (profile.getSkillLevel(skill) < level) {
                return false;
            }
        }
        return true;
    }

    public static Integer getRandomTesouro(Player player, SkillType skillType, int skillLevel) {
        if (System.currentTimeMillis() - lastTesouro < 500) {
            return null;
        }
        boolean vipWorld = player.getWorld().getName().equals("vip");

        lastTesouro = System.currentTimeMillis();

        for (Map.Entry<Integer, TreeMap<Integer, Float>> entry : Settings.TESOUROS_PROBABILITY.entrySet()) {
            int level = entry.getKey();
            TreeMap<Integer, Float> probabilityMap = entry.getValue();
            Float probability = probabilityMap.floorEntry(skillLevel).getValue();

            if (probability <= 0 && vipWorld && level != 12) {
                if (Settings.TESOUROS_VIP.containsKey(level)) {
                    probability = Settings.TESOUROS_VIP.get(level);
                }
            }

            if (Settings.SKILLS_MODIFIERS.containsKey(skillType)) {
                TreeMap<Integer, Float> skillModifiers = Settings.SKILLS_MODIFIERS.get(skillType);
                Float skillModifier = skillModifiers.get(level);
                if (skillModifier != null)
                    probability *= skillModifier;
            }
            if (isChance(probability)) {
                if (level == 12) {
                    if (!hasAllSkillsInLevel(player, 500)) {
                        continue;
                    }
                }
                return entry.getKey();
            }
        }
        return null;
    }

    private static boolean isChance(float chance) {
        double random = Math.random() * 100.0;
        return random <= chance;
    }

}
