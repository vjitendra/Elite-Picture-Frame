package com.EFrame11;

import java.io.File;
import java.util.Calendar;
import android.app.Activity;

import android.content.Intent;
import android.location.LocationManager;
import android.os.Bundle;

public class EFrame11 extends Activity {
	DBAdapter db = new DBAdapter(this);
	protected LocationManager locationManager;
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       
        setContentView(R.layout.main);
        
        db.open();
        
        defaultInsert();
        
        Intent i = new Intent(EFrame11.this, home.class);
        startActivity(i);
        
     
    }
    
    void defaultInsert()
    {
    	if(db.checkIfPathExits() == 0)
			db.insertPath("/sdcard/elitepicturephotos");
		
		if(db.checkIfAlbumExist("temp") == 0)
		{
			Calendar calendar = Calendar.getInstance();
	    	java.util.Date now = calendar.getTime();
	    	java.util.Date today = new java.util.Date();
			java.sql.Timestamp ti = new java.sql.Timestamp(now.getTime());
			
			db.insertAlbum("temp", 0, "", ti.toString(), "", 0, 3000);
		}
		
		File camPhotosDirectory = new File("/sdcard/ElitePictureCamera/");
		if(!(camPhotosDirectory.exists()))
			camPhotosDirectory.mkdirs();
    }
}