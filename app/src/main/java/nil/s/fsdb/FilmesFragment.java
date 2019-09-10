package nil.s.fsdb;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.Spannable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.navigation.NavigationView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Locale;

public class FilmesFragment extends Fragment implements AdaptadorFilme.OnItemClickListener {

    /*==============================================================================================
        Declaração dos objetos
     =============================================================================================*/
    private final String TAG = "FilmesFragment"; //TAG para os logs

    private RecyclerView recyclerViewFilme;

    private AdaptadorFilme adaptadorFilme;

    private ArrayList<ItemFilme> itemFilmesList;

    private RequestQueue requestQueue;

    private EditText editText;

    private View rootView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        rootView = inflater.inflate(R.layout.filmes_fragment, container, false);
        /*==========================================================================================
            Criação dos objetos
         =========================================================================================*/
        recyclerViewFilme = rootView.findViewById(R.id.recyclerViewFilmesFragement);

        editText = rootView.findViewById(R.id.editTextProcurarFilmeNome);

        editText.addTextChangedListener(searchMovie);

        recyclerViewFilme.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        //linearLayoutManager.setOrientation(RecyclerView.HORIZONTAL);
        recyclerViewFilme.setLayoutManager(linearLayoutManager);

        itemFilmesList = new ArrayList<>();

        requestQueue = Volley.newRequestQueue(getActivity());
        parseJSON();

        return rootView;
    }

    private TextWatcher searchMovie = new TextWatcher(){

        /**
         * This method is called to notify you that, within <code>s</code>,
         * the <code>count</code> characters beginning at <code>start</code>
         * are about to be replaced by new text with length <code>after</code>.
         * It is an error to attempt to make changes to <code>s</code> from
         * this callback.
         *
         * @param s
         * @param start
         * @param count
         * @param after
         */
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        /**
         * This method is called to notify you that, within <code>s</code>,
         * the <code>count</code> characters beginning at <code>start</code>
         * have just replaced old text that had length <code>before</code>.
         * It is an error to attempt to make changes to <code>s</code> from
         * this callback.
         *
         * @param s
         * @param start
         * @param before
         * @param count
         */
        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            parseJSON();
        }

        /**
         * This method is called to notify you that, somewhere within
         * <code>s</code>, the text has been changed.
         * It is legitimate to make further changes to <code>s</code> from
         * this callback, but be careful not to get yourself into an infinite
         * loop, because any changes you make will cause this method to be
         * called again recursively.
         * (You are not told where the change took place because other
         * afterTextChanged() methods may already have made other changes
         * and invalidated the offsets.  But if you need to know here,
         * you can use {@link Spannable#setSpan} in {@link #onTextChanged}
         * to mark your place and then look up from here where the span
         * ended up.
         *
         * @param s
         */
        @Override
        public void afterTextChanged(Editable s) {

            //parseJSON();
        }
    };

    private void parseJSON() {
        itemFilmesList.clear();

        String language = Locale.getDefault().getLanguage() + "-" + Locale.getDefault().getLanguage().toUpperCase();
        Log.d(TAG, "lang - " + language);

        String url = "https://api.themoviedb.org/3/movie/popular?api_key=" + ChaveAPI.TMDb + "&region=PT&language=" + language;

        if(!editText.getText().toString().isEmpty()){
            url = "https://api.themoviedb.org/3/search/movie?api_key=" + ChaveAPI.TMDb + "&query=" + editText.getText().toString().trim();
        }

        Log.d(TAG, "url - " + url);

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONArray jsonArray = response.getJSONArray("results");

                            for(int i = 0;i < jsonArray.length();i++){
                                JSONObject result = jsonArray.getJSONObject(i);

                                ItemFilme itemFilme = new ItemFilme();

                                itemFilme.setTitle(result.getString("title"));
                                itemFilme.setId(result.getString("id"));
                                itemFilme.setPoster_path(result.getString("poster_path"));
                                itemFilme.setRelease_date(result.getString("release_date"));

                                itemFilmesList.add(itemFilme);
                            }

                            adaptadorFilme = new AdaptadorFilme(getActivity(), itemFilmesList);
                            recyclerViewFilme.setAdapter(adaptadorFilme);
                            adaptadorFilme.setOnItemClickListener(FilmesFragment.this);
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

    public void onItemClick(int position) {

        Intent intent = new Intent(getActivity(), DetalhesFilmesActivity.class);
        ItemFilme clickedItem = itemFilmesList.get(position);

        intent.putExtra("id", clickedItem.getId());

        startActivity(intent);
    }
}
