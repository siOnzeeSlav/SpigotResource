/*
 * [SpigotResource]
 * Created by sionzee.
 * All rights reserved to siOnzee.cz 2015
 * Repository: https://github.com/siOnzeeSlav/SpigotResource
 */

package cz.sionzee.spigotresource.commands;

import java.lang.reflect.Method;

public class CommandEntry {

    private Object subClassInstance;
    private Command subCommand;
    private Method method;

    public CommandEntry(Command subCommand, Method method) {
        this.subCommand = subCommand;
        this.method = method;
    }

    public CommandEntry(Command subCommand, Method method, Object subClassInstance) {
        this.subCommand = subCommand;
        this.method = method;
        this.subClassInstance = subClassInstance;
    }

    public boolean isSubCommandClass() {
        return method == null && subClassInstance != null;
    }

    public Command getSubCommand() {
        return subCommand;
    }

    public Method getMethod() {
        return method;
    }

    public Object getSubClassInstance() {
        return subClassInstance;
    }
}
