package nil.s.fsdb;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;
import com.synnapps.carouselview.CarouselView;
import com.synnapps.carouselview.ImageClickListener;
import com.synnapps.carouselview.ImageListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import static android.widget.ImageView.ScaleType.FIT_XY;

public class DetalhesTVActivity extends AppCompatActivity implements AdaptadorPessoas.OnItemClickListenerP {

    private String TAG = "DetalhesTVActivity";
    private String id;

    private ImageView imageViewBackdrop;
    private ImageView imageViewPoster;

    private TextView textViewNome;
    private TextView textViewProduction;
    private TextView textViewFirstData;
    private TextView textViewRuntime;
    private TextView textViewEpisodios;
    private TextView textViewTemporadas;
    private TextView textViewOverview;

    private RequestQueue requestQueue;

    private CarouselView carouselView;

    private ArrayList<String> backdrops = new ArrayList<>();
    private ArrayList<ItemPessoas> listPessoas = new ArrayList<>();
    private ArrayList<ItemPessoas> listEquipa = new ArrayList<>();

    private RecyclerView recyclerViewAtores;
    private RecyclerView recyclerViewEquipa;

    private AdaptadorPessoas adaptadorPessoas;
    private AdaptadorPessoas adaptadorEquipa;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalhes_tv);

        Intent intent = getIntent();
        id = intent.getStringExtra("id");

        imageViewPoster = findViewById(R.id.imageViewDetalhesTVsPoster);

        textViewNome = findViewById(R.id.textViewDetalhesTVsNome);
        textViewProduction = findViewById(R.id.textViewDetalhesTVsProduction);
        textViewFirstData = findViewById(R.id.textViewDetalhesTVsDataLancamento);
        textViewRuntime = findViewById(R.id.textViewDetalhesTVsRuntime);
        textViewEpisodios = findViewById(R.id.textViewDetalhesTVsEpisodios);
        textViewTemporadas = findViewById(R.id.textViewDetalhesTVsTemporadas);
        textViewOverview = findViewById(R.id.textViewDetalhesTVsOverview);

        carouselView = findViewById(R.id.carouselViewTVsBackground);

        recyclerViewAtores = findViewById(R.id.recyclerViewDetalhesTvAtores);
        recyclerViewEquipa = findViewById(R.id.recyclerViewDetalhesTvEquipa);

        requestQueue = Volley.newRequestQueue(this);

        recyclerViewAtores.setHasFixedSize(true);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);

        linearLayoutManager.setOrientation(RecyclerView.HORIZONTAL);

        recyclerViewAtores.setLayoutManager(linearLayoutManager);

        recyclerViewEquipa.setHasFixedSize(true);

        LinearLayoutManager linearLayoutManager1 = new LinearLayoutManager(this);

        linearLayoutManager1.setOrientation(RecyclerView.HORIZONTAL);

        recyclerViewEquipa.setLayoutManager(linearLayoutManager1);

        buscarPessoas();

        GetInfo();

        getImages();
    }

    private void buscarPessoas() {
        listPessoas.clear();
        listEquipa.clear();

        String url = "https://api.themoviedb.org/3/tv/" + id + "/credits?api_key=" + ChaveAPI.TMDb;
        Log.d(TAG, "url credits - " + url);

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONArray jsonArray = response.getJSONArray("cast");

                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject result = jsonArray.getJSONObject(i);

                                ItemPessoas itemPessoa = new ItemPessoas();

                                itemPessoa.setName(result.getString("name"));
                                itemPessoa.setCharacter(result.getString("character"));
                                itemPessoa.setProfile_path(result.getString("profile_path"));
                                itemPessoa.setId(result.getString("id"));

                                listPessoas.add(itemPessoa);
                                Log.d(TAG, "onResponse: cast - " + itemPessoa.getName());
                            }

                            jsonArray = response.getJSONArray("crew");

                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject result = jsonArray.getJSONObject(i);

                                ItemPessoas itemPessoa = new ItemPessoas();

                                itemPessoa.setName(result.getString("name"));
                                itemPessoa.setCharacter(result.getString("job"));
                                itemPessoa.setProfile_path(result.getString("profile_path"));
                                itemPessoa.setId(result.getString("id"));

                                listEquipa.add(itemPessoa);
                                Log.d(TAG, "onResponse: crew - " + itemPessoa.getName());
                            }

                            adaptadorPessoas = new AdaptadorPessoas(DetalhesTVActivity.this, listPessoas);
                            recyclerViewAtores.setAdapter(adaptadorPessoas);
                            adaptadorPessoas.setOnItemClickListenerP(DetalhesTVActivity.this);

                            adaptadorEquipa = new AdaptadorPessoas(DetalhesTVActivity.this, listEquipa);
                            recyclerViewEquipa.setAdapter(adaptadorEquipa);
                            adaptadorEquipa.setOnItemClickListenerP(DetalhesTVActivity.this);
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

    private void GetInfo() {
        String url = "https://api.themoviedb.org/3/tv/" + id + "?api_key=" + ChaveAPI.TMDb + "&append_to_response=videos";
        Log.d(TAG, "GetInfo: url - " + url);

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            ItemTV itemTV = new ItemTV();

                            //ArrayList<String> runtimes = (ArrayList) response.get("episode_run_time");

                            itemTV.setBackdrop_path(response.getString("backdrop_path"));
                            itemTV.setPoster_path(response.getString("poster_path"));
                            itemTV.setNome(response.getString("name"));
                            itemTV.setIn_production((boolean) response.get("in_production"));
                            itemTV.setFirst_air_date(response.getString("first_air_date"));
                            itemTV.setRuntime("-");
                            itemTV.setNumeroEpisodios(response.getString("number_of_episodes"));
                            itemTV.setNumeroTemporadas(response.getString("number_of_seasons"));
                            itemTV.setOverview(response.getString("overview"));

                            Log.d(TAG, "onResponse: itemTV - " + itemTV.toString());

                            PreencherCampos(itemTV);
                        } catch (JSONException e) {
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

    private void PreencherCampos(ItemTV tv) {
        Log.d(TAG, "PreencherCampos: " + tv.toString());

        Picasso.get().load(tv.getPoster_path()).into(imageViewPoster, new Callback() {
            @Override
            public void onSuccess() {}

            @Override
            public void onError(Exception e) {
                imageViewPoster.setImageResource(R.mipmap.ic_no_image);
            }
        });

        textViewNome.setText(tv.getNome());

        if (tv.getIn_production()) {
            textViewProduction.setText("Yes");
        } else {
            textViewProduction.setText("No");
        }

        textViewFirstData.setText(tv.getFirst_air_date());
        textViewRuntime.setText("-");
        textViewEpisodios.setText(tv.getNumeroEpisodios());
        textViewTemporadas.setText(tv.getNumeroTemporadas());
        textViewOverview.setText(tv.getOverview());
    }

    private void getImages() {
        String url = "https://api.themoviedb.org/3/tv/" + id + "/images?api_key=" + ChaveAPI.TMDb;
        Log.d(TAG, "getImages: url - " + url);

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONArray jsonArray = response.getJSONArray("backdrops");

                    for (int i = 0; i < jsonArray.length(); i++) {
                        backdrops.add("https://image.tmdb.org/t/p/original/" + jsonArray.getJSONObject(i).getString("file_path"));
                    }

                    carouselView.setImageListener(imageListener);
                    carouselView.setPageCount(jsonArray.length());
                    carouselView.setImageClickListener(imageClickListener);
                    Log.d(TAG, "onCreate: size - " + jsonArray.length());
                } catch (JSONException e) {
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

    ImageClickListener imageClickListener = new ImageClickListener() {
        @Override
        public void onClick(int position) {
            AlertDialog.Builder ImageDialog = new AlertDialog.Builder(DetalhesTVActivity.this);
            ImageDialog.setTitle("Title");
            final ImageView showImage = new ImageView(DetalhesTVActivity.this);
            ImageDialog.setView(showImage);
            showImage.setScaleType(FIT_XY);
            showImage.setAdjustViewBounds(true);
            Picasso.get().load(backdrops.get(position)).placeholder(R.drawable.progress_animation).into(showImage);

            ImageDialog.setNegativeButton("ok", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface arg0, int arg1) {
                }
            });
            ImageDialog.show();
        }
    };

    ImageListener imageListener = new ImageListener() {
        @Override
        public void setImageForPosition(int position, ImageView imageView) {
            Picasso.get().load(backdrops.get(position)).placeholder(R.drawable.progress_animation).into(imageView);
            Log.d(TAG, "setImageForPosition: " + backdrops.get(position));
        }
    };

    @Override
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
}
