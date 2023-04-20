package de.polarwolf.bbcd.config;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.bukkit.configuration.ConfigurationSection;

import de.polarwolf.bbcd.exception.BBCDException;

public class ConfigSection {

	protected List<BBCDTemplate> templates = new ArrayList<>();

	protected ConfigSection() {
	}

	protected ConfigSection(ConfigurationSection fileSection) throws BBCDException {
		loadFromFile(fileSection);
	}

	public List<BBCDTemplate> getTemplates() {
		return new ArrayList<>(templates);
	}

	protected void addTemplate(BBCDTemplate newTemplate) {
		templates.add(newTemplate);
	}

	protected void addTemplate(String name, Map<String, String> parameters) throws BBCDException {
		BBCDTemplate newTemplate = new BBCDTemplate(name, parameters);
		addTemplate(newTemplate);
	}

	protected void addTemplate(ConfigurationSection fileSection) throws BBCDException {
		BBCDTemplate newTemplate = new BBCDTemplate(fileSection);
		addTemplate(newTemplate);
	}

	protected void loadFromFile(ConfigurationSection fileSection) throws BBCDException {
		for (String myTemplateName : fileSection.getKeys(false)) {
			if (!fileSection.contains(myTemplateName, true)) {
				continue;
			}
			if (!fileSection.isConfigurationSection(myTemplateName)) {
				throw new BBCDException(fileSection.getName(), "Illegal template section", myTemplateName);
			}
			ConfigurationSection myTemplateFileSection = fileSection.getConfigurationSection(myTemplateName);
			addTemplate(myTemplateFileSection);
		}
	}

	public BBCDTemplate findTemplate(String templateName) {
		for (BBCDTemplate configTemplate : templates) {
			if (configTemplate.getName().equalsIgnoreCase(templateName)) {
				return configTemplate;
			}
		}
		return null;
	}

}
