package nahdi.ghazi.insat.com.insat_biblio.Services;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.RingtoneManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import nahdi.ghazi.insat.com.insat_biblio.MyObjects.UserEmail;
import nahdi.ghazi.insat.com.insat_biblio.MyObjects.UserGet;
import nahdi.ghazi.insat.com.insat_biblio.R;
import nahdi.ghazi.insat.com.insat_biblio.WebServices.UserServices;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class NotificationService extends Service {


    private Thread thread = null;
    private final String URL = "https://insat-biblio.herokuapp.com/";



    public NotificationService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {




        thread = new Thread(new ConnectionVerif());
        thread.start();

        return START_STICKY;
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        if(thread != null && thread.isAlive())
            thread.interrupt();

    }







    //verif cnx for notification and
    class ConnectionVerif implements Runnable{


        @Override
        public void run() {



            while (true){


                ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
                NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();


                if(networkInfo != null && networkInfo.getState() == NetworkInfo.State.CONNECTED){


                    runNotification();


                }


                try {
                    Thread.sleep(5000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }


            }





        }

        void runNotif(){




            Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);


            NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(getApplicationContext())
                    .setSmallIcon(R.drawable.logo_white)
                    .setContentTitle("Vérification de votre compte")
                    .setContentText("Votre compte est vérifié vous pouvez connecter sans probléme")
                    .setSound(alarmSound)
                    .setPriority(NotificationCompat.PRIORITY_DEFAULT);


            Notification notification = mBuilder.build();

            NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);


            notificationManager.notify(1,notification);

        }


        private void runNotification() {


            final SharedPreferences sp = getApplicationContext().getSharedPreferences("valid", MODE_PRIVATE);

            boolean state = sp.getBoolean("validate", false);

            if (!state) {

                String email = sp.getString("email", "");

                Gson gson = new GsonBuilder()
                        .setLenient()
                        .create();

                Retrofit retrofit = new Retrofit.Builder().baseUrl(URL).addConverterFactory(GsonConverterFactory.create(gson)).build();

                UserServices userServices = retrofit.create(UserServices.class);

                UserEmail userEmail = new UserEmail(email);

                Call<UserGet> call = userServices.getUser(userEmail);


                call.enqueue(new Callback<UserGet>() {
                    @Override
                    public void onResponse(Call<UserGet> call, Response<UserGet> response) {


                        UserGet userGet = response.body();




                        if(userGet != null && userGet.validate == true){

                            runNotif();

                            SharedPreferences.Editor editor = sp.edit();

                            editor.putBoolean("validate",true);
                            editor.putString("email","");

                            editor.commit();



                        }




                    }

                    @Override
                    public void onFailure(Call<UserGet> call, Throwable t) {

                        Log.e("hhhhh", t.getMessage());
                    }
                });


            }

        }








    }




}
