package com.EFrame11;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Calendar;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.os.Bundle;

public class getPhotosFromPC extends Activity{

	DBAdapter db = new DBAdapter(this);
	Session ss = new Session();
	InputStream is = null;
	int flag=0;
	
	 /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        db.open();
        getPhotos();
    }
	
    void getPhotos()
    {
    	String result="", temp="";
    	
    	ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
	    nameValuePairs.add(new BasicNameValuePair("epfid",ss.getSessionElitePictureFrameId()));
	    
	    try
        {
               HttpClient httpclient = new DefaultHttpClient();
               HttpPost httppost = new HttpPost("http://bpsi.us/blueplanetsolutions/elitepictureframe/EliteSite/getPicturesFromPC.php");
               httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
               HttpResponse response = httpclient.execute(httppost);
               HttpEntity entity = response.getEntity();
               is = entity.getContent();
        }
        catch(Exception e)
        {
       	 	flag=1;
        }
     
        if(flag!=1)
        {
       
       try
       {
       	BufferedReader reader = new BufferedReader(new InputStreamReader(is,"iso-8859-1"),8);
       	StringBuilder sb = new StringBuilder();
       	String line = null;
    
       	while ((line = reader.readLine()) != null) 
       	{
       		sb.append(line + "\n");
       	}
       	is.close();
       	result=sb.toString();
       }
       catch(Exception e)
       {
   	 
       }
     
    	try{
     
    		
           JSONArray jArray = new JSONArray(result);
           
           	Calendar calendar = Calendar.getInstance();
       		java.util.Date now = calendar.getTime();
       		java.util.Date today = new java.util.Date();
       		java.sql.Timestamp ti = new java.sql.Timestamp(now.getTime());
           
           	for(int i=0;i<jArray.length();i++)
           	{
           			JSONObject json_data = jArray.getJSONObject(i);
           			temp = temp+"\nId: "+json_data.getString("photo_link");
           			db.insertPhoto("/sdcard/"+json_data.getString("photo_link"), "60MB", ti.toString(), "", 
           	    			"","", "", "", 
           	    			json_data.getString("photo_link"), "");
           	}
           
    	}
    	catch(JSONException e)
    	{
    		
    	}
        }
    }
}
