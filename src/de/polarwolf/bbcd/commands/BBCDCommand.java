package de.polarwolf.bbcd.commands;

import static de.polarwolf.bbcd.commands.ParamType.PLAYER;
import static de.polarwolf.bbcd.commands.ParamType.PROGRESS;
import static de.polarwolf.bbcd.commands.ParamType.SPEED;
import static de.polarwolf.bbcd.commands.ParamType.TEMPLATE;
import static de.polarwolf.bbcd.commands.ParamType.TITLE;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import de.polarwolf.bbcd.api.BBCDAPI;
import de.polarwolf.bbcd.api.BBCDProvider;
import de.polarwolf.bbcd.bossbars.BBCDBossBar;
import de.polarwolf.bbcd.config.BBCDTemplate;
import de.polarwolf.bbcd.exception.BBCDException;
import de.polarwolf.bbcd.main.Main;

public class BBCDCommand implements CommandExecutor {

	protected final Main main;
	protected final String commandName;
	protected final BBCDAPI bbcdAPI;
	protected final BBCDTabCompleter tabCompleter;

	public BBCDCommand(Main main, String commandName) {
		this.main = main;
		this.commandName = commandName;
		this.bbcdAPI = BBCDProvider.getAPI();
		main.getCommand(commandName).setExecutor(this);
		tabCompleter = new BBCDTabCompleter(main, this);
	}

	public String getCommandName() {
		return commandName;
	}

	protected void cmdHelp(CommandSender sender) {
		String s = String.join(" ", enumActions());
		sender.sendMessage(String.format(Message.HELP.toString(), s));
	}

	protected void cmdStart(BBCDTemplate template, Player player) throws BBCDException {
		BBCDBossBar bbcdBossBar = bbcdAPI.findBbcdBossBar(player, template);
		if (bbcdBossBar == null) {
			bbcdBossBar = bbcdAPI.addBbcdBossBar(player, template);
		}
		bbcdBossBar.start();
	}

	protected void cmdStop(BBCDTemplate template, Player player) {
		BBCDBossBar bbcdBossBar = bbcdAPI.findBbcdBossBar(player, template);
		if (bbcdBossBar == null) {
			bbcdBossBar = bbcdAPI.addBbcdBossBar(player, template);
		}
		bbcdBossBar.stop();
	}

	protected void cmdShow(BBCDTemplate template, Player player) {
		BBCDBossBar bbcdBossBar = bbcdAPI.findBbcdBossBar(player, template);
		if (bbcdBossBar == null) {
			bbcdBossBar = bbcdAPI.addBbcdBossBar(player, template);
		}
		bbcdBossBar.setVisible(true);
	}

	protected void cmdHide(BBCDTemplate template, Player player) {
		BBCDBossBar bbcdBossBar = bbcdAPI.findBbcdBossBar(player, template);
		if (bbcdBossBar == null) {
			bbcdBossBar = bbcdAPI.addBbcdBossBar(player, template);
		}
		bbcdBossBar.setVisible(false);
	}

	protected void cmdSetProgress(BBCDTemplate template, Player player, double newProgress) throws BBCDException {
		BBCDBossBar bbcdBossBar = bbcdAPI.findBbcdBossBar(player, template);
		if (bbcdBossBar == null) {
			throw new BBCDException(Message.BOSSBAR_NOT_FOUND);
		}
		bbcdBossBar.setProgress(newProgress);
	}

	protected void cmdSetSpeed(BBCDTemplate template, Player player, double newSpeed) throws BBCDException {
		BBCDBossBar bbcdBossBar = bbcdAPI.findBbcdBossBar(player, template);
		if (bbcdBossBar == null) {
			throw new BBCDException(Message.BOSSBAR_NOT_FOUND);
		}
		bbcdBossBar.setSpeed(newSpeed);
	}

	protected void cmdSetTitle(BBCDTemplate template, Player player, String newTitle) throws BBCDException {
		BBCDBossBar bbcdBossBar = bbcdAPI.findBbcdBossBar(player, template);
		if (bbcdBossBar == null) {
			throw new BBCDException(Message.BOSSBAR_NOT_FOUND);
		}
		bbcdBossBar.setTitle(newTitle);
	}

	protected void cmdCancel(BBCDTemplate template, Player player) {
		BBCDBossBar bbcdBossBar = bbcdAPI.findBbcdBossBar(player, template);
		if (bbcdBossBar != null) {
			bbcdBossBar.finish();
		}
	}

	protected void cmdList(CommandSender sender) {
		String s = String.join(", ", enumTemplates());
		if (s.isEmpty()) {
			s = Message.NO_TEMPLATES.toString();
		} else {
			s = String.format(Message.TEMPLATE_LIST.toString(), s);
		}
		sender.sendMessage(s);
	}

	protected void cmdInfo(CommandSender sender) {
		String s = String.join(", ", enumBossBars());
		if (s.isEmpty()) {
			s = Message.NO_BOSSBARS.toString();
		} else {
			s = String.format(Message.BOSSBAR_LIST.toString(), s);
		}
		sender.sendMessage(s);
	}

	protected void cmdReload(CommandSender sender) throws BBCDException {
		bbcdAPI.reload();
		int count = bbcdAPI.getTemplates().size();
		sender.sendMessage(Integer.toString(count) + Message.TEMPLATES_LOADED.toString());
	}

