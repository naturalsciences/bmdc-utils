/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package be.naturalsciences.bmdc.utils;

import java.io.File;
import java.io.InputStream;
import java.net.MalformedURLException;
import javax.servlet.ServletContext;

/**
 *
 * @author thomas
 */
public class JavaUtils {

    public static InputStream getResource(String fileName, Class cls) {
        return cls.getResourceAsStream("/" + fileName);
    }

    public static File getResourceFile(String fileName, Class cls, ServletContext context) {
        String resource = cls.getClassLoader().getResource(fileName).getFile();
        File searchedFile = new File(resource);
        if (context != null && !searchedFile.exists()) {
            try {
                resource = context.getResource("/WEB-INF/classes/" + fileName).getFile();
            } catch (MalformedURLException ex) {
                throw new RuntimeException("The searched file has a malformed name.");
            }
            searchedFile = new File(resource);
        }
        if (!searchedFile.exists()) {
            throw new RuntimeException("The searched file should be located at " + searchedFile.getAbsolutePath() + " but doesn't exist.");
        }
        return searchedFile;
    }
}
