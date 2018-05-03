package nahdi.ghazi.insat.com.insat_biblio;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;

import nahdi.ghazi.insat.com.insat_biblio.MyObjects.PathUtil;
import nahdi.ghazi.insat.com.insat_biblio.MyObjects.User;
import nahdi.ghazi.insat.com.insat_biblio.MyObjects.UserEmail;
import nahdi.ghazi.insat.com.insat_biblio.WebServices.UserServices;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class SignUpActivity extends AppCompatActivity {


    private final String URL = "https://insat-biblio.herokuapp.com/";



    EditText email = null;
    EditText password = null;
    EditText confirmPassword = null;
    EditText nom = null;
    EditText prenom = null;
    EditText numCin = null;
    EditText numCarteEtudiant = null;
    Button validIdentity = null;
    Button valid = null;
    ImageView img = null;



    private Intent dataCard = null;
    private String globalError = "";



    private ProgressDialog progressDialog = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);



        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("S'inscrire");



        email = findViewById(R.id.sign_in_email);
        password = findViewById(R.id.sign_in_password);
        confirmPassword = findViewById(R.id.sign_in_password_confirm);
        nom = findViewById(R.id.sign_in_nom);
        prenom = findViewById(R.id.sign_in_prenom);
        numCin = findViewById(R.id.sign_in_cin);
        numCarteEtudiant = findViewById(R.id.sign_in_carte_etudiant);
        validIdentity = findViewById(R.id.sign_in_valid_identity);
        valid = findViewById(R.id.sign_in_valid);
        img = findViewById(R.id.sign_in_valid_img);


        Typeface typeface = Typeface.createFromAsset(getAssets(),"fonts/Letters for Learners.ttf");
        email.setTypeface(typeface);
        password.setTypeface(typeface);
        confirmPassword.setTypeface(typeface);
        nom.setTypeface(typeface);
        prenom.setTypeface(typeface);
        numCin.setTypeface(typeface);
        numCarteEtudiant.setTypeface(typeface);
        validIdentity.setTypeface(typeface);
        valid.setTypeface(typeface);



        validIdentity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SignUpActivity.this,IdentityValidationActivity.class);
                startActivityForResult(intent,0);
            }
        });




        img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                AlertDialog.Builder dialog = new AlertDialog.Builder(SignUpActivity.this);

                dialog.setTitle("Verification");
                dialog.setMessage("Etes vous sur de conserver cette photo de votre carte d'étudiant ?");
                dialog.setPositiveButton("Oui", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                    dialogInterface.dismiss();
                    }
                });

                dialog.setNegativeButton("Non", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dataCard = null;
                        img.setVisibility(View.GONE);
                        validIdentity.setVisibility(View.VISIBLE);
                        dialogInterface.dismiss();
                    }
                });

                dialog.show();

            }
        });




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
    protected void onStart() {
        super.onStart();


        valid.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

             progressDialog = new ProgressDialog(SignUpActivity.this);
             progressDialog.setCancelable(false);
             progressDialog.setMessage("Loading...");
             progressDialog.show();


             globalError = "";

             String emailStr = email.getText().toString();
             String passwordStr = password.getText().toString();
             String confirmStr = confirmPassword.getText().toString();
             String nomStr = nom.getText().toString();
             String prenomStr = prenom.getText().toString();
             String numCinStr = numCin.getText().toString();
             String numCardStr = numCarteEtudiant.getText().toString();


             if(validData(dataCard) && validEmail(emailStr) && validPassword(passwordStr) && validConfirm(passwordStr,confirmStr) && validNom(nomStr) && validPrenom(prenomStr)&& validCin(numCinStr) && validCarteEtude(numCardStr)) {

                 int numCinInt = Integer.parseInt(numCinStr);
                 int numCardInt = Integer.parseInt(numCardStr);
                 String passworCrypte = Base64.encodeToString(passwordStr.getBytes(),Base64.DEFAULT);
                 sendIfNoexistingEmail(dataCard,emailStr,passworCrypte,nomStr,prenomStr,numCinInt,numCardInt);

             }else{

                 if(progressDialog != null && progressDialog.isShowing())
                     progressDialog.dismiss();

                 AlertDialog.Builder errorDialog = new AlertDialog.Builder(SignUpActivity.this);

                 errorDialog.setTitle("Erreur");
                 errorDialog.setIcon(R.drawable.err);
                 errorDialog.setMessage(globalError);
                 errorDialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                     @Override
                     public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                     }
                 });

                 errorDialog.show();

             }

            }
        });


    }



    private void sendIfNoexistingEmail(final Intent dataCard, final String emailStr, final String passworCrypte, final String nomStr, final String prenomStr, final int numCinInt, final int numCardInt){



        Gson gson = new GsonBuilder()
                .setLenient()
                .create();
        Retrofit retrofit = new Retrofit.Builder().baseUrl(URL).addConverterFactory(GsonConverterFactory.create(gson)).build();


        UserServices userServices = retrofit.create(UserServices.class);

        UserEmail userEmail = new UserEmail(emailStr);

        Call<ResponseBody> call = userServices.getUserByEmail(userEmail);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                if(progressDialog != null && progressDialog.isShowing())
                    progressDialog.dismiss();

                try {
                    String rep =  response.body().string();

                    if(rep.equals("nothing"))
                        createNewUser(dataCard,emailStr,passworCrypte,nomStr,prenomStr,numCinInt,numCardInt);
                        else
                        Toast.makeText(SignUpActivity.this,"Email déja utilisé !",Toast.LENGTH_SHORT).show();


                } catch (IOException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                if(progressDialog != null && progressDialog.isShowing())
                    progressDialog.dismiss();
            }
        });

    }


    private boolean validData(Intent data){

        boolean ok = data != null;

        if(!ok)
            globalError += "Validez votre identité Svp\n";

        return ok;

    }

    private boolean validEmail(String email){



        String regularExp = "(?:[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*|\"(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21\\x23-\\x5b\\x5d-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])*\")@(?:(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?|\\[(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?|[a-z0-9-]*[a-z0-9]:(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21-\\x5a\\x53-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])+)\\])";

        boolean ok =  email.matches(regularExp);

        if(!ok)
            globalError += "Votre email est invalide\n";

        return ok;
    }


    private boolean validPassword(String password){

        boolean ok = false;

        if(password.length() >= 8)
            ok = true;



        if(!ok)
            globalError += "Le mot de passe doit contenir 8 caractéres au minimum\n";

            return ok;
    }


    private boolean validConfirm(String pass,String confirm){

        boolean ok = false;

        if(pass.equals(confirm))
            ok = true;

        if(!ok)
            globalError += "Les mots de passe ne sont pas identiques\n";

        return ok;
    }


    private boolean validNom(String nom){

        boolean ok = false;

        if(nom.length() >= 3)
            ok = true;

        if(!ok)
            globalError += "Le Nom doit contenir au moin 4 caractéres";

        return ok;
    }


    private boolean validPrenom(String nom){

        boolean ok = false;

        if(nom.length() >= 3 )
            ok = true;

        if(!ok)
            globalError += "Le Prénom doit contenir au moin 4 caractéres";

        return ok;
    }




    private boolean validCin(String cin){

        boolean ok = true;

        try {
            Integer.parseInt(cin);

        }catch (Exception e){
            ok = false;
        }


        if(ok) {
            if (cin.length() != 8)
                ok = false;
        }


        if(!ok)
            globalError += "Votre numéro CIN n'est pas valide\n";

        return ok;
    }


    private boolean validCarteEtude(String c){

        boolean ok = true;

        try {
            Integer.parseInt(c);
        }catch (Exception e){
            ok = false;
        }


        if(ok) {
            if (c.length() != 7)
                ok = false;
        }



        if(!ok)
            globalError += "Votre numéro de carte d'étudiant n'est pas valide";


        return ok;
    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


        if(data != null && resultCode == 0){

            validIdentity.setVisibility(View.GONE);
            img.setVisibility(View.VISIBLE);

           this.dataCard = data;

        }


    }




    void createNewUser(Intent data, final String emailStr, final String passwordStr, final String nomStr, final String prenomStr, final int numCinInt, final int numCardInt){
        Gson gson = new GsonBuilder()
                .setLenient()
                .create();

        Retrofit retrofit = new Retrofit.Builder().baseUrl(URL).addConverterFactory(GsonConverterFactory.create(gson)).build();

        File file = null;
        try {
            file = new File(PathUtil.getPath(SignUpActivity.this,data.getData()));
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }

        RequestBody fileBody = RequestBody.create(MediaType.parse("image/*"),file);
        MultipartBody.Part body = MultipartBody.Part.createFormData("key", file.getName(), fileBody);


        UserServices userServices = retrofit.create(UserServices.class);


        Call<ResponseBody> call = userServices.sendImage(body);

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    String fileCard = response.body().string();
                    User user = new User(emailStr,passwordStr,nomStr,prenomStr,numCinInt,numCardInt,fileCard);

                    Retrofit retrofit = new Retrofit.Builder().baseUrl(URL).addConverterFactory(GsonConverterFactory.create()).build();
                    UserServices userServices = retrofit.create(UserServices.class);

                    Call<User> call1 = userServices.createUser(user);
                    call1.enqueue(new Callback<User>() {
                        @Override
                        public void onResponse(Call<User> call, Response<User> response) {

                            AlertDialog.Builder popup = new AlertDialog.Builder(SignUpActivity.this);
                            popup.setCancelable(false);
                            popup.setTitle("Opération réussite");
                            popup.setMessage("L'inscription est terminé avec succés");
                            popup.setNegativeButton("OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    Intent intent = new Intent(SignUpActivity.this,VerificationActivity.class);
                                    finish();
                                    intent.putExtra("email",emailStr);
                                    startActivity(intent);
                                }
                            });

                            popup.show();

                        }
                        @Override
                        public void onFailure(Call<User> call, Throwable t) {

                            AlertDialog.Builder popup = new AlertDialog.Builder(SignUpActivity.this);
                            popup.setCancelable(false);
                            popup.setTitle("Opération réussite");
                            popup.setMessage("L'inscription est terminé avec succés");
                            popup.setNegativeButton("OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    Intent intent = new Intent(SignUpActivity.this,VerificationActivity.class);
                                    finish();
                                    intent.putExtra("email",emailStr);
                                    startActivity(intent);
                                }
                            });

                            popup.show();
                        }
                    });



                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.e("hhhhh",t.getMessage()+" 2");
            }
        });
    }














}
