/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package be.naturalsciences.bmdc.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.Ignore;

/**
 *
 * @author thomas
 */
public class StringUtilsTest {

    public StringUtilsTest() {
    }

    @BeforeClass
    public static void setUpClass() {
    }

    @AfterClass
    public static void tearDownClass() {
    }

    @Before
    public void setUp() {
    }

    @After
    public void tearDown() {
    }

    /**
     * Test of getRegexFromLine method, of class StringUtils.
     */
    @Test
    public void testGetMultiplePatternGroupResult() {
        System.out.println("matcher");
        String line = "{\"AphiaID\":1,\"rank\":\"Superdomain\",\"scientificname\":\"Biota\",\"child\":{\"AphiaID\":2,\"rank\":\"Kingdom\",\"scientificname\":\"Animalia\",\"child\":{\"AphiaID\":1065,\"rank\":\"Phylum\",\"scientificname\":\"Arthropoda\",\"child\":{\"AphiaID\":1066,\"rank\":\"Subphylum\",\"scientificname\":\"Crustacea\",\"child\":{\"AphiaID\":845959,\"rank\":\"Superclass\",\"scientificname\":\"Multicrustacea\",\"child\":{\"AphiaID\":889925,\"rank\":\"Class\",\"scientificname\":\"Hexanauplia\",\"child\":{\"AphiaID\":1080,\"rank\":\"Subclass\",\"scientificname\":\"Copepoda\",\"child\":{\"AphiaID\":155876,\"rank\":\"Infraclass\",\"scientificname\":\"Neocopepoda\",\"child\":{\"AphiaID\":155879,\"rank\":\"Superorder\",\"scientificname\":\"Podoplea\",\"child\":{\"AphiaID\":1101,\"rank\":\"Order\",\"scientificname\":\"Cyclopoida\",\"child\":{\"AphiaID\":1103,\"rank\":\"Suborder\",\"scientificname\":\"Poecilostomatoida\",\"child\":{\"AphiaID\":128595,\"rank\":\"Family\",\"scientificname\":\"Sapphirinidae\",\"child\":{\"AphiaID\":347103,\"rank\":\"Genus\",\"scientificname\":\"Edwardsia\",\"child\":null}}}}}}}}}}}}}";
        String patternString = ":(\\d*),";
        List<String> expResult = null;
        List<String> result = StringUtils.getRegexGroupResults(line, Pattern.compile(patternString));
        assertTrue(result.size() > 5);
    }

    public void testGetMultiplePatternGroupResult2() {
        System.out.println("matcher");
        String line = "AphiaID";
        String patternString = "(AphiaID)";
        List<String> result = StringUtils.getRegexGroupResults(line, Pattern.compile(patternString));
        assertTrue(result.size() > 0);
    }

    private static Pattern d5 = Pattern.compile("\\d{5}");

    @Test
    public void testReplaceAll() {
        System.out.println("Replace all");
        Map<Pattern, String> repl = new HashMap();
        String d5T = "THISWASADIGIT";
        repl.put(d5, d5T);

        String input = "Beverly Hills 90510";
        input = StringUtils.replaceAll(input, repl);
        assertTrue(input.contains(d5T));
        assertFalse(input.contains("90510"));
    }

