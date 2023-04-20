package de.polarwolf.bbcd.bossbars;

import java.util.HashSet;
import java.util.Set;

import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import de.polarwolf.bbcd.api.BBCDOrchestrator;
import de.polarwolf.bbcd.config.BBCDTemplate;
import de.polarwolf.bbcd.config.ConfigManager;
import de.polarwolf.bbcd.events.EventManager;
import de.polarwolf.bbcd.exception.BBCDException;

public class BossBarManager {

	protected final Plugin plugin;
	protected final ConfigManager configManager;
	protected final EventManager eventManager;
	private boolean disabled = false;
	protected Set<BBCDBossBar> bbcdBossBars = new HashSet<>();
	protected Scheduler scheduler = null;

	public BossBarManager(BBCDOrchestrator orchestrator) {
		this.plugin = orchestrator.getPlugin();
		this.configManager = orchestrator.getConfigManager();
		this.eventManager = orchestrator.getEventManager();
	}

	// Get the complete list of of active BBCDBossBars.
	// Perhaps you need it for some advanced game-features.
	// BBCDBossBars already finished are excluded from set.
	public Set<BBCDBossBar> getBbcdBossBars() {
		Set<BBCDBossBar> myBbcdBossBars = new HashSet<>();
		for (BBCDBossBar bbcdBossBar : bbcdBossBars) {
			if (!bbcdBossBar.isFinished()) {
				myBbcdBossBars.add(bbcdBossBar);
			}
		}
		return myBbcdBossBars;
	}

	// Find the BBCDBossBar given by Player and Template.
	// BBCDBossBars already finished are excluded from search.
	public BBCDBossBar findBbcdBossBar(Player player, BBCDTemplate bbcdTemplate) {
		for (BBCDBossBar bbcdBossBar : getBbcdBossBars()) {
			if ((bbcdBossBar.getPlayer().equals(player)) && (bbcdBossBar.getBbcdTemplate().equals(bbcdTemplate))) {
				return bbcdBossBar;
			}
		}
		return null;
	}

	// Simply create a new BBCDBossbar-object.
	// Override this for custom objects.
	protected BBCDBossBar createBbcdBossBar(Player player, BBCDTemplate bbcdTemplate) {
		return new BBCDBossBar(plugin, player, bbcdTemplate);
	}

	protected void prepareBbcdBossBar(BBCDBossBar bbcdBossBar) {
		bbcdBossBar.setEventHelper(eventManager.getEventHelper());
	}

	// Add a new BBCDBossbar for the given player using the given template.
	// Starts the scheduler if needed.
	// A previous BBCDBossbar gets overwritten with no action executed
	public BBCDBossBar addBbcdBossBar(Player player, BBCDTemplate bbcdTemplate) {
		BBCDBossBar oldBbcdBossBar = findBbcdBossBar(player, bbcdTemplate);
		if (oldBbcdBossBar != null) {
			oldBbcdBossBar.finish();
		}

		BBCDBossBar newBbcdBossBar = createBbcdBossBar(player, bbcdTemplate);
		prepareBbcdBossBar(newBbcdBossBar);
		bbcdBossBars.add(newBbcdBossBar);
		startScheduler();
		return newBbcdBossBar;
	}

	// Called by the scheduler to increment the progress of BBCDBossBars
	// and executes modification if End is reached
	protected void handleTick() {
		if (bbcdBossBars.isEmpty()) {
			stopScheduler();
			return;
		}

		Set<BBCDBossBar> myBbcdBossBars = new HashSet<>(bbcdBossBars);
		for (BBCDBossBar bbcdBossBar : myBbcdBossBars) {
			try {
				bbcdBossBar.handleTick();
			} catch (BBCDException e) {
				plugin.getLogger().warning("ERROR: " + e.getMessage());
				bbcdBossBar.finish();
			} catch (Exception e) {
				e.printStackTrace();
				bbcdBossBar.finish();
			}
			if (bbcdBossBar.isFinished()) {
				bbcdBossBars.remove(bbcdBossBar);
			}
		}
	}

	// Cancel all active BBCDBossBars for the given player
	// Needed for the PlayerQuitEvent
	public void cancel(Player player) {
		for (BBCDBossBar myBbcdBossBar : bbcdBossBars) {
			if (myBbcdBossBar.getPlayer().equals(player)) {
				myBbcdBossBar.finish();
			}
		}
	}

	// Start the Scheduler-Task if needed.
	protected void startScheduler() {
		if (scheduler == null) {
			scheduler = new Scheduler(this);
			scheduler.runTaskTimer(plugin, 1, 1);
		}
	}

	// Stop the scheduler
	protected void stopScheduler() {
		if (scheduler != null) {
			scheduler.cancel();
			scheduler = null;
		}
	}

	public boolean isDisabled() {
		return disabled;
	}

	// Stop the Scheduler if the plugin gets disabled
	public void disable() {
		disabled = true;
		stopScheduler();
		for (BBCDBossBar myBbcdBossBar : getBbcdBossBars()) {
			myBbcdBossBar.finish();
		}
	}

}
