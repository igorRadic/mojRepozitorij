package hr.ferit.igorradic.reversechordy;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ResultActivity extends AppCompatActivity {

    private static final String TAG = "ResultActivity";

    private static final String BASE_URL = "https://jsondatabaseofchords.000webhostapp.com/";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);

        Intent intent = getIntent();
        final String output = intent.getStringExtra(MainActivity.EXTRA_TEXT);

        TextView chord_name = (TextView) findViewById(R.id.resultTV);
        final TextView about = (TextView) findViewById(R.id.about);

        chord_name.setText(output);



        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        Api api = retrofit.create(Api.class);


        Call<List<Theory>> call = api.getInfo();

        call.enqueue(new Callback<List<Theory>>(){
            @Override
            public void onResponse(Call<List<Theory>> call, Response<List<Theory>> response){
                Log.d(TAG, "onResponse: Server Response: " + response.toString());
                Log.d(TAG, "onResponse: recieved information: " + response.body().toString());

                //https://jsondatabaseofchords.000webhostapp.com/chords.json     moja stranica sa podatcima
                List<Theory> theoryList = response.body(); // za dohvacanje sa neta u listu


                if(output.contains("major 7")){
                    about.setText(theoryList.get(2).getAbout());
                    about.setVisibility(View.VISIBLE);
                }
                else if(output.contains("major")){
                    about.setText(theoryList.get(0).getAbout());
                    about.setVisibility(View.VISIBLE);
                }
                else if(output.contains("minor 7")){
                    about.setText(theoryList.get(3).getAbout());
                    about.setVisibility(View.VISIBLE);
                }
                else if(output.contains("minor")){
                    about.setText(theoryList.get(1).getAbout());
                    about.setVisibility(View.VISIBLE);
                }
                else if(output.contains("half-diminished")){
                    about.setText(theoryList.get(8).getAbout());
                    about.setVisibility(View.VISIBLE);
                }
                else if(output.contains("diminished 7")){
                    about.setText(theoryList.get(5).getAbout());
                    about.setVisibility(View.VISIBLE);
                }
                else if(output.contains("diminished")){
                    about.setText(theoryList.get(4).getAbout());
                    about.setVisibility(View.VISIBLE);
                }
                else if(output.contains("augmented")){
                    about.setText(theoryList.get(7).getAbout());
                    about.setVisibility(View.VISIBLE);
                }
                else if(output.contains("dominant 7")){
                    about.setText(theoryList.get(6).getAbout());
                    about.setVisibility(View.VISIBLE);
                }



            }


            @Override
            public void onFailure(Call<List<Theory>> call, Throwable t){
                Log.e(TAG, "onFailure: Something went wrong: " + t.getMessage());
                Toast.makeText(ResultActivity.this, "Something went wrong with description. Check your WiFi.", Toast.LENGTH_SHORT).show();
            }



        });
    }
}
