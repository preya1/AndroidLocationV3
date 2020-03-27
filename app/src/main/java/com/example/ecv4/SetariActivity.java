package com.example.ecv4;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ecv4.Predominant.Predominant;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;

import java.util.HashMap;
import java.util.Objects;

import static android.widget.Toast.*;
import static android.widget.Toast.makeText;

@SuppressWarnings("ALL")
public class SetariActivity extends AppCompatActivity {

    private ImageView profilImageView ;
    private EditText editareNume,editareTelefon,editareAdresa;
    private TextView schimbareProfil,inchideText,actualizeazaText;

    private Uri ImagineUri;
    private String url = "";
    private StorageReference stochareImagineProfilRef;
    private StorageTask actualizareSarcina;
    private String verificare = "";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setari);

        stochareImagineProfilRef = FirebaseStorage.getInstance().getReference().child("Poze profil");

        profilImageView = (ImageView) findViewById(R.id.utilizator_poza);

        editareNume =(EditText)findViewById(R.id.setare_nume);
        editareTelefon = (EditText)findViewById(R.id.setare_telefon);
        editareAdresa = (EditText)findViewById(R.id.setare_adresa);

        schimbareProfil = (TextView)findViewById(R.id.schimbare_profil_imagine);
        inchideText = (TextView)findViewById(R.id.inchide_setari);
        actualizeazaText = (TextView)findViewById(R.id.actualizeaza_setari);

        afisareUtilizatorInfo(profilImageView,editareTelefon,editareNume,editareAdresa);

        inchideText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                finish();

            }
        });

        actualizeazaText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {

                if(verificare.equals("apasat"))
                {

                    salvareUtilizatorInfo();

                }
                else
                {

                    actualizareUtilizatorInfoFaraPoza();
                }

            }
        });

        schimbareProfil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                verificare = "apasat";

                CropImage.activity(ImagineUri)
                        .setAspectRatio(1,1)
                        .start(SetariActivity.this);

            }
        });
    }

    private void actualizareUtilizatorInfoFaraPoza()
    {

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("Utilizatori");

        HashMap<String,Object> utilizatorMap = new HashMap<>();
        utilizatorMap.put("nume",editareNume.getText().toString());
        utilizatorMap.put("telefonComanda",editareTelefon.getText().toString());
        utilizatorMap.put("adresa",editareAdresa.getText().toString());
        ref.child(Predominant.utilizatorCurent.getTelefon()).updateChildren(utilizatorMap);

        startActivity(new Intent(SetariActivity.this,MainActivity.class));
        Toast.makeText(SetariActivity.this,"Profil actualizat cu success ", LENGTH_LONG).show();
        finish();
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data)
    {

        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE && resultCode == RESULT_OK && data!=null)
        {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            ImagineUri = result.getUri();

            profilImageView.setImageURI(ImagineUri);
        }
        else
        {
            makeText(SetariActivity.this,"Eroare la incarcare imagine", LENGTH_LONG).show();

            startActivity(new Intent(SetariActivity.this,SetariActivity.class));
            finish();
        }
    }

    private void salvareUtilizatorInfo()
    {
        if(TextUtils.isEmpty(editareNume.getText().toString()))
        {
            makeText(this,"Numele este obligatoriu", LENGTH_LONG).show();
        }
        else  if(TextUtils.isEmpty(editareTelefon.getText().toString()))
        {
            makeText(this,"Telefonul este obligatoriu", LENGTH_LONG).show();
        }
        else  if(TextUtils.isEmpty(editareAdresa.getText().toString()))
        {
            makeText(this,"Adresa este obligatorie", LENGTH_LONG).show();
        }
        else if(verificare.equals("apasat"))
        {
            actualizareImagine();
        }
    }

    private void actualizareImagine()
    {
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Actualizeaza Profilul");
        progressDialog.setMessage("Asteptati cat timp va actualizam informatiile contului");
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();

        if(ImagineUri!=null)
        {
            final StorageReference fisierRef = stochareImagineProfilRef
                    .child(Predominant.utilizatorCurent.getTelefon() + ".jpg");


            actualizareSarcina = fisierRef.putFile(ImagineUri);

            actualizareSarcina.continueWithTask(new Continuation() {
                @Override
                public Object then(@NonNull Task task) throws Exception
                {
                    if(!task.isSuccessful())
                    {
                        throw task.getException();
                    }

                    return fisierRef.getDownloadUrl();

                }
            })
            .addOnCompleteListener(new OnCompleteListener<Uri>() {
                @Override
                public void onComplete(@NonNull Task<Uri> task)
                {

                    if(task.isSuccessful())
                    {
                        Uri descarcaUrl = task.getResult();
                        url = descarcaUrl.toString();

                        DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("Utilizatori");

                        HashMap<String,Object> utilizatorMap = new HashMap<>();
                        utilizatorMap.put("nume",editareNume.getText().toString());
                        utilizatorMap.put("telefonComanda",editareTelefon.getText().toString());
                        utilizatorMap.put("adresa",editareAdresa.getText().toString());
                        utilizatorMap.put("imagine",url);
                        ref.child(Predominant.utilizatorCurent.getTelefon()).updateChildren(utilizatorMap);

                        progressDialog.dismiss();

                        startActivity(new Intent(SetariActivity.this,MainActivity.class));
                        Toast.makeText(SetariActivity.this,"Profil actualizat cu success ", LENGTH_LONG).show();
                        finish();
                    }

                    else
                    {
                        progressDialog.dismiss();
                        Toast.makeText(SetariActivity.this,"Eroare", LENGTH_LONG).show();
                    }

                }
            });
        }

        else
        {
            makeText(this,"Imaginea nu este selectata", LENGTH_LONG).show();
        }
    }


    private void afisareUtilizatorInfo(final ImageView profilImageView, final EditText editareTelefon, final EditText editareNume, final EditText editareAdresa)
    {

        DatabaseReference UtilizatorRef = FirebaseDatabase.getInstance().getReference().child("Utilizatori").child(Predominant.utilizatorCurent.getTelefon());

        UtilizatorRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot)
            {
                //Daca numarul de telefon exista

                if(dataSnapshot.exists())
                {

                    //Daca imaginea exista pentru utilizatorul curent
                    //La parametrii constructorului nu avem si imagine pentru utilizator

                    if(dataSnapshot.child("imagine").exists())
                    {
                        //Vom prelua imaginea

                        String imagine = dataSnapshot.child("imagine").getValue().toString();
                        String nume = dataSnapshot.child("nume").getValue().toString();
                        String telefon = dataSnapshot.child("telefon").getValue().toString();
                        String adresa = dataSnapshot.child("adresa").getValue().toString();

                        Picasso.get().load(imagine).into(profilImageView);

                        editareNume.setText(nume);
                        editareTelefon.setText(telefon);
                        editareAdresa.setText(adresa);

                    }
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }
}
