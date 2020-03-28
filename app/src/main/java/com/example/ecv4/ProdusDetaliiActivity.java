package com.example.ecv4;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.cepheuen.elegantnumberbutton.view.ElegantNumberButton;
import com.example.ecv4.Model.Produse;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

public class ProdusDetaliiActivity extends AppCompatActivity {

    private FloatingActionButton adaugaCosBtn;
    private ImageView produsImagine;
    private ElegantNumberButton numarBtn;
    private TextView produsPret,produsDescriere,produsNume;
    private String produsID = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_produs_detalii);

        produsID = getIntent().getStringExtra("produsid");

        adaugaCosBtn = (FloatingActionButton)findViewById(R.id.adauga_produs_cos);
        produsImagine = (ImageView)findViewById(R.id.produs_imagine_detalii);
        numarBtn = (ElegantNumberButton)findViewById(R.id.elegant_btn);

        produsPret = (TextView)findViewById(R.id.pret_produs_detalii);
        produsNume = (TextView)findViewById(R.id.nume_produs_detalii);
        produsDescriere = (TextView)findViewById(R.id.descriere_produs_detalii);


        preiaDetaliiProdus(produsID);

    }

    private void preiaDetaliiProdus(final String produsID)
    {
        DatabaseReference produsRef = FirebaseDatabase.getInstance().getReference().child("Produse");

        produsRef.child(produsID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot)
            {
                if(dataSnapshot.exists())
                {
                    Produse produse = dataSnapshot.getValue(Produse.class);

                    produsNume.setText(produse.getProdusnume());
                    produsPret.setText(produse.getPret());
                    produsDescriere.setText(produse.getDescriere());
                    Picasso.get().load(produse.getImagine()).into(produsImagine);

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }
}
