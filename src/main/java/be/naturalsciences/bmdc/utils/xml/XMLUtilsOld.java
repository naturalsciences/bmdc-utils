///*
// * To change this license header, choose License Headers in Project Properties.
// * To change this template file, choose Tools | Templates
// * and open the template in the editor.
// */
//package be.naturalsciences.bmdc.utils.xml;
//
//import be.naturalsciences.bmdc.utils.StringUtils;
//import java.io.File;
//import java.io.IOException;
//import java.io.InputStream;
//import java.net.URISyntaxException;
//import java.net.URL;
//import java.util.List;
//import javax.xml.transform.Source;
//import javax.xml.transform.Transformer;
//import javax.xml.transform.TransformerException;
//import javax.xml.transform.TransformerFactory;
//import javax.xml.transform.stream.StreamResult;
//import javax.xml.transform.stream.StreamSource;
//import com.jcabi.xml.XML;
//import com.jcabi.xml.XMLDocument;
//import java.io.StringReader;
//import java.io.StringWriter;
//import java.net.MalformedURLException;
//import java.util.Map;
//import java.util.logging.Level;
//import java.util.logging.Logger;
//import javax.xml.parsers.DocumentBuilder;
//import javax.xml.parsers.DocumentBuilderFactory;
//import javax.xml.transform.OutputKeys;
///*import nu.xom.Attribute;
//
//import nu.xom.Element;
//import nu.xom.Nodes;
//import nu.xom.Builder;
//import nu.xom.Document;
//import nu.xom.Node;
//import nu.xom.ParsingException;
//import nu.xom.XPathContext;*/
//
///**
// *
// * @author thomas
// */
//public class XMLUtilsOld {
//
//    /**
//     * *
//     * Transform an input XML file using an input XLST file and save the result
//     * to the given file.
//     *
//     * @param xsltFile
//     * @param inputFile
//     * @param outputFile
//     * @throws IOException
//     * @throws URISyntaxException
//     * @throws TransformerException
//     */
//    public static void xsltTransform(File xsltFile, InputStream inputFile, File outputFile) throws IOException, URISyntaxException, TransformerException {
//        TransformerFactory factory = TransformerFactory.newInstance();
//        Source xslt = new StreamSource(xsltFile);
//        Transformer transformer = factory.newTransformer(xslt);
//
//        Source text = new StreamSource(inputFile);
//        transformer.transform(text, new StreamResult(outputFile.getPath()));
//    }
//
//    /**
//     * *
//     * Transform an input XML file using an input XLST file and return the
//     * result as a String result.
//     *
//     * @param xsltFile
//     * @param inputFile
//     * @return
//     * @throws IOException
//     * @throws URISyntaxException
//     * @throws TransformerException
//     */
//    public static String xsltTransform(File xsltFile, InputStream inputFile) throws IOException, URISyntaxException, TransformerException {
//        TransformerFactory factory = TransformerFactory.newInstance();
//        Source xslt = new StreamSource(xsltFile);
//        Transformer transformer = factory.newTransformer(xslt);
//
//        Source text = new StreamSource(inputFile);
//        StringWriter w = new StringWriter();
//        transformer.transform(text, new StreamResult(w));
//        return w.toString();
//    }
//
//    public static String prettyPrint(String inputString) throws TransformerException {
//        Transformer transformer = TransformerFactory.newInstance().newTransformer();
//        transformer.setOutputProperty(OutputKeys.INDENT, "yes");
//        transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");
//        StreamResult result = new StreamResult(new StringWriter());
//        StringReader reader = new StringReader(inputString);
//        Source text = new StreamSource(reader);
//        transformer.transform(text, result);
//        return result.getWriter().toString();
//    }
//
//    /**
//     * *
//     * Returns null when input is null
//     *
//     * @param urlOrXMLText
//     * @param xpathQuery
//     * @param namespaces
//     * @return
//     * @throws MalformedURLException
//     * @throws IOException
//     */
//    public static List<String> xpathQuery(String urlOrXMLText, String xpathQuery, Map<String, String> namespaces) throws MalformedURLException, IOException {
//        XML xml = null;
//        if (StringUtils.isURL(urlOrXMLText)) {
//            xml = new XMLDocument(new URL(urlOrXMLText));
//        } else if (urlOrXMLText != null) {
//            xml = new XMLDocument(urlOrXMLText);
//        } else {
//            return null;
//        }
//        for (Map.Entry<String, String> entry : namespaces.entrySet()) {
//            String key = entry.getKey();
//            String value = entry.getValue();
//            xml = xml.registerNs(key, value);
//        }
//
//        return xml.xpath(xpathQuery);
//
//    }
//
//    public static List<String> xpathQuery(File xmlFile, String xpathQuery, Map<String, String> namespaces) throws MalformedURLException, IOException {
//        XML xml = null;
//        xml = new XMLDocument(xmlFile);
//        for (Map.Entry<String, String> entry : namespaces.entrySet()) {
//            String key = entry.getKey();
//            String value = entry.getValue();
//            xml = xml.registerNs(key, value);
//        }
//        return xml.xpath(xpathQuery);
//    }
//
//    private static XPathContext getNamespaceContext(Element root, Map<String, String> namespaces) {
//        XPathContext namespaceContext = XPathContext.makeNamespaceContext(root);
//
//        for (Map.Entry<String, String> entry : namespaces.entrySet()) {
//            String key = entry.getKey();
//            String value = entry.getValue();
//            namespaceContext.addNamespace(key, value);
//        }
//        return namespaceContext;
//    }
//
//    /**
//     * *
//     * Launch an xpath query on the given file and return a completely valid XML
//     * String (i.e. with <?xml version=\"1.0\" encoding=\"UTF-8\"?>)
//     *
//     * @param xmlFile
//     * @param xpathQuery
//     * @param namespaces
//     * @return
//     */
//    public static String xpathQueryReturningXML(File xmlFile, String xpathQuery, Map<String, String> namespaces) throws ParsingException, IOException {
//        Builder builder = new Builder();
//        Document doc = builder.build(xmlFile);
//
//        Element root = doc.getRootElement();
//
//        XPathContext namespaceContext = getNamespaceContext(root, namespaces);
//        Nodes result = root.query(xpathQuery, namespaceContext);
//        root.removeChildren();
//        root.appendChild(result.get(0));
//        return "<?xml version=\"1.0\" encoding=\"UTF-8\"?>" + root.toXML();
//    }
//
//    private static Element getElement(String xml) throws IOException, ParsingException {
//        Builder builder = new Builder();
//
//        Document doc = builder.build(new StringReader(xml));
//        Element root = doc.getRootElement();
//        return root;
//    }
//
//    public static void appendAttribute(StringBuilder xmlSb, String elementXPath, String attributeNamespace, String attributeName, String attributeValue, Map<String, String> namespaces) {
//        try {
//            Builder builder = new Builder();
//            Document doc = builder.build(new StringReader(xmlSb.toString()));
//            Element root = doc.getRootElement();
//
//            XPathContext namespaceContext = getNamespaceContext(root, namespaces);
//            Nodes result = root.query(elementXPath, namespaceContext);
//            for (int i = 0; i < result.size(); i++) {
//                Attribute attr = new Attribute(attributeName, attributeNamespace, attributeValue);
//                ((Element) result.get(i)).addAttribute(attr);
//            }
//            xmlSb = new StringBuilder(root.toXML());
//            int a = 5;
//        } catch (ParsingException ex) {
//            Logger.getLogger(XMLUtilsOld.class.getName()).log(Level.SEVERE, null, ex);
//        } catch (IOException ex) {
//            Logger.getLogger(XMLUtilsOld.class.getName()).log(Level.SEVERE, null, ex);
//        }
//    }
//
//    /**
//     * *
//     * Paste an XML fragment as a sibling of the element identified by the XPath
//     * String elementXPath of the StringBuilder xmlSb using a namespaces Map
//     *
//     * @param xmlSb
//     * @param elementXPath
//     * @param xmlExtra
//     * @param namespaces
//     */
//    public static void pasteAfter(StringBuilder xmlSb, String elementXPath, String xmlExtra, Map<String, String> namespaces) {
//        Nodes result = null;
//        Node extra = null;
//        int i = 0;
//        try {
//            Element root = getElement(xmlSb.toString());
//            XPathContext namespaceContext = getNamespaceContext(root, namespaces);
//            result = root.query(elementXPath, namespaceContext);
//            for (i = 0; i < result.size(); i++) {
//                extra = getElement(xmlExtra.toString()).copy();
//                result.get(i).getParent().appendChild(extra);
//            }
//            xmlSb = new StringBuilder(root.toXML());
//            int a = 5;
//        } catch (IOException ex) {
//            Logger.getLogger(XMLUtilsOld.class.getName()).log(Level.SEVERE, null, ex);
//        } catch (ParsingException ex) {
//            Logger.getLogger(XMLUtilsOld.class.getName()).log(Level.SEVERE, null, ex);
//        } catch (Exception ex) {
//            Logger.getLogger(XMLUtilsOld.class.getName()).log(Level.SEVERE, null, ex);
//        }
//    }
//}
