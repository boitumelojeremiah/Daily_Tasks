package com.jeremiah.daily_tasks.ui.home;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.checkbox.MaterialCheckBox;
import com.google.firebase.messaging.FirebaseMessaging;
import com.jeremiah.daily_tasks.MySingleton;
import com.jeremiah.daily_tasks.R;
import com.jeremiah.daily_tasks.SendMail;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class HomeFragment extends Fragment {

    private HomeViewModel homeViewModel;

    MaterialCheckBox creditsCheck, smsSpace, smsBackups, agentSpace, agentBackups, fdsrvoneSpace, fdsrvoneMissed, fdsrvtwoSpace, fdsrvtwoBackup, capPDCcheck, capPDCbackup, dvsSpace, dvsBackups, abiSpace, abiBackups, manifestSpace, manifestBackups, caftanSpace, caftanBackup, amsSpace, amsBackup, pawnSpace, pawnBackup;
    Button updateSMSBtn, agentBtn, fdsrvoneBtn, fdsrvtwoBtn, capPDCBtn, dvsUpdateBtn, abiUpdateBtn, manifestBtnUpdate, caftanBtnUpdate, amsBtnUpdate, pawnBtnUpdate;
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy.MM.dd G 'at' HH:mm:ss z");
    String currDate = sdf.format(new Date());
    String sms;

    final private String FCM_API = "https://fcm.googleapis.com/fcm/send";
    final private String serverKey = "key=" + "AAAAdPiocKs:APA91bHSxbu7ojW87tseOKxMcR79gXdUPJ7ZoejyvHuMwQL1LprogNT6F76cPusOo753SR8CLGb-EXlCa2f0-pKxDrVJo1KRxA398bQmHr06cbmnjv8pOrV9GmJbQPGhcupf3eZipbjX";
    final private String contentType = "application/json";
    final String TAG = "NOTIFICATION TAG";
    private RequestQueue mRequestQue;
    private String URL = "https://fcm.googleapis.com/fcm/send";

    String NOTIFICATION_TITLE;
    String NOTIFICATION_MESSAGE;
    String TOPIC;
    String RANDOM_TEXT;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        homeViewModel =
                ViewModelProviders.of(this).get(HomeViewModel.class);
        View root = inflater.inflate(R.layout.fragment_home, container, false);
        //final TextView textView = root.findViewById(R.id.text_home);
       /* homeViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });*/
       creditsCheck = root.findViewById(R.id.creditsCheck);
       smsSpace = root.findViewById(R.id.smsSpace);
       smsBackups = root.findViewById(R.id.smsBackups);
       agentSpace = root.findViewById(R.id.agentSpace);
       agentBackups = root.findViewById(R.id.agentBackups);
       fdsrvoneSpace = root.findViewById(R.id.fdsrvoneSpace);
       fdsrvoneMissed = root.findViewById(R.id.fdsrvonemissed);
       fdsrvtwoSpace = root.findViewById(R.id.fdsrvtwoSpace);
       fdsrvtwoBackup = root.findViewById(R.id.fdsrvtwobackup);
       capPDCcheck = root.findViewById(R.id.capPDCcheck);
       capPDCbackup = root.findViewById(R.id.capPDCbackup);
       dvsSpace = root.findViewById(R.id.dvsSpace);
       dvsBackups = root.findViewById(R.id.dvsBackups);
       abiSpace = root.findViewById(R.id.abiSpace);
       abiBackups = root.findViewById(R.id.abiBackups);
       manifestSpace = root.findViewById(R.id.manifestSpace);
       manifestBackups = root.findViewById(R.id.manifestBackup);
       caftanSpace = root.findViewById(R.id.caftanSpace);
       caftanBackup = root.findViewById(R.id.caftanBackup);
       amsSpace = root.findViewById(R.id.amsSpace);
       amsBackup = root.findViewById(R.id.amsBackup);
       pawnSpace = root.findViewById(R.id.pawnSpace);
       pawnBackup = root.findViewById(R.id.pawnBackup);

       updateSMSBtn = root.findViewById(R.id.upDateSMSBtn);
       agentBtn = root.findViewById(R.id.agentBtn);
       fdsrvoneBtn = root.findViewById(R.id.fdsrvoneBtn);
       fdsrvtwoBtn = root.findViewById(R.id.fdsrvtwoBtn);
       capPDCBtn = root.findViewById(R.id.capPDCBtn);
       dvsUpdateBtn = root.findViewById(R.id.dvsUpdateBtn);
       abiUpdateBtn = root.findViewById(R.id.abiBtnUpdate);
       manifestBtnUpdate = root.findViewById(R.id.manifestBtnUpdate);
       caftanBtnUpdate = root.findViewById(R.id.caftanBtnUpdate);
       amsBtnUpdate = root.findViewById(R.id.amsBtnUpdate);
       pawnBtnUpdate = root.findViewById(R.id.pawnBtnUpdate);

        mRequestQue = Volley.newRequestQueue(getActivity());
        FirebaseMessaging.getInstance().subscribeToTopic("news");

       updateSMSBtn.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               sms = "SMSGateway:";
               if (creditsCheck.isChecked()){
                   sms += "\n SMS Credits checked";
               }if (smsSpace.isChecked()){
                   sms += "\n Space has been checked";
               }if (smsBackups.isChecked()){
                   sms += "\n Backups completed";
               }else {
                   sms += "\n Nothing completed";
               }
               Toast.makeText(getActivity(), sms+"\n at "+currDate, Toast.LENGTH_LONG).show();
               TOPIC = "/topics/userFINDEV"; //topic has to match what the receiver subscribed to
               NOTIFICATION_TITLE = "SMS Gateway";
               NOTIFICATION_MESSAGE = sms+" SMS Gateway updated at "+currDate;
               RANDOM_TEXT = "Message testing";

               JSONObject notification = new JSONObject();
               JSONObject notifcationBody = new JSONObject();
               try {
                   notifcationBody.put("title", "SMS Gateway");
                   notifcationBody.put("message", "SMS Gateway Server has been updated");

                   notification.put("to", TOPIC);
                   notification.put("data", notifcationBody);
               } catch (JSONException e) {
                   Log.e(TAG, "onCreate: " + e.getMessage() );
               }
               sendNotification(notification);
               sendNotifications();
              // sendEmail();
           }
       });
       agentBtn.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               String agent = "LAMAgent Server";
               if (agentSpace.isChecked()){
                   agent += "\n Space has been checked";
               }if (agentBackups.isChecked()){
                   agent += "\n Backups completed";
               }else {
                   agent += "\n Nothing completed";
               }
               Toast.makeText(getActivity(), agent+"\n at "+currDate, Toast.LENGTH_LONG).show();
               sendNotificationAgent();
           }
       });
       fdsrvoneBtn.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               String fdsrv01 = "FDSRV01 Server";
               if (fdsrvoneMissed.isChecked()){
                   fdsrv01 += "\n Missed call has been checked";
               }if (fdsrvoneSpace.isChecked()){
                   fdsrv01 += "\n Space has been checked";
               }else {
                   fdsrv01 += "\n Nothing completed";
               }
               Toast.makeText(getActivity(), fdsrv01+"\n at "+currDate, Toast.LENGTH_LONG).show();
               sendNotificationFDSRV01();
           }
       });
       fdsrvtwoBtn.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               String fdsrv02 = "FDSRV02 Server";
               if (fdsrvtwoSpace.isChecked()){
                   fdsrv02 += "\n Space has been checked";
               }if (fdsrvtwoBackup.isChecked()){
                   fdsrv02 += "\n Backup has been completed";
               }else {
                   fdsrv02 += "\n Nothing completed";
               }
               Toast.makeText(getActivity(), fdsrv02+"\n at "+currDate, Toast.LENGTH_LONG).show();
               sendNotificationFDSRV02();
           }
       });
       capPDCBtn.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               String lampdc = "LAMPDC Server";
               if (caftanSpace.isChecked()){
                   lampdc += "\n Space has been checked";
               }if (capPDCbackup.isChecked()){
                   lampdc += "\n Backup has been completed";
               }else {
                   lampdc += "\n Nothing completed";
               }
               Toast.makeText(getActivity(), lampdc+"\n at "+currDate, Toast.LENGTH_LONG).show();
               sendNotificationCAP();
           }
       });
       dvsUpdateBtn.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               String dvs = "DVS Server";
               if (dvsSpace.isChecked()){
                   dvs += "\n Space has been checked";
               }if (dvsBackups.isChecked()){
                   dvs += "\n Backup has been completed";
               }else {
                   dvs += "\n Nothing completed";
               }
               Toast.makeText(getActivity(), dvs+"\n at "+currDate, Toast.LENGTH_LONG).show();
               sendNotificationDVS();
           }
       });
       abiUpdateBtn.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               String abi = "ABI Server";
               if (abiSpace.isChecked()){
                   abi += "\n Space has been checked";
               }if (abiBackups.isChecked()){
                   abi += "\n Backup has been completed";
               }else {
                   abi += "\n Nothing completed";
               }
               Toast.makeText(getActivity(), abi+"\n at "+currDate, Toast.LENGTH_LONG).show();
               sendNotificationABI();
           }
       });
       manifestBtnUpdate.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               String manifest = "Manifest Server";
               if (manifestSpace.isChecked()){
                   manifest += "\n Space has been checked";
               }if (manifestBackups.isChecked()){
                   manifest += "\n Backup has been completed";
               }else {
                   manifest += "\n Nothing completed";
               }
               Toast.makeText(getActivity(), manifest+"\n at "+currDate, Toast.LENGTH_LONG).show();
               sendNotificationMan();
           }
       });
       caftanBtnUpdate.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               String caftan = "Caftan/ct20 Server";
               if (caftanSpace.isChecked()){
                   caftan += "\n Space has been checked";
               }if (caftanBackup.isChecked()){
                   caftan += "\n Backup has been completed";
               }else {
                   caftan += "\n Nothing completed";
               }
               Toast.makeText(getActivity(), caftan+"\n at "+currDate, Toast.LENGTH_LONG).show();
               sendNotificationCaf();
           }
       });
       amsBtnUpdate.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               String ams = "AMS Server";
               if (amsSpace.isChecked()){
                   ams += "\n Space has been checked";
               }if (amsBackup.isChecked()){
                   ams += "\n Backup has been completed";
               }else {
                   ams += "\n Nothing completed";
               }
               Toast.makeText(getActivity(), ams+"\n at "+currDate, Toast.LENGTH_LONG).show();
               sendNotificationAMS();
           }
       });
       pawnBtnUpdate.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               String pawnit = "PawnIt Server";
               if (pawnSpace.isChecked()){
                   pawnit += "\n Space has been checked";
               }if (pawnBackup.isChecked()){
                   pawnit += "\n Backup has been completed";
               }else {
                   pawnit += "\n Nothing completed";
               }
               Toast.makeText(getActivity(), pawnit+"\n at "+currDate, Toast.LENGTH_LONG).show();
               sendNotificationPawn();
           }
       });

        return root;
    }

    private void sendNotification(JSONObject notification) {
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(FCM_API, notification,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.i(TAG, "onResponse: " + response.toString());

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getActivity(), "Request error", Toast.LENGTH_LONG).show();
                        Log.i(TAG, "onErrorResponse: Didn't work");
                    }
                }){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("Authorization", serverKey);
                params.put("Content-Type", contentType);
                return params;
            }
        };
        MySingleton.getInstance(getActivity()).addToRequestQueue(jsonObjectRequest);
    }
    private void sendNotifications() {

        JSONObject json = new JSONObject();
        try {
            json.put("to","/topics/"+"news");
            JSONObject notificationObj = new JSONObject();
            notificationObj.put("title","SMS GATEWAY");
            notificationObj.put("body","SMSGateway server has been checked at "+currDate);

            JSONObject extraData = new JSONObject();
            extraData.put("brandId","puma");
            extraData.put("category","Shoes");



            json.put("notification",notificationObj);
            json.put("data",extraData);


            JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, URL,
                    json,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {

                            Log.d("MUR", "onResponse: ");
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.d("MUR", "onError: "+error.networkResponse);
                }
            }
            ){
                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    Map<String,String> header = new HashMap<>();
                    header.put("content-type","application/json");
                    header.put("authorization","key=AAAAdPiocKs:APA91bHSxbu7ojW87tseOKxMcR79gXdUPJ7ZoejyvHuMwQL1LprogNT6F76cPusOo753SR8CLGb-EXlCa2f0-pKxDrVJo1KRxA398bQmHr06cbmnjv8pOrV9GmJbQPGhcupf3eZipbjX");
                    return header;
                }
            };
            mRequestQue.add(request);
        }
        catch (JSONException e)

        {
            e.printStackTrace();
        }
    }
    private void sendNotificationAgent() {

        JSONObject json = new JSONObject();
        try {
            json.put("to","/topics/"+"news");
            JSONObject notificationObj = new JSONObject();
            notificationObj.put("title","LAMAgent");
            notificationObj.put("body","LAMAgent server has been checked at "+currDate);

            JSONObject extraData = new JSONObject();
            extraData.put("brandId","puma");
            extraData.put("category","Shoes");



            json.put("notification",notificationObj);
            json.put("data",extraData);


            JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, URL,
                    json,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {

                            Log.d("MUR", "onResponse: ");
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.d("MUR", "onError: "+error.networkResponse);
                }
            }
            ){
                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    Map<String,String> header = new HashMap<>();
                    header.put("content-type","application/json");
                    header.put("authorization","key=AAAAdPiocKs:APA91bHSxbu7ojW87tseOKxMcR79gXdUPJ7ZoejyvHuMwQL1LprogNT6F76cPusOo753SR8CLGb-EXlCa2f0-pKxDrVJo1KRxA398bQmHr06cbmnjv8pOrV9GmJbQPGhcupf3eZipbjX");
                    return header;
                }
            };
            mRequestQue.add(request);
        }
        catch (JSONException e)

        {
            e.printStackTrace();
        }
    }
    private void sendNotificationFDSRV01() {

        JSONObject json = new JSONObject();
        try {
            json.put("to","/topics/"+"news");
            JSONObject notificationObj = new JSONObject();
            notificationObj.put("title","FDSRV01");
            notificationObj.put("body","FDSRV01 server has been checked at "+currDate);

            JSONObject extraData = new JSONObject();
            extraData.put("brandId","puma");
            extraData.put("category","Shoes");



            json.put("notification",notificationObj);
            json.put("data",extraData);


            JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, URL,
                    json,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {

                            Log.d("MUR", "onResponse: ");
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.d("MUR", "onError: "+error.networkResponse);
                }
            }
            ){
                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    Map<String,String> header = new HashMap<>();
                    header.put("content-type","application/json");
                    header.put("authorization","key=AAAAdPiocKs:APA91bHSxbu7ojW87tseOKxMcR79gXdUPJ7ZoejyvHuMwQL1LprogNT6F76cPusOo753SR8CLGb-EXlCa2f0-pKxDrVJo1KRxA398bQmHr06cbmnjv8pOrV9GmJbQPGhcupf3eZipbjX");
                    return header;
                }
            };
            mRequestQue.add(request);
        }
        catch (JSONException e)

        {
            e.printStackTrace();
        }
    }
    private void sendNotificationCAP() {

        JSONObject json = new JSONObject();
        try {
            json.put("to","/topics/"+"news");
            JSONObject notificationObj = new JSONObject();
            notificationObj.put("title","LAMCAP PDC");
            notificationObj.put("body","LAMCAP PDC server has been checked at "+currDate);

            JSONObject extraData = new JSONObject();
            extraData.put("brandId","puma");
            extraData.put("category","Shoes");



            json.put("notification",notificationObj);
            json.put("data",extraData);


            JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, URL,
                    json,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {

                            Log.d("MUR", "onResponse: ");
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.d("MUR", "onError: "+error.networkResponse);
                }
            }
            ){
                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    Map<String,String> header = new HashMap<>();
                    header.put("content-type","application/json");
                    header.put("authorization","key=AAAAdPiocKs:APA91bHSxbu7ojW87tseOKxMcR79gXdUPJ7ZoejyvHuMwQL1LprogNT6F76cPusOo753SR8CLGb-EXlCa2f0-pKxDrVJo1KRxA398bQmHr06cbmnjv8pOrV9GmJbQPGhcupf3eZipbjX");
                    return header;
                }
            };
            mRequestQue.add(request);
        }
        catch (JSONException e)

        {
            e.printStackTrace();
        }
    }
    private void sendNotificationFDSRV02() {

        JSONObject json = new JSONObject();
        try {
            json.put("to","/topics/"+"news");
            JSONObject notificationObj = new JSONObject();
            notificationObj.put("title","FDSRV02");
            notificationObj.put("body","FDSRV02 server has been checked at "+currDate);

            JSONObject extraData = new JSONObject();
            extraData.put("brandId","puma");
            extraData.put("category","Shoes");



            json.put("notification",notificationObj);
            json.put("data",extraData);


            JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, URL,
                    json,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {

                            Log.d("MUR", "onResponse: ");
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.d("MUR", "onError: "+error.networkResponse);
                }
            }
            ){
                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    Map<String,String> header = new HashMap<>();
                    header.put("content-type","application/json");
                    header.put("authorization","key=AAAAdPiocKs:APA91bHSxbu7ojW87tseOKxMcR79gXdUPJ7ZoejyvHuMwQL1LprogNT6F76cPusOo753SR8CLGb-EXlCa2f0-pKxDrVJo1KRxA398bQmHr06cbmnjv8pOrV9GmJbQPGhcupf3eZipbjX");
                    return header;
                }
            };
            mRequestQue.add(request);
        }
        catch (JSONException e)

        {
            e.printStackTrace();
        }
    }
    private void sendNotificationDVS() {

        JSONObject json = new JSONObject();
        try {
            json.put("to","/topics/"+"news");
            JSONObject notificationObj = new JSONObject();
            notificationObj.put("title","DVS");
            notificationObj.put("body","DVS server has been checked at "+currDate);

            JSONObject extraData = new JSONObject();
            extraData.put("brandId","puma");
            extraData.put("category","Shoes");



            json.put("notification",notificationObj);
            json.put("data",extraData);


            JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, URL,
                    json,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {

                            Log.d("MUR", "onResponse: ");
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.d("MUR", "onError: "+error.networkResponse);
                }
            }
            ){
                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    Map<String,String> header = new HashMap<>();
                    header.put("content-type","application/json");
                    header.put("authorization","key=AAAAdPiocKs:APA91bHSxbu7ojW87tseOKxMcR79gXdUPJ7ZoejyvHuMwQL1LprogNT6F76cPusOo753SR8CLGb-EXlCa2f0-pKxDrVJo1KRxA398bQmHr06cbmnjv8pOrV9GmJbQPGhcupf3eZipbjX");
                    return header;
                }
            };
            mRequestQue.add(request);
        }
        catch (JSONException e)

        {
            e.printStackTrace();
        }
    }
    private void sendNotificationABI() {

        JSONObject json = new JSONObject();
        try {
            json.put("to","/topics/"+"news");
            JSONObject notificationObj = new JSONObject();
            notificationObj.put("title","ABI");
            notificationObj.put("body","ABI server has been checked at "+currDate);

            JSONObject extraData = new JSONObject();
            extraData.put("brandId","puma");
            extraData.put("category","Shoes");



            json.put("notification",notificationObj);
            json.put("data",extraData);


            JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, URL,
                    json,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {

                            Log.d("MUR", "onResponse: ");
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.d("MUR", "onError: "+error.networkResponse);
                }
            }
            ){
                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    Map<String,String> header = new HashMap<>();
                    header.put("content-type","application/json");
                    header.put("authorization","key=AAAAdPiocKs:APA91bHSxbu7ojW87tseOKxMcR79gXdUPJ7ZoejyvHuMwQL1LprogNT6F76cPusOo753SR8CLGb-EXlCa2f0-pKxDrVJo1KRxA398bQmHr06cbmnjv8pOrV9GmJbQPGhcupf3eZipbjX");
                    return header;
                }
            };
            mRequestQue.add(request);
        }
        catch (JSONException e)

        {
            e.printStackTrace();
        }
    }
    private void sendNotificationMan() {

        JSONObject json = new JSONObject();
        try {
            json.put("to","/topics/"+"news");
            JSONObject notificationObj = new JSONObject();
            notificationObj.put("title","Manifest");
            notificationObj.put("body","Manifest server has been checked at "+currDate);

            JSONObject extraData = new JSONObject();
            extraData.put("brandId","puma");
            extraData.put("category","Shoes");



            json.put("notification",notificationObj);
            json.put("data",extraData);


            JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, URL,
                    json,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {

                            Log.d("MUR", "onResponse: ");
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.d("MUR", "onError: "+error.networkResponse);
                }
            }
            ){
                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    Map<String,String> header = new HashMap<>();
                    header.put("content-type","application/json");
                    header.put("authorization","key=AAAAdPiocKs:APA91bHSxbu7ojW87tseOKxMcR79gXdUPJ7ZoejyvHuMwQL1LprogNT6F76cPusOo753SR8CLGb-EXlCa2f0-pKxDrVJo1KRxA398bQmHr06cbmnjv8pOrV9GmJbQPGhcupf3eZipbjX");
                    return header;
                }
            };
            mRequestQue.add(request);
        }
        catch (JSONException e)

        {
            e.printStackTrace();
        }
    }
    private void sendNotificationCaf() {

        JSONObject json = new JSONObject();
        try {
            json.put("to","/topics/"+"news");
            JSONObject notificationObj = new JSONObject();
            notificationObj.put("title","Caftan ct20");
            notificationObj.put("body","Caftan/ct20 server has been checked at "+currDate);

            JSONObject extraData = new JSONObject();
            extraData.put("brandId","puma");
            extraData.put("category","Shoes");



            json.put("notification",notificationObj);
            json.put("data",extraData);


            JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, URL,
                    json,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {

                            Log.d("MUR", "onResponse: ");
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.d("MUR", "onError: "+error.networkResponse);
                }
            }
            ){
                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    Map<String,String> header = new HashMap<>();
                    header.put("content-type","application/json");
                    header.put("authorization","key=AAAAdPiocKs:APA91bHSxbu7ojW87tseOKxMcR79gXdUPJ7ZoejyvHuMwQL1LprogNT6F76cPusOo753SR8CLGb-EXlCa2f0-pKxDrVJo1KRxA398bQmHr06cbmnjv8pOrV9GmJbQPGhcupf3eZipbjX");
                    return header;
                }
            };
            mRequestQue.add(request);
        }
        catch (JSONException e)

        {
            e.printStackTrace();
        }
    }
    private void sendNotificationAMS() {

        JSONObject json = new JSONObject();
        try {
            json.put("to","/topics/"+"news");
            JSONObject notificationObj = new JSONObject();
            notificationObj.put("title","AMS");
            notificationObj.put("body","AMS server has been checked at "+currDate);

            JSONObject extraData = new JSONObject();
            extraData.put("brandId","puma");
            extraData.put("category","Shoes");



            json.put("notification",notificationObj);
            json.put("data",extraData);


            JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, URL,
                    json,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {

                            Log.d("MUR", "onResponse: ");
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.d("MUR", "onError: "+error.networkResponse);
                }
            }
            ){
                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    Map<String,String> header = new HashMap<>();
                    header.put("content-type","application/json");
                    header.put("authorization","key=AAAAdPiocKs:APA91bHSxbu7ojW87tseOKxMcR79gXdUPJ7ZoejyvHuMwQL1LprogNT6F76cPusOo753SR8CLGb-EXlCa2f0-pKxDrVJo1KRxA398bQmHr06cbmnjv8pOrV9GmJbQPGhcupf3eZipbjX");
                    return header;
                }
            };
            mRequestQue.add(request);
        }
        catch (JSONException e)

        {
            e.printStackTrace();
        }
    }
    private void sendNotificationPawn() {

        JSONObject json = new JSONObject();
        try {
            json.put("to","/topics/"+"news");
            JSONObject notificationObj = new JSONObject();
            notificationObj.put("title","PawnIt");
            notificationObj.put("body","PawnIt server has been checked at "+currDate);

            JSONObject extraData = new JSONObject();
            extraData.put("brandId","puma");
            extraData.put("category","Shoes");



            json.put("notification",notificationObj);
            json.put("data",extraData);


            JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, URL,
                    json,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {

                            Log.d("MUR", "onResponse: ");
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.d("MUR", "onError: "+error.networkResponse);
                }
            }
            ){
                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    Map<String,String> header = new HashMap<>();
                    header.put("content-type","application/json");
                    header.put("authorization","key=AAAAdPiocKs:APA91bHSxbu7ojW87tseOKxMcR79gXdUPJ7ZoejyvHuMwQL1LprogNT6F76cPusOo753SR8CLGb-EXlCa2f0-pKxDrVJo1KRxA398bQmHr06cbmnjv8pOrV9GmJbQPGhcupf3eZipbjX");
                    return header;
                }
            };
            mRequestQue.add(request);
        }
        catch (JSONException e)

        {
            e.printStackTrace();
        }
    }


}