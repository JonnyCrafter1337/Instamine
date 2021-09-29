import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;

import java.util.Arrays;
import java.util.List;

public class InstamineTabCompleter implements TabCompleter {
    @Override
    public List<String> onTabComplete(CommandSender commandSender, Command command, String s, String[] strings) {
        if( command.getName().equalsIgnoreCase("instamine") && strings.length == 1){
            return Arrays.asList("on", "off");
        }
        return null;
    }
}
