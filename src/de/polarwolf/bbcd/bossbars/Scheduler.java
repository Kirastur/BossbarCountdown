package de.polarwolf.bbcd.bossbars;

import org.bukkit.scheduler.BukkitRunnable;

public class Scheduler extends BukkitRunnable {
	
	protected BossBarManager bossBarManager;
	

	public Scheduler(BossBarManager bossBarManager) {
		this.bossBarManager = bossBarManager;
	}
	

	@Override
    public void run() {
		try {
			bossBarManager.handleTick();
		} catch (Exception e) {
			e.printStackTrace();
			cancel();
		}
    }

}
