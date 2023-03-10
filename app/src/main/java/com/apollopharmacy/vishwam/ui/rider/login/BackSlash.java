package com.apollopharmacy.vishwam.ui.rider.login;

public class BackSlash {
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
