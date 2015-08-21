/*
 * [SpigotResource]
 * Created by sionzee.
 * All rights reserved to siOnzee.cz 2015
 * Repository: https://github.com/siOnzeeSlav/SpigotResource
 */

package cz.sionzee.spigotresource.utils;

import org.bukkit.ChatColor;

public class Formater {
    public static final String LINE_NUMBER = "\\{LINE_NUMBER\\}".replace("\\", "");
    public static final String FILE_NAME = "\\{FILE_NAME\\}".replace("\\", "");
    public static final String CLASS_NAME = "\\{CLASS_NAME\\}".replace("\\", "");
    public static final String METHOD_NAME = "\\{METHOD_NAME\\}".replace("\\", "");

    public static String format(Exception e, Object instance, String format, Object... args) {
        String rslt = ChatColor.translateAlternateColorCodes('&', format);
        for (int i = 0; i < args.length; i++) {
            if (rslt.contains("{" + i + "}")) {
                rslt = rslt.replaceAll("\\{" + i + "\\}", String.valueOf(args[i]));
            }
        }

        if (instance != null) {
            StackTraceElement ste = Refractors.getStackTrace(e.getStackTrace(), instance);
            rslt = rslt.replace(LINE_NUMBER, (ste != null ? ste.getLineNumber() : "NULL") + "").replace(METHOD_NAME, ste != null ? ste.getMethodName() : "NULL").replace(CLASS_NAME, ste != null ? ste.getClassName() : "NULL").replace(FILE_NAME, ste != null ? ste.getFileName() : "NULL");
        }

        return rslt;
    }

    public static String _format(Object instance, String format, Object... args) {
        String rslt = ChatColor.translateAlternateColorCodes('&', format);
        for (int i = 0; i < args.length; i++) {
            if (rslt.contains("{" + i + "}")) {
                rslt = rslt.replaceAll("\\{" + i + "\\}", String.valueOf(args[i]));
            }
        }

        if (instance != null) {
            StackTraceElement ste = Refractors.getStackTrace(null, instance);
            rslt = rslt.replace(LINE_NUMBER, (ste != null ? ste.getLineNumber() : "NULL") + "").replace(METHOD_NAME, ste != null ? ste.getMethodName() : "NULL").replace(CLASS_NAME, ste != null ? ste.getClassName() : "NULL").replace(FILE_NAME, ste != null ? ste.getFileName() : "NULL");
        }

        return rslt;
    }

    public static String format(String format, Object... args) {
        String rslt = ChatColor.translateAlternateColorCodes('&', format);
        for (int i = 0; i < args.length; i++) {
            if (rslt.contains("{" + i + "}")) {
                rslt = rslt.replaceAll("\\{" + i + "\\}", String.valueOf(args[i]));
            }
        }
        return rslt;
    }
}
