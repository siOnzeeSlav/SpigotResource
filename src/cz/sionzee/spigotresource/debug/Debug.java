/*
 * [SpigotResource]
 * Created by sionzee.
 * All rights reserved to siOnzee.cz 2015
 * Repository: https://github.com/siOnzeeSlav/SpigotResource
 */

package cz.sionzee.spigotresource.debug;

import cz.sionzee.spigotresource.utils.Formater;
import org.bukkit.Bukkit;

import java.util.logging.Level;

public class Debug {

    /**
     * Log via {@link Formater#_format(Object, String, Object...)}
     * @param instanceOfClass Object
     * @param format String
     * @param args Object...
     */
    public static void log(Object instanceOfClass, String format, Object... args) {
        Bukkit.getLogger().log(Level.INFO, Formater._format(instanceOfClass, format, args));
    }

    /**
     * Log error via {@link Formater#_format(Object, String, Object...)}
     * @param instanceOfClass Object
     * @param format String
     * @param args Object...
     */
    public static void error(Object instanceOfClass, String format, Object... args) {
        Bukkit.getLogger().log(Level.SEVERE, Formater._format(instanceOfClass, format, args));
    }

    /**
     * Log error via {@link Formater#format(Exception, Object, String, Object...)}
     * @param exception Exception
     * @param instance Object
     * @param format String
     * @param args Object...
     */
    public static void error(Exception exception, Object instance, String format, Object... args) {
        Bukkit.getLogger().log(Level.SEVERE, Formater.format(exception, instance, format, args));
    }
}
