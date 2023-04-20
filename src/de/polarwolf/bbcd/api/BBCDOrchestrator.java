package de.polarwolf.bbcd.api;

import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;

import de.polarwolf.bbcd.bossbars.BossBarManager;
import de.polarwolf.bbcd.config.ConfigManager;
import de.polarwolf.bbcd.events.EventManager;
import de.polarwolf.bbcd.exception.BBCDException;
import de.polarwolf.bbcd.listener.ListenManager;

public class BBCDOrchestrator {

	public static final String PLUGIN_NAME = "BossbarCountdown";

	private final Plugin plugin;
	private final ConfigManager configManager;
	private final EventManager eventManager;
	private final BossBarManager bossBarManager;
	private final ListenManager listenManager;

	public BBCDOrchestrator(Plugin plugin) throws BBCDException {
		if (plugin != null) {
			this.plugin = plugin;
		} else {
			this.plugin = Bukkit.getPluginManager().getPlugin(PLUGIN_NAME);
		}
		if (!BBCDProvider.clearAPI()) {
			throw new BBCDException(this.plugin.getName(),
					"Can't start orchestrator, another instance is already running", null);
		}
		configManager = createConfigManager();
		eventManager = createEventManager();
		bossBarManager = createBossBarManager();
		listenManager = createListenManager();
		BBCDProvider.setAPI(createAPI());
	}

	// Getter
	public Plugin getPlugin() {
		return plugin;
	}

	public ConfigManager getConfigManager() {
		return configManager;
	}

	public EventManager getEventManager() {
		return eventManager;
	}

	public BossBarManager getBossBarManager() {
		return bossBarManager;
	}

	public ListenManager getListenManager() {
		return listenManager;
	}

	// Creator
	protected ConfigManager createConfigManager() {
		return new ConfigManager(this);
	}

	protected EventManager createEventManager() {
		return new EventManager(this);
	}

	protected BossBarManager createBossBarManager() {
		return new BossBarManager(this);
	}

	protected ListenManager createListenManager() {
		return new ListenManager(this);
	}

	protected BBCDAPI createAPI() {
		return new BBCDAPI(this);
	}

	// Disable
	public void disable() {
		listenManager.unregisterListener();
		bossBarManager.disable();
		BBCDProvider.clearAPI();
	}

	public boolean isDisabled() {
		return bossBarManager.isDisabled();
	}

}
