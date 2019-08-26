package nil.s.fsdb;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Locale;

public class DetalhesPessoasActivity extends AppCompatActivity {

    private String TAG = "DetalhesPessoasActivity";

    private String id;

    private RequestQueue requestQueue;

    private ImageView imageView;

    private TextView textViewNome;
    private TextView textViewBio;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalhes_pessoas);

        imageView = findViewById(R.id.imageViewDetalhesPessoas);

        textViewNome = findViewById(R.id.textViewDetalhesPessoasNome);
        textViewBio = findViewById(R.id.textViewDetalhesPessoasBiblio);

        Intent intent = getIntent();

        id = intent.getStringExtra("id");

        requestQueue = Volley.newRequestQueue(this);

        parseJSON();
    }

    private void parseJSON() {

        final String iso_3166_1 = Locale.getDefault().getDisplayLanguage();
        String language = iso_3166_1 + "-" + iso_3166_1.toUpperCase();
        Log.d(TAG, "lang - " + language);

        String url = "https://api.themoviedb.org/3/person/" + id + "?api_key=" + ChaveAPI.TMDb;
        Log.d(TAG, "url - " + url);

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {

                            String nome = response.getString("name");
                            String imagePath = "https://image.tmdb.org/t/p/original/" + response.getString("profile_path");
                            String biography = response.getString("biography");

                            Picasso.get().load(imagePath).placeholder(R.drawable.progress_animation).into(imageView);

                            textViewNome.setText(nome);
                            textViewBio.setText(biography);

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
