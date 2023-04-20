package de.polarwolf.bbcd.api;

public class BBCDProvider {

	private static BBCDAPI bbcdAPI;

	private BBCDProvider() {
	}

	public static BBCDAPI getAPI() {
		return bbcdAPI;
	}

	static boolean clearAPI() {
		if (bbcdAPI == null) {
			return true;
		}
		if (!bbcdAPI.isDisabled()) {
			return false;
		}
		bbcdAPI = null;
		return true;
	}

	static boolean setAPI(BBCDAPI newAPI) {
		if (!clearAPI()) {
			return false;
		}
		bbcdAPI = newAPI;
		return true;
	}

}
