/*
 * [SpigotResource]
 * Created by sionzee.
 * All rights reserved to siOnzee.cz 2015
 * Repository: https://github.com/siOnzeeSlav/SpigotResource
 */

package cz.sionzee.spigotresource.io;

import cz.sionzee.spigotresource.utils.Lists;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.CharBuffer;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.util.LinkedList;
import java.util.Scanner;

public class FileReader {

    private File file;
    private FileInputStream fInput;
    private FileChannel fChannel;
    private MappedByteBuffer mappedBuffer;
    private CharsetDecoder decoder;
    private CharBuffer charBuffer;
    private Scanner scanner;
    private String[] lines;

    public FileReader(File file) {
        this.file = file;
    }

    public FileInputStream getfInput() {
        return fInput;
    }

    public FileChannel getfChannel() {
        return fChannel;
    }

    public MappedByteBuffer getMappedBuffer() {
        return mappedBuffer;
    }

    public CharsetDecoder getDecoder() {
        return decoder;
    }

    public CharBuffer getCharBuffer() {
        return charBuffer;
    }

    public Scanner getScanner() {
        return scanner;
    }

    public String[] getLines() {
        return lines;
    }

    public FileReader read() {
        try {
            fInput = new FileInputStream(file);
            fChannel = fInput.getChannel();
            mappedBuffer = fChannel.map(FileChannel.MapMode.READ_ONLY, 0L, fChannel.size());
            mappedBuffer.load();
            decoder = Charset.forName("UTF-8").newDecoder();
            charBuffer = decoder.decode(mappedBuffer);
            scanner = new Scanner(charBuffer);
            scanner.useDelimiter("\\n");
            LinkedList<String> list = new LinkedList<>();
            while (scanner.hasNext()) {
                String line = scanner.next();
                list.add(line);
            }
            lines = Lists.toArray(list, String.class);
            list.clear();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return this;
    }

    public FileReader clear() {
        scanner.close();
        charBuffer.clear();
        mappedBuffer.clear();
        try {
            fChannel.close();
            fInput.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return this;
    }
}
