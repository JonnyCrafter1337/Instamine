import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

public class BlockToInvMain extends JavaPlugin {
    private static BlockToInvMain instance;
    GameListener listener;
    @Override
    public void onEnable() {

        getLogger().info("Instamining is enabled");
        PluginManager pm = getServer().getPluginManager();
        listener = new GameListener(this);
        pm.registerEvents(listener, this);
        this.getCommand("instamine").setExecutor(new CommandInstamine(this));
        getCommand("instamine").setTabCompleter(new InstamineTabCompleter());
    }

    public GameListener getListener() {
        return listener;
    }

    @Override
    public void onDisable() {
        getLogger().info("instamining is disabled");
    }


}