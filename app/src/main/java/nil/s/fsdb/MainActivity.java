package nil.s.fsdb;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    /*
        TODO: dar credito à fonte da base de dados
     */

    /*==============================================================================================
        Declaração dos objetos
     =============================================================================================*/
    private final String TAG = "MainActivity"; //TAG para os logs

    private RecyclerView recyclerViewFilme;

    private AdaptadorFilme adaptadorFilme;

    private ArrayList<ItemFilme> itemFilmesList;

    private RequestQueue requestQueue;

    private int page = 1;
    private int npage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        /*==========================================================================================
            Criação dos objetos
         =========================================================================================*/
        recyclerViewFilme = findViewById(R.id.recyclerViewMainActivity);
        recyclerViewFilme.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        //linearLayoutManager.setOrientation(RecyclerView.HORIZONTAL);
        recyclerViewFilme.setLayoutManager(linearLayoutManager);

        itemFilmesList = new ArrayList<>();

        requestQueue = Volley.newRequestQueue(this);
        parseJSON();
    }

    private void parseJSON() {

        String url = "https://api.themoviedb.org/3/movie/upcoming?api_key=c816441df0108db98214080d85446617";

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

                            adaptadorFilme = new AdaptadorFilme(MainActivity.this, itemFilmesList);
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
