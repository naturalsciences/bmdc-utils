/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package be.naturalsciences.bmdc.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.Field;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.StringJoiner;
import java.util.zip.CRC32;

import javax.xml.bind.DatatypeConverter;

/**
 *
 * @author thomas
 */
public class ObjectUtils {

    public static void printObjectToFile(Object object, File file) throws FileNotFoundException, IOException {
        FileOutputStream fileOutputStream = new FileOutputStream(file);
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream);
        objectOutputStream.writeObject(object);
        objectOutputStream.flush();
        objectOutputStream.close();
    }

    public static Object readObjectFromFile(File file)
            throws FileNotFoundException, IOException, ClassNotFoundException {
        FileInputStream fileInputStream = new FileInputStream(file);
        ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);
        Object o = objectInputStream.readObject();
        objectInputStream.close();
        return o;
    }

    /**
     * Returns a String representation of all fields of the given object. The method is used to calculate CRC checksums which might be persisted in a adatabase. Do not modify the code in any way or a mismatch may happen!
     * @param o
     * @return
     */
    public static String toStringFields(Object o) {
        StringJoiner sj = new StringJoiner(",");

        for (Field field : o.getClass().getFields()) {
            if (!java.lang.reflect.Modifier.isStatic(field.getModifiers())) {
                try {
                    sj.add(String.format("%s: %s", field.getName(), field.get(o)));
                } catch (IllegalArgumentException | IllegalAccessException e) {
                    sj.add(String.format("%s: %s", field.getName(), "null"));
                }
            }

        }
        return sj.toString();
    }

    /**
     * Returns a CRC32 checksum of the given object.
     * @param o
     * @return
     */
    public static String getCRC32(Object o) {
        String data = toStringFields(o);
        CRC32 fileCRC32 = new CRC32();
        fileCRC32.update(data.getBytes());
        String a = String.format("%08X", fileCRC32.getValue());
        return a;
    }

    /**
     * Returns a MD5 checksum of the given object.
     * @param o
     * @return
     */
    public static String getMD5(Object o) {
        String data = toStringFields(o);
        MessageDigest md;
        try {
            md = MessageDigest.getInstance("MD5");
            md.update(data.getBytes());
            byte[] digest = md.digest();
            String myHash = DatatypeConverter
                    .printHexBinary(digest).toUpperCase();
            return myHash;
        } catch (NoSuchAlgorithmException e) {
            return null;
        }
    }
}
