package loginovayuliya.ru.mobilization;

import android.app.IntentService;
import android.content.ContentResolver;
import android.content.Intent;
import android.util.Log;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;


/**
 * Created by Iulkolog on 17.08.2015.
 */
public class DownloadTask extends IntentService {

    public static final String TAG = "DownloadIntent";


    public DownloadTask(){
        super(DownloadTask.class.getName());
        setIntentRedelivery(true);
        //по умолчанию восстанавливается, только если есть ожидающие запросы на запуск
        //с этим флагом будет восстанавливаться с передачей последнего доставленного намерения
    }

    public void onHandleIntent(Intent intent){
        HttpURLConnection httpConnection = null;

        try {

            //пытаемся сделать url из строки
            URL url = new URL(intent.getStringExtra("url"));
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

                //превращаем в строку
                byte[] data = new byte[1024];
                int count;
                String inputLine = "";

                while( (count = in.read(data)) != -1){
                    inputLine += new String(data, 0, count);
                }
                in.close();

                ContentResolver myContentResolver = getContentResolver();
                //парсим документ
                JSONParser jParser = new JSONParser(inputLine, myContentResolver, getBaseContext());

                ArtistInfo[] artistsInfos = jParser.getArrayList();

                int arraySize = artistsInfos.length;
                Log.i(TAG, "addDatatoBD arrSize=" + arraySize);
                /*for (int i=0; i < arraySize; i++){

                    //artistsInfos[i].putIntoDatabase(myContentResolver);
                    Intent intent2 = new Intent(getBaseContext(), DownloadImage.class);
                    intent2.putExtra("url", artistsInfos[i].getSmallCoverLink());
                    intent2.putExtra("id", artistsInfos[i].getId());
                    getBaseContext().startService(intent2);
                }*/
                Log.i(TAG, "addDatatoBD completed");


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
