package de.polarwolf.bbcd.commands;

public enum Message {

	OK ("OK"),
	ERROR ("ERROR"),
	JAVA_EXCEPTOPN ("Java Exception Error"),
	HELP ("Valid commands are: "),
	BOSSBAR_NOT_FOUND ("Bossbar not found"),
	VALUE_NOT_NUMERIC ("Value is not numeric"),
	TEMPLATE_LIST ("Available templates are: "),
	NO_TEMPLATES ("There are no templates avail"),
	BOSSBAR_LIST ("Active Bossbars are: "),
	NO_BOSSBARS ("There are no active BossBars"),
	TEMPLATES_LOADED (" templates loaded"),
	UNKNOWN_PARAMETER ("Unknown parameter"),
	TOO_MANY_PARAMETERS ("Too many parameters"),
	MISSING_TEMPLATE ("Template name is missing"),
	MISSING_PLAYER ("Player name is missing"),
	MISSING_VALUE ("Value is missing"),
	UNKNOWN_TEMPLATE ("Unknown template"),
	UNKNOWN_PLAYER ("Unknown player"),
	LOAD_ERROR ("Error loading configuration");
	
	
	private final String messageText;
	

	private Message(String messageText) {
		this.messageText = messageText;
	}

	
	@Override
	public String toString() {
		return messageText;
	}

}
