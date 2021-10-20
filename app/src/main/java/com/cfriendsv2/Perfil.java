package com.cfriendsv2;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Perfil extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private DatabaseReference mDataBase;
    private ImageView fotoperfil;
    private TextView tvnombre, tvperfil;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perfil);

         mDataBase = FirebaseDatabase.getInstance().getReference();
         mAuth = FirebaseAuth.getInstance();
         fotoperfil = (ImageView)findViewById(R.id.imagenPerfil);
         tvnombre = (TextView)findViewById(R.id.tvNombre);
         tvperfil = (TextView)findViewById(R.id.tvEmail);

        String id = mAuth.getCurrentUser().getUid().toString();
        mDataBase.child("user").child(id).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    String nombre = snapshot.child("nombre").getValue().toString();
                    String email = snapshot.child("email").getValue().toString();
                    String perfil = snapshot.child("imagen").getValue().toString();

                    tvnombre.setText(nombre);
                    tvperfil.setText(email);
                    Glide.with(Perfil.this)
                            .load(perfil)
                            .fitCenter()
                            .centerCrop()
                            .circleCrop()
                            .into(fotoperfil);


                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}