package com.apollopharmacy.vishwam.ui.rider.login;

public class BackSlash {
    public static String removeBackSlashes(String resp) {
        String resp1 = resp.toString()
                .replace("\"[", "[").replace("]\"", "]")
                .replace("\\\"{", "{").replace("}\\\"", "}")
                .replace("\\\\\\\"", "\"");
        return resp1.replaceAll("\\\\", "");
    }
//     String responseString = resp.replaceAll("\\\\", "");
//        if (responseString.contains("\\\\")){
//            return responseString.replaceAll("\\\\", "");
//        }else {
//            return responseString;
//        }

    public static String removeSubString(String res) {


        if (res.contains("}{"))
            return res.substring(1, res.indexOf("}{") + 1);
        else
            return res.substring(1, res.length() - 1);


    }
}
