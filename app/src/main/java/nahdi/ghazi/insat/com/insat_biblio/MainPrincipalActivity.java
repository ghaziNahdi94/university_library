package nahdi.ghazi.insat.com.insat_biblio;

import android.app.AlertDialog;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;


import java.io.File;

import io.realm.Realm;
import io.realm.RealmResults;
import nahdi.ghazi.insat.com.insat_biblio.Fragments.BooksFragment;
import nahdi.ghazi.insat.com.insat_biblio.Fragments.HistoryFragment;
import nahdi.ghazi.insat.com.insat_biblio.MyObjects.History;

public class MainPrincipalActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {



    TextView toolbar_title;
    ImageView toolbar_icon;


    private BooksFragment booksFragment = new BooksFragment();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_principal);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        toolbar_title = findViewById(R.id.toolbar_title);
        toolbar_icon = findViewById(R.id.toolbar_icon);


        toolbar_title.setText("Les livres");


        Typeface typeface = Typeface.createFromAsset(getAssets(),"fonts/Letters for Learners.ttf");
        toolbar_title.setTypeface(typeface);

        Realm.init(getApplicationContext());





        File file = new File(getFilesDir()+"/history/");
        if(!file.exists())
            file.mkdir();




        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);



        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.FrameContainer,booksFragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();

    }





    @Override
    protected void onStart() {
        super.onStart();


        toolbar_icon.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {


                if(motionEvent.getAction() == MotionEvent.ACTION_DOWN){

                    toolbar_icon.setBackgroundColor(Color.WHITE);

                    AlertDialog.Builder dialog = new AlertDialog.Builder(MainPrincipalActivity.this);
                    dialog.setTitle("Chercher...");
                    dialog.setIcon(R.drawable.s3);

                    View v = LayoutInflater.from(MainPrincipalActivity.this).inflate(R.layout.dialog_search,null);
                    final EditText editText = v.findViewById(R.id.search);
                    final Button button = v.findViewById(R.id.button_search);
                    dialog.setView(v);

                    button.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            booksFragment.getBooks("");
                        }
                    });

                    editText.addTextChangedListener(new TextWatcher() {
                        @Override
                        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                        }

                        @Override
                        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                            booksFragment.getBooks(charSequence.toString());
                        }

                        @Override
                        public void afterTextChanged(Editable editable) {

                        }
                    });

                    dialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {

                            booksFragment.getBooks(editText.getText().toString());
                        }
                    });
                    dialog.show();

                }else{

                    toolbar_icon.setBackgroundColor(Color.TRANSPARENT);


                }



                return true;
            }
        });


    }









    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }



    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {

        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();


        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_books) {

            fragmentTransaction.replace(R.id.FrameContainer,booksFragment);
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commit();

            toolbar_icon.setVisibility(View.VISIBLE);
            toolbar_title.setText("Les livres");


        } else if (id == R.id.nav_history) {


            fragmentTransaction.replace(R.id.FrameContainer, new HistoryFragment());
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commit();

            toolbar_icon.setVisibility(View.GONE);
            toolbar_title.setText("Historique");


        }


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
