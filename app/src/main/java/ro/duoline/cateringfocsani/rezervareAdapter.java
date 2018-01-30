package ro.duoline.cateringfocsani;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by Paul on 27.05.2017.
 */

public class rezervareAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private Context context;
    private TextView txtSumar;
    private RezervareValues setrezervare;
    private int nrLocatii;

    public class MulteViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        private TextView text_locatie;
        private customImageView img_locatie;//


        public MulteViewHolder(View view) {
            super(view);

            text_locatie = (TextView)view.findViewById(R.id.text_locatie);
            img_locatie =  (customImageView) view.findViewById(R.id.image_locatie);//(customImageView)
            img_locatie.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    setrezervare.setLocatie(text_locatie.getText().toString());
                    txtSumar.setText(setrezervare.getFinalString());
                }
            });
        }
    }

    public class OpozaViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        private TextView text_locatie;
        private ImageView img_locatie;//


        public OpozaViewHolder(View view) {
            super(view);

            text_locatie = (TextView)view.findViewById(R.id.text_locatie1);
            img_locatie =  (ImageView) view.findViewById(R.id.imagineOPoza);//(customImageView)
            img_locatie.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    setrezervare.setLocatie(text_locatie.getText().toString());
                    txtSumar.setText(setrezervare.getFinalString());
                }
            });
        }
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public rezervareAdapter(Context context, TextView txt, RezervareValues setrezervare, int nrlocatii) {
        this.context = context;
        this.txtSumar = txt;
        this.setrezervare = setrezervare;
        this.nrLocatii = nrlocatii;
        if(nrlocatii == 1){
            List<String> myList1 = new ArrayList<String>(Arrays.asList(setrezervare.getsrcLocatie1().split(",")));
            setrezervare.setLocatie(myList1.get(0));
            txtSumar.setText(setrezervare.getFinalString());
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // create a new view
        View v;
        if(nrLocatii == 1){
            v = LayoutInflater.from(parent.getContext()).inflate(R.layout.grid_rezervare_o_poza, parent, false);
           OpozaViewHolder vh = new OpozaViewHolder(v);
            return vh;
        } else {
            v = LayoutInflater.from(parent.getContext()).inflate(R.layout.grid_rezervare, parent, false);
            MulteViewHolder vh = new MulteViewHolder(v);
            return vh;
        }
        // set the view's size, margins, paddings and layout parameters

    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        List<String> myList1 = new ArrayList<String>(Arrays.asList(setrezervare.getsrcLocatie1().split(",")));
        if(myList1.size() == 1) myList1.add("");
        List<String> myList2 = new ArrayList<String>(Arrays.asList(setrezervare.getsrcLocatie2().split(",")));
        if(myList2.size() == 1) myList2.add("");
        List<String> myList3 = new ArrayList<String>(Arrays.asList(setrezervare.getsrcLocatie3().split(",")));
        if(myList3.size() == 1) myList3.add("");


        switch(position){
            case 0:
                if(holder instanceof OpozaViewHolder){
                    ((OpozaViewHolder) holder).text_locatie.setText(myList1.get(0).toString());
                    Picasso.with(context).load(myList1.get(1)).into(((OpozaViewHolder) holder).img_locatie);
                } else if (holder instanceof  MulteViewHolder){
                    ((MulteViewHolder) holder).text_locatie.setText(myList1.get(0).toString());
                    Picasso.with(context).load(myList1.get(1)).into(((MulteViewHolder) holder).img_locatie);
                }
                break;
            case 1:
                if(holder instanceof OpozaViewHolder){
                    ((OpozaViewHolder) holder).text_locatie.setText(myList2.get(0).toString());
                    Picasso.with(context).load(myList2.get(1)).into(((OpozaViewHolder) holder).img_locatie);
                } else if (holder instanceof  MulteViewHolder){
                    ((MulteViewHolder) holder).text_locatie.setText(myList2.get(0).toString());
                    Picasso.with(context).load(myList2.get(1)).into(((MulteViewHolder) holder).img_locatie);
                }
                break;
            case 2:
                if(holder instanceof OpozaViewHolder){
                    ((OpozaViewHolder) holder).text_locatie.setText(myList3.get(0).toString());
                    Picasso.with(context).load(myList3.get(1)).into(((OpozaViewHolder) holder).img_locatie);
                } else if (holder instanceof  MulteViewHolder){
                    ((MulteViewHolder) holder).text_locatie.setText(myList3.get(0).toString());
                    Picasso.with(context).load(myList3.get(1)).into(((MulteViewHolder) holder).img_locatie);
                }
                break;
            default:
                break;
        }
    }

    @Override
    public int getItemCount() {
        return nrLocatii;
    }
}
