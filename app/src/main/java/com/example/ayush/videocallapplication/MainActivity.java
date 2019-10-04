package com.example.ayush.videocallapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.opentok.android.Session;
import com.opentok.android.Stream;
import com.opentok.android.Publisher;
import com.opentok.android.PublisherKit;
import com.opentok.android.Subscriber;

import android.view.View;
import android.widget.FrameLayout;
import com.opentok.android.OpentokError;

import androidx.annotation.NonNull;
import android.Manifest;
import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.EasyPermissions;
import android.opengl.GLSurfaceView;

import android.os.Bundle;

public class MainActivity extends AppCompatActivity implements  Session.SessionListener, PublisherKit.PublisherListener {
    private static String API_KEY;
    private static String SESSION_ID;
    private static String TOKEN;
    private static final String LOG_TAG = MainActivity.class.getSimpleName();
    private static final int RC_SETTINGS_SCREEN_PERM = 123;
    private static final int RC_VIDEO_APP_PERM = 124;
    private Session mSession;
    private FrameLayout mPublisherViewContainer;
    private FrameLayout mSubscriberViewContainer;
    private Publisher mPublisher;
    private Subscriber mSubscriber;
    private DatabaseReference mDatabase;
    private boolean update = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mDatabase = FirebaseDatabase.getInstance().getReference();
        mDatabase = FirebaseDatabase.getInstance().getReference().child("Proj_1").child("CONNECTION_1");
        mDatabase.setValue("False");
        mDatabase = FirebaseDatabase.getInstance().getReference().child("Proj_1").child("CONNECTION_2");
        mDatabase.setValue("False");
        mDatabase = FirebaseDatabase.getInstance().getReference().child("Proj_2").child("CONNECTION_1");
        mDatabase.setValue("False");
        mDatabase = FirebaseDatabase.getInstance().getReference().child("Proj_2").child("CONNECTION_2");
        mDatabase.setValue("False");
        requestPermissions();
    }

    @AfterPermissionGranted(RC_VIDEO_APP_PERM)
    private void requestPermissions() {
        String[] perms = { Manifest.permission.INTERNET, Manifest.permission.CAMERA, Manifest.permission.RECORD_AUDIO };
        if (EasyPermissions.hasPermissions(this, perms)) {
            // initialize view objects from your layout
            mPublisherViewContainer = (FrameLayout)findViewById(R.id.publisher_container);
            mSubscriberViewContainer = (FrameLayout)findViewById(R.id.subscriber_container);
            SESSION_ID = "1_MX40NjQzMTkzMn5-MTU3MDAyMjE0MDI1N35BMEFIZWtDeG1qRDhMRk5rQ3dEN1M1SUx-fg";
            TOKEN = "T1==cGFydG5lcl9pZD00NjQzMTkzMiZzaWc9MjU3YzdkM2M3ZGQwMmM3ZGYxYjFhYjVlZmI0N2ZiNzUxYjE3MGY0MjpzZXNzaW9uX2lkPTFfTVg0ME5qUXpNVGt6TW41LU1UVTNNREF5TWpFME1ESTFOMzVCTUVGSVpXdERlRzFxUkRoTVJrNXJRM2RFTjFNMVNVeC1mZyZjcmVhdGVfdGltZT0xNTcwMDIyMTQ4Jm5vbmNlPTAuMDk0MzExNzkzNjg2OTg2ODMmcm9sZT1wdWJsaXNoZXImZXhwaXJlX3RpbWU9MTU3MjYxNDE0NyZpbml0aWFsX2xheW91dF9jbGFzc19saXN0PQ==";
            API_KEY = "46431932";
            makeConnection();

        } else {
            EasyPermissions.requestPermissions(this, "This app needs access to your camera and mic to make video calls", RC_VIDEO_APP_PERM, perms);
        }
    }

    public void updateInitials(FirebaseCallback fbcb)
    {
        String proj = "Proj_";
        update = false;
        for(int i = 4; i > 0; i--)
        {
            String num = String.valueOf(i);
            mDatabase = FirebaseDatabase.getInstance().getReference().child(proj + num);
            mDatabase.addListenerForSingleValueEvent(new ValueEventListener()
            {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot)
                {
                    String ans1 =  dataSnapshot.child("CONNECTION_1").getValue().toString();
                    Log.i("ANSWER1", ans1);
                    if(!ans1.equals("True"))
                    {
                        if(update == true)
                        {
                            return;
                        }
                        update = true;
                        mDatabase = FirebaseDatabase.getInstance().getReference().child(proj + num);
                        mDatabase.addListenerForSingleValueEvent(new ValueEventListener()
                        {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot)
                            {
                                String api = dataSnapshot.child("API_KEY").getValue().toString();
                                String token = dataSnapshot.child("TOKEN").getValue().toString();
                                String session = dataSnapshot.child("SESSION_ID").getValue().toString();
                                String changeTo = num;
                                String CONN = "1";

                                fbcb.onCallback(api, token, session, changeTo, CONN);
                                return;
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError)
                            {

                            }
                        });
                        return;
                    }
                    String ans2 =  dataSnapshot.child("CONNECTION_2").getValue().toString();
                    Log.i("ANSWER2", ans1);
                    if(!ans2.equals("True"))
                    {
                        if(update == true)
                        {
                            return;
                        }
                        update = true;
                        mDatabase = FirebaseDatabase.getInstance().getReference().child(proj + num);
                        mDatabase.addListenerForSingleValueEvent(new ValueEventListener()
                        {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot)
                            {
                                String api = dataSnapshot.child("API_KEY").getValue().toString();
                                String token = dataSnapshot.child("TOKEN").getValue().toString();
                                String session = dataSnapshot.child("SESSION_ID").getValue().toString();
                                String changeTo = num;
                                String CONN = "2";

                                fbcb.onCallback(api, token, session, changeTo, CONN);
                                return;
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError)
                            {

                            }
                        });
                        return;
                    }
                }
                @Override
                public void onCancelled(@NonNull DatabaseError databaseError)
                {

                }
            });
        }
    }

    private interface FirebaseCallback
    {
        void onCallback(String API, String TOKEN, String SESSION, String CHANGE, String CONN);
    }


    public void connectTo1(View view)
    {
        updateInitials(new FirebaseCallback()
        {
            @Override
            public void onCallback(String API, String token, String SESSION, String change, String conn)
            {
               API_KEY = API;
               SESSION_ID = SESSION;
               TOKEN = token;
               mDatabase = FirebaseDatabase.getInstance().getReference().child("Proj_" + change).child("CONNECTION_" + conn);
               mDatabase.setValue("True");
               Log.i("CHECK_LOL", API_KEY);
               makeConnection();
            }
        });

    }

    public void makeConnection()
    {
        mSession = new Session.Builder(this, API_KEY, SESSION_ID).build();
        mSession.setSessionListener(this);
        mSession.connect(TOKEN);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }

    // SessionListener methods
    @Override
    public void onConnected(Session session) {
        Log.i(LOG_TAG, "Session Connected");

        mPublisher = new Publisher.Builder(this).build();
        mPublisher.setPublisherListener(this);
        mPublisherViewContainer.addView(mPublisher.getView());

        if (mPublisher.getView() instanceof GLSurfaceView){
            ((GLSurfaceView) mPublisher.getView()).setZOrderOnTop(true);
        }

        mSession.publish(mPublisher);
    }

    @Override
    public void onDisconnected(Session session) {
        Log.i(LOG_TAG, "Session Disconnected");
    }

    @Override
    public void onStreamReceived(Session session, Stream stream) {
        Log.i(LOG_TAG, "Stream Received");

        if (mSubscriber == null) {
            mSubscriber = new Subscriber.Builder(this, stream).build();
            mSession.subscribe(mSubscriber);
            mSubscriberViewContainer.addView(mSubscriber.getView());
        }
    }

    @Override
    public void onStreamDropped(Session session, Stream stream) {
        Log.i(LOG_TAG, "Stream Dropped");

        if (mSubscriber != null) {
            mSubscriber = null;
            mSubscriberViewContainer.removeAllViews();
        }
    }

    @Override
    public void onError(Session session, OpentokError opentokError) {
        Log.e(LOG_TAG, "Session error: " + opentokError.getMessage());
    }

    @Override
    public void onStreamCreated(PublisherKit publisherKit, Stream stream) {

    }

    @Override
    public void onStreamDestroyed(PublisherKit publisherKit, Stream stream) {

    }

    @Override
    public void onError(PublisherKit publisherKit, OpentokError opentokError) {

    }
}