package com.minecraftdimensions.factionscontrol;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import com.massivecraft.factions.Rel;
import com.massivecraft.factions.entity.BoardColls;
import com.massivecraft.factions.entity.Faction;
import com.massivecraft.factions.entity.UPlayer;
import com.massivecraft.mcore.ps.PS;

public class FactionManager {
	
	public static List<Material> SafeBlocks = Arrays.asList(Material.STRING, Material.TRIPWIRE, Material.VINE, Material.RED_ROSE, Material.LEAVES, Material.GLASS, Material.YELLOW_FLOWER, Material.SUGAR_CANE_BLOCK);
	public static List<Material> BlockSwitches = Arrays.asList(Material.ENDER_CHEST, Material.LEVER, Material.ANVIL, Material.WOOD_BUTTON, Material.WOOD_DOOR, Material.WOODEN_DOOR, Material.TRAP_DOOR, Material.STONE_BUTTON, Material.WOOD_PLATE, Material.STONE_PLATE, Material.IRON_PLATE, Material.GOLD_PLATE);
	public static List<Material> Containers = Arrays.asList(Material.CHEST, Material.FURNACE, Material.TRAPPED_CHEST);
	public static List<Material> blockedItems = Arrays.asList(Material.LAVA_BUCKET, Material.WATER_BUCKET);
	public static ArrayList<String> underAttack = new ArrayList<>();
	
	public static boolean canPlayerBreakHere(Player p, Block b){
		UPlayer up = getFPlayer(p);
		if(up.isUsingAdminMode()){
			return true;
		}
		Faction host = getFactionAt(b);
		if(host.isNone()){
			return true;
		}
		if(up.getFaction().equals(host)){
			return true;
		}
		if(Containers.contains(b.getType())){
			p.sendMessage(ChatColor.RED+"You are unable to break this block");
			return false;
		}
		if(host.getName().equalsIgnoreCase("safezone") || host.getName().equalsIgnoreCase("WarZone")){
			p.sendMessage(ChatColor.RED+"You are unable to break blocks in the "+ host.getName());
			return false;
		}
		if(!isFactionOnline(host)){
			p.sendMessage(ChatColor.RED+host.getName(up)+ChatColor.RED+ " is protected as they are offline");
			return false;
		}
		if(SafeBlocks.contains(b.getType())){
			return true;
		}
		Rel rel = host.getRelationTo(up);
		if(rel.isAtLeast(Rel.ALLY)){
			return true;
		}else{
			if(rel== Rel.ENEMY){
				damagePlayer(p);
				sendUnderAttackMessage(host, up.getFaction());
				return true;
			}else{
				p.sendMessage(ChatColor.RED+"Your faction must /enemy "+host.getName()+ " before you raid them");
				return false;
			}
		}
	}
	
	public static UPlayer getFPlayer(Player p){
		return UPlayer.get(p);
	}
	
	public static Faction getFactionAt(Block  b){
		return BoardColls.get().getFactionAt(PS.valueOf(b.getLocation()));
	}
	
	public Faction getPlayersFaction(Player p){
		return getFPlayer(p).getFaction();
	}
	
	public static boolean isFactionOnline(Faction f){
		return f.getOnlinePlayers().size()>0;
	}
	
	public static void damagePlayer(Player p){
		p.damage(8.0);
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
	
	public static void sendUnderAttackMessage(final Faction host, Faction attacker){
		if(!underAttack.contains(host.getName())){
		host.sendMessage(ChatColor.DARK_RED+"Your faction is under attack by "+attacker.getName(host) + "!");
		underAttack.add(host.getName());
			Bukkit.getScheduler().runTaskLaterAsynchronously(FactionsControl.instance, new Runnable(){

				@Override
				public void run() {
				underAttack.remove(host.getName());
				
				}
			
			}, 100);
		}
	}

	public static boolean canPlayerInteractHere(Player p, Block b) {
		UPlayer up = getFPlayer(p);
		if(up.isUsingAdminMode()){
			return true;
		}
		Faction host = getFactionAt(b);
		if(host.isNone()){
			return true;
		}
		if(up.getFaction().equals(host)){
			return true;
		}
		if(host.isNone() || host.getName().equalsIgnoreCase("safezone") || host.getName().equalsIgnoreCase("WarZone")){
			return true;
		}
		if(!isFactionOnline(host) && (BlockSwitches.contains(b.getType()) || Containers.contains(b.getType()))){
			p.sendMessage(ChatColor.RED+host.getName(up)+ChatColor.RED+ " is protected as they are offline");
			return false;
		}
		Rel rel = host.getRelationTo(up);
		if(blockedItems.contains(p.getItemInHand().getType()) && !rel.isAtLeast(Rel.MEMBER)){
			return false;
		}
		if(rel.isAtLeast(Rel.ALLY)){
			return true;
		}
		if(BlockSwitches.contains(b.getType())){
			if(rel== Rel.ENEMY){
				damagePlayer(p);
				sendUnderAttackMessage(host, up.getFaction());
				return true;
			}else{
				p.sendMessage(ChatColor.RED+"Your faction must /enemy "+host.getName()+ " before you raid them");
				return false;
			}
		}else if (Containers.contains(b.getType())){
			if(rel== Rel.ENEMY){
				damagePlayer(p);
				sendUnderChestAttackMessage(host, up.getFaction());
				return true;
			}else{
				p.sendMessage(ChatColor.RED+"Your faction must /enemy "+host.getName()+ " before you raid them");
				return false;
			}
		}else{
			return true;
		}
		
	}

	private static void sendUnderChestAttackMessage(Faction host,
			Faction faction) {
		host.sendMessage(ChatColor.DARK_RED+"Your faction chests are being raided by "+faction.getName(host) + "!");
	}

	public static boolean canPlayerPlaceHere(Player p, Block b) {
		UPlayer up = getFPlayer(p);
		if(up.isUsingAdminMode()){
			return true;
		}
		Faction host = getFactionAt(b);
		if(host.isNone()){
			return true;
		}
		if(up.getFaction().equals(host)){
			return true;
		}
		if(host.getName().equalsIgnoreCase("safezone") || host.getName().equalsIgnoreCase("WarZone")){
			p.sendMessage(ChatColor.RED+"You are unable to place blocks in the "+ host.getName());
			return false;
		}
		if(!isFactionOnline(host)){
			p.sendMessage(ChatColor.RED+host.getName(up)+ChatColor.RED+ " is protected as they are offline");
			return false;
		}
		Rel rel = host.getRelationTo(up);
		if(rel.isAtLeast(Rel.ALLY)){
			return true;
		}else{
			if(rel== Rel.ENEMY){
				damagePlayer(p);
				sendUnderAttackMessage(host, up.getFaction());
				return true;
			}else{
				p.sendMessage(ChatColor.RED+"Your faction must /enemy "+host.getName()+ " before you raid them");
				return false;
			}
		}
	}
	
	
	
	

}
