package de.polarwolf.bbcd.main;

import de.polarwolf.bbcd.api.BBCDAPI;

public class BBCDProvider {

	private static BBCDAPI bbcdAPI;
	
	private BBCDProvider() {
	}

	protected static void setAPI (BBCDAPI newAPI) {
		bbcdAPI=newAPI;
	}
		    
	public static BBCDAPI getAPI() {
		return bbcdAPI;
	}

}
