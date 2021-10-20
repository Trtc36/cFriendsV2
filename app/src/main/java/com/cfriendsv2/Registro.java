package com.cfriendsv2;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.cfriendsv2.pojos.Users;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Objects;

public class Registro extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private DatabaseReference mDataBase;
    private StorageReference mStorage;
    private EditText nombre;
    private EditText usuario;
    private EditText password;
    private EditText passwordRepetir;
    private ImageView foto;
    private ImageButton subir, seleccionar;
    private Bitmap thumb_bitmap = null;
    public String imagenperfil;


    private String nombre_usuario= "";
    private String email = "";
    private String password_usuario = "";
    private String password_usuario2 = "";
    private String estado;
    public String fecha;
    public String hora;
    private int solicitudes;
    private int nuevos_mensajes;
    private Date date;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro);

        mDataBase = FirebaseDatabase.getInstance().getReference();
        mAuth = FirebaseAuth.getInstance();
        date = Calendar.getInstance().getTime();
        SimpleDateFormat  df = new SimpleDateFormat("dd-MM-yyyy");
        SimpleDateFormat hf = new SimpleDateFormat("hh:mm:SS");
        fecha = df.format(date);
        hora = hf.format(date);


        nombre = findViewById(R.id.edtNombre);
        usuario = findViewById(R.id.edtUsuario);
        password = findViewById(R.id.edtPassword);
        passwordRepetir = findViewById(R.id.edtPasswordRepetir);
        foto = (ImageView)findViewById(R.id.ivUsuario);
        seleccionar = findViewById(R.id.btnSeleccionar);
        subir = findViewById(R.id.btnSubir);
        mStorage = FirebaseStorage.getInstance().getReference().child("Img_Perfil");

        seleccionar.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                CropImage.startPickImageActivity(Registro.this);

            }
        });


    }//Fin del onCreate

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == CropImage.PICK_IMAGE_CHOOSER_REQUEST_CODE && resultCode == Activity.RESULT_OK){
            Uri imageuri = CropImage.getPickImageResultUri(this, data);

            //Recortar Imagen
            CropImage.activity(imageuri)
            .setGuidelines(CropImageView.Guidelines.ON)
            .setRequestedSize(640,640)
            .setAspectRatio(2,2)
            .start(Registro.this);
        }

        if(requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE){
            CropImage.ActivityResult result = CropImage.getActivityResult(data);

            if(resultCode == RESULT_OK){
                Uri resulturi = result.getUri();
                File url = new File(resulturi.getPath());

                Glide.with(this).load(url).fitCenter().circleCrop().into(foto);
                //Picasso.with(this).load(url).into(foto);

                //Comprimiento Imágen
                /*try {
                    thumb_bitmap = new Compressor(this)
                            .setMaxWidth(640)
                            .setMaxHeight(480)
                            .setQuality(90)
                            .compressToBitmap(url);
                }catch (IOException e){
                    e.printStackTrace();
                }

                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                thumb_bitmap.compress(Bitmap.CompressFormat.JPEG, 90, byteArrayOutputStream);
                final byte [] thumb_byte = byteArrayOutputStream.toByteArray();
                //fin del compresor*/

                int p = (int) (Math.random()*25+1);int s = (int) (Math.random()*25+1);
                int t = (int) (Math.random()*25+1);int c = (int) (Math.random()*25+1);
                int num1 = (int) (Math.random()*1012+2111);
                int num2 = (int) (Math.random()*1012+2111);
                String [] elementos = {"a", "b", "c", "d", "e", "f", "g", "h", "i", "j", "k", "l", "m", "n",
                "p", "q", "r", "s", "t", "u", "v", "w", "x", "y", "z"};

                final String aleatorio = elementos[p] + elementos[s] + num1 + elementos[t] + elementos[c] + num2 + "cpmprimido.jpg";

                subir.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        final StorageReference ref = mStorage.child(aleatorio);
                        UploadTask uploadTask = ref.putFile(resulturi);

                        //subir imagen en storage
                        Task<Uri> uriTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                            @Override
                            public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                                if(!task.isSuccessful()){
                                    throw Objects.requireNonNull(task.getException());
                                }
                                return ref.getDownloadUrl();
                            }
                        }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                            @Override
                            public void onComplete(@NonNull Task<Uri> task) {
                                Uri downloadUri = task.getResult();
                                imagenperfil = downloadUri.toString();
                                Toast.makeText(Registro.this, "Imágen cargada exitosamente", Toast.LENGTH_LONG).show();
                            }
                        });
                    }
                });
            }
        }
    }

    public  void onStart(){
        super.onStart();

        FirebaseUser currentUser = mAuth.getCurrentUser();
        //updateUI(currentUser);
    }


    public void Cancelar(View view){
        Intent i = new Intent(this, MainActivity.class);
        startActivity(i);
        finish();
    }

    public void crearCuenta(View view){
        nombre_usuario = nombre.getText().toString();
        email = usuario.getText().toString();
        password_usuario = password.getText().toString();
        password_usuario2 = passwordRepetir.getText().toString();
        estado = "Desconectado";
        solicitudes = 0;
        nuevos_mensajes = 0;


        if(!nombre_usuario.isEmpty() && !email.isEmpty() && !password_usuario.isEmpty() && !password_usuario2.isEmpty()){
            if(password_usuario.equals(password_usuario2)){
                if(password_usuario.length() >= 6){
                    mAuth.createUserWithEmailAndPassword(usuario.getText().toString(), password.getText().toString())
                            .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {
                                        Toast.makeText(getApplicationContext(), "Usuario creado.", Toast.LENGTH_SHORT).show();
                                        /*Map<String, Object> map = new HashMap<>();
                                        map.put("nombre", nombre_usuario);
                                        map.put("email", email);
                                        map.put("password", password_usuario);
                                        map.put("imagen", imagenperfil);
                                        map.put("estado", estado);
                                        map.put("fecha", fecha);
                                        map.put("hora", hora);
                                        map.put("solicitudes", solicitudes);
                                        map.put("mensajes", nuevos_mensajes);*/

                                        String id = mAuth.getCurrentUser().getUid();
                                        mDataBase = FirebaseDatabase.getInstance().getReference().child("user").child(id);

                                        mDataBase.addListenerForSingleValueEvent(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                if(!snapshot.exists()){
                                                    Users uu = new Users(
                                                            id,
                                                            nombre_usuario,
                                                            password_usuario,
                                                            email,
                                                            imagenperfil,
                                                            estado,
                                                            fecha,
                                                            hora,
                                                            solicitudes,
                                                            nuevos_mensajes
                                                    );
                                                    mDataBase.setValue(uu);
                                                    Toast.makeText(getApplicationContext(), "Datos agregados.", Toast.LENGTH_SHORT).show();
                                                    startActivity(new Intent(Registro.this, home.class));
                                                    finish();
                                                }
                                            }

                                            @Override
                                            public void onCancelled(@NonNull DatabaseError error) {

                                            }
                                        });




                                       /* mDataBase.child("user").child(id).setValue(map).addOnCompleteListener(new OnCompleteListener<Void>() {

                                            @Override
                                            public void onComplete(@NonNull Task<Void> task2) {
                                                if(task2.isSuccessful()){
                                                    Toast.makeText(getApplicationContext(), "Datos agregados.", Toast.LENGTH_SHORT).show();
                                                    startActivity(new Intent(Registro.this, home.class));
                                                    finish();
                                                }else{
                                                    Toast.makeText(getApplicationContext(), "Datos no agregados.", Toast.LENGTH_SHORT).show();
                                                }
                                            }
                                        });*/
                                        // Sign in success, update UI with the signed-in user's information

                                        //updateUI(user);
                                    } else {
                                        // If sign in fails, display a message to the user.
                                        Toast.makeText(getApplicationContext(), "Usuario no creado.", Toast.LENGTH_SHORT).show();
                                        //updateUI(null);
                                    }
                                }
                            });
                }else{
                    Toast.makeText(this, "La contraseña debe tener al menos 6 caracteres", Toast.LENGTH_LONG).show();
                }
            }else{
                Toast.makeText(this, "Las contraseñas deben ser iguales", Toast.LENGTH_LONG).show();
            }
        }else{
            Toast.makeText(this, "Debe rellenar todos los campos", Toast.LENGTH_LONG).show();
        }
    }


    private void updateUI(FirebaseUser user){

    }
}