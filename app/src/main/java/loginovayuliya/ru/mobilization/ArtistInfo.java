package loginovayuliya.ru.mobilization;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.provider.BaseColumns;

import java.util.ArrayList;

/**
 * Created by Iulkolog on 08.04.2016.
 */
public class ArtistInfo {
    public static final String TAG = "ArtistInfo";
    private  String id;
    private  String name;
    private  ArrayList<String> genres;
    private  String  tracks;
    private  String  albums;
    private  String  link;
    private  String  description;
    private  String link_cover_big;
    private  String link_cover_small;

    public ArtistInfo(String _id, String _name, ArrayList<String>  _genres,
                      String  _tracks, String  _albums,
                      String  _link, String  _description,
                      String _link_cover_small,
                      String _link_cover_big){

        id = _id;
        name = _name;
        genres = _genres;
        tracks = _tracks;
        albums = _albums;
        link = _link;
        description = _description;
        link_cover_small = _link_cover_small;
        link_cover_big = _link_cover_big;

    }

    public void putIntoDatabase(ContentResolver myContentResolver){
        //Log.i(TAG, "puIntoDB");
        ContentValues values = new ContentValues();


        values.put(DatabaseHelper.KEY_COLUMN_NAME, name);
        values.put(DatabaseHelper.KEY_COLUMN_ALBUMS_NUMBER, albums);
        values.put(DatabaseHelper.KEY_COLUMN_TRACKS_NUMBER, tracks);
        //TODO:genres сделать в бд как отдельную таблицу
        values.put(DatabaseHelper.KEY_COLUMN_GENRES, genres.toString());
        values.put(DatabaseHelper.KEY_COLUMN_DESCRIPTION, description);
        values.put(DatabaseHelper.KEY_COLUMN_COVER_BIG, link_cover_big);
        values.put(DatabaseHelper.KEY_COLUMN_COVER_SMALL, link_cover_small);
        values.put(DatabaseHelper.KEY_COLUMN_LINK, link);
        //if (myContentResolver.update(ArtistsInfoProvider.CONTENT_URI, values, BaseColumns._ID + "=" + id, null) == 0) {
            values.put(BaseColumns._ID, id);
            myContentResolver.insert(ArtistsInfoProvider.CONTENT_URI, values);
        //}
    }

    public String getId(){ return id;}
    public String getName(){ return name;}
    public ArrayList<String> getGenres(){ return genres;}
    public String getTracksNumber(){ return tracks;}
    public String getAlbumsNumber(){ return albums;}
    public String getLink(){ return link;}
    public String getDescription(){ return description;}
    public String getSmallCoverLink(){ return link_cover_small;}
    public String getBigCoverLink(){ return link_cover_big;}

}
