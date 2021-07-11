package de.polarwolf.bbcd.api;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class BBCDGoalReachedEvent extends Event {
	
	private static final HandlerList handlers = new HandlerList();
	
	protected final BBCDBossBar bbcdBossBar;
	
	
	public BBCDGoalReachedEvent(BBCDBossBar bbcdBossBar) {
		this.bbcdBossBar = bbcdBossBar;
	}


	public BBCDBossBar getBbcdBossBar() {
		return bbcdBossBar;
	}


	@Override
	public HandlerList getHandlers() {
		return handlers;
	}
	

	public static HandlerList getHandlerList() {
	    return handlers;
	}

}