/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package be.naturalsciences.bmdc.utils;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.nio.file.attribute.FileAttribute;
import java.nio.file.attribute.GroupPrincipal;
import java.nio.file.attribute.PosixFileAttributeView;
import java.nio.file.attribute.PosixFilePermission;
import java.nio.file.attribute.PosixFilePermissions;
import java.nio.file.attribute.UserPrincipal;
import java.nio.file.attribute.UserPrincipalLookupService;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.zip.GZIPInputStream;

import javax.xml.bind.DatatypeConverter;

import org.apache.commons.compress.archivers.sevenz.SevenZArchiveEntry;
import org.apache.commons.compress.archivers.sevenz.SevenZFile;
import org.apache.commons.io.IOUtils;
import org.apache.commons.io.filefilter.WildcardFileFilter;

/**
 *
 * @author Thomas Vandenberghe
 */
public class FileUtils extends org.apache.commons.io.FileUtils {

    /**
     * Utility method to save InputStream data to target location/file
     *
     * @param inStream - InputStream to be saved
     * @param target - full path to destination file
     */
    public static void saveToFile(InputStream inStream, String target)
            throws IOException {
        OutputStream out = null;
        int read = 0;
        byte[] bytes = new byte[1024];

        out = new FileOutputStream(new File(target));
        while ((read = inStream.read(bytes)) != -1) {
            out.write(bytes, 0, read);
        }
        out.flush();
        out.close();
    }

    /**
     * Utility method to save String data to target location/file
     *
     * @param in - String to be saved
     * @param encoding
     * @param target - full path to destination file
     */
    public static void saveToFile(String in, String encoding, File target)
            throws IOException {

        FileUtils.writeStringToFile(target, in, encoding);
        /* OutputStream out = null;
        InputStream inStream = IOUtils.toInputStream(in, encoding);
        int read = 0;
        byte[] bytes = new byte[1024];
        
        out = new FileOutputStream(target);
        while ((read = inStream.read(bytes)) != -1) {
            out.write(bytes, 0, read);
        }
        out.flush();
        out.close();*/
    }

    public static void copyURLToDir(URL url, File dir) throws IOException {
        String file = url.getFile();
        org.apache.commons.io.FileUtils.copyURLToFile(url, new File(dir, file));
    }

    public static void copyURLToDir(String urlString, File dir) throws MalformedURLException, IOException {
        URL url = new URL(urlString);
        String file = url.getFile();
        org.apache.commons.io.FileUtils.copyURLToFile(url, new File(dir, file));
    }

    /**
     * *
     * Write the given stream into the given path. Keep previous old versions of
     * the file (if any are present) by appending these with a unix timestamp
     *
     * @param stream
     * @param target
     * @param keepPrevious
     * @throws IOException
     */
    public static void saveToFile(ByteArrayOutputStream stream, Path target, boolean keepPrevious)
            throws IOException {
        if (stream.size() > 0) {
            if (keepPrevious && Files.exists(target)) {
                String fileName = target.getFileName().toString();
                if (fileName.contains(".") && !fileName.startsWith(".")) {
                    String name = fileName.split("\\.")[0];
                    String extension = fileName.split("\\.")[1];
                    fileName = name + "-" + (System.currentTimeMillis() / 1000L) + "." + extension;
                } else {
                    fileName = fileName + "-" + (System.currentTimeMillis() / 1000L);
                }
                Path renamedTarget = target.resolveSibling(fileName);
                Files.move(target, renamedTarget);
            }
            FileOutputStream fos = new FileOutputStream(target.toFile());

            stream.writeTo(fos);
            stream.flush();
            stream.close();
            fos.flush();
            fos.close();
        } else {
            stream.flush();
            stream.close();
            throw new IOException("The provided stream contains no data. Nothing has been saved.");
        }
    }

    /**
     * Utility method to save OutputStream data to target location/file
     *
     */
    public static void saveToFile(ByteArrayOutputStream stream, String target)
            throws IOException {
        if (stream.size() > 0) {
            File file = new File(target);
            FileOutputStream fos = new FileOutputStream(file);
            stream.writeTo(fos);
            stream.writeTo(fos);
            stream.flush();
            stream.close();
            fos.flush();
            fos.close();
        } else {
            stream.flush();
            stream.close();
            throw new IOException("The provided stream contains no data. Nothing has been saved.");
        }
    }

