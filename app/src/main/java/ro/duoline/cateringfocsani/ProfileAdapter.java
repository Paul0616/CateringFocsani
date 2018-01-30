package ro.duoline.cateringfocsani;

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
 * Created by Paul on 27.05.2017.
 */

public class ProfileAdapter extends RecyclerView.Adapter<ProfileAdapter.ViewHolder> {
    private Context context;
    private ArrayList<ProfileValue> profileArray;
    private AfisareProfile_Activity instance;

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

                    for(int i = 0; i< profileArray.size(); i++){
                        if(i == getAdapterPosition()) {
                            profileArray.get(i).setChecked(true);
                        } else {
                            profileArray.get(i).setChecked(false);
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
                    String[] strArray = new String[4];
                    strArray[0] = profileArray.get(getAdapterPosition()).getNume();
                    strArray[1] = profileArray.get(getAdapterPosition()).getEmail();
                    strArray[2] = profileArray.get(getAdapterPosition()).getTelefon();
                    strArray[3] = String.valueOf(getAdapterPosition());
                    instance.editProfil(strArray);
                }
            });
            deleteBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v){
                    profileArray.remove(getAdapterPosition());
                    //   if(profileArray.size() == 0){
                    //       instance.selecteaza.setEnabled(false);
                    //   }
                    JSONArray jsonArray = new JSONArray();

                    for(int i = 0; i< profileArray.size(); i++){
                        JSONObject json;
                        Map<String, Object> data;
                        data = new HashMap<String, Object>();
                        data.put("nume", profileArray.get(i).getNume());
                        data.put("email", profileArray.get(i).getEmail());
                        data.put("telefon", profileArray.get(i).getTelefon());
                        data.put("checked", profileArray.get(i).getChecked());
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
    public ProfileAdapter(Context context, AfisareProfile_Activity instance, ArrayList<ProfileValue> profile) {
        this.profileArray = profile;
        this.context = context;
        this.instance = instance;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public ProfileAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // create a new view
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.listview_element_adapter, parent, false);
        // set the view's size, margins, paddings and layout parameters
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ProfileAdapter.ViewHolder holder, int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        holder.radioBtn.setText(profileArray.get(position).getNume()+"|"+profileArray.get(position).getTelefon());
        holder.radioBtn.setChecked(profileArray.get(position).getChecked());
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return profileArray.size();
    }
}
