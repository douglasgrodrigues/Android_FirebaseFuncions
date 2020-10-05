package com.example.firebaseapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.firebase.ui.storage.images.FirebaseImageLoader;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.util.UUID;

public class MainActivity extends AppCompatActivity {

    //private DatabaseReference referencia = FirebaseDatabase.getInstance().getReference();
    //private FirebaseAuth usuario = FirebaseAuth.getInstance();

    /* =================================== UPLOAD DA IMAGEM =================================== */
    private Button buttonUpload;
    private ImageView imageFoto;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        /* =================================== UPLOAD DA IMAGEM =================================== */
        buttonUpload = findViewById(R.id.buttonUpload);
        imageFoto = findViewById(R.id.imageFoto);


        buttonUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //Salvar imagem em memoria
                imageFoto.setDrawingCacheEnabled(true);
                imageFoto.buildDrawingCache();

                //Recuperar o bitmap da imagem a ser carregada
                Bitmap bitmap = imageFoto.getDrawingCache();

                //comprimir o bitmap para um formato jpg ou png
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 75, baos);

                //Converter o baos para pixel brutos em uma matriz de bites
                //dados da imagem
                byte[] dadosImagem = baos.toByteArray();

                //Definir os nos para o storage
                StorageReference storageReference = FirebaseStorage.getInstance().getReference();
                StorageReference imagens = storageReference.child("imagens");   //Criando a pasta
                final StorageReference imagemRef = imagens.child("celular.jpeg");

                imagemRef.getDownloadUrl().addOnSuccessListener(MainActivity.this, new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        Glide.with(MainActivity.this)
                                .load(uri)
                                .into(imageFoto);
                    }
                }).addOnFailureListener(MainActivity.this, new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(
                                MainActivity.this,
                                "Erro ao fazer download: " + e.getMessage(),
                                Toast.LENGTH_LONG
                        ).show();
                    }
                });

/*
                //Downlaod do arquivo no celular
                Glide.with(MainActivity.this)
                        .load(imagemRef)
                        .into(imageFoto);*/



                //Deletar uma um arquivo
                /*
                imagemRef.delete().addOnFailureListener(MainActivity.this, new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(MainActivity.this, "Erro ao deletar",
                                Toast.LENGTH_SHORT).show();
                    }
                }).addOnSuccessListener(MainActivity.this, new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(MainActivity.this, "Sucesso ao deletar",
                                Toast.LENGTH_SHORT).show();

                    }
                });*/


                //Nome da imagem
                //String nomeArquivo = UUID.randomUUID().toString();   //Função que cria um nome para arquivo randomico com base no DD/MM/YYYY: HH:MM
                //StorageReference imagemRef = imagens.child("celular.jpeg");    //criando o arquivo jpeg

                //Retorna o objeto que ira controlar o upload
                /*UploadTask uploadTask = imagemRef.putBytes(dadosImagem);

                uploadTask.addOnFailureListener(MainActivity.this, new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(MainActivity.this, "Upload da imagem falhou: " +
                                        e.getMessage().toString(),
                                Toast.LENGTH_SHORT).show();
                    }
                }).addOnSuccessListener(MainActivity.this, new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        //TaskSnapshot.
                        imagemRef.getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
                            @Override
                            public void onComplete(@NonNull Task<Uri> task) {
                                Uri url = task.getResult();
                                Toast.makeText(MainActivity.this, "Sucesso ao fazer upload " + url.toString(),
                                        Toast.LENGTH_SHORT).show();
                            }
                        });

                    }
                });*/




            }
        });


        /*=================================== FIM UPLOAD IMAGEM =================================== */

        //DatabaseReference usuarios = referencia.child("usuarios");

        //TIPOS DE PESQUISAS E ORDENAÇÃO COM FIREBASE

        //DatabaseReference usuarioPesquisa = usuarios.child("-MIkEuMxB_2Q4l8QJdXw");
        //Query usuarioPesquisa = usuarios.orderByChild("nome").equalTo("Jamilton");
        //Query usuarioPesquisa = usuarios.orderByKey().limitToFirst(3);
        //Query usuarioPesquisa = usuarios.orderByKey().limitToLast(2);

        //Marior ou igual >=
        //Query usuarioPesquisa = usuarios.orderByChild("idade").startAt(35);

        //Menor igual <=
        //Query usuarioPesquisa = usuarios.orderByChild("idade").endAt(22);

        //Entre dois valores
        //Query usuarioPesquisa = usuarios.orderByChild("idade").startAt(18).endAt(25);

        //Filtrar palavras
        /*Query usuarioPesquisa = usuarios.orderByChild("nome")
                .startAt("Ja").endAt("Ja" + "\uf8ff" );         // "\uf8ff" -> utilizado para buscar por letras

        usuarioPesquisa.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                //Usuario dadosUsuario = dataSnapshot.getValue(Usuario.class);
                //
                //Log.i("Dados usuario","nome: " + dadosUsuario.getNome());

                Log.i("Dados usuario", dataSnapshot.getValue().toString());

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });



        //Crias usuários de forma dinamica
        usuarios.push().setValue(usuario);   // Cria uma identificador unico para cada usuário com o PUSH


        //Deslogar usuario
        //usuario.signOut();

        //logar usuario
        usuario.signInWithEmailAndPassword( "d.roodrigues-rs@hotmail.com", "mortadela" )
        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()){
                    Log.i("signIn", "Sucesso ao logar usuario!");
                }else{
                    Log.i("signIn", "Erro ao logar usuario!");
                }
            }
        });
//te

        //Verifica usuario logado
        if ( usuario.getCurrentUser() != null){
            Log.i("currentUser", " Usuario logado!");
        }else{
            Log.i("currentUser", "Usuario não logado!");
        }


        //Cadastrar usuário
        usuario.createUserWithEmailAndPassword( "d.roodrigues-rs@hotmail.com", "mortadela" )
                .addOnCompleteListener(MainActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()){
                            Log.i("CreateUser", "Sucesso ao cadastrar usuario!");
                        }else{
                            Log.i("CreateUser", "Erro ao cadastrar usuario!");
                        }
                    }
                });
*/


        //Inserir usuário no firebase

//        DatabaseReference usuarios = referencia.child("usuarios");
//        DatabaseReference produtos = referencia.child("produtos");
//
//        usuarios.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                Log.i("FIREBASE", dataSnapshot.getValue().toString());
//
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError databaseError) {
//
//            }
//        });
//
        //referencia.child( "pontos" ).setValue( "200" );
//        DatabaseReference usuarios = referencia.child( "usuarios" );
//
//        Usuario usuario = new Usuario();
//        usuario.setNome("Maria");
//        usuario.setSobrenome("Silva");
//        usuario.setIdade(45);
//        usuarios.child("002").setValue( usuario );
//
//        DatabaseReference produtos = referencia.child("produtos");
//
//        Produto produto = new Produto();
//        produto.setDescricao("Celular");
//        produto.setMarca("Apple");
//        produto.setPreco(3.400);
//        produtos.child("001").setValue(produto);

    }
}