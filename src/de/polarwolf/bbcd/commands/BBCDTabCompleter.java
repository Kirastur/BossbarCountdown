package de.polarwolf.bbcd.commands;

import static de.polarwolf.bbcd.commands.ParamType.PLAYER;
import static de.polarwolf.bbcd.commands.ParamType.PROGRESS;
import static de.polarwolf.bbcd.commands.ParamType.SPEED;
import static de.polarwolf.bbcd.commands.ParamType.TEMPLATE;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;

import de.polarwolf.bbcd.main.Main;

public class BBCDTabCompleter implements TabCompleter {

	protected final BBCDCommand bbcdCommand;

	public BBCDTabCompleter(Main main, BBCDCommand bbcdCommand) {
		this.bbcdCommand = bbcdCommand;
		main.getCommand(bbcdCommand.getCommandName()).setTabCompleter(this);
	}

	protected List<String> listActions() {
		return bbcdCommand.enumActions();
	}

	protected List<String> listTemplates() {
		return bbcdCommand.enumTemplates();
	}

	protected List<String> handleTabComplete(String[] args) {
		if (args.length < 1) {
			return new ArrayList<>();
		}
		if (args.length == 1) {
			return listActions();
		}

		String actionName = args[0];
		Action action = bbcdCommand.findAction(actionName);
		if ((action == null) || (args.length - 1 > action.getParamCount())) {
			return new ArrayList<>();
		}

		ParamType paramType = action.getParam(args.length - 1);
		if (paramType == TEMPLATE) {
			return listTemplates();
		}
		if (paramType == PLAYER) {
			return null; // NOSONAR
		}
		if ((paramType == PROGRESS) || (paramType == SPEED)) {
			return Arrays.asList("1.0");
		}
		return new ArrayList<>();
	}

	@Override
	public List<String> onTabComplete(CommandSender sender, Command cmd, String label, String[] args) {
		try {
			return handleTabComplete(args);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return new ArrayList<>();
	}

}
