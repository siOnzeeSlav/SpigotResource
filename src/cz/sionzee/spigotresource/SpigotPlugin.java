/*
 * [SpigotResource]
 * Created by sionzee.
 * All rights reserved to siOnzee.cz 2015
 * Repository: https://github.com/siOnzeeSlav/SpigotResource
 */

package cz.sionzee.spigotresource;

import org.bukkit.plugin.java.JavaPlugin;

public abstract class SpigotPlugin extends JavaPlugin implements ISpigotPlugin {

    private final AutoLoader autoLoader = new AutoLoader(this, true);

    @Override
    public void onEnable() {
        autoLoader.LoadEventService();
        onPluginEnable();
    }

    @Override
    public void onDisable() {
        onPluginDisable();
    }

    @Override
    public void onLoad() {
        autoLoader.LoadCommandService();
        onPluginLoad();
    }

}
