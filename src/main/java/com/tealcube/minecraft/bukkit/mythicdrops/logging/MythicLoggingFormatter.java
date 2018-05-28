/*
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
package com.tealcube.minecraft.bukkit.mythicdrops.logging;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.text.MessageFormat;
import java.util.Date;
import java.util.logging.Formatter;
import java.util.logging.LogRecord;

public class MythicLoggingFormatter extends Formatter {

  private Date dat = new Date();
  private final static String format = "{0,date} {0,time}";
  private MessageFormat formatter;
  private Object args[] = new Object[1];
  private String lineSeparator = System.lineSeparator();

  /**
   * Format the given LogRecord.
   * @param record the log record to be formatted.
   * @return a formatted log record
   */
  public synchronized String format(LogRecord record) {

    StringBuilder sb = new StringBuilder();

    // Minimize memory allocations here.
    dat.setTime(record.getMillis());
    args[0] = dat;


    // Date and time
    StringBuffer text = new StringBuffer();
    if (formatter == null) {
      formatter = new MessageFormat(format);
    }
    formatter.format(args, text, null);
    sb.append(text);
    sb.append(" ");


    // Class name
    if (record.getSourceClassName() != null) {
      sb.append(record.getSourceClassName());
    } else {
      sb.append(record.getLoggerName());
    }

    // Method name
    if (record.getSourceMethodName() != null) {
      sb.append(" ");
      sb.append(record.getSourceMethodName());
    }
    sb.append(" - "); // lineSeparator



    String message = formatMessage(record);

    // Level
    sb.append(record.getLevel().getLocalizedName());
    sb.append(": ");

    // Indent - the more serious, the more indented.
    //sb.append( String.format("% ""s") );
    int iOffset = (1000 - record.getLevel().intValue()) / 100;
    for( int i = 0; i < iOffset;  i++ ){
      sb.append(" ");
    }


    sb.append(message);
    sb.append(lineSeparator);
    if (record.getThrown() != null) {
      try {
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        record.getThrown().printStackTrace(pw);
        pw.close();
        sb.append(sw.toString());
      } catch (Exception ignored) {
      }
    }
    return sb.toString();
  }

}
