package com.my.vkclient;

public interface Constants {
    String STRING_EQUALS = "=";
    String STRING_QUESTION = "?";
    String STRING_AND = "&";
    String STRING_COMMA = ",";
    String STRING_NUMBER_FORMAT = "%.1f%c";
    String STRING_NUMBER_POSTFIX = "KMGTPE";
    String STRING_EMPTY = "";
    String STRING_SPACE = " ";
    String STRING_SLASH = "/";
    int INT_THOUSAND = 1000;
    String USER_ID_INTENT_KEY = "UserId";

    interface Database {
        String USER_TABLE_NAME = "USER_TABLE_NAME";
        String FRIEND_TABLE_NAME = "FRIEND_TABLE_NAME";
        String GROUP_TABLE_NAME = "GROUP_TABLE_NAME";
        String NEWS_TABLE_NAME = "NEWS_TABLE_NAME";

        String DATABASE_NAME = "VK_DATABASE";
        String PRIMARY_KEY = "PRIMARY KEY";
        String AUTOINCREMENT = "AUTOINCREMENT";
        int DATABASE_VERSION = 1;

        String SQL_TABLE_CREATE_TEMPLATE = "CREATE TABLE IF NOT EXISTS %s (%s);";
        String SELECT_FROM = "SELECT * FROM ";
        String DATABASE_WHERE = " WHERE ";
        String DATABASE_JOIN_LIMIT = " JOIN %1$s ON %2$s = %3$s order by %4$s, %5$s LIMIT %6$s,%7$s";
        String DATABASE_LIMIT = " LIMIT ";

        String UPGRADE_NOT_SUPPORTED = "Upgrade not supported";
        String FIELD_DONT_HAVE_TYPE_ANNOTATION = "Field don't have type annotation";
    }

    interface API_VK {
        //        public static final String GROUP_FIELDS = "&fields=";
//        public static final String API_VK_GET_AUTHORIZE_URL = "https://oauth.vk.com/authorize?client_id=6870329&display=mobile&redirect_uri=https://oauth.vk.com/blank.html&scope=friends,photos,audio,video,status,wall,messages,notifications&response_type=token&v=5.92&state=requestToken";
//        public static final String API_VK_GET_FRIENDS_LIST_URL = "https://api.vk.com/method/friends.get?order=hints&fields=photo_50,photo_100,photo_200_orig,online&v=5.92&access_token=";
        String ACCESS_TOKEN = "&v=5.92&access_token=";
        String API_VK_RESPONSE_ACCESS_DENIED_ERROR = "https://oauth.vk.com/blank.html#error=access_denied&error_reason=user_denied";
        String API_VK_RESPONSE_ACCESS_TOKEN = "https://oauth.vk.com/blank.html#access_token=";
        String API_VK_GET_AUTHORIZE_URL = "https://oauth.vk.com/authorize?client_id=6870329&display=mobile&redirect_uri=https://oauth.vk.com/blank.html&scope=friends,photos,audio,video,status,wall,notifications&response_type=token&v=5.92&state=requestToken";
        String API_VK_GET_FRIENDS_URL = "https://api.vk.com/method/friends.get?order=name";
        String FRIENDS_COUNT = "&count=";
        String FRIENDS_OFFSET = "&offset=";
        String FRIENDS_FIELDS = "&fields=online,photo_100,photo_max_orig,crop_photo";
        String API_VK_GET_USER_URL = "https://api.vk.com/method/users.get?name_case=nom";
        String USER_ID = "&user_ids=";
        String USER_FIELDS = "&fields=online,photo_100,photo_max_orig,crop_photo";
        String API_VK_GET_NEWS_URL = "https://api.vk.com/method/newsfeed.get?filters=post";
        String NEWS_START_FROM = "&start_from=";
        String NEWS_FIELDS = "&fields=online,photo_100,photo_max_orig,crop_photo";
        String NEWS_COUNT = "&count=";
        String API_VK_GET_GROUP_URL = "https://api.vk.com/method/groups.getById?group_id=";
        String STATE_REQUEST_TOKEN = "state=requestToken";
    }
}