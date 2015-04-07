/*
 * Copyright (C) 2003-2014 eXo Platform SAS.
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.exoplatform.lab;

import java.io.PrintStream;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

/**
 * Created by The eXo Platform SAS
 * Author : eXoPlatform
 *          exo@exoplatform.com
 * Aug 5, 2014  
 */
public class Trace {
  /**
   * There are 3 trace levels (values of TRACELEVEL): 0 - No messages are
   * printed 1 - Trace messages are printed, but there is no indentation
   * according to the call stack 2 - Trace messages are printed, and they are
   * indented according to the call stack
   */
  public static int TRACELEVEL = 0;

  protected static PrintStream stream = null;

  protected static int callDepth = 0;

  /**
   * Initialization.
   */
  public static void initStream(PrintStream s) {
    stream = s;
  }

  /**
   * Prints an "entering" message. It is intended to be called in the beginning
   * of the blocks to be traced.
   */
  public static void traceEntry(String str) {
    if (TRACELEVEL == 0)
      return;
    if (TRACELEVEL == 2)
      callDepth++;
    printEntering(str);
  }

  /**
   * Prints an "exiting" message. It is intended to be called in the end of the
   * blocks to be traced.
   */
  public static void traceExit(String str) {
    if (TRACELEVEL == 0)
      return;
    printExiting(str);
    if (TRACELEVEL == 2)
      callDepth--;
  }

  private static void printEntering(String str) {
    printIndent();
    stream.println("Entering " + str);
    stream.println("thread dump: " + getDumpFor(Thread.currentThread()));

  }

  private static void printExiting(String str) {
    printIndent();
    stream.println("Exiting " + str);
  }

  private static void printIndent() {
    for (int i = 0; i < callDepth; i++)
      stream.print("  ");
  }

  public static String getDumpFor(Thread thread) {
    StringBuilder st = new StringBuilder();
    if (thread.isAlive()) {
      StackTraceElement[] stackTrace = thread.getStackTrace();
      st.append(thread.toString()).append("\n")
              .append(String.format(" State - %s,", thread.getState()))
              .append(String.format(" Is daemon = %s,", thread.isDaemon()));
      for (StackTraceElement s : stackTrace)
          st.append("\tat ").append(s.getClassName()).append(".").append(s.getMethodName()).append("(").append(s.getFileName()).append(":").append(s.getLineNumber()).append(")")
                  .append("\n");
    } else {
      st.append(" No thread ");
    }
    return st.toString();
  }

  public static void dumpActiveThreads() {
    Map<Thread, StackTraceElement[]> stackTraces = Thread.getAllStackTraces(); 
    Set<Thread> keySet = stackTraces.keySet();
    System.out.println("\nThread dump begin:");
    Iterator<Thread> it = keySet.iterator();
    
    while(it.hasNext()) {
      getDumpFor(it.next());
    }
    System.out.println("\nThread dump end.");

  }

}