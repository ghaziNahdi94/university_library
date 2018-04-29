package nahdi.ghazi.insat.com.insat_biblio.WebServices;

import java.util.List;

import nahdi.ghazi.insat.com.insat_biblio.MyObjects.Book;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;

/**
 * Created by ghazi on 26/03/2018.
 */

public interface BookService {




    @GET("/livres")
    Call<List<Book>> getBooks();







}
