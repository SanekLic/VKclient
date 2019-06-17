package com.my.vkclient;

import android.support.annotation.AnimRes;

public interface Constants {
    String STRING_EQUALS = "=";
    String STRING_AND = "&";
    String STRING_COMMA = ",";
    String STRING_NUMBER_FORMAT = "%.1f%c";
    String STRING_NUMBER_POSTFIX = "KMGTPE";
    String STRING_EMPTY = "";
    String STRING_SPACE = " ";
    String NAME_FORMAT = "%s %s";
    String AUDIO_FORMAT = "%s\n%s";
    String SEARCH_IN_GOOGLE_FORMAT = "https://www.google.com/search?q=%s %s";
    String DATE_FORMAT = "dd MMMM HH:mm";
    int INT_ZERO = 0;
    int INT_THOUSAND = 1000;
    int INT_ONE_KB = 1024;
    int TIME_TO_RE_REQUEST = 500;

    interface IntentKey {
        String USER_ID_INTENT_KEY = "USER_ID_INTENT_KEY";
        String GROUP_ID_INTENT_KEY = "GROUP_ID_INTENT_KEY";
        String IMAGE_URL_INTENT_KEY = "IMAGE_URL_INTENT_KEY";
        String EDIT_STATUS_INTENT_KEY = "EDIT_STATUS_INTENT_KEY";
        String EDIT_CITY_INTENT_KEY = "EDIT_CITY_INTENT_KEY";
    }

    interface DialogFragment {
        String USER_INFO_DIALOG_FRAGMENT_TAG = "USER_INFO_DIALOG_FRAGMENT_TAG";
        String GROUP_INFO_DIALOG_FRAGMENT_TAG = "GROUP_INFO_DIALOG_FRAGMENT_TAG";
        String EDIT_USER_DIALOG_FRAGMENT_TAG = "EDIT_USER_DIALOG_FRAGMENT_TAG";
    }

    interface StateKey {
        String SCROLL_VIEW_POSITION_STATE_KEY = "SCROLL_VIEW_POSITION_STATE_KEY";
        String LINEAR_LAYOUT_MANAGER_STATE_KEY = "LINEAR_LAYOUT_MANAGER_STATE_KEY";
        String IS_LOAD_COMPLETE_STATE_KEY = "IS_LOAD_COMPLETE_STATE_KEY";
    }

    interface Database {
        String USER_TABLE_NAME = "USER_TABLE";
        String FRIEND_TABLE_NAME = "FRIEND_TABLE";
        String GROUP_TABLE_NAME = "GROUP_TABLE";
        String NEWS_TABLE_NAME = "NEWS_TABLE";
        String ATTACHMENT_TABLE_NAME = "ATTACHMENT_TABLE";
        String USER_PHOTO_TABLE_NAME = "USER_PHOTO_TABLE";
        String USER_GROUP_TABLE_NAME = "USER_GROUP_TABLE";

        String DATABASE_NAME = "VkDatabase.db";
        String PRIMARY_KEY = "PRIMARY KEY";
        String AUTOINCREMENT = "AUTOINCREMENT";
        int DATABASE_VERSION = 6;

        String SQL_TABLE_CREATE_TEMPLATE = "CREATE TABLE IF NOT EXISTS %s (%s);";
        String DROP_TABLE_IF_EXISTS = "DROP TABLE IF EXISTS %s;";
        String SELECT_FROM = "SELECT * FROM ";
        String DATABASE_WHERE = " WHERE %s %s %s";
        String DATABASE_WHERE_CLAUSE = "%s %s %s";
        String DATABASE_JOIN = " JOIN %s ON %s = %s ORDER BY %s, %s";
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
        String FIELDS = "&fields=";
        String USER_FIELDS = "online,photo_100,photo_max_orig,crop_photo,about,common_count,counters,education,followers_count,games,home_town,interests,last_seen,movies,music,status,verified,";
        String API_VK_GET_FRIENDS_URL = API_VK_METHOD + "friends.get?order=name";
        String FRIENDS_FIELDS = USER_FIELDS;
        String API_VK_GET_GROUP_URL = API_VK_METHOD + "groups.getById?group_id=";
        String LIKE_FIELDS = "?type=post&owner_id=%s&item_id=%s";
        String API_VK_SET_LIKE_POST = API_VK_METHOD + "likes.add" + LIKE_FIELDS + ACCESS_TOKEN;
        String API_VK_SET_DISLIKE_POST = API_VK_METHOD + "likes.delete" + LIKE_FIELDS + ACCESS_TOKEN;
        String API_VK_GET_PHOTOS_URL = API_VK_METHOD + "photos.getAll?extended=0";
        String PHOTOS_OWNER_ID = "&owner_id=";
        String PHOTOS_OFFSET = "&offset=";
        String PHOTOS_COUNT = "&count=";
        String API_VK_GET_GROUPS_URL = API_VK_METHOD + "groups.get?extended=1";
        String GROUPS_FIELDS = "activity,description,status,site,verified";
        String REQUEST_COUNT = "&count=";
        String REQUEST_OFFSET = "&offset=";
        String API_VK_GET_NEWS_URL = API_VK_METHOD + "newsfeed.get?filters=post";
        String NEWS_START_FROM = "&start_from=";
        String NEWS_FIELDS = USER_FIELDS + GROUPS_FIELDS;
        String NEWS_COUNT = "&count=";
        String API_VK_SAVE_PROFILE = API_VK_METHOD + "account.saveProfileInfo";
        String SAVE_STATUS = "?status=";
        String SAVE_HOME_TOWN = "?home_town=";
    }

    interface ImageLoader {
        int PERCENTAGE = 100;

        @AnimRes
        int DEFAULT_ANIMATION = R.anim.item_fade_in_anim;
    }

    interface SharedPreferences {
        String APP_SETTINGS = "APP_SETTINGS";
        String ACCESS_TOKEN_SHARED_KEY = "ACCESS_TOKEN";
        String NEWS_START_FROM_SHARED_KEY = "NEWS_START_FROM";
        String ANIMATION_ENABLE_SHARED_KEY = "ANIMATION_ENABLE";
        String PROFILE_ID_SHARED_KEY = "PROFILE_ID";
    }

    interface RecyclerView {
        int VIEW_MARGIN = 16;
    }

    interface UserActivity {
        String FRIENDS_COUNT_FORMAT = "%s друзей • %s общих";
        String FRIENDS_ONLY_COUNT_FORMAT = "%s друзей";
        String FRIENDS_COMMON_COUNT_FORMAT = "%s общих друзей";
        String EDUCATION_FORMAT = "%s\n%s";
        String FOLLOWERS_FORMAT = "%s подписчиков";
        String ONLINE_FORMAT = "online - %s";
        String STATE_ONLINE = "сейчас online";
        String COUNT_PHOTO_FORMAT = "Фото %s";
    }
}