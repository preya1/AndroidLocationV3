package com.example.ecv4;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.cepheuen.elegantnumberbutton.view.ElegantNumberButton;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class ProdusDetaliiActivity extends AppCompatActivity {

    private FloatingActionButton adaugaCosBtn;
    private ImageView produsImagine;
    private ElegantNumberButton numarBtn;
    private TextView produsPret,produsDescriere,produsNume;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_produs_detalii);

        adaugaCosBtn = (FloatingActionButton)findViewById(R.id.adauga_produs_cos);
        produsImagine = (ImageView)findViewById(R.id.produs_imagine_detalii);
        numarBtn = (ElegantNumberButton)findViewById(R.id.elegant_btn);

        produsPret = (TextView)findViewById(R.id.pret_produs_detalii);
        produsNume = (TextView)findViewById(R.id.nume_produs_detalii);
        produsDescriere = (TextView)findViewById(R.id.descriere_produs_detalii);

    }
}
