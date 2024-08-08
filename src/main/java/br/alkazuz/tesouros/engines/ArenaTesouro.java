package br.alkazuz.tesouros.engines;

import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Chest;

import java.util.ArrayList;
import java.util.List;

public class ArenaTesouro {
    private final Location location;
    private final List<Location> spawnableLocations = new ArrayList<>();
    private Chest chest;

    public ArenaTesouro(Location location) {
        this.location = location;
        for (int x = -15; x <= 15; x++) {
            for (int z = -15; z <= 15; z++) {
                for (int y = 0; y <= 15; y++) {
                    Location loc = location.clone().add(x, y, z);
                    Chunk chunk = location.getChunk();
                    if (!chunk.isLoaded()) {
                        chunk.load();
                    }
                    if (loc.getBlock().getType().name().contains("CHEST")) {
                        chest = (Chest) loc.getBlock().getState();
                    }
                }
            }
        }
    }

    public Location getLocation() {
        return location;
    }

    public Chest getChest() {
        Chunk chunk = chest.getChunk();
        if (!chunk.isLoaded()) {
            chunk.load();
        }
        return chest;
    }

    public List<Location> spawnableLocations() {
        if (!spawnableLocations.isEmpty()) {
            return spawnableLocations;
        }

        World world = chest.getLocation().getWorld();
        int baseX = chest.getLocation().getBlockX();
        int baseY = chest.getLocation().getBlockY();
        int baseZ = chest.getLocation().getBlockZ();
        int radius = 7;

        for (int x = baseX - radius; x <= baseX + radius; x++) {
            for (int z = baseZ - radius; z <= baseZ + radius; z++) {
                if ((x - baseX) * (x - baseX) + (z - baseZ) * (z - baseZ) <= radius * radius) {
                    Location loc = new Location(world, x, baseY, z);
                    spawnableLocations.add(loc);
                }
            }
        }

        return spawnableLocations;
    }

    public Location getRandomSpawnableLocation() {
        List<Location> spawnableLocations = spawnableLocations();
        return spawnableLocations.get((int) (Math.random() * spawnableLocations.size()));
    }


}
