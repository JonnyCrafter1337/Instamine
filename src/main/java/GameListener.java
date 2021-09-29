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

        if(!CommandInstamine.isIsInastamining(e.getPlayer())){
            return;
        }
        if(e.getBlock().getType() == Material.BEDROCK){
            return;
        }


        if (e.getBlock().getType().isItem()) {

            Player player = e.getPlayer();
            Location loc = e.getBlock().getLocation();
            HashMap<Integer, ? extends ItemStack> isInInv = new HashMap<Integer, ItemStack>();
            boolean addedSuccess = false;
            if (player.getInventory().firstEmpty() == -1) {         //Is inventory full
                if (e.getPlayer().getInventory().contains(e.getBlock().getType())) {

                    isInInv = e.getPlayer().getInventory().all(e.getBlock().getType());
                    for (Map.Entry<Integer, ? extends ItemStack> entry : isInInv.entrySet())

                        if (entry.getValue().getAmount() < entry.getValue().getType().getMaxStackSize()) {
                            if(e.getBlock().getBlockData() instanceof SeaPickle) {
                                SeaPickle s = ((SeaPickle) e.getBlock().getBlockData());
                                int newAmmount = entry.getValue().getAmount() + s.getPickles();
                                entry.getValue().setAmount(newAmmount);
                                e.getBlock().setType(Material.AIR);
                                addedSuccess = true;
                            }
                            else if(e.getBlock().getBlockData() instanceof TurtleEgg) {
                                TurtleEgg t = ((TurtleEgg) e.getBlock().getBlockData());
                                int newAmmount = entry.getValue().getAmount() + t.getEggs();
                                entry.getValue().setAmount(newAmmount);
                                e.getBlock().setType(Material.AIR);
                                addedSuccess = true;
                            }
                            else if (e.getBlock().getBlockData() instanceof Bisected) {

                                Bisected b = ((Bisected) e.getBlock().getBlockData());
                                if (b.getHalf().equals(Bisected.Half.TOP)) {
                                    e.getBlock().getLocation().add(0, -1, 0).getBlock().setType(Material.AIR);

                                    int newAmmount = entry.getValue().getAmount() + 1;
                                    entry.getValue().setAmount(newAmmount);
                                    e.getBlock().setType(Material.AIR);
                                    addedSuccess = true;



                                }

                            }else {
                                int newAmmount = entry.getValue().getAmount() + 1;
                                entry.getValue().setAmount(newAmmount);
                                e.getBlock().setType(Material.AIR);

                                addedSuccess = true;
                                break;
                            }
                        }

                }
            }
            else if(e.getBlock().getState() instanceof ShulkerBox){
                ShulkerBox s = ((ShulkerBox) e.getBlock().getState());
                ItemStack shulker = new ItemStack(e.getBlock().getType());
                BlockStateMeta bsm = (BlockStateMeta) shulker.getItemMeta();
                bsm.setBlockState(s);
                shulker.setItemMeta(bsm);

                e.getPlayer().getInventory().addItem(shulker);
                e.getBlock().setType(Material.AIR);
                addedSuccess = true;

            }
            else if(e.getBlock().getBlockData() instanceof SeaPickle){
                SeaPickle s = ((SeaPickle) e.getBlock().getBlockData());
                ItemStack newItemStack = new ItemStack(e.getBlock().getType());
                newItemStack.setAmount(s.getPickles());
                e.getPlayer().getInventory().addItem(newItemStack);
                e.getBlock().setType(Material.AIR);
                addedSuccess = true;

            }
            else if(e.getBlock().getBlockData() instanceof TurtleEgg){
                TurtleEgg t = ((TurtleEgg) e.getBlock().getBlockData());
                ItemStack newItemStack = new ItemStack(e.getBlock().getType());
                newItemStack.setAmount(t.getEggs());
                e.getPlayer().getInventory().addItem(newItemStack);
                e.getBlock().setType(Material.AIR);
                addedSuccess = true;

            }



            else if (e.getBlock().getBlockData() instanceof Bisected) {

                Bisected b = ((Bisected) e.getBlock().getBlockData());
                if (b.getHalf().equals(Bisected.Half.TOP)) {


                    ItemStack newItemStack = new ItemStack(e.getBlock().getType());
                    e.getPlayer().getInventory().addItem(newItemStack);
                    e.getBlock().setType(Material.AIR);
                    addedSuccess = true;
                    e.getBlock().getLocation().add(0, -1, 0).getBlock().setType(Material.AIR);


                }

            } else if (e.getBlock().getBlockData() instanceof Bed) {

                Bed b = ((Bed) e.getBlock().getBlockData());
                Bukkit.getLogger().info(b.getFacing().name());
                if (b.getPart() == Bed.Part.FOOT) {
                    String facing = b.getFacing().name();


                    ItemStack newItemStack = new ItemStack(e.getBlock().getType());
                    e.getPlayer().getInventory().addItem(newItemStack);
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
                } else {
                    ItemStack newItemStack = new ItemStack(e.getBlock().getType());

                    e.getPlayer().getInventory().addItem(newItemStack);
                    e.getBlock().setType(Material.AIR);
                    addedSuccess = true;
                }

            } else {
                ItemStack newItemStack = new ItemStack(e.getBlock().getType());
                e.getPlayer().getInventory().addItem(newItemStack);
                e.getBlock().setType(Material.AIR);
                addedSuccess = true;
            }


            e.setCancelled(true);
            if (!addedSuccess) {
                e.getPlayer().sendMessage("You dont have space in your inventory");
            }
        }
    }
}