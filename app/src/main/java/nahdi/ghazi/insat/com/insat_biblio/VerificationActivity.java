package nahdi.ghazi.insat.com.insat_biblio;

import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class VerificationActivity extends AppCompatActivity {

    TextView verifText = null;
    Button retour = null;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verification);



        getSupportActionBar().setTitle("Vérification en cours");


        verifText = findViewById(R.id.verification_text);
        retour = findViewById(R.id.verification_button);


        Typeface typeface = Typeface.createFromAsset(getAssets(),"fonts/Letters for Learners.ttf");

        verifText.setTypeface(typeface);
        retour.setTypeface(typeface);



        retour.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });


        String email = getIntent().getStringExtra("email");

        SharedPreferences sharedPreferences = this.getSharedPreferences("valid",MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean("validate",false);
        editor.putString("email",email);
        editor.commit();







    }
}
