package be.naturalsciences.bmdc.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.StringTokenizer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

/**
 *
 * @author Thomas Vandenberghe
 */
public class StringUtils extends org.apache.commons.lang3.StringUtils {

    public static Pattern T_TABS = Pattern.compile("\t");
    public static Pattern T_SPACES = Pattern.compile(" +");
    public static Pattern T_NEWLINES = Pattern.compile("\r\n");
    public static Pattern T_NEWLINES2 = Pattern.compile("\n");
    public static Pattern T_EMPTYLINES = Pattern.compile("([\\n\\r]+\\s*)*$");

    /*public static String concatString(Collection<String> strings, String separator) {
        StringBuilder sb = new StringBuilder();
        int i = 0;
        int l = strings.size();
        for (String s : strings) {
            sb.append(s);
            if (l > 1 && i < l - 1) {
                sb.append(separator);
            }
            i++;
        }
        return sb.toString();
    }*/
    public static int countOccurences(String line, String of) {
        return line.length() - line.replace(of, "").length();
    }

    public static String[] parseUrn(String urn) {
        return urn.split(":");
    }

    public static String getLastUrnPart(String urn) {
        String[] parts = urn.split(":");
        return parts[parts.length - 1];
    }

    /**
     * *
     * Retrieve the regex group defined in the provided Pattern ptFind from a
     * provided url assign it to a Map with given key key, and stop at a
     * provided Pattern ptStop. Only matching lines where the key is found are
     * added.
     *
     * @param url
     * @param ptFind
     * @param ptStop
     * @param key
     * @return
     */
    public static Map<String, String> getRegexFromUrl(String url, Pattern ptFind, Pattern ptStop, String key) throws MalformedURLException, IOException {
        return getRegexFromStream(new URL(url).openStream(), ptFind, ptStop, key);
    }

    /* public static Map<String, String> getRegexFromFile(File file, Pattern ptFind, Pattern ptStop, String key) throws FileNotFoundException {
        return getRegexFromStream(new BufferedInputStream(new FileInputStream(file)), ptFind, ptStop, key);
    }*/
    public static Map<String, String> getRegexFromFile(File file, Pattern ptFind, Pattern ptStop, String key) throws IOException {
        return getRegexFromReader(new BufferedReader(new FileReader(file)), ptFind, ptStop, key);
    }

    /**
     * *
     * Read a file line by line and return all matches
     *
     * @param file
     * @param ptFind
     * @param ptStop
     * @return
     * @throws IOException
     */
    public static List<List<String>> getRegexFromFile(File file, Pattern ptFind, Pattern ptStop) throws IOException {
        List<List<String>> res = new ArrayList<>();
        BufferedReader reader = new BufferedReader(new FileReader(file));
        String line;
        while ((line = reader.readLine()) != null) {
            List<String> multiplePatternGroupResult = getRegexGroupResults(line, ptFind);
            if (multiplePatternGroupResult.size() > 0) {
                res.add(multiplePatternGroupResult);
            }
        }
        return res;
    }

    /**
     * *
     * Read a String line by line and return all matches. They are grouped by
     * line first and then by capturing pattern group next.
     *
     * @param lines
     * @param ptFind
     * @param ptStop
     * @return
     * @throws IOException
     */
    public static List<List<String>> getRegexResultFromMultilineString(String lines, Pattern ptFind, Pattern ptStop) {
        List<List<String>> res = new ArrayList<>();
        Scanner scanner = new Scanner(lines);
        while (scanner.hasNextLine()) {
            String line = scanner.nextLine();
            List<String> multiplePatternGroupResult = getRegexGroupResults(line, ptFind);
            if (multiplePatternGroupResult.size() > 0) {
                res.add(multiplePatternGroupResult);
            }
        }
        return res;
    }

    public static <E extends Object> List<E> flattenListOfLists(List<List<E>> listOfLists) {
        List<E> result = new ArrayList<>();
        listOfLists.forEach(result::addAll);
        return result;
    }

