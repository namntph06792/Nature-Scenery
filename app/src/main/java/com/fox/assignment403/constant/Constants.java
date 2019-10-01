package com.fox.assignment403.constant;

import com.fox.assignment403.BuildConfig;

public class Constants {

    public static final String FLICKR_DOMAIN = "https://www.flickr.com/";

    public static final String METHOD = "services/rest/?method=flickr.favorites.getList";

    public static final String PARAM = "views%2C+media%2C+path_alias%2C+url_sq%2C+url_t%2C+url_s%2C+url_q%2C+url_m%2C+url_n%2C+url_z%2C+url_c%2C+url_l%2C+url_o";

    public static final String OPTION_PER_PAGE = "20";

    public static final String OPTION_PAGE = "1";

    public static final String FORMAT = "json&nojsoncallback=1";

    public static final String FAVOURITE_PHOTO_API = FLICKR_DOMAIN + METHOD +
            "&" + "api_key=" + BuildConfig.API_KEY +
            "&" + "user_id=" + BuildConfig.USER_ID +
            "&" + "extras=" + PARAM +
            "&" + "per_page=" + OPTION_PER_PAGE +
            "&" + "page=" + OPTION_PAGE +
            "&" + "format=" + FORMAT;
}
