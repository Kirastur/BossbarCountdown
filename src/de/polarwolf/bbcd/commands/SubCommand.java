package de.polarwolf.bbcd.commands;

public enum SubCommand {

	HELP ("help", false, false, false, false),
	START ("start", true, true, false, false),
	STOP ("stop", true, true, false, false),
	SHOW ("show", true, true, false, false),
	HIDE ("hide", true, true, false, false),
	SETPROGRESS ("setprogress", true, true, true, false),
	SETSPEED ("setspeed", true, true, true, false),
	SETTITLE ("settitle", true, true, true, true),
	CANCEL ("cancel", true, true, false, false),
	LIST ("list", false, false, false, false),
	INFO ("info", false, false, false, false),
	RELOAD ("reload", false, false, false, false);


	private final String command;
	private final boolean parseTemplate;
	private final boolean parsePlayer;
	private final boolean parseValue;
	private final boolean parseInfiniteString;
	

	private SubCommand(String command, boolean parseTemplate, boolean parsePlayer, boolean parseValue, boolean parseInfiniteString) {
		this.command = command;
		this.parseTemplate = parseTemplate;
		this.parsePlayer = parsePlayer;
		this.parseValue = parseValue;
		this.parseInfiniteString = parseInfiniteString;
	}


	public String getCommand() {
		return command;
	}


	public boolean hasParseTemplate() {
		return parseTemplate;
	}


	public boolean hasParsePlayer() {
		return parsePlayer;
	}


	public boolean hasParseValue() {
		return parseValue;
	}


	public boolean hasParseInfiniteString() {
		return parseInfiniteString;
	}
}
