/*
 * [SpigotResource]
 * Created by sionzee.
 * All rights reserved to siOnzee.cz 2015
 * Repository: https://github.com/siOnzeeSlav/SpigotResource
 */

package cz.sionzee.spigotresource.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Regex {
    /**
     * Return a group of regex
     *
     * @param string  string where search
     * @param pattern to match in string
     * @param group   which group
     * @return Group
     */
    public static String getGroup(String string, Pattern pattern, int group) {
        Matcher matcher = pattern.matcher(string);
        matcher.find();
        return matcher.group(group);
    }

    public static String replace(String string, Pattern pattern, String to) {
        Matcher matcher = pattern.matcher(string);
        matcher.find();
        return string.replace(matcher.group(), to);
    }

    public static String[] getGroups(String string, Pattern pattern) {
        Matcher matcher = pattern.matcher(string);
        matcher.find();
        String[] groups = new String[matcher.groupCount()];
        for(int i = 0; i < matcher.groupCount(); i++) {
            groups[i] = matcher.group(i + 1);
        }
        return groups;
    }

    public static boolean isMatch(String string, Pattern pattern) {
        Matcher mat = pattern.matcher(string);
        return mat.find();
    }
}
