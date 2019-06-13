package com.whstone.utils.array;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;

public class ArrUtil {


    private static Logger logger = LoggerFactory.getLogger(ArrUtil.class);
    public static String[] reverseArr(String[] arr) {
        String[] reverse = new String[arr.length];
        for(int i=0;i<arr.length;i++) {
            reverse[reverse.length-1-i] = arr[i];
        }
        return reverse;
    }

    public static void main(String[] args) {
        String[] arr = {"1","2","3","4"};
        System.out.println(Arrays.toString(reverseArr(arr)));
    }

}
