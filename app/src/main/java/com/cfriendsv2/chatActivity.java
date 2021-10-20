package com.cfriendsv2;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.cfriendsv2.adapters.chatsAdapter;
import com.cfriendsv2.pojos.Chats;
import com.cfriendsv2.pojos.Estado;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class chatActivity extends AppCompatActivity {

    ImageView img_perfil, img_Online, img_Offline;
    TextView tvNombre;
    EditText edtMensaje;
    ImageButton btnEnviar;
    SharedPreferences mPref;
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    DatabaseReference ref_estado = FirebaseDatabase.getInstance().getReference("Estado").child(user.getUid());
    DatabaseReference ref_chats = FirebaseDatabase.getInstance().getReference("Chats");

    //ID CHAT GLOBAL
    String id_chat_global;
    Boolean amigoonline = false;

    //RecyclerView
    RecyclerView rv_chats;
    chatsAdapter adapter;
    ArrayList<Chats> chatsArrayList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mPref = getApplicationContext().getSharedPreferences("usuario_sp", MODE_PRIVATE);
        img_perfil = findViewById(R.id.image_profile);
        img_Online = findViewById(R.id.iconConectado);
        img_Offline = findViewById(R.id.iconDesconectado);
        tvNombre = findViewById(R.id.tv_user);
        edtMensaje = findViewById(R.id.edtMensaje);
        btnEnviar = findViewById(R.id.btnEnviar);


        String usuario = getIntent().getExtras().getString("nombre");
        String foto = getIntent().getExtras().getString("img_user");
        String id_user = getIntent().getExtras().getString("id_user");
        id_chat_global = getIntent().getExtras().getString("id_unico");

        colocarenvisto();

        btnEnviar.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                String msj = edtMensaje.getText().toString();
                if(!msj.isEmpty()){
                    final Calendar c = Calendar.getInstance();
                    final SimpleDateFormat timeFormat = new SimpleDateFormat("hh:mm");
                    final SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
                    String id_push = ref_chats.push().getKey();

                    if(amigoonline){
                        Chats chatmsj = new Chats(id_push, user.getUid(), id_user, msj, "Si", dateFormat.format(c.getTime()),timeFormat.format(c.getTime()));
                        ref_chats.child(id_chat_global).child(id_push).setValue(chatmsj);
                        Toast.makeText(chatActivity.this, "Menaje Enviado", Toast.LENGTH_SHORT).show();
                        edtMensaje.setText("");
                    }else{
                        Chats chatmsj = new Chats(id_push, user.getUid(), id_user, msj, "No", dateFormat.format(c.getTime()),timeFormat.format(c.getTime()));
                        ref_chats.child(id_chat_global).child(id_push).setValue(chatmsj);
                        Toast.makeText(chatActivity.this, "Menaje Enviado", Toast.LENGTH_SHORT).show();
                        edtMensaje.setText("");
                    }


                }else{
                    Toast.makeText(chatActivity.this, "El mensaje esta vacio", Toast.LENGTH_SHORT).show();
                }

            }
        });

        final String id_user_sp = mPref.getString("usuario_sp","");

        tvNombre.setText(usuario);
        Glide.with(chatActivity.this).load(foto).fitCenter().centerCrop().circleCrop().into(img_perfil);

        final DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Estado").child(id_user_sp).child("chatCon");
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String chatCon = snapshot.getValue(String.class);
                if(snapshot.exists()){
                    if(chatCon.equals(user.getUid())){
                        amigoonline = true;
                        img_Online.setVisibility(View.VISIBLE);
                        img_Offline.setVisibility(View.GONE);
                    }else{
                        amigoonline = false;
                        img_Online.setVisibility(View.GONE);
                        img_Offline.setVisibility(View.VISIBLE);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        //RV--
        rv_chats = findViewById(R.id.rv);
        rv_chats.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setStackFromEnd(true);
        rv_chats.setLayoutManager(linearLayoutManager);

        chatsArrayList = new ArrayList<>();
        adapter = new chatsAdapter(chatsArrayList, this);
        rv_chats.setAdapter(adapter);

        leermensajes();

    }//Fin del onCreate

    private void colocarenvisto() {
        ref_chats.child(id_chat_global).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot snapshot1 : snapshot.getChildren()){
                    Chats chats = snapshot1.getValue(Chats.class);
                    if(chats.getRecibe().equals(user.getUid())){
                        ref_chats.child(id_chat_global).child(chats.getId()).child("visto").setValue("Si");
                    }
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void leermensajes() {
        ref_chats.child(id_chat_global).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    chatsArrayList.removeAll(chatsArrayList);
                    for(DataSnapshot snapshot1 : snapshot.getChildren()){
                        Chats chats = snapshot1.getValue(Chats.class);
                        chatsArrayList.add(chats);
                        //setScroll();
                    }
                    adapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void setScroll() {
        rv_chats.scrollToPosition(adapter.getItemCount()-1);
    }

    private void estadouser(String estado) {
        ref_estado.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                final String id_user_sp = mPref.getString("usuario_sp","");
                Estado est = new Estado(estado,"","",id_user_sp);
                ref_estado.setValue(est);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        estadouser("Conectado");
    }

    @Override
    protected void onPause() {
        super.onPause();
        estadouser("Desconectado");
        ultimaConexion();
    }

    private void ultimaConexion() {
        Calendar c = Calendar.getInstance();
        SimpleDateFormat tf = new SimpleDateFormat("hh:mm");
        SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy");
        ref_estado.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                ref_estado.child("fecha").setValue(df.format(c.getTime()));
                ref_estado.child("hora").setValue(tf.format(c.getTime()));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}