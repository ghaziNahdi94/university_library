package nahdi.ghazi.insat.com.insat_biblio.Adapters;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.util.ArrayList;

import nahdi.ghazi.insat.com.insat_biblio.MyObjects.History;
import nahdi.ghazi.insat.com.insat_biblio.R;

/**
 * Created by ghazi on 26/04/2018.
 */

public class HistoryAdapter extends RecyclerView.Adapter {


    private ArrayList<History> histories = null;
    private Activity activity = null;

    private String PATH;

    public HistoryAdapter(Activity activity, ArrayList<History> histories){

        this.histories = histories;
        this.activity = activity;

        this.PATH = activity.getFilesDir()+"/history";


    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.history_books,null);

        HistoryViewHolder historyViewHolder = new HistoryViewHolder(view);

        return historyViewHolder;

    }



    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        HistoryViewHolder historyViewHolder = (HistoryViewHolder) holder;

        History history = this.histories.get(position);


        Bitmap bitmap = BitmapFactory.decodeFile(this.PATH+"/"+history.path+".jpg");
        historyViewHolder.imageView.setImageBitmap(bitmap);
        historyViewHolder.title.setText(history.title);
        historyViewHolder.de.setText("de : "+history.de);
        historyViewHolder.a.setText("à : "+history.a);


    }



    @Override
    public int getItemCount() {
        return this.histories.size();
    }





    class HistoryViewHolder extends RecyclerView.ViewHolder{

        ImageView imageView;
        TextView title;
        TextView de;
        TextView a;


        public HistoryViewHolder(View itemView) {
            super(itemView);


            imageView = itemView.findViewById(R.id.history_img);
            title = itemView.findViewById(R.id.history_title);
            de = itemView.findViewById(R.id.history_de);
            a = itemView.findViewById(R.id.history_a);


        }


    }



}
