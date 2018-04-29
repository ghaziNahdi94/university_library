package nahdi.ghazi.insat.com.insat_biblio;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Typeface;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.FileNotFoundException;
import java.io.InputStream;

public class IdentityValidationActivity extends AppCompatActivity {

    TextView validText = null;
    Button valid = null;
    ImageView img = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_identity_validation);


        getSupportActionBar().setTitle("Valider votre identité");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        validText = findViewById(R.id.identity_valid_text);
        valid = findViewById(R.id.identity_valid_button);
        img = findViewById(R.id.identity_valid_img);


        Typeface typeface = Typeface.createFromAsset(getAssets(),"fonts/Letters for Learners.ttf");
        validText.setTypeface(typeface);
        valid.setTypeface(typeface);






        valid.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                ActivityCompat.requestPermissions(IdentityValidationActivity.this,
                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                        1);


            }
        });





        img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });




    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


        if(data != null && requestCode == 0) {
            setResult(0, data);
            valid.setVisibility(View.GONE);
            img.setVisibility(View.VISIBLE);
        }



    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }



    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case 1: {

                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.
                    Intent intent = new Intent(Intent.ACTION_PICK);
                    intent.setType("image/*");
                    startActivityForResult(intent, 0);

                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                    Toast.makeText(IdentityValidationActivity.this, "Permission denied to read your External storage", Toast.LENGTH_SHORT).show();
                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request
        }


    }












}
