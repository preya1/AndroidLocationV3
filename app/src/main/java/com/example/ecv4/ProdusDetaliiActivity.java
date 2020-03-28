package com.example.ecv4;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.cepheuen.elegantnumberbutton.view.ElegantNumberButton;
import com.example.ecv4.Model.Produse;
import com.example.ecv4.Predominant.Predominant;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

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

        adaugaCosBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {

                adaugareCosLista();

            }
        });

    }

    private void adaugareCosLista()
    {
        String salvareTimpCurent,salvareDataCurenta;
        Calendar dataCalendar = Calendar.getInstance();

        SimpleDateFormat dataCurenta = new SimpleDateFormat("MMM dd,yyyy");
        salvareDataCurenta = dataCurenta.format(dataCalendar.getTime());

        SimpleDateFormat timpCurent = new SimpleDateFormat("HH:mm:ss a");
        salvareTimpCurent = timpCurent.format(dataCalendar.getTime());

        final DatabaseReference listaCosRef = FirebaseDatabase.getInstance().getReference().child("Lista Cos");

        final HashMap<String ,Object> cosMap = new HashMap<>();
        cosMap.put("produsid",produsID);
        cosMap.put("produsnume",produsNume.getText().toString());
        cosMap.put("pret",produsPret.getText().toString());
        cosMap.put("data",salvareDataCurenta);
        cosMap.put("timp",salvareTimpCurent);
        cosMap.put("cantitate",numarBtn.getNumber());
        cosMap.put("discount","");

        listaCosRef.child("Vizualizare Utilizator")
                .child(Predominant.utilizatorCurent.getTelefon()).child("Produse").child(produsID)
                .updateChildren(cosMap)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task)
                    {
                        if(task.isSuccessful())
                        {
                            listaCosRef.child("Vizualizare Admin")
                                    .child(Predominant.utilizatorCurent.getTelefon()).child("Produse").child(produsID)
                                    .updateChildren(cosMap)
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task)
                                        {

                                            if(task.isSuccessful())
                                            {
                                                Toast.makeText(ProdusDetaliiActivity.this,"Adaugat in Lista Cos",Toast.LENGTH_LONG).show();
                                                Intent intent = new Intent(ProdusDetaliiActivity.this,AcasaActivity.class);
                                                startActivity(intent);
                                            }

                                        }
                                    });

                        }

                    }
                });

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
