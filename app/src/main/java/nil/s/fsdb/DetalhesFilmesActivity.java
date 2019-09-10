package nil.s.fsdb;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Locale;

public class DetalhesFilmesActivity extends YouTubeBaseActivity implements YouTubePlayer.OnInitializedListener, AdaptadorPessoas.OnItemClickListenerP {

    private String TAG = "DetalhesFilmesActivity";

    private String id;
    
    private ImageView imageViewBackground;
    private ImageView imageViewPoster;
    
    private TextView textViewNome;
    private TextView textViewOverview;
    private TextView textViewData;
    private TextView textViewBoxOffice;
    private TextView textViewBudget;
    private TextView textViewRuntime;
    private TextView textView;
    
    private RequestQueue requestQueue;

    private YouTubePlayerView youTubeView;

    private String key = "No Video";

    private RecyclerView recyclerViewPessoas;
    private RecyclerView recyclerViewEquipa;

    private AdaptadorPessoas adaptadorPessoas;
    private AdaptadorPessoas adaptadorEquipa;

    private ArrayList<ItemPessoas> itemPessoasList;
    private ArrayList<ItemPessoas> itemEquipaList;

    private double rating;

    private ProgressBar progressBar;

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
        textViewData = findViewById(R.id.textViewDetalhesFilmesData);
        textViewBoxOffice = findViewById(R.id.textViewDetalhesFilmesBoxOffice);
        textViewRuntime = findViewById(R.id.textViewDetalhesFilmesRuntime);
        textView = findViewById(R.id.textViewDetalhesFilmesVoteAverage);
        textViewBudget = findViewById(R.id.textViewDetalhesFilmesBudget);

        recyclerViewPessoas = findViewById(R.id.recyclerViewDetalhesFilmeAtores);
        recyclerViewEquipa = findViewById(R.id.recyclerViewDetalhesFilmeEquipa);

        youTubeView = findViewById(R.id.videoViewDetalhesFilmeTrailer);

        progressBar = findViewById(R.id.progress_circularFilmeClass);

        parseJSON();

        recyclerViewPessoas.setHasFixedSize(true);
        recyclerViewEquipa.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(RecyclerView.HORIZONTAL);

        LinearLayoutManager linearLayoutManager1 = new LinearLayoutManager(this);
        linearLayoutManager1.setOrientation(RecyclerView.HORIZONTAL);
        recyclerViewPessoas.setLayoutManager(linearLayoutManager);
        recyclerViewEquipa.setLayoutManager(linearLayoutManager1);

        itemPessoasList = new ArrayList<>();
        itemEquipaList = new ArrayList<>();

