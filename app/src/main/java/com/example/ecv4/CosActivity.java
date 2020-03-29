package com.example.ecv4;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ecv4.Model.Cos;
import com.example.ecv4.Predominant.Predominant;
import com.example.ecv4.ProdusVizualizare.CosVizualizare;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class CosActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private Button urmatorulProces;
    private TextView sumaTotala;

    private int pretTotalLivrare = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cos);

        recyclerView = findViewById(R.id.cos_lista);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        urmatorulProces = (Button)findViewById(R.id.pas_urmator);
        sumaTotala = (TextView)findViewById(R.id.pret_total);

        urmatorulProces.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                sumaTotala.setText("Pret Total : "  + String.valueOf(pretTotalLivrare));

                Intent intent = new Intent(CosActivity.this,FinalizareComandaActivity.class);
                intent.putExtra("Pret Total",String.valueOf(pretTotalLivrare));
                startActivity(intent);
                finish();

            }
        });

    }

    @Override
    protected void onStart()
    {
        super.onStart();

        final DatabaseReference cosListaRef = FirebaseDatabase.getInstance().getReference().child("Lista Cos");
        FirebaseRecyclerOptions<Cos> options = new FirebaseRecyclerOptions.Builder<Cos>()
                .setQuery(cosListaRef.child("Vizualizare Utilizator")
                .child(Predominant.utilizatorCurent.getTelefon())
                        .child("Produse"),Cos.class)
                        .build();

        FirebaseRecyclerAdapter<Cos, CosVizualizare> adapter
                = new FirebaseRecyclerAdapter<Cos, CosVizualizare>(options) {
            @Override
            protected void onBindViewHolder(@NonNull CosVizualizare holder, int position, @NonNull final Cos model)
            {
                  holder.produsCantitate.setText( "Cantitate " + model.getCantitate());
                  holder.produsNume.setText(model.getProdusnume());
                  holder.produsPret.setText( "Pret " + model.getPret() + "RON");

                  int pretTotalPerProdus = ((Integer.valueOf(model.getPret())))*Integer.valueOf(model.getCantitate());
                  pretTotalLivrare = pretTotalLivrare + pretTotalPerProdus;

                  holder.itemView.setOnClickListener(new View.OnClickListener() {
                      @Override
                      public void onClick(View v)
                      {

                          CharSequence optiuni[] = new CharSequence[]
                                  {
                                         "Editare",
                                         "Stergere"
                                  };

                          AlertDialog.Builder builder = new AlertDialog.Builder(CosActivity.this);
                          builder.setTitle("Selectare Optiuni Cos");

                          builder.setItems(optiuni, new DialogInterface.OnClickListener() {
                              @Override
                              public void onClick(DialogInterface dialog, int which)
                              {
                                  if(which == 0)
                                  {
                                      Intent intent = new Intent(CosActivity.this,ProdusDetaliiActivity.class);
                                      intent.putExtra("produsid",model.getProdusid());
                                      startActivity(intent);

                                  }

                                  if(which == 1)
                                  {
                                     cosListaRef.child("Vizualizare Utilizator")
                                             .child(Predominant.utilizatorCurent.getTelefon())
                                             .child("Produse")
                                             .child(model.getProdusid())
                                             .removeValue()
                                             .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                 @Override
                                                 public void onComplete(@NonNull Task<Void> task)
                                                 {
                                                     
                                                     if(task.isSuccessful())
                                                     {
                                                         Toast.makeText(CosActivity.this, "Produs sters cu success !", Toast.LENGTH_SHORT).show();

                                                         Intent intent = new Intent(CosActivity.this,AcasaActivity.class);
                                                         startActivity(intent);
                                                     }
                                                 }
                                             });
                                  }

                              }
                          });

                          builder.show();

                      }
                  });
            }

            @NonNull
            @Override
            public CosVizualizare onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
            {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cos_comenzi,parent,false);
                CosVizualizare holder = new CosVizualizare(view);
                return holder;
            }
        };

        recyclerView.setAdapter(adapter);
        adapter.startListening();

    }
}
