package ro.duoline.cateringfocsani;

import android.content.Intent;
import android.os.Bundle;

import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import android.view.View;
import android.widget.Button;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;


public class AfisareProfile_Activity extends AppCompatActivity {
    private JSONArray textCititdinFisier;
    private ArrayList<ProfileValue> m_data;
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private ProfileAdapter adapter;
    public Button adauga;

    public Button cancel;
    public AfisareProfile_Activity mInstance;
    public int profilCurent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        getSupportActionBar().hide();
        recyclerView = (RecyclerView)findViewById(R.id.rez_recycler_view);
        layoutManager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);
        adauga = (Button) findViewById(R.id.adauga);

        cancel = (Button) findViewById(R.id.cancel);
        mInstance = this;


        adauga.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent i = new Intent(AfisareProfile_Activity.this,AddProfilActivity.class);
                startActivity(i);
            }
        });


        cancel.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                finish();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(getFileStreamPath("profile.txt").exists()){
            ReadBtn();
        } else {
            textCititdinFisier = new JSONArray();
        }
        m_data = new ArrayList<ProfileValue>();
        for(int i = 0; i < textCititdinFisier.length(); i++) {

            try {
                ProfileValue object = new ProfileValue();
                object.setNume(textCititdinFisier.getJSONObject(i).get("nume").toString());
                object.setEmail(textCititdinFisier.getJSONObject(i).get("email").toString());
                object.setTelefon(textCititdinFisier.getJSONObject(i).get("telefon").toString());
                object.setChecked(textCititdinFisier.getJSONObject(i).getBoolean("checked"));
                m_data.add(object);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        //selecteaza.setEnabled(false);


        adapter = new ProfileAdapter(getApplicationContext(), mInstance, m_data);
        recyclerView.setAdapter(adapter);
    }

    public void editProfil(String[] strArray){
        Intent i = new Intent(AfisareProfile_Activity.this,AddProfilActivity.class);
        i.putExtra("profilDeEditat", strArray);
        startActivity(i);
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

        } catch (JSONException e) {
            e.printStackTrace();
        }
        WriteUpdate(textCititdinFisier.toString());
    }

    //Read text from file
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

    public void WriteUpdate(String textToWrite) {
        // add-write text into file
        try {
            FileOutputStream fileout=openFileOutput("profile.txt", MODE_PRIVATE);
            OutputStreamWriter outputWriter=new OutputStreamWriter(fileout);
            outputWriter.write(textToWrite);
            outputWriter.close();


        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
