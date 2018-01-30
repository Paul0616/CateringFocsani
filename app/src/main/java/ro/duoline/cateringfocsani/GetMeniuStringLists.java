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
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Paul on 28.05.2017.
 */

public class GetMeniuStringLists extends AsyncTask<String, Void, List<String>> {
    private  static String CATEGORII_FILE_PHP_QUERY;//"getCategoriiV2.php";
    private  static String RESTAURANT_QUERY;//"restaurant";

    InputStream is=null;
    StringBuilder sb=null;
    private static int denumiriSauPoze; // valoarea 1 inseamna ca se vor incarca denumirile din baza de date a restaurantului si
    // 2 inseamna ca se vor incarca linkurile pozelor din bza de date OnMedia

    private List<String> LIST = new ArrayList<String>();
    private  List<String> LIST1 = new ArrayList<String>();
    private  List<String> LIST2 = new ArrayList<String>();

    public GetMeniuStringLists(Listener listener, int denumiriSauPoze){
        mListener = listener;
        this.denumiriSauPoze = denumiriSauPoze;
        if(denumiriSauPoze == 1) {

            CATEGORII_FILE_PHP_QUERY = "getCategorii.php";
            RESTAURANT_QUERY = "dateconectare";
        }
        if (denumiriSauPoze == 2) {

            CATEGORII_FILE_PHP_QUERY = "getIconuriCategoriiV2.php";
            RESTAURANT_QUERY = "";
        }
    }

    public interface Listener{
        void updateLists_Resume(int denumiriSauPoze, List<String> A, List<String> B, List<String> C);
        void onError();
    }

    private Listener mListener;

    protected List<String> doInBackground(String... args) {

        String result = "";
        ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
        //CONNECT TO DATABASE
        try{

            HttpClient httpclient = new DefaultHttpClient();
            Uri bultUri = Uri.parse(args[1]);
            bultUri = Uri.withAppendedPath(bultUri, CATEGORII_FILE_PHP_QUERY);
            if(denumiriSauPoze == 1) {
                bultUri = bultUri.buildUpon().appendQueryParameter(RESTAURANT_QUERY, args[0]).build();
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

            JSONArray jArray = new JSONArray(result);
            JSONObject json_data=null;

            for(int i=0;i<jArray.length();i++) {

                json_data = jArray.getJSONObject(i);
                if (denumiriSauPoze == 1) {
                    if (json_data.has("nume_categorie"))
                        LIST.add(json_data.getString("nume_categorie"));
                }
                if (denumiriSauPoze == 2){
                    if (json_data.has("poza_categorie"))
                        LIST1.add(json_data.getString("poza_categorie"));
                    if (json_data.has("denumire"))
                        LIST2.add(json_data.getString("denumire"));
                }

            }

        } catch(JSONException e){
            e.printStackTrace();
        }


        return null; //I also need to return the list2 here

    }



    protected void onPostExecute(List<String> pngsloaded){
        if(denumiriSauPoze == 1) {
            if (LIST.size() != 0){

                mListener.updateLists_Resume(denumiriSauPoze, LIST, LIST1, LIST2);
                return;
            } else {
                mListener.onError();
            }
        }
        if(denumiriSauPoze == 2) {
            if (LIST1.size() != 0 && LIST2.size() != 0){
                mListener.updateLists_Resume(denumiriSauPoze, LIST, LIST1, LIST2);
                return;
            } else {
                mListener.onError();
            }
        }

    }
}
