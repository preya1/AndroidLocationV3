package com.example.ecv4;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.ecv4.Model.Cos;
import com.example.ecv4.Predominant.Predominant;
import com.example.ecv4.ProdusVizualizare.CosVizualizare;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class CosActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private Button urmatorulProces;
    private TextView sumaTotala;


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
            protected void onBindViewHolder(@NonNull CosVizualizare holder, int position, @NonNull Cos model)
            {
                  holder.produsCantitate.setText( "Cantitate" + model.getCantitate());
                  holder.produsNume.setText(model.getProdusnume());
                  holder.produsPret.setText( "Pret" + model.getPret() + "RON");
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
