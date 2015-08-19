/*
 * [SpigotResource]
 * Created by sionzee.
 * All rights reserved to siOnzee.cz 2015
 * Repository: https://github.com/siOnzeeSlav/SpigotResource
 */

package cz.sionzee.spigotresource;

public interface ISpigotPlugin {

    /**
     * Called when this plugin is enabled
     */
    void onPluginEnable();

    /**
     * Called when this plugin is disabled
     */
    void onPluginDisable();

    /**
     * Called after a plugin is loaded but before it has been enabled.
     */
    void onPluginLoad();
}
