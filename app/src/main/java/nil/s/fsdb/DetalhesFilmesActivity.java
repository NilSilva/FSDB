package nil.s.fsdb;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
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
    private TextView textViewRevenue;
    private TextView textViewRuntime;
    
    private RequestQueue requestQueue;

    private ItemFilme itemFilme;

    private YouTubePlayerView youTubeView;

    private String key = "No Video";

    private RecyclerView recyclerViewPessoas;
    private RecyclerView recyclerViewEquipa;

    private AdaptadorPessoas adaptadorPessoas;
    private AdaptadorPessoas adaptadorEquipa;

    private ArrayList<ItemFilmePessoas> itemPessoasList;
    private ArrayList<ItemFilmePessoas> itemEquipaList;

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
        textViewRevenue = findViewById(R.id.textViewDetalhesFilmesRevenue);
        textViewRuntime = findViewById(R.id.textViewDetalhesFilmesRuntime);

        recyclerViewPessoas = findViewById(R.id.recyclerViewDetalhesFilmeAtores);
        recyclerViewEquipa = findViewById(R.id.recyclerViewDetalhesFilmeEquipa);

        youTubeView = findViewById(R.id.videoViewDetalhesFilmeTrailer);

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

                                String nome = result.getString("name");
                                String personagem = result.getString("character");
                                String image = result.getString("profile_path");
                                String ID = result.getString("id");
                                itemPessoasList.add(new ItemFilmePessoas(nome, personagem, image, ID));
                                Log.d(TAG, "onResponse: cast - " + nome);
                            }

                            jsonArray = response.getJSONArray("crew");

                            for(int i = 0;i < jsonArray.length();i++){
                                JSONObject result = jsonArray.getJSONObject(i);

                                String nome = result.getString("name");
                                String job = result.getString("job");
                                String image = result.getString("profile_path");
                                String ID = result.getString("id");
                                itemEquipaList.add(new ItemFilmePessoas(nome, job, image, ID));
                                Log.d(TAG, "onResponse: crew - " + nome);
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

        final String iso_3166_1 = Locale.getDefault().getDisplayLanguage();
        String language = iso_3166_1 + "-" + iso_3166_1.toUpperCase();
        Log.d(TAG, "lang - " + language);

        String url = "https://api.themoviedb.org/3/movie/" + id + "?api_key=" + ChaveAPI.TMDb + "&language=" + language + "&append_to_response=videos,release_dates";
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

                            JSONObject release_dates = response.getJSONObject("release_dates");
                            JSONArray RDresults = release_dates.getJSONArray("results");
                            JSONObject info = null;

                            for(int i = 0;i < RDresults.length();i++){
                                JSONObject result = RDresults.getJSONObject(i);
                                Log.d(TAG, "onResponse: result iso - " + result.getString("iso_3166_1"));

                                if(result.getString("iso_3166_1").equals(iso_3166_1)){
                                    //TODO:this gives an array!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
                                    info = result.getJSONObject("release_dates");
                                    break;
                                }
                            }

                            Log.d(TAG, "video key - " + key);

                            String bud = response.getString("budget");
                            int budget;
                            String rev = response.getString("revenue");
                            long revenue;
                            String run = response.getString("runtime");
                            int runtime;

                            try {
                                budget = Integer.parseInt(bud);
                            }catch (Exception e){
                                budget = -1;
                            }
                            try {
                                revenue = Long.parseLong(rev);
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
                            if (info != null) {
                                Log.d(TAG, "onResponse: data" + info.getString("release_date"));
                            }else {
                                Log.d(TAG, "onResponse: info = null");
                            }

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
        Log.d(TAG, "Campos: data - " + filme.getRelease_date());
        Log.d(TAG, "Campos: revenue - " + filme.getRevenue());
        Log.d(TAG, "Campos: budget - " + filme.getBudget());
        Log.d(TAG, "Campos: runtime - " + filme.getRuntime());

        Picasso.get().load(backURL).placeholder(R.drawable.progress_animation).into(imageViewBackground, new Callback() {
            @Override
            public void onSuccess() {
            }

            @Override
            public void onError(Exception e) {

                imageViewBackground.setImageResource(R.mipmap.ic_no_image);
            }
        });
        Picasso.get().load(posterURL).placeholder(R.drawable.progress_animation).into(imageViewPoster, new Callback() {
            @Override
            public void onSuccess() {
            }

            @Override
            public void onError(Exception e) {

                imageViewPoster.setImageResource(R.mipmap.ic_no_image);
            }
        });

        textViewNome.setText(nome);
        textViewOverview.setText(overview);
        textViewData.setText(filme.getRelease_date());
        long lucro = filme.getRevenue() - filme.getBudget();
        String boxOffice = filme.getRevenue() + "/" + filme.getBudget() + "(" + lucro + ")";
        textViewRevenue.setText(boxOffice);
        textViewRuntime.setText(String.valueOf(filme.getRuntime()));

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
}
