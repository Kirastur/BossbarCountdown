package de.polarwolf.bbcd.events;

import de.polarwolf.bbcd.bossbars.BBCDBossBar;

public class EventHelper {

	protected final EventManager eventManager;

	EventHelper(EventManager eventManager) {
		this.eventManager = eventManager;
	}

	public void goalReached(BBCDBossBar bossBar) {
		eventManager.goalReached(bossBar);
	}

}
