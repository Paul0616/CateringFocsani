package ro.duoline.papacatering;

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
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

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

import ro.duoline.papacatering.data.RestauranteContract;
import ro.duoline.papacatering.data.RestauranteDbHelper;

public class ConfirmaComanda_Activity extends AppCompatActivity {
    private TextView textAdresa;
    private TextView textPersoana;
    private ArrayList<ConfirmareValues> valoriConfirmare;
    private ConfirmaComandaAdapter adapter;
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private ArrayList<String> adresa;
    private ArrayList<String> profil;
    private TextView tg;
    public int idRestaurant;
    final Context context = this;
    public String dateconectare;
    public ArrayList<String> al;
    private SQLiteDatabase mDb;


    private static String MESAJ_COMANDA_TRIMISA;
    private int valoareMinima;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirmacomanda);
        getSupportActionBar().hide();
        idRestaurant = getIntent().getIntExtra("ID", 0);
        al = getIntent().getExtras().getStringArrayList("dateconectare");
        dateconectare = "" + al.get(0) + "," + al.get(1) + "," + al.get(2);
        ArrayList<String> denumiri = getIntent().getExtras().getStringArrayList("denumiri");
        ArrayList<String> cerinte = getIntent().getExtras().getStringArrayList("cerinte");
        float[] pret = getIntent().getExtras().getFloatArray("pret");
        int[] cantitate = getIntent().getExtras().getIntArray("cantitate");
        int[] cod = getIntent().getExtras().getIntArray("cod");
        textAdresa = (TextView) findViewById(R.id.textAdresa);
        textPersoana = (TextView) findViewById(R.id.textPersoana);
        tg = (TextView) findViewById(R.id.total);
        recyclerView = (RecyclerView)findViewById(R.id.recyclerViewRezervarea);
        layoutManager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);
        RestauranteDbHelper dbHelper = new RestauranteDbHelper(this);
        mDb = dbHelper.getWritableDatabase();
        String selection = RestauranteContract.RestauranteEntry.COLUMN_REATAURANT_ID + "=" + Integer.toString(idRestaurant);
        Cursor cursor = mDb.query(RestauranteContract.RestauranteEntry.TABLE_NAME, null, selection, null, null, null, null);
        cursor.moveToFirst();
        MESAJ_COMANDA_TRIMISA = cursor.getString(cursor.getColumnIndex(RestauranteContract.RestauranteEntry.COLUMN_MESAJ_CATERING));
        valoareMinima = cursor.getInt(cursor.getColumnIndex(RestauranteContract.RestauranteEntry.COLUMN_VALOARE_MINIMA_LIVRARE));
        cursor.close();
        findViewById(R.id.backButton1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){

                try {
                    Intent intent = new Intent();
                    JSONObject jo1 = new JSONObject();
                    for (int i = 0; i < valoriConfirmare.size(); i++) {
                        jo1.accumulate("cod", valoriConfirmare.get(i).getCod());
                        jo1.accumulate("bucati", valoriConfirmare.get(i).getBuc_comandate());

                    }
                    intent.putExtra("ValoriConfirmate",jo1.toString());
                    setResult(RESULT_OK, intent);

                } catch (JSONException e){
                    e.printStackTrace();
                    //return null;
                }

                finish();
            }
        });

        findViewById(R.id.trimite_comanda).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                String adr = adresa.get(0) + " " + adresa.get(1);
                String prs = profil.get(0) + "|" + profil.get(1);

                if(adr.length() == 1 || prs.length() == 3){
                    createAlertDialogBuilder(new AlertDialog.Builder(context,R.style.RedTheme), "Nu poti trimite comanda daca nu ai ales/adaugat o adresa de livrare si numarul de telefon al unei persoane de contact!", false).show();
                } else if(valoriConfirmare.size() == 0){
                    createAlertDialogBuilder(new AlertDialog.Builder(context,R.style.RedTheme), "Nu ai ales nimic. Comanda ceva si apoi trimite din nou.", false).show();
                } else if(totalGeneral(tg.getText().toString()) < valoareMinima){
                    createAlertDialogBuilder(new AlertDialog.Builder(context,R.style.RedTheme), "Valoarea comenzii trebuie sa fie de cel putin " + valoareMinima + " RON.", false).show();
                } else {
                    new HttpAsyncTask().execute(al.get(3), tg.getText().toString());
                }
            }
        });

        findViewById(R.id.buttonAdresa).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                Intent i=new Intent(ConfirmaComanda_Activity.this,AfisareAdrese_Activity.class);
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                i.putExtra("ID", idRestaurant);
                startActivity(i);
            }
        });

        findViewById(R.id.buttonPersoana).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                Intent i=new Intent(ConfirmaComanda_Activity.this,AfisareProfile_Activity.class);
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(i);
            }
        });

        valoriConfirmare = new ArrayList<ConfirmareValues>();
        for(int i = 0; i<denumiri.size(); i++){
            ConfirmareValues obj = new ConfirmareValues();
            obj.setCod(cod[i]);
            obj.setDenumire(denumiri.get(i));
            obj.setCerinte(cerinte.get(i));
            obj.setBuc_comandate(cantitate[i]);
            obj.setPret(pret[i]);
            obj.setTotal(pret[i]);
            valoriConfirmare.add(obj);
        }

        adapter = new ConfirmaComandaAdapter(getApplicationContext(), tg, valoriConfirmare);
        recyclerView.setAdapter(adapter);
    }

    private JSONArray makeJson(String adr, String prs, String tg){
        try {
            JSONArray jarrFinal = new JSONArray();
            JSONObject jo = new JSONObject();
            jo.accumulate("adresa", adr);
            jo.accumulate("persoana", prs);
            jo.accumulate("totalgeneral", tg);
            jarrFinal.put(jo);
            JSONArray jarr = new JSONArray();
            JSONObject jo1 = new JSONObject();
            for (int i = 0; i < valoriConfirmare.size(); i++) {
                jo1.accumulate("idRestaurant", Integer.toString(idRestaurant));
                jo1.accumulate("produs", valoriConfirmare.get(i).getDenumire().toString());
                jo1.accumulate("bucati", valoriConfirmare.get(i).getBuc_comandate().toString());
                jo1.accumulate("cod", valoriConfirmare.get(i).getCod().toString());
                jo1.accumulate("cerinte_speciale", valoriConfirmare.get(i).getCerinte().toString());
                jo1.accumulate("pret", valoriConfirmare.get(i).getPret().toString());
                jo1.accumulate("total", valoriConfirmare.get(i).getTotal().toString());
            }
            jarr.put(jo1);
            jarrFinal.put(jo1);
            return jarrFinal;

        } catch (JSONException e){
            e.printStackTrace();
            return null;
        }

    }
    public static void POST(String url, JSONArray jarr, String dateconectare){
        //InputStream inputStream = null;
        try{
            String json = URLEncoder.encode(jarr.toString(), "UTF-8");
            // 1. create HttpClient
            HttpClient httpclient = new DefaultHttpClient();
            // 2. initialize HTTPGet request
            HttpGet request = new HttpGet();


            request.setURI(new URI(url+"/setComanda.php?sirjson="+json+"&dateconectare="+dateconectare));
            // 2. Execute GET request to the given URL
            HttpResponse response = httpclient.execute(request);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private class HttpAsyncTask extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... urls) {
            String adr = adresa.get(0) + " " + adresa.get(1);
            String prs = profil.get(0) + "|" + profil.get(1);
            JSONArray comanda = makeJson(adr, prs, urls[1]);
            POST(urls[0],comanda, dateconectare);
            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            createAlertDialogBuilder(new AlertDialog.Builder(context), MESAJ_COMANDA_TRIMISA, true).show();
            tg.setText("TOTAL: ");
            valoriConfirmare.clear();
            adapter.notifyDataSetChanged();
            String where = RestauranteContract.RestauranteEntry.COLUMN_REATAURANT_ID + "=" + idRestaurant;
            Cursor cursor = mDb.query(RestauranteContract.RestauranteEntry.TABLE_NAME,null,where,null,null,null,null);
            if(cursor != null) {
                cursor.moveToFirst();
                int scor = cursor.getInt(cursor.getColumnIndex(RestauranteContract.RestauranteEntry.COLUMN_SCOR));
                ContentValues cv = new ContentValues();
                scor = scor + 1;
                cv.put(RestauranteContract.RestauranteEntry.COLUMN_SCOR, scor);
                mDb.update(RestauranteContract.RestauranteEntry.TABLE_NAME, cv, where, null);
            }
        }
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
                            tg.setText("TOTAL: ");
                            valoriConfirmare.clear();
                            adapter.notifyDataSetChanged();
                            Intent i = new Intent(ConfirmaComanda_Activity.this,IstoricComenzi_Activity.class);
                            i.putStringArrayListExtra("dateConectare", al);
                            startActivity(i);
                        }
                    }
                });
        return ad.create();
    }

    @Override
    protected void onResume() {
        super.onResume();
        profil = new ArrayList<String>();
        adresa = new ArrayList<String>();
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

        if(getFileStreamPath("adrese"+Integer.toString(idRestaurant)+".txt").exists()){
            ReadBtn("adrese"+Integer.toString(idRestaurant)+".txt");
        } else {
            adresa.add("");
            adresa.add("");
        }
        if(adresa.size() == 0){
            adresa.add("");
            adresa.add("");
        }

        textAdresa.setText(adresa.get(0) + " " + adresa.get(1));
        textPersoana.setText(profil.get(0) + " - " + profil.get(1));
    }
    private float totalGeneral(String tg){
        float total =  Float.valueOf(tg.substring(7, tg.length()-4));
        return total;
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
            if(file.equals("profile.txt")) {
                for (int i = 0; i < jArr.length(); i++) {
                    if (jArr.getJSONObject(i).getBoolean("checked")) {
                        profil.add(jArr.getJSONObject(i).get("nume").toString());
                        profil.add(jArr.getJSONObject(i).get("telefon").toString());
                    }
                }
            }

            if(file.equals("adrese"+Integer.toString(idRestaurant)+".txt")) {
                for (int i = 0; i < jArr.length(); i++) {
                    if (jArr.getJSONObject(i).getBoolean("checked")) {
                        adresa.add(jArr.getJSONObject(i).get("adresa").toString());
                        adresa.add(jArr.getJSONObject(i).get("localitatea").toString());
                    }
                }
            }


        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
