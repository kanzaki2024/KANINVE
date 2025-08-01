package org.kanzakiserver;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerGameModeChangeEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;
import java.util.UUID;

public class Kaninve extends JavaPlugin implements Listener {

    private final HashMap<UUID, ItemStack[]> survivalInventories = new HashMap<>();
    private final HashMap<UUID, ItemStack[]> creativeInventories = new HashMap<>();

    @Override
    public void onEnable() {
        Bukkit.getPluginManager().registerEvents(this, this);
        getLogger().info("Kaninve が有効になりました！");
    }

    @Override
    public void onDisable() {
        getLogger().info("Kaninve が無効になりました！");
    }

    @EventHandler
    public void onGameModeChange(PlayerGameModeChangeEvent event) {
        Player player = event.getPlayer();
        UUID uuid = player.getUniqueId();
        GameMode newMode = event.getNewGameMode();

        if (player.getGameMode() == GameMode.SURVIVAL) {
            survivalInventories.put(uuid, player.getInventory().getContents());
        } else if (player.getGameMode() == GameMode.CREATIVE) {
            creativeInventories.put(uuid, player.getInventory().getContents());
        }

        Bukkit.getScheduler().runTaskLater(this, () -> {
            if (newMode == GameMode.SURVIVAL && survivalInventories.containsKey(uuid)) {
                player.getInventory().setContents(survivalInventories.get(uuid));
            } else if (newMode == GameMode.CREATIVE && creativeInventories.containsKey(uuid)) {
                player.getInventory().setContents(creativeInventories.get(uuid));
            } else {
                player.getInventory().clear();
            }
        }, 1L);
    }
}
