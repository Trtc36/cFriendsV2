package com.cfriendsv2.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Vibrator;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.cfriendsv2.R;
import com.cfriendsv2.chatActivity;
import com.cfriendsv2.pojos.Solicitudes;
import com.cfriendsv2.pojos.Users;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

public class usersAdapter extends RecyclerView.Adapter<usersAdapter.viewHolderAdapter> {

    List<Users> userList;
    Context context;
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    SharedPreferences mPref;

    public usersAdapter(List<Users> userList, Context context) {
        this.userList = userList;
        this.context = context;
    }

    @NonNull
    @Override
    public viewHolderAdapter onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_users, parent, false);
        viewHolderAdapter holder = new viewHolderAdapter(v);
        return holder;
    }

    @SuppressLint("RestrictedApi")
    @Override
    public void onBindViewHolder(@NonNull viewHolderAdapter holder, int position) {
        Users users = userList.get(position);
        final Vibrator vibrator = (Vibrator)context.getSystemService(context.VIBRATOR_SERVICE);
        Glide.with(context).load(users.getImagen()).fitCenter().centerCrop().circleCrop().into(holder.imgUsers);
        holder.tvUsers.setText(users.getNombre());

        if(users.getId().equals(user.getUid())){
            holder.cardView.setVisibility(View.GONE);
        }else{
            holder.cardView.setVisibility(View.VISIBLE);
        }

        DatabaseReference ref_buttons = FirebaseDatabase.getInstance().getReference("Solicitudes").child(user.getUid()).child(users.getId());
        ref_buttons.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String estado = snapshot.child("estado").getValue(String.class);
                if(snapshot.exists()){
                    if(estado.equals("Enviado")){
                        holder.send.setVisibility(View.VISIBLE);
                        holder.add.setVisibility(View.GONE);
                        holder.amigos.setVisibility(View.GONE);
                        holder.solicitud.setVisibility(View.GONE);
                        holder.progressBar.setVisibility(View.GONE);
                    }

                    if(estado.equals("Amigos")){
                        holder.send.setVisibility(View.GONE);
                        holder.add.setVisibility(View.GONE);
                        holder.amigos.setVisibility(View.VISIBLE);
                        holder.solicitud.setVisibility(View.GONE);
                        holder.progressBar.setVisibility(View.GONE);
                    }

                    if(estado.equals("Solicitud")){
                        holder.send.setVisibility(View.GONE);
                        holder.add.setVisibility(View.GONE);
                        holder.amigos.setVisibility(View.GONE);
                        holder.solicitud.setVisibility(View.VISIBLE);
                        holder.progressBar.setVisibility(View.GONE);
                    }
                }else{
                    holder.send.setVisibility(View.GONE);
                    holder.add.setVisibility(View.VISIBLE);
                    holder.amigos.setVisibility(View.GONE);
                    holder.solicitud.setVisibility(View.GONE);
                    holder.progressBar.setVisibility(View.GONE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        holder.add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final DatabaseReference A = FirebaseDatabase.getInstance().getReference("Solicitudes").child(user.getUid());
                A.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        Solicitudes solA = new Solicitudes("Enviado","");
                        A.child(users.getId()).setValue(solA);
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                    }
                });

                final DatabaseReference B = FirebaseDatabase.getInstance().getReference("Solicitudes").child(users.getId());
                B.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        Solicitudes solB = new Solicitudes("Solicitud","");
                        B.child(user.getUid()).setValue(solB);
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                    }
                });

                final DatabaseReference count = FirebaseDatabase.getInstance().getReference("Contador").child(users.getId());
                count.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if(snapshot.exists()){
                            Integer val = snapshot.getValue(Integer.class);
                            if(val == 0){
                                count.setValue(1);
                            }else{
                                count.setValue(val+1);
                            }
                        }else{
                            count.setValue(1);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

                vibrator.vibrate(300);
            }//Fin del onClick
        });

        holder.solicitud.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String idChat = ref_buttons.push().getKey();
                final DatabaseReference A = FirebaseDatabase.getInstance().getReference("Solicitudes").child(users.getId()).child(user.getUid());
                A.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        Solicitudes solA = new Solicitudes("Amigos", idChat);
                        A.setValue(solA);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

                final DatabaseReference B = FirebaseDatabase.getInstance().getReference("Solicitudes").child(user.getUid()).child(users.getId());
                B.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        Solicitudes solB = new Solicitudes("Amigos",idChat);
                        B.setValue(solB);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

                vibrator.vibrate(300);
            }//Fin del Click
        });//Fin del SetonClickListener Solicitud

        holder.amigos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                mPref = view.getContext().getSharedPreferences("usuario_sp",Context.MODE_PRIVATE);
                final SharedPreferences.Editor editor = mPref.edit();

                final DatabaseReference ref = FirebaseDatabase.getInstance()
                        .getReference("Solicitudes").child(user.getUid()).child(users.getId()).child("idChat");
                ref.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        String id_unico = snapshot.getValue(String.class);
                        if(snapshot.exists()){
                            Intent intent = new Intent(view.getContext(), chatActivity.class);
                            intent.putExtra("nombre", users.getNombre());
                            intent.putExtra("img_user", users.getImagen());
                            intent.putExtra("id_user", users.getId());
                            intent.putExtra("id_unico", id_unico);
                            editor.putString("usuario_sp", users.getId());
                            editor.apply();
                            view.getContext().startActivity(intent);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }//Fin del onClick amigos
        });//Fin del setOnClickListener Amigos
    }//Fin del onViewBingHolder

    @Override
    public int getItemCount() {
        return userList.size();
    }

    public class viewHolderAdapter extends RecyclerView.ViewHolder {
        TextView tvUsers;
        ImageView imgUsers;
        CardView cardView;
        Button add, send, amigos, solicitud;
        ProgressBar progressBar;

        public viewHolderAdapter(@NonNull View itemView) {
            super(itemView);
            tvUsers = itemView.findViewById(R.id.tv_user);
            imgUsers = itemView.findViewById(R.id.image_profile);
            cardView = itemView.findViewById(R.id.cardview);
            add = itemView.findViewById(R.id.btnAdd);
            send = itemView.findViewById(R.id.btnSend);
            amigos = itemView.findViewById(R.id.btnAmigos);
            solicitud = itemView.findViewById(R.id.btnSolicitud);
            progressBar = itemView.findViewById(R.id.progresbar);
        }
    }

}
