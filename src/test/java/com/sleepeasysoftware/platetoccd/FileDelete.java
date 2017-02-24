package com.sleepeasysoftware.platetoccd;

import java.io.File;

/**
 * Created by Daniel Kaplan on behalf of Sleep Easy Software.
 */
public class FileDelete {
    public static void deleteAndFlushFs(String pathName) {
        System.gc();  // http://stackoverflow.com/a/4213208/61624
        //noinspection ResultOfMethodCallIgnored
        new File(pathName).delete();
    }
}
