package com.my.vkclient;

public interface Constants {
    String STRING_EQUALS = "=";
    String STRING_NOT_EQUALS = "<>";
    String STRING_QUESTION = "?";
    String STRING_AND = "&";
    String STRING_COMMA = ",";
    String STRING_NUMBER_FORMAT = "%.1f%c";
    String STRING_NUMBER_POSTFIX = "KMGTPE";
    String STRING_EMPTY = "";
    String STRING_SPACE = " ";
    String STRING_SLASH = "/";
    String NAME_FORMAT = "%s %s";
    String AUDIO_FORMAT = "%s\n%s";
    String SEARCH_IN_GOOGLE_FORMAT = "https://www.google.com/search?q=%s %s";
    String DATE_FORMAT = "dd MMMM HH:mm";
    int INT_ZERO = 0;
    int INT_THOUSAND = 1000;
    int INT_ONE_KB = 1024;

    interface IntentKey {
        String USER_ID_INTENT_KEY = "USER_ID_INTENT_KEY";
        String IMAGE_URL_INTENT_KEY = "IMAGE_URL_INTENT_KEY";
    }

    interface Database {
        String USER_TABLE_NAME = "USER_TABLE";
        String FRIEND_TABLE_NAME = "FRIEND_TABLE";
        String GROUP_TABLE_NAME = "GROUP_TABLE";
        String NEWS_TABLE_NAME = "NEWS_TABLE";
        String ATTACHMENT_TABLE_NAME = "ATTACHMENT_TABLE";
        String USER_PHOTO_TABLE_NAME = "USER_PHOTO_TABLE";

        String DATABASE_NAME = "VkDatabase.db";
        String PRIMARY_KEY = "PRIMARY KEY";
        String AUTOINCREMENT = "AUTOINCREMENT";
        int DATABASE_VERSION = 6;

        String SQL_TABLE_CREATE_TEMPLATE = "CREATE TABLE IF NOT EXISTS %s (%s);";
        String DROP_TABLE_IF_EXISTS = "DROP TABLE IF EXISTS %s;";
        String SELECT_FROM = "SELECT * FROM ";
        String DATABASE_WHERE = " WHERE %s %s %s";
        String DATABASE_JOIN = " JOIN %s ON %s = %s order by %s, %s";
        String DATABASE_LIMIT = " LIMIT %s,%s";
        String DATABASE_ORDER_BY_DESC = " ORDER BY %s DESC";
        String DATABASE_ORDER_BY_ASC = " ORDER BY %s ASC";

        String FIELD_DO_NOT_HAVE_TYPE_ANNOTATION = "Field don't have type annotation";
    }

    interface API_VK {
        String API_VERSION = "&v=5.95&";
        String ACCESS_TOKEN = API_VERSION + "access_token=";
        String API_VK_OAUTH_VK_COM = "https://oauth.vk.com/";
        String API_VK_RESPONSE_ACCESS_DENIED_ERROR = API_VK_OAUTH_VK_COM + "blank.html#error=access_denied&error_reason=user_denied";
        String API_VK_RESPONSE_ACCESS_TOKEN = API_VK_OAUTH_VK_COM + "blank.html#access_token=";
        String AUTHORIZE_SCOPE = "&scope=friends,photos,audio,video,status,wall,notifications,offline";
        String CLIENT_ID = "6870329";
        String STATE_REQUEST_TOKEN = "state=requestToken";
        String API_VK_GET_AUTHORIZE_URL = API_VK_OAUTH_VK_COM + "authorize?client_id=" + CLIENT_ID + "&display=mobile&redirect_uri=" + API_VK_OAUTH_VK_COM + "blank.html" + AUTHORIZE_SCOPE + "&response_type=token" + API_VERSION + STATE_REQUEST_TOKEN;
        String API_VK_METHOD = "https://api.vk.com/method/";
        String API_VK_GET_USER_URL = API_VK_METHOD + "users.get?name_case=nom";
        String USER_ID = "&user_ids=";
        String USER_FIELDS = "&fields=online,photo_100,photo_max_orig,crop_photo,about,common_count,counters,education,followers_count,games,home_town,interests,last_seen,movies,music,status,verified";
        String API_VK_GET_FRIENDS_URL = API_VK_METHOD + "friends.get?order=name";
        String FRIENDS_COUNT = "&count=";
        String FRIENDS_OFFSET = "&offset=";
        String FRIENDS_FIELDS = USER_FIELDS;
        String API_VK_GET_NEWS_URL = API_VK_METHOD + "newsfeed.get?filters=post";
        String NEWS_START_FROM = "&start_from=";
        String NEWS_FIELDS = USER_FIELDS;
        String NEWS_COUNT = "&count=";
        String API_VK_GET_GROUP_URL = API_VK_METHOD + "groups.getById?group_id=";
        String LIKE_FIELDS = "?type=post&owner_id=%s&item_id=%s";
        String API_VK_SET_LIKE_POST = API_VK_METHOD + "likes.add" + LIKE_FIELDS + ACCESS_TOKEN;
        String API_VK_SET_DISLIKE_POST = API_VK_METHOD + "likes.delete" + LIKE_FIELDS + ACCESS_TOKEN;
        String API_VK_GET_PHOTOS_URL = API_VK_METHOD + "photos.getAll?extended=0";
        String PHOTOS_OWNER_ID = "&owner_id=";
        String PHOTOS_OFFSET = "&offset=";
        String PHOTOS_COUNT = "&count=";
    }

    interface ImageLoader {
        int PERCENTAGE = 100;
    }

    interface SharedPreferences {
        String APP_SETTINGS = "AppSettings";
        String ACCESS_TOKEN_SHARED_KEY = "accessTokenSharedKey";
        String NEWS_START_FROM_SHARED_KEY = "newsStartFromSharedKey";
    }

    interface RecyclerView {
        String LINEAR_LAYOUT_MANAGER_STATE_KEY = "LINEAR_LAYOUT_MANAGER_STATE_KEY";
        String IS_LOAD_COMPLETE_STATE_KEY = "IS_LOAD_COMPLETE_STATE_KEY";
        int VIEW_MARGIN = 16;
    }

    interface UserActivity {
        String FRIENDS_COUNT_FORMAT = "%s друзей • %s общих";
        String FRIENDS_COMMON_COUNT_FORMAT = "%s общих друзей";
        String EDUCATION_FORMAT = "%s\n%s";
        String FOLLOWERS_FORMAT = "%s подписчиков";
        String ONLINE_FORMAT = "online - %s";
        String STATE_ONLINE = "сейчас online";
        String COUNT_PHOTO_FORMAT = "Фото %s";
    }
}