package de.polarwolf.bbcd.events;

import java.util.List;

import org.bukkit.plugin.Plugin;

import de.polarwolf.bbcd.api.BBCDOrchestrator;
import de.polarwolf.bbcd.bossbars.BBCDBossBar;
import de.polarwolf.bbcd.config.BBCDTemplate;
import de.polarwolf.bbcd.config.ConfigManager;

public class EventManager {

	protected final Plugin plugin;
	protected final ConfigManager configManager;

	public EventManager(BBCDOrchestrator orchestrator) {
		this.plugin = orchestrator.getPlugin();
		this.configManager = orchestrator.getConfigManager();
	}

	public EventHelper getEventHelper() {
		return new EventHelper(this);
	}

	public BBCDTemplate findTemplate(String templateName) {
		BBCDGetTemplatesEvent event = new BBCDGetTemplatesEvent(configManager.getConfigTemplates());
		plugin.getServer().getPluginManager().callEvent(event);
		return event.findTemplate(templateName);
	}

	public List<BBCDTemplate> getTemplates() {
		BBCDGetTemplatesEvent event = new BBCDGetTemplatesEvent(configManager.getConfigTemplates());
		plugin.getServer().getPluginManager().callEvent(event);
		return event.templates;
	}

	public void goalReached(BBCDBossBar bbcdBossBar) {
		BBCDGoalReachedEvent event = new BBCDGoalReachedEvent(bbcdBossBar);
		plugin.getServer().getPluginManager().callEvent(event);
	}

}
