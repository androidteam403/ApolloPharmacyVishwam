package com.apollopharmacy.vishwam.ui.home.cms.complainList;

public class BackShlash {

    public static String removeBackSlashes(String resp) {
        String resp1 = resp.toString()
                .replace("\"[", "[").replace("]\"", "]")
                .replace("\\\"{", "{").replace("}\\\"", "}")
                .replace("\\\\\\\"", "\"");
        return resp1.replaceAll("\\\\", "");
       // return resp.replaceAll("\\\\", "");
    }

    public static String removeSubString(String res) {
        if (res.contains("}{"))
            return res.substring(1, res.indexOf("}{") + 1);
        else
            return res.substring(1, res.length() - 1);


    }



}
