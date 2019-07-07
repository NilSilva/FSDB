package nil.s.fsdb;

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
import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerView;
import com.r0adkll.slidr.Slidr;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Locale;

public class DetalhesFilmesActivity extends YouTubeBaseActivity implements YouTubePlayer.OnInitializedListener {

    private String TAG = "DetalhesFilmesActivity";

    private String id;
    
    private ImageView imageViewBackground;
    private ImageView imageViewPoster;
    
    private TextView textViewNome;
    private TextView textViewOverview;
    
    private RequestQueue requestQueue;

    private ItemFilme itemFilme;

    private YouTubePlayerView youTubeView;

    private String key = "No Video";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalhes_filmes);

        Slidr.attach(this);

        Intent intent = getIntent();
        id = intent.getStringExtra("id");

        requestQueue = Volley.newRequestQueue(this);

        imageViewBackground = findViewById(R.id.imageViewDetalhesFilmesBackground);
        imageViewPoster = findViewById(R.id.imageViewDetalhesFilmesPoster);

        textViewNome = findViewById(R.id.textViewDetalhesFilmesNome);
        textViewOverview = findViewById(R.id.textViewDetalhesFilmesOverview);

        youTubeView = findViewById(R.id.videoViewDetalhesFilmeTrailer);

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

                            String bud = response.getString("budget");
                            int budget;
                            String rev = response.getString("revenue");
                            int revenue;
                            String run = response.getString("runtime");
                            int runtime;

                            try {
                                budget = Integer.parseInt(bud);
                            }catch (Exception e){
                                budget = -1;
                            }
                            try {
                                revenue = Integer.parseInt(rev);
                            }catch (Exception e){
                                revenue = -1;
                            }
                            try {
                                runtime = Integer.parseInt(run);
                            }catch (Exception e){
                                runtime = -1;
                            }

                            itemFilme = new ItemFilme(
                                    response.getString("title"),
                                    response.getString("poster_path"),
                                    response.getString("backdrop_path"),
                                    response.getString("release_date"),
                                    budget,
                                    revenue,
                                    response.getString("overview"),
                                    runtime,
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
        key = filme.getKey();

        Log.d(TAG, "nome - " + nome);
        Log.d(TAG, "poster - " + posterURL);
        Log.d(TAG, "back - " + backURL);
        Log.d(TAG, "video url - " + key);

        Picasso.get().load(backURL).into(imageViewBackground);
        Picasso.get().load(posterURL).into(imageViewPoster);

        textViewNome.setText(nome);
        textViewOverview.setText(overview);

        textViewNome.measure(0, 0);       //must call measure!
        int Px = textViewNome.getMeasuredWidth();
        int maxPx = (int)(220 * getResources().getDisplayMetrics().density);
        if(Px > maxPx){
            textViewNome.setWidth(maxPx);
        }
        Log.d(TAG, "width - " + Px);
        Log.d(TAG, "widthMax - " + maxPx);

        youTubeView.initialize(ChaveAPI.youtube, this);
    }

    @Override
    public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer player, boolean wasRestored) {
        if (!wasRestored) {
            player.cueVideo(key);
            Log.d("Youtube -->", "Success - " + key);
        }
    }
    @Override
    public void onInitializationFailure(YouTubePlayer.Provider arg0, YouTubeInitializationResult arg1){
        Log.d("Youtube -->", "Failure");
    }
}
