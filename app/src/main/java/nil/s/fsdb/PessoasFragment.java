package nil.s.fsdb;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Editable;
import android.text.Spannable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

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

public class PessoasFragment extends Fragment implements AdaptadorProcurarPessoas.OnItemClickListenerProcurar {

    /*==============================================================================================
        Declaração dos objetos
     =============================================================================================*/
    private final String TAG = "PessoasFragment"; //TAG para os logs

    private RecyclerView recyclerViewPessoas;

    private AdaptadorProcurarPessoas adaptadorPessoas;

    private ArrayList<ItemFilmePessoas> itemPessoasList;

    private RequestQueue requestQueue;

    private EditText editText;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_pessoas, container, false);
        /*==========================================================================================
            Criação dos objetos
         =========================================================================================*/
        recyclerViewPessoas = rootView.findViewById(R.id.recyclerViewPessoasFragement);

        editText = rootView.findViewById(R.id.editTextProcurarPessoaNome);

        editText.addTextChangedListener(searchPerson);

        recyclerViewPessoas.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        recyclerViewPessoas.setLayoutManager(linearLayoutManager);

        itemPessoasList = new ArrayList<>();

        requestQueue = Volley.newRequestQueue(getActivity());
        parseJSON();

        return rootView;
    }

    private TextWatcher searchPerson = new TextWatcher(){

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
        itemPessoasList.clear();

        String language = Locale.getDefault().getLanguage() + "-" + Locale.getDefault().getLanguage().toUpperCase();

        String url = "https://api.themoviedb.org/3/person/popular?api_key=" + ChaveAPI.TMDb + "&region=PT&language=" + language;

        if(!editText.getText().toString().isEmpty()){
            url = "https://api.themoviedb.org/3/search/person?api_key=" + ChaveAPI.TMDb + "&query=" + editText.getText().toString().trim();
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

                                String id = result.getString("id");
                                String nome = result.getString("name");
                                String image = result.getString("profile_path");
                                itemPessoasList.add(new ItemFilmePessoas(id, nome, image));
                            }

                            adaptadorPessoas = new AdaptadorProcurarPessoas(getActivity(), itemPessoasList);
                            recyclerViewPessoas.setAdapter(adaptadorPessoas);
                            adaptadorPessoas.setOnItemClickListenerProcurar(PessoasFragment.this);
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

        Intent intent = new Intent(getActivity(), DetalhesPessoasActivity.class);
        ItemFilmePessoas clickedItem = itemPessoasList.get(position);

        intent.putExtra("id", clickedItem.getId());

        startActivity(intent);
    }

}
