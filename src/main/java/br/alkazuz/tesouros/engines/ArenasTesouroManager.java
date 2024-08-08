package br.alkazuz.tesouros.engines;

import org.bukkit.Location;
import org.bukkit.entity.Player;

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
        List<ArenaTesouro> remainingArenas = new ArrayList<>(arenas);
        for (ArenaTesouro arena : arenas) {
            boolean hasPlayerNear = false;
            List<Player> players = arena.getLocation().getWorld().getPlayers();
            for (Player player : players) {
                if (player.getLocation().distance(arena.getLocation()) <= 25) {
                    hasPlayerNear = true;
                    break;
                }
            }
            if (hasPlayerNear) {
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
