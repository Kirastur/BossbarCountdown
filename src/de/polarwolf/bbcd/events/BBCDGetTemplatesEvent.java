package de.polarwolf.bbcd.events;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import de.polarwolf.bbcd.config.BBCDTemplate;

public class BBCDGetTemplatesEvent extends Event {

	private static final HandlerList handlers = new HandlerList();

	List<BBCDTemplate> templates;

	BBCDGetTemplatesEvent(List<BBCDTemplate> initialTemplates) {
		this.templates = new ArrayList<>(initialTemplates);
	}

	public BBCDTemplate findTemplate(String templateName) {
		for (BBCDTemplate myTemplate : templates) {
			if (myTemplate.getName().equals(templateName)) {
				return myTemplate;
			}
		}
		return null;
	}

	public boolean hasTemplate(String templateName) {
		return (findTemplate(templateName) != null);
	}

	public boolean addTemplate(BBCDTemplate newTemplate) {
		if (hasTemplate(newTemplate.getName())) {
			return false;
		}
		templates.add(newTemplate);
		return true;
	}

	@Override
	public HandlerList getHandlers() {
		return getHandlerList();
	}

	public static HandlerList getHandlerList() {
		return handlers;
	}

}