package ro.duoline.papacatering;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RadioButton;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Paul on 28.05.2017.
 */

public class AdreseAdapter extends RecyclerView.Adapter<AdreseAdapter.ViewHolder> {
    private Context context;
    private ArrayList<AdreseValue> adreseArray;
    private AfisareAdrese_Activity instance;


    ////--------------------CLASA VIEWHOLDER-----------------------------------///
    public class ViewHolder extends RecyclerView.ViewHolder {
        private RadioButton radioBtn;
        private ImageView editBtn;
        private ImageView deleteBtn;


        public ViewHolder(View view) {
            super(view);
            // view.setOnClickListener(this);
            radioBtn = (RadioButton) view.findViewById(R.id.radioButton);
            editBtn = (ImageView) view.findViewById(R.id.editBtn);
            deleteBtn = (ImageView) view.findViewById(R.id.deleteBtn);


            radioBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v){
                    //     Toast.makeText(context,getAdapterPosition(),Toast.LENGTH_SHORT).show();

                    for(int i = 0; i< adreseArray.size(); i++){
                        if(i == getAdapterPosition()) {
                            adreseArray.get(i).setChecked(true);
                        } else {
                            adreseArray.get(i).setChecked(false);
                        }
                    }
                    //    instance.selecteaza.setEnabled(true);
                    instance.profilSelectat(getAdapterPosition());
                    notifyDataSetChanged();
                }
            });

            editBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v){
                    String[] strArray = new String[3];
                    strArray[0] = adreseArray.get(getAdapterPosition()).getAdresa();
                    strArray[1] = adreseArray.get(getAdapterPosition()).getLocalitatea();
                    strArray[2] = String.valueOf(getAdapterPosition());
                    instance.editProfil(strArray);
                }
            });
            deleteBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v){
                    adreseArray.remove(getAdapterPosition());

                    JSONArray jsonArray = new JSONArray();

                    for(int i = 0; i< adreseArray.size(); i++){
                        JSONObject json;
                        Map<String, Object> data;
                        data = new HashMap<String, Object>();
                        data.put("adresa", adreseArray.get(i).getAdresa());
                        data.put("localitatea", adreseArray.get(i).getLocalitatea());
                        data.put("checked", adreseArray.get(i).getChecked());
                        json = new JSONObject(data);
                        jsonArray.put(json);
                    }
                    instance.WriteUpdate(jsonArray.toString());
                    notifyDataSetChanged();
                }
            });
        }


    }
    ////-------------------------------------------------------///


    // Provide a suitable constructor (depends on the kind of dataset)
    public AdreseAdapter(Context context, AfisareAdrese_Activity instance, ArrayList<AdreseValue> adrese) {
        this.adreseArray = adrese;
        this.context = context;
        this.instance = instance;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public AdreseAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // create a new view
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.listview_element_adapter, parent, false);
        // set the view's size, margins, paddings and layout parameters
        AdreseAdapter.ViewHolder vh = new AdreseAdapter.ViewHolder(v);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(AdreseAdapter.ViewHolder holder, int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        String etichetaAdresa;
        if(adreseArray.get(position).getAdresa().length() <= 10){
            etichetaAdresa = adreseArray.get(position).getAdresa();
        } else{
            etichetaAdresa = adreseArray.get(position).getAdresa().substring(0,10);
        }
        holder.radioBtn.setText(etichetaAdresa);
        holder.radioBtn.setChecked(adreseArray.get(position).getChecked());
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return adreseArray.size();
    }
}
