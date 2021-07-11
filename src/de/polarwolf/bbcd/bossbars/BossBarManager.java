package de.polarwolf.bbcd.bossbars;

import java.util.HashSet;
import java.util.Set;

import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import de.polarwolf.bbcd.api.BBCDBossBar;
import de.polarwolf.bbcd.api.BBCDException;
import de.polarwolf.bbcd.api.BBCDTemplate;
import de.polarwolf.bbcd.config.ConfigManager;

public class BossBarManager {

	
	protected final Plugin plugin;
	protected final ConfigManager configManager;
	protected Set<BBCDBossBar> bbcdBossBars = new HashSet<>();
	protected Scheduler scheduler = null;
	

	public BossBarManager(Plugin plugin, ConfigManager configManager) {
		this.plugin = plugin;
		this.configManager = configManager;
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
	
	
	// Add a new BBCDBossbar for the given player using the given template.
	// Starts the scheduler if needed.
	// A previous BBCDBossbar gets overwritten with no action executed 
	public BBCDBossBar addBbcdBossBar(Player player, BBCDTemplate bbcdTemplate) {
		BBCDBossBar oldBbcdBossBar = findBbcdBossBar(player, bbcdTemplate);
		if (oldBbcdBossBar != null) {
			oldBbcdBossBar.finish();
		}
		
		BBCDBossBar newBbcdBossBar = createBbcdBossBar(player, bbcdTemplate);
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
	
}
