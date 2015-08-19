/*
 * [SpigotResource]
 * Created by sionzee.
 * All rights reserved to siOnzee.cz 2015
 * Repository: https://github.com/siOnzeeSlav/SpigotResource
 */

package cz.sionzee.spigotresource.utils;

import cz.sionzee.spigotresource.SpigotPlugin;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.jar.JarFile;
import java.util.stream.Collectors;
import java.util.zip.ZipEntry;

public class Utils {

    /**
     * Foreach this JAR and get all classNames
     *
     * @return
     */
    public static List<String> getClassNames() {
        List<String> classesNames = new ArrayList<>();
        JarFile file = null;
        try {
            file = new JarFile(new File(SpigotPlugin.class.getProtectionDomain().getCodeSource().getLocation().toURI().getPath()));
            classesNames = file.stream().filter(f -> f.getName().endsWith(".class") && !f.getName().contains("$")).map(ZipEntry::getName).collect(Collectors.toList());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return classesNames;
    }

    /**
     * Get class from ClassName
     *
     * @param names [cz/sionzee/spigotresource/SpigotPlugin.class] (package/class)
     * @return
     */
    public static List<Class<?>> getClassesFromNames(List<String> names) {
        List<Class<?>> classes = names.stream().map(f -> {
            try {
                return Class.forName(f.replaceAll("/", ".").replaceAll(".class", ""));
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
            return null;
        }).collect(Collectors.toList());
        return classes;
    }

    public static boolean throwAFakeException(String message) {
        System.out.println("\n\n" + "SpigotPlugin Exception Occurred --------\n\n" + message + "\n\n----------------------------------------");
        return true;
    }

    public static boolean isClearNumber(String a) {
        try {
            Integer.parseInt(a);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }
}
