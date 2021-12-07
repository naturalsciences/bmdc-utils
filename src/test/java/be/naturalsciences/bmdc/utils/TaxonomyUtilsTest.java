/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package be.naturalsciences.bmdc.utils;

import java.util.LinkedHashMap;
import java.util.Map;
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
public class TaxonomyUtilsTest {

    public TaxonomyUtilsTest() {
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
     * Test of splitSpecies method, of class TaxonomyUtils.
     */
    @Test
    @Ignore
    public void testSplitSpecies() {
        System.out.println("splitSpecies");
        String speciesName = "";
        String[] expResult = null;
        String[] result = TaxonomyUtils.splitSpecies(speciesName);
        assertArrayEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of nameIsBinomial method, of class TaxonomyUtils.
     */
    @Test
    @Ignore
    public void testNameIsBinomial() {
        System.out.println("nameIsBinomial");
        Map<String, Boolean> tests = new LinkedHashMap(); //keep order
        tests.put("Cucumaria koellikeri Semper, 1868", true);
        tests.put("Laophontina posidoniae", true);
        tests.put("Halecium robustum robustum", false);
        for (Map.Entry<String, Boolean> entry : tests.entrySet()) {
            String speciesName = entry.getKey();
            Boolean expResult = entry.getValue();
            boolean result = TaxonomyUtils.nameIsBinomial(speciesName);
            assertEquals(expResult, result);
        }
    }

    /**
     * Test of nameIsTrinomial method, of class TaxonomyUtils.
     */
    @Test
    @Ignore
    public void testNameIsTrinomial() {
        System.out.println("nameIsTrinomial");
        Map<String, Boolean> tests = new LinkedHashMap(); //keep order
        tests.put("Cucumaria koellikeri Semper, 1868", false);
        tests.put("Cucumaria koellikeri Semper", false);
        tests.put("Laophontina posidoniae", false);
        tests.put("Halecium robustum robustum", true);
        tests.put("Halecium robustum robustum Linnaeus, 1758", true);
        tests.put("Halecium robustum robustum Linnaeus", true);
        for (Map.Entry<String, Boolean> entry : tests.entrySet()) {
            String speciesName = entry.getKey();
            Boolean expResult = entry.getValue();
            boolean result = TaxonomyUtils.nameIsTrinomial(speciesName);
            assertEquals(expResult, result);
        }
    }

    /**
     * Test of getSpeciesFromSubspecies method, of class TaxonomyUtils.
     */
    @Test
    @Ignore
    public void testGetSpeciesFromSubspecies() {
        System.out.println("getSpeciesFromSubspecies");
        String subspeciesName = "";
        String expResult = "";
        String result = TaxonomyUtils.getSpeciesFromSubspecies(subspeciesName);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getGenusFromSpecies method, of class TaxonomyUtils.
     */
    @Test
    @Ignore
    public void testGetGenusFromSpecies() {
        System.out.println("getGenusFromSpecies");
        String subspeciesName = "";
        String expResult = "";
        String result = TaxonomyUtils.getGenusFromSpecies(subspeciesName);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of isHybrid method, of class TaxonomyUtils.
     */
    @Test
    public void testIsHybrid() {
        System.out.println("isHybrid");
        Map<String, Boolean> tests = new LinkedHashMap(); //keep order
        tests.put("Acipenser gueldenstadtii x ruthenus", true);
        tests.put("Acipenser gueldenstadtii X ruthenus", true);
        tests.put("Acipenser gueldenstadtii × ruthenus", true);
        tests.put("Acipenser gueldenstadtii×ruthenus", true);
        tests.put("Xiphius sp.", false);
        tests.put("Adalia inexpectata", false);
        for (Map.Entry<String, Boolean> entry : tests.entrySet()) {
            String speciesName = entry.getKey();
            Boolean expResult = entry.getValue();
            boolean result = TaxonomyUtils.isHybrid(speciesName);
            assertEquals(expResult, result);
        }
    }

    /**
     * Test of getAuthorAndDate method, of class TaxonomyUtils.
     */
    @Test
    public void testGetAuthorAndDate() {
        System.out.println("getAuthorAndDate");
        Map<String, String> tests = new LinkedHashMap(); //keep order
        tests.put("Acipenser gueldenstadtii x ruthenus", null);
        tests.put("Laetmogone wyvillethompsoni Théel, 1882", "Théel, 1882");
        tests.put("Zoobotryon pellucidum Ehrenberg, 1829", "Ehrenberg, 1829");
        tests.put("Adalia inexpectata", null);
        tests.put("Eschara cobata Lamoureux ?", "Lamoureux ?");
        tests.put("Thompsonula hyaena (Thompson, 1889)", "(Thompson, 1889)");
        tests.put("Koellikerina fasciulata (Péron & Lesueur, 1810)", "(Péron & Lesueur, 1810)");
        tests.put("Koellikerina fasciulata (Péron & Lesueur, )", "(Péron & Lesueur, )");
        tests.put("Koellikerina fasciulata (Péron & Lesueur, 1810", "(Péron & Lesueur, 1810)");
        tests.put("Koellikerina fasciulata Péron & Lesueur, 1810)", "(Péron & Lesueur, 1810)");
        for (Map.Entry<String, String> entry : tests.entrySet()) {
            String speciesName = entry.getKey();
            String expResult = entry.getValue();
            String result = TaxonomyUtils.getAuthorAndDate(speciesName);
            assertEquals(expResult, result);
            System.out.println("--SUCCESS--");
            System.out.println(speciesName + ": " + result);
        }
    }

}
