package br.alkazuz.tesouros.listener;

import br.alkazuz.correio.object.CorreioItem;
import br.alkazuz.correio.object.CorreioItemManager;
import br.alkazuz.tesouros.itens.TesouroItems;
import br.alkazuz.tesouros.util.RandomTesourFish;
import com.gmail.nossr50.events.experience.McMMOPlayerXpGainEvent;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;

public class mcMMOListener implements Listener {
    private static final HashMap<String, Long> tesouroDelay = new HashMap<>();

    @EventHandler
    public void onGainXP(McMMOPlayerXpGainEvent event) {

        if (tesouroDelay.containsKey(event.getPlayer().getName())
                && tesouroDelay.get(event.getPlayer().getName()) > System.currentTimeMillis()) {
            return;
        }

        Integer randomLevel = RandomTesourFish.getRandomTesouro(event.getPlayer(), event.getSkill(), event.getSkillLevel());
        if (randomLevel != null) {
            ItemStack item = TesouroItems.getTesouro(randomLevel).clone();
            CorreioItem correioItem = new CorreioItem(item, "Nightcraft", event.getPlayer().getName());

            correioItem.save();

            CorreioItemManager.addCorreioItem(
                    event.getPlayer().getName(),
                    correioItem);
            Bukkit.broadcastMessage(String.format(
                    "§3[Tesouros] %s §7encontrou um §ftesouro de nível %d",
                    event.getPlayer().getDisplayName(), randomLevel
            ));

            event.getPlayer().sendMessage("§aVocê encontrou um tesouro! Verifique seu correio.");

            tesouroDelay.put(event.getPlayer().getName(), System.currentTimeMillis() + 60000 * 10);
        } else {
            //tesouroDelay.put(event.getPlayer().getName(), System.currentTimeMillis() + 60000 * 5);
        }

    }
}
