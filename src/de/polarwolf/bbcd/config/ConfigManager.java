package de.polarwolf.bbcd.config;

import java.util.List;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.plugin.Plugin;

import de.polarwolf.bbcd.api.BBCDOrchestrator;
import de.polarwolf.bbcd.exception.BBCDException;

public class ConfigManager {

	public static final String TEMPLATE_SECTION = "templates";

	protected Plugin plugin;
	protected ConfigSection section;

	public ConfigManager(BBCDOrchestrator orchestrator) {
		this.plugin = orchestrator.getPlugin();
		section = new ConfigSection();
	}

	public void reload() throws BBCDException {
		plugin.reloadConfig();
		ConfigurationSection fileConfig = plugin.getConfig().getConfigurationSection(TEMPLATE_SECTION);
		ConfigSection newSection = new ConfigSection(fileConfig);
		section = newSection;
	}

	public List<BBCDTemplate> getConfigTemplates() {
		return section.getTemplates();
	}

}