    /**
     * Reserve a BufferedOutputStream to write file data to on a UNIX system
     * with group and permission settings.
     *
     * @param path
     * @param extension
     * @param perm
     * @param group
     * @param bufferSize
     * @return
     */
    public static BufferedOutputStream createFile(Path path, String extension, String owner, String perm, String group,
            int bufferSize) {
        if (path == null) {
            throw new IllegalArgumentException("The path is null.");
            //reportError("Path is null", null);
        }
        /*int els=path.getNameCount();
         Path file = path.getName(els-1);
         Path dir=path.subpath(0, els-1);
         String prefix = file.toString().split("\\.(?=[^\\.]+$)")[0];
         String suffix = file.toString().split("\\.(?=[^\\.]+$)")[1];
         */
        //FileAttribute<Set<PosixFilePermission>> attr = setPermAndGroup(path, perm, group);

        FileAttribute<Set<PosixFilePermission>> attr = getPerm(perm);
        if (FileUtils.isFile(path, extension)) {
            try {
                if (Files.notExists(path)) {
                    //path=Files.createFile(dir, prefix, suffix, attr);
                    if (attr == null) {
                        path = Files.createFile(path);
                    } else {
                        path = Files.createFile(path, attr);
                    }

                } else {
                    Files.delete(path);
                    //path=Files.createFile(dir, prefix, suffix, attr);
                    if (attr == null) {
                        path = Files.createFile(path);
                    } else {
                        path = Files.createFile(path, attr);
                    }
                }
                if (group != null) {
                    setGroup(path, group);
                }
                if (owner != null) {
                    setOwner(path, owner);
                }
            } catch (IOException ex) {
                reportError("Failed in creating file.", ex);
            }
        } else {
            throw new IllegalArgumentException("The path doesn't represent a file.");
        }
        /*
         try {
         Files.deleteIfExists(path);
         // } catch (NoSuchFileException x) {
         //System.err.format("%s: No such" + " file or directory%n", fullPath);
         //return null;
         //} catch (DirectoryNotEmptyException x) {
         //    System.err.format("%s not empty%n", fullPath);
         //    return null;
         } catch (IOException x) {
         // File permission problems are caught here.
         System.err.println(x);
         return null;
         }*/

        BufferedOutputStream fos = null;
        try {
            fos = new BufferedOutputStream(new FileOutputStream(path.toFile()), bufferSize);
        } catch (FileNotFoundException ex) {
            reportError("Failed in creating file", ex);
        }

        OutputStreamWriter out = null;
        try {
            out = new OutputStreamWriter(fos, "UTF-8");

        } catch (UnsupportedEncodingException ex) {
            Logger.getLogger(FileUtils.class.getName()).log(Level.SEVERE, "An exception occured.", ex);
        } catch (IOException ex) {
            Logger.getLogger(FileUtils.class.getName()).log(Level.SEVERE, "An exception occured.", ex);
        }
        return fos;
    }

    public static void createDirectoryIfNotExists(Path path) {
        if (!Files.exists(path)) {
            try {
                Files.createDirectories(path);
            } catch (IOException e) {
                Logger.getLogger(FileUtils.class.getName()).log(Level.SEVERE,
                        "Couldn't create directory: " + path.toString(), e);
            }
        }
    }

    private static void setGroup(Path path, String group) {

        UserPrincipalLookupService lookupservice = FileSystems.getDefault().getUserPrincipalLookupService();
        try {
            final GroupPrincipal earsGroup = lookupservice.lookupPrincipalByGroupName(group);
            Files
                    .getFileAttributeView(path, PosixFileAttributeView.class).setGroup(earsGroup);
        } catch (IOException ex) {
            reportError("Failed in setting group", ex);
        }
    }

    private static void setOwner(Path path, String owner) {

        UserPrincipalLookupService lookupservice = FileSystems.getDefault().getUserPrincipalLookupService();
        try {
            final UserPrincipal glassfishOwner = lookupservice.lookupPrincipalByName(owner);
            Files.getFileAttributeView(path, PosixFileAttributeView.class).setOwner(glassfishOwner);
        } catch (IOException ex) {
            reportError("Failed in setting owner", ex);
        }
    }

    private static FileAttribute<Set<PosixFilePermission>> getPerm(String perm) {
        if (perm != null) {
            Set<PosixFilePermission> perms = PosixFilePermissions.fromString(perm);
            FileAttribute<Set<PosixFilePermission>> attr = PosixFilePermissions.asFileAttribute(perms);
            return attr;
        }
        return null;
    }

