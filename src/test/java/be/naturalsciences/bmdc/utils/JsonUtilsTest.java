/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package be.naturalsciences.bmdc.utils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import org.apache.commons.collections4.map.SingletonMap;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author thomas
 */
public class JsonUtilsTest {

    public JsonUtilsTest() {
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
    private static String url1 = "http://ontologies.ef-ears.eu/ears2/1#dev_15";
    private static String prefLabel1 = "BIOMAPER-II+-+Wiebe+et+al+(1999,+2002)";
    private static String JSON1 = "{\"http://ontologies.ef-ears.eu/ears2/1#dev_15\":\"BIOMAPER-II+-+Wiebe+et+al+(1999,+2002)\"}";

    private static String url2 = "http://ontologies.ef-ears.eu/ears2/1/11BE#dev_1929";
    private static String prefLabel2 = "Rosette";
    private static String JSON2 = "{\"http://ontologies.ef-ears.eu/ears2/1/11BE#dev_1929\":\"Rosette\"}";

    private static String url3 = "http://ontologies.ef-ears.eu/ears2/1#dev_85";
    private static String prefLabel3 = "Niskin+bottle";
    private static String JSON3 = "{\"http://ontologies.ef-ears.eu/ears2/1#dev_85\":\"Niskin+bottle\"}";

    private static String JSONCOMBO = "[{\"http://ontologies.ef-ears.eu/ears2/1/11BE#dev_1929\":\"Rosette\"},{\"http://ontologies.ef-ears.eu/ears2/1#dev_85\":\"Niskin+bottle\"}]";

    /**
     * Test of serializeConcepts method, of class JsonUtils.
     */
    @Test
    public void testSerializeConcepts() {
        System.out.println("serializeConcepts");
        LinkedHashMap<String, String> concepts = new LinkedHashMap<>();
        concepts.put(url1, prefLabel1);
        String expResult = JSON1;
        String result = JsonUtils.serializeConcepts(concepts);
        assertEquals(expResult, result);
    }

    /**
     * Test of serializeConcepts method, of class JsonUtils.
     */
    @Test
    public void testSerializeConcepts2() {
        System.out.println("serializeConcepts");
        LinkedHashMap<String, String> concepts = new LinkedHashMap<>();
        concepts.put(url2, prefLabel2);
        concepts.put(url3, prefLabel3);
        String expResult = JSONCOMBO;
        String result = JsonUtils.serializeConcepts(concepts);
        assertEquals(expResult, result);
    }

    /**
     * Test of serializeConcept method, of class JsonUtils.
     */
    @Test
    public void testSerializeConcept_String_String() {
        System.out.println("serializeConcept");
        String uri = url1;
        String name = prefLabel1;
        String expResult = JSON1;
        String result = JsonUtils.serializeConcept(uri, name);
        assertEquals(expResult, result);
    }

    /**
     * Test of serializeConcept method, of class JsonUtils.
     */
    @Test
    public void testSerializeConcept_SingletonMap() {
        System.out.println("serializeConcept");
        SingletonMap<String, String> concept = new SingletonMap<>(url1, prefLabel1);
        String expResult = JSON1;
        String result = JsonUtils.serializeConcept(concept);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        //fail("The test case is a prototype.");
    }

    /**
     * Test of deserializeConcepts method, of class JsonUtils.
     */
    @Test
    public void testDeserializeConcepts() {
        System.out.println("deserializeConcepts");
        Collection<String> jsonConcepts = new ArrayList();
        jsonConcepts.add(JSON1);
        jsonConcepts.add(JSONCOMBO);
        Map<String, String> expResult = new HashMap();
        expResult.put(url2, prefLabel2);
        expResult.put(url3, prefLabel3);
        expResult.put(url1, prefLabel1);
        Map<String, String> result = JsonUtils.deserializeConcepts(jsonConcepts);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        //  fail("The test case is a prototype.");
    }

    /**
     * Test of deserializeConcept method, of class JsonUtils.
     */
    @Test
    public void testDeserializeConcept() {
        System.out.println("deserializeConcept");
        String jsonConcept = "";
        SingletonMap<String, String> expResult = null;
//        SingletonMap<String, String> result = JsonUtils.deserializeConcept(jsonConcept);
        //      assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        // fail("The test case is a prototype.");
    }

}
