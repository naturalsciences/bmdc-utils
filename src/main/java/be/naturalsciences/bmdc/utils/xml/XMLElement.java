/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package be.naturalsciences.bmdc.utils.xml;

/**
 * A class to model an XML leaf tag in a straightforward way. Can convert the
 * element into an XPath expression to select those elements in an xml documents
 * that match this. Leaf tags are those elements that contain only text.
 *
 * @author thomas
 */
public class XMLElement {

    private String tag;
    private String attributeName;
    private String attributeValue;
    private String content;

    public XMLElement(String element, String content, String attributeName, String attributeValue) {
        if ((attributeName != null && attributeValue == null) || (attributeName == null && attributeValue != null)) {
            throw new IllegalArgumentException("Cannot create an element with an incomplete attribute: both attribute name and value must be provided.");
        }
        this.tag = element;
        // if (content != null && content.length() > 200) {
        //    this.content = content.substring(0, 200);
        //} else {
        this.content = content;
        //}
        this.attributeName = attributeName;
        this.attributeValue = attributeValue;
    }

    /**
     * *
     * Return the tag of the xml element, including its xmlns namespace
     *
     * @return
     */
    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    /**
     * *
     * Return the text content of the xml element
     *
     * @return
     */
    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    /**
     * *
     * Return the attribute name of the xml element
     *
     * @return
     */
    public String getAttributeName() {
        return attributeName;
    }

    public void setAttributeName(String attributeName) {
        this.attributeName = attributeName;
    }

    /**
     * *
     * Return the attribute value of the xml element
     *
     * @return
     */
    public String getAttributeValue() {
        return attributeValue;
    }

    public void setAttributeValue(String attributeValue) {
        this.attributeValue = attributeValue;
    }

    /**
     * *
     * Convert this element to an xpath. A number of issues have been found, for
     * which this method has a solution: -presence of apostrophes in the text;
     * escaping them when present -very long text, which fails when exact
     * matching; contains or start-with are needed here. -problem when exact
     * matches are needed eg. "RBINS", vs "RBINS, OD Nature", exact matching up
     * to the point it no longer works (see point above) -Aside from the
     * problems above, it is possible that this method finds more than one hit
     *
     * @return
     */
    public String toXPath() {
        //  &apos;
        String xPath = null;
        if (this.getContent() != null) { //caveat is that this supposes content will uniquely identify the tag... This will not always be the case... Not a use case until now.
            String content = this.getContent();
            if (content.contains("'")) {
                content = escapeApostrophes(this.getContent());
                //xPathPart = "concat('" + xPathPart + "')";
                if (content.length() > 668) {
                    xPath = "//" + this.getTag() + "[contains(text(),concat('','" + content.substring(0, 668) + "'))]"; //starts-with
                } else {
                    xPath = "//" + this.getTag() + "[text()=concat('','" + content + "')]";
                }
            } else {
                if (content.length() > 668) {
                    xPath = "//" + this.getTag() + "[contains(text(),'" + content.substring(0, 668) + "')]"; //starts-with
                } else {
                    xPath = "//" + this.getTag() + "[text()='" + content + "']";
                }
            }
            //contains leads to inconsistent results
            //xPath = "//" + this.getTag() + "[contains(text(),'" + escapeApostrophes(this.getContent()) + "')]"; //starts-with
            //    xPath = "//" + this.getTag() + "[contains(text(),'" + escapeApostrophes(this.getContent()) + "')]"; //starts-with

        } else if (this.getTag() != null && this.getAttributeName() != null && this.getAttributeValue() != null) {
            xPath = "//" + this.getTag() + "[@" + this.getAttributeName() + "='" + this.getAttributeValue() + "']";
        } else if (this.getTag() != null) {
            xPath = "//" + this.getTag();
        }
        return xPath;
    }

    public String toString() {
        /* List<String> tags = Arrays.asList(tag.split("/"));
        StringBuilder sb = new StringBuilder(tag);
        for (String taggy : tags) {
            sb.append("<");
            sb.append(taggy);
            sb.append(">");
        }*/
        return "<" + tag + ">" + content + "<" + tag + "> (attr:" + attributeName + "=" + attributeValue + ")";
    }

    public String escapeApostrophes(String xPathPart) {
        //   return xPathPart.replace("'", "''");//.replace("\"", "&quot;");
        if (xPathPart.contains("'")) {
            xPathPart = xPathPart.replace("'", "', \"'\", '");
            //xPathPart = "concat('" + xPathPart + "')";
        }
        return xPathPart;
    }

}
