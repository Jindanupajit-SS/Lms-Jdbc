package com.smoothstack.jan2020.LmsJDBC;

public class Debug {

    private static boolean debug = false;

    static {
        debug = java.lang.management.ManagementFactory.getRuntimeMXBean().
                getInputArguments().toString().indexOf("-agentlib:jdwp") > 0;
    }

    public static boolean isDebug() {
        return debug;
    }

    public static void setDebug(boolean debug) {
        Debug.debug = debug;
    }

    public static void println(String s) {
        printf(s+"\n");
    }

    public static void print(String s) {
        printf(s);
    }

    public static void printf(String format, Object... objects) {
        if (!isDebug()) return;

        String message = String.format(format, objects);
        System.err.print(message.replaceAll("^"," * "));
    }
}
