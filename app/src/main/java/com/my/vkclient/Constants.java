package com.my.vkclient;

public interface Constants {

    //    public static final String GROUP_FIELDS = "&fields=";

    interface Database {
        String USER_TABLE_NAME = "USER_TABLE_NAME";
        String GROUP_TABLE_NAME = "GROUP_TABLE_NAME";

        String DATABASE_NAME = "VK_DATABASE";
        String PRIMARY_KEY = "PRIMARY KEY";
        int DATABASE_VERSION = 1;

        String SELECT_FROM = "SELECT * FROM ";
        String WHERE = " where ";
        String STRING_QUESTION = "=?";
    }

    interface API_VK {

//        public static final String API_VK_GET_AUTHORIZE_URL = "https://oauth.vk.com/authorize?client_id=6870329&display=mobile&redirect_uri=https://oauth.vk.com/blank.html&scope=friends,photos,audio,video,status,wall,messages,notifications&response_type=token&v=5.92&state=requestToken";
//        public static final String API_VK_GET_FRIENDS_LIST_URL = "https://api.vk.com/method/friends.get?order=hints&fields=photo_50,photo_100,photo_200_orig,online&v=5.92&access_token=";
        String ACCESS_TOKEN = "&v=5.92&access_token=";
        String API_VK_RESPONSE_ACCESS_DENIED_ERROR = "https://oauth.vk.com/blank.html#error=access_denied&error_reason=user_denied";
        String API_VK_RESPONSE_ACCESS_TOKEN = "https://oauth.vk.com/blank.html#access_token=";
        String API_VK_GET_AUTHORIZE_URL = "https://oauth.vk.com/authorize?client_id=6870329&display=mobile&redirect_uri=https://oauth.vk.com/blank.html&scope=friends,photos,audio,video,status,wall,notifications&response_type=token&v=5.92&state=requestToken";
        String API_VK_GET_FRIENDS_URL = "https://api.vk.com/method/friends.get?order=name";
        String FRIENDS_COUNT = "&count=";
        String FRIENDS_OFFSET = "&offset=";
        String FRIENDS_FIELDS = "&fields=photo_100,online,photo_max_orig,crop_photo";
        String API_VK_GET_USER_URL = "https://api.vk.com/method/users.get?name_case=nom";
        String USER_ID = "&user_ids=";
        String USER_FIELDS = "&fields=photo_100,photo_max_orig,crop_photo";
        String API_VK_GET_NEWS_URL = "https://api.vk.com/method/newsfeed.get?filters=post";
        String NEWS_START_FROM = "&start_from=";
        String NEWS_FIELDS = "&fields=crop_photo,photo_100,photo_max_orig";
        String NEWS_COUNT = "&count=";
        String API_VK_GET_GROUP_URL = "https://api.vk.com/method/groups.getById?group_id=";
    }
}