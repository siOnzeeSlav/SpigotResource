/*
 * [SpigotResource]
 * Created by sionzee.
 * All rights reserved to siOnzee.cz 2015
 * Repository: https://github.com/siOnzeeSlav/SpigotResource
 */

package cz.sionzee.spigotresource.commands;

import org.bukkit.command.CommandSender;

public interface ICommand {

    boolean execute(CommandExecutor commandExecutor, CommandSender commandSender, String[] args, boolean isSubCommand);

}