	protected void dispatchCommand(CommandSender sender, Action action, BBCDTemplate template, Player player,
			double progress, double speed, String title) {
		try {
			switch (action) {
			case HELP:
				cmdHelp(sender);
				break;
			case START:
				cmdStart(template, player);
				break;
			case STOP:
				cmdStop(template, player);
				break;
			case SHOW:
				cmdShow(template, player);
				break;
			case HIDE:
				cmdHide(template, player);
				break;
			case SETPROGRESS:
				cmdSetProgress(template, player, progress);
				break;
			case SETSPEED:
				cmdSetSpeed(template, player, speed);
				break;
			case SETTITLE:
				cmdSetTitle(template, player, title);
				break;
			case CANCEL:
				cmdCancel(template, player);
				break;
			case LIST:
				cmdList(sender);
				break;
			case INFO:
				cmdInfo(sender);
				break;
			case RELOAD:
				cmdReload(sender);
				break;
			default:
				sender.sendMessage(Message.ERROR.toString());
			}
		} catch (BBCDException be) {
			sender.sendMessage(be.getMessage());
		} catch (Exception e) {
			sender.sendMessage(Message.JAVA_EXCEPTOPN.toString());
			e.printStackTrace();
		}
	}

	public Action findAction(String actionName) {
		for (Action myAction : Action.values()) {
			if (myAction.getCommand().equalsIgnoreCase(actionName)) {
				return myAction;
			}
		}
		return null;
	}

	public List<String> enumActions() {
		List<String> actionNames = new ArrayList<>();
		for (Action myAction : Action.values()) {
			actionNames.add(myAction.getCommand());
		}
		return actionNames;
	}

	public BBCDTemplate findTemplate(String templateName) {
		for (BBCDTemplate myTemplate : bbcdAPI.getTemplates()) {
			if (myTemplate.getName().equals(templateName)) {
				return myTemplate;
			}
		}
		return null;
	}

	public List<String> enumTemplates() {
		List<String> templateNames = new ArrayList<>();
		for (BBCDTemplate myTemplate : bbcdAPI.getTemplates()) {
			templateNames.add(myTemplate.getName());
		}
		return templateNames;
	}

	protected BBCDTemplate parseTemplate(Action action, String[] args) throws BBCDException {
		int templatePosition = action.findPosition(TEMPLATE);
		if (templatePosition == 0) {
			return null;
		}
		if (args.length < templatePosition + 1) {
			throw new BBCDException(Message.MISSING_TEMPLATE);
		}
		String templateName = args[templatePosition];
		BBCDTemplate template = findTemplate(templateName);
		if (template == null) {
			throw new BBCDException(Message.UNKNOWN_TEMPLATE);
		}
		return template;
	}

	protected Player parsePlayer(Action action, String[] args) throws BBCDException {
		int playerPosition = action.findPosition(PLAYER);
		if (playerPosition == 0) {
			return null;
		}
		if (args.length < playerPosition + 1) {
			throw new BBCDException(Message.MISSING_PLAYER);
		}
		String playerName = args[playerPosition];
		Player player = main.getServer().getPlayer(playerName);
		if (player == null) {
			throw new BBCDException(Message.UNKNOWN_PLAYER);
		}
		return player;
	}

	protected double parseNumeric(Action action, ParamType param, String[] args) throws BBCDException {
		int valuePosition = action.findPosition(param);
		if (valuePosition == 0) {
			return 0.0;
		}
		if (args.length < valuePosition + 1) {
			throw new BBCDException(Message.MISSING_VALUE);
		}
		String valueName = args[valuePosition];
		double value;
		try {
			value = Double.parseDouble(valueName);
		} catch (NumberFormatException e) {
			throw new BBCDException(Message.VALUE_NOT_NUMERIC);
		}
		return value;
	}

	protected String parseTitle(Action action, String[] args) throws BBCDException {
		int titlePosition = action.findPosition(TITLE);
		if (titlePosition == 0) {
			return null;
		}
		if (args.length < titlePosition + 1) {
			throw new BBCDException(Message.MISSING_TITLE);
		}
		return String.join(" ", Arrays.copyOfRange(args, titlePosition, args.length));
	}

	public List<String> enumBossBars() {
		List<String> bossBarNames = new ArrayList<>();
		for (BBCDBossBar myBossBar : bbcdAPI.getBbcdBossBars()) {
			bossBarNames.add(myBossBar.getName());
		}
		return bossBarNames;
	}

	protected boolean handleCommand(CommandSender sender, String[] args) {
		Action action;
		BBCDTemplate template;
		Player player;
		double progress;
		double speed;
		String title;

		if (args.length == 0) {
			return false;
		}

		try {
			String actionName = args[0];
			action = findAction(actionName);
			if (action == null) {
				throw new BBCDException(Message.UNKNOWN_ACTION);
			}
			if ((args.length - 1 > action.getParamCount()) && (action.findPosition(TITLE) == 0)) {
				throw new BBCDException(Message.TOO_MANY_PARAMETERS);
			}
			template = parseTemplate(action, args);
			player = parsePlayer(action, args);
			progress = parseNumeric(action, PROGRESS, args);
			speed = parseNumeric(action, SPEED, args);
			title = parseTitle(action, args);
		} catch (BBCDException e) {
			sender.sendMessage(e.getMessage());
			return true;
		}

		dispatchCommand(sender, action, template, player, progress, speed, title);
		return true;
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		try {
			return handleCommand(sender, args);
		} catch (Exception e) {
			e.printStackTrace();
			sender.sendMessage(Message.JAVA_EXCEPTOPN.toString());
		}
		return true;
	}

}
