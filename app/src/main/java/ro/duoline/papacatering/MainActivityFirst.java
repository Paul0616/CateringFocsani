package ro.duoline.papacatering;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import java.util.Calendar;
import android.net.Uri;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.firebase.messaging.FirebaseMessaging;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Scanner;

import ro.duoline.papacatering.data.RestauranteContract;
import ro.duoline.papacatering.data.RestauranteTasks;

public class MainActivityFirst extends AppCompatActivity implements LoaderManager.LoaderCallbacks<String>{

    private static final int RESTAURANT_LOADER_ID = 35;
    private final static String RESTAURANTE_URL_BASE = "https://www.duoline.ro/catering";
    private final static String RESTAURANTE_FILE_PHP_QUERY = "getRestaurante.php";
    private final static String HEADER_RECYCLER_VIEW = "Unde poti\nComanda\nMancare";
    private ImageView pozaFundal;
    private RecyclerView headerRecyclerView;
    private ProgressBar mLoadingIndicator;
    public Boolean ordonareRestauranteDupaScor = true;
    private HeaderRecyclerViewAdapter customAdapter;
    private List<ContentValues> listaRestauranteDePeServer;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_first);
        mLoadingIndicator = (ProgressBar) findViewById(R.id.pb_loading_indicator);
        Drawable drw = getResources().getDrawable(R.drawable.action_bar);
        getSupportActionBar().setBackgroundDrawable(drw);
        ActionBar mActionBar = getSupportActionBar();
        mActionBar.setDisplayShowTitleEnabled(false);
        headerRecyclerView = (RecyclerView) findViewById(R.id.add_header);
        final LinearLayoutManager linearLayoutManager = new LinearLayoutManager(MainActivityFirst.this);
        headerRecyclerView.setLayoutManager(linearLayoutManager);
        headerRecyclerView.setHasFixedSize(true);
        pozaFundal = (ImageView) findViewById(R.id.imageView2);

        //startAlarmManager();
        FirebaseMessaging.getInstance().subscribeToTopic("news");
    }

    @Override
    protected void onResume() {
        super.onResume();

        loadListaRestaurante();

    }
    public void startAlarmManager(){
        //----------------Configurez servicul AlarmManager ------------------
        AlarmManager manager = (AlarmManager) this.getSystemService(Context.ALARM_SERVICE);
        Intent myIntent;
        PendingIntent pendingIntent;
        myIntent = new Intent(MainActivityFirst.this, MesajeNotificationReceiver.class);
        pendingIntent = PendingIntent.getBroadcast(this, 123456789, myIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.HOUR, 11);
        calendar.set(Calendar.AM_PM, Calendar.AM);
        //calendar.add(Calendar.DAY_OF_MONTH, 1);
        manager.setInexactRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(),6000, pendingIntent);
        //===================================================================
    }

    public void afisareMesajAdaugareRestaurant(List<ContentValues> lista){
        String msg = "Acum poti comanda si de la ";
        for(int i =0;i<lista.size();i++){
            if(lista.get(i).getAsInteger("nou") == 1){
                msg += "\n" + lista.get(i).get(RestauranteContract.RestauranteEntry.COLUMN_REATAURANT_NAME).toString() + ";";
            }
        }
        if(!msg.equals("Acum poti comanda si de la ")) {
            Toast toast = Toast.makeText(MainActivityFirst.this, msg, Toast.LENGTH_LONG);
            toast.setGravity(Gravity.TOP,0,0);
            toast.show();
        }
    }


    public static String getResponseFromHttpUrl(URL url) throws IOException {
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        try {
            InputStream in = urlConnection.getInputStream();

            Scanner scanner = new Scanner(in);
            scanner.useDelimiter("\\A");

            boolean hasInput = scanner.hasNext();
            if (hasInput) {
                return scanner.next();
            } else {
                return null;
            }
        }  finally {
            urlConnection.disconnect();
        }
    }

    public void loadListaRestaurante(){
        makeURLConnection(makeURL(RESTAURANTE_URL_BASE, RESTAURANTE_FILE_PHP_QUERY), RESTAURANT_LOADER_ID);

    }

    private void makeURLConnection(URL queryURL, int loaderID){
        Bundle queryBundle = new Bundle();
        queryBundle.putString("link",queryURL.toString());
        LoaderManager loaderManager = getSupportLoaderManager();
        Loader<String> queryLoader = loaderManager.getLoader(loaderID);
        if(queryLoader == null){
            loaderManager.initLoader(loaderID, queryBundle, this);
        } else {
            loaderManager.restartLoader(loaderID, queryBundle, this);
        }
    }

    private URL makeURL(String base, String file){
        Uri bultUri = Uri.parse(base);
        bultUri = Uri.withAppendedPath(bultUri, file);
        URL queryURL = null;
        try {
            queryURL = new URL(bultUri.toString());
            return queryURL;
        } catch (MalformedURLException e){
            e.printStackTrace();
            return null;
        }
    }

    //jArray este vectorul care trebuie sortat iar jarrayScor este un vector de aceiasi lungime ca jArray ce contine key "id_restaurant" si "scor"
    public JSONArray getJSONSorted(JSONArray jArray, JSONArray jarrayScor){
        JSONArray temp = new JSONArray();
        try {
            temp = jArray;
            for (int i = 0; i < jArray.length(); i++) {
                temp.getJSONObject(i).put("scor", 0);
            }
            for (int i = 0; i < jArray.length(); i++) {
                for (int j = 0; j < jarrayScor.length(); j++) {
                    if (temp.getJSONObject(i).get("id").toString().equals(jarrayScor.getJSONObject(j).get("id_restaurant").toString())) {
                        temp.getJSONObject(i).put("scor", Integer.parseInt(jarrayScor.getJSONObject(j).get("scor").toString()));
                    }
                }
            }
        } catch(JSONException e) {
            e.printStackTrace();
        }
        JSONArray sortedJsonArray = new JSONArray();
        List<JSONObject> jsonValues = new ArrayList<JSONObject>();
        for (int i = 0; i < temp.length(); i++) {
            try {
                jsonValues.add(temp.getJSONObject(i));
            } catch(JSONException e){
                e.printStackTrace();
            }
        }
        Collections.sort( jsonValues, new Comparator<JSONObject>() {
            //You can change "Name" with "ID" if you want to sort by ID
            private static final String KEY_NAME = "scor";
            @Override
            public int compare(JSONObject a, JSONObject b) {
                Integer valA = 0;
                Integer valB = 0;
                try {
                    valA = (Integer) a.get(KEY_NAME);
                    valB = (Integer) b.get(KEY_NAME);
                }
                catch (JSONException e) {
                    //do something
                }
                return -valA.compareTo(valB);
                //if you want to change the sort order, simply use the following:
                //return -valA.compareTo(valB);
            }
        });

        for (int i = 0; i < temp.length(); i++) {
            sortedJsonArray.put(jsonValues.get(i));
        }
        return sortedJsonArray;
    }

    private List<HeaderRecyclerViewItems> getDataSource(JSONArray jArray){
        List<HeaderRecyclerViewItems> data = new ArrayList<HeaderRecyclerViewItems>();
        if (jArray == null || jArray.length() == 0){
            return data;
        } else {
            data.add(new HeaderRecyclerViewItems(null, HEADER_RECYCLER_VIEW));
            try{
                for(int i = 0; i < jArray.length(); i++){

                    data.add(new HeaderRecyclerViewItems(jArray.getJSONObject(i), HEADER_RECYCLER_VIEW));
                }
            } catch (JSONException e){
                e.printStackTrace();
            }
            return data;
        }
    }

    @Override
    public Loader<String> onCreateLoader(int id, final Bundle args) {
        return new AsyncTaskLoader<String>(this) {
            @Override
            protected void onStartLoading() {
                super.onStartLoading();
                if(args == null){
                    return;
                }
                mLoadingIndicator.setVisibility(View.VISIBLE);
                forceLoad();
            }

            @Override
            public String loadInBackground() {
                String queryURLString = args.getString("link");
                if(queryURLString == null || queryURLString == "") return null;
                try{
                    URL queryURL = new URL(queryURLString);
                    String result = getResponseFromHttpUrl(queryURL);

                    return result;
                } catch (IOException e){

                    e.printStackTrace();
                    return null;
                }
            }

        };
    }

    @Override
    public void onLoadFinished(Loader<String> loader, String data) {
        mLoadingIndicator.setVisibility(View.INVISIBLE);
        try {
            if (loader.getId() == RESTAURANT_LOADER_ID) {
                JSONArray jArray = null;
                if (data != null) {
                    jArray = new JSONArray(data);
                    List<ContentValues> list = new ArrayList<ContentValues>();

                    for (int i = 0; i < jArray.length(); i++) {
                        ContentValues cv = new ContentValues();
                        cv.put(RestauranteContract.RestauranteEntry.COLUMN_REATAURANT_ID, jArray.getJSONObject(i).getInt("id"));
                        cv.put(RestauranteContract.RestauranteEntry.COLUMN_REATAURANT_NAME, jArray.getJSONObject(i).getString("denumire"));
                        cv.put(RestauranteContract.RestauranteEntry.COLUMN_VALOARE_MINIMA_LIVRARE, jArray.getJSONObject(i).getInt("valoare_minima"));
                        cv.put(RestauranteContract.RestauranteEntry.COLUMN_MESAJ_CATERING, jArray.getJSONObject(i).getString("mesaj_catering"));
                        cv.put(RestauranteContract.RestauranteEntry.COLUMN_MESAJ_REZERVARE, jArray.getJSONObject(i).getString("mesaj_rezervare"));
                        cv.put(RestauranteContract.RestauranteEntry.COLUMN_LIVRARE, jArray.getJSONObject(i).getString("locatii_livrare"));
                        cv.put(RestauranteContract.RestauranteEntry.COLUMN_DATABASE, jArray.getJSONObject(i).getString("dbname_ip"));
                        cv.put(RestauranteContract.RestauranteEntry.COLUMN_USER, jArray.getJSONObject(i).getString("dbname"));
                        cv.put(RestauranteContract.RestauranteEntry.COLUMN_PASS, jArray.getJSONObject(i).getString("passw"));
                        cv.put(RestauranteContract.RestauranteEntry.COLUMN_IP, jArray.getJSONObject(i).getString("ip"));
                        list.add(cv);
                    }

                    listaRestauranteDePeServer = RestauranteTasks.syncRestauranteDB(this, list);


                }

                 if (ordonareRestauranteDupaScor) {
                     JSONArray jarrayScor = RestauranteTasks.getVectorScor(jArray);
                     jArray = getJSONSorted(jArray, jarrayScor);
                 }
                 if (jArray != null) {
                    customAdapter = new HeaderRecyclerViewAdapter(getApplicationContext(), getDataSource(jArray));

                    headerRecyclerView.setAdapter(customAdapter);
                    afisareMesajAdaugareRestaurant(listaRestauranteDePeServer);
                } else
                    Toast.makeText(getApplicationContext(), "Eroare de conectare la server!!!", Toast.LENGTH_LONG).show();
            }


        } catch (JSONException e) {
            e.printStackTrace();

        }
    }

    @Override
    public void onLoaderReset(Loader<String> loader) {

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_scrolling,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int menuItemThatWasSeleted = item.getItemId();
        if(menuItemThatWasSeleted == R.id.action_profile){
            Intent intent = new Intent(MainActivityFirst.this,AfisareProfile_Activity.class);
            startActivity(intent);
        }
        if(menuItemThatWasSeleted == R.id.action_about){

            /*TODO: Afisare About
            Intent intent = new Intent(MainActivityFirst.this, AfisareAdrese_Activity.class);
            startActivity(intent);
            */
        }
        return super.onOptionsItemSelected(item);
    }



}
