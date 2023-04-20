package de.polarwolf.bbcd.api;

import java.util.List;
import java.util.Set;

import org.bukkit.entity.Player;

import de.polarwolf.bbcd.bossbars.BBCDBossBar;
import de.polarwolf.bbcd.bossbars.BossBarManager;
import de.polarwolf.bbcd.config.BBCDTemplate;
import de.polarwolf.bbcd.config.ConfigManager;
import de.polarwolf.bbcd.events.EventManager;
import de.polarwolf.bbcd.exception.BBCDException;

public class BBCDAPI {

	protected final ConfigManager configManager;
	protected final EventManager eventManager;
	protected final BossBarManager bossBarManager;
	protected final BBCDOrchestrator orchestrator;

	public BBCDAPI(BBCDOrchestrator orchestrator) {
		this.configManager = orchestrator.getConfigManager();
		this.eventManager = orchestrator.getEventManager();
		this.bossBarManager = orchestrator.createBossBarManager();
		this.orchestrator = orchestrator;
	}

	// ConfigManager
	public void reload() throws BBCDException {
		configManager.reload();
	}

	// EventManager
	public BBCDTemplate findTemplate(String templateName) {
		return eventManager.findTemplate(templateName);
	}

	public List<BBCDTemplate> getTemplates() {
		return eventManager.getTemplates();
	}

	// BossBarManager
	public BBCDBossBar findBbcdBossBar(Player player, BBCDTemplate bbcdTemplate) {
		return bossBarManager.findBbcdBossBar(player, bbcdTemplate);
	}

	public Set<BBCDBossBar> getBbcdBossBars() {
		return bossBarManager.getBbcdBossBars();
	}

	public BBCDBossBar addBbcdBossBar(Player player, BBCDTemplate bbcdTemplate) {
		return bossBarManager.addBbcdBossBar(player, bbcdTemplate);
	}

	public void cancel(Player player) {
		bossBarManager.cancel(player);
	}

	// Disable
	public boolean isDisabled() {
		return orchestrator.isDisabled();
	}

}
