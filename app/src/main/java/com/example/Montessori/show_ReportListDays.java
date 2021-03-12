package com.example.Montessori;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

public class show_ReportListDays extends AppCompatActivity {
    private RecyclerView ReportListRec;
    Query query;
    private Toolbar mtoolbar;
    private ProgressDialog loaddingBar;
    private FirebaseAuth mAuth;
    private  String currentUserID,page_flag="0";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_report_list);

        mtoolbar=(Toolbar) findViewById(R.id.report_list_layout);
        mtoolbar.setTitle("إظهار التقارير");
        setSupportActionBar(mtoolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        loaddingBar=new ProgressDialog(this);
        ReportListRec = (RecyclerView)findViewById(R.id.report_result_list);

        if (savedInstanceState == null) {
            Bundle extras = getIntent().getExtras();
            if(extras == null) {
             //    page_flag= null;
            } else {
              //   page_flag= extras.getString("page_flag");

            }
        } else {
            // page_flag= (String) savedInstanceState.getSerializable("page_flag");
        }
        mAuth=FirebaseAuth.getInstance();
        currentUserID=mAuth.getCurrentUser().getUid();

//        ReportListRec.setHasFixedSize(true);
//        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(this);
//        linearLayoutManager.setReverseLayout(true);
//        linearLayoutManager.setStackFromEnd(true);
//        ReportListRec.setLayoutManager(linearLayoutManager);

        ReportListRec.setLayoutManager(new LinearLayoutManager(this));
        query = FirebaseDatabase.getInstance().getReference().child("Report").child(currentUserID);
         showReportListmodel();



    }

    private void showReportListmodel() {

        FirebaseRecyclerOptions<showReport_Model> options =
                new FirebaseRecyclerOptions.Builder<showReport_Model>()
                        .setQuery(query, showReport_Model.class)
                        .build();


        FirebaseRecyclerAdapter<showReport_Model, show_ReportListDays.showReportViewHolder> firebaseRecyclerAdapter=
                new FirebaseRecyclerAdapter<showReport_Model, show_ReportListDays.showReportViewHolder>(options) {

                    @Override
                    protected void onBindViewHolder(@NonNull show_ReportListDays.showReportViewHolder viewHolder, final int position, @NonNull final showReport_Model model) {


                         viewHolder.setDate_child_name(model.date_child_name);

                            viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                long i = getItemId(position);
                                 SendUserToReceiveReport(model.getDate_child_name(), page_flag);
                            }
                        });


                     }


                    @NonNull
                    @Override
                    public show_ReportListDays.showReportViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                        View view = LayoutInflater.from(parent.getContext())
                                .inflate(R.layout.reoprt_date_card, parent, false);

                        return new showReportViewHolder(view);
                    }

                };
        firebaseRecyclerAdapter.getSnapshots().isEmpty();

        firebaseRecyclerAdapter.startListening();
        ReportListRec.setAdapter(firebaseRecyclerAdapter);
 





    }

    public class showReportViewHolder extends RecyclerView.ViewHolder{

        View mView;
        public showReportViewHolder(@NonNull View itemView) {
            super(itemView);
            mView = itemView;
//            mView.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//
//                }
//            });

        }
        public void setDate_child_name(String date_child_name){
            TextView post_time =(TextView) mView.findViewById(R.id.text_date_show_list);
            post_time.setText(date_child_name);


        }


    }




    @Override
    public void onBackPressed() {
        SendUserToMainActivty(page_flag);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id=item.getItemId();
        if (id==android.R.id.home){
            SendUserToMainActivty(page_flag);
        }
        return super.onOptionsItemSelected(item);
    }

    private void SendUserToMainActivty(String page_flag) {
        Intent MainIntent=new Intent(this,MainActivity.class);
     //   MainIntent.putExtra("page_flag",page_flag);
        MainIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(MainIntent);
        finish();
    }
    private void SendUserToReceiveReport(String  Date_child_name,String page_flag) {
        Intent Receive_Report=new Intent(this, show_Report.class);
        Receive_Report.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
        Receive_Report.putExtra("Date_child_name",Date_child_name);
     //   Receive_Report.putExtra("page_flag",page_flag);
        startActivity(Receive_Report);
        finish();
    }


}