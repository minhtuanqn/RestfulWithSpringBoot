package com.utils;

import java.util.Map;

public class ValidatorUtils {
    /**
     * check not negative number
     * @param number
     * @param numberErr
     * @param fieldName
     * @return number object
     */
    public static  Integer checkNotNegativeIntNumber(String number,
                                      Map<String, String> numberErr, String fieldName) {
        Integer checkedNum = null;
        if(!number.matches("[0-9]{1,}")) {
            numberErr.put(fieldName, "must be a number and must not be negative");
            return null;
        }
        else {
            return Integer.parseInt(number);
        }
    }

    /**
     * check positive number or not
     * @param number
     * @param numberErr
     * @param fieldName
     * @return
     */
    public static boolean checkGreaterThanZero(Integer number, Map<String, String> numberErr, String fieldName) {
        if(number <= 0) {
            numberErr.put(fieldName, "must be positive");
            return false;
        }
        return true;
    }


}
