package ro.duoline.papacatering;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class IstoricComenzi_Activity extends AppCompatActivity implements  GetIstoricLists.Listener{

    private RecyclerView recyclerView;
    private istoricAdapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    public ArrayList<String> al;

    private JSONArray textCititdinFisier;
    private ArrayList<ProfileValue> m_data;
    public int profilCurent;
    public String dateconectare;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_istoriccomenzi);
        getSupportActionBar().hide();
        recyclerView = (RecyclerView)findViewById(R.id.recycleview_istoric);
        layoutManager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);

        recyclerView.addItemDecoration(new LineItemDecoration(this));
        al = getIntent().getExtras().getStringArrayList("dateConectare");
        dateconectare = "" + al.get(0) + "," + al.get(1) + "," + al.get(2);
        findViewById(R.id.backButton3).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                finish();
            }
        });

        if(getFileStreamPath("profile.txt").exists()){
            ReadBtn();
        } else {
            textCititdinFisier = new JSONArray();
        }
        m_data = new ArrayList<ProfileValue>();
        String persoana = "";
        try {
            for(int i = 0; i < textCititdinFisier.length(); i++) {


                ProfileValue object = new ProfileValue();
                object.setNume(textCititdinFisier.getJSONObject(i).get("nume").toString());
                object.setEmail(textCititdinFisier.getJSONObject(i).get("email").toString());
                object.setTelefon(textCititdinFisier.getJSONObject(i).get("telefon").toString());
                object.setChecked(textCititdinFisier.getJSONObject(i).getBoolean("checked"));
                // if(object.getChecked()) profilCurent = i;
                m_data.add(object);
                persoana += "'" + textCititdinFisier.getJSONObject(i).get("nume").toString() + "|" + textCititdinFisier.getJSONObject(i).get("telefon").toString() + "',";
            }

            if(!persoana.equals(""))
                persoana = persoana.substring(0,persoana.length()-1);
            else
                persoana = "'-'";
            AsyncTask<String, Void, JSONArray> execute = new GetIstoricLists(this).execute(dateconectare, persoana, al.get(3));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();



    }

    public void updateLists(JSONArray jarray){
        List<IstoricComenziValues> istoricComenzi = new ArrayList<>();
        Boolean child;
        try{
            for(int i=0; i< jarray.length(); i++){
                if(jarray.getJSONObject(i).get("child").equals("true")) {
                    child = true;
                    istoricComenzi.add(new IstoricComenziValues(jarray.getJSONObject(i), child, false));
                }
                else {
                    child = false;
                    istoricComenzi.add(new IstoricComenziValues(jarray.getJSONObject(i), child, true));
                }

            }
        } catch (JSONException e){
            e.printStackTrace();
        }

        adapter = new istoricAdapter(getApplicationContext(), istoricComenzi);
        recyclerView.setAdapter(adapter);


    }

    public void onError() {
        Toast.makeText(this, "Nu exista comenzi in ultima saptamana pentru acest profil sau \nEroare conectare server! VERIFICA CONEXIUNEA LA INTERNET", Toast.LENGTH_SHORT).show();
    }


    public void profilSelectat(int profil){
        profilCurent = profil;

        try {

            for(int i =0; i<textCititdinFisier.length(); i++){

                if(i == profilCurent){
                    textCititdinFisier.getJSONObject(i).put("checked", true);
                } else {
                    textCititdinFisier.getJSONObject(i).put("checked", false);
                }

            }
            String persoana = textCititdinFisier.getJSONObject(profilCurent).get("nume").toString() + " - " + textCititdinFisier.getJSONObject(profilCurent).get("telefon").toString();
            AsyncTask<String, Void, JSONArray> execute = new GetIstoricLists(this).execute(dateconectare, persoana);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    public void ReadBtn() {
        //reading text from file
        try {
            FileInputStream fileIn=openFileInput("profile.txt");

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
            textCititdinFisier = new  JSONArray();
            JSONArray jArr = new JSONArray(s);
            for(int i = 0; i<jArr.length(); i++){
                textCititdinFisier.put(jArr.getJSONObject(i));
            }

            // JSONObject json = new JSONObject(s);
            // Toast.makeText(getBaseContext(), textCititdinFisier.toString(), Toast.LENGTH_LONG).show();


        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
