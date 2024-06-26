package br.alkazuz.tesouros.util;

import br.alkazuz.tesouros.config.Settings;
import com.gmail.nossr50.datatypes.skills.SkillType;

import java.util.Arrays;
import java.util.Map;
import java.util.Objects;
import java.util.TreeMap;

public class RandomTesourFish {

    public static Integer getRandomTesouro(SkillType skillType, int level) {
        for (Map.Entry<Integer, TreeMap<Integer, Float>> entry : Settings.TESOUROS_PROBABILITY.entrySet()) {
            TreeMap<Integer, Float> probabilityMap = entry.getValue();
            Float probability = probabilityMap.floorEntry(level).getValue();
            if (probability != null && isChance(probability)) {
                return entry.getKey();
            }
        }
        return null;
    }

    private static boolean isChance(float chance) {
        return Math.random() * 100.0 < chance;
    }

}
