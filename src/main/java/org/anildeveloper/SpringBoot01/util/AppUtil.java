package org.anildeveloper.SpringBoot01.util;

import java.io.File;

public class AppUtil {

    public static String get_upload_path(String filename) {
        return new File("src\\main\\resources\\static\\assets\\uploads").getAbsolutePath() + "\\" + filename;
    }

}
