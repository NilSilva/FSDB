package nil.s.fsdb;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.media.Image;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

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
import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Locale;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

public class DetalhesFilmesActivity extends AppCompatActivity {

    private String TAG = "DetalhesFilmesActivity";

    private String id;
    
    private ImageView imageViewBackground;
    private ImageView imageViewPoster;
    
    private TextView textViewNome;
    private TextView textViewOverview;
    
    private RequestQueue requestQueue;

    private ItemFilme itemFilme;

    private VideoView videoViewTrailer;

    private String key = "No Video";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalhes_filmes);

        Intent intent = getIntent();
        id = intent.getStringExtra("id");

        requestQueue = Volley.newRequestQueue(this);

        imageViewBackground = findViewById(R.id.imageViewDetalhesFilmesBackground);
        imageViewPoster = findViewById(R.id.imageViewDetalhesFilmesPoster);

        textViewNome = findViewById(R.id.textViewDetalhesFilmesNome);
        textViewOverview = findViewById(R.id.textViewDetalhesFilmesOverview);

        videoViewTrailer = findViewById(R.id.videoViewDetalhesFilmeTrailer);

        parseJSON();
    }

    private void parseJSON() {

        String language = Locale.getDefault().getLanguage() + "-" + Locale.getDefault().getLanguage().toUpperCase();
        Log.d(TAG, "lang - " + language);

        String url = "https://api.themoviedb.org/3/movie/" + id + "?api_key=" + ChaveAPI.TMDb + "&language=" + language + "&append_to_response=videos";
        Log.d(TAG, "url - " + url);

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {

                            JSONObject videos = response.getJSONObject("videos");
                            JSONArray results = videos.getJSONArray("results");

                            for(int i = 0;i < results.length();i++){
                                JSONObject result = results.getJSONObject(i);

                                if(result.getString("type").equals("Trailer") && result.getString("site").equals("YouTube")){
                                    key = result.getString("key");
                                    break;
                                }
                            }

                            Log.d(TAG, "video key - " + key);

                            itemFilme = new ItemFilme(
                                    response.getString("title"),
                                    response.getString("poster_path"),
                                    response.getString("backdrop_path"),
                                    response.getString("release_date"),
                                    response.getInt("budget"),
                                    response.getInt("revenue"),
                                    response.getString("overview"),
                                    response.getInt("runtime"),
                                    key
                            );

                            Campos(itemFilme);

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

    private void Campos(ItemFilme filme){
        String backURL = filme.getBackdrop_path();
        String posterURL = filme.getPoster_path();
        String nome = filme.getTitle();
        String overview = filme.getOverview();
        key = "https://www.youtube.com/watch?v=" + filme.getKey();

        Log.d(TAG, "nome - " + nome);
        Log.d(TAG, "poster - " + posterURL);
        Log.d(TAG, "back - " + backURL);
        Log.d(TAG, "video url - " + key);

        Picasso.get().load(backURL).into(imageViewBackground);
        Picasso.get().load(posterURL).into(imageViewPoster);

        textViewNome.setText(nome);
        textViewOverview.setText(overview);
    }
}
