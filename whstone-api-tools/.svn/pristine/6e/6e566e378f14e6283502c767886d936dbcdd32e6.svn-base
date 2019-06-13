package com.whstone.utils.EncryptionAndDecryption;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.methods.GetMethod;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLEncoder;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;


public class Sha1Utils {

    public static String signRequest(String request, String key) {
        try {
            Mac mac = Mac.getInstance("HmacSHA1");
            SecretKeySpec keySpec = new SecretKeySpec(key.getBytes(), "HmacSHA1");
            mac.init(keySpec);
            mac.update(request.getBytes());
            byte[] encryptedBytes = mac.doFinal();
            //System.out.println("HmacSHA1 hash: " + encryptedBytes);
            return Base64.encodeBase64String(encryptedBytes);
        } catch (Exception ex) {
            System.out.println("unable to sign request");
            ex.printStackTrace();
        }
        return null;
    }

    public static void main(String[] args) throws InvalidKeyException,
            NoSuchAlgorithmException, HttpException, IOException {
        // TODO Auto-generated method stub

        String developerServer = "http://172.23.131.101:8080//client//api";
        String ApiKey = "NioXWStUPMHiSfQFab_cl82_l57JXdlTPDXWQZbmIM94HNcGCVjgUEqwD3h0GiyHS0X623hqHiIpTAZbwqT0AA";
        String s_secretKey = "1XJ3uxpl6xy3M5P_mcLcTapWvb8MqdIqLuWB0hFtvXIyagcKeUSYXN4Db2MmZDwo8TKNe1lp3wQZkX0kfzU1Ow";

        String encodedApiKey = URLEncoder.encode(ApiKey, "UTF-8");

        //	String encodedApiKey = ApiKey;
        String encodedPublicIpId = "", encodedVmId = "";

        //������queryAsyncJobResult
        String methodName = "getUser";
        //���� 37610c7d-8703-447b-ac5c-d4fc5f2cca74
//		String autoParmeter="&serviceofferingid=0d2541de-0d7e-454b-b7fd-cc9b6fd07a77&templateid=2d102467-4f19-42aa-ada6-cc578d128b07&zoneid=c6cc1565-7f87-4e31-b5bf-7220b348ec3f";
        String autoParmeter = "&response=json&userapikey=NioXWStUPMHiSfQFab_cl82_l57JXdlTPDXWQZbmIM94HNcGCVjgUEqwD3h0GiyHS0X623hqHiIpTAZbwqT0AA";
        //	String autoParmeter="";
        String urlold = "apikey=" + encodedApiKey + "&command=" + methodName + autoParmeter;

        urlold = urlold.toLowerCase();
        System.out.println(urlold);
        String signature = signRequest(urlold, s_secretKey);
        String encodedSignature = URLEncoder.encode(signature, "UTF-8");


        System.out.println("encodedSignature=" + encodedSignature);

        String url = developerServer + "?apikey=" + encodedApiKey + "&command=getUser&response=json&userapikey=NioXWStUPMHiSfQFab_cl82_l57JXdlTPDXWQZbmIM94HNcGCVjgUEqwD3h0GiyHS0X623hqHiIpTAZbwqT0AA"
                + "&signature=" + encodedSignature;
        System.out.println(url);


        HttpClient client = new HttpClient();
        GetMethod method = new GetMethod(url);

        int responseCode = client.executeMethod(method);

        // s_logger.info("url is " + url);
        // s_logger.info("list ip addresses for user " + userId +
        // " response code: " + responseCode);
        if (responseCode == 200) {
            System.out.println(method.getResponseBodyAsString());


            System.out.println("返回的结果：" + method.getResponseBodyAsString());

            InputStream is = method.getResponseBodyAsStream();

            Map<String, String> success = getSingleValueFromXML(is,
                    new String[]{"account"});
            System.out.println(success.get("account") + "====" + success.get("apikey"));


            // s_logger.info("Enable Static NAT..success? " +
            // success.get("success"));
        } else {
            // s_logger.error("Enable Static NAT failed with error code: " +
            // responseCode + ". Following URL was sent: " + url);
            // return responseCode;
            System.out.println("error=" + method.getResponseBodyAsString());
        }

    }

    public static Map<String, String> getSingleValueFromXML(InputStream is,
                                                            String[] tagNames) {
        Map<String, String> returnValues = new HashMap<String, String>();
        try {
            // ���� DocumentBuilderFactory.newInstance() �����õ����� DOM �������Ĺ���
            DocumentBuilderFactory builderFactory = DocumentBuilderFactory
                    .newInstance();
            // ���ù�������� newDocumentBuilder�����õ� DOM ����������
            DocumentBuilder builder = builderFactory.newDocumentBuilder();
            Document doc = builder.parse(is);
            Element rootElement = doc.getDocumentElement();

            for (int i = 0; i < tagNames.length; i++) {
                NodeList targetNodes = rootElement
                        .getElementsByTagName(tagNames[i]);
                if (targetNodes.getLength() <= 0) {
                    // s_logger.error("no " + tagNames[i] +
                    // " tag in XML response...returning null");
                } else {
                    // TODO 下面两行更改
//					returnValues.put(tagNames[i], targetNodes.item(0)
//							.getTextContent());
                }
            }
        } catch (Exception ex) {
            // s_logger.error("error processing XML", ex);
        }
        System.out.println(returnValues);
        return returnValues;
    }
}
