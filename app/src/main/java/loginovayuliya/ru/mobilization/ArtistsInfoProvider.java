package loginovayuliya.ru.mobilization;

import android.content.ContentProvider;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.net.Uri;
import android.provider.BaseColumns;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;

/**
 * Created by Iulkolog on 11.04.2016.
 */
public class ArtistsInfoProvider extends ContentProvider {

    public static final String TAG = ArtistsInfoProvider.class.getName();

    public static final String AUTHORITY = "loginovayuliya.ru.mobilization.ArtistsInfoProvider";
    public static final String SCHEME = "content";
    public static final String PATH_INFOITEMS = "infoitems";
    public static final Uri CONTENT_URI = Uri.parse(SCHEME + "://" + AUTHORITY + "/" + PATH_INFOITEMS);

    /*@NonNull
    public static Uri createContentUri(final String id) {
        return Uri.parse(SCHEME + "://" + AUTHORITY + "/" + PATH_INFOITEMS +" /" + id);
    }*/

    public static final int ALLINFOITEMS = 1; //возвращает все карточки
    public static final int INFOITEM_ID = 2; //возвращает запись по id

    private static final UriMatcher mUriMatcher;
    static {
        mUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        mUriMatcher.addURI(AUTHORITY, PATH_INFOITEMS, ALLINFOITEMS);
        mUriMatcher.addURI(AUTHORITY, PATH_INFOITEMS + "/#",INFOITEM_ID );
    }

    //MIME-типы
    public static final String ALLINFOITEMS_MIME_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE +
            AUTHORITY + "." + PATH_INFOITEMS;
    public static final String INFOITEM_MIME_TYPE = ContentResolver.CURSOR_ITEM_BASE_TYPE +
            AUTHORITY + "." + PATH_INFOITEMS;

    private DatabaseHelper myDBHelper;


    @Override
    public boolean onCreate() {
        Log.d(TAG, "OnCreate");
        myDBHelper = new DatabaseHelper(getContext());
        return true;
    }


    @Nullable
    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        switch (mUriMatcher.match(uri)) {
            case ALLINFOITEMS:
                Log.d(TAG, "URI_ALLINFOITEMS");
                break;
            case INFOITEM_ID: // Uri с ID
                String id = uri.getLastPathSegment();
                Log.d(TAG, "URI_INFOITEM_ID, " + id);
                // добавляем ID к условию выборки
                if (TextUtils.isEmpty(selection)) {
                    selection = BaseColumns._ID + " = " + id;
                } else {
                    selection = selection + " AND " + BaseColumns._ID + " = " + id;
                }
                break;
            default:
                throw new IllegalArgumentException("Wrong URI: " + uri);
        }

        Cursor cursor = myDBHelper.query(projection, selection, selectionArgs, sortOrder);
        cursor.setNotificationUri(getContext().getContentResolver(), CONTENT_URI );
        return cursor;

    }


    @Nullable
    @Override
    public String getType(Uri uri) {
        Log.i(TAG, "getType");

        switch (mUriMatcher.match(uri)) {
            case ALLINFOITEMS:
                return ALLINFOITEMS_MIME_TYPE;

            case INFOITEM_ID:
                return INFOITEM_MIME_TYPE;

            default:
                throw new IllegalArgumentException("Unknown URL " + uri);
        }
    }


    @Nullable
    @Override
    public Uri insert(Uri uri, ContentValues values) {
        Log.i(TAG, "insert");
        if (mUriMatcher.match(uri) != ALLINFOITEMS) {
            throw new IllegalArgumentException("Wrong URI: " + uri);
        }
        long count = myDBHelper.insert(uri,values);
        Uri resultUri = ContentUris.withAppendedId(CONTENT_URI, count);
        getContext().getContentResolver().notifyChange(uri, null);
        return resultUri;
    }

//TODO: прописать код delete
    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        Log.d(TAG, "delete");
        return 0;
    }


    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        Log.i(TAG, "update");
        switch (mUriMatcher.match(uri)) {
            case ALLINFOITEMS:
                Log.d(TAG, "URI_ALLINFOITEMS");
                break;
            case INFOITEM_ID:
                String id = uri.getLastPathSegment();
                Log.d(TAG, "URI_INFO_ID, " + id);
                if (TextUtils.isEmpty(selection)) {
                    selection = BaseColumns._ID + " = " + id;
                } else {
                    selection = selection + " AND " + BaseColumns._ID + " = " + id;
                }
                break;
            default:
                throw new IllegalArgumentException("Wrong URI: " + uri);
        }
        int count = myDBHelper.update(uri, values);
        Uri resultUri = ContentUris.withAppendedId(CONTENT_URI, count);
        getContext().getContentResolver().notifyChange(uri, null);

        return count;
    }


}
