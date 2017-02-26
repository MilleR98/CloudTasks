package com.example.miller.cloudtasks;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseListAdapter;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.w3c.dom.Text;


public class ListTasks extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private DatabaseReference myRef;

    FirebaseUser user = mAuth.getInstance().getCurrentUser();
    FirebaseListAdapter mAdapter;

    private EditText ET_new_task;
    private Button Btn_new_task;

    private static class TaskViewHolder extends RecyclerView.ViewHolder{

        TextView mTitleText;
        Button mDel;
        public TaskViewHolder(View itemView) {
            super(itemView);
            mTitleText = (TextView)itemView.findViewById(R.id.tv_title_task);
            mDel = (Button)itemView.findViewById(R.id.btn_del);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_tasks);

        //ListUserTasks = (ListView)findViewById(R.id.discr_for_tasks);

        myRef = FirebaseDatabase.getInstance().getReference();

        /*mAdapter = new FirebaseListAdapter<String>(this,String.class,android.R.layout.simple_list_item_1,myRef.child(user.getUid()).child("Tasks")) {
            @Override
            protected void populateView(View v, String s, int position) {
                TextView text = (TextView)v.findViewById(android.R.id.text1);
                text.setText(s);
            }
        };

        ListUserTasks.setAdapter(mAdapter);*/

        Btn_new_task =(Button)findViewById(R.id.btn_add);
        ET_new_task = (EditText)findViewById(R.id.et_new_tasks);

        Btn_new_task.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myRef.child(user.getUid()).child("Tasks").push().setValue(ET_new_task.getText().toString());
            }
        });

        RecyclerView recyclerView = (RecyclerView)findViewById(R.id.rv_list_tasks);

        FirebaseRecyclerAdapter<String,TaskViewHolder> adapter;
        recyclerView.setLayoutManager( new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);

        adapter = new FirebaseRecyclerAdapter<String,TaskViewHolder>(
                String.class,
                R.layout.task_layout,
                TaskViewHolder.class,
                myRef.child(user.getUid()).child("Tasks")
        ){

            @Override
            protected void populateViewHolder(TaskViewHolder viewHolder, String title,final int position) {
                viewHolder.mTitleText.setText(title);
                viewHolder.mDel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        DatabaseReference itemRef = getRef(position);
                        itemRef.removeValue();
                    }
                });
            }
        };

        recyclerView.setAdapter(adapter);
    }

}
