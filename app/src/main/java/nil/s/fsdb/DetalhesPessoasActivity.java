package nil.s.fsdb;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Locale;

public class DetalhesPessoasActivity extends AppCompatActivity implements AdaptadorFilmografia.OnItemClickListenerP {

    private String TAG = "DetalhesPessoasActivity";

    private String id;

    private RequestQueue requestQueue;

    private ImageView imageView;

    private TextView textViewNome;
    private TextView textViewBio;
    private TextView textViewDateOfBirth;
    private TextView textViewKnownForDepartment;
    private TextView textViewGender;

    private RecyclerView recyclerView;

    private AdaptadorFilmografia adaptadorFilmografia;

    private ArrayList<ItemFilme> itemFilmografias;

    private Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalhes_pessoas);

        imageView = findViewById(R.id.imageViewDetalhesPessoas);

        textViewNome = findViewById(R.id.textViewDetalhesPessoasNome);
        textViewBio = findViewById(R.id.textViewDetalhesPessoasBiblio);
        textViewDateOfBirth = findViewById(R.id.textViewDetalhesPessoasDataNascimento);
        textViewKnownForDepartment = findViewById(R.id.textViewDetalhesPessoasJob);
        textViewGender = findViewById(R.id.textViewDetalhesPessoasSexo);

        recyclerView = findViewById(R.id.recyclerViewDetalhesPessoasFilmografia);

        button = findViewById(R.id.butt);

        Intent intent = getIntent();

        id = intent.getStringExtra("id");

        requestQueue = Volley.newRequestQueue(this);

        parseJSON();

        recyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(RecyclerView.HORIZONTAL);
        recyclerView.setLayoutManager(linearLayoutManager);

        itemFilmografias = new ArrayList<>();

        carregarFilmografia();
    }

    private void carregarFilmografia() {

        itemFilmografias.clear();

        final String iso_3166_1 = Locale.getDefault().getDisplayLanguage();
        String language = iso_3166_1 + "-" + iso_3166_1.toUpperCase();
        Log.d(TAG, "lang - " + language);

        String url = "https://api.themoviedb.org/3/person/" + id + "/combined_credits?api_key=" + ChaveAPI.TMDb;
        Log.d(TAG, "url - " + url);

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {

                            JSONArray jsonArray = response.getJSONArray("cast");

                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject object = jsonArray.getJSONObject(i);

                                ItemFilme filmografia = new ItemFilme();

                                filmografia.setType(object.getString("media_type"));
                                filmografia.setId(object.getString("id"));

                                if (filmografia.getType().equals("movie")) {

                                    try {
                                        filmografia.setRelease_date(object.getString("release_date").substring(0, 4));
                                    } catch (Exception e) {
                                        filmografia.setRelease_date("n/a");
                                    }

                                    filmografia.setTitle(object.getString("title"));
                                } else {

                                    try {
                                        filmografia.setRelease_date(object.getString("first_air_date").substring(0, 4));
                                    } catch (Exception e) {
                                        filmografia.setRelease_date("n/a");
                                    }

                                    filmografia.setTitle(object.getString("name"));
                                }

                                try {
                                    filmografia.setChar_job(object.getString("character"));
                                } catch (JSONException e) {
                                    filmografia.setChar_job("n/a");
                                }

                                filmografia.setPoster_path(object.getString("poster_path"));

                                Log.d(TAG, "onResponse: " + filmografia.toString());

                                itemFilmografias.add(filmografia);
                            }

                            jsonArray = response.getJSONArray("crew");

                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject object = jsonArray.getJSONObject(i);

                                ItemFilme filmografia = new ItemFilme();

                                String media = object.getString("media_type");

                                filmografia.setType(object.getString("media_type"));
                                filmografia.setId(object.getString("id"));

                                if (filmografia.getType().equals("movie")) {

                                    try {
                                        filmografia.setRelease_date(object.getString("release_date").substring(0, 4));
                                    } catch (Exception e) {
                                        filmografia.setRelease_date("n/a");
                                    }

                                    filmografia.setTitle(object.getString("title"));
                                } else {

                                    try {
                                        filmografia.setRelease_date(object.getString("first_air_date").substring(0, 4));
                                    } catch (Exception e) {
                                        filmografia.setRelease_date("n/a");
                                    }

                                    filmografia.setTitle(object.getString("name"));
                                }

                                filmografia.setChar_job(object.getString("job"));

                                filmografia.setPoster_path(object.getString("poster_path"));
                                itemFilmografias.add(filmografia);
                            }

                            Collections.sort(itemFilmografias, new Comparator<ItemFilme>() {
                                @Override
                                public int compare(ItemFilme filmografia1, ItemFilme filmografia2) {
                                    return filmografia2.getRelease_date().compareTo(filmografia1.getRelease_date());
                                }
                            });

                            adaptadorFilmografia = new AdaptadorFilmografia(DetalhesPessoasActivity.this, itemFilmografias);
                            recyclerView.setAdapter(adaptadorFilmografia);
                            adaptadorFilmografia.setOnItemClickListenerP(DetalhesPessoasActivity.this);

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
                            String birthday = response.getString("birthday");
                            String knownForDepartment = response.getString("known_for_department");
                            String gender = response.getString("gender");

                            Picasso.get().load(imagePath).placeholder(R.drawable.progress_animation).into(imageView, new Callback() {
                                @Override
                                public void onSuccess() {
                                }

                                @Override
                                public void onError(Exception e) {

                                    imageView.setImageResource(R.mipmap.ic_no_image);
                                }
                            });

                            //TODO: fazer isto em condições!!!!!!!!!!!
                            int currentYear = Calendar.getInstance().get(Calendar.YEAR);
                            int birthYear = 0;
                            int age;
                            try {
                                birthYear = Integer.parseInt(birthday.substring(0, 4));
                                age = currentYear - birthYear;
                                textViewDateOfBirth.setText(birthday + "(" + age + ")");
                            } catch (NumberFormatException e) {
                                textViewDateOfBirth.setText("-");
                            }

                            Log.d(TAG, "onResponse: cur - " + currentYear);
                            Log.d(TAG, "onResponse: birth - " + birthYear);

                            textViewNome.setText(nome);
                            textViewBio.setText(biography);
                            textViewKnownForDepartment.setText(knownForDepartment);

                            if (gender.equals("1")) {

                                textViewGender.setText("Female");
                            } else {

                                textViewGender.setText("male");
                            }

                            textViewBio.post(new Runnable() {
                                @Override
                                public void run() {
                                    int lineCnt = textViewBio.getLineCount();

                                    Log.d(TAG, "onResponse: lines - " + lineCnt);

                                    if(lineCnt <= 4){
                                        button.setVisibility(View.GONE);
                                    }else {
                                        textViewBio.setMaxLines(5);
                                    }
                                }
                            });
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

    @Override
    public void onItemClick(int position) {
        if (adaptadorFilmografia.getMedia(position).equals("movie")) {
            Intent intent = new Intent(this, DetalhesFilmesActivity.class);

            String id = adaptadorFilmografia.getId(position);

            Log.d(TAG, "onItemClick: position - " + position);

            intent.putExtra("id", id);

            Toast.makeText(this, "ID - " + id, Toast.LENGTH_LONG).show();

            startActivity(intent);
        } else {
            Intent intent = new Intent(this, DetalhesTVActivity.class);

            String id = adaptadorFilmografia.getId(position);

            Log.d(TAG, "onItemClick: position - " + position);

            intent.putExtra("id", id);

            Toast.makeText(this, "ID - " + id, Toast.LENGTH_LONG).show();

            startActivity(intent);
        }
    }

    public void Mostrar(View view) {
        if (button.getText().toString().equals("Mostrar mais")) {
            textViewBio.setMaxLines(10000);
            button.setText("Mostrar menos");
        } else {
            textViewBio.setMaxLines(5);
            button.setText("Mostrar mais");
        }
    }
}
