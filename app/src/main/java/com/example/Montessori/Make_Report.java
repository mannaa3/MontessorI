package com.example.Montessori;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
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

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;

public class Make_Report extends AppCompatActivity {
    private Toolbar mtoolbar;
    private ProgressDialog loaddingBar;
    private CircleImageView selecteduser_profilepic;
    TextView userName;
    EditText yy,mm,dd,edi_academic,edi_note,edi_reminder;
    CheckBox check_manners,check_mental,check_science,check_education,check_animals,check_arts,check_story,check_maps,check_planting,check_puzzle,
             check_active,check_playful,check_sleepy,check_quite,check_rad_not_on_mood;
    RadioButton radio_meal_none,radio_meal_all,radio_meal_some;
    Button but_send_report;
    private  String currentUserID,selcetUSERID="",saveCurentData,saveCurentTime,page_flag;
    private DatabaseReference UserRef,selectRef,send_reportRef;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_report);
        mtoolbar=(Toolbar) findViewById(R.id.send_report_toolbar);
        mtoolbar.setTitle("إارسال  التقارير ");
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
             }
        } else {
            selcetUSERID= (String) savedInstanceState.getSerializable("uid");
        }
        Toast.makeText(this,"UID Report "+selcetUSERID,Toast.LENGTH_LONG).show();
        selecteduser_profilepic=(CircleImageView)findViewById(R.id. selectuser_profilepic);
        userName=(TextView) findViewById(R.id.selectuser_userName );
        yy=(EditText)findViewById(R.id.yy);
        mm=(EditText)findViewById(R.id.mm);
        dd=(EditText)findViewById(R.id.dd);
        edi_academic=(EditText)findViewById(R.id.edi_academic);
        edi_note=(EditText)findViewById(R.id.edi_note);
        edi_reminder=(EditText)findViewById(R.id.edi_reminder);
        check_manners=(CheckBox)findViewById(R.id.check_manners);
        check_mental=(CheckBox)findViewById(R.id.check_mental);
        check_science=(CheckBox)findViewById(R.id.check_science);
        check_education=(CheckBox)findViewById(R.id.check_education);
        check_education=(CheckBox)findViewById(R.id.check_education);
        check_animals=(CheckBox)findViewById(R.id.check_animals);
        check_arts=(CheckBox)findViewById(R.id.check_arts);
        check_story=(CheckBox)findViewById(R.id.check_story);
        check_maps=(CheckBox)findViewById(R.id.check_maps);
        check_planting=(CheckBox)findViewById(R.id.check_planting);
        check_puzzle=(CheckBox)findViewById(R.id.check_puzzle);
        check_quite=(CheckBox)findViewById(R.id.check_quite);
        check_rad_not_on_mood=(CheckBox)findViewById(R.id.check_rad_not_on_mood);
        check_playful=(CheckBox)findViewById(R.id.check_playful);
        check_sleepy=(CheckBox)findViewById(R.id.check_sleepy);
        check_active=(CheckBox)findViewById(R.id.check_active);
        radio_meal_none=(RadioButton)findViewById(R.id.radio_meal_none);
        radio_meal_all=(RadioButton)findViewById(R.id.radio_meal_all);
        radio_meal_some=(RadioButton)findViewById(R.id.radio_meal_some);
        but_send_report=(Button) findViewById(R.id.but_send_report);
        mAuth=FirebaseAuth.getInstance();
        currentUserID=mAuth.getCurrentUser().getUid();
//        UserRef= FirebaseDatabase.getInstance().getReference().child("Users");
        send_reportRef= FirebaseDatabase.getInstance().getReference().child("Report");
        selectRef= FirebaseDatabase.getInstance().getReference().child("Users");