    /**
     * Creates a folder to desired location if it not already exists
     *
     * @param dirName - full path to the folder
     * @throws SecurityException - in case you don't have permission to create
     * the folder
     */
    public static void createDirectoryIfNotExists(String dirName)
            throws SecurityException {
        File theDir = new File(dirName);
        if (!theDir.exists()) {
            theDir.mkdir();
        }
    }

    public static void deleteFilesNotDirs(File dir) {
        for (File file : dir.listFiles()) {
            if (!file.isDirectory()) {
                file.delete();
            }
        }
    }

    /**
     * Create a directory on a UNIX system with group and permission settings.
     *
     * @param path
     * @param perm
     * @param group
     * @param bufferSize
     * @return
     */
    public static void createDirectory(Path path, String perm, String group) {
        if (path == null) {
            reportError("Path is null", null);
        }
        FileAttribute<Set<PosixFilePermission>> attr = getPerm(perm);

        try {
            if (Files.notExists(path)) {
                Files.createDirectory(path, attr);
            } else if (!Files.isDirectory(path)) {
                Files.delete(path);
                Files.createDirectory(path, attr);
            }
            setGroup(path, group);
        } catch (IOException ex) {
            reportError("Failed in creating directory.", ex);
        }
    }

    /**
     * Replace a string by another string in a given path and write it to
     * another path.
     *
     * @param fromPath
     * @param saveToPath
     * @param replace
     * @param by
     */
    public static void replaceStringInFile(Path fromPath, Path saveToPath, String replace, String by) {
        try {
            //Path fromPath = Paths.get(fromUri);
            //Path toPath = Paths.get(toUri);
            Charset charset = StandardCharsets.UTF_8;
            String content = new String(Files.readAllBytes(fromPath), charset);
            content = content.replaceAll(replace, by);
            Files.write(saveToPath, content.getBytes(charset),
                    StandardOpenOption.CREATE,
                    StandardOpenOption.TRUNCATE_EXISTING);
        } catch (Exception e) {
            reportError(e.getMessage(), e);
        }
    }

    public static boolean findStringInFile(Path file, String find) {
        Charset charset = StandardCharsets.UTF_8;
        String content;
        try {
            content = new String(Files.readAllBytes(file), charset);
        } catch (IOException ex) {
            return false;
        }
        return content.contains(find);
    }

    public static boolean findStringInFile(File file, String find) {
        Charset charset = StandardCharsets.UTF_8;
        String content;
        try {
            content = new String(Files.readAllBytes(file.toPath()), charset);
        } catch (IOException ex) {
            return false;
        }
        return content.contains(find);
    }

    public static int countStringInFile(File file, String find) {
        Charset charset = StandardCharsets.UTF_8;
        String content;
        try {
            content = new String(Files.readAllBytes(file.toPath()), charset);
        } catch (IOException ex) {
            return 0;
        }
        return StringUtils.countMatches(content, find);
    }

    /**
     * Read all content as a string from a reader.
     *
     * @param rd
     * @return
     * @throws IOException
     */
    public static String readAll(Reader rd) throws IOException {
        StringBuilder sb = new StringBuilder();
        int cp;
        while ((cp = rd.read()) != -1) {
            sb.append((char) cp);
        }
        return sb.toString();
    }

    public static String readAll(String path, Charset encoding) throws IOException {
        byte[] encoded = Files.readAllBytes(Paths.get(path));
        return new String(encoded, encoding);
    }

    public static String readAll(Path path, Charset encoding) throws IOException {
        byte[] encoded = Files.readAllBytes(path);
        return new String(encoded, encoding);
    }

    /**
     * Read all content as a string from a (remote) url. Return null if the
     * connection can't be made. UTF-8 is understood.
     *
     * @param url
     * @return
     * @throws IOException
     */
    public static String readStringFromUrl(String url) {
        InputStreamReader isr;
        try (InputStream is = new URL(url).openStream()) {
            isr = new InputStreamReader(is, Charset.forName("UTF-8"));
            try (BufferedReader rd = new BufferedReader(isr)) {
                return readAll(rd);
            }
        } catch (IOException e) {
            return null;
        }
    }

    /**
     * Read the first length chars of content as a string from a (remote) url.
     * UTF-8 is understood.
     *
     * @param url
     * @param length
     * @return
     * @throws IOException
     */
    public static String readStringFromUrl(String url, int length) throws IOException {
        InputStreamReader isr;
        try (InputStream is = new URL(url).openStream()) {
            isr = new InputStreamReader(is, Charset.forName("UTF-8"));
            try (BufferedReader rd = new BufferedReader(isr)) {
                return readFirstN(rd, length);
            }
        } catch (IOException e) {
            return null;
        }
    }

