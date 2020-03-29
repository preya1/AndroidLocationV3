package com.example.ecv4;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class FinalizareComandaActivity extends AppCompatActivity {

    private EditText numeLivrare,telefonLivrare,adresaLivrare,orasLivrare;
    private Button confirmaLivrare;

    private String sumaTotala = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_finalizare_comanda);

        confirmaLivrare = (Button)findViewById(R.id.confirma_livrare);

        numeLivrare = (EditText)findViewById(R.id.nume_livrare);
        telefonLivrare = (EditText)findViewById(R.id.setare_telefon_livrare);
        adresaLivrare = (EditText)findViewById(R.id.setare_adresa_livrare);
        orasLivrare = (EditText)findViewById(R.id.setare_oras_livrare);

        sumaTotala = getIntent().getStringExtra("Pret Total");
        Toast.makeText(FinalizareComandaActivity.this,"Pret Total : " +sumaTotala + "RON",Toast.LENGTH_SHORT).show();

    }
}
