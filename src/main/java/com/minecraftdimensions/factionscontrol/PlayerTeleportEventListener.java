package com.minecraftdimensions.factionscontrol;

import java.util.Arrays;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.event.player.PlayerTeleportEvent.TeleportCause;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import com.massivecraft.factions.entity.UPlayer;

public class PlayerTeleportEventListener implements Listener {
	List<String> commands = Arrays.asList("tpa", "tpaccept", "ws", "worldspawn","ss", "serverspawn","warp", "home", "spawn");
	
	@EventHandler
	public void teleportCommand(PlayerCommandPreprocessEvent e) {
		if(e.isCancelled()){
			return;
		}
		UPlayer up = UPlayer.get(e.getPlayer());
		
		if(up.isUsingAdminMode()){
			return;
		}
		if(TeleportManager.ignore.contains(e.getPlayer())){
			return;
		}
		String message = e.getMessage();
		boolean facCommand = false;
		String command = null;
		String args = null;
		if(message.split(" ").length>1){
			String[] formatted = message.split(" ", 2);
			command = formatted[0];
			args = formatted[1];
		}else{
			command = message;
		}
		if(command.equalsIgnoreCase("/f") && args!=null){
			facCommand = true;
			command = args.split(" ")[0];
		}else{
			command = command.substring(1,command.length());
		}
		if(commands.contains(command)){
			e.setCancelled(true);
			TeleportManager.setPlayerPending(up, command,args, facCommand);
		}
	}
	
	
	@EventHandler
	public void playerMove(PlayerMoveEvent e) {
		if( (e.getFrom().getBlockX()!=e.getTo().getBlockX())||(e.getFrom().getBlockY()!=e.getTo().getBlockY()) || (e.getFrom().getBlockZ()!=e.getTo().getBlockZ())){
			if(TeleportManager.pendingTeleportCommands.containsKey(e.getPlayer())){
				TeleportManager.pendingTeleportCommands.remove(e.getPlayer());
				e.getPlayer().sendMessage(ChatColor.RED+"Movement canceled your teleport");
			}
		}
	}
	
	@EventHandler
	public void playerDeath(PlayerDeathEvent e) {
		if(e.getEntity() instanceof Player){
			Player p = (Player) e.getEntity();
			if(TeleportManager.pendingTeleportCommands.containsKey(p)){
				TeleportManager.pendingTeleportCommands.remove(p);
				p.sendMessage(ChatColor.RED+"Death canceled your teleport");
			}
		}
	}
	
	@EventHandler
	public void playerDeath(PlayerQuitEvent e) {
			if(TeleportManager.pendingTeleportCommands.containsKey(e.getPlayer())){
				TeleportManager.pendingTeleportCommands.remove(e.getPlayer());
			}
	}
	
	@EventHandler
	public void playerTP(PlayerTeleportEvent e) {
		UPlayer up = UPlayer.get(e.getPlayer());
		if(up.isUsingAdminMode()){
			return;
		}
		if(e.getCause().equals(TeleportCause.COMMAND)|| e.getCause().equals(TeleportCause.PLUGIN)){
			if(!TeleportManager.canPlayerTeleportHere(up, e.getFrom(), e.getTo())){
				e.setCancelled(true);
			}
		}else if(e.getCause().equals(TeleportCause.ENDER_PEARL)){
			if(TeleportManager.canPlayerEnderPerlHere(up, e.getFrom(), e.getTo())){
				damagePlayer(e.getPlayer());
			}else {
				e.setCancelled(true);
				return;
			}
		}
	}
	
	public static void damagePlayer(Player p){
		p.damage(2.0);
		for(PotionEffect pe:p.getActivePotionEffects()){
			if(pe.getType().equals(PotionEffectType.HEALTH_BOOST)){
				p.removePotionEffect(pe.getType());
			}else if(pe.getType().equals(PotionEffectType.INVISIBILITY)){
				p.removePotionEffect(pe.getType());
			}else if(pe.getType().equals(PotionEffectType.REGENERATION)){
				p.removePotionEffect(pe.getType());
			}else if(pe.getType().equals(PotionEffectType.ABSORPTION)){
				p.removePotionEffect(pe.getType());
			}
		}
	}
}
