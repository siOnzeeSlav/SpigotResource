/*
 * [SpigotResource]
 * Created by sionzee.
 * All rights reserved to siOnzee.cz 2015
 * Repository: https://github.com/siOnzeeSlav/SpigotResource
 */

package cz.sionzee.spigotresource.commands;

import java.util.HashMap;

public class CommandExecutor {

    private Command classCommand;

    private HashMap<String, CommandEntry> subCommands;

    private Object classInstance;

    public CommandExecutor(Command classCommand, Object classInstance, HashMap<String, CommandEntry> subCommands) {
        this.classCommand = classCommand;
        this.subCommands = subCommands;
        this.classInstance = classInstance;
    }

    public Command getClassCommand() {
        return classCommand;
    }

    public HashMap<String, CommandEntry> getSubCommands() {
        return subCommands;
    }

    public Object getClassInstance() {
        return classInstance;
    }
}
