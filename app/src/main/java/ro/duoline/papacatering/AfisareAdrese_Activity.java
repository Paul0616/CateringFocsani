package ro.duoline.papacatering;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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

public class AfisareAdrese_Activity extends AppCompatActivity {

    private JSONArray textCititdinFisier;
    private ArrayList<AdreseValue> m_data;
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private AdreseAdapter adapter;
    public Button adauga;

    public Button cancel;
    public AfisareAdrese_Activity mInstance;
    public int adresaCurenta;
    public String livrare;
    public int id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adrese);
        getSupportActionBar().hide();
        recyclerView = (RecyclerView)findViewById(R.id.rez_recycler_view);
        layoutManager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);
        adauga = (Button) findViewById(R.id.adauga);
        livrare = getIntent().getStringExtra("LIVRARE");
        id = getIntent().getIntExtra("ID", 0);
        cancel = (Button) findViewById(R.id.cancel);
        mInstance = this;

        adauga.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent i = new Intent(AfisareAdrese_Activity.this,AddAdreseActivity.class);
                i.putExtra("LIVRARE", livrare);
                i.putExtra("ID", id);
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
    protected void onResume(){
        super.onResume();
        if(getFileStreamPath("adrese"+Integer.toString(id)+".txt").exists()){
            ReadBtn();
        } else {
            textCititdinFisier = new JSONArray();
        }
        m_data = new ArrayList<AdreseValue>();
        for(int i = 0; i < textCititdinFisier.length(); i++) {

            try {
                AdreseValue object = new AdreseValue();
                object.setAdresa(textCititdinFisier.getJSONObject(i).get("adresa").toString());
                object.setLocalitatea(textCititdinFisier.getJSONObject(i).get("localitatea").toString());
                object.setChecked(textCititdinFisier.getJSONObject(i).getBoolean("checked"));
                m_data.add(object);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        //selecteaza.setEnabled(false);


        adapter = new AdreseAdapter(getApplicationContext(), mInstance, m_data);
        recyclerView.setAdapter(adapter);

    }

    public void editProfil(String[] strArray){
        Intent i = new Intent(AfisareAdrese_Activity.this,AddAdreseActivity.class);
        i.putExtra("adresaDeEditat", strArray);
        i.putExtra("LIVRARE", livrare);
        i.putExtra("ID", id);
        startActivity(i);
    }

    public void profilSelectat(int profil){
        adresaCurenta = profil;

        try {

            for(int i =0; i<textCititdinFisier.length(); i++){

                if(i == adresaCurenta){
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
            FileInputStream fileIn=openFileInput("adrese"+Integer.toString(id)+".txt");

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

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void WriteUpdate(String textToWrite) {
        // add-write text into file
        try {
            FileOutputStream fileout=openFileOutput("adrese"+Integer.toString(id)+".txt", MODE_PRIVATE);
            OutputStreamWriter outputWriter=new OutputStreamWriter(fileout);
            outputWriter.write(textToWrite);
            outputWriter.close();


        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
