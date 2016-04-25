package loginovayuliya.ru.mobilization;

import android.app.ListFragment;
import android.app.LoaderManager;
import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;
import android.provider.BaseColumns;
import android.util.Log;
import android.widget.CursorAdapter;
import android.widget.SimpleCursorAdapter;


public class ArtistsInfoListFragment extends ListFragment implements LoaderManager.LoaderCallbacks<Cursor>
 {

    CursorAdapter adapter;
    private static final String TAG = "ArtistsInfoListFragment";
    Handler handler = new Handler();



     @Override
    public void onActivityCreated (Bundle savedInstanceState){

        super.onActivityCreated(savedInstanceState);
        Log.i(TAG, "onActivityCreated");
       

        String from[] = new String[]{DatabaseHelper.KEY_COLUMN_NAME, DatabaseHelper.KEY_COLUMN_GENRES, DatabaseHelper.KEY_COLUMN_TRACKS_NUMBER, DatabaseHelper.KEY_COLUMN_COVER_SMALL_LOADED};
        int to[] = new int[] {R.id.labelItem, R.id.genresItem,R.id.albumsAndSongsItem, R.id.smallimg};

        setEmptyText("there are no downloaded items");

        adapter = new SimpleCursorAdapter(
                getActivity(), //данный контекст
                R.layout.item_artist_info, //Layout для одного ряда
                null, //курсор
                from,
                to,
                0); //flags


        setListAdapter(adapter);


        getLoaderManager().initLoader(ArtistsInfoProvider.ALLINFOITEMS, null, ArtistsInfoListFragment.this);

        Thread t = new Thread(new Runnable() {
            public void run() {
                refreshCategoryList();
            }
        });
        t.start();

    }

     @Override
     public Loader<Cursor> onCreateLoader(int loaderID, Bundle args) {
         Log.i(TAG, " onCreateLoader");
         String[] projection = new String[] {
                 BaseColumns._ID,
                 DatabaseHelper.KEY_COLUMN_NAME,
                 DatabaseHelper.KEY_COLUMN_GENRES,
                 DatabaseHelper.KEY_COLUMN_ALBUMS_NUMBER,
                 DatabaseHelper.KEY_COLUMN_TRACKS_NUMBER,
                 DatabaseHelper.KEY_COLUMN_COVER_SMALL_LOADED,
                 DatabaseHelper.KEY_COLUMN_COVER_BIG_LOADED
         };

         switch (loaderID){
             case ArtistsInfoProvider.ALLINFOITEMS:
                 return new CursorLoader(getActivity(),
                         ArtistsInfoProvider.CONTENT_URI,
                         projection,
                         null,
                         null,
                         null);

             default:
                 return null;

         }

     }

     @Override
     public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
         Log.i(TAG, "onLoadFinished");

         if (loader!=null && cursor!=null)
             adapter.swapCursor(cursor);
         else Log.i(TAG, "OnLoadFinished Else case");
     }

     @Override
     public void onLoaderReset(Loader<Cursor> loader) {
         Log.i(TAG, "onLoadReset");
         adapter.swapCursor(null);

     }

     public void refreshCategoryList(){
         Log.i(TAG, "refreshCategoryList");
         handler.post(new Runnable() {
             public void run() {
                 getLoaderManager().restartLoader(ArtistsInfoProvider.ALLINFOITEMS, null, ArtistsInfoListFragment.this);
             }
         });

     }


}

