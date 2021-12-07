/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package be.naturalsciences.bmdc.utils.xml;

import be.naturalsciences.bmdc.utils.FileUtils;
import be.naturalsciences.bmdc.utils.StringUtils;
import com.jcabi.aspects.Cacheable;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.List;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import com.jcabi.xml.XML;
import com.jcabi.xml.XMLDocument;
import com.jcabi.xml.XPathContext;
import java.io.ByteArrayInputStream;
import java.io.StringReader;
import java.io.StringWriter;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.XMLConstants;
import javax.xml.bind.JAXBException;
import javax.xml.namespace.NamespaceContext;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;
import org.apache.commons.io.IOUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

/**
 *
 * @author thomas
 *
 * Conclusions: JCABI seems to mix with w3c.dom as it is a wrapper for the dom
 * objects. However in practice this seems not to work; Documents created by
 * classical methods can't find any element with JCABI xpath methods. XOM
 * totally doesn't mix as it is not a wrapper around dom.
 */
public class XMLUtils {

    /**
     * *
     * Transform an input XML file using an input XLST file and save the result
     * to the given file. Return true if the file is created, false if not.
     *
     * @param xsltFile
     * @param inputStream
     * @param outputFile
     * @param xsltVersion
     * @param rejectIfEmpty If the generated file would be empty or contain only
     * the xml header <?xml version=\"1.0\" encoding=\"UTF-8\"?> then do not
     * save the file.
     * @return Return true if the file is created, false if not.
     * @throws IOException
     * @throws URISyntaxException
     * @throws TransformerException
     */
    public static boolean xsltTransform(File xsltFile, InputStream inputStream, File outputFile, int xsltVersion, boolean rejectIfEmpty) throws URISyntaxException, TransformerException {
        if (xsltVersion == 2) {
            System.setProperty("javax.xml.transform.TransformerFactory", "net.sf.saxon.TransformerFactoryImpl");
        }
        TransformerFactory factory = TransformerFactory.newInstance();
        Source xslt = new StreamSource(xsltFile);

        /*Transformer transformer = factory.newTransformer(xslt);
        Source text = new StreamSource(inputStream);
        StreamResult sr = new StreamResult(outputFile.getPath());
        transformer.transform(text, sr);*/
        String result = xsltTransform(xslt, inputStream, xsltVersion, rejectIfEmpty, false);

        if (result != null) {
            try {
                FileUtils.saveToFile(result, "UTF-8", outputFile);
                return true;
            } catch (IOException ex) {
                Logger.getLogger(XMLUtils.class.getName()).log(Level.SEVERE, null, ex);
                return false;
            }

        } else {
            return false;
        }
    }

    /**
     * *
     * Transform an input XML InputStream using an input XLST file and return
     * the result as a String result.
     *
     * @param xsltFile
     * @param inputStream
     * @param xsltVersion
     * @return
     * @throws IOException
     * @throws URISyntaxException
     * @throws TransformerException
     */
    public static String xsltTransform(File xsltFile, InputStream inputStream, int xsltVersion, boolean rejectIfEmpty) throws IOException, URISyntaxException {
        Source xslt = new StreamSource(xsltFile);
        return xsltTransform(xslt, inputStream, xsltVersion, rejectIfEmpty, false);
    }

    public static String xsltTransform(InputStream xsltFile, InputStream inputStream, int xsltVersion, boolean rejectIfEmpty) throws IOException, URISyntaxException {
        Source xslt = new StreamSource(xsltFile);
        return xsltTransform(xslt, inputStream, xsltVersion, rejectIfEmpty, false);
    }

