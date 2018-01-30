package ro.duoline.cateringfocsani;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.icu.text.DateFormat;


import android.net.Uri;
import android.os.AsyncTask;
import android.support.v4.app.NotificationCompat;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


import ro.duoline.cateringfocsani.data.RestauranteContract;
import ro.duoline.cateringfocsani.data.RestauranteDbHelper;
import ro.duoline.cateringfocsani.data.RestauranteTasks;

/**
 * Created by Paul on 12.08.2017.
 */

public class MesajeNotificationReceiver extends BroadcastReceiver {



    @Override
    public void onReceive(Context context, Intent intent) {

        new GetMesajeNotificari(context).execute("http://www.ondesign.ro/getMesaje.php");


    }

    public void makeNotifications(Context context, String titlu, String mesaj, int idNotification, PendingIntent pendingIntent){




        NotificationCompat.Builder builder = new NotificationCompat.Builder(context);
        NotificationCompat.BigTextStyle notiStyle = new NotificationCompat.BigTextStyle();
        notiStyle.bigText(mesaj);
        builder.setAutoCancel(true)
                .setDefaults(Notification.DEFAULT_ALL)
                .setWhen(System.currentTimeMillis())
                .setSmallIcon(R.mipmap.careting_r)
                .setContentTitle("Catering Focsani: " + titlu)
                .setStyle(notiStyle)
                .setDefaults(Notification.DEFAULT_LIGHTS | Notification.DEFAULT_SOUND)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true);//.setContentText(mesaj)
        //;
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(idNotification, builder.build());
    }


    public class GetMesajeNotificari extends AsyncTask<String, Void, String> {

        InputStream is=null;
        StringBuilder sb=null;
        JSONArray jarray;
        private Context context;

        public GetMesajeNotificari(Context context){
            this.context = context;
        }

        @Override
        protected String doInBackground(String... params) {
            String result = "";
            ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
            //CONNECT TO DATABASE
            try{

                HttpClient httpclient = new DefaultHttpClient();
                Uri bultUri = Uri.parse(params[0]);
                HttpPost httppost = new HttpPost(bultUri.toString());
                httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
                HttpResponse response = httpclient.execute(httppost);
                HttpEntity entity = response.getEntity();
                is = entity.getContent();

            }catch(Exception e){
                e.printStackTrace();
            }

            //BUFFERED READER FOR INPUT STREAM
            try{
                BufferedReader reader = new BufferedReader(new InputStreamReader(is,"iso-8859-1"),8);
                sb = new StringBuilder();
                String line = "0";

                while ((line = reader.readLine()) != null) {
                    sb.append(line + "\n");
                }
                is.close();
                result = sb.toString();

            }catch(Exception e){
                e.printStackTrace();
            }

            //CONVERT JSON TO STRING
            try{

                jarray = new JSONArray(result);


            } catch(JSONException e){
                e.printStackTrace();
            }

            return result;
        }

        @Override
        protected void onPostExecute(String s) {

            List<ContentValues> list = new ArrayList<ContentValues>();
            SimpleDateFormat dateformat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
            try {
                for (int i = 0; i < jarray.length(); i++) {
                    ContentValues cv = new ContentValues();
                    cv.put(RestauranteContract.MesajeEntry.COLUMN_REATAURANT_ID, jarray.getJSONObject(i).getInt("restaurantID"));
                    cv.put(RestauranteContract.MesajeEntry.COLUMN_MESAJ, jarray.getJSONObject(i).getString("mesaj"));
                    Date date = new Date();
                    try {
                        date = dateformat.parse(jarray.getJSONObject(i).getString("data_mesaj"));
                    } catch (ParseException e){
                        e.printStackTrace();
                    }
                    cv.put(RestauranteContract.MesajeEntry.COLUMN_DATA_MESAJ, dateformat.format(date));
                    list.add(cv);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            if (RestauranteTasks.mDb == null) {
                RestauranteTasks.initDatabase(context);
            }
            RestauranteTasks.adaugaMesaje(list);
            List<ContentValues> list1 =  RestauranteTasks.getAllMesaje();
            Intent myIntent = new Intent(context, MainActivity.class);;
            PendingIntent pendingIntent;
            for(int i = 0 ; i < list1.size(); i++){
                ArrayList<String> al = new ArrayList<String>();
                al.add(list1.get(i).getAsString(RestauranteContract.RestauranteEntry.COLUMN_DATABASE));
                al.add(list1.get(i).getAsString(RestauranteContract.RestauranteEntry.COLUMN_USER));
                al.add(list1.get(i).getAsString(RestauranteContract.RestauranteEntry.COLUMN_PASS));
                al.add(list1.get(i).getAsString(RestauranteContract.RestauranteEntry.COLUMN_IP));
                if(myIntent.hasExtra("DENUMIRE")) {
                    myIntent.removeExtra("DENUMIRE");
                }
                if(myIntent.hasExtra("ID")) {
                    myIntent.removeExtra("ID");
                }
                if(myIntent.hasExtra("LIVRARE")) {
                    myIntent.removeExtra("LIVRARE");
                }
                if(myIntent.hasExtra("dateConectare")) {
                    myIntent.removeExtra("dateConectare");
                }
                myIntent.putExtra("DENUMIRE", list1.get(i).getAsString("denumire"));
                myIntent.putExtra("ID", list1.get(i).getAsInteger("id"));
                myIntent.putExtra("LIVRARE", list1.get(i).getAsString("livrare"));
                myIntent.putStringArrayListExtra("dateConectare", al);
                pendingIntent = PendingIntent.getActivity(context, list1.get(i).getAsInteger("id"), myIntent, PendingIntent.FLAG_UPDATE_CURRENT);
               makeNotifications(context, list1.get(i).getAsString("denumire"), list1.get(i).getAsString("mesaj"), list1.get(i).getAsInteger("id"), pendingIntent);
            }


        }
    }
}
