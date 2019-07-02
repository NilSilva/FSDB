package nil.s.fsdb;

import androidx.appcompat.app.AppCompatActivity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class MainActivity extends AppCompatActivity {
    //TODO: dar credito à fonte da base de dados
    /*==============================================================================================
        Declaração dos objetos
     =============================================================================================*/

    private final String TAG = "MainActivity"; //TAG para os logs

    private ProgressBar progressBar;

    private EditText editText;

    private TextView textView;

    private Button button;

    private final String API_URL_INICIO = "http://www.omdbapi.com/?t=";
    private final String API_URL_FIM = "&apikey=f68d82ec";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        /*==========================================================================================
            Criação dos objetos
         =========================================================================================*/
        progressBar = findViewById(R.id.progressBar);

        editText = findViewById(R.id.editTextNome);

        textView = findViewById(R.id.textViewResposta);

        button = findViewById(R.id.buttonProcurar);

        //fazer com que o butão fassa algo
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new RecolherDados().execute();
            }
        });
    }

    class RecolherDados extends AsyncTask<Void, Void, String>{
        private Exception exception;

        protected void onPreExecute() {
            progressBar.setVisibility(View.VISIBLE);
            textView.setText("");
        }

        protected String doInBackground(Void... urls) {
            String nome = editText.getText().toString().trim();

            try{
                URL url = new URL("https://api.themoviedb.org/3/search/movie?api_key=c816441df0108db98214080d85446617&query=" + nome);
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();

                try{
                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
                    StringBuilder stringBuilder = new StringBuilder();
                    String line;
                    while((line = bufferedReader.readLine()) != null){
                        stringBuilder.append(line).append("\n");
                    }
                    bufferedReader.close();
                    return stringBuilder.toString();
                }finally {
                    urlConnection.disconnect();
                }
            }catch (Exception e){
                Log.e(TAG, e.getMessage(), e);
            }

            return null;
        }

        @Override
        protected void onPostExecute(String res) {
            if(res == null){
                res = "There was an error";
            }
            progressBar.setVisibility(View.GONE);
            Log.i(TAG, res);
            textView.setText(res);
        }
    }
}
