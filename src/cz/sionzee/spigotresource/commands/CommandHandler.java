/*
 * [SpigotResource]
 * Created by sionzee.
 * All rights reserved to siOnzee.cz 2015
 * Repository: https://github.com/siOnzeeSlav/SpigotResource
 */

package cz.sionzee.spigotresource.commands;

import cz.sionzee.spigotresource.SpigotPlugin;
import cz.sionzee.spigotresource.utils.Annotations;
import cz.sionzee.spigotresource.utils.Utils;
import org.bukkit.plugin.java.JavaPlugin;

import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.TreeMap;

public class CommandHandler {
    public static TreeMap<String, CommandExecutor> getAllCommands(SpigotPlugin spigotPlugin) {
        TreeMap<String, CommandExecutor> commands = new TreeMap<>();
        for(Class<?> clazz : Utils.getClassesFromNames(Utils.getClassNames())) {
            Command[] commandsArray = Annotations.getAnnotationsByType(clazz, Command.class);
            if(commandsArray.length > 0) {
                if(ICommand.class.isAssignableFrom(clazz) && clazz != ICommand.class) {
                    Command command = commandsArray[0];
                    Object classInstance = null;
                    if(clazz.getDeclaredConstructors().length > 0)
                        for (Constructor c : clazz.getDeclaredConstructors())
                            if (c.getParameterTypes().length > 0)
                                if (JavaPlugin.class.isAssignableFrom(c.getParameterTypes()[0]))
                                    try {
                                        classInstance = c.newInstance(spigotPlugin);
                                        break;
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }

                    try {
                        if (classInstance == null)
                            classInstance = clazz.newInstance();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    LinkedHashMap<String, CommandEntry> subCommands = new LinkedHashMap<>();

                    if(classInstance != null)
                        subCommands.putAll(getCommandsWithSubCommands(classInstance, command.value()));

                    commands.put(command.value(), new CommandExecutor(command, classInstance, subCommands));
                }

            }
        }

        return commands;
    }

    private static TreeMap<String, CommandEntry> getCommandsWithSubCommands(Object instance, String path) {
        TreeMap<String, CommandEntry> commands = new TreeMap<>();

        commands.putAll(getSubCommands(instance.getClass(), path + ".", instance));

        Class<?>[] clazzs = instance.getClass().getDeclaredClasses();
        if (clazzs.length > 0)

            for(Class<?> clazz : clazzs) {
                if(ISubCommand.class.isAssignableFrom(clazz)) {
                    Object innerInstance = null;
                    try {
                        if (clazz.getConstructors().length == 0)
                            if (Utils.throwAFakeException("Class [" + clazz.getSimpleName() + "] isn't public."))
                                break;

                        innerInstance = clazz.getConstructors()[0].newInstance(instance);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    Command[] commandsArray = Annotations.getAnnotationsByType(clazz, Command.class);
                    if (commandsArray.length == 0)
                        if (Utils.throwAFakeException("Missing @Command annotation for a [" + clazz.getSimpleName() + "] ISubCommand class."))
                            break;

                    commands.put(path + "." + commandsArray[0].value(), new CommandEntry(commandsArray[0], null, innerInstance));
                    commands.putAll(getCommandsWithSubCommands(innerInstance, path + "." + commandsArray[0].value()));
                }
            }
        return commands;
    }

    private static TreeMap<String, CommandEntry> getSubCommands(Class clazz, String path, Object subClassInstance) {
        TreeMap<String, CommandEntry> subCommands = new TreeMap<>();

        for(Method method : clazz.getDeclaredMethods()) {
            if(method.getName() == "actionDefault")
                continue;

            Command[] subCommandsArray = Annotations.getAnnotationsByType(method, Command.class);
            if(subCommandsArray.length > 0) {
                Command subCommand = subCommandsArray[0];
                subCommands.put(path + subCommand.value(), new CommandEntry(subCommand, method, subClassInstance));
            }
        }
        return subCommands;
    }
}
