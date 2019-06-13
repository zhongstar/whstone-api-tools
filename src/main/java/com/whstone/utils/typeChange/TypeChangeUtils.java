package com.whstone.utils.typeChange;

import net.sf.json.JSON;
import net.sf.json.JSONObject;
import net.sf.json.xml.XMLSerializer;

import org.w3c.dom.Document;

public class TypeChangeUtils {


    /**
     * 将xml字符串转换为JSON对象
     *
     * @param
     * @return JSON对象
     */
    public JSON getJSONFromXml(String xmlString) {
        XMLSerializer xmlSerializer = new XMLSerializer();
        JSON json = xmlSerializer.read(xmlString);
        return json;
    }

    /**
     * 将xmlDocument转换为JSON对象
     *
     * @param xmlDocument XML Document
     * @return JSON对象
     */
    public JSON getJSONFromXml(Document xmlDocument) {
        String xmlString = xmlDocument.toString();
        return getJSONFromXml(xmlString);
    }

    /**
     * 将xml字符串转换为JSON字符串
     *
     * @param xmlString
     * @return JSON字符串
     */
    public String getJSONStringFromXml(String xmlString) {
        return getJSONFromXml(xmlString).toString();
    }

    /**
     * 将xmlDocument转换为JSON字符串
     *
     * @param xmlDocument XML Document
     * @return JSON字符串
     */
    public String getXMLtoJSONString(Document xmlDocument) {
        return getJSONStringFromXml(xmlDocument.toString());
    }


    /**
     * 读取XML文件准换为JSON字符串
     * @param xmlFile  XML文件
     * @return JSON字符串
     */
//    public String getXMLFiletoJSONString(String xmlFile) {
//        InputStream is = JsonUtil.class.getResourceAsStream(xmlFile);
//        String xml;
//        JSON json = null;
//        try {
//            xml = IOUtils.toString(is);
//            XMLSerializer xmlSerializer = new XMLSerializer();
//            json = xmlSerializer.read(xml);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        return json.toString();
//    }

    /**
     * 将Java对象转换为JSON格式的字符串
     *
     * @param javaObj POJO,例如日志的model
     * @return JSON格式的String字符串
     */
    public static String getJsonStringFromJavaPOJO(Object javaObj) {
        return JSONObject.fromObject(javaObj).toString(1);
    }

}
