package nahdi.ghazi.insat.com.insat_biblio.Fragments;


import android.os.Bundle;
import android.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmResults;
import nahdi.ghazi.insat.com.insat_biblio.Adapters.HistoryAdapter;
import nahdi.ghazi.insat.com.insat_biblio.MyObjects.History;
import nahdi.ghazi.insat.com.insat_biblio.R;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * A simple {@link Fragment} subclass.
 */
public class HistoryFragment extends Fragment {


    RecyclerView recyclerView;


    Realm realm = null;


    public HistoryFragment() {

        realm = Realm.getDefaultInstance();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_history, container, false);

        setHasOptionsMenu(false);


        recyclerView = view.findViewById(R.id.recyclerViewHistories);

        LinearLayoutManager llm = new LinearLayoutManager(getActivity());
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(llm);




        RealmResults<History> results = realm.where(History.class).findAll();




        ArrayList<History> histories = new ArrayList<>(results);




        HistoryAdapter historyAdapter = new HistoryAdapter(getActivity(),histories);
        recyclerView.setAdapter(historyAdapter);

        return view;
    }

}
