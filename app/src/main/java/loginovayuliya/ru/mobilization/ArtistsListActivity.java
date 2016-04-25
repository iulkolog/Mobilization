package loginovayuliya.ru.mobilization;

import android.app.FragmentManager;
import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import java.util.ArrayList;

public class ArtistsListActivity extends AppCompatActivity {

    private static final String TAG ="MainActivity";
    private static DownloadTask downloadTask;

    Handler handler = new Handler();

    private ArrayList<ArtistInfo> artistsInfoItems;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i("MainActivity", "OnCreate");
        setContentView(R.layout.activity_artists_list);
        Log.i("MainActivity", "setContentView");

        Thread t = new Thread(new Runnable() {
            public void run() {
                refreshCategoryList();
            }
        });
        t.start();
        Log.i(TAG, "done");
    }

    public void refreshCategoryList(){
        Log.i(TAG, "refreshCategoryList");

        String artistsList = getString(R.string.url_artists_list);
        Log.i(TAG, "getstring");
        Intent intent = new Intent(this, DownloadTask.class);
        Log.i(TAG, "getstring");
        intent.putExtra("url", artistsList);
        this.startService(intent);
        Log.i(TAG, "startserv");
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.i(TAG, "onActivityResult");

        FragmentManager fm = getFragmentManager();
        ArtistsInfoListFragment categoryList = (ArtistsInfoListFragment) fm.findFragmentById(R.id.CategoryListFragment);
        categoryList.refreshCategoryList();
    }
}
