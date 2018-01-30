package ro.duoline.papacatering;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.HashMap;
import java.util.Map;

public class AddProfilActivity extends AppCompatActivity {
    private EditText nume, email, telefon;
    private JSONArray textCititdinFisier;
    private boolean editeaza; // true inseamna ca la salvare nu se adauga un profil nou ci se editeaza cel transmit prin getExtras()
    private int editID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addprofile);
        getSupportActionBar().hide();
        Button b1 = (Button) findViewById(R.id.salveaza);
        Button b2 = (Button) findViewById(R.id.cancel);
        nume = (EditText) findViewById(R.id.nume_prenume);
        email = (EditText) findViewById(R.id.email);
        telefon = (EditText) findViewById(R.id.telefon);
        //textCititdinFisier = new JSONArray();
        editeaza = false;
        nume.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_CAP_WORDS);

        Intent intent = getIntent();
        Bundle bd = intent.getExtras();
        if(bd != null){
            String[] getProfil = (String[]) bd.get("profilDeEditat");
            nume.setText(getProfil[0]);
            email.setText(getProfil[1]);
            telefon.setText(getProfil[2]);
            editeaza = true;
            editID = Integer.parseInt(getProfil[3]);
        }

        b1.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                String msg = validareNrTelefon(telefon.getText().toString());
                if(!msg.equals("OK")){
                    telefon.setError(msg);
                } else {

                    ReadBtn();
                    makeJSON(nume.getText().toString(), email.getText().toString(), telefon.getText().toString());


                }
            }
        });

        b2.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){

                finish();
            }
        });
    }

    private String validareNrTelefon(String nrtelefon){
        if(nrtelefon.length() == 0) {
            return "Nr de telefon e obligatoriu";
        }

       // Integer val; //= Integer.valueOf(value);
        String nrtelefonCuratat = "";
        for(int i = 0; i<nrtelefon.length(); i++) {
          //val = Integer.valueOf(nrtelefon.substring(i, i+1));

         // if(val != null) {
            if(nrtelefon.substring(i, i+1).matches("\\d+(?:\\.\\d+)?")){
              nrtelefonCuratat = nrtelefonCuratat + nrtelefon.substring(i, i+1);
          }
        }

        if(!nrtelefonCuratat.substring(0,1).equals("0")){
            telefon.setText(nrtelefonCuratat);
            return "Prima cifra trebuie sa fie 0";
        }

        if(nrtelefonCuratat.length() != 10){
            telefon.setText(nrtelefonCuratat);
            return "Trebuie ca nr de telefon sa aiba 10 cifre";
        }
        telefon.setText(nrtelefonCuratat);
        return "OK";
    }

    private void makeJSON(String nume, String email, String telefon) {

        try {

            JSONObject json;
            //    JSONArray jArray = new JSONArray();
            Map<String, Object> data;
            data = new HashMap<String, Object>();
            data.put("nume", nume);
            data.put("email", email);
            data.put("telefon", telefon);
            data.put("checked", true);
            json = new JSONObject(data);
            if(textCititdinFisier != null) {
                for (int i = 0; i < textCititdinFisier.length(); i++) {
                    textCititdinFisier.getJSONObject(i).put("checked", false);
                }
            } else{
                textCititdinFisier = new JSONArray();
            }
            if (!editeaza) {
                textCititdinFisier.put(json);
            } else {
                textCititdinFisier.put(editID, json);
            }
            String ad = textCititdinFisier.toString();
            WriteBtn(textCititdinFisier.toString());
        } catch (JSONException e){
            e.printStackTrace();
        }
        finish();
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
            //Toast.makeText(getBaseContext(), textCititdinFisier.toString(), Toast.LENGTH_LONG).show();


        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    // write text to file
    public void WriteBtn(String textToWrite) {
        // add-write text into file
        try {
            FileOutputStream fileout=openFileOutput("profile.txt", MODE_PRIVATE);
            OutputStreamWriter outputWriter=new OutputStreamWriter(fileout);
            outputWriter.write(textToWrite);
            outputWriter.close();

            //display file saved message
            Toast.makeText(getBaseContext(), "Profilul tau a fost salvat in telefon",
                    Toast.LENGTH_SHORT).show();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
