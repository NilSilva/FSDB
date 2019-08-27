package nil.s.fsdb;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
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

import java.util.Calendar;
import java.util.Locale;

public class DetalhesPessoasActivity extends AppCompatActivity {

    private String TAG = "DetalhesPessoasActivity";

    private String id;

    private RequestQueue requestQueue;

    private ImageView imageView;

    private TextView textViewNome;
    private TextView textViewBio;
    private TextView textViewDateOfBirth;
    private TextView textViewKnownForDepartment;
    private TextView textViewGender;

    private Context context = this;

    private TableLayout tb;

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

        tb = findViewById(R.id.tableLayoutDetalhesPessoasFilmografia);

        TableRow tr = new TableRow(this);
        TextView c1 = new TextView(this);
        TextView c2 = new TextView(this);
        TextView c3 = new TextView(this);
        TextView c4 = new TextView(this);

        c1.setBackgroundResource(R.drawable.border);
        c1.setText("Year");
        c2.setBackgroundResource(R.drawable.border);
        c2.setText("Type");
        c3.setBackgroundResource(R.drawable.border);
        c3.setText("Name");
        c4.setBackgroundResource(R.drawable.border);
        c4.setText("Character");

        tr.addView(c1);
        tr.addView(c2);
        tr.addView(c3);
        tr.addView(c4);

        tb.addView(tr);

        Intent intent = getIntent();

        id = intent.getStringExtra("id");

        requestQueue = Volley.newRequestQueue(this);

        parseJSON();

        popularTabela();
    }

    private void popularTabela() {

        final String iso_3166_1 = Locale.getDefault().getDisplayLanguage();
        String language = iso_3166_1 + "-" + iso_3166_1.toUpperCase();
        Log.d(TAG, "lang - " + language);

        String url = "https://api.themoviedb.org/3/person/" + id + "/combined_credits?api_key=" + ChaveAPI.TMDb;
        Log.d(TAG, "url - " + url);

        final JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {

                            JSONArray jsonArray = response.getJSONArray("cast");

                            for (int i = 0; i < jsonArray.length(); i++) {

                                JSONObject jsonObject = jsonArray.getJSONObject(i);

                                String media = jsonObject.getString("media_type");
                                Log.d(TAG, "onResponse: media - " + media);

                                String ano;
                                String title;
                                if (media.equals("movie")) {

                                    try {
                                        ano = jsonObject.getString("release_date").substring(0, 4);
                                    } catch (Exception e) {
                                        ano = "n/a";
                                    }
                                    Log.d(TAG, "onResponse: ano - " + ano);

                                    title = jsonObject.getString("title");
                                    Log.d(TAG, "onResponse: title - " + title);
                                } else {

                                    try {
                                        ano = jsonObject.getString("first_air_date").substring(0, 4);
                                    } catch (Exception e) {
                                        ano = "n/a";
                                    }
                                    Log.d(TAG, "onResponse: ano - " + ano);

                                    title = jsonObject.getString("name");
                                    Log.d(TAG, "onResponse: title - " + title);
                                }

                                String character = jsonObject.getString("character");
                                Log.d(TAG, "onResponse: char - " + character);

                                TableRow tr = new TableRow(context);
                                tr.setBackgroundResource(R.drawable.border);

                                TextView c1 = new TextView(context);
                                TextView c2 = new TextView(context);
                                TextView c3 = new TextView(context);
                                TextView c4 = new TextView(context);

                                c1.setBackgroundResource(R.drawable.border);
                                c1.setGravity(Gravity.CENTER);
                                c1.setWidth(100);
                                c1.setText(ano);

                                c2.setBackgroundResource(R.drawable.border);
                                c2.setGravity(Gravity.CENTER);
                                c1.setWidth(100);
                                c2.setText(media);

                                c3.setBackgroundResource(R.drawable.border);
                                c3.setGravity(Gravity.CENTER);
                                c3.setText(title);

                                c4.setBackgroundResource(R.drawable.border);
                                c4.setGravity(Gravity.CENTER);
                                c4.setText(character);

                                tr.addView(c1);
                                tr.addView(c2);
                                tr.addView(c3);
                                tr.addView(c4);

                                Log.d(TAG, "onResponse: tr - " + tr.getChildCount());

                                tb.addView(tr, new TableLayout.LayoutParams(TableLayout.LayoutParams.MATCH_PARENT, TableLayout.LayoutParams.WRAP_CONTENT));

                                Log.d(TAG, "onResponse: tb - " + tb.getChildCount());
                            }

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

                            Picasso.get().load(imagePath).placeholder(R.drawable.progress_animation).into(imageView);

                            //TODO: fazer isto em condições!!!!!!!!!!!
                            int currentYear = Calendar.getInstance().get(Calendar.YEAR);
                            int birthYear = Integer.parseInt(birthday.substring(0, 4));
                            int age = currentYear - birthYear;
                            Log.d(TAG, "onResponse: cur - " + currentYear);
                            Log.d(TAG, "onResponse: birth - " + birthYear);

                            textViewNome.setText(nome);
                            textViewBio.setText(biography);
                            textViewDateOfBirth.setText(birthday + "(" + age + ")");
                            textViewKnownForDepartment.setText(knownForDepartment);

                            if (gender.equals("1")) {

                                textViewGender.setText("Female");
                            } else {

                                textViewGender.setText("male");
                            }

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
