/*
 * [SpigotResource]
 * Created by sionzee.
 * All rights reserved to siOnzee.cz 2015
 * Repository: https://github.com/siOnzeeSlav/SpigotResource
 */

package cz.sionzee.spigotresource.commands;

import org.bukkit.command.CommandSender;

public interface IArg<T> {
    /**
     * @param sender Send user message if use incorrect format
     * @param object what user enter..
     * @return is it correct? (it fallback command)
     * @Example
     */
    boolean isParsableFormat(CommandSender sender, Object object);

    /**
     * When user passes through isParsableFormat, this will called for new Instance
     *
     * @param object
     */
    T getInstanceFrom(Object object);
}