//                        child(yy.getText().toString()+"-"+mm.getText().toString()+"-"+dd.getText().toString());

        but_send_report.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (isNetworkAvailable(Make_Report.this)){
                SendReportmodel();
            }else{
                loaddingBar.setTitle("تنبية الاتصال");
                loaddingBar.setMessage("يرجي الاتصال بالانترنت");
                loaddingBar.show();
                loaddingBar.setCanceledOnTouchOutside(true);


            }

            }
        });

        selectRef.child(selcetUSERID).addValueEventListener(new ValueEventListener() {
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
                    Glide.with(Make_Report.this).load(myprofileImage).into(selecteduser_profilepic);
                    userName.setText(""+myUserName);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }

    private void SendReportmodel() {

        Calendar calForDate=Calendar.getInstance();
        SimpleDateFormat currentDate=new SimpleDateFormat("dd-MMMM-yyyy");
        saveCurentData=currentDate.format(calForDate.getTime());
       ///////
        Calendar calForTime=Calendar.getInstance();
        SimpleDateFormat currentTime=new SimpleDateFormat("HH:mm");
        saveCurentTime=currentTime.format(calForTime.getTime());
        ///////
        String y=yy.getText().toString();
        String m=mm.getText().toString();
        String d=dd.getText().toString();

        String academic=""+edi_academic.getText().toString();
        String note=""+edi_note.getText().toString();
        String reminder=""+edi_reminder.getText().toString();
        String manners=""+check_manners.isChecked();
        String mental=""+check_mental.isChecked();
        String science=""+check_science.isChecked();
        String education=""+check_education.isChecked();
        String animals=""+check_animals.isChecked();
        String arts=""+check_arts.isChecked();
        String story=""+check_story.isChecked();
        String maps=""+check_maps.isChecked();
        String planting=""+check_planting.isChecked();
        String puzzle=""+check_puzzle.isChecked();
        String quite=""+check_quite.isChecked();
        String rad_not_on_mood=""+check_rad_not_on_mood.isChecked();
        String playful=""+check_playful.isChecked();
        String sleepy=""+check_sleepy.isChecked();
        String active=""+check_active.isChecked();
        String meal_none=""+radio_meal_none.isChecked();
        String meal_all=""+radio_meal_all.isChecked();
        String meal_some=""+radio_meal_some.isChecked();

        HashMap postMap=new HashMap();
        postMap.put("date_child_name",y+"-"+m+"-"+d);
        postMap.put("uid",selcetUSERID);
        postMap.put("report_made_by",currentUserID);
        postMap.put("report_date",saveCurentData);
        postMap.put("report_time",saveCurentTime);
        postMap.put("academic",academic);
        postMap.put("note",note);
        postMap.put("reminder",reminder);
        postMap.put("manners",manners);
        postMap.put("mental",mental);
        postMap.put("science",science);
        postMap.put("education",education);
        postMap.put("animals",animals);
        postMap.put("arts",arts);
        postMap.put("story",story);
        postMap.put("maps",maps);
        postMap.put("planting",planting);
        postMap.put("puzzle",puzzle);
        postMap.put("quite",quite);
        postMap.put("rad_not_on_mood",rad_not_on_mood);
        postMap.put("playful",playful);
        postMap.put("sleepy",sleepy);
        postMap.put("active",active);
        postMap.put("meal_none",meal_none);
        postMap.put("meal_all",meal_all);
        postMap.put("meal_some",meal_some);
        send_reportRef.child(selcetUSERID).child(y+"-"+m+"-"+d).updateChildren(postMap)
                .addOnCompleteListener(new OnCompleteListener() {
                    @Override
                    public void onComplete(@NonNull Task task) {
                        if(task.isSuccessful())
                        {
                            loaddingBar.dismiss();
                            Toast.makeText(Make_Report.this, "تم إرسال التقرير ", Toast.LENGTH_LONG).show();

                        }else {
                            loaddingBar.dismiss();
                            String message=task.getException().getMessage().toString();
                            Toast.makeText(Make_Report.this, "حدث خطأ يرجي المحاولة لاحقأ .."+message, Toast.LENGTH_LONG).show();

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
        Intent mainIntent=new Intent(Make_Report.this,MainActivity.class);
       // mainIntent.putExtra("page_flag",page_flag);
        mainIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(mainIntent);
        finish();
    }
    public boolean isNetworkAvailable(Context context) {
        ConnectivityManager connectivityManager = ((ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE));
        NetworkInfo wificonn=connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        NetworkInfo mobileconn=connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
        if ((wificonn!=null&&wificonn.isConnected())||(mobileconn!=null&&mobileconn.isConnected())){
            return  true;
        }else {
            return false;
        }



    }}