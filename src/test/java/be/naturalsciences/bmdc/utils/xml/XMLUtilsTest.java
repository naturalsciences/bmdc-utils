/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package be.naturalsciences.bmdc.utils.xml;

import be.naturalsciences.bmdc.utils.StringUtils;
import static be.naturalsciences.bmdc.utils.xml.XMLUtils.toDocument;
import static be.naturalsciences.bmdc.utils.xml.XMLUtils.toXML;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.xml.bind.JAXBException;
import javax.xml.parsers.ParserConfigurationException;
import org.apache.commons.io.FileUtils;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.Ignore;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;

/**
 *
 * @author thomas
 */
public class XMLUtilsTest {

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

    public XMLUtilsTest() {
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
     * Test of xsltTransform method, of class XMLUtils.
     */
    @Test
    @Ignore
    public void testXsltTransform_3args() throws Exception {
        System.out.println("xsltTransform");
        File xsltFile = null;
        InputStream inputFile = null;
        File outputFile = null;
        XMLUtils.xsltTransform(xsltFile, inputFile, outputFile, 1, false);
        // TODO review the generated test code and remove the default call to fail.
        // fail("The test case is a prototype.");
    }

    /**
     * Test of xsltTransform method, of class XMLUtils.
     */
    @Test
    @Ignore
    public void testXsltTransform_File_InputStream() throws Exception {
        System.out.println("xsltTransform");
        File xsltFile = null;
        InputStream inputFile = null;
        String expResult = "";
        String result = XMLUtils.xsltTransform(xsltFile, inputFile, 1, false);
        // assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        // fail("The test case is a prototype.");
    }

    /**
     * Test of prettyPrint method, of class XMLUtils.
     */
    @Test
    @Ignore
    public void testPrettyPrint() throws Exception {
        System.out.println("prettyPrint");
        String inputString = "";
        String expResult = "";
        String result = XMLUtils.prettyPrint(inputString);
        //  assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        // fail("The test case is a prototype.");
    }

    /**
     * Test of xpathQueryString method, of class XMLUtils.
     */
    @Test
    public void testXpathQueryString() throws JAXBException, ParserConfigurationException, SAXException, IOException {
        System.out.println("xpathQueryString");
        String xpathQuery = "//gco:CharacterString[text()='info@naturalsciences.be']/text()";
        List<String> result = XMLUtils.xpathQueryString(xmlFile, xpathQuery, MD_NAMESPACES);
        assertTrue(result.size() > 0);
        for (String string : result) {
            assertTrue(string.equals("info@naturalsciences.be"));
        }

        List<String> result2 = XMLUtils.xpathQueryString(XMLUtils.toDocument(xmlFile), xpathQuery, MD_NAMESPACES);
        assertTrue(result2.size() == result.size());
    }

    /**
     * Test of xpathQueryNodes method, of class XMLUtils.
     */
    @Test
    public void testXpathQueryNodes() throws Exception {
        System.out.println("xpathQueryNodes");
        String xpathQuery = "//gco:CharacterString[text()='info@naturalsciences.be']";
        List<Node> result1 = XMLUtils.xpathQueryNodes(xmlFile, xpathQuery, MD_NAMESPACES);
        assertTrue(result1.size() > 0);

        String xpathQuery2 = "//gco:CharacterString[text()='info@naturalsciences.be']/parent::*";
        List<Node> result2 = XMLUtils.xpathQueryNodes(xmlFile, xpathQuery2, MD_NAMESPACES);
        assertTrue(result2.size() == result1.size());

        String xpathQuery3 = "//gmx:Anchor[text()='Belgian part of the North Sea']/parent::*";
        List<Node> result3 = XMLUtils.xpathQueryNodes(xmlFile, xpathQuery3, MD_NAMESPACES);
        for (Node node : result3) {
            node.getLocalName().equals("keyword");
        }
        assertTrue(result3.size() == 1);

        String xpathQuery4 = "//gco:CharacterString/parent::*";
        List<Node> result4 = XMLUtils.xpathQueryNodes(xmlFile, xpathQuery4, MD_NAMESPACES);
        assertTrue(result4.size() > 0);

        List<Node> result5 = XMLUtils.xpathQueryNodes(XMLUtils.toDocument(xmlFile), xpathQuery4, MD_NAMESPACES);
        assertTrue(result5.size() == result4.size());

    }

    /**
     * Test of xpathQueryNode method, of class XMLUtils.
     */
    @Test
    @Ignore
    public void testXpathQueryNode() throws Exception {
        System.out.println("xpathQueryNode");
        File xmlFile = null;
        String xpathQuery = "";
        Map<String, String> namespaces = null;
        Node expResult = null;
        Node result = XMLUtils.xpathQueryNode(xmlFile, xpathQuery, namespaces);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of xpathQueryNodeXML method, of class XMLUtils.
     */
    @Test
    @Ignore
    public void testXpathQueryNodeXML() throws Exception {
        System.out.println("xpathQueryNodeXML");
        File xmlFile = null;
        String xpathQuery = "";
        Map<String, String> namespaces = null;
        String expResult = "";
        String result = XMLUtils.xpathQueryNodeXML(xmlFile, xpathQuery, namespaces);
        //   assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        // fail("The test case is a prototype.");
    }

    /**
     * Test of toDocument method, of class XMLUtils.
     */
    @Test
    public void testToDocumentStringAndBack() throws Exception {
        System.out.println("toDocumentString");
        String xml = "<tag>content</tag>";
        Node extra = toDocument(xml).getDocumentElement();
        String toXML = toXML(extra);
        assertTrue(toXML.contains(xml));
    }

    /**
     * Test of toDocument method, of class XMLUtils.
     */
    @Test
    public void testToDocumentFileAndBack() throws Exception {
        System.out.println("toDocumentFile");
        String readFileToString = XMLUtils.prettyPrint(FileUtils.readFileToString(xmlFile, "UTF8"));
        Node extra = toDocument(xmlFile).getDocumentElement();
        String toXML = XMLUtils.prettyPrint(toXML(extra));
        List<String> findNotMatching = StringUtils.findNotMatching(readFileToString, toXML);
        for (String string : findNotMatching) {
            assertTrue(string.contains("codeSpace") || string.contains("codeList") || string.contains("codeListValue")); // just switches around some attributes.
        }
        assertTrue(findNotMatching.size() == 6);
    }

    /**
     * Test of appendAttribute method, of class XMLUtils.
     */
    @Test
    public void testAppendAttribute() throws Exception {
        System.out.println("appendAttribute");
        Document document = XMLUtils.toDocument(xmlFile);
        String testString = "NEEDSTOHAVETHIS";
        XMLUtils.appendAttribute(document, "//gco:CharacterString[text()='info@naturalsciences.be']", "gco", "test_attribute", testString, MD_NAMESPACES);
        String resultString = XMLUtils.toXML(document);
        assertTrue(resultString.contains("gco:test_attribute=\"NEEDSTOHAVETHIS\""));//<gmd:country xsi:type="gmd:PT_FreeText_PropertyType">

        int size = XMLUtils.xpathQueryNodes(document, "//gco:CharacterString[text()='info@naturalsciences.be']", MD_NAMESPACES).size();

        assertTrue(StringUtils.countMatches(resultString, testString) == size);
    }

    /**
     * Test of pasteAfter method, of class XMLUtils.
     */
    @Test
    public void testPasteAfter() throws Exception {
        System.out.println("pasteAfter");
        Document document = XMLUtils.toDocument(xmlFile);
        String testString = "<test_addition>NEEDSTOHAVETHIS</test_addition>";
        XMLUtils.pasteAfter(document, "//gco:CharacterString", testString, MD_NAMESPACES);
        String resultString = XMLUtils.toXML(document);
        assertTrue(resultString.contains(testString));

        int size = XMLUtils.xpathQueryNodes(document, "//gco:CharacterString", MD_NAMESPACES).size();

        assertTrue(StringUtils.countMatches(resultString, "NEEDSTOHAVETHIS") == size);
    }

    /**
     * Test of pasteAfter method, of class XMLUtils.
     */
    @Test
    public void testReplace() throws Exception {
        System.out.println("replace");
        Document document = XMLUtils.toDocument(xmlFile);
        int originalNbCharacterString = XMLUtils.xpathQueryNodes(document, "//gco:CharacterString", MD_NAMESPACES).size();
        String testString = "<test_replacement>NEEDSTOHAVETHIS</test_replacement>";
        XMLUtils.replace(document, "//gco:CharacterString", testString, MD_NAMESPACES);
        String resultString = XMLUtils.toXML(document);
        assertTrue(resultString.contains(testString));

        int newNbCharacterString = XMLUtils.xpathQueryNodes(document, "//gco:CharacterString", MD_NAMESPACES).size();
        int nbReplacement = XMLUtils.xpathQueryNodes(document, "//test_replacement", MD_NAMESPACES).size();
        assertTrue(newNbCharacterString == 0);
        assertTrue(nbReplacement == originalNbCharacterString);
    }

    @Test
    public void testReplaceBlurbs() {
        System.out.println("replaceXMLBlurbs");
        String testString = "<gmd:PT_FreeText xmlns:gmd=\"http://www.isotc211.org/2005/gmd\">\n"
                + "  <gmd:textGroup>\n"
                + "    <gmd:LocalisedCharacterString locale=\"#NL\">\n"
                + "      Deze dataset maakt deel uit van de Belgische rapportage voor de Mariene Strategiekaderrichtlijn (MSFD) 2018 gekoppeld aan descriptor 8, criterium 1. \n"
                + "      Deze dataset bevat de concentraties verontreinigende stoffen gemeten in sedimenten op 10 meetstations in het Belgische deel van de Noordzee tussen 2011 en 2015. 88 parameters worden gemeten \n"
                + "      in het bovenste deel van de zeebodem door een grijpbunker van Van Veen. De verontreinigingen worden geanalyseerd op de fractie <63 ¿m. PCB's en zware metalen worden geanalyseerd door ILVO (en CODA). \n"
                + "      PBDE's, PAK's en organotinverbindingen worden geanalyseerd door KBIN-OD Nature, PAK's en organotine op basis van een geaccrediteerde methode (ISO / IEC 17025).\n"
                + "    </gmd:LocalisedCharacterString>\n"
                + "  </gmd:textGroup>\n"
                + "  <gmd:textGroup>\n"
                + "    <gmd:LocalisedCharacterString locale=\"#FR\">\n"
                + "      Cet ensemble de données fait partie de la soumission belge de 2018 relative à la directive-cadre sur la stratégie marine (DCSMM) liée au descripteur 8, critère 1. \n"
                + "      Cet ensemble de données contient les concentrations de contaminants mesurées dans les sédiments de 10 stations de surveillance situées dans la partie belge de la mer du Nord 2015. 88 paramètres sont mesurés\n"
                + "      dans la partie supérieure du fond de la mer par un échantillonneur à main Van Veen. Les contaminants sont analysés sur la fraction <63 µm. Les PCB et les métaux lourds sont analysés par ILVO (et CODA). \n"
                + "      Les PBDE, les HAP et les composés organostanniques sont analysés par KBIN-OD Nature, les HAP et les organostanniques selon une méthode accréditée (ISO / IEC 17025).\n"
                + "    </gmd:LocalisedCharacterString>\n"
                + "  </gmd:textGroup>\n"
                + "  <gmd:textGroup>\n"
                + "    <gmd:LocalisedCharacterString locale=\"#EN\">\n"
                + "      This dataset is part of the 2018 Belgian submission for the Marine Strategy Framework Directive (MSFD) linked to descriptor 8, criterion 1. \n"
                + "      This dataset contains the contaminant concentrations measured in sediments at 10 monitoring stations in the Belgian part of the North Sea between 2011 and 2015. 88 parameters are measured \n"
                + "      in the upper part of the sea bottom by a Van Veen grab sampler. The contaminants are analyzed on the fraction <63 µm. PCBs and heavy metals are analyzed by ILVO (and CODA). \n"
                + "      PBDEs, PAHs and organotin compounds are analysed by KBIN-OD Nature, PAHs and organotin based on an accredited method (ISO/IEC 17025).\n"
                + "    </gmd:LocalisedCharacterString>\n"
                + "  </gmd:textGroup>\n"
                + "  <gmd:textGroup>\n"
                + "    <gmd:LocalisedCharacterString locale=\"#DE\">\n"
                + "      Dieser Datensatz ist Teil der belgischen Vorlage 2018 für die Meeresstrategie-Rahmenrichtlinie (MSRL), die an Deskriptor 8, Kriterium 1, gebunden ist. \n"
                + "      Dieser Datensatz enthält die Schadstoffkonzentrationen, die in Sedimenten an 10 Messstationen in der belgischen Nordsee zwischen 2011 und 2011 gemessen wurden 2015. \n"
                + "      88 Parameter werden im oberen Teil des Meeresbodens mit einem Van Veen Grab-Sampler gemessen. Die Verunreinigungen werden an der Fraktion <63 um analysiert. \n"
                + "      PCB und Schwermetalle werden von ILVO (und CODA) analysiert. PBDEs, PAKs und Organozinnverbindungen werden durch KBIN-OD Nature, PAKs und Organozinn nach einer anerkannten Methode (ISO / IEC 17025) analysiert.\n"
                + "    </gmd:LocalisedCharacterString>\n"
                + "  </gmd:textGroup>\n"
                + "</gmd:PT_FreeText>";
        String resultString = XMLUtils.replaceXMLTextNode(testString);
        assertFalse(resultString.contains("<63"));
    }

}
