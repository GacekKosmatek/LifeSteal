package wtf.gacek.lifesteal.commands;

import java.util.*;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.CommandExecutor;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import wtf.gacek.lifesteal.Lifesteal;
import wtf.gacek.lifesteal.Utils;

public class LifestealCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length == 0) {
            Utils.colorize(sender, "&cLifeSteal Plugin 1.3");
            Utils.colorize(sender, "&cCreated by GacekKosmatek");
            Utils.colorize(sender, "");
            if (sender.hasPermission("lifesteal.admin")) {
                Utils.colorize(sender, "&cAdmin Commands:");
                Utils.colorize(sender, " - &c/lifesteal sethearts <player> <amount> - Sets players hearts to the amount provided");
                Utils.colorize(sender, " - &c/lifesteal gethearts <player> - Get how many hearts a player has");
            }
            Utils.colorize(sender, "&cCommands: ");
            Utils.colorize(sender, " - &c/lifesteal withdraw [amount] - Withdraw a amount of hearts, defaults to 1");
            return true;
        }
        Player target;
        switch (args[0].toLowerCase()) {
            case "sethearts":
                if (!sender.hasPermission("lifesteal.admin")) {
                    Utils.colorize(sender, "&cCommand not found.");
                    break;
                }
                target = Bukkit.getServer().getPlayer(args[1]);
                if (target == null) {
                    Utils.colorize(sender, "&cCould not find " + args[1] + ", are you sure they are online?");
                    break;
                }
                if (args.length != 3) {
                    Utils.colorize(sender, "&cNice hearts amount");
                    break;
                }
                Lifesteal.getLifeManager().setHealth(target, Integer.parseInt(args[2]) * 2);
                Utils.colorize(sender, "&cSet " + target.getName() + "'s hearts to " + args[2]);
                break;
            case "gethearts":
                if (!sender.hasPermission("lifesteal.admin")) {
                    Utils.colorize(sender, "&cCommand not found.");
                    break;
                }
                target = Bukkit.getServer().getPlayer(args[1]);
                if (target == null) {
                    Utils.colorize(sender, "&cCould not find " + args[1] + ", are you sure they are online?");
                    break;
                }
                Utils.colorize(sender, "&6" + target.getName() + "'s health: " + Lifesteal.getLifeManager().getHealth(target) / 2);
                break;
            case "withdraw":
                if (!(sender instanceof Player)) {
                    Utils.colorize(sender, "&cYou have to be a player to use this command!");
                    break;
                }
                int amount = 1;
                if (args.length >= 2) {
                    try {
                        amount = Integer.parseInt(args[1]);
                    } catch (NumberFormatException e) {
                        Utils.colorize(sender, "&cThat does not look like a number!");
                        break;
                    }
                }
                Player p = (Player) sender;
                ItemStack itemStack = new ItemStack(Material.NETHER_STAR);
                ItemMeta itemMeta = Bukkit.getItemFactory().getItemMeta(itemStack.getType());
                assert itemMeta != null;
                itemMeta.setDisplayName(Utils.colorize("&cHeart"));
                itemMeta.setLore(List.of(Utils.colorize("&cRight click to redeem a single heart")));
                itemStack.setItemMeta(itemMeta);
                if ((Lifesteal.getLifeManager().getHealth(p) - (amount * 2)) <= 0) {
                    Utils.colorize(sender, "&cYou can't withdraw that many hearts!");
                    break;
                }
                itemStack.setAmount(amount);
                p.getInventory().addItem(itemStack);
                Lifesteal.getLifeManager().setHealth((Player) sender, Lifesteal.getLifeManager().getHealth((Player) sender) - (amount * 2));
                Utils.colorize(sender, "&cYou withdrew " + amount + " heart!");
                break;
            default:
                Utils.colorize(sender, "&cCommand not found.");
                break;
        }
        return true;
    }
}

