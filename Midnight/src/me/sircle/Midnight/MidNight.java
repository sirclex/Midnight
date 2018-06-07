package me.sircle.Midnight;

import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

public class MidNight extends JavaPlugin implements Listener {
	
	public HashMap<Integer, ItemStack> items;
	
	public boolean midnightOccur;
	
	public Inventory midnightAdminChest;
	
	public String adminChestName;
	
	public void onEnable() {
		items = new HashMap<Integer, ItemStack>();
		midnightOccur = false;
		adminChestName = "Required Item"; 
		midnightAdminChest = Bukkit.createInventory(null, 9, adminChestName);
		this.getServer().getPluginManager().registerEvents(this, this);
	}
	
	public void onDisable() {
		
	}
	
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (!(sender instanceof Player) ) {
			sender.sendMessage("Only player can excute this command");
		} else {
			Player p = (Player) sender;
			if (args.length < 1) {
				if (p.hasPermission("midnight.admin")) {
					p.sendMessage("/midnight create - Create new midnight set");
					p.sendMessage("/midnight clear - Clear the current midnight set");
					p.sendMessage("/midnight check - Check required midnight item");
					p.sendMessage("/midnight done - Get midnight ticket in case has enough item.");
					return true;
				} else if (p.hasPermission("midnight.member")) {
					p.sendMessage("/midnight check - Check required midnight item");
					p.sendMessage("/midnight done - Get midnight ticket in case has enough item.");
					return true;
				}
			} else {
				if (args[0].equalsIgnoreCase("create")) {
					if (p.hasPermission("midnight.admin")) {
						for (int i = 0; i < 9; i++) {
							items.put(i, p.getInventory().getItem(i));
							midnightAdminChest.setItem(i, p.getInventory().getItem(i));
							
						}
						this.setMidnightStatus(true);
						p.sendMessage("A new midnight set has been created");
					} else {
						sender.sendMessage("You don't have permission to do that.");
					}
					return true;
				}
				if (args[0].equalsIgnoreCase("clear")) {
					if (p.hasPermission("midnight.admin")) {
						for (int i = 0; i < 9; i++) {
							items.put(i, null);
							midnightAdminChest.clear();
						}
						this.setMidnightStatus(false);
						p.sendMessage("Cleared current midnight set");
					} else {
						sender.sendMessage("You don't have permission to do that.");
					}
					return true;
				}
				if (args[0].equalsIgnoreCase("check")) {
					if (p.hasPermission("midnight.member")) {
						if (this.midnightOccur) {
							p.openInventory(midnightAdminChest);
						} else {
							p.sendMessage("Midnight haven't occured yet");
						}
					} else {
						sender.sendMessage("You don't have permission to do that.");
					}
				}
				if (args[0].equalsIgnoreCase("done")) {
					if (p.hasPermission("midnight.member")) {
						if (this.midnightOccur) {
							for (int i = 0; i < 9; i++) {
								System.out.println(midnightAdminChest.getItem(i));
								if (p.getInventory().getItem(i).equals(midnightAdminChest.getItem(i))) {
									if (i == 9) {
										//Give Midnight Ticket
									}
								} else {
									p.sendMessage("You don't have enough item");
									break;
								}
							}
						} else {
							p.sendMessage("Midnight haven't occured yet");
						}
					} else {
						sender.sendMessage("You don't have permission to do that.");
					}
				}
			}
		}
		return false;
	}
	
	public boolean midnightStatus() {
		return this.midnightOccur;
	}
	
	public void setMidnightStatus(boolean b) {
		this.midnightOccur = b;
	}
	
	@EventHandler
	public void onInventoryClick(InventoryClickEvent e) {
		Inventory inv = e.getInventory();
		if (inv.getName() == this.adminChestName) {
			e.setCancelled(true);
		}
	}
	
}
