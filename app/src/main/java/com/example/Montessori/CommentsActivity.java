package com.example.Montessori;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

public class CommentsActivity extends AppCompatActivity {

    private RecyclerView CommentList;
    private ImageButton PostCommentButton;
    private EditText CommentInputText;
    private String Post_Key,Current_User_id;
    private DatabaseReference UsersRef,PostRef;
    private FirebaseAuth mAuth;
    Query query;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comments);
        try {
            Post_Key = getIntent().getExtras().get("postKey").toString();

        UsersRef = FirebaseDatabase.getInstance().getReference().child("Users");
        PostRef = FirebaseDatabase.getInstance().getReference().child("posts").child(Post_Key).child("Comments");
            query = FirebaseDatabase.getInstance().getReference().child("posts").child(Post_Key).child("Comments");

        mAuth = FirebaseAuth.getInstance();
        Current_User_id = mAuth.getCurrentUser().getUid();
        CommentList = (RecyclerView) findViewById(R.id.comments_list);

        CommentList.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setReverseLayout(true);
        linearLayoutManager.setStackFromEnd(true);
        CommentList.setLayoutManager(linearLayoutManager);

        CommentInputText = (EditText) findViewById(R.id.comment_input);
        PostCommentButton = (ImageButton) findViewById(R.id.post_comment_btn);

        PostCommentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                UsersRef.child(Current_User_id).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                        if (dataSnapshot.exists()) {
                            String userName = dataSnapshot.child("username").getValue().toString();
                            ValidateComment(userName);
                            CommentInputText.setText("");
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

            }
        });
    }catch (Exception e){
            Toast.makeText(CommentsActivity.this,"you      "+e,Toast.LENGTH_LONG).show();

        }
     }

    @Override
    protected void onStart() {
        super.onStart();

        FirebaseRecyclerOptions<Comments> options =
                new FirebaseRecyclerOptions.Builder<Comments>()
                        .setQuery(query, Comments.class)
                        .build();
        FirebaseRecyclerAdapter<Comments,CommentsViewHolder> FirebaseRecyclerAdapter
                =new FirebaseRecyclerAdapter<Comments, CommentsViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull CommentsViewHolder viewHolder, int position, @NonNull Comments model) {


                viewHolder.setUsername(model.getUsername());
                viewHolder.setComment(model.getComment());
                viewHolder.setData(model.getData());
                viewHolder.setTime(model.getTime());
            }

            @NonNull
            @Override
            public CommentsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.activity_comments, parent, false);
                return new CommentsViewHolder(view);

            }
        };

        FirebaseRecyclerAdapter.startListening();
        CommentList.setAdapter(FirebaseRecyclerAdapter);

    }

    public static class CommentsViewHolder extends RecyclerView.ViewHolder{
        View mView;

        public CommentsViewHolder(View itemView) {
            super(itemView);
             mView =itemView;
        }


        public void setUsername(String username) {
            TextView myUserName=(TextView)mView.findViewById(R.id.comment_username);
            myUserName.setText("@"+username+"   ");
        }
        public void setComment(String comment) {
            TextView myComment=(TextView)mView.findViewById(R.id.comment_text);
            myComment.setText(comment);
        }
        public void setData(String data) {
            TextView myData=(TextView)mView.findViewById(R.id.comment_date);
            myData.setText("  Date :"+data);
        }

        public void setTime(String time) {
            TextView myTime=(TextView)mView.findViewById(R.id.comment_time);
            myTime.setText("  Time : "+time);
        }
    }
    private void ValidateComment(String userName) {
        String commentText=CommentInputText.getText().toString();
        if (TextUtils.isEmpty(commentText)){
            Toast.makeText(this,"please write your comment",Toast.LENGTH_LONG).show();

        }else {

            Calendar calForDate=Calendar.getInstance();
            SimpleDateFormat currentDate=new SimpleDateFormat("dd-MMMM-yyyy");
            final  String saveCurentData=currentDate.format(calForDate.getTime());
            Calendar calForTime=Calendar.getInstance();
            SimpleDateFormat currentTime=new SimpleDateFormat("HH:mm");
            final  String saveCurentTime=currentTime.format(calForDate.getTime());
            final String RandomKey=Current_User_id+saveCurentData+saveCurentTime;
            HashMap commentMap=new HashMap();
            commentMap.put("uid",Current_User_id);
            commentMap.put("comment",commentText);
            commentMap.put("date",saveCurentData);
            commentMap.put("time",saveCurentTime);
            commentMap.put("username",userName);
            PostRef.child(RandomKey).updateChildren(commentMap)
                    .addOnCompleteListener(new OnCompleteListener() {
                        @Override
                        public void onComplete(@NonNull Task task) {
                            if(task.isSuccessful()){
                                Toast.makeText(CommentsActivity.this,"you have comment successfully",Toast.LENGTH_LONG).show();

                            }else {
                                Toast.makeText(CommentsActivity.this,"Error Occuerdtry again...",Toast.LENGTH_LONG).show();


                            }

                        }
                    });

        }
    }
}
