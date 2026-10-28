package com.example.kostki;

import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

public class MainActivity extends AppCompatActivity {

    EditText editTextLiczbaKostek;
    TextView textViewWyniki;
    GridLayout gridKostki;
    ArrayList<Integer> wyniki = new ArrayList<>();
    Random random = new Random();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        editTextLiczbaKostek = findViewById(R.id.editTextLiczbaKostek);
        textViewWyniki = findViewById(R.id.textViewWyniki);
        gridKostki = findViewById(R.id.gridKostki);
        Button buttonLosuj = findViewById(R.id.buttonLosuj);
        Button buttonRosnaco = findViewById(R.id.buttonRosnaco);
        Button buttonMalejaco = findViewById(R.id.buttonMalejaco);

        buttonLosuj.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                losuj();
            }
        });

        buttonRosnaco.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (wyniki.isEmpty()) {
                    Toast.makeText(MainActivity.this, "Najpierw wylosuj kostki", Toast.LENGTH_SHORT).show();
                    return;
                }
                Collections.sort(wyniki);
                pokazWyniki();
            }
        });

        buttonMalejaco.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (wyniki.isEmpty()) {
                    Toast.makeText(MainActivity.this, "Najpierw wylosuj kostki", Toast.LENGTH_SHORT).show();
                    return;
                }
                Collections.sort(wyniki, Collections.reverseOrder());
                pokazWyniki();
            }
        });
        if (savedInstanceState != null) {
            ArrayList<Integer> zapisane = savedInstanceState.getIntegerArrayList("wyniki");
            if (zapisane != null) {
                wyniki = zapisane;
                pokazWyniki();
            }
        }
    }

    void losuj() {
        String tekst = editTextLiczbaKostek.getText().toString().trim();
        int liczba;
        try {
            liczba = Integer.parseInt(tekst);
        } catch (NumberFormatException e) {
            editTextLiczbaKostek.setError("Wpisz liczbę całkowitą od 1 do 100");
            return;
        }

        if (liczba < 1 || liczba > 100) {
            editTextLiczbaKostek.setError("Liczba kostek musi być od 1 do 100");
            return;
        }

        editTextLiczbaKostek.setError(null);
        wyniki.clear();
        for (int i = 0; i < liczba; i++) {
            wyniki.add(random.nextInt(6) + 1);
        }
        pokazWyniki();
    }

    void pokazWyniki() {
        gridKostki.removeAllViews();
        if (wyniki.isEmpty()) {
            textViewWyniki.setText("Wyniki pojawią się tutaj");
            return;
        }

        int kolumny = Math.min(5, wyniki.size());
        gridKostki.setColumnCount(kolumny);
        String tekst = "";
        for (int i = 0; i < wyniki.size(); i++) {
            if (i > 0) {
                tekst += ", ";
            }
            tekst += wyniki.get(i);

            TextView kostka = new TextView(this);
            kostka.setText(String.valueOf(wyniki.get(i)));
            kostka.setTextSize(24);
            kostka.setTextColor(android.graphics.Color.BLACK);
            kostka.setGravity(Gravity.CENTER);
            kostka.setBackgroundResource(R.drawable.kostka);
            kostka.setContentDescription("Kostka " + (i + 1) + ": " + wyniki.get(i));

            GridLayout.LayoutParams parametry = new GridLayout.LayoutParams();
            parametry.width = 0;
            parametry.height = (int) (48 * getResources().getDisplayMetrics().density);
            parametry.rowSpec = GridLayout.spec(i / kolumny);
            parametry.columnSpec = GridLayout.spec(i % kolumny, 1f);
            kostka.setLayoutParams(parametry);
            gridKostki.addView(kostka);
        }
        textViewWyniki.setText(tekst);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putIntegerArrayList("wyniki", wyniki);
    }
}
