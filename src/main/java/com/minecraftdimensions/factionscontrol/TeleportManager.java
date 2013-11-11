package com.minecraftdimensions.factionscontrol;

import java.util.ArrayList;
import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import com.massivecraft.factions.Rel;
import com.massivecraft.factions.entity.Faction;
import com.massivecraft.factions.entity.UPlayer;

public class TeleportManager {
	public static ArrayList<Player> ignore = new ArrayList<>();
	public static HashMap<Player,String> pendingTeleportCommands = new HashMap<>();

	public static void setPlayerPending(final UPlayer p, String command, final String args, final boolean facCommand) {
		if(pendingTeleportCommands.containsKey(p.getPlayer())){
			p.sendMessage(ChatColor.RED+"You already have a teleport pending!");
			return;
		}else{
			int timer=0;
			Faction host =  FactionManager.getFactionAt(p.getPlayer().getLocation().getBlock());
			Rel relation = host.getRelationTo(p);
			if(relation==Rel.ENEMY){
				timer = 200;
				p.sendMessage(ChatColor.RED+"Commencing teleport from enemy land in 10 seconds");
			}else if(host.getName().equalsIgnoreCase("Warzone")){
				timer = 100;
				p.sendMessage(ChatColor.RED+"Commencing teleport from warzone in 5 seconds");
			}
			if(facCommand){
				command = "f "+command;
			}
			pendingTeleportCommands.put(p.getPlayer(), command);
			Bukkit.getScheduler().runTaskLater(FactionsControl.instance, new Runnable(){

				@Override
				public void run() {
					if(pendingTeleportCommands.containsKey(p.getPlayer())){
						ignore.add(p.getPlayer());
						String full = "/"+ pendingTeleportCommands.get(p.getPlayer());
						if(facCommand){
							if(args.split(" ").length>1){
								full+=" "+args.split(" ",2)[1];
							}
						}else
						if(args!=null && args.length()>0){
							full+=" "+args;
						}
						p.getPlayer().chat(full);
						ignore.remove(p.getPlayer());
						pendingTeleportCommands.remove(p.getPlayer());
					}
					
				}
					
			}, timer);
		}
		
	}

	public static boolean canPlayerEnderPerlHere(UPlayer up, Location from, Location to) {
		Faction host = FactionManager.getFactionAt(to.getBlock());
		if(host.getName().equalsIgnoreCase("SafeZone")){
			up.sendMessage(ChatColor.RED+"You are unable to enderpearl into the "+ host.getName(up));
			return false;
		}else if(host.isNone() || host.getName().equalsIgnoreCase("WarZone") || host.getRelationTo(up).isAtLeast(Rel.TRUCE)){
			return true;
		}else{
			up.sendMessage(ChatColor.RED+"You are unable to enderpearl into the faction "+ host.getName(up));
			return false;
		}
	}

	public static boolean canPlayerTeleportHere(UPlayer up, Location from,
			Location to) {
		Faction host = FactionManager.getFactionAt(to.getBlock());
		if(host.getRelationTo(up).isAtLeast(Rel.TRUCE)){
			return true;
		}
		if(host.isDefault() && !host.isNone()){
			return true;
		}
		if(up.getPower()>1 && host.isNone()){
			return true;
		}else if(up.getPower()<=1){
			up.sendMessage(ChatColor.RED+"Your weak power is preventing you from teleporting");
			return false;
		}
		else{
			up.sendMessage(ChatColor.RED+"You are unable to teleport to the faction "+ host.getName(up));
			return false;
		}
	}
	
}
