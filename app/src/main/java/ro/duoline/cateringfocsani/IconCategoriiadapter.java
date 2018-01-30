package ro.duoline.cateringfocsani;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by Paul on 28.05.2017.
 */

public class IconCategoriiadapter extends RecyclerView.Adapter<IconCategoriiadapter.ViewHolder> {
    private ArrayList<String> categoriiArray;
    //private Context context;
    private FeluriMeniu_Activity mInstance;

    public IconCategoriiadapter(FeluriMeniu_Activity mInstance, ArrayList<String> categoriiArray ){
        this.categoriiArray = new ArrayList<String>();
        this.categoriiArray.addAll(categoriiArray);
        this.mInstance = mInstance;
    }

    @Override
    public IconCategoriiadapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.grid_categorii_produse, viewGroup, false);
        return new IconCategoriiadapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(IconCategoriiadapter.ViewHolder viewHolder, int pos) {

        String uri = categoriiArray.get(pos).toLowerCase();
        uri = uri.replace(" ", "_");
        uri = uri.replace("/", "_");
        uri = uri.replace(".", "");
        /*
        if(categoriiArray.get(pos).equals("supe_/_ciorbe")){

            uri = "ciorbe";
        }
        if(categoriiArray.get(pos).equals("produse_neafum.")){

            uri = "produse_neafum";
        }
        */
        viewHolder.denumIcon.setText(categoriiArray.get(pos).toString());


        Picasso.with(mInstance.getApplicationContext()).load("http://www.ondesign.ro/catering/imagesIconCategorii/"+uri+".png")
                .error(R.drawable.general)
                .memoryPolicy(MemoryPolicy.NO_CACHE, MemoryPolicy.NO_STORE)
                .networkPolicy(NetworkPolicy.NO_CACHE)
                .into(viewHolder.iconCategorie);


    }

    @Override
    public int getItemCount() {
        return (null != categoriiArray ? categoriiArray.size() : 0);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private ImageView iconCategorie;
        private TextView denumIcon;

        public ViewHolder(View view) {
            super(view);

            iconCategorie = (ImageView) view.findViewById(R.id.icon_categorie);
            denumIcon = (TextView) view.findViewById(R.id.textDenIcon);
            iconCategorie.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v){
                    String fltr = categoriiArray.get(getAdapterPosition()).substring(0,1).toUpperCase() + categoriiArray.get(getAdapterPosition()).substring(1);
                    fltr = fltr.replaceAll("_", " ").toUpperCase();
                    mInstance.setAdapterFilter(fltr);
                }
            });

        }
    }
}
