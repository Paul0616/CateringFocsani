package ro.duoline.papacatering;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by Paul on 28.05.2017.
 */

public class categoriiAdapter extends RecyclerView.Adapter<categoriiAdapter.ViewHolder> {
    private ArrayList<CategoriiMeniuValues> categorii;
    private Context context;
    private CategoriiMeniu_Activity mInstance;

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        // each data item is just a string in this case
        private TextView text_categorii;
        private ImageView img_categorii;


        public ViewHolder(View view) {
            super(view);
            view.setOnClickListener(this);
            text_categorii = (TextView)view.findViewById(R.id.grid_text);
            img_categorii = (ImageView) view.findViewById(R.id.grid_image);
        }

        @Override
        public void onClick(View view) {
            mInstance.callFelMeniuActivity(categorii.get(getAdapterPosition()).getCategorii_meniu_nume());
            // Toast.makeText(view.getContext(), "URL = " + categorii.get(getAdapterPosition()).getCategorii_meniu_image_url(), Toast.LENGTH_LONG).show();
        }
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public categoriiAdapter(Context context, CategoriiMeniu_Activity mInstance, ArrayList<CategoriiMeniuValues> categorii) {
        this.categorii = categorii;
        this.context = context;
        this.mInstance = mInstance;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public categoriiAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // create a new view
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.grid_text_image, parent, false);
        // set the view's size, margins, paddings and layout parameters
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(categoriiAdapter.ViewHolder holder, int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        holder.text_categorii.setText(categorii.get(position).getCategorii_meniu_nume());
        Picasso.with(context).load(categorii.get(position).getCategorii_meniu_image_url()).into(holder.img_categorii);
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return categorii.size();
    }
}
