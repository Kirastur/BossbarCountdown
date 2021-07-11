package de.polarwolf.bbcd.main;

import org.bukkit.ChatColor;
import org.bukkit.plugin.java.JavaPlugin;

import de.polarwolf.bbcd.api.BBCDAPI;
import de.polarwolf.bbcd.api.BBCDException;
import de.polarwolf.bbcd.bossbars.BossBarManager;
import de.polarwolf.bbcd.bstats.Metrics;
import de.polarwolf.bbcd.commands.BBCDCommand;
import de.polarwolf.bbcd.commands.BBCDTabCompleter;
import de.polarwolf.bbcd.commands.Message;
import de.polarwolf.bbcd.config.ConfigManager;
import de.polarwolf.bbcd.listener.ListenManager;


public class Main extends JavaPlugin {

	// Please see modifications.ModificationManager for the Business-Logic
	
	public static final String TEMPLATE_SECTION = "templates";
	public static final String COMMAND_NAME = "bbcd";
	
	protected ConfigManager configManager = null;
	protected BossBarManager bossBarManager = null;
	protected ListenManager listenManager = null;
	protected BBCDAPI bbcdAPI = null;
	protected BBCDCommand bbcdCommand = null;
	protected BBCDTabCompleter bbcdTabCompleter = null;

	@Override
	public void onEnable() {
		
		// Prepare Configuration
		saveDefaultConfig();
		
		// Create ConfigManager
		configManager = new ConfigManager(this, TEMPLATE_SECTION);
		
		// Create BossBarManager
		bossBarManager = new BossBarManager(this, configManager);
		
		// Register EventHandler (Listener)
		listenManager = new ListenManager (this, bossBarManager);
		listenManager.registerListener();
	    
	    // Initialize the API
	    bbcdAPI = new BBCDAPI(configManager, bossBarManager);
		BBCDProvider.setAPI(bbcdAPI);
		
		// Register Command and TabCompleter
		bbcdCommand = new BBCDCommand(this, bbcdAPI);
		getCommand(COMMAND_NAME).setExecutor(bbcdCommand);
		bbcdTabCompleter = new BBCDTabCompleter(bbcdCommand);
		getCommand(COMMAND_NAME).setTabCompleter(bbcdTabCompleter);
		
		// Enable bStats Metrics
		// Please download the bstats-code direct form their homepage
		// or disable the following instruction
		new Metrics(this, Metrics.PLUGINID_BOSSBARCOUNTDOWN);

		// Load Configuration Section
		try {
			configManager.reload();
			int count = configManager.getTemplates().size();
			getLogger().info(Integer.toString(count) + Message.TEMPLATES_LOADED.toString());
		} catch (BBCDException e) {
			getServer().getConsoleSender().sendMessage(ChatColor.RED + "ERROR " + e.getMessage());
			getLogger().warning(Message.LOAD_ERROR.toString());
		}
	}
	
	@Override
	public void onDisable() {
		BBCDProvider.setAPI(null);

		if (listenManager != null) {
			listenManager.unregisterListener();
		}
	}
	
}
