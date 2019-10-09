package com.fox.assignment403.constant;

import com.fox.assignment403.BuildConfig;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

public class Constants {

    public static final String FLICKR_DOMAIN = "https://www.flickr.com/services/rest/";

    public static final String METHOD = "flickr.favorites.getList";

    public static final String OPTION = "views, media, path_alias, url_sq, url_t, url_s, url_q, url_m, url_n, url_z, url_c, url_l, url_o";

    public static final String FORMAT = "json";

    public static final int NUM_COLUMNS = 2;

    public static final int MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE = 999;

    public static final String API_KEY = BuildConfig.API_KEY;

    public static String USER_ID;

    static {
        try {
            USER_ID = URLDecoder.decode(BuildConfig.USER_ID, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

}
