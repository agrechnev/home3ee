package agrechnev.linktable;

import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by Oleksiy Grechnyev on 10/22/2016.
 * Manages the table with all links between our entities from package model
 * <p>
 * Can read the link table from an XML file
 */
public class EntityLinkTable {

    private Set<EntityLink> linkTable = new HashSet<>(); // The links table

    private Set<Field> fieldTable = new HashSet<>(); // All fields of linkTable for quick search

    /**
     * Exception used for this class
     */
    public static class EntityLinkException extends Exception {
        public EntityLinkException(String message) {
            super(message);
        }

        public EntityLinkException(String message, Throwable cause) {
            super(message, cause);
        }
    }

    /**
     * Costructor:
     * Load table from an XML file
     * File structure: see example
     *
     * @param fileName name of the XML file scripts/links.xml
     * @throws EntityLinkException is anything goes wrong
     */
    public EntityLinkTable(String fileName) throws EntityLinkException {
        try {
            // Read the XML file using JDOM
            Document document = new SAXBuilder().build(new File(fileName));
            Element root=document.getRootElement();
            if (!root.getName().equals("links")) {
                throw new EntityLinkException("EntityLinkTable: no root element <links> in the XML file");
            }

            // Get the class prefix if any
            String prefix=root.getAttributeValue("prefix");
            if (prefix == null) {
                prefix="";
            }

            // Get all links as a list and add process them one by one
            List<Element> elements=root.getChildren();
            for (Element element : elements) {
                addXMLLink(element,prefix);
            }

        } catch (IOException|JDOMException e) {
            throw new EntityLinkException("Exception in EntityLinkTable ",e);
        }
    }

    /**
     * Parse a JDOM XML <link> linkTag and add it to the table
     * @param linkTag <link> JDOM element
     * @param prefix class prefix, e.g. "agrechnev.models."
     * @throws EntityLinkException If syntax error in XML
     */
    private void addXMLLink(Element linkTag, String prefix) throws EntityLinkException {
        // Check that the XML element is <link>
        String name=linkTag.getName();
        if (!name.equals("link")) {
            throw new EntityLinkException(String.format("addXMLLink: wrong tag <%s>, expected <link>",name));
        }

        // Read the values of classes and fields

        String class_S= getTagValue(linkTag,"class_s");
        String field_S= getTagValue(linkTag,"field_s");
        String class_L= getTagValue(linkTag,"class_l");
        String field_L= getTagValue(linkTag,"field_l");

        // Now read the <columns> tag
        List<String> columns_S=new ArrayList<>();
        List<String> columns_L=new ArrayList<>();

        Element element=linkTag.getChild("columns");
        if (element == null) throw new EntityLinkException("addXMLLink: no <columns> in a link");
        for (Element columnTag : element.getChildren()) {
            // Check that the XML element is <column>
            name = columnTag.getName();
            if (!name.equals("column")) {
                throw new EntityLinkException(String.format("addXMLLink: wrong tag <%s>, expected <column>", name));
            }

            // Add the <s> tag
            columns_S.add(getTagValue(columnTag,"s"));
            // Add the <l> tag
            columns_S.add(getTagValue(columnTag,"l"));
        }

        // Add the class prefix
        class_S=prefix+class_S;
        class_L=prefix+class_L;

        // Create a new link object
        EntityLink newLink=new EntityLink(class_S,field_S,class_L,field_L,columns_S,columns_L);

        // Add it to the table with all checks
        addLink(newLink);
    }

    /**
     * JDOM getChildText, throwing exception if the text is null or empty
     * @param element The parent element
     * @param child The child tag name
     * @return  The child tag text
     * @throws EntityLinkException if null or empty
     */
    private static String getTagValue(Element element, String child) throws EntityLinkException {
        String result=element.getChildText(child);
        if (result == null || result.trim().equals(""))
            throw new EntityLinkException(String.format("getTagValue: tag <%s> not found or empty",child));

        return result.trim();
    }

    private void addLink(EntityLink newLink) throws EntityLinkException {
        // Check everything first

        // Check that the class names with prefix are correct
        Class cS = checkClass(newLink.getClass_S());
        Class cL = checkClass(newLink.getClass_L());

        // Check that field names are correct and not used twice
        // then add fields to fieldTable
        Field fS=addField(cS,newLink.getField_S());
        Field fL=addField(cL,newLink.getField_L());

        // Check field types
        if (fL.getType() != cS) {
            throw new EntityLinkException(String.format("Field %s.%s is of wrong type", cL.getName(), fL.getName()));
        }

        if (fS.getType() != HashSet.class) {
            throw new EntityLinkException(String.format("Field %s.%s is of wrong type", cS.getName(), fS.getName()));
        }

        // Finally add new object to the table
        linkTable.add(newLink);
    }

    /**
     * Find class by name, exception if not found
     * @param name Class name
     * @return Class object
     * @throws EntityLinkException if class does not exist
     */
    private Class checkClass(String name) throws EntityLinkException {
        Class c;
        try {
            c=Class.forName(name);
        } catch (ClassNotFoundException e) {
            throw new EntityLinkException(String.format("Cannot find class %s", name));
        }
        return c;
    }

    /**
     * Check that field is correct and add it to fieldTable
     * @param c  Class
     * @param name Field name
     * @return Field object
     * @throws EntityLinkException on error
     */
    private Field addField(Class c, String name) throws EntityLinkException {
        Field f;
        try {
            f=c.getField(name);
        } catch (NoSuchFieldException e) {
            throw new EntityLinkException(String.format("Cannot find field %s", name));
        }
        if (fieldTable.contains(f)) {
            throw new EntityLinkException(String.format("Field %s used twice", name));
        } else {
            fieldTable.add(f);
        }

        return f;
    }


}
