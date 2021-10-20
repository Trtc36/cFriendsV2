package com.cfriendsv2.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Vibrator;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.cfriendsv2.R;
import com.cfriendsv2.chatActivity;
import com.cfriendsv2.pojos.Users;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

public class chatlistAdapter extends RecyclerView.Adapter<chatlistAdapter.viewHolderAdapterChatList> {

    List<Users> userList;
    Context context;
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    SharedPreferences mPref;

    public chatlistAdapter(List<Users> userList, Context context) {
        this.userList = userList;
        this.context = context;
    }

    @NonNull
    @Override
    public viewHolderAdapterChatList onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_chatlist, parent, false);
        viewHolderAdapterChatList holder = new viewHolderAdapterChatList(v);
        return holder;
    }

    @SuppressLint("RestrictedApi")
    @Override
    public void onBindViewHolder(@NonNull viewHolderAdapterChatList holder, int position) {
        Users users = userList.get(position);
        final Vibrator vibrator = (Vibrator)context.getSystemService(context.VIBRATOR_SERVICE);

        holder.tvUsers.setText(users.getNombre());
        Glide.with(context).load(users.getImagen()).fitCenter().centerCrop().circleCrop().into(holder.imgUsers);

        DatabaseReference mis_amigos = FirebaseDatabase.getInstance().getReference("Solicitudes").child(user.getUid());
        mis_amigos.child(users.getId()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String estado = snapshot.child("estado").getValue(String.class);
                if(snapshot.exists()){
                    if(estado.equals("Amigos")){
                        holder.cardView.setVisibility(View.VISIBLE);
                    }else{
                        holder.cardView.setVisibility(View.GONE);
                    }
                }else{
                    holder.cardView.setVisibility(View.GONE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        Calendar c = Calendar.getInstance();
        SimpleDateFormat df = new SimpleDateFormat("dd/mm/yyyy");

        DatabaseReference ref_estado = FirebaseDatabase.getInstance().getReference("Estado").child(users.getId());
        ref_estado.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String estado = snapshot.child("estado").getValue(String.class);
                String fecha = snapshot.child("fecha").getValue(String.class);
                String hora = snapshot.child("hora").getValue(String.class);

                if(snapshot.exists()){
                    if(estado.equals("Conectado")){
                        holder.tvOnline.setVisibility(View.VISIBLE);
                        holder.iconOnline.setVisibility(View.VISIBLE);
                        holder.tvOffline.setVisibility(View.GONE);
                        holder.iconOffline.setVisibility(View.GONE);
                    }else{
                        holder.tvOnline.setVisibility(View.GONE);
                        holder.iconOnline.setVisibility(View.GONE);
                        holder.tvOffline.setVisibility(View.VISIBLE);
                        holder.iconOffline.setVisibility(View.VISIBLE);

                        if(fecha.equals(df.format(c.getTime()))){
                            holder.tvOffline.setText("Ult. Vez hoy " + hora);
                        }else {
                            holder.tvOffline.setText("Ult. Vez " + fecha + " a las " + hora);
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        holder.cardView.setOnClickListener(new View.OnClickListener() {
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
            }
        });

    }//Fin del onViewBingHolder

    @Override
    public int getItemCount() {
        return userList.size();
    }

    public class viewHolderAdapterChatList extends RecyclerView.ViewHolder {
        TextView tvUsers, tvOnline, tvOffline;
        ImageView imgUsers, iconOnline, iconOffline;
        CardView cardView;


        public viewHolderAdapterChatList(@NonNull View itemView) {
            super(itemView);
            tvUsers = itemView.findViewById(R.id.tv_user);
            imgUsers = itemView.findViewById(R.id.image_profile);
            cardView = itemView.findViewById(R.id.cardview);
            tvOnline = itemView.findViewById(R.id.tvOnline);
            tvOffline = itemView.findViewById(R.id.tvOffline);
            iconOnline = itemView.findViewById(R.id.iconOnline);
            iconOffline = itemView.findViewById(R.id.iconOffline);
        }
    }

}
