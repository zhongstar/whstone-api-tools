package com.whstone.utils.String;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by zhongkf on 2018/08/07
 */
public class StringWSUtil {

    /**
     * 获取指定字符串出现的次数
     *
     * @param srcText  源字符串
     * @param findText 要查找的字符串
     * @return
     */
    public static int appearNumber(String srcText, String findText) {
        int count = 0;
        Pattern p = Pattern.compile(findText);
        Matcher m = p.matcher(srcText);
        while (m.find()) {
            count++;
        }
        return count;
    }

    public static String[] removeArrayEmptyTextBackNewArray(String[] strArray) {
        List<String> strList = Arrays.asList(strArray);
        List<String> strListNew = new ArrayList<>();
        for (int i = 0; i < strList.size(); i++) {
            if (strList.get(i) != null && !strList.get(i).equals("")) {
                strListNew.add(strList.get(i));
            }
        }
        String[] strNewArray = strListNew.toArray(new String[strListNew.size()]);
        return strNewArray;
    }

    /**
     * <b>Summary: 忽略大小写比较两个字符串</b>
     * ignoreCaseEquals()
     *
     * @param str1
     * @param str2
     * @return
     */
    public static boolean ignoreCaseEquals(String str1, String str2) {
        return str1 == null ? str2 == null : str1.equalsIgnoreCase(str2);
    }


    public static void main(String[] args) {
        // System.out.println(StringWSUtil.appearNumber("123456787654321eeee","ee"));
        List<String> stringList = new ArrayList<>();
        stringList.add("222");
        stringList.add("333");
        stringList.add("444");
        stringList.add("555");
        System.out.println(StringWSUtil.joinList(stringList, ";"));
    }

    /**
     * 设置调整配置文件的 内存参数 k = [(stand_size-2) / pri_size]
     * MEMORY_MAX_TARGET * k
     * SGA_MAX_SIZE * k
     * MEMORY_TARGET * k
     * SGA_TARGET * k
     * PGA_AGGREGATE_TARGET * k
     *
     * @param standPfile
     * @return
     */
    static public String setMenPfile(String standPfile, float k) {

        String newStandPfile = new String(standPfile);

        String[] pfileArr = standPfile.split("\n");
        for (int i = 0; i < pfileArr.length; i++) {

            if (pfileArr[i].toLowerCase().contains("memory_target")) {

                String value = pfileArr[i].split("=")[1].trim();
                char lastChar = value.charAt(value.length() - 1);
                boolean lastCharIsDigit = Character.isDigit(lastChar);
                if (!lastCharIsDigit) {
                    value = value.substring(0, value.length() - 1);
                }
                String newValue = String.valueOf(Float.valueOf(Long.parseLong(value) * k).longValue());
                String pfile = pfileArr[i].replace(value, newValue);
                newStandPfile = newStandPfile.replace(pfileArr[i], pfile);

            }

            if (pfileArr[i].toLowerCase().contains("sga_target")) {

                String value = pfileArr[i].split("=")[1].trim();
                char lastChar = value.charAt(value.length() - 1);
                boolean lastCharIsDigit = Character.isDigit(lastChar);
                if (!lastCharIsDigit) {
                    value = value.substring(0, value.length() - 1);
                }
                String newValue = String.valueOf(Float.valueOf(Long.parseLong(value) * k).longValue());
                String pfile = pfileArr[i].replace(value, newValue);
                newStandPfile = newStandPfile.replace(pfileArr[i], pfile);

            }

            if (pfileArr[i].toLowerCase().contains("pga_aggregate_target")) {

                String value = pfileArr[i].split("=")[1].trim();

                char lastChar = value.charAt(value.length() - 1);
                boolean lastCharIsDigit = Character.isDigit(lastChar);
                if (!lastCharIsDigit) {
                    value = value.substring(0, value.length() - 1);
                }
                String newValue = String.valueOf(Float.valueOf(Long.parseLong(value) * k).longValue());
                String pfile = pfileArr[i].replace(value, newValue);
                newStandPfile = newStandPfile.replace(pfileArr[i], pfile);

            }

            if (pfileArr[i].toLowerCase().contains("memory_max_target")) {

                String value = pfileArr[i].split("=")[1].trim();
                char lastChar = value.charAt(value.length() - 1);
                boolean lastCharIsDigit = Character.isDigit(lastChar);
                if (!lastCharIsDigit) {
                    value = value.substring(0, value.length() - 1);
                }
                String newValue = String.valueOf(Float.valueOf(Long.parseLong(value) * k).longValue());
                String pfile = pfileArr[i].replace(value, newValue);
                newStandPfile = newStandPfile.replace(pfileArr[i], pfile);

            }

            if (pfileArr[i].toLowerCase().contains("sga_max_size")) {

                String value = pfileArr[i].split("=")[1].trim();
                char lastChar = value.charAt(value.length() - 1);
                boolean lastCharIsDigit = Character.isDigit(lastChar);
                if (!lastCharIsDigit) {
                    value = value.substring(0, value.length() - 1);
                }
                String newValue = String.valueOf(Float.valueOf(Long.parseLong(value) * k).longValue());
                String pfile = pfileArr[i].replace(value, newValue);
                newStandPfile = newStandPfile.replace(pfileArr[i], pfile);

            }
        }


        return newStandPfile;
    }


    /**
     * 使用指定的 表达式拼接 list字符串
     *
     * @param stringList
     * @param symbol
     * @return
     */
    static public String joinList(List<String> stringList, String symbol) {

        StringBuffer sb = new StringBuffer();
        for (String s : stringList) {

            sb.append(s).append(symbol);


        }
        return sb.toString();

    }

    /**
     * 使用指定的 表达式拼接 数组字符串
     *
     * @param stringArr
     * @param symbol
     * @return
     */
    static public String joinArray(String[] stringArr, String symbol) {

        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < stringArr.length; i++) {

            sb.append(stringArr[i]).append(symbol);


        }
        return sb.toString();

    }

    /**
     * 去掉字符串中指定所有字符，并将分割的字符串放入集合
     *
     * @param string 传入的字符串
     * @param regex  需要去掉的字符
     * @return
     */
    public static List<String> splitString(String string, char regex) {
        List<String> list = new ArrayList<>();
        char[] chars = string.toCharArray();
        StringBuffer sb = new StringBuffer();
        for (char aChar : chars) {
            if (aChar == regex) {
                if (!sb.toString().equals("") && sb.toString() != null) {
                    list.add(sb.toString());
                }
                sb.setLength(0);
            } else {
                sb.append(aChar);
            }
        }
        return list;
    }
}