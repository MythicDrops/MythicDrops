package com.tealcube.minecraft.bukkit.mythicdrops.io;

/*
 * #%L
 * MythicDrops
 * %%
 * Copyright (C) 2013 - 2015 TealCube
 * %%
 * Permission to use, copy, modify, and/or distribute this software for any purpose with or without fee is hereby
 * granted,
 * provided that the above copyright notice and this permission notice appear in all copies.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS" AND THE AUTHOR DISCLAIMS ALL WARRANTIES WITH REGARD TO THIS SOFTWARE INCLUDING ALL
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS. IN NO EVENT SHALL THE AUTHOR BE LIABLE FOR ANY SPECIAL, DIRECT,
 * INDIRECT, OR CONSEQUENTIAL DAMAGES OR ANY DAMAGES WHATSOEVER RESULTING FROM LOSS OF USE, DATA OR PROFITS, WHETHER
 * IN AN
 * ACTION OF CONTRACT, NEGLIGENCE OR OTHER TORTIOUS ACTION, ARISING OUT OF OR IN CONNECTION WITH THE USE OR
 * PERFORMANCE OF
 * THIS SOFTWARE.
 * #L%
 */


import java.io.*;
import java.util.ArrayList;
import java.util.List;

public final class SmartTextFile {

    private File debugFolder;
    private File debugFile;

    public SmartTextFile(File file) {
        this(file.getParentFile(), file);
    }

    public SmartTextFile(File folder, File file) {
        if (!folder.exists() && !folder.mkdirs() || !folder.isDirectory()) {
            return;
        }
        this.debugFolder = folder;
        this.debugFile = file;
    }

    public SmartTextFile(String folderPath, String fileName) {
        this(new File(folderPath), new File(folderPath, fileName));
    }

    public void write(String... messages) {
        try {
            if (getDebugFolder() == null || getDebugFile() == null) {
                return;
            }
            if (!getDebugFolder().exists() && !getDebugFolder().mkdirs()) {
                return;
            }
            File saveTo = getDebugFile();
            if (!saveTo.exists() && !saveTo.createNewFile()) {
                return;
            }
            FileWriter fw = new FileWriter(saveTo.getPath(), false);
            PrintWriter pw = new PrintWriter(fw);
            for (String message : messages) {
                pw.println(message);
            }
            pw.flush();
            pw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public File getDebugFolder() {
        return debugFolder;
    }

    public File getDebugFile() {
        return debugFile;
    }

    public List<String> read() {
        List<String> list = new ArrayList<>();
        try {
            if (getDebugFolder() == null || getDebugFile() == null) {
                return list;
            }
            if (!getDebugFolder().exists() && !getDebugFolder().mkdirs()) {
                return list;
            }
            File readFile = getDebugFile();
            if (!readFile.exists() && !readFile.createNewFile()) {
                return list;
            }
            FileReader fileReader = new FileReader(readFile);
            BufferedReader bufferedReader = new BufferedReader(fileReader);
            String p;
            while ((p = bufferedReader.readLine()) != null) {
                if (!p.contains("#") && p.length() > 0) {
                    list.add(p);
                }
            }
            bufferedReader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return list;
    }

}
