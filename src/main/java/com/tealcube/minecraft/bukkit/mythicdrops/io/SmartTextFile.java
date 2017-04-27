/**
 * This file is part of MythicDrops, licensed under the MIT License.
 *
 * Copyright (C) 2013 Richard Harrah
 *
 * Permission is hereby granted, free of charge,
 * to any person obtaining a copy of this software and associated documentation files (the "Software"),
 * to deal in the Software without restriction, including without limitation the rights to use,
 * copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software,
 * and to permit persons to whom the Software is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or
 * substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED,
 * INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS
 * OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY,
 * WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN
 * CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */
package com.tealcube.minecraft.bukkit.mythicdrops.io;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
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
