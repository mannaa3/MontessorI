package com.example.Montessori;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.RadioButton;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class show_Report extends AppCompatActivity {
    TextView yy,mm,dd,edi_academic,edi_note,edi_reminder;
    CheckBox check_manners,check_mental,check_science,check_education,check_animals,check_arts,check_story,check_maps,check_planting,check_puzzle,
            check_active,check_playful,check_sleepy,check_quite,check_rad_not_on_mood;
    RadioButton radio_meal_none,radio_meal_all,radio_meal_some;
    Button but_back;

    private Toolbar mtoolbar;
    private ProgressDialog loaddingBar;
    private DatabaseReference ResultReportRef;

    private FirebaseAuth mAuth;
    private  String currentUserID,Date_child_name,page_flag="1";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_receive__report);
        mtoolbar=(Toolbar) findViewById(R.id.show_receive_report_layout);
        mtoolbar.setTitle("إظهار التقارير");
        setSupportActionBar(mtoolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        loaddingBar=new ProgressDialog(this);
        if (savedInstanceState == null) {
            Bundle extras = getIntent().getExtras();
            if(extras == null) {
                Date_child_name= null;
              //  page_flag= null;
            } else {
                Date_child_name= extras.getString("Date_child_name");
               // page_flag= extras.getString("page_flag");
            }
        } else {
            Date_child_name= (String) savedInstanceState.getSerializable("Date_child_name");
           // page_flag= (String) savedInstanceState.getSerializable("page_flag");
        }

        mAuth=FirebaseAuth.getInstance();
        currentUserID=mAuth.getCurrentUser().getUid();
//        yy=(TextView)findViewById(R.id.yy);
//        mm=(TextView)findViewById(R.id.mm);
//        dd=(TextView)findViewById(R.id.dd);
        edi_academic=(TextView)findViewById(R.id.edi_academic);
        edi_note=(TextView)findViewById(R.id.edi_note);
        edi_reminder=(TextView)findViewById(R.id.edi_reminder);
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
      //  but_back=(Button) findViewById(R.id.but_back);
        mAuth=FirebaseAuth.getInstance();
        currentUserID=mAuth.getCurrentUser().getUid();
        ResultReportRef= FirebaseDatabase.getInstance().getReference().child("Report").child(currentUserID);
        ResultReportRef.child(Date_child_name).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot datasnapshot) {

                if(datasnapshot.exists()) {

                    // Picasso.with(ProfileActivity.this).load(myprofileImage).placeholder(R.drawable.profile).into(userProfileImage);

                    String date_child_name=datasnapshot.child("date_child_name").getValue().toString();
                    String uid=datasnapshot.child("uid").getValue().toString();
                    String academic= datasnapshot.child("academic").getValue().toString();
                    String note= datasnapshot.child("note").getValue().toString();
                    String reminder= datasnapshot.child("reminder").getValue().toString();
                    String manners= datasnapshot.child("manners").getValue().toString();
                    String mental= datasnapshot.child("mental").getValue().toString();
                    String science= datasnapshot.child("science").getValue().toString();
                    String education= datasnapshot.child("education").getValue().toString();
                    String animals= datasnapshot.child("animals").getValue().toString();
                    String arts= datasnapshot.child("arts").getValue().toString();
                    String story= datasnapshot.child("story").getValue().toString();
                    String maps= datasnapshot.child("maps").getValue().toString();
                    String planting= datasnapshot.child("planting").getValue().toString();
                    String puzzle= datasnapshot.child("puzzle").getValue().toString();
                    String quite= datasnapshot.child("quite").getValue().toString();
                    String rad_not_on_mood= datasnapshot.child("rad_not_on_mood").getValue().toString();
                    String playful= datasnapshot.child("playful").getValue().toString();
                    String sleepy= datasnapshot.child("sleepy").getValue().toString();
                    String active= datasnapshot.child("active").getValue().toString();
                    String meal_none= datasnapshot.child("meal_none").getValue().toString();
                    String meal_all= datasnapshot.child("meal_all").getValue().toString();
                    String meal_some= datasnapshot.child("meal_some").getValue().toString();

                    edi_academic.setText(""+academic);
                    edi_note.setText(""+note);
                    edi_reminder.setText(""+reminder);
                    check_mental.setChecked(Boolean.valueOf(mental));
                    check_science.setChecked(Boolean.valueOf(science));
                    check_education.setChecked(Boolean.valueOf(education));
                    check_animals.setChecked(Boolean.valueOf(animals));
                    check_arts.setChecked(Boolean.valueOf(arts));
                    check_story.setChecked(Boolean.valueOf(story));
                    check_maps.setChecked(Boolean.valueOf(maps));
                    check_planting.setChecked(Boolean.valueOf(planting));
                    check_puzzle.setChecked(Boolean.valueOf(puzzle));
                    check_rad_not_on_mood.setChecked(Boolean.valueOf(rad_not_on_mood));
                    check_quite.setChecked(Boolean.valueOf(quite));
                    check_playful.setChecked(Boolean.valueOf(playful));
                    check_sleepy.setChecked(Boolean.valueOf(sleepy));
                    check_active.setChecked(Boolean.valueOf(active));
                    radio_meal_none.setChecked(Boolean.valueOf(meal_none));
                    radio_meal_all.setChecked(Boolean.valueOf(meal_all));
                    radio_meal_some.setChecked(Boolean.valueOf(meal_some));



                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });



    }

    @Override
    public void onBackPressed() {
        SendUserToShowReportListActivty();
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id=item.getItemId();
        if (id==android.R.id.home){
            finish();
            SendUserToShowReportListActivty();
        }
        return super.onOptionsItemSelected(item);
    }


     private void SendUserToShowReportListActivty() {
        Intent showReport=new Intent(show_Report.this, show_ReportListDays.class);
       // showReport.putExtra("page_flag",page_flag);
        showReport.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(showReport);
        finish();
    }

}