package com.minecraftdimensions.factionscontrol;

import java.util.HashMap;
import java.util.Set;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.ScoreboardManager;
import org.bukkit.scoreboard.Team;

import com.massivecraft.factions.entity.Faction;
import com.massivecraft.factions.entity.UPlayer;

public class TeamManager {
	
	public static HashMap<String, Team> teams = new HashMap<>();
	static ScoreboardManager manager;
	static Scoreboard board;
	static Objective objective;
	
	public static void initialise(){
		 manager = Bukkit.getScoreboardManager();
		 board = manager.getNewScoreboard();
		 objective = board.registerNewObjective("showhealth", "health");
		 objective.setDisplaySlot(DisplaySlot.BELOW_NAME);
		 objective.setDisplayName("/ 20");
	}
	
	public static void addPlayerToScoreboard(final Player p){
		Bukkit.getScheduler().runTaskLater(FactionsControl.instance, new Runnable(){

			@Override
			public void run() {
				p.setScoreboard(board);
				p.setHealth(p.getHealth());
				addPlayerToTeam(p);
			}
		}, 10);
	}
	
	public static void addPlayerToTeam(Player p){
		
		Faction faction = FactionManager.getFPlayer(p).getFaction();
		if(faction.isDefault()){
			return;
		}
		if(board.getTeam(faction.getName())!=null){
			teams.get(faction.getName()).addPlayer(p);
		}else{
			Team t = createTeam(faction.getName());
			t.addPlayer(p);
		}
	}
	
	public static void removePlayerFromTeam(Player p){
		if(p==null){
			return;
		}
		Team t =board.getPlayerTeam(p);
		if(t==null){
			return;
		}
		t.removePlayer(p);
		if(t.getSize()==0){
			teams.remove(t.getName());
			t.unregister();
		}
	}
	
	public static void renameTeam(String team, String newName){
		Team a = board.getTeam(team);
		Set<OfflinePlayer> list =a.getPlayers();
		a.unregister();
		
		Team b =createTeam(newName);
		for(OfflinePlayer p: list){
			b.addPlayer(p);
		}
	}
	
	public boolean isTeamRegistered(String name){
		return teams.containsKey(name);
	}
	
	public static Team createTeam(String team){
		Team t = board.registerNewTeam(team);
		teams.put(team, t);
		t.setCanSeeFriendlyInvisibles(true);
		t.setAllowFriendlyFire(false);
		return t;
	}

	public static void addPlayerToScoreboard(UPlayer uSender, String universe) {
		Faction faction = uSender.getFaction();
		Player p = uSender.getPlayer();
		if(teams.containsKey(faction.getName())){
			teams.get(faction.getName()).addPlayer(p);
		}else{
			Team t = createTeam(faction.getName());
			t.addPlayer(p);
		}
		
	}

	public static void addPlayerToScoreboard(UPlayer uPlayer, Faction faction) {
		Player p = uPlayer.getPlayer();
		if(teams.containsKey(faction.getName())){
			teams.get(faction.getName()).addPlayer(p);
		}else{
			Team t = createTeam(faction.getName());
			t.addPlayer(p);
		}
	}

}
