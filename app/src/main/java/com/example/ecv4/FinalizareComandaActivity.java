package com.example.ecv4;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.ecv4.Predominant.Predominant;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

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

        confirmaLivrare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
               Verifica();
            }
        });

    }

    private void Verifica()
    {
        if(TextUtils.isEmpty(numeLivrare.getText().toString()))
        {
            Toast.makeText(FinalizareComandaActivity.this, "Va rog introduceti numele complet", Toast.LENGTH_SHORT).show();

        } else if(TextUtils.isEmpty(telefonLivrare.getText().toString()))
        {
            Toast.makeText(FinalizareComandaActivity.this, "Va rog introduceti numarul de telefon", Toast.LENGTH_SHORT).show();

        } else if(TextUtils.isEmpty(adresaLivrare.getText().toString()))
        {
            Toast.makeText(FinalizareComandaActivity.this, "Va rog introduceti adresa", Toast.LENGTH_SHORT).show();

        } else if(TextUtils.isEmpty(orasLivrare.getText().toString()))
        {
            Toast.makeText(FinalizareComandaActivity.this, "Va rog introduceti orasul", Toast.LENGTH_SHORT).show();

        }
        else
        {
            ConfirmaComanda();
        }
    }

    private void ConfirmaComanda()
    {
        final String salvareTimpCurent,salvareDataCurenta;
        Calendar dataCalendar = Calendar.getInstance();

        SimpleDateFormat dataCurenta = new SimpleDateFormat("MMM dd,yyyy");
        salvareDataCurenta = dataCurenta.format(dataCalendar.getTime());

        SimpleDateFormat timpCurent = new SimpleDateFormat("HH:mm:ss a");
        salvareTimpCurent = timpCurent.format(dataCalendar.getTime());

        final DatabaseReference comenziRef = FirebaseDatabase.getInstance().getReference()
                .child("Comenzi")
                .child(Predominant.utilizatorCurent.getTelefon());

        HashMap<String,Object>comenziMap = new HashMap<>();
        comenziMap.put("sumaTotala",sumaTotala);
        comenziMap.put("nume",numeLivrare.getText().toString());
        comenziMap.put("telefon",telefonLivrare.getText().toString());
        comenziMap.put("adresa",adresaLivrare.getText().toString());
        comenziMap.put("oras",orasLivrare.getText().toString());
        comenziMap.put("data",salvareDataCurenta);
        comenziMap.put("timp",salvareTimpCurent);
        comenziMap.put("status","Neexpediat");


        comenziRef.updateChildren(comenziMap).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task)
            {
                if(task.isSuccessful())
                {
                    FirebaseDatabase.getInstance().getReference().child("Lista Cos")
                            .child("Vizualizare Utilizator")
                            .child(Predominant.utilizatorCurent.getTelefon())
                            .removeValue()
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task)
                                {

                                    if(task.isSuccessful())
                                    {
                                        Toast.makeText(FinalizareComandaActivity.this, "Comanda ta a fost plasata cu success", Toast.LENGTH_SHORT).show();

                                        Intent intent = new Intent(FinalizareComandaActivity.this,AcasaActivity.class);
                                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                        startActivity(intent);
                                        finish();
                                    }

                                }
                            });

                }

            }
        });


    }
}
