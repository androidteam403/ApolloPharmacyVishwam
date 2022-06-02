package com.apollopharmacy.vishwam.ui.home.cms.complainList;

public class BackShlash {

    public static String removeBackSlashes(String resp) {
        return resp.replaceAll("\\\\", "");
    }

    public static String removeSubString(String res) {
        if (res.contains("}{"))
            return res.substring(1, res.indexOf("}{") + 1);
        else
            return res.substring(1, res.length() - 1);
    }
}
