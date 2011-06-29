package com.EFrame11;

import java.io.InputStream;
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

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;

public class InsertDBAndUpdate extends Activity {
	
	DBAdapter db = new DBAdapter(this);
	Session ss = new Session();
	String elite_id="";
	InputStream is = null;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.update);

		db.open();
				
		Cursor mCursor = db.getEpfid();
	    	if (mCursor.moveToFirst())  
			{
	    		elite_id = mCursor.getString(1);
			}
	    	
	    	Calendar calendar = Calendar.getInstance();
       		java.util.Date now = calendar.getTime();
       		java.util.Date today = new java.util.Date();
       		java.sql.Timestamp ti = new java.sql.Timestamp(now.getTime());
	    	
       		int count = ss.getSessionPathsCount();
       		
       		for(int i=0; i<count; i++)
       		{
       			String path = ss.getSessionAllPaths(i);
       			int temp = path.lastIndexOf("/");
       			String image = path.substring(temp+1, path.length());
       			
       			System.out.println("Image: "+image);
       			
       			db.insertPhoto("/sdcard/ElitePicsFromPC/"+image, "60MB", ti.toString(), "", 
   	    			"","", "", "", 
   	    			image+","+elite_id, "");
       			System.out.println("Data inserted");
   			   			
       			ArrayList<NameValuePair> nameValuePairs1 = new ArrayList<NameValuePair>();
		        nameValuePairs1.add(new BasicNameValuePair("epfid",elite_id));
		        nameValuePairs1.add(new BasicNameValuePair("photo_link",image));
		        
		      try
	         {
	                HttpClient httpclient = new DefaultHttpClient();
	                HttpPost httppost = new HttpPost("http://bpsi.us/blueplanetsolutions/elitepictureframe/EliteSite/updateFlagPC.php");
	                httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs1));
	                HttpResponse response = httpclient.execute(httppost);
	                HttpEntity entity = response.getEntity();
	                is = entity.getContent();
	               
	             System.out.println("Flag updated");
	         }
	         catch(Exception e)
	         {
	        	 	
	         }
       		}
       		
       		Intent i = new Intent(InsertDBAndUpdate.this, home.class);
			 startActivity(i);
	}
	
}
