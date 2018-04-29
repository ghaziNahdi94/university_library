package nahdi.ghazi.insat.com.insat_biblio;

import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.concurrent.ExecutionException;


import io.realm.Realm;
import nahdi.ghazi.insat.com.insat_biblio.MyObjects.Emprunt;
import nahdi.ghazi.insat.com.insat_biblio.MyObjects.History;
import nahdi.ghazi.insat.com.insat_biblio.WebServices.UserServices;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class BookActivity extends AppCompatActivity {


    ImageView book_img;
    TextView title;
    TextView author;
    TextView date;
    TextView pages;

    TextView description;
    Button valid;



    private String _id = "";
    private String titleStr = "";
    private String url = "";
    private String url_photo = "";

    private final String URL = "http://10.0.2.2:8080/";

    private  String PATH;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book);


        PATH = getFilesDir()+"/history";


        book_img = findViewById(R.id.book_img);
        title = findViewById(R.id.book_title);
        author = findViewById(R.id.book_author);
        date = findViewById(R.id.book_date);
        pages = findViewById(R.id.book_nbr);
        description = findViewById(R.id.book_desc);
        valid = findViewById(R.id.book_valid);


        Intent intent = getIntent();

        _id = intent.getStringExtra("_id");
        url = intent.getStringExtra("url");
        url_photo = intent.getStringExtra("photo");
        titleStr = intent.getStringExtra("title");
        String authorStr = intent.getStringExtra("author");
        String dateStr = intent.getStringExtra("date");
        String nbrStr = intent.getStringExtra("nbr");
        String descStr = intent.getStringExtra("desc");


        String actionBarTitle = "";
        if(actionBarTitle.length() >= 30)
            actionBarTitle = titleStr.substring(0,30)+"...";
        else
            actionBarTitle = titleStr;


        getSupportActionBar().setTitle(actionBarTitle);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        RequestOptions options = new RequestOptions()
                .centerCrop()
                .placeholder(R.mipmap.ic_launcher_round)
                .error(R.mipmap.ic_launcher_round);


        Glide.with(this).load(url).apply(options).into(book_img);


        title.setText(titleStr);
        author.setText(authorStr);
        date.setText(dateStr);
        pages.setText(nbrStr);
        description.setText(descStr);




        Typeface typeface = Typeface.createFromAsset(getAssets(),"fonts/Letters for Learners.ttf");

        valid.setTypeface(typeface);


        Realm.init(BookActivity.this);
    }


    @Override
    protected void onStart() {
        super.onStart();


        valid.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                AlertDialog.Builder dialog = new AlertDialog.Builder(BookActivity.this);


                dialog.setTitle("Choisir la période");

                View v = LayoutInflater.from(BookActivity.this).inflate(R.layout.dialog_confirm_emprunt,null);

                final EditText e1 = v.findViewById(R.id.de);
                final EditText e2 = v.findViewById(R.id.a);


                dialog.setView(v);


                e1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        setDatePicker(e1);

                    }
                });

                e2.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        setDatePicker(e2);

                    }
                });


                dialog.setNegativeButton("Annuler", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                });


                dialog.setPositiveButton("Valider", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        String date1 = e1.getText().toString();
                        String date2 = e2.getText().toString();

                        final Emprunt emprunt = new Emprunt(date1,date2,"ghaziNahdi94@outlook.com",_id);



                        Gson gson = new GsonBuilder().setLenient().create();
                        Retrofit retrofit = new Retrofit.Builder().baseUrl(URL).addConverterFactory(GsonConverterFactory.create(gson)).build();
                        UserServices userServices = retrofit.create(UserServices.class);


                        Call<ResponseBody> call = userServices.createBorrow(emprunt);
                        call.enqueue(new Callback<ResponseBody>() {
                            @Override
                            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                                valid.setText("Livre emprunté");
                                valid.setEnabled(false);


                                History history = new History(titleStr,emprunt.dateDebut,emprunt.dateFin,url_photo);

                                setNewHistory(history);


                            }

                            @Override
                            public void onFailure(Call<ResponseBody> call, Throwable t) {
                                Log.e("hhhhhhh2",t.getMessage());
                            }
                        });

                        dialogInterface.dismiss();
                    }
                });



                dialog.show();

            }
        });


    }




    private void setNewHistory(final History history){

        Realm realm = Realm.getDefaultInstance();



        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Bitmap bitmap = Glide.with(BookActivity.this).asBitmap().load(url).into(100,100).get();
                    FileOutputStream fos = new FileOutputStream(PATH+"/"+history.path+".jpg");
                    bitmap.compress(Bitmap.CompressFormat.JPEG,100,fos);
                    fos.close();
                    Log.e("hhhhh",""+new File(PATH+"/"+history.path+".jpg").exists());
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();


        realm.beginTransaction();
        realm.copyToRealm(history);
        realm.commitTransaction();



    }


    private void setDatePicker(final EditText editText){

        final Calendar calendar = Calendar.getInstance();
        DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {

                calendar.set(Calendar.YEAR,i);
                calendar.set(Calendar.MONTH,i1);
                calendar.set(Calendar.DAY_OF_MONTH,i2);


                String dateFormat = "dd/MM/yyyy";
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat(dateFormat);


                editText.setText(simpleDateFormat.format(calendar.getTime()));


            }
        };





         new DatePickerDialog(BookActivity.this,date,calendar.get(Calendar.YEAR),calendar.get(Calendar.MONTH),calendar.get(Calendar.DAY_OF_MONTH)).show();









    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {


        switch (item.getItemId()){

            case android.R.id.home :
                onBackPressed();
                break;

        }



        return super.onOptionsItemSelected(item);



    }
}
