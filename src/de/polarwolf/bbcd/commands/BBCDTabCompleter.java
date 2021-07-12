package de.polarwolf.bbcd.commands;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;


public class BBCDTabCompleter implements TabCompleter{
	
	protected final BBCDCommand bbcdCommand;
	
	public BBCDTabCompleter(BBCDCommand bbcdCommand) {
		this.bbcdCommand = bbcdCommand;
	}
		

	protected List<String> handleTabComplete(String[] args) {
		if (args.length == 0) {
			return new ArrayList<>();			
		}

		if (args.length==1) {
			return bbcdCommand.listCommands();
		}
		
		String subCommandName = args[0];
		SubCommand subCommand = bbcdCommand.findSubCommand(subCommandName);
		if (subCommand == null) {
			return new ArrayList<>();			
		}
		
		if ((args.length == 2) && subCommand.hasParseTemplate()) {
			return bbcdCommand.listTemplates();
		}
		
		if ((args.length == 3) && subCommand.hasParsePlayer()) {
			return null; // This automatically lists players
		}
		
		
		if ((args.length == 4) && subCommand.hasParseValue() && !subCommand.hasParseInfiniteString()) {
			List<String> myExamples = new ArrayList<>();
			myExamples.add("1.0");
			return myExamples;
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
