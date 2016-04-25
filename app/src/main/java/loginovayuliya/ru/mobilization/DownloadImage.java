package loginovayuliya.ru.mobilization;

import android.app.IntentService;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.provider.BaseColumns;
import android.util.Log;

import java.io.BufferedInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

/**
 * Created by Iulkolog on 25.04.2016.
 */
public class DownloadImage extends IntentService{
    public static final String TAG = "DownloadImage";


    public DownloadImage(){
        super(DownloadImage.class.getName());
        setIntentRedelivery(true);
    }

    public void onHandleIntent(Intent intent){
        HttpURLConnection httpConnection = null;
        Bitmap bm = null;
        try {

            //пытаемся сделать url из строки
            URL url = new URL(intent.getStringExtra("url"));
            String id = intent.getStringExtra("id");
            Log.i(TAG, "URL: " + url.toString());

            //соединение
            httpConnection = (HttpURLConnection) url.openConnection();
            httpConnection.connect();

            int responseCode = httpConnection.getResponseCode();
            Log.i(TAG, "ResponseCode: " + Integer.toString(responseCode));

            //если ответ 200, то продолжаем
            if (responseCode == HttpsURLConnection.HTTP_OK) {

                //качаем файл
                BufferedInputStream in = new BufferedInputStream(httpConnection.getInputStream());
                bm = BitmapFactory.decodeStream(in);

                in.close();

                Log.i(TAG, "filename " + Integer.toString(url.getFile().hashCode()));

                FileOutputStream fos = openFileOutput(Integer.toString(url.getFile().hashCode()), Context.MODE_PRIVATE);


                bm.compress(Bitmap.CompressFormat.PNG, 100, fos);

                fos.close();

                ContentResolver myContentResolver = getContentResolver();
                ContentValues values = new ContentValues();
                values.put(DatabaseHelper.KEY_COLUMN_COVER_SMALL, Integer.toString(url.getFile().hashCode()));

                if (myContentResolver.update(ArtistsInfoProvider.CONTENT_URI, values, BaseColumns._ID + "=" + id, null) == 0){
                    values.put(BaseColumns._ID, id);
                    myContentResolver.insert(ArtistsInfoProvider.CONTENT_URI, values);
                }


            }
        }
        catch (MalformedURLException e) {
            Log.e(TAG, "Malformed URL Exception", e);
        } catch (IOException e) {
            Log.e(TAG, "IO Exception", e);
        } catch (NullPointerException e) {
            Log.e(TAG, "NullPointerException Exception", e);
        }
        finally {
            if (httpConnection != null)
                httpConnection.disconnect();
        }
    }


}