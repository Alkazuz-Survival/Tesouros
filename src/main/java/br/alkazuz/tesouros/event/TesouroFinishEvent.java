package br.alkazuz.tesouros.event;

import br.alkazuz.tesouros.engines.TesouroOpening;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class TesouroFinishEvent extends Event {

    private static final HandlerList handlers = new HandlerList();

    private final Player player;
    private final TesouroOpening tesouro;

    public TesouroFinishEvent(Player player, TesouroOpening tesouro) {
        this.player = player;
        this.tesouro = tesouro;
    }

    public Player getPlayer() {
        return player;
    }

    public TesouroOpening getTesouro() {
        return tesouro;
    }

    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }
}