    /**
     * *
     * Return a list of all captured groups that are found in the provided
     * String line and match the Pattern patternString. patternString must
     * contain at least one capturing group.
     *
     * @param line
     * @param ptFind
     * @return
     */
    public static List<String> getRegexGroupResults(String string, Pattern ptFind) {
        int nbPatterns = StringUtils.countMatches(ptFind.pattern(), "(");
        List<String> results = new ArrayList(nbPatterns);
        Matcher matcher = ptFind.matcher(string);

        while (matcher.find()) {
            for (int i = 0; i < nbPatterns; i++) {
                results.add(matcher.group(i + 1));
            }
        }
        return results;
    }

    private static boolean getSinglePatternGroupResult(String line, Pattern ptFind, Pattern ptStop, String key, Map<String, String> res) {
        String r = null;
        Matcher m = ptFind.matcher(line);

        if (line.contains(key)) {
            if (m.find()) {
                r = m.group(1);
                res.put(key, r);
                return false;
            }
        }
        if (ptStop != null) {
            Matcher m2 = ptStop.matcher(line);
            if (m2.find()) {
                return true; //return endFound to caller
            }
        }
        return false;
    }

    public static Map<String, String> getRegexFromReader(BufferedReader reader, Pattern ptFind, Pattern ptStop, String key) throws IOException {
        Map<String, String> res = new HashMap<>();
        String line;
        while ((line = reader.readLine()) != null) {
            boolean endFound = getSinglePatternGroupResult(line, ptFind, ptStop, key, res);
            if (endFound) {
                reader.close();
                break;
            }
        }
        return res;
    }

    public static Map<String, String> getRegexFromStream(InputStream is, Pattern ptFind, Pattern ptStop, String key) {

        Map<String, String> res = new HashMap<>();
        Scanner scanner = new Scanner(is);
        String r = null;

        while (scanner.hasNextLine()) {

            String line = scanner.nextLine();
            boolean endFound = getSinglePatternGroupResult(line, ptFind, ptStop, key, res);
            if (endFound) {
                scanner.close();
                break;
            }

        }
        scanner.close();
        return res;
    }

    public static boolean containsViceVersa(String source, String subItem) {
        if (source == null || subItem == null) {
            throw new IllegalArgumentException("Arguments can't be null");
        }
        return source.equals(subItem) || source.contains(subItem) || subItem.contains(source);
    }

    /**
     * *
     * Test if the String source contains the string subItem as a whole word and
     * if the String subItem contains the string source as a whole word.
     *
     * @param source
     * @param subItem
     * @return
     */
    public static boolean wholeWordContainsViceVersa(String source, String subItem) {
        if (source == null || subItem == null || source.equals("") || subItem.equals("")) {
            return false;
        }
        if (source.equals(subItem)) {
            return true;
        }
        boolean r = false;
        String pattern = "\\b" + subItem + "\\b";
        Pattern p;
        Matcher m;
        try {
            p = Pattern.compile(pattern);
            m = p.matcher(source);
            r = m.find();
        } catch (PatternSyntaxException ex) {
            r = false;
        }
        try {
            pattern = "\\b" + source + "\\b";
            p = Pattern.compile(pattern);
            m = p.matcher(subItem);
            r = r || m.find();
        } catch (PatternSyntaxException ex) {
            r = r || false;
        }
        return r;
    }

    public static Map<String, ArrayList<String>> mappifyPropertyString(String propString, String KEYVAL_DELIM, String PROP_DELIM, String REGEX_PROP_DELIM, String REGEX_KEYVAL_DELIM) {
        Map r = new HashMap<String, String>();
        if (propString.contains(KEYVAL_DELIM)) {//must contain at least one key-value pair
            if (StringUtils.countOccurences(propString, "|") > 1 && propString.contains(PROP_DELIM)) { //if has more than one key-value pair, then must be delimited correctly
                propString = propString.replace("\\", "");
                String properties[] = propString.split(REGEX_PROP_DELIM);
                for (int i = 0; i < properties.length; i++) {
                    String property = properties[i];
                    String keyVal[] = property.split(REGEX_KEYVAL_DELIM);
                    if (!r.containsKey(keyVal[0])) {
                        List l = new ArrayList();
                        l.add(keyVal[1]);
                        r.put(keyVal[0], l);
                    } else {
                        List vals = (ArrayList) r.get(keyVal[0]);
                        vals.add(keyVal[1]);
                        r.put(keyVal[0], vals);
                    }
                }
            }
        }
        return r;
    }

