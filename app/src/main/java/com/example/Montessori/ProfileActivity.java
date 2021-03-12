package com.example.Montessori;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import de.hdodenhof.circleimageview.CircleImageView;

public class ProfileActivity extends AppCompatActivity {
    private TextView userName,userprofName,userStatus,userCountry,userGender,userRelation,userDOB;
    private CircleImageView userProfileImage;
    private DatabaseReference profileUserRef;
    private FirebaseAuth mAuth;
    private String currentUserId,page_flag="0";
    private Toolbar mtoolbar;
    private ProgressDialog loaddingBar;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        mtoolbar=(Toolbar) findViewById(R.id.activity_profile_toolbar);
        mtoolbar.setTitle("الصفحة الشخصية");
        setSupportActionBar(mtoolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        loaddingBar=new ProgressDialog(this);
        if (savedInstanceState == null) {
            Bundle extras = getIntent().getExtras();
            if(extras == null) {
               //  page_flag= null;
            } else {
              //   page_flag= extras.getString("page_flag");
            }
        } else {
             //page_flag= (String) savedInstanceState.getSerializable("page_flag");
        }

        mAuth=FirebaseAuth.getInstance();
        currentUserId=mAuth.getCurrentUser().getUid();
        profileUserRef= FirebaseDatabase.getInstance().getReference().child("Users").child(currentUserId);

        userName=(TextView) findViewById(R.id.my_username );
        userprofName=(TextView)findViewById(R.id.my_profile_full_name );
        userStatus=(TextView)findViewById(R.id.my_profile_status );
        userCountry=(TextView)findViewById(R.id.my_country );
        userGender=(TextView)findViewById(R.id. my_gender);
        userRelation=(TextView)findViewById(R.id. my_relationship_status);
        userDOB=(TextView)findViewById(R.id.my_dob );
        userProfileImage=(CircleImageView)findViewById(R.id.my_profile_pic);
        profileUserRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot datasnapshot) {

                if(datasnapshot.exists()) {
                    String myprofileImage = datasnapshot.child("profileimage").getValue().toString();
                    String myUserName = datasnapshot.child("username").getValue().toString();
                    String myprofileName = datasnapshot.child("fullname").getValue().toString();
                    String myprofileStatus = datasnapshot.child("staus").getValue().toString();
                    String myDOB = datasnapshot.child("dop").getValue().toString();
                    String address = datasnapshot.child("address").getValue().toString();
                    String mobile = datasnapshot.child("mobile").getValue().toString();
                    String myGender = datasnapshot.child("gender").getValue().toString();
                    String myRelationStatuse = datasnapshot.child("relationshipstatus").getValue().toString();
                   // Picasso.with(ProfileActivity.this).load(myprofileImage).placeholder(R.drawable.profile).into(userProfileImage);
                    Glide.with(ProfileActivity.this).load(myprofileImage).into(userProfileImage);

                    userName.setText("@"+myUserName);
                    userprofName.setText(myprofileName);
                    userStatus.setText(myprofileStatus);
                    userDOB.setText("DOB :"+myDOB);
                    userCountry.setText("address: "+address);
                    userGender.setText("Gender: "+myGender);
                    userRelation.setText("Relation :"+myRelationStatuse);
                }
                }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    @Override
    public void onBackPressed() {
        SendUserToMainActivty();
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id=item.getItemId();
        if (id==android.R.id.home){
            SendUserToMainActivty();
        }
        return super.onOptionsItemSelected(item);
    }

    private void SendUserToMainActivty() {
        Intent MainIntent=new Intent(this,MainActivity.class);
      //  MainIntent.putExtra("page_flag",page_flag);
        MainIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(MainIntent);
        finish();
    }
}