    /**
     * Read the first length chars of content as a string from an InputStream.
     * Provide a -1 length to return the whole inputstream.
     *
     * @param is
     * @param length
     * @return
     * @throws IOException
     */
    public static String readStringFromInputStream(InputStream is, int length) throws IOException {
        try (BufferedReader rd = new BufferedReader(new InputStreamReader(is, Charset.forName("UTF-8")))) {
            if (length > -1) {
                return readFirstN(rd, length);
            } else if (length == -1) {
                return readAll(rd);
            }

        }
        return null;
    }

    /**
     * Read the first n chars from a reader.
     *
     * @param rd
     * @return
     * @throws IOException
     */
    public static String readFirstN(Reader rd, int n) throws IOException {
        StringBuilder sb = new StringBuilder();
        int cp;
        int count = 0;
        while ((cp = rd.read()) != -1 && count < n) {
            count++;
            sb.append((char) cp);
        }
        return sb.toString();
    }

    private static void reportError(String message, Exception e) {
        //ExceptionHelper.reportError(message, e, this.getClass());
    }

    public static void executeCommand(String cmd) {
        String s;
        java.lang.Process p;
        try {
            p = Runtime.getRuntime().exec(cmd);
            BufferedReader br = new BufferedReader(
                    new InputStreamReader(p.getInputStream()));
            while ((s = br.readLine()) != null) {
                Logger.getLogger(FileUtils.class.getName()).info("line: " + s);
            }
            p.waitFor();
            Logger.getLogger(FileUtils.class.getName()).info("exit: " + p.exitValue());
            p.destroy();
        } catch (Exception e) {
        }
    }

    public static boolean websiteIsAvailable(String urlS) {
        try {
            final URL url = new URL(urlS);
            final HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            int statusCode = conn.getResponseCode();
            Logger.getLogger(FileUtils.class.getName())
                    .info("Website " + urlS + " returns status code:" + statusCode + ".");
            conn.connect();
            return statusCode == 200;

        } catch (Exception e) {
            Logger.getLogger(FileUtils.class.getName()).info("Connecting to " + urlS + " failed: "
                    + e.getClass().getSimpleName() + " thrown when trying to connect.");
            return false;
        }
    }

    /**
     * *
     * Return whether a HttpURLConnection can be made with the givedn url.
     * Return false if the url is null or if connecting results in an exception.
     *
     * @param url
     * @return
     */
    public static boolean websiteIsAvailable(URL url) {
        if (url == null) {
            return false;
        }
        try {
            final HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            int statusCode = conn.getResponseCode();
            Logger.getLogger(FileUtils.class.getName())
                    .info("Website " + url.toString() + " returns status code:" + statusCode + ".");
            conn.connect();
            return statusCode == 200;

        } catch (Exception e) {
            Logger.getLogger(FileUtils.class.getName()).info("Connecting to " + url.toString() + " failed: "
                    + e.getClass().getSimpleName() + " thrown when trying to connect.");
            return false;
        }
    }

    /**
     * *
     * Test whether the given path represents a file with a given extension,
     * regardless of whether it exists.
     *
     * @return
     */
    public static boolean isFile(Path path, String extension) {
        extension = extension.replace(".", "");
        String nameArr[] = path.toFile().getName().split("\\.");
        if (nameArr.length < 2) {
            return false;
        }
        String thisExtension = nameArr[nameArr.length - 1];
        return thisExtension.equalsIgnoreCase(extension);
    }

    /**
     * **
     * Return the dir String of the first file that matches a given wildcard in
     * a given directory.
     *
     * @param dir
     * @param wc
     * @return
     */
    public static String findByWildcard(String dir, String wc) {
        File dirFile = new File(dir);
        FileFilter fileFilter = new WildcardFileFilter(wc);
        File[] files = dirFile.listFiles(fileFilter);
        if (files.length > 0) {
            return files[0].getAbsolutePath();
        }
        return null;
    }

    /**
     * **
     * Return the dir String of all the files that matches a given wildcard in a
     * given directory.
     *
     * @param dir
     * @param wc
     * @return
     */
    public static List<File> findByWildcardMultiple(String dir, String wc) {
        File dirFile = new File(dir);
        FileFilter fileFilter = new WildcardFileFilter(wc);
        File[] files = dirFile.listFiles(fileFilter);
        return Arrays.asList(files);
    }

