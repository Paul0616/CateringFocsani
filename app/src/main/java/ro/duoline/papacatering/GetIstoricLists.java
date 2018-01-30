package ro.duoline.papacatering;

import android.net.Uri;
import android.os.AsyncTask;

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
import java.util.ArrayList;

/**
 * Created by Paul on 28.05.2017.
 */

public class GetIstoricLists extends AsyncTask<String, Void, JSONArray> {

    private final String FILE = "getIstoric1.php";
    private final static String ISTORIC_QUERY = "dateconectare";//"restaurant";
    InputStream is=null;
    StringBuilder sb=null;


    public GetIstoricLists(GetIstoricLists.Listener listener){
        mListener = listener;
    }

    public interface Listener{
        void updateLists(JSONArray jarray);
        void onError();
    }

    private GetIstoricLists.Listener mListener;

    protected JSONArray doInBackground(String... args) {

        String result = "";
        JSONArray jArray = null;
        ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
        //CONNECT TO DATABASE
        try{

            HttpClient httpclient = new DefaultHttpClient();
            Uri bultUri = Uri.parse(args[2]);
            bultUri = Uri.withAppendedPath(bultUri, FILE);
            bultUri = bultUri.buildUpon().appendQueryParameter(ISTORIC_QUERY, args[0]).build();
            if(!args[1].isEmpty()){
                bultUri = bultUri.buildUpon().appendQueryParameter("persoana", args[1]).build();
            }
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
            result=sb.toString();

        }catch(Exception e){
            e.printStackTrace();
        }

        //CONVERT JSON TO STRING

        try{

            jArray = new JSONArray(result);


        }catch(JSONException e){
            e.printStackTrace();
        }


        return jArray; //I also need to return the list2 here

    }



    protected void onPostExecute(JSONArray jarray){

        if (jarray != null){
            mListener.updateLists(jarray);
        } else {
            mListener.onError();
        }

    }
}
