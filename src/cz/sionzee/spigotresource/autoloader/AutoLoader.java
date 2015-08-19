/*
 * [SpigotResource]
 * Created by sionzee.
 * All rights reserved to siOnzee.cz 2015
 * Repository: https://github.com/siOnzeeSlav/SpigotResource
 */

package cz.sionzee.spigotresource.autoloader;

import cz.sionzee.spigotresource.SpigotPlugin;
import cz.sionzee.spigotresource.commands.*;
import cz.sionzee.spigotresource.events.EventEntity;
import cz.sionzee.spigotresource.events.EventHandler;
import cz.sionzee.spigotresource.locale.Messages;
import cz.sionzee.spigotresource.utils.Array;
import cz.sionzee.spigotresource.utils.Formater;
import cz.sionzee.spigotresource.utils.Utils;
import org.bukkit.command.Command;
import org.bukkit.command.CommandMap;
import org.bukkit.command.CommandSender;
import org.bukkit.event.Listener;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.SimplePluginManager;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AutoLoader {

    private SpigotPlugin spigotPlugin;
    private PluginManager pluginManager;
    private boolean isDebug = false;

    public AutoLoader(SpigotPlugin spigotPlugin, boolean isDebug) {
        this.spigotPlugin = spigotPlugin;
        this.isDebug = isDebug;
        this.pluginManager = spigotPlugin.getServer().getPluginManager();
    }

    public void registerService(Object instance) {
        InjectHandler.register(instance);
    }

    public void LoadInjectService() {
        /* ---------------------------- AutoLoader Injects ---------------------------- */

        InjectHandler.load();

        /* ---------------------------- AutoLoader End Injects ---------------------------- */
    }

    public void LoadCommandService() {
        /* ---------------------------- AutoLoader Commands ---------------------------- */

        Field commandMapField = null;
        try {
            commandMapField = SimplePluginManager.class.getDeclaredField("commandMap");
            commandMapField.setAccessible(true);
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
            return;
        }

        CommandMap commandMap = null;

        try {
            commandMap = (CommandMap) commandMapField.get(pluginManager);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
            return;
        }

        for (Map.Entry<String, CommandExecutor> commands : CommandHandler.getAllCommands().entrySet()) {
            final String commandName = commands.getKey();
            final CommandExecutor commandExecutor = commands.getValue();

            commandMap.register(commandName, new Command(commandName, commandExecutor.getClassCommand().description(), commandExecutor.getClassCommand().usage(), Arrays.asList(commandExecutor.getClassCommand().alias())) {
                @Override
                public boolean execute(CommandSender commandSender, String s, String[] args) {
                    ICommand iCommand = (ICommand) commandExecutor.getClassInstance();
                    boolean state;
                    CommandEntry commandEntry = null;
                    int lastIndex = 0;
                    boolean hasMoreArgs = false;
                    String parent = commandName + ".";

                    if (args.length > 0) {

                        for (String key : commandExecutor.getSubCommands().keySet()) {
                            if (key.equalsIgnoreCase(parent + args[lastIndex])) {
                                parent += args[lastIndex].toLowerCase();

                                if ((args.length - 1) <= lastIndex) break;
                                else {
                                    parent += ".";
                                    lastIndex++;
                                }
                            }
                        }

                        if (parent.endsWith(".")) {
                            commandEntry = commandExecutor.getSubCommands().get(parent.substring(parent.indexOf(commandName + "."), parent.length() - 1));
                            hasMoreArgs = true;
                        } else {
                            commandEntry = commandExecutor.getSubCommands().get(parent.substring(parent.indexOf(commandName + "."), parent.length()));
                        }
                    }

                    state = iCommand.execute(commandExecutor, commandSender, args, commandEntry != null);

                    if (commandEntry != null) {
                        try {
                            if (commandEntry.isSubCommandClass()) {
                                ISubCommand subCommand = (ISubCommand) commandEntry.getSubClassInstance();

                                if (hasMoreArgs) {
                                    subCommand.actionDefault(Array.startFrom(args, String.class, lastIndex));
                                } else {
                                    subCommand.actionDefault(new String[0]);
                                }
                            } else {

                                if (!hasMoreArgs) {
                                    commandEntry.getMethod().setAccessible(true);
                                    commandEntry.getMethod().invoke(commandEntry.getSubClassInstance());
                                    return false;
                                }

                                boolean isArray = commandEntry.getMethod().getParameterTypes()[0].isArray();
                                if (commandEntry.getMethod().getParameterTypes().length != args.length - lastIndex && !isArray) {

                                    if (commandEntry.getSubCommand().usage().length() == 0 && isDebug) {
                                        Utils.throwAFakeException("Please fill @SubCommand usage at SubCommand " + parent + ".");
                                    } else {
                                        String message = commandEntry.getSubCommand().usage().replace("{cmd}", parent.replace(".", " ").substring(0, parent.length() - 1));
                                        commandSender.sendMessage(Formater.format(this, message));
                                    }
                                    return false;
                                }

                                if (!isArray) {
                                    Object[] correctArgs = new Object[args.length - (lastIndex)];
                                    for (int i = 0; i < commandEntry.getMethod().getParameterTypes().length; i++) {
                                        Class<?> parameterType = commandEntry.getMethod().getParameterTypes()[i];
                                        if (parameterType == int.class) {
                                            if (Utils.isClearNumber(args[lastIndex + i])) {
                                                correctArgs[i] = Integer.parseInt(args[lastIndex + i]);
                                            } else {
                                                commandSender.sendMessage(Formater.format(this, Messages.INCORRECT_PARAMETER_NUMBER, args[lastIndex + i]));
                                                return false;
                                            }
                                        } else if (parameterType == String.class) {
                                            if (args[lastIndex + i].getClass() != String.class || Utils.isClearNumber(args[lastIndex + i])) {
                                                commandSender.sendMessage(Formater.format(this, Messages.INCORRECT_PARAMETER_STRING, args[lastIndex + i]));
                                                return false;
                                            }
                                            correctArgs[i] = args[lastIndex + i];
                                        } else {

                                            //TODO: Make test

                                            if (IArg.class.isAssignableFrom(parameterType)) {
                                                IArg arg = (IArg) parameterType.newInstance();
                                                if (arg.isParsableFormat(commandSender, args[lastIndex + i])) {
                                                    correctArgs[i] = arg.getInstanceFrom(args[lastIndex + i]);
                                                }
                                            }
                                        }
                                    }
                                    commandEntry.getMethod().setAccessible(true);
                                    commandEntry.getMethod().invoke(commandEntry.getSubClassInstance(), correctArgs);
                                } else {
                                    Object[] correctArgs = new Object[args.length - lastIndex];

                                    for (int i = 0; i < (args.length - lastIndex); i++) {
                                        correctArgs[i] = args[lastIndex + i];
                                    }

                                    //TODO: Works only on Object[]

                                    commandEntry.getMethod().setAccessible(true);
                                    commandEntry.getMethod().invoke(commandEntry.getSubClassInstance(), new Object[]{correctArgs});
                                }


                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                    return state;
                }
            });
        }

        /* ---------------------------- AutoLoader End Commands ---------------------------- */
    }

    public void LoadEventService() {
         /* ---------------------------- AutoLoader Events ---------------------------- */

        for (Map.Entry<String, EventEntity> listeners : EventHandler.getAllListeners().entrySet()) {
            pluginManager.registerEvents((Listener) listeners.getValue().getClassInstance(), spigotPlugin);
        }

        /* ---------------------------- AutoLoader End Events ---------------------------- */
    }

}
