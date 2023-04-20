package de.polarwolf.bbcd.listener;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.Plugin;

import de.polarwolf.bbcd.api.BBCDOrchestrator;
import de.polarwolf.bbcd.bossbars.BossBarManager;

public class ListenManager implements Listener {

	protected final Plugin plugin;
	protected final BossBarManager bossBarManager;

	public ListenManager(BBCDOrchestrator orchestrator) {
		this.plugin = orchestrator.getPlugin();
		this.bossBarManager = orchestrator.getBossBarManager();
	}

	public void registerListener() {
		plugin.getServer().getPluginManager().registerEvents(this, plugin);
	}

	public void unregisterListener() {
		HandlerList.unregisterAll(this);
	}

	protected void handlePlayerQuit(PlayerQuitEvent event) {
		Player player = event.getPlayer();
		bossBarManager.cancel(player);
	}

	@EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
	public void onPlayerQuitEvent(PlayerQuitEvent event) {
		try {
			handlePlayerQuit(event);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
