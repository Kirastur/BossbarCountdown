package de.polarwolf.bbcd.commands;

import static de.polarwolf.bbcd.commands.ParamType.NONE;
import static de.polarwolf.bbcd.commands.ParamType.PLAYER;
import static de.polarwolf.bbcd.commands.ParamType.PROGRESS;
import static de.polarwolf.bbcd.commands.ParamType.SPEED;
import static de.polarwolf.bbcd.commands.ParamType.TEMPLATE;
import static de.polarwolf.bbcd.commands.ParamType.TITLE;

import java.util.ArrayList;
import java.util.List;

public enum Action {

	HELP("help", NONE, NONE, NONE, NONE),
	START("start", TEMPLATE, PLAYER, NONE, NONE),
	STOP("stop", TEMPLATE, PLAYER, NONE, NONE),
	SHOW("show", TEMPLATE, PLAYER, NONE, NONE),
	HIDE("hide", TEMPLATE, PLAYER, NONE, NONE),
	SETPROGRESS("setprogress", TEMPLATE, PLAYER, PROGRESS, NONE),
	SETSPEED("setspeed", TEMPLATE, PLAYER, SPEED, NONE),
	SETTITLE("settitle", TEMPLATE, PLAYER, TITLE, NONE),
	CANCEL("cancel", TEMPLATE, PLAYER, NONE, NONE),
	LIST("list", NONE, NONE, NONE, NONE),
	INFO("info", NONE, NONE, NONE, NONE),
	RELOAD("reload", NONE, NONE, NONE, NONE);

	private final String command;
	private List<ParamType> params = new ArrayList<>();

	private Action(String command, ParamType param1, ParamType param2, ParamType param3, ParamType param4) {
		this.command = command;
		if (param1 != NONE) {
			params.add(param1);
		}
		if (param2 != NONE) {
			params.add(param2);
		}
		if (param3 != NONE) {
			params.add(param3);
		}
		if (param4 != NONE) {
			params.add(param4);
		}
	}

	public String getCommand() {
		return command;
	}

	public int getParamCount() {
		return params.size();
	}

	public ParamType getParam(int position) {
		if ((position < 1) || (position > params.size())) {
			return NONE;
		}
		return params.get(position - 1);
	}

	public int findPosition(ParamType param) {
		for (int i = 0; i < params.size(); i++) {
			if (params.get(i) == param) {
				return i + 1;
			}
		}
		return 0;
	}

}