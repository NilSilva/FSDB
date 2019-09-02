package nil.s.fsdb;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class TVFragment extends Fragment implements AdaptadorTV.OnItemClickListener {

    private String TAG = "TVFragment";

    private RecyclerView recyclerView;

    private AdaptadorTV adaptadorTV;

    private ArrayList<ItemTV> list = new ArrayList<>();

    private RequestQueue requestQueue;

    private EditText editText;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.d(TAG, "onCreateView: TVFragment");

        View rootView = inflater.inflate(R.layout.fragment_tv, container, false);

        editText = rootView.findViewById(R.id.editTextProcurarTVNome);

        recyclerView = rootView.findViewById(R.id.recyclerViewTVFragement);

        Log.d(TAG, "onCreateView: hint - " + editText.getHint().toString());

        editText.addTextChangedListener(searchTV);

        recyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(linearLayoutManager);

        requestQueue = Volley.newRequestQueue(getActivity());

        getURL();

        Log.d(TAG, "onCreateView: hint - " + editText.getHint().toString());
        return rootView;
    }

    private TextWatcher searchTV = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            Log.d(TAG, "onTextChanged: tv");
            getURL();
        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    };

    private void getURL() {
        String query = editText.getText().toString().trim();
        String url;

        if(query.isEmpty()){
            url = "https://api.themoviedb.org/3/tv/popular?api_key=" + ChaveAPI.TMDb;
        }else {
            url = "https://api.themoviedb.org/3/search/tv?api_key=" + ChaveAPI.TMDb + "&query=" + query;
        }
        Log.d(TAG, "getURL: " + url);

        parseJSON(url);
    }

    private void parseJSON(String url){
        list.clear();

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try{
                            JSONArray jsonArray = response.getJSONArray("results");

                            for(int i = 0; i < jsonArray.length(); i++){
                                JSONObject jsonObject = jsonArray.getJSONObject(i);

                                ItemTV itemTV = new ItemTV();

                                itemTV.setId(jsonObject.getString("id"));
                                itemTV.setPoster_path(jsonObject.getString("poster_path"));
                                itemTV.setNome(jsonObject.getString("name"));

                                list.add(itemTV);

                                Log.d(TAG, "onResponse: TV - " + list.get(i).toString());
                            }

                            adaptadorTV = new AdaptadorTV(getActivity(), list);
                            recyclerView.setAdapter(adaptadorTV);
                            adaptadorTV.setOnItemClickListener(TVFragment.this);

                            Log.d(TAG, "onResponse: tv - " + editText.getHint());
                        }catch (JSONException e){
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        });

        requestQueue.add(request);
    }

    @Override
    public void onItemClick(int position) {
        Intent intent = new Intent(getActivity(), DetalhesTVActivity.class);
        ItemTV clickedItem = list.get(position);

        intent.putExtra("id", clickedItem.getId());

        startActivity(intent);
    }
}
