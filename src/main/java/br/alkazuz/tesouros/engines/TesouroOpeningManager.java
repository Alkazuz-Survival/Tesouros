package br.alkazuz.tesouros.engines;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class TesouroOpeningManager {
    private static final TesouroOpeningManager instance = new TesouroOpeningManager();
    private final List<TesouroOpening> tesouros = new ArrayList<>();

    public void addTesouro(TesouroOpening tesouroOpening) {
        tesouros.add(tesouroOpening);
    }

    public void removeTesouro(TesouroOpening tesouroOpening) {
        tesouros.remove(tesouroOpening);
    }

    public TesouroOpening getTesouro(UUID uuid) {
        for (TesouroOpening tesouro : tesouros) {
            if (tesouro.getUuid().equals(uuid)) {
                return tesouro;
            }
        }
        return null;
    }

    public List<TesouroOpening> getTesouros() {
        return tesouros;
    }

    public static TesouroOpeningManager getInstance() {
        return instance;
    }
}
