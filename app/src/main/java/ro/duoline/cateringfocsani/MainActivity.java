package ro.duoline.cateringfocsani;

import android.content.Intent;
import android.graphics.Typeface;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    TextView b1, textRestaurant, textDenumire;
    public String denumireRestaurant, livrare;
    public ArrayList<String> al;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().hide();
        denumireRestaurant = getIntent().getStringExtra("DENUMIRE");
        livrare = getIntent().getStringExtra("LIVRARE");
        WriteUpdate(livrare);
        al = getIntent().getExtras().getStringArrayList("dateConectare");
        final int id = getIntent().getIntExtra("ID", 0);
        b1= (TextView) findViewById(R.id.button);
        textRestaurant = (TextView) findViewById(R.id.textRestaurant) ;
        Typeface face= Typeface.createFromAsset(textRestaurant.getContext().getAssets(), "font/PoiretOne-Regular.ttf");
        textRestaurant.setTypeface(face);
        textDenumire = (TextView) findViewById(R.id.textDenumire) ;
        textDenumire.setTypeface(face);
        textDenumire.setText(denumireRestaurant);
        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){

                Intent i = new Intent(MainActivity.this,CategoriiMeniu_Activity.class);
                i.putExtra("DENUMIRE", denumireRestaurant.replace(" ", "_"));
                i.putExtra("ID", id);
                i.putStringArrayListExtra("dateConectare", al);
                startActivity(i);
            }
        });


        findViewById(R.id.istoric).setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){

                Intent i = new Intent(MainActivity.this,IstoricComenzi_Activity.class);
                i.putStringArrayListExtra("dateConectare", al);
                startActivity(i);
            }
        });

        findViewById(R.id.profile).setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){

                Intent intent = new Intent(MainActivity.this,AfisareProfile_Activity.class);
                startActivity(intent);
            }
        });

        findViewById(R.id.adrese).setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){

                Intent intent = new Intent(MainActivity.this, AfisareAdrese_Activity.class);
                intent.putExtra("LIVRARE", livrare);
                intent.putExtra("ID", id);
                startActivity(intent);

            }
        });
    }

    public void WriteUpdate(String textToWrite) {
        // add-write text into file
        try {
            FileOutputStream fileout=openFileOutput("livrare.txt", MODE_PRIVATE);
            OutputStreamWriter outputWriter=new OutputStreamWriter(fileout);
            outputWriter.write(textToWrite);
            outputWriter.close();


        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