    public static boolean isURL(String url) {
        try {
            new URL(url);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public static StringBuilder stringBuilderReplace(StringBuilder sb, String from, String to) {
        return new StringBuilder(sb.toString().replace(from, to));
    }

    public static StringBuilder stringBuilderReplaceAll(StringBuilder sb, Pattern fromRegex, String toRegex) {
        Matcher m = fromRegex.matcher(sb);
        return new StringBuilder(m.replaceAll(toRegex));
    }

    public static String replaceAll(String input, Map<Pattern, String> replacements) {
        StringBuilder inputSb = new StringBuilder(input);

        for (Iterator<Map.Entry<Pattern, String>> entries = replacements.entrySet().iterator(); entries.hasNext();) { //all this to maintain the iteration maintains the original insertion order!
            Map.Entry<Pattern, String> entry = entries.next();
            Pattern pt = entry.getKey();
            String rep = entry.getValue();
            inputSb = new StringBuilder(pt.matcher(inputSb).replaceAll(rep));
        }

        /*   for (Map.Entry<Pattern, String> entry : replacements.entrySet()) {
            Pattern pt = entry.getKey();
            String rep = entry.getValue();
            inputSb = new StringBuilder(pt.matcher(inputSb).replaceAll(rep));
        }*/
        return inputSb.toString();
    }

    /**
     * *
     * Get a list of all non-matching parts between 2 strings.
     *
     * https://stackoverflow.com/questions/18344721/extract-the-difference-between-two-strings-in-java/18344837
     *
     * @param sourceStr
     * @param anotherStr
     * @return
     */
    public static List<String> findNotMatching(String sourceStr, String anotherStr) {
        StringTokenizer at = new StringTokenizer(sourceStr, " ");
        StringTokenizer bt = null;
        int i = 0, token_count = 0;
        String token = null;
        boolean flag = false;
        List<String> missingWords = new ArrayList<String>();
        while (at.hasMoreTokens()) {
            token = at.nextToken();
            bt = new StringTokenizer(anotherStr, " ");
            token_count = bt.countTokens();
            while (i < token_count) {
                String s = bt.nextToken();
                if (token.equals(s)) {
                    flag = true;
                    break;
                } else {
                    flag = false;
                }
                i++;
            }
            i = 0;
            if (flag == false) {
                missingWords.add(token);
            }
        }
        return missingWords;
    }

    /**
     * *
     *
     * @param input
     * @return
     */
    public static String flattenString(String input) {
        return input.replaceAll(T_TABS.pattern(), "").replaceAll(T_NEWLINES.pattern(), "").replaceAll(T_NEWLINES2.pattern(), "").replaceAll(T_EMPTYLINES.pattern(), "");
    }

    public static double round(double value, int places) {
        if (places < 0) {
            throw new IllegalArgumentException();
        }

        BigDecimal bd = new BigDecimal(value);
        bd = bd.setScale(places, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }

    public static boolean isNumeric(String str) {
        return str.matches("-?\\d+(\\.\\d+)?");  //match a number with optional '-' and decimal.
    }

    public static boolean isDouble(String str) {
        if (str == null) {
            return false;
        }
        str = str.replace(",", ".");
        try {
            double d = Integer.parseInt(str);
            return false;
        } catch (NumberFormatException nfe) {
            try {
                double d = Double.parseDouble(str);
            } catch (NumberFormatException nfe2) {
                return false;
            }
        }

        return true;
    }

    public static boolean isInteger(String str) {
        if (str == null) {
            return false;
        }
        try {
            int d = Integer.parseInt(str);
        } catch (NumberFormatException nfe) {
            return false;
        }
        return true;
    }

    public static boolean isText(String str) {
        Pattern p = Pattern.compile("[a-zA-Z_\\-/\\\\]+");
        Matcher m = p.matcher(str);
        return m.find();
    }

}
