/*
 * [SpigotResource]
 * Created by sionzee.
 * All rights reserved to siOnzee.cz 2015
 * Repository: https://github.com/siOnzeeSlav/SpigotResource
 */

package cz.sionzee.spigotresource.configuration;

import cz.sionzee.spigotresource.utils.Lists;
import cz.sionzee.spigotresource.utils.Regex;
import cz.sionzee.spigotresource.utils.StringUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.nio.charset.CharsetEncoder;
import java.util.*;
import java.util.regex.Pattern;

//TODO UNFINISHED CLASS
public class NeonConfiguration {

    private final Pattern REGEX_PARENT = Pattern.compile("^[\\s]*(.*:)$");
    private final Pattern REGEX_COMMENT = Pattern.compile("((/\\*)([^\\*/]|\n)*(\\*/)|^[\\s\n]$|^$)");
    private final Pattern REGEX_GLOBAL = Pattern.compile("^([^\t].*)\"(.*)\"$");
    private final Pattern REGEX_KEY = Pattern.compile("[^\\s](.*):");
    private final Pattern REGEX_VALUE = Pattern.compile("\"(.*)\"");

    private HashMap<String, Object> list = new HashMap<>();
    private String[] lines;
    private long byteCount;
    private File file;

    public NeonConfiguration(File file) {
        this.file = file;

        File directory = new File(file.getAbsolutePath().substring(0, file.getAbsolutePath().lastIndexOf(File.separator)));

        if (!directory.exists()) {
            directory.mkdir();
        }

        if (!this.file.exists())
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        loadFile();
        parseFile();

       /* list.entrySet().stream().forEach(stringObjectEntry -> {
            System.out.println(stringObjectEntry.getKey() + " = " + stringObjectEntry.getValue());
        });*/

    }

    private void parseFile() {
        int tempTab = 0;
        String parent = "";

        //System.out.println("Found " + lines.length + " elements.");

        if (lines.length > 0)
            for (String line : lines) {
                int tabs = cz.sionzee.spigotresource.utils.StringUtils.countChar(line, '\t');
                //System.out.println("Found " + tabs + " tabs.");
                boolean isParent = Regex.isMatch(line, REGEX_PARENT);
                if (tabs > tempTab) {
                    if (isParent) {
                        parent += line.replace(":", ".").replace("\t", "");
                    } else {
                        String key = parent + Regex.getGroup(line, REGEX_KEY, 0);
                        key = key.substring(0, key.length() - 1);
                        String value = Regex.getGroup(line, REGEX_VALUE, 1);
                        list.put(key, value);
                    }
                } else if (tabs < tempTab) {
                    for (int a = tempTab; a > tabs; a--) {
                        int dots = StringUtils.countChar(parent, '.');
                        if (dots > 0) {
                            String without = parent.substring(0, parent.length() - 1);
                            parent = parent.substring(0, without.lastIndexOf(".") + 1);
                        } else {
                            parent = "";
                        }

                    }

                    if (isParent) {
                        parent += line.replace(":", ".").replace("\t", "");
                    } else {
                        String key = parent + Regex.getGroup(line, REGEX_KEY, 0);
                        key = key.substring(0, key.length() - 1);
                        String value = Regex.getGroup(line, REGEX_VALUE, 1);
                        list.put(key, value);
                    }
                } else if (tabs == tempTab) {
                    if (isParent) {
                        parent += line.replace(":", ".").replace("\t", "");
                    } else {
                        String key = parent + Regex.getGroup(line, REGEX_KEY, 0);
                        key = key.substring(0, key.length() - 1);
                        String value = Regex.getGroup(line, REGEX_VALUE, 1);
                        list.put(key, value);
                    }
                }
                tempTab = tabs;
            }
    }

    private void loadFile() {
        try {
            FileInputStream fInput = new FileInputStream(file);
            FileChannel fChannel = fInput.getChannel();
            MappedByteBuffer mappedBuffer = fChannel.map(FileChannel.MapMode.READ_ONLY, 0L, fChannel.size());
            mappedBuffer.load();
            CharsetDecoder decoder = Charset.forName("UTF-8").newDecoder();
            CharBuffer charBuffer = decoder.decode(mappedBuffer);

            Scanner scanner = new Scanner(charBuffer);
            scanner.useDelimiter("\\n");
            List<String> lines = new ArrayList<>();
            String line;
            while (scanner.hasNext()) {
                line = scanner.next();
                byteCount += line.length() * 8 * 8;
                lines.add(line);
                //System.out.println("Line: ' " + line + " ' added.");
                break;
            }
            this.lines = Lists.toArray(lines, String.class);
            scanner.close();
            charBuffer.clear();
            mappedBuffer.clear();
            fChannel.close();
            fInput.close();
            //System.out.println("File readed successfully");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void save() {
        try {
            FileOutputStream fOutput = new FileOutputStream(file);
            FileChannel fileChannel = fOutput.getChannel();

            MappedByteBuffer mappedByteBuffer = fileChannel.map(FileChannel.MapMode.READ_WRITE, 0L, byteCount);
            Arrays.stream(lines).parallel().forEach(s -> mappedByteBuffer.put(s.getBytes()));

            CharsetEncoder encoder = Charset.forName("UTF-8").newEncoder();
            ByteBuffer buffer = encoder.encode(mappedByteBuffer.asCharBuffer());

            fOutput.write(buffer.array());

            buffer.clear();
            mappedByteBuffer.clear();
            fileChannel.close();
            fOutput.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
