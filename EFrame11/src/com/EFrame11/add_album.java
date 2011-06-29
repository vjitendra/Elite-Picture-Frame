package com.EFrame11;

import java.util.Calendar;
import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class add_album extends Activity{
	
	int total;
	private EditText album_name = null;
	private Button add_new_album=null;
	DBAdapter db = new DBAdapter(this);
	String []album_names;
	int i=0;
	Session ss = new Session();
	Dialog dialog2;
	
	/** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
              
        db.open();
  
        dialog2 = new Dialog(add_album.this);
		dialog2.setContentView(R.layout.add_album);
	
		dialog2.setCancelable(true);
        
        Button back = (Button)dialog2.findViewById(R.id.back);
        back.setOnClickListener(new Button.OnClickListener() 
		{ public void onClick (View v)
			{
				dialog2.dismiss();
				Intent i = new Intent(add_album.this, home.class);
				startActivity(i);
			}
		});
        
        add_new_album = (Button)dialog2.findViewById(R.id.add_new_album);
    	add_new_album.setOnClickListener(new Button.OnClickListener() 
    			{ public void onClick (View v)
    				{
    					addAlbum();
    				}
    			});
    	dialog2.show();
    }
    
   
    
    void addAlbum()
    {
    	album_name = (EditText)dialog2.findViewById(R.id.album_name);
					
		if((album_name.getText().toString()).equals(""))
		{
			Toast toast = Toast.makeText(add_album.this, 
            		"Please enter the Name!!",
            		Toast.LENGTH_LONG);
            toast.show();
		}
		else if(db.checkIfAlbumExist(album_name.getText().toString()) == 0)
		{
			Calendar calendar = Calendar.getInstance();
	    	java.util.Date now = calendar.getTime();
	    	java.util.Date today = new java.util.Date();
			java.sql.Timestamp ti = new java.sql.Timestamp(now.getTime());
			
			db.insertAlbum(album_name.getText().toString(), 0, "", ti.toString(), "", 0, 3000);
			
			ss.setSessionAlbumName(album_name.getText().toString());
			
			dialog2.dismiss();
			
			db.close();
			
			Intent i = new Intent(this, SdCardPhotos.class);
			startActivity(i);
			
		}
		else
		{
			Toast toast = Toast.makeText(add_album.this, 
            		"Album already EXIST!!\nPlease enter other name..",
            		Toast.LENGTH_LONG);
            toast.show();
		}
		
    }
}
