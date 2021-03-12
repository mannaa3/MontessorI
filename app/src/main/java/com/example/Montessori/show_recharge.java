package com.example.Montessori;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import de.hdodenhof.circleimageview.CircleImageView;

public class show_recharge extends AppCompatActivity {
    private Toolbar mtoolbar;
    private ProgressDialog loaddingBar;
    private DatabaseReference rechageRef,UserRef;
    private  String currentUserID;
    private CircleImageView userProfileImage;

    private FirebaseAuth mAuth;
    ImageButton mon1,mon2,mon3,mon4,mon5,mon6,mon7,mon8,mon9,mon10,mon11,mon12;
    TextView mon_CheckBox,bus_CheckBox;
    TextView montemp,userName,mon_status_textView,bus_status_textView;

    private  String mon1temp,mon2temp,mon3temp,mon4temp,mon5temp,mon6temp,mon7temp,mon8temp,page_flag="0",
            mon9temp,mon10temp,mon11temp,mon12temp,bus1temp,bus2temp,bus3temp,bus4temp,bus5temp,bus6temp,bus7temp,bus8temp,
            bus9temp,bus10temp,bus11temp,bus12temp;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_recharge);

        mtoolbar=(Toolbar) findViewById(R.id.activity_make_user_show_recharge_toolbar);
        mtoolbar.setTitle("الإشتراك الشهري");
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
         //   page_flag= (String) savedInstanceState.getSerializable("page_flag");
        }
        mAuth=FirebaseAuth.getInstance();
        currentUserID=""+mAuth.getCurrentUser().getUid();
        rechageRef = FirebaseDatabase.getInstance().getReference().child("payment").child(currentUserID);
        UserRef= FirebaseDatabase.getInstance().getReference().child("Users").child(currentUserID);
        userProfileImage=(CircleImageView)findViewById(R.id. my_profile_pic_show_recharge);
        userName=(TextView) findViewById(R.id.my_profile_full_name_show_recharge );
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
        mon_status_textView=(TextView)findViewById(R.id.mon_status_textView);
        bus_status_textView=(TextView)findViewById(R.id.bus_status_textView);
        mon_CheckBox=(TextView) findViewById(R.id.mon_CheckBox);
        bus_CheckBox=(TextView) findViewById(R.id.mon_bus_CheckBox);

        UserRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot datasnapshot) {

                if(datasnapshot.exists()) {
                    String myprofileImage = datasnapshot.child("profileimage").getValue().toString();
                    String myUserName = datasnapshot.child("username").getValue().toString();/*
                    String myprofileName = datasnapshot.child("fullname").getValue().toString();
                    String myprofileStatus = datasnapshot.child("staus").getValue().toString();
                    String myDOB = datasnapshot.child("dop").getValue().toString();
                    String mobile = datasnapshot.child("mobile").getValue().toString();
                    String address = datasnapshot.child("address").getValue().toString();
                    String myGender = datasnapshot.child("gender").getValue().toString();
                    String myRelationStatuse = datasnapshot.child("relationshipstatus").getValue().toString();*/
                    // Picasso.with(ProfileActivity.this).load(myprofileImage).placeholder(R.drawable.profile).into(userProfileImage);
                    Glide.with(show_recharge.this).load(myprofileImage).into(userProfileImage);

                    userName.setText(""+myUserName);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        rechageRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot datasnapshot) {
                if(datasnapshot.exists()){
                    mon1temp=datasnapshot.child("mon1").getValue().toString();
                    mon2temp=datasnapshot.child("mon2").getValue().toString();
                    mon3temp=datasnapshot.child("mon3").getValue().toString();
                    mon4temp=datasnapshot.child("mon4").getValue().toString();
                    mon5temp=datasnapshot.child("mon5").getValue().toString();
                    mon6temp=datasnapshot.child("mon6").getValue().toString();
                    mon7temp=datasnapshot.child("mon7").getValue().toString();
                    mon8temp=datasnapshot.child("mon8").getValue().toString();
                    mon9temp=datasnapshot.child("mon9").getValue().toString();
                    mon10temp=datasnapshot.child("mon10").getValue().toString();
                    mon11temp=datasnapshot.child("mon11").getValue().toString();
                    mon12temp=datasnapshot.child("mon12").getValue().toString();
                    bus1temp=datasnapshot.child("bus1").getValue().toString();
                    bus2temp=datasnapshot.child("bus2").getValue().toString();
                    bus3temp=datasnapshot.child("bus3").getValue().toString();
                    bus4temp=datasnapshot.child("bus4").getValue().toString();
                    bus5temp=datasnapshot.child("bus5").getValue().toString();
                    bus6temp=datasnapshot.child("bus6").getValue().toString();
                    bus7temp=datasnapshot.child("bus7").getValue().toString();
                    bus8temp=datasnapshot.child("bus8").getValue().toString();
                    bus9temp=datasnapshot.child("bus9").getValue().toString();
                    bus10temp=datasnapshot.child("bus10").getValue().toString();
                    bus11temp=datasnapshot.child("bus11").getValue().toString();
                    bus12temp=datasnapshot.child("bus12").getValue().toString();

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        mon1.setOnClickListener(new  View.OnClickListener(){
            @Override
            public void onClick(View v) {
                montemp.setText("1");
                Boolean m = Boolean.valueOf(mon1temp);
                Boolean b = Boolean.valueOf(bus1temp);
                if (mon1temp.equals("true")){
                    mon_status_textView.setText("تم السداد ");

                }else {
                    mon_status_textView.setText("لم يتم السداد");

                }
                if (bus1temp.equals("true")){
                    bus_status_textView.setText("تم السداد ");

                }else {
                    bus_status_textView.setText("لم يتم السداد");

                }

              }
        } );
        mon2.setOnClickListener(new  View.OnClickListener(){
            @Override
            public void onClick(View v) {
                montemp.setText("2");
                Boolean m = Boolean.valueOf(mon1temp);
                Boolean b = Boolean.valueOf(bus1temp);
                if (mon1temp.equals("true")){
                    mon_status_textView.setText("تم السداد ");

                }else {
                    mon_status_textView.setText("لم يتم السداد");

                }
                if (bus1temp.equals("true")){
                    bus_status_textView.setText("تم السداد ");

                }else {
                    bus_status_textView.setText("لم يتم السداد");

                }


            }
        } );
        mon3.setOnClickListener(new  View.OnClickListener(){
            @Override
            public void onClick(View v) {
                montemp.setText("3");
                Boolean m = Boolean.valueOf(mon1temp);
                Boolean b = Boolean.valueOf(bus1temp);
                if (mon3temp.equals("true")){
                    mon_status_textView.setText("تم السداد ");

                }else {
                    mon_status_textView.setText("لم يتم السداد");

                }
                if (bus3temp.equals("true")){
                    bus_status_textView.setText("تم السداد ");

                }else {
                    bus_status_textView.setText("لم يتم السداد");

                }

            }
        } );
        mon4.setOnClickListener(new  View.OnClickListener(){
            @Override
            public void onClick(View v) {
                montemp.setText("4");
                Boolean m = Boolean.valueOf(mon1temp);
                Boolean b = Boolean.valueOf(bus1temp);
                if (mon4temp.equals("true")){
                    mon_status_textView.setText("تم السداد ");

                }else {
                    mon_status_textView.setText("لم يتم السداد");

                }
                if (bus4temp.equals("true")){
                    bus_status_textView.setText("تم السداد ");

                }else {
                    bus_status_textView.setText("لم يتم السداد");

                }


            }
        } );
        mon5.setOnClickListener(new  View.OnClickListener(){
            @Override
            public void onClick(View v) {
                montemp.setText("5");
                Boolean m = Boolean.valueOf(mon1temp);
                Boolean b = Boolean.valueOf(bus1temp);
                if (mon5temp.equals("true")){
                    mon_status_textView.setText("تم السداد ");

                }else {
                    mon_status_textView.setText("لم يتم السداد");

                }
                if (bus5temp.equals("true")){
                    bus_status_textView.setText("تم السداد ");

                }else {
                    bus_status_textView.setText("لم يتم السداد");

                }



            }
        } );

        mon6.setOnClickListener(new  View.OnClickListener(){
            @Override
            public void onClick(View v) {
                montemp.setText("6");
                Boolean m = Boolean.valueOf(mon1temp);
                Boolean b = Boolean.valueOf(bus1temp);
                if (mon6temp.equals("true")){
                    mon_status_textView.setText("تم السداد ");

                }else {
                    mon_status_textView.setText("لم يتم السداد");

                }
                if (bus6temp.equals("true")){
                    bus_status_textView.setText("تم السداد ");

                }else {
                    bus_status_textView.setText("لم يتم السداد");

                }

            }
        } );
        mon7.setOnClickListener(new  View.OnClickListener(){
            @Override
            public void onClick(View v) {
                montemp.setText("7");
                Boolean m = Boolean.valueOf(mon1temp);
                Boolean b = Boolean.valueOf(bus1temp);
                if (mon7temp.equals("true")){
                    mon_status_textView.setText("تم السداد ");

                }else {
                    mon_status_textView.setText("لم يتم السداد");

                }
                if (bus7temp.equals("true")){
                    bus_status_textView.setText("تم السداد ");

                }else {
                    bus_status_textView.setText("لم يتم السداد");

                }


            }
        } );
        mon8.setOnClickListener(new  View.OnClickListener(){
            @Override
            public void onClick(View v) {
                montemp.setText("8");
                Boolean m = Boolean.valueOf(mon1temp);
                Boolean b = Boolean.valueOf(bus1temp);
                if (mon8temp.equals("true")){
                    mon_status_textView.setText("تم السداد ");

                }else {
                    mon_status_textView.setText("لم يتم السداد");

                }
                if (bus8temp.equals("true")){
                    bus_status_textView.setText("تم السداد ");

                }else {
                    bus_status_textView.setText("لم يتم السداد");

                }


            }
        } );
        mon9.setOnClickListener(new  View.OnClickListener(){
            @Override
            public void onClick(View v) {
                montemp.setText("9");
                Boolean m = Boolean.valueOf(mon1temp);
                Boolean b = Boolean.valueOf(bus1temp);
                if (mon9temp.equals("true")){
                    mon_status_textView.setText("تم السداد ");

                }else {
                    mon_status_textView.setText("لم يتم السداد");

                }
                if (bus9temp.equals("true")){
                    bus_status_textView.setText("تم السداد ");

                }else {
                    bus_status_textView.setText("لم يتم السداد");

                }

            }
        } );
        mon10.setOnClickListener(new  View.OnClickListener(){
            @Override
            public void onClick(View v) {
                montemp.setText("10");
                Boolean m = Boolean.valueOf(mon1temp);
                Boolean b = Boolean.valueOf(bus1temp);
                if (mon10temp.equals("true")){
                    mon_status_textView.setText("تم السداد ");

                }else {
                    mon_status_textView.setText("لم يتم السداد");

                }
                if (bus10temp.equals("true")){
                    bus_status_textView.setText("تم السداد ");

                }else {
                    bus_status_textView.setText("لم يتم السداد");

                }


            }
        } );
        mon11.setOnClickListener(new  View.OnClickListener(){
            @Override
            public void onClick(View v) {
                montemp.setText("11");
                Boolean m = Boolean.valueOf(mon1temp);
                Boolean b = Boolean.valueOf(bus1temp);
                if (mon11temp.equals("true")){
                    mon_status_textView.setText("تم السداد ");

                }else {
                    mon_status_textView.setText("لم يتم السداد");

                }
                if (bus11temp.equals("true")){
                    bus_status_textView.setText("تم السداد ");

                }else {
                    bus_status_textView.setText("لم يتم السداد");

                }


            }
        } );
        mon12.setOnClickListener(new  View.OnClickListener(){
            @Override
            public void onClick(View vb) {
                montemp.setText("12");
                Boolean m = Boolean.valueOf(mon1temp);
                Boolean b = Boolean.valueOf(bus1temp);
                if (mon12temp.equals("true")){
                    mon_status_textView.setText("تم السداد ");

                }else {
                    mon_status_textView.setText("لم يتم السداد");

                }
                if (bus12temp.equals("true")){
                    bus_status_textView.setText("تم السداد ");

                }else {
                    bus_status_textView.setText("لم يتم السداد");

                }


            }
        } );

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
        Intent mainIntent=new Intent(show_recharge.this,MainActivity.class);
    //    mainIntent.putExtra("page_flag",page_flag);
        mainIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(mainIntent);
        finish();
    }
}