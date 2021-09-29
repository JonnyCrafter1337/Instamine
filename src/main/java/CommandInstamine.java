import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.HashMap;

public class CommandInstamine implements CommandExecutor {
    private final BlockToInvMain plugin;
    public CommandInstamine(BlockToInvMain plugin) {
        this.plugin = plugin;
    }
    private static final HashMap<Player, Boolean> isInstamining = new HashMap<Player, Boolean>();
    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] args) {
        if (commandSender instanceof Player){
            Player sender = (((Player) commandSender).getPlayer());
            if(args.length == 0 || (!args[0].equals("on") && !args[0].equals("off"))){
                sender.sendMessage("please select on or off");
                return true;
            }
            if(args[0].equals("on") ){
                if(!isInstamining.containsKey(sender)) {
                    isInstamining.put(sender, true);
                    sender.sendMessage("You will now mine blocks instantaneously");
                }else{
                    sender.sendMessage("Instamining is already on");
                }

            }else if(args[0].equals("off")){
                if(isInstamining.containsKey(sender)){
                    isInstamining.remove(sender);
                    sender.sendMessage("You will no longer Instamine");

                }else{
                    sender.sendMessage("Instamine is already off");
                }

            }
        }
        return true;
    }

    public static boolean isIsInastamining(Player p){
        return isInstamining.containsKey(p);
    }
}
