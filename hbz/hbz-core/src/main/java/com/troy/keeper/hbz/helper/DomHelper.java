package com.troy.keeper.hbz.helper;

import org.w3c.dom.Document;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.ByteArrayInputStream;
import java.io.StringWriter;

/**
 * Created by leecheng on 2017/11/1.
 */
public class DomHelper {

    public static String doc2str(Document doc) {
        try {
            StringWriter sw = new StringWriter();
            Transformer transformer = TransformerFactory.newInstance().newTransformer();
            transformer.transform(new DOMSource(doc), new StreamResult(sw));
            return sw.toString();
        } catch (Exception e) {
            throw new IllegalStateException(e);
        }
    }

    public static Document str2doc(String xml) {
        try {
            return DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(new ByteArrayInputStream(xml.getBytes()));
        } catch (Exception e) {
            throw new IllegalStateException(e);
        }
    }

}
