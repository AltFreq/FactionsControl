package com.minecraftdimensions.factionscontrol;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import com.massivecraft.factions.event.FactionsEventCreate;
import com.massivecraft.factions.event.FactionsEventMembershipChange;
import com.massivecraft.factions.event.FactionsEventNameChange;
import com.massivecraft.factions.event.FactionsEventMembershipChange.MembershipChangeReason;

public class FactionTeamListener implements Listener {
	
	
	@EventHandler
	public void login(PlayerLoginEvent e) {
		TeamManager.addPlayerToScoreboard(e.getPlayer());
	}
	
	@EventHandler
	public void logout(PlayerQuitEvent e) {
		TeamManager.removePlayerFromTeam(e.getPlayer());
	}
	
	@EventHandler
	public void createFaction(FactionsEventCreate e) {
		if(e.isAsynchronous()){
			return;
		}
		TeamManager.addPlayerToScoreboard(e.getUSender(), e.getUniverse());
	}
	
	@EventHandler
	public void createFaction(FactionsEventNameChange e) {
		if(e.isCancelled()){
			return;
		}
		
		TeamManager.renameTeam(e.getFaction().getName(), e.getNewName());
	}
	
	@EventHandler
	public void createFaction(FactionsEventMembershipChange e) {
		if(e.isCancelled()){
			return;
		}
		
		if(e.getReason().equals(MembershipChangeReason.JOIN) || e.getReason().equals(MembershipChangeReason.CREATE)){
			TeamManager.addPlayerToScoreboard(e.getUPlayer(), e.getNewFaction());
			return;
		}
		
		if(e.getReason().equals(MembershipChangeReason.LEAVE) || e.getReason().equals(MembershipChangeReason.KICK)){
			TeamManager.removePlayerFromTeam(e.getUPlayer().getPlayer());
			return;
		}
	}

}
