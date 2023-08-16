/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package be.naturalsciences.bmdc.utils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 *
 * @author thomas
 */
public class FilesizeGetter {

    private final URLConnection connection;
    private final URL url;

    private static Map<URL, Double> URL_SIZE_MAP_B = new LinkedHashMap<>(); // maintain insertion order!

    public URLConnection getConnection() {
        return connection;
    }

    public URL getUrl() {
        return url;
    }

    public FilesizeGetter(URL url, int readTimeoutMillis) throws IOException {
        this.url = url;
        this.connection = url.openConnection();
        this.connection.setConnectTimeout(5000);
        this.connection.setReadTimeout(readTimeoutMillis);
    }

    public Double getFilesizeInMBytesOfUrl(boolean isExpectedToBeBigFile) throws IOException {
        if (URL_SIZE_MAP_B.get(this.url) != null) {
            return URL_SIZE_MAP_B.get(this.url) / 1000000D;
        } else {
            Double sizeInB = null;
            Double sizeInMB = null;
            sizeInB = getFilesizeInBytesOfUrl(isExpectedToBeBigFile); // format.getDistributionFormatMime() !=
                                                                      // FormatEnum.NETCDF ||
                                                                      // format.getDistributionFormatMime() ==
                                                                      // FormatEnum.GML
            if (sizeInB != null) {
                sizeInMB = (sizeInB / 1000000D);
            }

            return sizeInMB;
        }
    }

    public Double getFilesizeInBytesOfUrl(boolean isExpectedToBeLargeFile) throws IOException {
        if (URL_SIZE_MAP_B.get(url) != null) {
            return URL_SIZE_MAP_B.get(url);
        } else {
            HttpURLConnection httpConn = null;
            Double sizeInB = null;
            if (connection instanceof HttpURLConnection) {
                httpConn = (HttpURLConnection) connection;
                httpConn.setRequestMethod("HEAD");
                int responseCode = 0;
                responseCode = httpConn.getResponseCode();

                if (responseCode == 302) {
                    httpConn.disconnect();
                    String newUrlString = this.url.toString().replaceFirst("^http://", "https://");
                    URL newUrl = new URL(newUrlString);
                    if (!newUrl.equals(this.url)) {
                        FilesizeGetter child = new FilesizeGetter(newUrl, this.connection.getReadTimeout());
                        return child.getFilesizeInBytesOfUrl(isExpectedToBeLargeFile);
                    }
                } else if (responseCode == HttpURLConnection.HTTP_OK) {
                    String sizeHeader = connection.getHeaderField("Content-Length");
                    if (sizeHeader != null) {
                        sizeInB = Double.parseDouble(sizeHeader);
                    }
                    if ((sizeInB == null || sizeInB == 0D) && !isExpectedToBeLargeFile) {
                        httpConn.disconnect();
                        httpConn = (HttpURLConnection) connection.getURL().openConnection();
                        // httpConn = (HttpURLConnection)
                        // convertedDistributionResourceFileUrl.openConnection();
                        httpConn.setRequestMethod("GET");

                        long contentLengthLong = httpConn.getContentLengthLong(); // / 1000000;

                        if (contentLengthLong > 0) {
                            sizeInB = new Double(contentLengthLong);// / 1000000;}
                        } else {
                            File tmp = new File(System.getProperty("java.io.tmpdir"));
                            File tmpfile = Paths.get(tmp.getPath(), "tmpfile").toFile();
                            FileUtils.copyURLToFile(url, tmpfile, 10000, 10000);
                            sizeInB = new Double(Files.size(tmpfile.toPath()));
                        }
                        httpConn.disconnect();
                        URL_SIZE_MAP_B.put(url, sizeInB);
                    }
                } else {
                    throw new FileNotFoundException(
                            String.format("No valid resource found at url %s", this.url.toString()));
                }
            } else { //for instance for FTP connections, just download it.
                File tmp = new File(System.getProperty("java.io.tmpdir"));
                File tmpfile = Paths.get(tmp.getPath(), "tmpfile").toFile();
                FileUtils.copyURLToFile(url, tmpfile, 10000, 10000);
                sizeInB = new Double(Files.size(tmpfile.toPath()));
            }

            return sizeInB;
        }
    }
}
