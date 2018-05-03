package nahdi.ghazi.insat.com.insat_biblio.Adapters;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import nahdi.ghazi.insat.com.insat_biblio.BookActivity;
import nahdi.ghazi.insat.com.insat_biblio.MyObjects.Book;
import nahdi.ghazi.insat.com.insat_biblio.R;

/**
 * Created by ghazi on 23/03/2018.
 */

public class BookAdapter extends RecyclerView.Adapter {



    Activity activity = null;
    private String URL = "https://insat-biblio.herokuapp.com/books/";


    private ArrayList<Book> books = null;


    public BookAdapter(Activity activity, List<Book> books){

        this.activity = activity;
        this.books = (ArrayList<Book>) books;
    }



    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.carte_livre,null);

        MyHolder myHolder = new MyHolder(view);

        return myHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {



    MyHolder myHolder = (MyHolder) holder;

    final Book book = books.get(position);


    final String _id = book._id;
    String titre = book.titre;
    final String url = URL+book.urlPhoto;
    final String title = book.titre;
    final String author = "Auteur : "+book.auteur;
    final String date = book.date_sortie;
    final String nbr = book.nbr_page+" pages";
    final String desc = book.description;




    if(book.titre.length() >= 20)
        titre = book.titre.substring(0,15)+"...";


    myHolder.textView.setText(titre);



        RequestOptions options = new RequestOptions()
                .centerCrop()
                .placeholder(R.mipmap.ic_launcher_round)
                .error(R.mipmap.ic_launcher_round);


        Glide.with(activity).load(url).apply(options).into(((MyHolder) holder).imageView);






        myHolder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                Intent intent = new Intent(activity, BookActivity.class);

                intent.putExtra("_id",_id);
                intent.putExtra("url",url);
                intent.putExtra("title",title);
                intent.putExtra("author",author);
                intent.putExtra("date",date);
                intent.putExtra("nbr",nbr);
                intent.putExtra("desc",desc);
                intent.putExtra("photo",book.urlPhoto);

                activity.startActivity(intent);
            }
        });


    }

    @Override
    public int getItemCount() {
        return this.books.size();
    }











    class MyHolder extends RecyclerView.ViewHolder{

        ImageView imageView = null;
        TextView textView = null;
        CardView cardView = null;

        public MyHolder(View itemView) {
            super(itemView);

        imageView = itemView.findViewById(R.id.bookImageCard);
        textView = itemView.findViewById(R.id.titleBookCard);
        cardView = itemView.findViewById(R.id.card_livre);

        Typeface typeface = Typeface.createFromAsset(activity.getAssets(),"fonts/Letters for Learners.ttf");
        textView.setTypeface(typeface);


        }



    }







}
