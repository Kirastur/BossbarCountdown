package de.polarwolf.bbcd.commands;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import de.polarwolf.bbcd.api.BBCDAPI;
import de.polarwolf.bbcd.api.BBCDBossBar;
import de.polarwolf.bbcd.api.BBCDException;
import de.polarwolf.bbcd.api.BBCDTemplate;
import de.polarwolf.bbcd.main.Main;

public class BBCDCommand implements CommandExecutor {
	
	protected final Main main;
	protected final BBCDAPI bbcdAPI;
	

	public BBCDCommand(Main main, BBCDAPI bbcdAPI) {
		this.main = main;
		this.bbcdAPI = bbcdAPI;
	}
	

	public List<String> listCommands() {
		List<String> names = new ArrayList<>();
		for (SubCommand subCommand : SubCommand.values()) {
			names.add(subCommand.getCommand());
		}
		return names;
	}

	
	public List<String> listTemplates() {
		List<String> names = new ArrayList<>();
		for (BBCDTemplate template: bbcdAPI.getTemplates()) {
			names.add(template.getName());
		}
		return names;
	}
	
	
	public List<String> listBossBars() {
		List<String> names = new ArrayList<>();
		for (BBCDBossBar bbcdBossBar: bbcdAPI.getBbcdBossBars()) {
			names.add(bbcdBossBar.getName());
		}
		return names;
	}
	