    private static String xsltTransform(Source xslt, InputStream inputStream, int xsltVersion, boolean rejectIfEmpty, boolean printInput) throws URISyntaxException {
        try {
            if (xsltVersion == 2) {
                System.setProperty("javax.xml.transform.TransformerFactory", "net.sf.saxon.TransformerFactoryImpl");
            }
            String inputResult = null;
            try {
                inputResult = IOUtils.toString(inputStream, "UTF8");
                if (printInput) {
                    System.out.println("XML: " + inputResult);
                }
            } catch (IOException ex) {
                Logger.getLogger(XMLUtils.class.getName()).log(Level.SEVERE, null, ex);
            }

            inputStream = new ByteArrayInputStream(inputResult.getBytes());
            TransformerFactory factory = TransformerFactory.newInstance();
            Transformer transformer = factory.newTransformer(xslt);
            Source text = new StreamSource(inputStream, "");
            StringWriter w = new StringWriter();
            transformer.transform(text, new StreamResult(w));
            String result = w.toString();
            System.clearProperty("javax.xml.transform.TransformerFactory");
            if (rejectIfEmpty && (result.equals("") || result.equals("<?xml version=\"1.0\" encoding=\"UTF-8\"?>"))) {
                return null;
            } else {
                return w.toString();
            }

        } catch (TransformerException ex) {
            Logger.getLogger(XMLUtils.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    /**
     * *
     * Transform an input XML String using an input XLST file and return the
     * result as a String result.
     *
     * @param xsltFile
     * @param inputString
     * @param xsltVersion
     * @return
     * @throws IOException
     * @throws URISyntaxException
     * @throws TransformerException
     */
    public static String xsltTransform(File xsltFile, String inputString, int xsltVersion, boolean rejectIfEmpty) throws IOException, URISyntaxException, TransformerException {
        Source xslt = new StreamSource(xsltFile);
        return xsltTransform(xslt, inputString, xsltVersion, rejectIfEmpty);
    }

    public static String xsltTransform(InputStream xsltFile, String inputString, int xsltVersion, boolean rejectIfEmpty) throws IOException, URISyntaxException, TransformerException {
        Source xslt = new StreamSource(xsltFile);
        return xsltTransform(xslt, inputString, xsltVersion, rejectIfEmpty);
    }

    private static String xsltTransform(Source xslt, String inputString, int xsltVersion, boolean rejectIfEmpty) throws IOException, URISyntaxException, TransformerException {
        return xsltTransform(xslt, IOUtils.toInputStream(inputString, "UTF-8"), xsltVersion, rejectIfEmpty, false);
    }

    public static Source stringToSource(String inputString) {
        StringReader reader = new StringReader(inputString);
        return new StreamSource(reader);
    }

    public static String prettyPrint(String inputString) throws TransformerException {
        Transformer transformer = TransformerFactory.newInstance().newTransformer();
        transformer.setOutputProperty(OutputKeys.INDENT, "yes");
        transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");
        StreamResult result = new StreamResult(new StringWriter());
        Source text = stringToSource(inputString);
        transformer.transform(text, result);
        return result.getWriter().toString();
    }

    /**
     * *
     * Replace chars in a string that can't be used inside an XML text node, but
     * are ok eg. for an attribute
     *
     * @param xml
     * @return
     */
    public static String replaceXMLTextNode(String xml) {
        //xml = StringEscapeUtils.escapeXml10(xml);
        if (xml != null) {
            xml = xml.replace(">", "&gt;");
            xml = xml.replace("<", "&lt;");
        }
        return xml;
    }

    /**
     * *
     * Returns null when input is null
     *
     * @param urlOrXMLText
     * @param xpathQuery
     * @param namespaces
     * @return
     * @throws MalformedURLException
     * @throws IOException
     */
    public static List<String> xpathQueryString(String urlOrXMLText, String xpathQuery, Map<String, String> namespaces) throws MalformedURLException, IOException {
        XML xml = null;
        if (StringUtils.isURL(urlOrXMLText)) {
            xml = new XMLDocument(new URL(urlOrXMLText));
        } else if (urlOrXMLText != null) {
            xml = new XMLDocument(urlOrXMLText);
        } else {
            return null;
        }
        return xpathQueryString(xml, xpathQuery, namespaces);
    }

    public static List<String> xpathQueryString(File xmlFile, String xpathQuery, Map<String, String> namespaces) throws JAXBException, ParserConfigurationException, SAXException, IOException {
        return xpathQueryString(new XMLDocument(xmlFile), xpathQuery, namespaces);
    }

    public static List<String> xpathQueryString(Document xmlDoc, String xpathQuery, Map<String, String> namespaces) {
        return xpathQueryString(new XMLDocument(xmlDoc), xpathQuery, namespaces);
    }

    private static List<String> xpathQueryString(XML xml, String xpathQuery, Map<String, String> namespaces) {
        xml = registerNs(xml, namespaces);
        return xml.xpath(xpathQuery);
    }

    public static List<Node> xpathQueryNodes(File xmlFile, String xpathQuery, Map<String, String> namespaces) throws JAXBException, ParserConfigurationException, SAXException, IOException {
        return xpathQueryNodes(new XMLDocument(xmlFile), xpathQuery, namespaces);
    }

    public static List<Node> xpathQueryNodes(Document xmlDoc, String xpathQuery, Map<String, String> namespaces) throws XPathExpressionException {
        xpathQuery = xpathQuery.replace("'", "\'");
        XPath xPath = XPathFactory.newInstance().newXPath();

        NamespaceContext context = new NamespaceContextMap(namespaces);

        xPath.setNamespaceContext(context);
        NodeList nodes = (NodeList) xPath.evaluate(xpathQuery, xmlDoc, XPathConstants.NODESET);
        List<Node> result = new ArrayList<>();
        for (int i = 0; i < nodes.getLength(); ++i) {
            result.add(nodes.item(i));
        }
        return result;
        //  return xpathQueryNodes(new XMLDocument(xmlDoc), xpathQuery, namespaces);
    }

    private static List<Node> xpathQueryNodes(XML xml, String xpathQuery, Map<String, String> namespaces) {
        xml = registerNs(xml, namespaces);
        List<Node> result = new ArrayList<>();
        for (XML node : xml.nodes(xpathQuery)) {
            result.add(node.node());
        }
        return result;
    }

    public static Node xpathQueryNode(File xmlFile, String xpathQuery, Map<String, String> namespaces) throws JAXBException, ParserConfigurationException, SAXException, IOException {
        return xpathQueryNode(new XMLDocument(xmlFile), xpathQuery, namespaces);
    }

    public static Node xpathQueryNode(Document xmlDoc, String xpathQuery, Map<String, String> namespaces) {
        return xpathQueryNode(new XMLDocument(xmlDoc), xpathQuery, namespaces);
    }

    private static Node xpathQueryNode(XML xml, String xpathQuery, Map<String, String> namespaces) {
        xml = registerNs(xml, namespaces);
        List<XML> nodes = xml.nodes(xpathQuery);
        return nodes.get(0) != null ? nodes.get(0).node() : null;
        // return nodes.size() == 1 ? nodes.get(0).node() : null; //TODO: IMPROVE!
    }

    /**
     * *
     * Launch an xpath query on the given file and return a completely valid XML
     * String (i.e. with <?xml xsltVersion=\"1.0\" encoding=\"UTF-8\"?>)
     *
     * @param xmlFile
     * @param xpathQuery
     * @param namespaces
     * @return
     */
    public static String xpathQueryNodeXML(File xmlFile, String xpathQuery, Map<String, String> namespaces) throws IOException, JAXBException, ParserConfigurationException, SAXException, XPathExpressionException {
        Node node = xpathQueryNode(xmlFile, xpathQuery, namespaces);
        return node != null ? new XMLDocument(node).toString() : null;
    }

    /**
     * *
     * Launch an xpath query on the given file and return a completely valid XML
     * String (i.e. with <?xml xsltVersion=\"1.0\" encoding=\"UTF-8\"?>)
     *
     * @param xmlFile
     * @param xpathQuery
     * @param namespaces
     * @return
     */
    public static String xpathQueryNodeXML(Document xmlDocument, String xpathQuery, Map<String, String> namespaces) throws IOException, JAXBException, ParserConfigurationException, SAXException, XPathExpressionException {
        Node node = xpathQueryNode(xmlDocument, xpathQuery, namespaces);
        return node != null ? new XMLDocument(node).toString() : null;
    }

    private static XML registerNs(XML xml, Map<String, String> namespaces) {
        XPathContext namespaceContext = new XPathContext();

        for (Map.Entry<String, String> entry : namespaces.entrySet()) {
            String key = entry.getKey();
            String value = entry.getValue();
            String registeredKey = namespaceContext.getNamespaceURI(key);
            if (registeredKey == null || registeredKey.equals("")) {
                namespaceContext = namespaceContext.add(key, value);
            }
        }
        xml = xml.merge(namespaceContext);
        return xml;
    }

    @Cacheable(lifetime = 1, unit = TimeUnit.HOURS)
    public static Document toDocument(String xml) throws JAXBException, ParserConfigurationException, SAXException, IOException {
        XMLDocument xmlDocument = new XMLDocument(xml);
        return (Document) xmlDocument.node();
        //return DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(new InputSource(new StringReader(xml)));
    }

    @Cacheable(lifetime = 1, unit = TimeUnit.HOURS)
    public static Document toDocument(File xmlFile) throws JAXBException, ParserConfigurationException, SAXException, IOException {
        //    return DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(xmlFile);
        XMLDocument xmlDocument = new XMLDocument(xmlFile);
        return (Document) xmlDocument.node();
        //return toDocument(FileUtils.readFileToString(xmlFile, "UTF8"));
    }

    public static String toXML(Node node) {
        return new XMLDocument(node).toString();
    }

    /**
     * *
     * Add an attribute key and value to an xml element.
     *
     * @param document The Document that needs to be edited
     * @param elementXPath The xpath that identifies the element that needs to
     * receive the attribute
     * @param attributeNamespace The namespace abbreviation.
     * @param attributeName The name of the attribute, without any namespace
     * abbreviation in front of it
     * @param attributeValue The value of the attribute
     * @param namespaces A map of namespaces containing abbreviation key and uri
     * value pairs
     * @throws XPathExpressionException
     */
    public static void appendAttribute(Document document, String elementXPath, String attributeNamespace, String attributeName, String attributeValue, Map<String, String> namespaces) throws XPathExpressionException {
        List<Node> nodes = xpathQueryNodes(document, elementXPath, namespaces);
        appendAttribute(document, nodes, attributeNamespace, attributeName, attributeValue, namespaces);
    }

    public static void appendAttribute(Document document, List<Node> result, String attributeNamespace, String attributeName, String attributeValue, Map<String, String> namespaces) throws XPathExpressionException {
        for (Node node : result) {
            ((Element) node).setAttributeNS(namespaces.get(attributeNamespace), attributeNamespace + ":" + attributeName, attributeValue);
        }
    }

    /**
     * *
     * Paste an XML fragment as a sibling of the element(s) identified by the
     * XPath String elementXPath of the Document document using a namespaces
     * Map. The xmlExtra must contain the explicit namespace declaration!
     *
     * @param xmlSb
     * @param elementXPath
     * @param xmlExtra
     * @param namespaces
     */
    public static void pasteAfter(Document document, String elementXPath, String xmlExtra, Map<String, String> namespaces) throws XPathExpressionException, JAXBException, ParserConfigurationException, IOException, SAXException {
        List<Node> nodes = xpathQueryNodes(document, elementXPath, namespaces);
        pasteAfter(document, nodes, xmlExtra, namespaces);
    }

    public static void pasteAfter(Document document, List<Node> nodes, String xmlExtra, Map<String, String> namespaces) throws XPathExpressionException, JAXBException, ParserConfigurationException, IOException, SAXException {
        if (document == null) {
            throw new IllegalArgumentException("The provided Document is null.");
        }
        if (nodes == null) {
            throw new IllegalArgumentException("The provided nodes list is null.");
        }
        if (xmlExtra == null) {
            throw new IllegalArgumentException("The provided xml addition String is null.");
        }
        if (nodes.size() > 0) {
            xmlExtra = xmlExtra.replace("&", "&amp;");
            Node extra = toDocument(xmlExtra).getDocumentElement();

            for (Node node : nodes) {
                extra = document.importNode(extra, true);
                Node parent = node.getParentNode();
                parent.insertBefore(extra, node.getNextSibling());
            }
        }
    }

    public static void replace(Document document, String elementXPath, String xmlReplacement, Map<String, String> namespaces) throws XPathExpressionException, JAXBException, ParserConfigurationException, IOException, SAXException {
        List<Node> nodes = xpathQueryNodes(document, elementXPath, namespaces);
        replace(document, nodes, xmlReplacement, namespaces);
    }

    public static void replace(Document document, List<Node> nodes, String xmlReplacement, Map<String, String> namespaces) throws XPathExpressionException, JAXBException, ParserConfigurationException, IOException, SAXException {
        if (document == null) {
            throw new IllegalArgumentException("The provided Document is null.");
        }
        if (xmlReplacement == null) {
            throw new IllegalArgumentException("The provided xml addition String is null.");
        }
        if (nodes.size() > 0) {
            xmlReplacement = xmlReplacement.replace("&", "&amp;");
            Node replacement = toDocument(xmlReplacement).getDocumentElement();

            for (Node node : nodes) {
                replacement = document.importNode(replacement, true);
                Node parent = node.getParentNode();
                // parent.appendChild(replacement);//insertBefore(replacement, node.getNextSibling());
                // parent.removeChild(node);
                parent.replaceChild(replacement, node);
            }
        }
    }

    public interface IValidationResult {

        public boolean isValid();

        public String getMessage();
    }

    public static class ValidationResult implements IValidationResult {

        private boolean valid;
        private String message;

        public ValidationResult(boolean valid, String message) {
            this.valid = valid;
            this.message = message;
        }

        @Override
        public boolean isValid() {
            return valid;
        }

        @Override
        public String getMessage() {
            return message;
        }
    }

    public static IValidationResult validateXMLAgainstXSD(URL schemaUrl, String xml) {
        Source xmlSourceFile = stringToSource(xml);
        SchemaFactory schemaFactory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
        try {
            Schema schema = schemaFactory.newSchema(schemaUrl);
            Validator validator = schema.newValidator();
            validator.validate(xmlSourceFile);
            return new ValidationResult(true, null);
        } catch (SAXException e) {
            return new ValidationResult(false, e.getMessage());
        } catch (IOException e) {
            //cannot happen
        }
        return null;
    }

    public static IValidationResult validateXMLAgainstXSD(URL schemaUrl, File xmlFile) throws IOException {
        Source xmlSourceFile = new StreamSource(xmlFile);
        SchemaFactory schemaFactory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
        try {
            Schema schema = schemaFactory.newSchema(schemaUrl);
            Validator validator = schema.newValidator();
            validator.validate(xmlSourceFile);
            return new ValidationResult(true, null);
        } catch (SAXException e) {
            return new ValidationResult(false, e.getMessage());
        }
    }
}
