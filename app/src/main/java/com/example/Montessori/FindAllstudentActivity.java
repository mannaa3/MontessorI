package com.example.Montessori;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import de.hdodenhof.circleimageview.CircleImageView;

public class FindAllstudentActivity extends AppCompatActivity {

    private Toolbar mToolBar;
    private TextView SearchButton;
    private EditText SearchInputText;
    private ProgressDialog loaddingBar;

    Query searchPeopleFrindsQuery;
    private DatabaseReference allUserDatabaseRef;
    private  String SelectUserId="non",page_flag="0",page_name="report";
    RecyclerView SearchResultList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_friends);
        if (savedInstanceState == null) {
            Bundle extras = getIntent().getExtras();
            if(extras == null) {
               // page_flag= null;
               // page_name=null;
            } else {
                //page_flag= extras.getString("page_flag");
               // page_name= extras.getString("page_name");

            }
        } else {
          //  page_flag= (String) savedInstanceState.getSerializable("page_flag");
          //  page_name= (String) savedInstanceState.getSerializable("page_name");

        }
        SharedPreferences pref = getApplicationContext().getSharedPreferences("MyPref", 0);
      //  currentUserID =pref.getString("uid_SharedPreferences",null);
        page_flag=pref.getString("page_flage_SharedPreferences",null);
       if (isNetworkAvailable(this)){
       // query = FirebaseDatabase.getInstance().getReference().child("Users");
        allUserDatabaseRef = FirebaseDatabase.getInstance().getReference().child("Users");

        mToolBar = (Toolbar) findViewById(R.id.find_friends_appbar_layout);
        mToolBar.setTitle("Find Freinds ");
        setSupportActionBar(mToolBar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
           loaddingBar=new ProgressDialog(this);

        SearchResultList = (RecyclerView) findViewById(R.id.search_result_list);
        SearchResultList.setHasFixedSize(true);
        SearchResultList.setLayoutManager(new LinearLayoutManager(this));

        SearchButton = (TextView) findViewById(R.id.search_people_friends_button);
        SearchInputText = (EditText) findViewById(R.id.search_box_input);

        SearchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isNetworkAvailable(FindAllstudentActivity.this)){
                String searchboxinput = SearchInputText.getText().toString();
                  searchPeopleFrindsQuery = allUserDatabaseRef.orderByChild("fullname")
                        .startAt(searchboxinput).endAt(searchboxinput + "\uf8ff");
                SearchPeopleFriends(searchboxinput);
                }else {


                        loaddingBar.setTitle("تنبية الاتصال");
                        loaddingBar.setMessage("يرجي الاتصال بالانترنت");
                        loaddingBar.show();
                        loaddingBar.setCanceledOnTouchOutside(true);

                    }

            }
        });
    }else {


           loaddingBar.setTitle("تنبية الاتصال");
           loaddingBar.setMessage("يرجي الاتصال بالانترنت");
           loaddingBar.show();
           loaddingBar.setCanceledOnTouchOutside(true);
       }

    }

    private void SearchPeopleFriends(String searchboxinput) {


         FirebaseRecyclerOptions<FindAllStudentModel> options =
                new FirebaseRecyclerOptions.Builder<FindAllStudentModel>()
                        .setQuery(searchPeopleFrindsQuery, FindAllStudentModel.class)
                        .build();


        FirebaseRecyclerAdapter<FindAllStudentModel, FindFriendsViewHloder> firebaseRecyclerAdapter
                = new FirebaseRecyclerAdapter<FindAllStudentModel, FindFriendsViewHloder>(options) {

            @Override
            protected void onBindViewHolder(final FindFriendsViewHloder viewHloder, final int position, @NonNull final FindAllStudentModel model) {

                viewHloder.setFullname(model.fullname);
                viewHloder.setStatus(model.staus);
                viewHloder.setprofileimage(model.profileimage);

                viewHloder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (isNetworkAvailable(FindAllstudentActivity.this)){

                        model.getUid();

                         if (page_flag.equals("2")) {
                             SendUserToSentReport(model.getUid());

                         }else if (page_flag.equals("3"))
                          {
                              if (page_name.equals("report")) {
                                       SendUserToSentReport(model.getUid());

                              }else {
                                       SendUserToMakeUserRechargeActivty(model.getUid(),page_flag);

                              }
                          }
                         else
                         {
                             Toast.makeText(FindAllstudentActivity.this,"page :"+page_flag,Toast.LENGTH_LONG ).show();
                         }
                    }else
                        {
                          Toast.makeText(FindAllstudentActivity.this,"لايوجد اتصال بالانترنت ",Toast.LENGTH_LONG ).show();
                        }

                }
                });

            }

            @NonNull
            @Override
            public FindFriendsViewHloder onCreateViewHolder(@NonNull ViewGroup parent,  int viewType) {
                View view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.all_user_card, parent, false);

                return new FindFriendsViewHloder(view);
            }
        };


       // SearchResultList.setOnClickListener();


         firebaseRecyclerAdapter.startListening();
        SearchResultList.setAdapter(firebaseRecyclerAdapter);

}
   public class FindFriendsViewHloder extends RecyclerView.ViewHolder    {

        View mView;

        public FindFriendsViewHloder(@NonNull View itemView) {
            super(itemView);
            mView = itemView;
            mView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                 }
            });
        }





         public void setprofileimage(String profileimage){
            CircleImageView myimage=(CircleImageView) mView.findViewById(R.id.all_users_profile_image);
            Glide.with(FindAllstudentActivity.this).load(profileimage).into(myimage);

        }
        public void setFullname(String fullname){
            TextView myName=(TextView)mView.findViewById(R.id.all_users_profile_full_name);
            myName.setText(fullname);

        }  public void setStatus(String status){
           TextView myStatus=(TextView)mView.findViewById(R.id.all_users_Status);
           myStatus.setText(status);

       }
       public void setUid(String uid){
           SelectUserId=uid;

       }


   }
    private void SendUserToMakeUserRechargeActivty(String uid,String page_flag) {
        Intent profile=new Intent(FindAllstudentActivity.this, Make_UserRecharge.class);
        profile.putExtra("uid",uid);
      //  profile.putExtra("page_flag",page_flag);
        startActivity(profile);
        finish();
    }
    private void SendUserToSentReport(String uid) {
        Intent profile=new Intent(FindAllstudentActivity.this, Make_Report.class);
        profile.putExtra("uid",uid);
 //       profile.putExtra("page_flag",page_flag);
        startActivity(profile);
        finish();
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
        Intent mainIntent=new Intent(FindAllstudentActivity.this,MainActivity.class);
//        mainIntent.putExtra("page_flag",page_flag);
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

       // return connectivityManager.getActiveNetworkInfo() != null && connectivityManager.getActiveNetworkInfo().isConnected();
    }


    }



