package com.cfriendsv2.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.cfriendsv2.R;
import com.cfriendsv2.adapters.usersAdapter;
import com.cfriendsv2.pojos.Users;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class usersFragment extends Fragment {
    private FirebaseAuth mAuth;
    private DatabaseReference mDataBase;

    public usersFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        mDataBase = FirebaseDatabase.getInstance().getReference();
        mAuth = FirebaseAuth.getInstance();

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_users, container, false);

        TextView tvUser = view.findViewById(R.id.tv_user);
        ImageView imgUser = view.findViewById(R.id.image_profile);
        ProgressBar progressBar = view.findViewById(R.id.progresbar);

        String id = mAuth.getCurrentUser().getUid().toString();
        mDataBase.child("user").child(id).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    String nombre = snapshot.child("nombre").getValue().toString();
                    String perfil = snapshot.child("imagen").getValue().toString();

                    tvUser.setText(nombre);
                    Glide.with(usersFragment.this)
                            .load(perfil)
                            .fitCenter()
                            .centerCrop()
                            .circleCrop()
                            .into(imgUser);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        RecyclerView rv;
        ArrayList<Users> usersArrayList;
        usersAdapter adapter;
        LinearLayoutManager mLayoutManager;

        mLayoutManager = new LinearLayoutManager(getContext());
        mLayoutManager.setReverseLayout(true);
        mLayoutManager.setStackFromEnd(true);
        rv = view.findViewById(R.id.rv);
        rv.setLayoutManager(mLayoutManager);

        usersArrayList = new ArrayList<>();
        adapter = new usersAdapter(usersArrayList, getContext());
        rv.setAdapter(adapter);

        DatabaseReference my_ref = FirebaseDatabase.getInstance().getReference();
        my_ref.child("user").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    rv.setVisibility(View.VISIBLE);
                    progressBar.setVisibility(View.GONE);

                    usersArrayList.removeAll(usersArrayList);
                    for(DataSnapshot snapshot1: snapshot.getChildren()){
                       Users uu = snapshot1.getValue(Users.class);
                       usersArrayList.add(uu);
                    }
                    adapter.notifyDataSetChanged();
                } else {
                    progressBar.setVisibility(View.GONE);
                    Toast.makeText(getContext(), "No Existen Contactos", Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        return view;
    }
}