    /**
     * Test of countOccurences method, of class StringUtils.
     */
    @Test
    @Ignore
    public void testCountOccurences() {
        System.out.println("countOccurences");
        String line = "";
        String of = "";
        int expResult = 0;
        int result = StringUtils.countOccurences(line, of);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of parseUrn method, of class StringUtils.
     */
    @Test
    @Ignore
    public void testParseUrn() {
        System.out.println("parseUrn");
        String urn = "";
        String[] expResult = null;
        String[] result = StringUtils.parseUrn(urn);
        assertArrayEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getLastUrnPart method, of class StringUtils.
     */
    @Test
    @Ignore
    public void testGetLastUrnPart() {
        System.out.println("getLastUrnPart");
        String urn = "";
        String expResult = "";
        String result = StringUtils.getLastUrnPart(urn);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getRegexFromUrl method, of class StringUtils.
     */
    @Test
    @Ignore
    public void testGetRegexFromUrl() throws Exception {
        System.out.println("getRegexFromUrl");
        String url = "";
        Pattern ptFind = null;
        Pattern ptStop = null;
        String key = "";
        Map<String, String> expResult = null;
        Map<String, String> result = StringUtils.getRegexFromUrl(url, ptFind, ptStop, key);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getRegexFromFile method, of class StringUtils.
     */
    @Test
    @Ignore
    public void testGetRegexFromFile_4args() throws Exception {
        System.out.println("getRegexFromFile");
        File file = null;
        Pattern ptFind = null;
        Pattern ptStop = null;
        String key = "";
        Map<String, String> expResult = null;
        Map<String, String> result = StringUtils.getRegexFromFile(file, ptFind, ptStop, key);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getRegexFromFile method, of class StringUtils.
     */
    @Test
    @Ignore
    public void testGetRegexFromFile_3args() throws Exception {
        System.out.println("getRegexFromFile");
        File file = null;
        Pattern ptFind = null;
        Pattern ptStop = null;
        List<List<String>> expResult = null;
        List<List<String>> result = StringUtils.getRegexFromFile(file, ptFind, ptStop);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getRegexResultFromMultilineString method, of class StringUtils.
     */
    @Test
    @Ignore
    public void testGetRegexResultFromMultilineString() {
        System.out.println("getRegexResultFromMultilineString");
        String lines = "";
        Pattern ptFind = null;
        Pattern ptStop = null;
        List<List<String>> expResult = null;
        List<List<String>> result = StringUtils.getRegexResultFromMultilineString(lines, ptFind, ptStop);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of flattenListOfLists method, of class StringUtils.
     */
    @Test
    @Ignore
    public void testFlattenListOfLists() {
        System.out.println("flattenListOfLists");
        List expResult = null;
        List result = StringUtils.flattenListOfLists(null);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getRegexGroupResults method, of class StringUtils.
     */
    @Test
    @Ignore
    public void testGetRegexGroupResults() {
        System.out.println("getRegexGroupResults");
        String string = "";
        Pattern ptFind = null;
        List<String> expResult = null;
        List<String> result = StringUtils.getRegexGroupResults(string, ptFind);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getRegexFromReader method, of class StringUtils.
     */
    @Test
    @Ignore
    public void testGetRegexFromReader() throws Exception {
        System.out.println("getRegexFromReader");
        BufferedReader reader = null;
        Pattern ptFind = null;
        Pattern ptStop = null;
        String key = "";
        Map<String, String> expResult = null;
        Map<String, String> result = StringUtils.getRegexFromReader(reader, ptFind, ptStop, key);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getRegexFromStream method, of class StringUtils.
     */
    @Test
    @Ignore
    public void testGetRegexFromStream() {
        System.out.println("getRegexFromStream");
        InputStream is = null;
        Pattern ptFind = null;
        Pattern ptStop = null;
        String key = "";
        Map<String, String> expResult = null;
        Map<String, String> result = StringUtils.getRegexFromStream(is, ptFind, ptStop, key);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of wholeWordContainsViceVersa method, of class StringUtils.
     */
    @Test
    public void testWholeWordContainsViceVersa() {
        System.out.println("wholeWordContainsViceVersa");
        Map<String, String> tests = new LinkedHashMap(); //keep order
        tests.put("Péron & Lesueur, 1810", "Péron & Lesueur, 1810");
        tests.put("Sars, 1835", "M. Sars, 1835");
        for (Map.Entry<String, String> entry : tests.entrySet()) {
            String source = entry.getKey();
            String subItem = entry.getValue();
            boolean result = StringUtils.wholeWordContainsViceVersa(source, subItem);
            assertTrue(result);
        }
    }

    /**
     * Test of mappifyPropertyString method, of class StringUtils.
     */
    @Test
    @Ignore
    public void testMappifyPropertyString() {
        System.out.println("mappifyPropertyString");
        String propString = "";
        String KEYVAL_DELIM = "";
        String PROP_DELIM = "";
        String REGEX_PROP_DELIM = "";
        String REGEX_KEYVAL_DELIM = "";
        Map<String, ArrayList<String>> expResult = null;
        Map<String, ArrayList<String>> result = StringUtils.mappifyPropertyString(propString, KEYVAL_DELIM, PROP_DELIM, REGEX_PROP_DELIM, REGEX_KEYVAL_DELIM);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of isURL method, of class StringUtils.
     */
    @Test
    @Ignore
    public void testIsURL() {
        System.out.println("isURL");
        String url = "";
        boolean expResult = false;
        boolean result = StringUtils.isURL(url);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of stringBuilderReplace method, of class StringUtils.
     */
    @Test
    @Ignore
    public void testStringBuilderReplace() {
        System.out.println("stringBuilderReplace");
        StringBuilder sb = null;
        String from = "";
        String to = "";
        StringBuilder expResult = null;
        StringBuilder result = StringUtils.stringBuilderReplace(sb, from, to);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of stringBuilderReplaceAll method, of class StringUtils.
     */
    @Test
    @Ignore
    public void testStringBuilderReplaceAll() {
        System.out.println("stringBuilderReplaceAll");
        StringBuilder sb = null;
        Pattern fromRegex = null;
        String toRegex = "";
        StringBuilder expResult = null;
        StringBuilder result = StringUtils.stringBuilderReplaceAll(sb, fromRegex, toRegex);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of findNotMatching method, of class StringUtils.
     */
    @Test
    @Ignore
    public void testFindNotMatching() {
        System.out.println("findNotMatching");
        String sourceStr = "";
        String anotherStr = "";
        List<String> expResult = null;
        List<String> result = StringUtils.findNotMatching(sourceStr, anotherStr);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of flattenString method, of class StringUtils.
     */
    @Test
    @Ignore
    public void testFlattenString() {
        System.out.println("flattenString");
        String input = "";
        String expResult = "";
        String result = StringUtils.flattenString(input);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of round method, of class StringUtils.
     */
    @Test
    @Ignore
    public void testRound() {
        System.out.println("round");
        double value = 0.0;
        int places = 0;
        double expResult = 0.0;
        double result = StringUtils.round(value, places);
        assertEquals(expResult, result, 0.0);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of isDouble method, of class StringUtils.
     */
    @Test
    public void testIsDouble() {
        System.out.println("isDouble");
        String str = "2.9";
        boolean expResult = true;
        boolean result = StringUtils.isDouble(str);
        assertEquals(expResult, result);

        str = "5";
        expResult = false;
        result = StringUtils.isDouble(str);
        assertEquals(expResult, result);

        str = "5,9";
        expResult = true;
        result = StringUtils.isDouble(str);
        assertEquals(expResult, result);
    }

    /**
     * Test of isInteger method, of class StringUtils.
     */
    @Test
    public void testIsInteger() {
        System.out.println("isInteger");
        String str = "2.9";
        boolean expResult = false;
        boolean result = StringUtils.isInteger(str);
        assertEquals(expResult, result);

        str = "5";
        expResult = true;
        result = StringUtils.isInteger(str);
        assertEquals(expResult, result);

        str = "5 males, 6 females";
        expResult = false;
        result = StringUtils.isInteger(str);
        assertEquals(expResult, result);
    }

    /**
     * Test of isText method, of class StringUtils.
     */
    @Test
    public void testIsText() {
        System.out.println("isText");
        String str = "2.9";
        boolean expResult = false;
        boolean result = StringUtils.isText(str);
        assertEquals(expResult, result);

        str = "5-8";
        expResult = true;
        result = StringUtils.isText(str);
        assertEquals(expResult, result);

        str = "DC-8";
        expResult = true;
        result = StringUtils.isText(str);
        assertEquals(expResult, result);

        str = "5/8";
        expResult = true;
        result = StringUtils.isText(str);
        assertEquals(expResult, result);

        str = "5\\8";
        expResult = true;
        result = StringUtils.isText(str);
        assertEquals(expResult, result);

        str = "not many shells, Liodon sp. and Anocthes sp.";
        expResult = true;
        result = StringUtils.isText(str);
        assertEquals(expResult, result);
    }

}
