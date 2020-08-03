package it.geosolutions.swgeoserver.comm.utils;

import java.math.BigDecimal;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SNUtil {


    public static String NonScientificNotation(Double d) {
        String num = String.valueOf(d);
//        Pattern pattern = Pattern.compile("-?[0-9]*.[0-9]*E[0-9]*");
//        Matcher match = null;
//        match = pattern.matcher(num);
//        if (match.matches()) {
            BigDecimal decimal = new BigDecimal(num);
            num = decimal.setScale(2, BigDecimal.ROUND_HALF_UP).toPlainString();
//        }
        return num;
    }
}
