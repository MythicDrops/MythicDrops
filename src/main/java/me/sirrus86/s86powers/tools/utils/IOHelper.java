package me.sirrus86.s86powers.tools.utils;

import java.io.File;
import java.io.IOException;
import java.net.URL;

public class IOHelper {

    public static int BYTE_BUFFER = 4096;

    public static URL getJarUrl(final File file) throws IOException {
        URL url = file.toURI().toURL();
        url = new URL("jar:" + url.toExternalForm() + "!/");
        return url;
    }

    public static boolean isJar(final File file) {
        return file.getName().endsWith(".jar");
    }
}