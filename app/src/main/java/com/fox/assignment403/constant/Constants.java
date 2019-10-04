package com.fox.assignment403.constant;

import com.fox.assignment403.BuildConfig;

public class Constants {

    public static final String FLICKR_DOMAIN = "https://www.flickr.com/";

    public static final String METHOD = "services/rest/?method=flickr.favorites.getList";

    public static final String PARAM = "views%2C+media%2C+path_alias%2C+url_sq%2C+url_t%2C+url_s%2C+url_q%2C+url_m%2C+url_n%2C+url_z%2C+url_c%2C+url_l%2C+url_o";

    public static final String FORMAT = "&format=json&nojsoncallback=1";

    public static final String FAVOURITE_PHOTO_API = FLICKR_DOMAIN + METHOD +
            "&" + "api_key=" + BuildConfig.API_KEY +
            "&" + "user_id=" + BuildConfig.USER_ID +
            "&" + "extras=" + PARAM;

    public static final int NUM_COLUMNS = 2;

    public static final String PROGRESS_UPDATE = "progress_update";

    public static final int PERMISSION_REQUEST_CODE = 1;

}
