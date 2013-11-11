package com.minecraftdimensions.factionscontrol;




import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

public class FactionsControl extends JavaPlugin {


	public static Plugin instance;

	@Override
	public void onEnable() {
		instance = this;
		TeamManager.initialise();
		getServer().getPluginManager().registerEvents(
				new BreakBlockEventListener(), this);
		getServer().getPluginManager().registerEvents(
				new PlaceBlockEventListener(), this);
		getServer().getPluginManager().registerEvents(
				new InteractEventListener(), this);
		getServer().getPluginManager().registerEvents(
				new CreatureSpawnEventListener(), this);
		getServer().getPluginManager().registerEvents(
				new PlayerDamageEventListener(), this);
		getServer().getPluginManager().registerEvents(
				new PlayerTeleportEventListener(), this);
		getServer().getPluginManager().registerEvents(
				new FactionTeamListener(), this);
		getServer().getPluginManager().registerEvents(
				new DisguiseEvents(), this);
	}


}
