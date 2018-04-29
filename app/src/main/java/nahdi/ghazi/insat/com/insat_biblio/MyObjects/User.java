package nahdi.ghazi.insat.com.insat_biblio.MyObjects;

import java.io.Serializable;

/**
 * Created by ghazi on 04/04/2018.
 */

public class User implements Serializable{


    public String email;
    public String password;
    public String nom;
    public String prenom;
    public int cin;
    public int numCard;
    public boolean validate;
    public String fileCard;
    public int emprunt;

    public User(String email, String password, String nom, String prenom, int cin, int numCard,String fileCard) {
        this.email = email;
        this.password = password;
        this.nom = nom;
        this.prenom = prenom;
        this.cin = cin;
        this.numCard = numCard;


        this.fileCard = fileCard;
        this.validate = false;
        this.emprunt = 0;
    }
}
