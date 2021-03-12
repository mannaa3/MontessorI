package com.example.Montessori;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;

public class Make_UserRecharge extends AppCompatActivity {
    private Toolbar mtoolbar;
    private ProgressDialog loaddingBar;
    ImageButton mon1,mon2,mon3,mon4,mon5,mon6,mon7,mon8,mon9,mon10,mon11,mon12;
     Button save_button;
     TextView montemp,userName;
     CheckBox mon_CheckBox,mon_bus_CheckBox;
    private CircleImageView userProfileImage;
    private FirebaseAuth mAuth;
    private DatabaseReference UserRef,paymentRef,profileUserRef;
    private  String selcetUSERID="",montempString="",bustempString="",page_flag="";
    boolean flagToSave=false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_make_user_recharge);
        mtoolbar=(Toolbar) findViewById(R.id.activity_make_user_recharge_toolbar);
        mtoolbar.setTitle("إعدادات الإشتراك الشهري");
        setSupportActionBar(mtoolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        loaddingBar=new ProgressDialog(this);
        if (savedInstanceState == null) {
            Bundle extras = getIntent().getExtras();
            if(extras == null) {
                selcetUSERID= null;
            } else {
                selcetUSERID= extras.getString("uid");
              //  page_flag= extras.getString("page_flag");
            }
        } else {
            selcetUSERID= (String) savedInstanceState.getSerializable("uid");
           // page_flag= (String) savedInstanceState.getSerializable("pag_flage");
        }
        mAuth=FirebaseAuth.getInstance();
        UserRef= FirebaseDatabase.getInstance().getReference().child("Users");
        paymentRef=FirebaseDatabase.getInstance().getReference().child("payment").child(selcetUSERID);
       UserRef= FirebaseDatabase.getInstance().getReference().child("Users").child(selcetUSERID);
        userProfileImage=(CircleImageView)findViewById(R.id. my_profile_pic_makerecharge);
        userName=(TextView) findViewById(R.id.my_profile_full_name_makerecharge );
        save_button=(Button) findViewById(R.id.save_button );


        UserRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot datasnapshot) {

                if(datasnapshot.exists()) {
                    String myprofileImage = datasnapshot.child("profileimage").getValue().toString();
                    String myUserName = datasnapshot.child("username").getValue().toString();
                    String myprofileName = datasnapshot.child("fullname").getValue().toString();
                    String myprofileStatus = datasnapshot.child("staus").getValue().toString();
                    String myDOB = datasnapshot.child("dop").getValue().toString();
                     String myGender = datasnapshot.child("gender").getValue().toString();
                    String myRelationStatuse = datasnapshot.child("relationshipstatus").getValue().toString();
                    // Picasso.with(ProfileActivity.this).load(myprofileImage).placeholder(R.drawable.profile).into(userProfileImage);
                    Glide.with(Make_UserRecharge.this).load(myprofileImage).into(userProfileImage);

                    userName.setText("@"+myUserName);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        mon1=(ImageButton)findViewById(R.id.mon1);
        mon2=(ImageButton)findViewById(R.id.mon2);
        mon3=(ImageButton)findViewById(R.id.mon3);
        mon4=(ImageButton)findViewById(R.id.mon4);
        mon5=(ImageButton)findViewById(R.id.mon5);
        mon6=(ImageButton)findViewById(R.id.mon6);
        mon7=(ImageButton)findViewById(R.id.mon7);
        mon8=(ImageButton)findViewById(R.id.mon8);
        mon9=(ImageButton)findViewById(R.id.mon9);
        mon10=(ImageButton)findViewById(R.id.mon10);
        mon11=(ImageButton)findViewById(R.id.mon11);
        mon12=(ImageButton)findViewById(R.id.mon12);
        montemp=(TextView)findViewById(R.id.mon_temp);
        mon_CheckBox=(CheckBox)findViewById(R.id.mon_CheckBox);
        mon_bus_CheckBox=(CheckBox)findViewById(R.id.mon_bus_CheckBox);

        mon1.setOnClickListener(new  View.OnClickListener(){
            @Override
            public void onClick(View v) {
montemp.setText("1");
                montempString="mon"+montemp.getText().toString();
                bustempString="bus"+montemp.getText().toString();
                flagToSave=true;
            }
        } );
        mon2.setOnClickListener(new  View.OnClickListener(){
            @Override
            public void onClick(View v) {
montemp.setText("2");
                montempString="mon"+montemp.getText().toString();
                bustempString="bus"+montemp.getText().toString();
                flagToSave=true;

            }
        } );
        mon3.setOnClickListener(new  View.OnClickListener(){
            @Override
            public void onClick(View v) {
                montemp.setText("3");
                montempString="mon"+montemp.getText().toString();
                bustempString="bus"+montemp.getText().toString();
                flagToSave=true;

            }
        } );
        mon4.setOnClickListener(new  View.OnClickListener(){
            @Override
            public void onClick(View v) {
                montemp.setText("4");
                montempString="mon"+montemp.getText().toString();
                bustempString="bus"+montemp.getText().toString();
                flagToSave=true;

            }
        } );
        mon5.setOnClickListener(new  View.OnClickListener(){
            @Override
            public void onClick(View v) {
                montemp.setText("5");
montempString="mon"+montemp.getText().toString();
                bustempString="bus"+montemp.getText().toString();
                flagToSave=true;

            }
        } );

        mon6.setOnClickListener(new  View.OnClickListener(){
            @Override
            public void onClick(View v) {
                montemp.setText("6");
montempString="mon"+montemp.getText().toString();
                bustempString="bus"+montemp.getText().toString();
                flagToSave=true;

            }
        } );
        mon7.setOnClickListener(new  View.OnClickListener(){
            @Override
            public void onClick(View v) {
                montemp.setText("7");
                montempString="mon"+montemp.getText().toString();
                bustempString="bus"+montemp.getText().toString();
                flagToSave=true;

            }
        } );
        mon8.setOnClickListener(new  View.OnClickListener(){
            @Override
            public void onClick(View v) {
                montemp.setText("8");
                montempString="mon"+montemp.getText().toString();
                bustempString="bus"+montemp.getText().toString();
                flagToSave=true;

            }
        } );
        mon9.setOnClickListener(new  View.OnClickListener(){
            @Override
            public void onClick(View v) {
                montemp.setText("9");
                montempString="mon"+montemp.getText().toString();
                bustempString="bus"+montemp.getText().toString();
                flagToSave=true;

            }
        } );
        mon10.setOnClickListener(new  View.OnClickListener(){
            @Override
            public void onClick(View v) {
                montemp.setText("10");
                montempString="mon"+montemp.getText().toString();
                bustempString="bus"+montemp.getText().toString();
                flagToSave=true;

            }
        } );
        mon11.setOnClickListener(new  View.OnClickListener(){
            @Override
            public void onClick(View v) {
                montemp.setText("11");
                montempString="mon"+montemp.getText().toString();
                bustempString="bus"+montemp.getText().toString();
                flagToSave=true;

            }
        } );
        mon12.setOnClickListener(new  View.OnClickListener(){
            @Override
            public void onClick(View vb) {
montemp.setText("12");
                montempString="mon"+montemp.getText().toString();
                bustempString="bus"+montemp.getText().toString();
                flagToSave=true;

            }
        } );

        save_button.setOnClickListener(new  View.OnClickListener(){
            @Override
            public void onClick(View v) {
                if (flagToSave==false){
                    Toast.makeText(Make_UserRecharge.this,"يرجي اختيار الشهر",Toast.LENGTH_LONG).show();

                }else {
                    saveRecharge();


                }
             }
        } );



    }

    private void saveRecharge() {
        loaddingBar.setTitle("يرجي الانتظار ");
        loaddingBar.setMessage("جاري حفظ التغيرات");
        loaddingBar.setCanceledOnTouchOutside(true);
        loaddingBar.show();

        String temp_mon_CheckBox=""+mon_CheckBox.isChecked();
        String temp_bus_CheckBox=""+mon_bus_CheckBox.isChecked();
        HashMap payment=new HashMap();
        payment.put(montempString,""+temp_mon_CheckBox);
        payment.put(bustempString,""+temp_bus_CheckBox);
         paymentRef.updateChildren(payment).addOnCompleteListener(new OnCompleteListener() {
            @Override
            public void onComplete(@NonNull Task task) {

                if(task.isSuccessful()){
                    Toast.makeText(Make_UserRecharge.this,"Account Setting Update Succesfully..",Toast.LENGTH_LONG).show();
                    loaddingBar.dismiss();
                }
                else {
                    Toast.makeText(Make_UserRecharge.this,"Error occured.."+task.getException().getMessage(),Toast.LENGTH_LONG).show();
                    loaddingBar.dismiss();

                }
            }
        });
    }
    @Override
    public void onBackPressed() {
        SendUserToMainActivity(page_flag);
     }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id=item.getItemId();
        if (id==android.R.id.home){
            SendUserToMainActivity(page_flag);
        }
        return super.onOptionsItemSelected(item);
    }
    private void SendUserToMainActivity(String page_flag) {
        Intent mainIntent=new Intent(Make_UserRecharge.this,MainActivity.class);
      //  mainIntent.putExtra("page_flag",page_flag);
        mainIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(mainIntent);
        finish();
    }

}