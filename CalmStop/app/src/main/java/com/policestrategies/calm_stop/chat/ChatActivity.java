package com.policestrategies.calm_stop.chat;

import android.content.Intent;
import android.database.DataSetObserver;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AbsListView;
import android.widget.EditText;
import android.widget.ListView;

import com.google.firebase.auth.FirebaseAuth;
import com.policestrategies.calm_stop.R;
import com.policestrategies.calm_stop.citizen.LoginActivity;

import static java.lang.System.currentTimeMillis;

// TODO: Clean up remaining code

public class ChatActivity extends AppCompatActivity implements View.OnClickListener {

    private ChatArrayAdapter mChatArrayAdapter;
    private ListView mListView;
    private EditText mChatText;

    private String mUid;

    private ChatManager mChatManager;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        mChatManager = new ChatManager(this);
        mListView = (ListView) findViewById(R.id.listView1);

        mChatArrayAdapter = new ChatArrayAdapter(getApplicationContext(),
                R.layout.activity_chat_singlemessage);

        mListView.setAdapter(mChatArrayAdapter);

        // Default Message object values to be passed to Message constructor
        mChatText = (EditText) findViewById(R.id.chat_text);

        // Obtaining the user's data from firebase
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();

        if (firebaseAuth.getCurrentUser() != null) {
            mUid = firebaseAuth.getCurrentUser().getUid();
        } else {
            firebaseAuth.signOut();
            Intent i = new Intent(this, LoginActivity.class);
            i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(i);
        }

        findViewById(R.id.button_send).setOnClickListener(this);

        mListView.setTranscriptMode(AbsListView.TRANSCRIPT_MODE_ALWAYS_SCROLL);
        mListView.setAdapter(mChatArrayAdapter);

        //to scroll the list view to bottom on data change
        mChatArrayAdapter.registerDataSetObserver(new DataSetObserver() {
            @Override
            public void onChanged() {
                super.onChanged();
                mListView.setSelection(mChatArrayAdapter.getCount() - 1);
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()) {
            case R.id.button_send:
                String timestamp = Long.toString(currentTimeMillis());
                String content = mChatText.getText().toString();
                Message newMessage = new Message(content, timestamp, mUid);
                mChatManager.sendToFirebase(newMessage);
                mChatText.setText("");
        }
    }

    void displayChatMessage(Message message){
        mChatArrayAdapter.add(message);
    }

}