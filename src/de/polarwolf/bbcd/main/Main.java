package de.polarwolf.bbcd.main;

import org.bstats.bukkit.Metrics;
import org.bukkit.ChatColor;
import org.bukkit.plugin.java.JavaPlugin;

import de.polarwolf.bbcd.api.BBCDOrchestrator;
import de.polarwolf.bbcd.commands.BBCDCommand;
import de.polarwolf.bbcd.commands.BBCDTabCompleter;
import de.polarwolf.bbcd.commands.Message;
import de.polarwolf.bbcd.exception.BBCDException;

public class Main extends JavaPlugin {

	// Please see modifications.ModificationManager for the Business-Logic

	public static final String TEMPLATE_SECTION = "templates";
	public static final String COMMAND_NAME = "bbcd";

	public static final int PLUGINID_BOSSBARCOUNTDOWN = 11904;

	protected BBCDOrchestrator orchestrator = null;
	protected BBCDCommand bbcdCommand = null;
	protected BBCDTabCompleter bbcdTabCompleter = null;

	@Override
	public void onEnable() {

		// Prepare Configuration
		saveDefaultConfig();

		// Startup Orchestrator
		try {
			orchestrator = new BBCDOrchestrator(null);
		} catch (BBCDException e) {
			e.printStackTrace();
			return;
		}

		// Register Command and TabCompleter
		new BBCDCommand(this, COMMAND_NAME);

		// Enable bStats Metrics
		// Please download the bstats-code direct form their homepage
		// or disable the following instruction
		new Metrics(this, PLUGINID_BOSSBARCOUNTDOWN);

		// Load Configuration Section
		try {
			orchestrator.getConfigManager().reload();
			int count = orchestrator.getConfigManager().getConfigTemplates().size();
			String s = String.format(Message.TEMPLATES_LOADED.toString(), count);
			getLogger().info(s);
		} catch (BBCDException e) {
			getServer().getConsoleSender().sendMessage(ChatColor.RED + "ERROR " + e.getMessage());
			getLogger().warning(Message.LOAD_ERROR.toString());
		}
	}

	@Override
	public void onDisable() {
		if (orchestrator != null) {
			orchestrator.disable();
			orchestrator = null;
		}
	}

}
