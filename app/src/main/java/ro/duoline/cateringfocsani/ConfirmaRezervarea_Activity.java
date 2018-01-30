package ro.duoline.cateringfocsani;

import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Date;

import ro.duoline.cateringfocsani.data.RestauranteContract;
import ro.duoline.cateringfocsani.data.RestauranteDbHelper;

public class ConfirmaRezervarea_Activity extends AppCompatActivity {
    private RezervareValues setRezervare;
    private TextView rezervareText;
    private TextView textPersoana;
    private ArrayList<String> profil;
    final Context context = this;
    public ArrayList<String> al;
    public String dateconectare;
    public static int idRestaurant;
    private static String MESAJ_REZERVARE_TRIMISA;
    private SQLiteDatabase mDb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirmarezervare);
        getSupportActionBar().hide();
        setRezervare = new RezervareValues();
        Date d = new Date();
        Long t = getIntent().getLongExtra("dataRezervarii", -1);
        d.setTime(t);
        setRezervare.setDate(d);
        setRezervare.setLocatie(getIntent().getExtras().getString("locatie"));
        setRezervare.setNrPersoane(getIntent().getExtras().getInt("nrPersoane"));
        idRestaurant = getIntent().getExtras().getInt("idRestaurant");
        al = getIntent().getExtras().getStringArrayList("dateConectare");
        dateconectare = "" + al.get(0) + "," + al.get(1) + "," + al.get(2);
        textPersoana = (TextView) findViewById(R.id.textPersoanaR);
        rezervareText = (TextView) findViewById(R.id.textRezervare);
        rezervareText.setText(setRezervare.getFinalString());
        RestauranteDbHelper dbHelper = new RestauranteDbHelper(this);
        mDb = dbHelper.getWritableDatabase();
        String selection = RestauranteContract.RestauranteEntry.COLUMN_REATAURANT_ID + "=" + Integer.toString(idRestaurant);
        Cursor cursor = mDb.query(RestauranteContract.RestauranteEntry.TABLE_NAME, null, selection, null, null, null, null);
        cursor.moveToFirst();
        MESAJ_REZERVARE_TRIMISA = cursor.getString(cursor.getColumnIndex(RestauranteContract.RestauranteEntry.COLUMN_MESAJ_REZERVARE));
        cursor.close();
        findViewById(R.id.backButton2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                finish();
            }
        });

        findViewById(R.id.buttonPersoanaR).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                Intent i=new Intent(ConfirmaRezervarea_Activity.this,AfisareProfile_Activity.class);
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(i);
            }
        });

        findViewById(R.id.trimite_rezervarea).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String prs = profil.get(0) + " - " + profil.get(1);

                if(prs.length() == 3){
                    createAlertDialogBuilder(new AlertDialog.Builder(context), "Nu poti trimite rezervarea daca nu ai ales/adaugat  numarul de telefon al unei persoane de contact!", false).show();
                }  else if(!rezervareText.getText().equals("")){
                        new ConfirmaRezervarea_Activity.HttpAsyncTask().execute(al.get(3));
                         } else {
                         createAlertDialogBuilder(new AlertDialog.Builder(context), "Lipsesc datele rezervarii. Rezervarea a fost deja trimisa.", false).show();
                         }
            }
        });
    }

    private AlertDialog createAlertDialogBuilder(AlertDialog.Builder ad, String message, final Boolean mesajfinal){
        ad.setMessage(message);
        ad.setCancelable(true)
                .setTitle("Atentie!")
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener(){
                    public void onClick(DialogInterface dialog, int id){
                        dialog.cancel();
                        if(mesajfinal){
                            finish();
                        }
                    }
                });
        return ad.create();
    }

    @Override
    protected void onResume() {
        super.onResume();
        profil = new ArrayList<String>();
        if(getFileStreamPath("profile.txt").exists()){
            ReadBtn("profile.txt");
        } else {
            profil.add("");
            profil.add("");
        }
        if(profil.size() == 0){
            profil.add("");
            profil.add("");
        }

        textPersoana.setText(profil.get(0) + " - " + profil.get(1));
    }

    private JSONArray makeJson(String prs, RezervareValues setRezervare){
        try {
            JSONArray jarrFinal = new JSONArray();
            JSONObject jo = new JSONObject();
            jo.accumulate("persoana", prs);
            jo.accumulate("locatie", setRezervare.getFullLocatie());
            jo.accumulate("dataRezervarii", setRezervare.getStringDateMYSQL());
            jo.accumulate("nrpersoane", setRezervare.getNrPersoane());
            jo.accumulate("idRestaurant", idRestaurant);
            jarrFinal.put(jo);

            return jarrFinal;

        } catch (JSONException e){
            e.printStackTrace();
            return null;
        }

    }
    public static String POST(String url, JSONArray jarr, String dateconectare){
        //InputStream inputStream = null;
        try{
            String json = URLEncoder.encode(jarr.toString(), "UTF-8");
            // 1. create HttpClient
            HttpClient httpclient = new DefaultHttpClient();
            // 2. initialize HTTPGet request
            HttpGet request = new HttpGet();
            request.setURI(new URI(url+"/setRezervarea.php?sirjson="+json+"&dateconectare="+dateconectare));
            // 2. Execute GET request to the given URL
            HttpResponse response = httpclient.execute(request);
            return "OK";
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private class HttpAsyncTask extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... urls) {
            String prs = profil.get(0) + " - " + profil.get(1);
            JSONArray rezervarea = makeJson(prs, setRezervare);
            String result = POST(urls[0],rezervarea, dateconectare);
            return result;
        }

        @Override
        protected void onPostExecute(String result) {
            if(result == null){
                Toast.makeText(context, "EROARE CONECTARE SERVER", Toast.LENGTH_LONG).show();
            }else {
                createAlertDialogBuilder(new AlertDialog.Builder(context), MESAJ_REZERVARE_TRIMISA, true).show();
                rezervareText.setText("");
                String where = RestauranteContract.RestauranteEntry.COLUMN_REATAURANT_ID + "=" + idRestaurant;
                Cursor cursor = mDb.query(RestauranteContract.RestauranteEntry.TABLE_NAME, null, where, null, null, null, null);
                if (cursor != null) {
                    cursor.moveToFirst();
                    int scor = cursor.getInt(cursor.getColumnIndex(RestauranteContract.RestauranteEntry.COLUMN_SCOR));
                    ContentValues cv = new ContentValues();
                    scor = scor + 1;
                    cv.put(RestauranteContract.RestauranteEntry.COLUMN_SCOR, scor);
                    mDb.update(RestauranteContract.RestauranteEntry.TABLE_NAME, cv, where, null);
                }
            }
        }
    }

    public void ReadBtn(String file) {
        //reading text from file
        try {
            FileInputStream fileIn=openFileInput(file); //"profile.txt"

            InputStreamReader InputRead= new InputStreamReader(fileIn);

            char[] inputBuffer= new char[100];
            String s="";
            int charRead;

            while ((charRead=InputRead.read(inputBuffer))>0) {
                // char to string conversion
                String readstring=String.copyValueOf(inputBuffer,0,charRead);
                s +=readstring;
            }
            InputRead.close();

            JSONArray jArr = new JSONArray(s);
            if(file == "profile.txt") {
                for (int i = 0; i < jArr.length(); i++) {
                    if (jArr.getJSONObject(i).getBoolean("checked")) {
                        profil.add(jArr.getJSONObject(i).get("nume").toString());
                        profil.add(jArr.getJSONObject(i).get("telefon").toString());
                    }
                }
            }


        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
