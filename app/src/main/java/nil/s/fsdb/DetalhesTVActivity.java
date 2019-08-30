package nil.s.fsdb;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DownloadManager;
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
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class DetalhesTVActivity extends AppCompatActivity {

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalhes_tv);

        Intent intent = getIntent();
        id = intent.getStringExtra("id");

        imageViewBackdrop = findViewById(R.id.imageViewDetalhesTVsBackground);
        imageViewPoster = findViewById(R.id.imageViewDetalhesTVsPoster);

        textViewNome = findViewById(R.id.textViewDetalhesTVsNome);
        textViewProduction = findViewById(R.id.textViewDetalhesTVsProduction);
        textViewFirstData = findViewById(R.id.textViewDetalhesTVsDataLancamento);
        textViewRuntime = findViewById(R.id.textViewDetalhesTVsRuntime);
        textViewEpisodios = findViewById(R.id.textViewDetalhesTVsEpisodios);
        textViewTemporadas = findViewById(R.id.textViewDetalhesTVsTemporadas);
        textViewOverview = findViewById(R.id.textViewDetalhesTVsOverview);

        requestQueue = Volley.newRequestQueue(this);

        GetInfo();
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
                            itemTV.setIn_production((boolean)response.get("in_production"));
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

    private void PreencherCampos(ItemTV tv){
        Log.d(TAG, "PreencherCampos: " + tv.toString());

        Picasso.get().load(tv.getBackdrop_path()).into(imageViewBackdrop, new Callback() {
            @Override
            public void onSuccess() {

            }

            @Override
            public void onError(Exception e) {
                imageViewBackdrop.setImageResource(R.mipmap.ic_no_image);
            }
        });
        Picasso.get().load(tv.getPoster_path()).into(imageViewPoster, new Callback() {
            @Override
            public void onSuccess() {

            }

            @Override
            public void onError(Exception e) {
                imageViewPoster.setImageResource(R.mipmap.ic_no_image);
            }
        });

        textViewNome.setText(tv.getNome());

        if (tv.getIn_production()){
            textViewProduction.setText("Yes");
        }else{
            textViewProduction.setText("No");
        }

        textViewFirstData.setText(tv.getFirst_air_date());
        textViewRuntime.setText("-");
        textViewEpisodios.setText(tv.getNumeroEpisodios());
        textViewTemporadas.setText(tv.getNumeroTemporadas());
        textViewOverview.setText(tv.getOverview());
    }
}