        buscarPessoas();
    }

    private void buscarPessoas() {
        itemPessoasList.clear();

        String language = Locale.getDefault().getLanguage() + "-" + Locale.getDefault().getLanguage().toUpperCase();
        Log.d(TAG, "lang - " + language);

        String url = "https://api.themoviedb.org/3/movie/" + id + "/credits?api_key=" + ChaveAPI.TMDb + "&language=" + language;

        Log.d(TAG, "url credits - " + url);

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONArray jsonArray = response.getJSONArray("cast");

                            for(int i = 0;i < jsonArray.length();i++){

                                JSONObject result = jsonArray.getJSONObject(i);

                                ItemPessoas itemPessoa = new ItemPessoas();

                                itemPessoa.setName(result.getString("name"));
                                itemPessoa.setCharacter(result.getString("character"));
                                itemPessoa.setProfile_path(result.getString("profile_path"));
                                itemPessoa.setId(result.getString("id"));

                                itemPessoasList.add(itemPessoa);

                                Log.d(TAG, "onResponse: cast - " + itemPessoa.getName());
                            }

                            jsonArray = response.getJSONArray("crew");

                            for(int i = 0;i < jsonArray.length();i++){

                                JSONObject result = jsonArray.getJSONObject(i);

                                ItemPessoas itemPessoa = new ItemPessoas();

                                itemPessoa.setName(result.getString("name"));
                                itemPessoa.setCharacter(result.getString("job"));
                                itemPessoa.setProfile_path(result.getString("profile_path"));
                                itemPessoa.setId(result.getString("id"));

                                itemEquipaList.add(itemPessoa);
                                Log.d(TAG, "onResponse: crew - " + itemPessoa.getName());
                            }

                            adaptadorPessoas = new AdaptadorPessoas(DetalhesFilmesActivity.this, itemPessoasList);
                            recyclerViewPessoas.setAdapter(adaptadorPessoas);
                            adaptadorPessoas.setOnItemClickListenerP(DetalhesFilmesActivity.this);

                            adaptadorEquipa = new AdaptadorPessoas(DetalhesFilmesActivity.this, itemEquipaList);
                            recyclerViewEquipa.setAdapter(adaptadorEquipa);
                            adaptadorEquipa.setOnItemClickListenerP(DetalhesFilmesActivity.this);
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

        Intent intent = new Intent(this, DetalhesPessoasActivity.class);

        String id;
        if (adaptadorPessoas.getInUse()) {
            id = adaptadorPessoas.getId(position);
            adaptadorPessoas.NotInUse();
        } else {
            id = adaptadorEquipa.getId(position);
        }

        Log.d(TAG, "onItemClick: position - " + position);

        intent.putExtra("id", id);

        Toast.makeText(this, "ID - " + id, Toast.LENGTH_LONG).show();

        startActivity(intent);
    }

    private void parseJSON() {

        /*final String iso_3166_1 = Locale.getDefault().getDisplayLanguage();
        String language = iso_3166_1 + "-" + iso_3166_1.toUpperCase();
        Log.d(TAG, "lang - " + language);*/

        String url = "https://api.themoviedb.org/3/movie/" + id + "?api_key=" + ChaveAPI.TMDb + "&append_to_response=videos,release_dates";
        Log.d(TAG, "url - " + url);

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {

                            JSONObject videos = response.getJSONObject("videos");
                            JSONArray results = videos.getJSONArray("results");

                            ItemFilme itemFilme = new ItemFilme();

                            for(int i = 0;i < results.length();i++){
                                JSONObject result = results.getJSONObject(i);

                                if(result.getString("type").equals("Trailer") && result.getString("site").equals("YouTube")){
                                    itemFilme.setKey(result.getString("key"));
                                    break;
                                }
                            }

                            Log.d(TAG, "video key - " + key);

                            itemFilme.setVote(response.getDouble("vote_average"));
                            itemFilme.setBudget(response.getInt("budget"));
                            itemFilme.setRevenue(response.getInt("revenue"));
                            itemFilme.setRuntime(response.getInt("runtime"));
                            itemFilme.setTitle(response.getString("title"));
                            itemFilme.setPoster_path(response.getString("poster_path"));
                            itemFilme.setBackdrop_path(response.getString("backdrop_path"));
                            itemFilme.setRelease_date(response.getString("release_date"));
                            itemFilme.setOverview(response.getString("overview"));

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

        Log.d(TAG, "Campos: " + filme.toString());

        Picasso.get().load(filme.getBackdrop_path()).placeholder(R.drawable.progress_animation).into(imageViewBackground, new Callback() {
            @Override
            public void onSuccess() {
            }

            @Override
            public void onError(Exception e) {

                imageViewBackground.setImageResource(R.mipmap.ic_no_image);
            }
        });
        Picasso.get().load(filme.getPoster_path()).placeholder(R.drawable.progress_animation).into(imageViewPoster, new Callback() {
            @Override
            public void onSuccess() {
            }

            @Override
            public void onError(Exception e) {

                imageViewPoster.setImageResource(R.mipmap.ic_no_image);
            }
        });

        textViewNome.setText(filme.getTitle());
        textViewOverview.setText(filme.getOverview());
        textViewData.setText(filme.getRelease_date());
        textViewBudget.setText(String.valueOf(filme.getBudget()));
        textViewBoxOffice.setText(String.valueOf(filme.getRevenue()));
        textViewRuntime.setText(String.valueOf(filme.getRuntime()));
        textView.setText(String.valueOf(progressBar.getProgress()));

        progressBar.setProgress((int)(filme.getVote() * 10));

        youTubeView.initialize(ChaveAPI.youtube, this);
    }

    @Override
    public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer player, boolean wasRestored) {
        if (!wasRestored) {
            player.cueVideo(key);
            Log.d("Youtube -->", "Success - " + key);
            if(key.equals("No Video")){
                CardView cardView = findViewById(R.id.cardViewDetalhesFilmeTrailer);
                cardView.setVisibility(View.GONE);
            }
        }
    }
    @Override
    public void onInitializationFailure(YouTubePlayer.Provider arg0, YouTubeInitializationResult arg1){
        CardView cardView = findViewById(R.id.cardViewDetalhesFilmeTrailer);
        cardView.setVisibility(View.GONE);
        Log.d("Youtube -->", "Failure");
    }

    public void copy(View view) {
        ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData clip = ClipData.newPlainText(textViewNome.getText().toString(), textViewNome.getText().toString());
        clipboard.setPrimaryClip(clip);
        Toast.makeText(this, "copiado!!!!!!", Toast.LENGTH_LONG).show();
    }
}
