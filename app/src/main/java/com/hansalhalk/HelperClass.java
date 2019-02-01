package com.hansalhalk;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.view.MotionEvent;
import android.widget.Button;

public class HelperClass {

    public static String domain_url = "https://hansalhalk.com";
    public static String login_url = domain_url + "/api/auth/login";
    public static String logout_url = domain_url + "/api/logout";
    public static String user_info_url = domain_url + "/api/user";
    public static String update_user_info_url = domain_url + "/api/profile/update";
    public static String departments_categories_url = domain_url + "/api/cats";
    public static String search_url = domain_url + "/api/advsearch";
    public static String profile_images_url = domain_url + "/resources/assets/cpanel/images/users/";

    public static String TOKEN = "token";
    public static String CHECKED_LIST = "checked_list";
    public static String COUNTRY = "country";
    public static String CITY = "city";
    public static String USER = "user";

    public static String SEARCH_OR_NAV_ITEM = "search_or_nav_item";
    public static String DEPARTMENT_ID = "department_id";

    public static String DEPARTMENTS_LIST = "departments_list";

    public static boolean isOnline(Context context){
        ConnectivityManager cm = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        boolean isConnected = activeNetwork != null && activeNetwork.isConnectedOrConnecting();
        return isConnected;
    }

    public static void highLightButton(Context context, Button button, MotionEvent event){
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            // change color
            button.setBackground(context.getResources().getDrawable(R.drawable.shape_button_pressed));
        }
        else if (event.getAction() == MotionEvent.ACTION_UP) {
            // set to normal color
            button.setBackground(context.getResources().getDrawable(R.drawable.shape_button_normal));
        }
        else if (event.getAction() == MotionEvent.ACTION_BUTTON_RELEASE) {
            // set to normal color
            button.setBackground(context.getResources().getDrawable(R.drawable.shape_button_normal));
        }
    }
}
