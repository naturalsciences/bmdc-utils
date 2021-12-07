/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package be.naturalsciences.bmdc.utils.xml;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.xml.bind.JAXBException;
import javax.xml.parsers.ParserConfigurationException;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;

/**
 *
 * @author thomas
 */
public class XMLElementTest {

    ClassLoader classLoader;
    File xmlFile;

    public static final Map<String, String> MD_NAMESPACES = new HashMap<>();

    static {
        MD_NAMESPACES.put("gmd", "http://www.isotc211.org/2005/gmd");
        MD_NAMESPACES.put("gco", "http://www.isotc211.org/2005/gco");
        MD_NAMESPACES.put("xlink", "http://www.w3.org/1999/xlink");
        MD_NAMESPACES.put("xsi", "http://www.w3.org/2001/XMLSchema-instance");
        MD_NAMESPACES.put("gml", "http://www.opengis.net/gml/3.2");
        MD_NAMESPACES.put("geonet", "http://www.fao.org/geonetwork");
        MD_NAMESPACES.put("gmx", "http://www.isotc211.org/2005/gmx");
    }

    public XMLElementTest() {
        classLoader = getClass().getClassLoader();
        xmlFile = new File(classLoader.getResource("file1.xml").getFile());
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
     * Test of toXPath method, of class XMLElement.
     */
    @Test
    public void testToXPath() throws JAXBException, ParserConfigurationException, SAXException, IOException {
        System.out.println("toXPath");
        XMLElement element = new XMLElement("gmd:LocalisedCharacterString", "Aucune condition ne s'applique à l'accès et l'utilisation.", null, null); //test whether apostrophes are correctly escaped.
        String xpathQuery = element.toXPath();
        List<Node> result1 = XMLUtils.xpathQueryNodes(xmlFile, xpathQuery, MD_NAMESPACES);
        assertTrue(result1.size() == 1);
    }

}
