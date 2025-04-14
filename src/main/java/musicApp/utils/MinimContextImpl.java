package musicApp.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

public class MinimContextImpl {

    public String sketchPath(String filename) {
        return new File(filename).getAbsolutePath();
    }

    public InputStream createInput(String filename) {
        try {
            return new FileInputStream(filename);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }
}
