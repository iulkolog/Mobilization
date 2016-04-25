package loginovayuliya.ru.mobilization;

import android.content.ContentResolver;
import android.content.Context;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Iulkolog on 20.08.2015.
 */
public class JSONParser {
    // JSON TAGS
    private static final String TAG = "JSONParser";
    private static final String TAG_ID = "id";
    private static final String TAG_ARTISTNAME = "name";
    private static final String TAG_GENRES = "genres";
    private static final String TAG_TRACKS = "tracks";
    private static final String TAG_ALBUMS = "albums";
    private static final String TAG_LINK = "link";
    private static final String TAG_DESCRIPTION = "description";
    private static final String TAG_COVER = "cover";
    private static final String TAG_COVER_SMALL = "small";
    private static final String TAG_COVER_BIG = "big";

    private JSONObject titles = null;
    private ArrayList<ArtistInfo> artistsInfoArray;

    private ArtistInfo[] artistsInfo;
    private ContentResolver myContentResolver;

    private Context context;

    JSONParser(String jsonString, ContentResolver _myContentResolver, Context _context){

        myContentResolver = _myContentResolver;

        context = _context;

        artistsInfoArray = new ArrayList<>();

        try {

            JSONArray jsonArray = new JSONArray(jsonString);

            Log.i(TAG, "JSON: " + jsonArray.toString());

            int level = 0;
            String path = "";
            parseJSONArray(jsonArray);

        }
        catch (JSONException e) {
            Log.e(TAG, "JSON Exception", e);
        }

    }

    private void parseJSONArray(JSONArray jsonArray) throws JSONException {
        String path;
        for (int i = 0; i < jsonArray.length(); i++) {

            JSONObject jObj = jsonArray.optJSONObject(i);

            String id = jObj.optString(TAG_ID);
            String name = jObj.optString(TAG_ARTISTNAME);
            JSONArray jObjGenres = jObj.getJSONArray(TAG_GENRES);
            ArrayList<String> genres= new ArrayList<>();
            for (int j = 0; j < jObjGenres.length(); j++) {
                genres.add(jObjGenres.getString(j));

            }

            String tracks = jObj.optString(TAG_TRACKS);
            String  albums = jObj.optString(TAG_ALBUMS);
            String  link = jObj.optString(TAG_LINK);
            String  description = jObj.optString(TAG_DESCRIPTION);

            JSONObject jObjCovers = jObj.getJSONObject(TAG_COVER);
            String link_cover_small = jObjCovers.optString(TAG_COVER_SMALL);
            String link_cover_big = jObjCovers.optString(TAG_COVER_BIG);

            new ArtistInfo(id, name, genres, tracks, albums, link, description,
                    link_cover_small, link_cover_big).putIntoDatabase(myContentResolver);
            artistsInfoArray.add(new ArtistInfo(id, name, genres, tracks, albums, link, description,
                                              link_cover_small, link_cover_big));



        }

    }

    public ArtistInfo[] getArrayList (){
        artistsInfo = new ArtistInfo[artistsInfoArray.size()];
        return artistsInfoArray.toArray(artistsInfo);
    }




}
