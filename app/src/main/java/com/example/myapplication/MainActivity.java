package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef = database.getReference("messages");
    private static int MAX_MESSAGE_LENGTH=150;
    EditText mEditTextMessage;
    Button msendButton;
    ArrayList<String>messages = new ArrayList<>();
    RecyclerView mMessagesRecycler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        msendButton=findViewById(R.id.send_message_button);
        mEditTextMessage=findViewById(R.id.message_input);
        mMessagesRecycler=findViewById(R.id.messages_recycler);
        mMessagesRecycler.setLayoutManager(new LinearLayoutManager(this));
        final DataAdapter dataAdapter= new DataAdapter(this,messages);
        mMessagesRecycler.setAdapter(dataAdapter);

        msendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String mas=mEditTextMessage.getText().toString();
                if (mas.equals(""))
                {
                    Toast.makeText(getApplicationContext(),"Enter the message",Toast.LENGTH_SHORT).show();
                    return;

                }
                if(mas.length()>MAX_MESSAGE_LENGTH)
                {
                    Toast.makeText(getApplicationContext(),"This message is very long",Toast.LENGTH_SHORT).show();
                    return;
                }
                myRef.push().setValue(mas);
                mEditTextMessage.setText("");

            }

        });

        myRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                String mes=dataSnapshot.getValue(String.class);
                messages.add(mes);
                dataAdapter.notifyDataSetChanged();
                mMessagesRecycler.smoothScrollToPosition(messages.size());
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }

        });
    }
}
