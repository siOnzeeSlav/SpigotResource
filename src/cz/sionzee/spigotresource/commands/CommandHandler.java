/*
 * [SpigotResource]
 * Created by sionzee.
 * All rights reserved to siOnzee.cz 2015
 * Repository: https://github.com/siOnzeeSlav/SpigotResource
 */

package cz.sionzee.spigotresource.commands;

import cz.sionzee.spigotresource.SpigotPlugin;
import cz.sionzee.spigotresource.utils.Utils;
import org.bukkit.plugin.java.JavaPlugin;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.TreeMap;

public class CommandHandler {
    public static TreeMap<String, CommandExecutor> getAllCommands(SpigotPlugin spigotPlugin) {
        TreeMap<String, CommandExecutor> commands = new TreeMap<>();
        final Command[][] commandsArray = new Command[1][1];
        Utils.getClassesFromNames(Utils.getClassNames()).stream().filter(aClass -> (commandsArray[0] = aClass.getAnnotationsByType(Command.class)).length > 0 && ICommand.class.isAssignableFrom(aClass) && aClass != ICommand.class).forEach(aClass1 -> {
                    Command classCommand = commandsArray[0][0];
                    Object classInstance = null;
                    if (aClass1.getDeclaredConstructors().length > 0) {

                        for (Constructor c : aClass1.getDeclaredConstructors()) {
                            if (c.getParameterCount() > 0) {
                                if (JavaPlugin.class.isAssignableFrom(c.getParameterTypes()[0])) {
                                    try {
                                        classInstance = c.newInstance(spigotPlugin);
                                    } catch (InstantiationException e) {
                                        e.printStackTrace();
                                    } catch (IllegalAccessException e) {
                                        e.printStackTrace();
                                    } catch (InvocationTargetException e) {
                                        e.printStackTrace();
                                    }
                                    break;
                                }
                            }
                        }
                    }
                    try {
                        if (classInstance == null)
                            classInstance = aClass1.newInstance();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    LinkedHashMap<String, CommandEntry> subCommands = new LinkedHashMap<>();

                    subCommands.putAll(getCommandsWithSubCommands(classInstance, classCommand.value()));

                    commands.put(classCommand.value(), new CommandExecutor(classCommand, classInstance, subCommands));
                }
        );

        return commands;
    }

    private static TreeMap<String, CommandEntry> getCommandsWithSubCommands(Object instance, String path) {
        TreeMap<String, CommandEntry> commands = new TreeMap<>();

        commands.putAll(getSubCommands(instance.getClass(), path + ".", instance));

        Class<?>[] clazzs = instance.getClass().getDeclaredClasses();
        if (clazzs.length > 0)
            Arrays.stream(clazzs).filter(ISubCommand.class::isAssignableFrom).forEach(aClass2 -> {
                Object innerInstance = null;
                try {
                    if (aClass2.getConstructors().length == 0)
                        if (Utils.throwAFakeException("Class [" + aClass2.getSimpleName() + "] isn't public."))
                            return;

                    innerInstance = aClass2.getConstructors()[0].newInstance(instance);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                Command[] commandsArray = aClass2.getAnnotationsByType(Command.class);
                if (commandsArray.length == 0)
                    if (Utils.throwAFakeException("Missing @Command annotation for a [" + aClass2.getSimpleName() + "] ISubCommand class."))
                        return;

                commands.put(path + "." + commandsArray[0].value(), new CommandEntry(commandsArray[0], null, innerInstance));
                commands.putAll(getCommandsWithSubCommands(innerInstance, path + "." + commandsArray[0].value()));
            });

        return commands;
    }

    private static TreeMap<String, CommandEntry> getSubCommands(Class clazz, String path, Object subClassInstance) {
        TreeMap<String, CommandEntry> subCommands = new TreeMap<>();

        final Command[][] subCommandsArray = new Command[1][1];
        Arrays.asList(clazz.getDeclaredMethods()).stream().filter(method -> (subCommandsArray[0] = method.getDeclaredAnnotationsByType(Command.class)).length > 0).forEach(method1 -> {

            if (subCommandsArray[0].length == 0)
                if (Utils.throwAFakeException("Missing @Command annotation for a [" + method1.getName() + "] SubCommand method."))
                    return;

            Command subCommand = subCommandsArray[0][0];
            subCommands.put(path + subCommand.value(), new CommandEntry(subCommand, method1, subClassInstance));
        });

        return subCommands;
    }
}
