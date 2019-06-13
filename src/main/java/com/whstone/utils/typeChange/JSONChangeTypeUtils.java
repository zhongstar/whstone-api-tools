package com.whstone.utils.typeChange;


import org.jetbrains.annotations.NotNull;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Method;
import java.util.*;


/**
 * JSON服务
 *
 * @author weijun
 */
public class JSONChangeTypeUtils {

    /**
     * 将JSON列表转换为字符串
     *
     * @param jsonList
     * @return
     */
    @NotNull
    public static String JSONListToString(List<JSONObject> jsonList) {
        if (jsonList == null) {
            return "[]";
        }
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append("[");
        for (JSONObject json : jsonList) {
            stringBuffer.append(json.toString());
            stringBuffer.append(",");
        }
        if (stringBuffer.length() != 1) {
            stringBuffer.deleteCharAt(stringBuffer.length() - 1);
        }
        stringBuffer.append("]");
        return stringBuffer.toString();
    }

    /**
     * 将JSON列表转换为字符串(支持num个字符)
     *
     * @param jsonList
     * @return
     */
    public static String JSONListToString(List<JSONObject> jsonList, int num) {
        if (jsonList == null) {
            return "[]";
        }
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append("[");
        for (JSONObject json : jsonList) {
            if (stringBuffer.length() + json.toString().length() >= num)
                break;
            stringBuffer.append(json.toString());
            stringBuffer.append(",");
        }
        if (stringBuffer.length() != 1) {
            stringBuffer.deleteCharAt(stringBuffer.length() - 1);
        }
        stringBuffer.append("]");
        return stringBuffer.toString();
    }

    /**
     * 将字符串转化为JSON列表
     *
     * @param jsonListString 格式：[{"id":1,num:3},{"id":2,num:4}]
     * @return
     */
    public static List<JSONObject> stringToJSONList(String jsonListString) {
        List<JSONObject> jsonList = new ArrayList<JSONObject>();
        try {
            if (jsonListString.length() > 2) {
                String jsonListStringTmp = jsonListString.substring(2, jsonListString.length() - 1);
                String[] jsonStrings = jsonListStringTmp.split("}");
                for (String jsonString : jsonStrings) {
                    System.out.println(jsonString.substring(0) + "}");

                    JSONObject jsonObject = new JSONObject(jsonString.substring(1) + "}");

                    jsonList.add(jsonObject);
                }
            }
        } catch (Exception e) {
            System.out.println(e);
            return new ArrayList<JSONObject>();
        }
        return jsonList;
    }

    /**
     * 解析从数据库转化的字符串
     *
     * @param jsonListString
     * @return
     */

    public static List<JSONObject> dataStringToJSONList(String jsonListString) {
        List<JSONObject> jsonList = new ArrayList<JSONObject>();
        try {
            if (jsonListString.length() > 2) {
                String jsonListStringTmp = jsonListString.substring(0, jsonListString.length() - 1);
                String[] jsonStrings = jsonListStringTmp.split("}");
                for (String jsonString : jsonStrings) {
                    JSONObject jsonObject = new JSONObject(jsonString.substring(0) + "}");

                    jsonList.add(jsonObject);
                }
            }
        } catch (Exception e) {
            System.out.println(e);
            return new ArrayList<JSONObject>();
        }
        return jsonList;
    }


    /**
     * 将Javabean转换为Map
     *
     * @param javaBean javaBean
     * @return Map对象
     */
    public static Map toMap(Object javaBean) {

        Map result = new HashMap();
        Method[] methods = javaBean.getClass().getDeclaredMethods();

        for (Method method : methods) {

            try {

                if (method.getName().startsWith("get")) {

                    String field = method.getName();
                    field = field.substring(field.indexOf("get") + 3);
                    field = field.toLowerCase().charAt(0) + field.substring(1);

                    Object value = method.invoke(javaBean, (Object[]) null);
                    result.put(field, null == value ? "" : value.toString());

                }

            } catch (Exception e) {
                e.printStackTrace();
            }

        }

        return result;

    }

    /**
     * 将Json对象转换成Map
     *
     * @param jsonString json对象
     * @return Map对象
     * @throws JSONException
     * @throws JSONException
     */
    public static Map toMap(String jsonString) throws JSONException {

        JSONObject jsonObject = new JSONObject(jsonString);

        Map result = new HashMap();
        Iterator iterator = jsonObject.keys();
        String key = null;
        Object value = null;

        while (iterator.hasNext()) {

            key = (String) iterator.next();
            //       value = jsonObject.getString(key).toString();
            value = jsonObject.get(key);
            System.out.println(key + "===" + value);
            result.put(key, value);

        }
        return result;

    }

    /**
     * 将JavaBean转换成JSONObject（通过Map中转）
     *
     * @param bean javaBean
     * @return json对象
     */
    public static JSONObject toJSON(Object bean) {

        return new JSONObject(toMap(bean));

    }

    /**
     * 将Map转换成Javabean
     *
     * @param javabean javaBean
     * @param data     Map数据
     */
    public static Object toJavaBean(Object javabean, Map data) {

        Method[] methods = javabean.getClass().getDeclaredMethods();
        for (Method method : methods) {

            try {
                if (method.getName().startsWith("set")) {

                    String field = method.getName();
                    field = field.substring(field.indexOf("set") + 3);
                    field = field.toLowerCase().charAt(0) + field.substring(1);
                    method.invoke(javabean, new Object[]{

                            data.get(field)

                    });

                }
            } catch (Exception e) {
            }

        }

        return javabean;

    }

    /**
     * JSONObject到JavaBean
     *
     * @param bean javaBean
     * @return json对象
     * @throws JSONException
     * @throws JSONException
     */
    public static void toJavaBean(Object javabean, String jsonString)
            throws JSONException {

        JSONObject jsonObject = new JSONObject(jsonString);

        Map map = toMap(jsonObject.toString());

        toJavaBean(javabean, map);

    }


}
