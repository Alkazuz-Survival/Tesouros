package br.alkazuz.tesouros.engines;

import org.bukkit.Location;

import java.util.ArrayList;
import java.util.List;

public class ArenasTesouroManager {
    private static final ArenasTesouroManager instance = new ArenasTesouroManager();
    private final List<ArenaTesouro> arenas = new ArrayList<>();

    public void addArena(ArenaTesouro arenaTesouro) {
        arenas.add(arenaTesouro);
    }

    public void removeArena(ArenaTesouro arenaTesouro) {
        arenas.remove(arenaTesouro);
    }

    public ArenaTesouro getArena(Location location) {
        for (ArenaTesouro arena : arenas) {
            if (arena.getLocation().equals(location)) {
                return arena;
            }
        }
        return null;
    }

    public ArenaTesouro getFreeArena() {
        System.out.println(arenas.size());
        List<ArenaTesouro> remainingArenas = new ArrayList<>(arenas);
        for (TesouroOpening tesouro : TesouroOpeningManager.getInstance().getTesouros()) {
            if (tesouro.getArenaTesouro() == null) {
                continue;
            }
            for (ArenaTesouro arena : arenas) {
                if (arena == tesouro.getArenaTesouro()) {
                    continue;
                }
                remainingArenas.remove(arena);
            }
        }

        if (remainingArenas.isEmpty()) {
            return null;
        }

        return remainingArenas.get(0);
    }

    public List<ArenaTesouro> getArenas() {
        return arenas;
    }

    public static ArenasTesouroManager getInstance() {
        return instance;
    }
}