    /**
     * Return the last file (alphabetically) in a directory.
     *
     * @param dir
     * @param extension
     * @return
     */
    public static File findLastFileByNameInDir(String dir, String extension) {
        File dirFile = new File(dir);
        File[] files = dirFile.listFiles(new FileFilter() {
            public boolean accept(File file) {
                return file.isFile();
            }
        });
        File choice = null;
        if (files != null) {
            String lastName = "";

            for (File file : files) {
                if (file.getName().compareTo(lastName) > 0 && file.getName().endsWith(extension)) {
                    choice = file;
                    lastName = file.getName();
                }
            }
        }
        return choice;
    }

    /**
     * Return the last file (alphabetically) in given set.
     *
     * @param dir
     * @param extension
     * @return
     */
    public static File findLastFileByNameInSet(List<File> files, String extension) {
        File choice = null;
        if (files != null) {
            String lastName = "";

            for (File file : files) {
                if (file.getName().compareTo(lastName) > 0 && file.getName().endsWith(extension)) {
                    choice = file;
                    lastName = file.getName();
                }
            }
        }
        return choice;
    }

    /**
     * Return the last file (alphabetically) that corresponds to a wildcard in a
     * directory.
     *
     * @param dir
     * @param extension
     * @return
     */
    public static File findLastFileInDirByWildCard(String dir, String wildCard, String extension) {
        return findLastFileByNameInSet(findByWildcardMultiple(dir, wildCard), extension);
    }

    public static File downloadFromUrl(URL website, File targetDir, String fileName) throws IOException {
        if (website == null) {
            throw new IllegalArgumentException("Provided website URL is null.");
        }
        if (targetDir == null) {
            throw new IllegalArgumentException("Provided target Directory is null.");
        }
        if (fileName == null) {
            throw new IllegalArgumentException("Provided fileName String is null.");
        }
        try (final InputStream in = website.openStream()) {
            File targetFile = new File(targetDir, fileName);
            Files.deleteIfExists(targetFile.toPath());
            OutputStream outStream = new FileOutputStream(targetFile);
            byte[] buffer = new byte[8 * 1024];
            int bytesRead;
            while ((bytesRead = in.read(buffer)) != -1) {
                outStream.write(buffer, 0, bytesRead);
            }
            IOUtils.closeQuietly(in);
            IOUtils.closeQuietly(outStream);
            return targetFile;
        } catch (IOException ex) {
            throw ex;
        }
    }

    public static File decompress7zFile(File in, File destinationPath) throws IOException {
        SevenZFile sevenZFile = new SevenZFile(in);
        File outFile = null;
        SevenZArchiveEntry entry;
        while ((entry = sevenZFile.getNextEntry()) != null) {
            if (entry.isDirectory()) {
                continue;
            }
            outFile = new File(destinationPath, entry.getName());
            File parent = outFile.getParentFile();
            if (!parent.exists()) {
                parent.mkdirs();
            }
            FileOutputStream out = new FileOutputStream(outFile);
            byte[] content = new byte[(int) entry.getSize()];
            sevenZFile.read(content, 0, content.length);
            out.write(content);
            out.close();
        }
        return outFile;
    }

    public static File decompressGzipFile(File in, File destinationPath) throws FileNotFoundException, IOException {
        File outFile = new File(destinationPath, in.getName().replace(".gz", ""));

        FileInputStream fis = new FileInputStream(in);
        GZIPInputStream gis = new GZIPInputStream(fis);
        FileOutputStream fos = new FileOutputStream(outFile);
        byte[] buffer = new byte[1024];
        int len;
        while ((len = gis.read(buffer)) != -1) {
            fos.write(buffer, 0, len);
        }
        //close resources
        fos.close();
        gis.close();

        return outFile;
    }

    public static String checksumMD5(Path file)
            throws IOException {
        MessageDigest md;
        try {
            md = MessageDigest.getInstance("MD5");

            md.update(Files.readAllBytes(file));
            byte[] digest = md.digest();
            String myChecksum = DatatypeConverter
                    .printHexBinary(digest).toUpperCase();
            return myChecksum;
        } catch (NoSuchAlgorithmException e) {
            return null;
        }
    }

    public static String checksumCRC32(Path file)
            throws IOException {
        return Long.toString(FileUtils.checksumCRC32(file.toFile()));
    }
}
