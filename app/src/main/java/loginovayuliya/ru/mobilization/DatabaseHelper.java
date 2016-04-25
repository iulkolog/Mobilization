package loginovayuliya.ru.mobilization;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.provider.BaseColumns;
import android.util.Log;

public class DatabaseHelper {

    private static final String TAG = "DBHelper";

    private static final String DATABASE_NAME = "Music.db";
    private static final String TABLE_NAME = "ArtistsInfo";
    private static final int DATABASE_VERSION = BuildConfig.DATABASE_VERSION;

    private DBOpenHelper myDBOpenHelper;

    public DatabaseHelper(final Context context) {
        Log.i(TAG, "constructor");
        myDBOpenHelper = new DBOpenHelper(context);
    }

    public Cursor query(String[] columns, String selection, String[] selectionArgs, String sortOrder) {
        Log.d(TAG, "query");
        SQLiteDatabase database = myDBOpenHelper.getWritableDatabase();

        SQLiteQueryBuilder builder = new SQLiteQueryBuilder();
        builder.setTables(TABLE_NAME);

        Cursor cursor = builder.query(database,
                columns, selection, selectionArgs,
                null, null, null);

        return cursor;
    }

    public int update(Uri uri, ContentValues contentValues){
        Log.i(TAG, "update");
        SQLiteDatabase database = myDBOpenHelper.getWritableDatabase();
        int count = database.update(TABLE_NAME, contentValues, null, null);
        return count;
    }

    public long insert(Uri uri, ContentValues contentValues){
        Log.i(TAG, "insert");
        SQLiteDatabase database = myDBOpenHelper.getWritableDatabase();
        long count = database.insert(TABLE_NAME, null, contentValues);
        return count;
    }



    public static final String KEY_COLUMN_NAME = "name";
    public static final String KEY_COLUMN_DESCRIPTION = "description";
    public static final String KEY_COLUMN_ALBUMS_NUMBER = "albums";
    public static final String KEY_COLUMN_TRACKS_NUMBER = "tracks";
    public static final String KEY_COLUMN_GENRES = "genres";
    public static final String KEY_COLUMN_LINK = "link";
    public static final String KEY_COLUMN_COVER_SMALL = "smallcover";
    public static final String KEY_COLUMN_COVER_BIG = "bigcover";
    public static final String KEY_COLUMN_COVER_BIG_LOADED = "bigcoverloaded";
    public static final String KEY_COLUMN_COVER_SMALL_LOADED = "smallcoverloaded";

    //строка для создания таблицы
    private static final String TABLE_CREATE = "CREATE TABLE " + TABLE_NAME +
            " (" + BaseColumns._ID + " INTEGER PRIMARY KEY NOT NULL, " +
            KEY_COLUMN_NAME + " TEXT UNIQUE NOT NULL, " +
            KEY_COLUMN_GENRES + " TEXT," +
            KEY_COLUMN_TRACKS_NUMBER + " INT, " +
            KEY_COLUMN_ALBUMS_NUMBER + " INT, " +
            KEY_COLUMN_DESCRIPTION + " TEXT, " +
            KEY_COLUMN_LINK + " TEXT, " +
            KEY_COLUMN_COVER_SMALL + " TEXT, " +
            KEY_COLUMN_COVER_BIG + " TEXT, " +
            KEY_COLUMN_COVER_SMALL_LOADED + " TEXT, " +
            KEY_COLUMN_COVER_BIG_LOADED + " TEXT " +
            ");";



    private static class DBOpenHelper extends SQLiteOpenHelper {

        private static final String TAG = "DBOpenHelper";

        public DBOpenHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }


        //если БД не создана, создаём
        @Override
        public void onCreate(SQLiteDatabase db) {
            Log.i(TAG, "onCreate " + DATABASE_NAME);
            db.execSQL(TABLE_CREATE);


        }

        //если у БД не совпадают версии, то обновляем БД
        @Override
        public void onUpgrade(SQLiteDatabase _db, int _oldVersion, int _newVersion) {
            Log.i(TAG, "onUpgrade");
            Log.w(TAG, "Upgrading from version " +
                    _oldVersion + " to " +
                    _newVersion + ", which will destroy all old data");

            _db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
            onCreate(_db);
        }

    }
}

