package com.tv.ui.utils;

import java.io.File;
import java.util.HashMap;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class SkyResLoader
{
    private DocumentBuilder docBuilder;

    public SkyResLoader()
    {
        try
        {
            docBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
        } catch (ParserConfigurationException e)
        {

        }
    }

    public void load(String res_file, String res_type, HashMap<String, String> results)
            throws Exception
    {
        Document doc = docBuilder.parse(new File(res_file));
        NodeList nodes = doc.getElementsByTagName(res_type);
        int index = 0;
        while (index < nodes.getLength())
        {
            Node node = nodes.item(index);
            String id = node.getAttributes().getNamedItem("name").getTextContent();
            String value = node.getTextContent();
            results.put(id, value);
            ++index;
        }
    }
}
