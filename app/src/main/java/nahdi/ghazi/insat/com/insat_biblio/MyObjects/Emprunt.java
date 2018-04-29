package nahdi.ghazi.insat.com.insat_biblio.MyObjects;

/**
 * Created by ghazi on 25/04/2018.
 */

public class Emprunt {


    public String dateDebut;
    public String dateFin;
    public String emailUser;
    public String _idBook;


    public Emprunt(String dateDebut, String dateFin, String emailUser, String _idBook) {
        this.dateDebut = dateDebut;
        this.dateFin = dateFin;
        this.emailUser = emailUser;
        this._idBook = _idBook;
    }
}
