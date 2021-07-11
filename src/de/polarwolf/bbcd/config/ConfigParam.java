package de.polarwolf.bbcd.config;

public enum ConfigParam {

	COLOR ("Color", "GREEN"),
	STYLE ("Style", "SOLID"),
	FLAGS ("Flags", ""),
	TITLE ("Title", ""),
	LEFT ("Left", "0.0"),
	RIGHT ("Right", "1.0"),
	START ("Start", "1.0"),
	SPEED ("Speed", "-0.01"),
	END ("End", "0.0"),
	AUTOSTART ("Autostart", "true"),
	SHOW ("Show", "true"),
	COMMAND ("Command", "");
	
	
	private final String attributeName;
	private final String defaultValue; 
	

	private ConfigParam(String attributeName, String defaultValue) {
		this.attributeName = attributeName;
		this.defaultValue = defaultValue;
	}


	public String getAttributeName() {
		return attributeName;
	}


	public String getDefaultValue() {
		return defaultValue;
	}
}
