import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.ShulkerBox;
import org.bukkit.block.data.Bisected;
import org.bukkit.block.data.type.Bed;
import org.bukkit.block.data.type.SeaPickle;
import org.bukkit.block.data.type.TurtleEgg;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockDamageEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BlockStateMeta;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.material.MaterialData;

import java.util.Collection;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.function.Supplier;

public class GameListener implements Listener {


    public GameListener(BlockToInvMain plugin) {
        this.plugin = plugin;

    }


    private final BlockToInvMain plugin;


    @EventHandler
    private void onDamageBlock(BlockDamageEvent e) {

        if (!CommandInstamine.isIsInastamining(e.getPlayer())) {
            return;
        }
        if (e.getBlock().getType() == Material.BEDROCK) {
            return;
        }


        if (e.getBlock().getType().isItem()) {

            Player player = e.getPlayer();
            Location loc = e.getBlock().getLocation();
            HashMap<Integer, ? extends ItemStack> isInInv;
            boolean addedSuccess = false;
            if (e.getBlock().getBlockData() instanceof Bisected) {
                Bisected b = ((Bisected) e.getBlock().getBlockData());
                if (b.getHalf() == Bisected.Half.TOP) {
                    loc.add(0, -1, 0);
                }

            }

            Collection<ItemStack> collection = loc.getBlock().getDrops(e.getItemInHand(),e.getPlayer());
            if (collection.size() == 0) {
                collection = loc.getBlock().getDrops();
            }
            if (collection.size() == 0){
                loc.getBlock().setType(Material.AIR);
            }
            else {
                if (player.getInventory().firstEmpty() == -1) { //Is inventory full
                    for (ItemStack itemStack : collection) {
                        if (e.getPlayer().getInventory().contains(itemStack.getType())) {
                            isInInv = e.getPlayer().getInventory().all(itemStack.getType());
                            for (Map.Entry<Integer, ? extends ItemStack> entry : isInInv.entrySet()) {
                                if (entry.getValue().getAmount() < entry.getValue().getType().getMaxStackSize()) {

                                    int newAmmount = entry.getValue().getAmount() + itemStack.getAmount();
                                    entry.getValue().setAmount(newAmmount);
                                    loc.getBlock().setType(Material.AIR);

                                    addedSuccess = true;
                                    break;
                                }

                            }
                        }
                    }
                }
                else {
                    for (ItemStack itemStack : collection) {
                        if (e.getBlock().getBlockData() instanceof Bed) {

                            Bed b = ((Bed) e.getBlock().getBlockData());
                            if (b.getPart() == Bed.Part.FOOT) {
                                String facing = b.getFacing().name();
                                e.getPlayer().getInventory().addItem(itemStack);
                                e.getBlock().setType(Material.AIR, false);
                                addedSuccess = true;
                                if (facing.equals("NORTH")) {
                                    e.getBlock().getLocation().add(0, 0, -1).getBlock().setType(Material.AIR);
                                } else if (facing.equals("SOUTH")) {
                                    e.getBlock().getLocation().add(0, 0, 1).getBlock().setType(Material.AIR);
                                } else if (facing.equals("WEST")) {
                                    e.getBlock().getLocation().add(-1, 0, 0).getBlock().setType(Material.AIR);
                                } else if (facing.equals("EAST")) {
                                    e.getBlock().getLocation().add(1, 0, 0).getBlock().setType(Material.AIR);
                                }
                            }
                        } else {
                            e.getPlayer().getInventory().addItem(itemStack);
                            loc.getBlock().setType(Material.AIR);
                            addedSuccess = true;
                        }
                    }
                }

                e.setCancelled(true);
                if (!addedSuccess) {
                    e.getPlayer().sendMessage("You dont have space in your inventory");
                }
            }

        }
    }
}
