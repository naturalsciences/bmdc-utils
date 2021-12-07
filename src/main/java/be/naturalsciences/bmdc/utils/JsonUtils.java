/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package be.naturalsciences.bmdc.utils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.lang.reflect.Type;
import java.util.Collection;
import java.util.Map;
import java.util.Set;
import org.apache.commons.collections4.map.SingletonMap;
import gnu.trove.set.hash.THashSet;
import com.google.gson.reflect.TypeToken;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.Charset;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 *
 * @author thomas
 */
public class JsonUtils {

    public static Gson gson = new GsonBuilder().disableHtmlEscaping().enableComplexMapKeySerialization().setLenient().create();
    public static String TOOL_DELIM = ",";

    /**
     * *
     * Turns a Map of with keys String urls and String names into a complete
     * String. The order provided by the LinkedHashSet will be kept.
     *
     * @param concepts
     * @return
     */
    public static String serializeConcepts(Map<String, String> concepts) {
        if (concepts.size() > 1) {
            Type termType = new TypeToken<List<Map<String, String>>>() {
            }.getType();
            List<Map<String, String>> cs = CollectionUtils.mapToListofMaps(concepts);
            String json = gson.toJson(cs, termType);
            return json;
        } else {
            return serializeConcept(new SingletonMap<>(concepts));
        }
    }

    /**
     * *
     * Convert a provided tool uri and tool name to a json tool representation
     *
     * @param uri
     * @param name
     * @return
     */
    public static String serializeConcept(String uri, String name) {
        Set<String> result = new THashSet<>();
        SingletonMap toolMap = new SingletonMap(uri, name);
        result.add(serializeConcept(toolMap));

        return StringUtils.join(result, TOOL_DELIM);
    }

    public static String serializeConcept(SingletonMap<String, String> concept) {
        String json = gson.toJson(concept);
        return json;
    }

    public static Map<String, String> deserializeConcepts(Collection<String> jsonConcepts) {
        /*Map<String, String> r = new LinkedHashMap();
        for (String jsonConcept : jsonConcepts) {
            r.putAll(deserializeConcept(jsonConcept));
        }
        return r;*/
        //{a:b}
        //"{a:b, c:d}"
        String jsonConcept = StringUtils.join(jsonConcepts, ",");
        jsonConcept = jsonConcept.replace("[", "");
        jsonConcept = jsonConcept.replace("]", "");
        jsonConcept = "[" + jsonConcept + "]";
        Type termType = new TypeToken<List<Map<String, String>>>() {
        }.getType();
        List<Map<String, String>> r = gson.fromJson(jsonConcept, termType);

        return CollectionUtils.listOfmapsToMap(r);
    }

    public static SingletonMap<String, String> deserializeConcept(String jsonConcept) {
        Type termType = new TypeToken<Map<String, String>>() {
        }.getType();
        Map<String, String> intermediate = gson.fromJson(jsonConcept, termType);

        SingletonMap<String, String> r = new SingletonMap(intermediate);
        String label = r.get(r.firstKey());
        label = label.replaceAll("\\+", " ");
        r.put(r.firstKey(), label);
        return r;
        /* Type type = new TypeToken<Map>() {
        }.getType();
        try {
            Map fromJson = gson.fromJson(jsonConcept, type);
            SingletonMap<String, String> r = new SingletonMap(fromJson);
            String label = r.get(r.firstKey());
            label = label.replaceAll("\\+", " ");
            r.put(r.firstKey(), label);
            return r;
        } catch (Exception e) {
            SingletonMap<String, String> r = new SingletonMap("pseudokey", jsonConcept);
            String label = r.get(r.firstKey());
            label = label.replaceAll("\\+", " ");
            r.put(r.firstKey(), label);
            return r;
        }
         */
    }

    private static String readAll(Reader rd) throws IOException {
        StringBuilder sb = new StringBuilder();
        int cp;
        while ((cp = rd.read()) != -1) {
            sb.append((char) cp);
        }
        return sb.toString();
    }

    public static JSONObject readJsonObjectFromFile(File file) throws IOException, JSONException {
        try ( InputStream is = new FileInputStream(file)) {
            BufferedReader rd = new BufferedReader(new InputStreamReader(is, Charset.forName("UTF-8")));
            String jsonText = readAll(rd);
            JSONObject json = new JSONObject(jsonText);

            return json;
        }
    }

    public static JSONObject readJsonObjectFromUrl(String url) throws IOException, JSONException {
        return readJSONObjectFromUrl(new URL(url));
    }

    public static JSONObject readJSONObjectFromUrl(URL url) throws IOException, JSONException {
        try ( InputStream is = url.openStream()) {
            BufferedReader rd = new BufferedReader(new InputStreamReader(is, Charset.forName("UTF-8")));
            String jsonText = readAll(rd);
            JSONObject json = new JSONObject(jsonText);
            return json;
        }
    }

    public static String readJsonStringFromUrl(URL url) throws IOException {
        //url = new URL(url.toString().replace(" ", "%20"));

        URI uri;
        try {
            uri = new URI(url.getProtocol(), url.getUserInfo(), url.getHost(), url.getPort(), url.getPath(), url.getQuery(), url.getRef());
        } catch (URISyntaxException ex) {
            throw new IOException(ex);
        }
        url = new URL(uri.toASCIIString());
        URLConnection conn = url.openConnection();
        InputStreamReader isr;
        try ( InputStream is = conn.getInputStream()) {
            isr = new InputStreamReader(is, Charset.forName("UTF-8"));
            try ( BufferedReader rd = new BufferedReader(isr)) {
                return readAll(rd);
            }
        } catch (IOException e) {
            Logger.getLogger(JsonUtils.class.getName()).log(Level.WARNING, "URL " + url + " not found.");
            int responseCode = 0;
            // try {
            if (conn != null) {
                // Casting to HttpURLConnection allows calling getResponseCode
                responseCode = ((HttpURLConnection) conn).getResponseCode();
                throw new IOException("HTTP Response code:" + String.valueOf(responseCode), e);
            } else {
                Logger.getLogger(FileUtils.class.getName()).log(Level.SEVERE, "connection is null.");
            }
            // } catch (IOException ex2) {
            //   Logger.getLogger(FileUtils.class.getName()).log(Level.SEVERE, "An exception occured.", ex2);
            //}
        }
        return null;
    }

    public static String readJsonStringFromUrl(String url) throws IOException {
        return readJsonStringFromUrl(new URL(url));
    }

    /**
     * *
     * Populate a provided key-value pair map (values may be empty), given the
     * provided json string, which contains corresponding key-value pairs.
     *
     * @param keyVals
     * @param json
     * @return
     */
    public static Map getJsonKeyVals(Map<String, String> keyVals, String json) {
        for (String key : keyVals.keySet()) {
            String pattern = "(\"" + key + "\"): *\"(.*?)\""; //json style
            Pattern p = Pattern.compile(pattern);
            Matcher m = p.matcher(json);
            if (m.find()) {
                keyVals.put(key, m.group(2));
            } else {
                pattern = "<" + key + ">(.*?)<\\/" + key + ">"; //xml style
                p = Pattern.compile(pattern);
                m = p.matcher(json);
                if (m.find()) {
                    keyVals.put(key, m.group(1));
                }
            }
        }
        return keyVals;
    }

    public static boolean isValidJSON(String test) {
        try {
            new JSONObject(test);
        } catch (JSONException ex) {
            // edited, to include @Arthur's comment
            // e.g. in case JSONArray is valid as well...
            try {
                new JSONArray(test);
            } catch (JSONException ex1) {
                return false;
            }
        }
        return true;
    }

}
