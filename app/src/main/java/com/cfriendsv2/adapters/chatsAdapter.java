package com.cfriendsv2.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.cfriendsv2.R;
import com.cfriendsv2.pojos.Chats;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

public class chatsAdapter extends RecyclerView.Adapter<chatsAdapter.viewHolderAdapter> {

    List<Chats> chatsList;
    Context context;
    public final static int msjRight = 1;
    public final static int msjLeft = 0;
    Boolean soloRight = false;
    FirebaseUser fuser;

    public chatsAdapter(List<Chats> chatsList, Context context) {
        this.chatsList = chatsList;
        this.context = context;
    }

    @NonNull
    @Override
    public viewHolderAdapter onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if(viewType == msjRight){
            View view = LayoutInflater.from(context).inflate(R.layout.chat_item_right, parent,false);
            return new chatsAdapter.viewHolderAdapter(view);
        }else{
            View view = LayoutInflater.from(context).inflate(R.layout.chat_item_left, parent,false);
            return new chatsAdapter.viewHolderAdapter(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull viewHolderAdapter holder, int position) {
        Chats chats = chatsList.get(position);

        holder.tvMensaje.setText(chats.getMensaje());

        if(soloRight){
            if(chats.getVisto().equals("Si")){
                holder.imgDelivered.setVisibility(View.GONE);
                holder.imgSaw.setVisibility(View.VISIBLE);
            }else{
                holder.imgDelivered.setVisibility(View.VISIBLE);
                holder.imgSaw.setVisibility(View.GONE);
            }

            final Calendar c = Calendar.getInstance();
            final SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");

            if(chats.getFecha().equals(dateFormat.format(c.getTime()))){
                holder.tvFecha.setText("Hoy "+chats.getHora());
            }else{
                holder.tvFecha.setText(chats.getFecha() + " "+ chats.getHora());
            }
        }//Fin del soloRight
    }

    @Override
    public int getItemCount() {
        return chatsList.size();
    }

    public class viewHolderAdapter extends RecyclerView.ViewHolder {
        TextView tvMensaje, tvFecha;
        ImageView imgDelivered, imgSaw;

        public viewHolderAdapter(@NonNull View itemView) {
            super(itemView);
            tvMensaje = itemView.findViewById(R.id.tvMensaje);
            tvFecha = itemView.findViewById(R.id.tvFecha);
            imgDelivered = itemView.findViewById(R.id.imgDeliverd);
            imgSaw = itemView.findViewById(R.id.imgSaw);
        }
    }

    @Override
    public int getItemViewType(int position) {
        fuser = FirebaseAuth.getInstance().getCurrentUser();
        if(chatsList.get(position).getEnvia().equals(fuser.getUid())){
            soloRight = true;
            return  msjRight;
        }else {
            soloRight = false;
            return msjLeft;
        }
    }
}
