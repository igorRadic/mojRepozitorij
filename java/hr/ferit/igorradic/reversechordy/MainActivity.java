package hr.ferit.igorradic.reversechordy;


import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;


public class MainActivity extends AppCompatActivity {

    public static final String EXTRA_TEXT = "hr.ferit.igorradic.reversechordy";
    public String output;

    private Databasehelper db;
    Button guitar, keyboard, other, search;
    EditText entry;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        guitar = findViewById(R.id.btnGtr);
        keyboard = findViewById(R.id.btnKey);
        other = findViewById(R.id.btnOther);
        search = findViewById(R.id.btnEnter);
        entry = findViewById(R.id.editTextNotes);


        db = new Databasehelper(MainActivity.this);
        try {
            db.createDataBase();
        } catch (Exception e) {
            e.printStackTrace();
        }


        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String input = entry.getText().toString();

                if(input.isEmpty()){
                    Toast toast = Toast.makeText(getApplicationContext(),"Enter notes.", Toast.LENGTH_LONG);
                    toast.show();
                }
                else {

                    db.openDataBase();
                    output = db.SearchForChord(input);
                    db.close();

                    if(output.matches("No result.")){
                        Toast toast = Toast.makeText(getApplicationContext(),"No result.", Toast.LENGTH_LONG);
                        toast.show();
                    }
                    else if(output.matches("Error.")){
                        Toast toast = Toast.makeText(getApplicationContext(),"Too many notes or no spaces between them, try again.", Toast.LENGTH_LONG);
                        toast.show();
                    }
                    else {
                        openResultActivity();
                    }
                }
            }
        });

        // da se prikazu uputstva na pocetku
        Fragment fragment = new Intro();
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.prikazFragment, fragment).commit();


        guitar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Fragment fragment = new GuitarFragment();
                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.prikazFragment, fragment).commit();
            }

        });

        keyboard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Fragment fragment = new KeyboardFragment();
                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.prikazFragment, fragment).commit();

            }

        });


        other.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Fragment fragment = new OtherFragment();
                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.prikazFragment, fragment).commit();

            }

        });

    }

    public void openResultActivity(){
        //za otvaranje novog activitija
        Intent intent = new Intent(this, ResultActivity.class);
        intent.putExtra(EXTRA_TEXT, output);
        startActivity(intent);
    }

}
