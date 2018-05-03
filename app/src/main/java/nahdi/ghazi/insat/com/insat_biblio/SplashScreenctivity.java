package nahdi.ghazi.insat.com.insat_biblio;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class SplashScreenctivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_spash_screenctivity);

        this.getSupportActionBar().hide();

        new Thread(new Runnable() {
            @Override
            public void run() {


                try {
                    Thread.sleep(5000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }


                Intent intent = new Intent(SplashScreenctivity.this,MainActivity.class);
                finish();
                startActivity(intent);
            }
        }).start();



    }
}
