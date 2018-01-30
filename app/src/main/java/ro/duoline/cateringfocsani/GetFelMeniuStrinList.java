package ro.duoline.cateringfocsani;

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
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Paul on 28.05.2017.
 */

public class GetFelMeniuStrinList extends AsyncTask<String, Void, List<String>> {
    private final static String PRODUSE_FILE_PHP_QUERY = "getProduse.php";//"getProduseV2.php";
    private final static String RESTAURANT_QUERY = "dateconectare";//"restaurant";
    InputStream is=null;
    StringBuilder sb=null;
    private List<String> NUME_CATEGORIE = new ArrayList<String>();
    private  List<String> DENUMIRE_PRODUS = new ArrayList<String>();
    private  List<String> DESCRIERE_PRODUS = new ArrayList<String>();
    private  List<Float> PRET_BUCATA = new ArrayList<Float>();
    private  List<Integer> COD = new ArrayList<Integer>();
    private  List<String> UM = new ArrayList<String>();
    private  List<String> POZA = new ArrayList<String>();

    public GetFelMeniuStrinList(GetFelMeniuStrinList.Listener listener){
        mListener = listener;

    }

    public interface Listener{
        void createAdapter(List<String> NUME_CATEGORIE, List<String> DENUMIRE_PRODUS, List<String> DESCRIERE_PRODUS, List<Float> PRET_BUCATA,
                           List<Integer> COD, List<String> UM, List<String> POZA);
        void onError();
    }

    private GetFelMeniuStrinList.Listener mListener;


    protected List<String> doInBackground(String... args) {

        String result = "";

        ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
        //CONNECT TO DATABASE
        try{

            HttpClient httpclient = new DefaultHttpClient();
            Uri bultUri = Uri.parse(args[1]);
            bultUri = Uri.withAppendedPath(bultUri, PRODUSE_FILE_PHP_QUERY);
            bultUri = bultUri.buildUpon().appendQueryParameter(RESTAURANT_QUERY, args[0]).build();
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

            JSONArray jArray = new JSONArray(result);
            JSONObject json_data=null;

            for(int i=0;i<jArray.length();i++){

                json_data = jArray.getJSONObject(i);
                NUME_CATEGORIE.add(json_data.getString("nume_categorie"));
                if(json_data.getString("denumire_produs").contains("&#34;")){
                    DENUMIRE_PRODUS.add(json_data.getString("denumire_produs").replace("&#34;","'"));
                } else {
                    DENUMIRE_PRODUS.add(json_data.getString("denumire_produs"));
                }
                DESCRIERE_PRODUS.add(json_data.getString("descriere_produs"));
                UM.add(json_data.getString("um"));
                PRET_BUCATA.add(BigDecimal.valueOf(json_data.getDouble("pret_bucata")).floatValue());
                COD.add(json_data.getInt("cod"));
                POZA.add(json_data.getString("poza"));

            }

        }catch(JSONException e){
            e.printStackTrace();
        }


        return null; //I also need to return the list2 here

    }



    protected void onPostExecute(List<String> pngsloaded){

        if (NUME_CATEGORIE.size() != 0){

            mListener.createAdapter(NUME_CATEGORIE, DENUMIRE_PRODUS, DESCRIERE_PRODUS, PRET_BUCATA, COD, UM, POZA );
        } else {
            mListener.onError();
        }

    }
}
