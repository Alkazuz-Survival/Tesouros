package br.alkazuz.tesouros.util;

import br.alkazuz.tesouros.engines.TesouroOpening;
import br.alkazuz.tesouros.items.TesouroItem;
import br.alkazuz.tesouros.items.TesouroItemManager;

import java.util.*;
import java.util.stream.Collectors;
import java.util.HashMap;
import java.util.stream.Collectors;

public class TesouroItemGenerator {

    public static List<TesouroItem> generateTesouroItems(int level) {
        List<TesouroItem> allItems = TesouroItemManager.getTesouroItems(level);
        Map<String, Integer> concurrenceMap = allItems.stream()
                .filter(tesouroItem -> tesouroItem.getConcurrence() != null)
                .map(tesouroItem -> {
                    String[] parts = tesouroItem.getConcurrence().split(":");
                    String key = parts[0];
                    int value = Integer.parseInt(parts[1]);
                    return new AbstractMap.SimpleEntry<>(key, value);
                })
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (oldValue, newValue) -> newValue, HashMap::new));

        List<TesouroItem> tesouroItems = new ArrayList<>();

        List<String> resolvedConcurrence = new ArrayList<>();

        for (Map.Entry<String, Integer> entry : concurrenceMap.entrySet()) {
            String key = entry.getKey();
            int value = entry.getValue();
            if (resolvedConcurrence.contains(key)) {
                continue;
            }

            List<TesouroItem> items = allItems.stream()
                    .filter(tesouroItem -> tesouroItem.getConcurrence() != null && tesouroItem.getConcurrence().startsWith(key))
                    .collect(Collectors.toList());

            if (value == 0) {
                for (TesouroItem tesouroItem : items) {
                    if (chance(tesouroItem.getChance())) {
                        tesouroItems.add(tesouroItem);
                        break;
                    }
                }
            } else if  (value == 1) {
                TesouroItem tesouroItem = items.get(new Random().nextInt(items.size()));
                tesouroItems.add(tesouroItem);
            }

            resolvedConcurrence.add(key);
        }

        for (TesouroItem tesouroItem : allItems) {
            if (tesouroItem.getConcurrence() != null) continue;

            if (chance(tesouroItem.getChance())) {
                tesouroItems.add(tesouroItem);
            }
        }

        return tesouroItems;
    }

    private static boolean chance(float chance) {
        float random = new Random().nextFloat() * 100.0f;
        return random <= chance;
    }

}
