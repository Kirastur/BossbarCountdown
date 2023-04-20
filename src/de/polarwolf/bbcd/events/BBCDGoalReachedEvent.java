package de.polarwolf.bbcd.events;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import de.polarwolf.bbcd.bossbars.BBCDBossBar;

public class BBCDGoalReachedEvent extends Event {

	private static final HandlerList handlers = new HandlerList();

	protected final BBCDBossBar bbcdBossBar;

	protected BBCDGoalReachedEvent(BBCDBossBar bbcdBossBar) {
		this.bbcdBossBar = bbcdBossBar;
	}

	public BBCDBossBar getBbcdBossBar() {
		return bbcdBossBar;
	}

	@Override
	public HandlerList getHandlers() {
		return getHandlerList();
	}

	public static HandlerList getHandlerList() {
		return handlers;
	}

}