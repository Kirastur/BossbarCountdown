package de.polarwolf.bbcd.api;

import java.util.Set;

import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import de.polarwolf.bbcd.bossbars.BossBarManager;
import de.polarwolf.bbcd.config.ConfigManager;

public class BBCDAPI {
	
	protected final ConfigManager configManager;
	protected final BossBarManager bossBarManager;
	
	public BBCDAPI(ConfigManager configManager, BossBarManager bossBarManager) {
		this.configManager = configManager;
		this.bossBarManager = bossBarManager; 
	}
	
	
	// ConfigManager
	public BBCDTemplate findTemplate(String templateName) {
		return configManager.findTemplate(templateName);
	}
	
	public Set<BBCDTemplate> getTemplates() {
		return configManager.getTemplates();
	}
	
	public void setConfig (Plugin newPlugin, String newConfigPath) {
		configManager.setConfig(newPlugin, newConfigPath);
	}
	
	public void reload() throws BBCDException {
		configManager.reload();
	}


	// BossBarManager
	public BBCDBossBar findBbcdBossBar(Player player, BBCDTemplate bbcdTemplate) {
		return bossBarManager.findBbcdBossBar(player, bbcdTemplate);
	}
	
	public Set<BBCDBossBar> getBbcdBossBars() {
		return bossBarManager.getBbcdBossBars();				
	}
	
	public BBCDBossBar addBbcdBossBar(Player player, BBCDTemplate bbcdTemplate) {
		return bossBarManager.addBbcdBossBar(player, bbcdTemplate);
	}

	public void cancel(Player player) {
		bossBarManager.cancel(player);
	}

}
