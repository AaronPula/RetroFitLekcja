package com.onaar.retrofitlekcja;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {

//    https://my-json-server.typicode.com/AaronPula/RetroFitLekcja/pytania

    ArrayList<Pytanie> pytania;
    ArrayList<RadioButton> radioButtons;
    ArrayList<View> questionObjs;
    int[] odpowiedzi;

    LinearLayout llKoniec;

    TextView tvQuestion, tvKoniec;
    RadioGroup rg;
    Button btnAnswer, btnPokaz, btnZakoncz;

    int nrPyt, points;

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

        rg = findViewById(R.id.rg);
        tvQuestion = findViewById(R.id.tvQuestion);
        tvKoniec = findViewById(R.id.tvKoniec);
        btnAnswer = findViewById(R.id.btnAnswer);
        radioButtons = new ArrayList<RadioButton>();
        btnPokaz = findViewById(R.id.btnPokaz);
        btnZakoncz = findViewById(R.id.btnZakoncz);
        llKoniec = findViewById(R.id.llKoniec);
        
        questionObjs = new ArrayList<View>(List.of(
                rg,
                tvQuestion,
                btnAnswer,
                btnZakoncz
        ));

        nrPyt = 0;
        points = 0;

        for (int i = 0; i < rg.getChildCount(); i++) {
            View view = rg.getChildAt(i);
            if (view instanceof RadioButton) {
                radioButtons.add((RadioButton) view);
            }
        }



        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://raw.githubusercontent.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        JsonPlaceHolderApi jsonPlaceHolderApi = retrofit.create(JsonPlaceHolderApi.class);

        Call<ArrayList<Pytanie>> call = jsonPlaceHolderApi.getPytania();

        btnPokaz.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        nrPyt = 0;
                        wyswietlPytanie(0);
                        for (View obj :
                                questionObjs) {
                            obj.setVisibility(View.VISIBLE);
                        }
                        llKoniec.setVisibility(View.GONE);
                    }
                }
        );

        btnZakoncz.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        for (int i = 0; i < odpowiedzi.length; i++) {
                            points = 0;
                            sprawdzOdp(i, odpowiedzi[i]);
                        }
                        tvKoniec.setText("Liczba punktów: " + String.valueOf(points));
                        llKoniec.setVisibility(View.VISIBLE);
                        for (View obj :
                                questionObjs) {
                            obj.setVisibility(View.GONE);
                        }
                    }
                }
        );

        btnAnswer.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        sprawdzOdp();

                        if (nrPyt < pytania.size() - 1) {
                            nrPyt++;
                            wyswietlPytanie(nrPyt);
                            for (RadioButton rbObj :
                                    radioButtons) {
                                rbObj.setChecked(false);
                            }
                        }
                        else {
                            for (View obj :
                                    questionObjs) {
                                obj.setVisibility(View.GONE);
                            }
                            llKoniec.setVisibility(View.VISIBLE);
                            tvKoniec.setText("Liczba punktów: " + String.valueOf(points));
                        }
                    }
                }
        );

        call.enqueue(
                new Callback<ArrayList<Pytanie>>() {
                    @Override
                    public void onResponse(Call<ArrayList<Pytanie>> call, Response<ArrayList<Pytanie>> response) {
                        if (!response.isSuccessful()) {
                            Toast.makeText(MainActivity.this, response.code(), Toast.LENGTH_SHORT).show();
                            return;
                        }
                        pytania = response.body();

                        Toast.makeText(MainActivity.this, "Pomyślnie pobrano pytania: " + pytania.size(), Toast.LENGTH_SHORT).show();
                        odpowiedzi = new int[pytania.size()];
                        Arrays.fill(odpowiedzi, -1);

                        wyswietlPytanie(0);
                        for (View obj :
                                questionObjs) {
                            obj.setVisibility(View.VISIBLE);
                        }
                    }

                    @Override
                    public void onFailure(Call<ArrayList<Pytanie>> call, Throwable t) {
                        Toast.makeText(MainActivity.this, t.getMessage(), Toast.LENGTH_LONG).show();
                    }
                }
        );
    }

    private void sprawdzOdp() {
        RadioButton zaznaczony = findViewById(rg.getCheckedRadioButtonId());
        int odpowiedz = rg.indexOfChild(zaznaczony);
        if (!zaznaczony.isChecked()) {
            odpowiedzi[nrPyt] = -1;
        }
        else if (odpowiedz == pytania.get(nrPyt).poprawna) {
            points++;
            Toast.makeText(this, "Poprawna odpowiedź", Toast.LENGTH_SHORT).show();
        }
        else {
            Toast.makeText(this, "Niepoprawna odpowiedź", Toast.LENGTH_SHORT).show();
        }
        odpowiedzi[nrPyt] = odpowiedz;
    }
    private void sprawdzOdp(int nrPyt2, int odp) {
        if (odp == -1) {
            odpowiedzi[nrPyt2] = -1;
        }
        else if (odp == pytania.get(nrPyt2).poprawna) {
            points++;
//            Toast.makeText(this, "Poprawna odpowiedź", Toast.LENGTH_SHORT).show();
        }
        else {
//            Toast.makeText(this, "Niepoprawna odpowiedź", Toast.LENGTH_SHORT).show();
        }
        odpowiedzi[nrPyt2] = odp;
    }

    private void wyswietlPytanie(int id) {
        tvQuestion.setText(pytania.get(id).tresc);
        radioButtons.get(0).setText(pytania.get(id).odp_a);
        radioButtons.get(1).setText(pytania.get(id).odp_b);
        radioButtons.get(2).setText(pytania.get(id).odp_c);
        radioButtons.get(3).setText(pytania.get(id).odp_d);

        if (odpowiedzi[id] != -1){
            for (RadioButton rbObj :
                    radioButtons) {
                rbObj.setChecked(false);
            }

            radioButtons.get(odpowiedzi[id]).setChecked(true);
        }
    }
}