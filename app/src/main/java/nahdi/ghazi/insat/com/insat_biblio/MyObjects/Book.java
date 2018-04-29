package nahdi.ghazi.insat.com.insat_biblio.MyObjects;


import java.sql.Date;

/**
 * Created by ghazi on 23/03/2018.
 */

public class Book {

    public String _id;

    public String titre;
    public String auteur;
    public String description;
    public String urlPhoto;
    public boolean disponible;
    public int nbr_page;
    public String langue;
    public String date_sortie;


    public Book(String titre, String auteur, String description, String urlPhoto, boolean disponible, int nbr_page, String langue, String date_sortie) {
        this.titre = titre;
        this.auteur = auteur;
        this.description = description;
        this.urlPhoto = urlPhoto;
        this.disponible = disponible;
        this.nbr_page = nbr_page;
        this.langue = langue;
        this.date_sortie = date_sortie;
    }













}
