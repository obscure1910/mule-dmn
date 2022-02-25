package de.obscure.dmn.internal;

public class XmlHelper {

    private XmlHelper() {
        // statics only
    }

    public static String toValidXmlTag(String str) {
        return str.replaceAll(" ", "_");
    }



}
