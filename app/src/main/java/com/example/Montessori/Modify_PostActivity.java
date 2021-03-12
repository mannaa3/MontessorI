package com.example.Montessori;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

public class Modify_PostActivity extends AppCompatActivity {
  private ImageView PostImage;
  private TextView PostDescription;
  private Button DeletePostButton,EditPostButton;
  private  String postKey,currentUserID,dataBaseUserID,description,image;
  private DatabaseReference ClickPostRef;
  private FirebaseAuth mAuth;
     @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_click_post);
        mAuth=FirebaseAuth.getInstance();
        currentUserID=mAuth.getCurrentUser().getUid();

        postKey=getIntent().getExtras().get("postKey").toString();
        ClickPostRef= FirebaseDatabase.getInstance().getReference().child("posts").child(postKey);
        PostImage=(ImageView)findViewById(R.id.click_post_image);
        PostDescription=(TextView)findViewById(R.id.click_post_description);
        DeletePostButton=(Button)findViewById(R.id.delete_post_boutton);
        EditPostButton=(Button)findViewById(R.id.edit_post_button);
         DeletePostButton.setVisibility(View.INVISIBLE);
         EditPostButton.setVisibility(View.INVISIBLE);

        postKey =getIntent().getExtras().get("postKey").toString() ;
        ClickPostRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {

                    description = dataSnapshot.child("description").getValue().toString();
                    image = dataSnapshot.child("postimage").getValue().toString();
                    dataBaseUserID = dataSnapshot.child("uid").getValue().toString();

                    PostDescription.setText(description);
                    Picasso.with(Modify_PostActivity.this).load(image).into(PostImage);
                    if (currentUserID.equals(dataBaseUserID)) {
                        DeletePostButton.setVisibility(View.VISIBLE);
                        EditPostButton.setVisibility(View.VISIBLE);
                    }
                    EditPostButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            EditCurrentPost(description);
                        }
                    });
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        DeletePostButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DeleteCurrentPost();
            }
        });
    }

    private void EditCurrentPost( String description) {
        AlertDialog.Builder builder=new AlertDialog.Builder(Modify_PostActivity.this);
        builder.setTitle("Edit Post");
        final EditText inputField=new EditText (Modify_PostActivity.this);
        inputField.setText(description);
        builder.setView(inputField);
        builder.setPositiveButton("Update", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                ClickPostRef.child("description").setValue(inputField.getText().toString());
                Toast.makeText(Modify_PostActivity.this,"Post  update sucssfully...",Toast.LENGTH_LONG).show();

            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        Dialog dialog=builder.create();
        dialog.show();
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.holo_green_dark);
    }

    private void DeleteCurrentPost() {
         ClickPostRef.removeValue();
         SendUserToMainActivity();
         Toast.makeText(this,"post has been Delete",Toast.LENGTH_LONG).show();


    }
    private void SendUserToMainActivity() {

        Intent mainIntent=new Intent(Modify_PostActivity.this,MainActivity.class);
        mainIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(mainIntent);
        finish();

    }

}
