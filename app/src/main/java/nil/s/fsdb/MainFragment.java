package nil.s.fsdb;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Locale;

public class MainFragment extends Fragment {

    /*==============================================================================================
        Declaração dos objetos
     =============================================================================================*/
    private final String TAG = "MainActivity"; //TAG para os logs

    private RecyclerView recyclerViewFilme;

    private AdaptadorFilme adaptadorFilme;

    private ArrayList<ItemFilme> itemFilmesList;

    private RequestQueue requestQueue;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.main_fragment, container, false);
        /*==========================================================================================
            Criação dos objetos
         =========================================================================================*/
        recyclerViewFilme = rootView.findViewById(R.id.recyclerViewMainActivity);
        recyclerViewFilme.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        //linearLayoutManager.setOrientation(RecyclerView.HORIZONTAL);
        recyclerViewFilme.setLayoutManager(linearLayoutManager);

        itemFilmesList = new ArrayList<>();

        requestQueue = Volley.newRequestQueue(getActivity());
        parseJSON();

        return rootView;
    }

    private void parseJSON() {

        String language = Locale.getDefault().getLanguage() + "-" + Locale.getDefault().getLanguage().toUpperCase();
        Log.d(TAG, "lang - " + language);
        
        String url = "https://api.themoviedb.org/3/movie/upcoming?api_key=c816441df0108db98214080d85446617&region=PT&language=" + language;

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONArray jsonArray = response.getJSONArray("results");

                            for(int i = 0;i < jsonArray.length();i++){
                                JSONObject result = jsonArray.getJSONObject(i);

                                String nome = result.getString("title");
                                String image = result.getString("poster_path");
                                String data = result.getString("release_date");
                                itemFilmesList.add(new ItemFilme(nome, image, data));
                            }

                            adaptadorFilme = new AdaptadorFilme(getActivity(), itemFilmesList);
                            recyclerViewFilme.setAdapter(adaptadorFilme);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            /**
             * Callback method that an error has been occurred with the provided error code and optional
             * user-readable message.
             *
             * @param error
             */
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        });

        requestQueue.add(request);
    }
}
