package ro.duoline.papacatering;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
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

public class AddAdreseActivity extends AppCompatActivity {
    private EditText adresa;
    private Spinner spinner;
    private JSONArray textCititdinFisier;
    private boolean editeaza; // true inseamna ca la salvare nu se adauga un profil nou ci se editeaza cel transmit prin getExtras()
    private int editID;
    // private ArrayAdapter<CharSequence> adp;
    private ArrayAdapter<String> adp;
    private String livrare;
    private int id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addadrese);
        getSupportActionBar().hide();
        Button b1 = (Button) findViewById(R.id.salveaza);
        Button b2 = (Button) findViewById(R.id.cancel);
        adresa = (EditText) findViewById(R.id.adresa);
        spinner = (Spinner) findViewById(R.id.spinner);
        livrare = getIntent().getStringExtra("LIVRARE");
        if(livrare == null) livrare = ReadLivrare();
        id = getIntent().getIntExtra("ID", 0);
        String[] localitatiLivrare = livrare.split(",");


        adresa.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_CAP_WORDS);
        // Create an ArrayAdapter using the string array and a default spinner layout
        adp = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, localitatiLivrare);
        //adp = ArrayAdapter.createFromResource(this, R.array.livrare_localitati, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adp.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        spinner.setAdapter(adp);

        editeaza = false;
        Intent intent = getIntent();
        Bundle bd = intent.getExtras();

        if(bd.containsKey("adresaDeEditat")){
            String[] getAdrese = (String[]) bd.get("adresaDeEditat");
            adresa.setText(getAdrese[0]);
            int spinnerPosition = adp.getPosition(getAdrese[1]);
            spinner.setSelection(spinnerPosition);
            editeaza = true;
            editID = Integer.parseInt(getAdrese[2]);
        }

        b1.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                if(adresa.getText().toString().length() == 0){
                    adresa.setError("Adresa de livrare e obligatoriu de completat");
                } else {

                    ReadBtn();

                    makeJSON(adresa.getText().toString());


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

    private void makeJSON(String adresa) {

        try {

            JSONObject json;
            //    JSONArray jArray = new JSONArray();
            Map<String, Object> data;
            data = new HashMap<String, Object>();
            data.put("adresa", adresa);
            data.put("localitatea", spinner.getSelectedItem().toString());
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
            WriteBtn(textCititdinFisier.toString());
        } catch (JSONException e){
            e.printStackTrace();
        }
        finish();
    }

    public String ReadLivrare() {
        //reading text from file
        String s="";
        try {
            FileInputStream fileIn=openFileInput("livrare.txt");
            InputStreamReader InputRead= new InputStreamReader(fileIn);

            char[] inputBuffer= new char[100];

            int charRead;

            while ((charRead=InputRead.read(inputBuffer))>0) {
                // char to string conversion
                String readstring=String.copyValueOf(inputBuffer,0,charRead);
                s +=readstring;
            }
            InputRead.close();




        } catch (Exception e) {
            e.printStackTrace();
        }
        return s;
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
            FileOutputStream fileout=openFileOutput("adrese"+Integer.toString(id)+".txt", MODE_PRIVATE);
            OutputStreamWriter outputWriter=new OutputStreamWriter(fileout);
            outputWriter.write(textToWrite);
            outputWriter.close();

            //display file saved message
            Toast.makeText(getBaseContext(), "File saved successfully!",
                    Toast.LENGTH_SHORT).show();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
