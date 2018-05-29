package com.example.omarla.food2u_repo;

/**
 * Created by hp on 2/27/2018.
 */


public class URLs {
    //Validate Contact No URLs
    public static String ValidateContactURL1 = "http://apilayer.net/api/validate?access_key=ceb880f5d03a6a34974069a530945ea7&number=+91";
    public static String ValidateContactURL2 = "&country_code=&format=1";

    //Validate Email URLs
    public static String ValidateEmailURL1 = "http://apilayer.net/api/check?access_key=6e2a3850859814fe3fbc9c3c08567204&email=";
    public static String ValidateEmaiURL2 = "&smtp=1&format=1";

    public static String BASE_URL = "http://172.26.17.35:8080";


    public static String SAVE_IMAGE="/uploadImage";
    public static String SHOW_FEEDBACK="/avg_feedback";

    public static String REGISTER = BASE_URL+"/Signup";

    public static String LOGIN = BASE_URL+"/login";

    public static String STALL_NAME=BASE_URL+"/stalls";

    public static String ITEM_LIST=BASE_URL+"/items";

    public static String ITEM_LIST2=BASE_URL+"/items2";

    public static String ADD_STALL=BASE_URL+"/add_bussiness";

    public static String VENDOR_STALL=BASE_URL+"/vendor_stall2";

    public static String REMOVE_ITEM=BASE_URL+"/remove_items";

    public static String REMOVE_STALL=BASE_URL+"/remove_stall";

    public static String ADD_ITEM=BASE_URL+"/item_master";

    public  static String UPDATE_STALL=BASE_URL+"/update_stall";

    public static String EDIT_PROFILE=BASE_URL+"/edit_profile";

    public static  String EDIT_PROFILE_PIC=BASE_URL+"/Uploading";

    public static String UPDATE_ITEM=BASE_URL+"/update_item";

    public static String ADD_CART=BASE_URL+"/add_orders";

    public static String CART_LIST=BASE_URL+"/cart_display";

    public static String DELETE_CART=BASE_URL+"/cart_delete";

    public static String HISTORY=BASE_URL+"/history";

    public static String GOOGLE_LOGIN=BASE_URL+"/Signup_google";

    public static String SAVE_FEEDBACK=BASE_URL+"/feedback_saving";

    public static String REQUEST_FOR_STALL=BASE_URL+"/fetch_request";

    public static String PERMISSION_STALL=BASE_URL+"/confirm_stall";

    public static String UNAVAILABLE=BASE_URL+"/item_available";

    public static String AVAILABLE=BASE_URL+"/item_available";

    public static String OPENSTALL=BASE_URL+"/open_stalls";

    public static String CLOSESTALL=BASE_URL+"/open_stalls";

    public static String ITEM_DESC=BASE_URL+"/des_item";
    public static String SEARCH=BASE_URL+"/search_food_location";




    public static String REQUEST_COUNT=BASE_URL+"/request_count";

    public static String PAYMENT_DETAILS_BY_DATE=BASE_URL+"/fetch_daily_amt";
    public static String PAYMENT_DETAILS_BY_DATE_paid=BASE_URL+"/fetch_daily_amt_paid";

    public static String IS_PAID=BASE_URL+"/is_paid";

    public static String USER_BUSINESS_COUNT=BASE_URL+"/count_stall";

    public static String GETPASSWORD=BASE_URL+"/getpassword";
    public static String GETPASSWORDUSER=BASE_URL+"/getpassword_userid";
    public  static  String CHANGEPASSWORD=BASE_URL+"/change_password";

    public  static  String SHOW_FEEDBACK1=BASE_URL+"/avg_feedback_stall";

}


