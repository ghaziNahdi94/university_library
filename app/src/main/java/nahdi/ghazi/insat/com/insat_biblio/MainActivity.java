package nahdi.ghazi.insat.com.insat_biblio;

import android.app.Notification;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatCheckBox;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;

import org.json.JSONObject;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import nahdi.ghazi.insat.com.insat_biblio.MyObjects.ResultCnx;
import nahdi.ghazi.insat.com.insat_biblio.MyObjects.User;
import nahdi.ghazi.insat.com.insat_biblio.MyObjects.UserEmail;
import nahdi.ghazi.insat.com.insat_biblio.MyObjects.UserEmailPassword;
import nahdi.ghazi.insat.com.insat_biblio.MyObjects.UserGet;
import nahdi.ghazi.insat.com.insat_biblio.Services.NotificationService;
import nahdi.ghazi.insat.com.insat_biblio.WebServices.UserServices;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {


    EditText email = null;
    EditText password = null;
    Button login = null;
    TextView forget = null;
    TextView signin = null;


    private final String URL = "https://insat-biblio.herokuapp.com/";




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



        if(restoreUser() != null){

            Intent intent = new Intent(MainActivity.this,MainPrincipalActivity.class);
            finish();
            startActivity(intent);

        }




        getSupportActionBar().setTitle("Authentifiez vous");



        email = findViewById(R.id.log_in_email);
        password = findViewById(R.id.log_in_password);
        login = findViewById(R.id.log_in_button);
        signin = findViewById(R.id.log_in_sign_in);


        Typeface typeface = Typeface.createFromAsset(getAssets(),"fonts/Letters for Learners.ttf");
        email.setTypeface(typeface);
        password.setTypeface(typeface);
        login.setTypeface(typeface);
        signin.setTypeface(typeface);







        signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(MainActivity.this,SignUpActivity.class);
                startActivity(intent);

            }
        });




        //Notif service
        Intent intent = new Intent(MainActivity.this, NotificationService.class);
        startService(intent);



    }


    @Override
    protected void onStart() {
        super.onStart();


        ifHuaweiAlert();



        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Gson gson = new GsonBuilder()
                        .setLenient()
                        .create();
                Retrofit retrofit = new Retrofit.Builder().baseUrl(URL).addConverterFactory(GsonConverterFactory.create(gson)).build();


                UserServices userServices = retrofit.create(UserServices.class);

                final String emailStr = email.getText().toString();
                String passwordStr = password.getText().toString();
                String passwordCrypte = Base64.encodeToString(passwordStr.getBytes(), Base64.DEFAULT);

                final UserEmailPassword userEmailPassword = new UserEmailPassword(emailStr, passwordCrypte);

                Call<ResultCnx> call = userServices.getUserByEmailPassword(userEmailPassword);

                call.enqueue(new Callback<ResultCnx>() {
                    @Override
                    public void onResponse(Call<ResultCnx> call, Response<ResultCnx> response) {

                        ResultCnx resultCnx = response.body();

                        if(resultCnx.ok){

                            UserGet userGet = resultCnx.user;

                            if(userGet.validate){


                                saveUser(userGet);

                                Intent intent = new Intent(MainActivity.this,MainPrincipalActivity.class);
                                startActivity(intent);

                            }else{

                                Intent intent = new Intent(MainActivity.this,VerificationActivity.class);
                                startActivity(intent);

                            }

                        }else {
                            Toast.makeText(MainActivity.this,"Email ou mot de passe invalide !",Toast.LENGTH_SHORT).show();
                        }


                    }

                    @Override
                    public void onFailure(Call<ResultCnx> call, Throwable t) {

                        Log.e("hhhhh1",t.getMessage());

                    }
                });





            }
        });
        }



        private void saveUser(UserGet userGet){

            SharedPreferences sp = getSharedPreferences("session",MODE_PRIVATE);
            SharedPreferences.Editor editor = sp.edit();

            Gson gson = new Gson();
            String json = gson.toJson(userGet);
            editor.putString("user",json);

            editor.commit();


        }




        private UserGet restoreUser(){

            Gson gson = new Gson();
            SharedPreferences sp = getSharedPreferences("session",MODE_PRIVATE);
            String json = sp.getString("user","");

            if(!json.equals(""))
            return gson.fromJson(json,UserGet.class);
            else
                return null;
        }









    private void ifHuaweiAlert() {
        final SharedPreferences settings = getSharedPreferences("ProtectedApps", MODE_PRIVATE);
        final String saveIfSkip = "skipProtectedAppsMessage";
        boolean skipMessage = settings.getBoolean(saveIfSkip, false);
        if (!skipMessage) {
            final SharedPreferences.Editor editor = settings.edit();
            Intent intent = new Intent();
            intent.setClassName("com.huawei.systemmanager", "com.huawei.systemmanager.optimize.process.ProtectActivity");
            if (isCallable(intent)) {
                final AppCompatCheckBox dontShowAgain = new AppCompatCheckBox(this);
                dontShowAgain.setText("Do not show again");
                dontShowAgain.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        editor.putBoolean(saveIfSkip, isChecked);
                        editor.apply();
                    }
                });

                new AlertDialog.Builder(this)
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .setTitle("Huawei Protected Apps")
                        .setMessage(String.format("L'application %s doit étre 'Protected Apps' pour fonctionner corréctement sur votre appareil.%n", getString(R.string.app_name)))
                        .setView(dontShowAgain)
                        .setPositiveButton("Protected Apps", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                huaweiProtectedApps();
                            }
                        })
                        .setNegativeButton(android.R.string.cancel, null)
                        .show();
            } else {
                editor.putBoolean(saveIfSkip, true);
                editor.apply();
            }
        }
    }


    private boolean isCallable(Intent intent) {
        List<ResolveInfo> list = getPackageManager().queryIntentActivities(intent,
                PackageManager.MATCH_DEFAULT_ONLY);
        return list.size() > 0;
    }



    private void huaweiProtectedApps() {
        try {
            String cmd = "am start -n com.huawei.systemmanager/.optimize.process.ProtectActivity";
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                cmd += " --user " + getUserSerial();
            }
            Runtime.getRuntime().exec(cmd);
        } catch (IOException ignored) {
        }
    }


    private String getUserSerial() {
        //noinspection ResourceType
        Object userManager = getSystemService(USER_SERVICE);
        if (null == userManager) return "";

        try {
            Method myUserHandleMethod = android.os.Process.class.getMethod("myUserHandle", (Class<?>[]) null);
            Object myUserHandle = myUserHandleMethod.invoke(android.os.Process.class, (Object[]) null);
            Method getSerialNumberForUser = userManager.getClass().getMethod("getSerialNumberForUser", myUserHandle.getClass());
            Long userSerial = (Long) getSerialNumberForUser.invoke(userManager, myUserHandle);
            if (userSerial != null) {
                return String.valueOf(userSerial);
            } else {
                return "";
            }
        } catch (NoSuchMethodException | IllegalArgumentException | InvocationTargetException | IllegalAccessException ignored) {
        }
        return "";
    }







}
