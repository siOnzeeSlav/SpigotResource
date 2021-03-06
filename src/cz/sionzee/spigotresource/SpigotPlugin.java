/*
 * [SpigotResource]
 * Created by sionzee.
 * All rights reserved to siOnzee.cz 2015
 * Repository: https://github.com/siOnzeeSlav/SpigotResource
 */

package cz.sionzee.spigotresource;

import cz.sionzee.spigotresource.autoloader.AutoLoader;
import org.bukkit.plugin.java.JavaPlugin;

public abstract class SpigotPlugin extends JavaPlugin implements ISpigotPlugin {

    private final AutoLoader autoLoader = new AutoLoader(this, true);

    @Override
    public void onEnable() {
        autoLoader.LoadListenerService();
        onPluginEnable();
        autoLoader.LoadInjectService();
    }

    @Override
    public void onDisable() {
        onPluginDisable();
    }

    @Override
    public void onLoad() {
        autoLoader.registerService(this);
        autoLoader.LoadCommandService();
        onPluginLoad();
    }

    /**
     * Return instance of autoloader
     * @return autoLoader
     */
    protected final AutoLoader getAutoLoader() {
        return autoLoader;
    }
}
