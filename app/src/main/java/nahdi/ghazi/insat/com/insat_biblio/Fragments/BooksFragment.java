package nahdi.ghazi.insat.com.insat_biblio.Fragments;


import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.app.Fragment;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;
import java.util.TimerTask;

import nahdi.ghazi.insat.com.insat_biblio.Adapters.BookAdapter;
import nahdi.ghazi.insat.com.insat_biblio.MyObjects.Book;
import nahdi.ghazi.insat.com.insat_biblio.R;
import nahdi.ghazi.insat.com.insat_biblio.WebServices.BookService;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * A simple {@link Fragment} subclass.
 */
public class BooksFragment extends Fragment {


    private final String URL = "https://insat-biblio.herokuapp.com/livres/";

    RecyclerView recyclerView = null;
    LinearLayout noCnx = null;

    private Thread cnxVerif = null;
    private boolean lastStateIsCnx = false;


    /********* Handlers *********/
    private Handler cnxOn = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            noCnx.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);
        }
    };

    private Handler cnxOff = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            recyclerView.setVisibility(View.GONE);
            noCnx.setVisibility(View.VISIBLE);
        }
    };

    public BooksFragment() {




    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        View view = inflater.inflate(R.layout.fragment_books, container, false);


        recyclerView = view.findViewById(R.id.recyclerViewBooks);
        noCnx = view.findViewById(R.id.noCnx);



        //Check connection state
        cnxVerif = new Thread(new CnxVerif());


        getBooks();


        return view;
    }



    public void getBooks(final String str){

        Retrofit retrofit = new Retrofit.Builder().baseUrl(URL).addConverterFactory(GsonConverterFactory.create()).build();

        BookService bookService = retrofit.create(BookService.class);


        Call<List<Book>> call =  bookService.getBooks();


        call.enqueue(new Callback<List<Book>>() {
            @Override
            public void onResponse(Call<List<Book>> call, Response<List<Book>> response) {

                List<Book> list = response.body();

                ArrayList<Book> newList = new ArrayList<>();

                if(!str.equals("")) {
                    for (Book book : list) {

                        if (book.titre.toUpperCase().startsWith(str.toUpperCase()))
                            newList.add(book);
                    }
                }else{
                    newList = (ArrayList<Book>) list;
                }



                recyclerView.setLayoutManager(new GridLayoutManager(getActivity(),2));

                BookAdapter bookAdapter = new BookAdapter(getActivity(),newList);

                recyclerView.setAdapter(bookAdapter);


            }

            @Override
            public void onFailure(Call<List<Book>> call, Throwable t) {
                Log.e("hhhhhh",t.getMessage());
            }
        });


    }

    private void getBooks(){

        Retrofit retrofit = new Retrofit.Builder().baseUrl(URL).addConverterFactory(GsonConverterFactory.create()).build();

        BookService bookService = retrofit.create(BookService.class);


        Call<List<Book>> call =  bookService.getBooks();


        call.enqueue(new Callback<List<Book>>() {
            @Override
            public void onResponse(Call<List<Book>> call, Response<List<Book>> response) {

                List<Book> list = response.body();


                recyclerView.setLayoutManager(new GridLayoutManager(getActivity(),2));

                BookAdapter bookAdapter = new BookAdapter(getActivity(),list);

                recyclerView.setAdapter(bookAdapter);


            }

            @Override
            public void onFailure(Call<List<Book>> call, Throwable t) {
                Log.e("hhhhhh",t.getMessage());
            }
        });


    }



    @Override
    public void onStart() {
        super.onStart();

        if(cnxVerif != null && !cnxVerif.isAlive())
        cnxVerif.start();

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if(cnxVerif != null && cnxVerif.isAlive())
        cnxVerif.interrupt();
    }

    class CnxVerif implements Runnable{

        @Override
        public void run() {


            while (true && getActivity() != null) {




                    ConnectivityManager cm = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
                    NetworkInfo networkInfo = cm.getActiveNetworkInfo();

                    if (networkInfo != null && networkInfo.isConnected()) {

                        if(!lastStateIsCnx) {

                            Log.e("hhhh","1");

                        cnxOn.sendEmptyMessage(0);

                        getBooks();

                        lastStateIsCnx = true;

                    }

                } else {

                    cnxOff.sendEmptyMessage(0);
                    lastStateIsCnx = false;

                        Log.e("hhhh","2");


                    }



                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

            }
        }

    }


}
