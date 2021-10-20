package com.cfriendsv2;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.viewpager2.widget.ViewPager2;

import com.cfriendsv2.adapters.pagesAdapter;
import com.cfriendsv2.pojos.Estado;
import com.cfriendsv2.pojos.Users;
import com.google.android.material.badge.BadgeDrawable;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class home extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private DatabaseReference mDataBase;
    private  DatabaseReference ref_sol_count, ref_state_user;
    private Users users;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        mDataBase = FirebaseDatabase.getInstance().getReference();
        mAuth = FirebaseAuth.getInstance();
        ref_sol_count = FirebaseDatabase.getInstance().getReference("Contador").child(mAuth.getCurrentUser().getUid());
        ref_state_user = FirebaseDatabase.getInstance().getReference("Estado").child(mAuth.getCurrentUser().getUid());

        ViewPager2 viewPager2 = findViewById(R.id.viewPager);
        viewPager2.setAdapter(new pagesAdapter(this));

        TabLayout tabLayout = findViewById(R.id.tabLayout);
        TabLayoutMediator tabLayoutMediator = new TabLayoutMediator(tabLayout, viewPager2, new TabLayoutMediator.TabConfigurationStrategy() {
            @Override
            public void onConfigureTab(@NonNull TabLayout.Tab tab, int position) {
                switch (position){
                    case 0:
                        tab.setText("Usuarios");
                        tab.setIcon(R.drawable.ic_users);
                        BadgeDrawable badgeDrawable0 = tab.getOrCreateBadge();
                        badgeDrawable0.setBackgroundColor(
                                ContextCompat.getColor(getApplicationContext(), R.color.white)
                        );
                        badgeDrawable0.setVisible(true);
                        badgeDrawable0.setNumber(16);
                        break;
                    case 1:
                        tab.setText("Chats");
                        tab.setIcon(R.drawable.ic_chats);
                        BadgeDrawable badgeDrawable1 = tab.getOrCreateBadge();
                        badgeDrawable1.setBackgroundColor(
                                ContextCompat.getColor(getApplicationContext(), R.color.white)
                        );
                        badgeDrawable1.setVisible(true);
                        badgeDrawable1.setNumber(12);
                        break;
                    case 2:
                        tab.setText("Solicitudes");
                        tab.setIcon(R.drawable.ic_requests);
                        BadgeDrawable badgeDrawable2 = tab.getOrCreateBadge();
                        badgeDrawable2.setBackgroundColor(
                                ContextCompat.getColor(getApplicationContext(), R.color.white)
                        );

                        ref_sol_count.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                if(snapshot.exists()){
                                    Integer val = snapshot.getValue(Integer.class);
                                    badgeDrawable2.setVisible(true);
                                    if(val.equals(0)){
                                        badgeDrawable2.setVisible(false);
                                    }else{
                                        badgeDrawable2.setNumber(val);
                                    }

                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });

                        break;
                    case 3:
                        tab.setText("Mis Solicitudes");
                        tab.setIcon(R.drawable.ic_mrequests);
                        BadgeDrawable badgeDrawable3 = tab.getOrCreateBadge();
                        badgeDrawable3.setBackgroundColor(
                                ContextCompat.getColor(getApplicationContext(), R.color.white)
                        );
                        badgeDrawable3.setVisible(true);
                        badgeDrawable3.setNumber(5);
                        break;
                }
            }
        });
        tabLayoutMediator.attach();

        viewPager2.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                BadgeDrawable badgeDrawable = tabLayout.getTabAt(position).getOrCreateBadge();
                badgeDrawable.setVisible(false);

                if(position == 2){
                    countacero();
                }
            }
        });
    }//Fin del onCreate

    private void estadouser(String estado) {
        ref_state_user.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Estado est = new Estado(estado,"","","");
                ref_state_user.setValue(est);
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
        SimpleDateFormat df = new SimpleDateFormat("dd/mm/yyyy");
        ref_state_user.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                ref_state_user.child("fecha").setValue(df.format(c.getTime()));
                ref_state_user.child("hora").setValue(tf.format(c.getTime()));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void countacero() {
        ref_sol_count.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    ref_sol_count.setValue(0);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_item, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.item_cerrar:
                Toast.makeText(this, "Cerrando Sesión...", Toast.LENGTH_LONG).show();
                mAuth.signOut();
                startActivity(new Intent(home.this, MainActivity.class));
                finish();
                break;
            case R.id.item_Perfil:
                Intent i = new Intent(home.this, Perfil.class);
                startActivity(i);
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}