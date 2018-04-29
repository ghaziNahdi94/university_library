package nahdi.ghazi.insat.com.insat_biblio.MyObjects;


import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by ghazi on 26/04/2018.
 */

public class History extends RealmObject{


    public String title;
    public String de;
    public String a;
    public String path;


    public History(){}

    public History(String title, String de, String a, String path) {
        this.title = title;
        this.de = de;
        this.a = a;
        this.path = path;
    }
}
