package com.example.ecv4;

import android.content.Intent;
import android.os.Bundle;

import com.example.ecv4.Model.Produse;
import com.example.ecv4.Predominant.Predominant;
import com.example.ecv4.ProdusVizualizare.ProdusVizualizare;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import android.view.LayoutInflater;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.core.view.GravityCompat;
import androidx.appcompat.app.ActionBarDrawerToggle;

import android.view.MenuItem;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import androidx.drawerlayout.widget.DrawerLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.Menu;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import static androidx.recyclerview.widget.RecyclerView.*;

public class AcasaActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private DatabaseReference ProdusRef;
    private RecyclerView recyclerView;
    LayoutManager layoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ProdusRef = FirebaseDatabase.getInstance().getReference().child("Produse");

        setContentView(R.layout.activity_acasa);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);

        View headerView = navigationView.getHeaderView(0);
        TextView utilizatorTextView = headerView.findViewById(R.id.utilizator_nume);
        ImageView profilImageView = headerView.findViewById(R.id.utilizator_poza);

        utilizatorTextView.setText(Predominant.utilizatorCurent.getNume());

        recyclerView = findViewById(R.id.scroll_menu);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
    }

    @Override
    protected void onStart() {
        super.onStart();

        FirebaseRecyclerOptions<Produse> options = new
                FirebaseRecyclerOptions.Builder<Produse>()
                .setQuery(ProdusRef,Produse.class)
                .build();


        FirebaseRecyclerAdapter<Produse, ProdusVizualizare> adapter = new FirebaseRecyclerAdapter<Produse, ProdusVizualizare>(options) {
            @Override
            protected void onBindViewHolder(@NonNull ProdusVizualizare holder, int position, @NonNull Produse model) {

                holder.txtProdusNume.setText(model.getProdusnume());
                holder.txtProdusDescriere.setText(model.getDescriere());
                holder.txtProdusPret.setText(" Pret " + model.getPret() + " RON ");
                Picasso.get().load(model.getImagine()).into(holder.imagineView);

            }

            @NonNull
            @Override
            public ProdusVizualizare onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.produse_design, parent, false);
                ProdusVizualizare holder = new ProdusVizualizare(view);
                return holder;

            }
        };


        recyclerView.setAdapter(adapter);
adapter.startListening();

    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.acasa, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
      //  if (id == R.id.action_settings) {
       //     return true;
     //   }

        return super.onOptionsItemSelected(item);
    }

    private void delogare(){
        FirebaseAuth.getInstance().signOut();

        Intent intent = new Intent(getApplicationContext(),MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }


    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_cos) {
            // Handle the camera action

        } else if (id == R.id.nav_categorii) {

        } else if (id == R.id.nav_comenzi) {

        } else if (id == R.id.nav_setari) {

            Intent intent = new Intent(AcasaActivity.this,SetariActivity.class );
            startActivity(intent);

        } else if (id == R.id.nav_delogare) {

            delogare();
        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
