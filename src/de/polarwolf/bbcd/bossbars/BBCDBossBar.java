package de.polarwolf.bbcd.bossbars;

import org.bukkit.boss.BarFlag;
import org.bukkit.boss.BossBar;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import de.polarwolf.bbcd.config.BBCDTemplate;
import de.polarwolf.bbcd.events.EventHelper;
import de.polarwolf.bbcd.exception.BBCDException;

public class BBCDBossBar {

	protected final Plugin plugin;
	protected final Player player;
	protected final BossBar bossBar;
	protected final BBCDTemplate bbcdTemplate;

	protected final double distance;

	private EventHelper eventHelper;
	protected double progress;
	protected double speed;
	protected boolean running;

	private boolean finished = false;

	public BBCDBossBar(Plugin plugin, Player player, BBCDTemplate bbcdTemplate) {
		this.plugin = plugin;
		this.player = player;
		this.bbcdTemplate = bbcdTemplate;

		this.bossBar = createBossbar(plugin);

		this.distance = bbcdTemplate.getRight() - bbcdTemplate.getLeft();
		this.speed = bbcdTemplate.getSpeed();
		this.progress = bbcdTemplate.getStart();
		this.running = bbcdTemplate.isAutostart();

		updateBossBar();
		bossBar.setVisible(bbcdTemplate.isShow());
		bossBar.addPlayer(player);
	}

	protected BossBar createBossbar(Plugin plugin) {
		BarFlag[] myEmptyFlagArray = {};
		BossBar myBossBar = plugin.getServer().createBossBar(bbcdTemplate.getTitle(), bbcdTemplate.getColor(),
				bbcdTemplate.getStyle(), myEmptyFlagArray);
		for (BarFlag myFlag : bbcdTemplate.getFlags()) {
			myBossBar.addFlag(myFlag);
		}
		return myBossBar;
	}

	protected EventHelper getEventHelper() {
		return eventHelper;
	}

	final void setEventHelper(EventHelper eventHelper) {
		this.eventHelper = eventHelper;
	}

	public String getName() {
		return bbcdTemplate.getName() + "." + player.getName();
	}

	protected double normalizeProgress(double value) {
		double normalizedProgress = (value - bbcdTemplate.getLeft()) / distance;
		if (normalizedProgress < 0.0) {
			normalizedProgress = 0.0;
		}
		if (normalizedProgress > 1.0) {
			normalizedProgress = 1.0;
		}
		return normalizedProgress;
	}

	protected void updateBossBar() {
		bossBar.setProgress(normalizeProgress(progress));
	}

	protected boolean hasReachedEnd() {
		if (speed < 0) {
			return (progress <= bbcdTemplate.getEnd());
		}
		if (speed > 0) {
			return (progress >= bbcdTemplate.getEnd());
		}
		return false;
	}

	public double getProgress() {
		return progress;
	}

	public void setProgress(double progress) throws BBCDException {
		if (finished) {
			throw new BBCDException(getName(), "Bossbar already finished", null);
		}

		this.progress = progress;
		updateBossBar();
		if (hasReachedEnd()) {
			handleEndGoal();
		}
	}

	public void setInitialProgress() throws BBCDException {
		setProgress(bbcdTemplate.getStart());
	}

	public double getSpeed() {
		return speed;
	}

	public void setSpeed(double speed) {
		this.speed = speed;
	}

	public String getTitle() {
		return bossBar.getTitle();
	}

	public void setTitle(String title) {
		bossBar.setTitle(title);
	}

	public boolean isRunning() {
		return running;
	}

	public boolean isFinished() {
		return finished;
	}

	public void start() throws BBCDException {
		if (finished) {
			throw new BBCDException(getName(), "Bossbar already finished", null);
		}
		running = true;
	}

	public void stop() {
		running = false;
	}

	public void finish() {
		running = false;
		finished = true;
		bossBar.removeAll();
	}

	public Player getPlayer() {
		return player;
	}

	public BBCDTemplate getBbcdTemplate() {
		return bbcdTemplate;
	}

	public boolean isVisible() {
		return bossBar.isVisible();
	}

	public void setVisible(boolean newVisible) {
		bossBar.setVisible(newVisible);
	}

	protected void incrementProgress() throws BBCDException {
		double newProgress = progress + speed;
		if (bbcdTemplate.getLeft() < bbcdTemplate.getRight()) {
			if (newProgress < bbcdTemplate.getLeft()) {
				newProgress = bbcdTemplate.getLeft();
			}
			if (newProgress > bbcdTemplate.getRight()) {
				newProgress = bbcdTemplate.getRight();
			}
		} else {
			if (newProgress > bbcdTemplate.getLeft()) {
				newProgress = bbcdTemplate.getLeft();
			}
			if (newProgress < bbcdTemplate.getRight()) {
				newProgress = bbcdTemplate.getRight();
			}
		}
		setProgress(newProgress);
	}

	public void handleTick() throws BBCDException {
		if (running && !finished) {
			incrementProgress();
		}
	}

	protected void executeEndGoalCommand() throws BBCDException {
		String commandString = bbcdTemplate.getCommand();
		if ((commandString == null) || (commandString.isEmpty())) {
			return;
		}

		commandString = commandString.replaceAll("(?i)%player%", player.getName());
		commandString = commandString.replaceAll("(?i)%displayname%", player.getDisplayName());
		commandString = commandString.replaceAll("(?i)%uuid%", player.getUniqueId().toString());

		CommandSender sender = plugin.getServer().getConsoleSender();
		boolean result = plugin.getServer().dispatchCommand(sender, commandString);
		if (!result) {
			throw new BBCDException(getName(), "Error executing command", commandString);
		}
	}

	protected void handleEndGoal() throws BBCDException {
		finish();
		try {
			executeEndGoalCommand();
		} finally {
			eventHelper.goalReached(this);
		}
	}

}
