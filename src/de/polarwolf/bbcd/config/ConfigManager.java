package de.polarwolf.bbcd.config;

import java.util.Set;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.plugin.Plugin;

import de.polarwolf.bbcd.api.BBCDException;
import de.polarwolf.bbcd.api.BBCDTemplate;

public class ConfigManager {

	protected Plugin plugin;
	protected String configPath;
	protected ConfigSection section;
	

	public ConfigManager(Plugin plugin, String configPath) {
		setConfig (plugin, configPath);
		section = new ConfigSection();
	}
	
	
	public void setConfig (Plugin newPlugin, String newConfigPath) {
		plugin = newPlugin;
		configPath = newConfigPath;
	}
	
	
	public void reload() throws BBCDException {
		plugin.reloadConfig();
		ConfigurationSection config = plugin.getConfig().getConfigurationSection(configPath);
		ConfigSection newSection = new ConfigSection();
		newSection.load(config);
		section = newSection;
	}


	public Set<BBCDTemplate> getTemplates() {
		return section.getTemplates();
	}
	
	
	public BBCDTemplate findTemplate(String templateName) {
		return section.findTemplate(templateName);
	}

}
