package nahdi.ghazi.insat.com.insat_biblio.MyObjects;

import java.io.Serializable;

/**
 * Created by ghazi on 28/04/2018.
 */

public class UserGet extends User implements Serializable {

    public String _id;

    public UserGet(String email, String password, String nom, String prenom, int cin, int numCard, String fileCard) {
        super(email, password, nom, prenom, cin, numCard, fileCard);
    }
}