	protected void cmdHelp(CommandSender sender) {
		String s = String.join(" ", listCommands());
		sender.sendMessage(Message.HELP.toString() + s);
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

	
	protected void cmdSetProgress(CommandSender sender, BBCDTemplate template, Player player, String value) throws BBCDException {
		BBCDBossBar bbcdBossBar = bbcdAPI.findBbcdBossBar(player, template);
		if (bbcdBossBar == null) {
			sender.sendMessage(Message.BOSSBAR_NOT_FOUND.toString());
			return;
		}
		
		try {
			double newProgress = Double.parseDouble(value);
			bbcdBossBar.setProgress(newProgress);
		} catch (NumberFormatException e) {
			sender.sendMessage(Message.VALUE_NOT_NUMERIC.toString());
		}	
	}
	
	
	protected void cmdSetSpeed(CommandSender sender, BBCDTemplate template, Player player, String value) {
		BBCDBossBar bbcdBossBar = bbcdAPI.findBbcdBossBar(player, template);
		if (bbcdBossBar == null) {
			sender.sendMessage(Message.BOSSBAR_NOT_FOUND.toString());
			return;
		}
		
		try {
			double newSpeed = Double.parseDouble(value);
			bbcdBossBar.setSpeed(newSpeed);
		} catch (NumberFormatException e) {
			sender.sendMessage(Message.VALUE_NOT_NUMERIC.toString());
		}
		
	}
	
	
	protected void cmdSetTitle(CommandSender sender, BBCDTemplate template, Player player, String value) {
		BBCDBossBar bbcdBossBar = bbcdAPI.findBbcdBossBar(player, template);
		if (bbcdBossBar == null) {
			sender.sendMessage(Message.BOSSBAR_NOT_FOUND.toString());
			return;
		}
		
		bbcdBossBar.setTitle(value);		
	}

	
	protected void cmdCancel(BBCDTemplate template, Player player) {
		BBCDBossBar bbcdBossBar = bbcdAPI.findBbcdBossBar(player, template);
		if (bbcdBossBar != null) {
			bbcdBossBar.finish();
		}
	}

	
	protected void cmdList(CommandSender sender) {
		String s = String.join(", ", listTemplates());
		if (!s.isEmpty()) {
			s = Message.TEMPLATE_LIST.toString() + s;
		} else {
			s = Message.NO_TEMPLATES.toString();
		}
		sender.sendMessage(s);		
	}
				
		
	protected void cmdInfo(CommandSender sender) {
		String s = String.join(", ", listBossBars());
		if (!s.isEmpty()) {
			s = Message.BOSSBAR_LIST.toString() + s;
		} else {
			s = Message.NO_BOSSBARS.toString();
		}
		sender.sendMessage(s);		
	}
		

	protected void cmdReload(CommandSender sender) throws BBCDException {
		bbcdAPI.reload();
		int count = bbcdAPI.getTemplates().size(); 
		sender.sendMessage(Integer.toString(count) + Message.TEMPLATES_LOADED.toString());
	}


	protected void dispatchCommand(CommandSender sender, SubCommand subCommand, BBCDTemplate template, Player player, String value) {
		try {
			switch (subCommand) {
				case HELP:		cmdHelp(sender);
								break;
				case START:		cmdStart(template, player);
								break;
				case STOP:		cmdStop(template, player);
								break;
				case SHOW:		cmdShow(template, player);
								break;
				case HIDE:		cmdHide(template, player);
								break;
				case SETPROGRESS: cmdSetProgress(sender, template, player, value);
								break;
				case SETSPEED:	cmdSetSpeed(sender, template, player, value);
								break;
				case SETTITLE:	cmdSetTitle(sender, template, player, value);
								break;
				case CANCEL:	cmdCancel(template, player);
								break;
				case LIST:		cmdList(sender);
								break;
				case INFO:		cmdInfo(sender);
								break;
				case RELOAD:	cmdReload(sender);
								break;
				default: sender.sendMessage(Message.ERROR.toString());
			}
		} catch (BBCDException e) {
			main.getLogger().warning(Message.ERROR.toString()+ " " + e.getMessage());
			sender.sendMessage(e.getMessage());
		}		
	}
	
	
	public SubCommand findSubCommand(String subCommandName) {
		for (SubCommand subCommand : SubCommand.values()) {
			if (subCommand.getCommand().equalsIgnoreCase(subCommandName)) {
				return subCommand;
			}
		}
		return null;
	}
	
	
	protected boolean handleCommand(CommandSender sender, String[] args) {
		if (args.length==0) {
			return false;
		}

		String subCommandName=args[0];
		SubCommand subCommand = findSubCommand(subCommandName);
		if (subCommand == null) {
			sender.sendMessage(Message.UNKNOWN_PARAMETER.toString());
			return true;
		}
		
		BBCDTemplate myTemplate = null;
		Player myPlayer = null;
		String myValue = "";
		
		if (subCommand.hasParseTemplate()) {
			if (args.length < 2) {
				sender.sendMessage(Message.MISSING_TEMPLATE.toString());
				return true;
			}
			myTemplate = bbcdAPI.findTemplate(args[1]);
			if (myTemplate == null) {
				sender.sendMessage(Message.UNKNOWN_TEMPLATE.toString());
				return true;
			}
		} else {
			if (args.length > 1) {
				sender.sendMessage(Message.TOO_MANY_PARAMETERS.toString());
				return true;
			}			
		}

		if (subCommand.hasParsePlayer()) {
			if (args.length < 3) {
				sender.sendMessage(Message.MISSING_PLAYER.toString());
				return true;
			}
			myPlayer = main.getServer().getPlayer(args[2]);
			if (myPlayer == null) {
				sender.sendMessage(Message.UNKNOWN_PLAYER.toString());
				return true;
			}
		} else {
			if (args.length > 2) {
				sender.sendMessage(Message.TOO_MANY_PARAMETERS.toString());
				return true;
			}			
		}
	
		if (subCommand.hasParseValue()) {
			if (args.length < 4) {
				sender.sendMessage(Message.MISSING_VALUE.toString());
				return true;
			}
			myValue = args[3];
		} else {
			if (args.length > 3) {
				sender.sendMessage(Message.TOO_MANY_PARAMETERS.toString());
				return true;
			}			
		}
		
		if (subCommand.hasParseInfiniteString()) {
			myValue = String.join(" ", Arrays.copyOfRange(args, 3, args.length));
		} else {
			if (args.length > 4) {
				sender.sendMessage(Message.TOO_MANY_PARAMETERS.toString());
				return true;
			}			
		}

		dispatchCommand (sender, subCommand, myTemplate, myPlayer, myValue);
			
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
