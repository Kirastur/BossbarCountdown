package de.polarwolf.bbcd.config;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.bukkit.configuration.ConfigurationSection;

import de.polarwolf.bbcd.api.BBCDException;
import de.polarwolf.bbcd.api.BBCDTemplate;

public class ConfigSection {

	protected Set<BBCDTemplate> templateSet = new HashSet<>();
	
	
	public Set<BBCDTemplate> getTemplates() {
		return new HashSet<>(templateSet);
	}
	

	protected BBCDTemplate createTemplate(String name, Map<String,String> parameters ) throws BBCDException {
		return new BBCDTemplate (name, parameters);
	}
	
	
	protected BBCDTemplate loadTemplate(ConfigurationSection config) throws BBCDException {
		String name = config.getName();
		Map <String,String> parameters = new HashMap<>();
		for (String attributeName : config.getKeys(false)) {
			String value = config.getString(attributeName);
			parameters.put(attributeName, value);
		}
		return createTemplate(name, parameters);
	}


	// You cannot call "load" directly, because the ConfigManager does not expose this object
	public void load(ConfigurationSection config) throws BBCDException {
		templateSet.clear();
		for (String ruleName : config.getKeys(false)) {

			ConfigurationSection section = config.getConfigurationSection(ruleName);
			if (section == null) {
				throw new BBCDException(config.getName(), "Config Syntax Error", ruleName);
			}

			BBCDTemplate newTemplate = loadTemplate(section);
			templateSet.add(newTemplate);
		}
	}
	

	public BBCDTemplate findTemplate(String templateName) {
		for (BBCDTemplate configTemplate : templateSet) {
			if (configTemplate.getName().equalsIgnoreCase(templateName)) {
				return configTemplate;
			}
		}
		return null;
	}

}

