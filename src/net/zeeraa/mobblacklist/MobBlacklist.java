package net.zeeraa.mobblacklist;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntitySpawnEvent;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

public class MobBlacklist extends JavaPlugin implements Listener {
	private static MobBlacklist instance;
	private List<EntityType> blacklist;

	/**
	 * Get the instance of this plugin
	 * 
	 * @return plugin instance
	 */
	public static MobBlacklist getInstance() {
		return instance;
	}

	/**
	 * Get a list containing blacklisted {@link EntityType}s
	 * 
	 * @return {@link EntityType} blacklist
	 */
	public List<EntityType> getBlacklist() {
		return blacklist;
	}

	@Override
	public void onEnable() {
		MobBlacklist.instance = this;
		saveDefaultConfig();

		this.blacklist = new ArrayList<EntityType>();

		boolean configChanged = false;
		for (EntityType type : EntityType.values()) {
			if (!this.getConfig().contains(type.name())) {
				configChanged = true;
				this.getConfig().set(type.name(), false);
			}

			if (this.getConfig().getBoolean(type.name())) {
				blacklist.add(type);
			}
		}

		if (configChanged) {
			this.saveConfig();
		}

		Bukkit.getServer().getPluginManager().registerEvents(this, this);

		getLogger().info(blacklist.size() + " entity type" + (blacklist.size() == 1 ? "" : "s") + " have been blacklisted");
	}

	@Override
	public void onDisable() {
		HandlerList.unregisterAll((Plugin) this);
		blacklist.clear();
	}

	@EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
	public void onEntitySpawn(EntitySpawnEvent e) {
		if (blacklist.contains(e.getEntityType())) {
			e.setCancelled(true);
		}
	}
}