package com.EFrame11;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

public class EditAlbum extends Activity{
	
	DBAdapter db = new DBAdapter(this);
	Session ss = new Session();
	String selectedAlbum;
	
	private static int aid1;
	EditText aname;
	EditText music;
	Button change;
	private static String aname1;
	private static String music1;
	private static String shuffle1;
	private static String transition1;
	int flag_exist;
	TextView text1;
	String music2="";
	String array_spinner[];
	Spinner s;
	
	/** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       
             
        db.open();
        selectedAlbum = ss.getSessionAlbumName();
        
        final Dialog dialog2 = new Dialog(EditAlbum.this);
			dialog2.setContentView(R.layout.edit_album);
			dialog2.setCancelable(true);
			
			       
        text1 = (TextView)dialog2.findViewById(R.id.text1);     
        aname = (EditText)dialog2.findViewById(R.id.aname);	
        music = (EditText)dialog2.findViewById(R.id.music);
       		
        array_spinner=new String[5];
        array_spinner[0]="2 sec";
        array_spinner[1]="3 sec";
        array_spinner[2]="4 sec";
        array_spinner[3]="5 sec";
        array_spinner[4]="6 sec";
   
        s = (Spinner) dialog2.findViewById(R.id.test);
        ArrayAdapter adapter = new ArrayAdapter(this,
        android.R.layout.simple_spinner_item, array_spinner);
        s.setAdapter(adapter);
        
        music2 = ss.getSessionMusicSelected();
        
        if((music2.equals("")))
        {
        	Cursor c = db.getAlbum(db.getAlbumId(selectedAlbum));
        	if (c.moveToFirst())  
        	{
        		aid1 = c.getInt(0);
        		
        		aname1 = c.getString(1);
        		music1 = c.getString(3);
        		shuffle1 = c.getString(6);
        		transition1 = c.getString(7);
        		
        		aname.setText(aname1);
        		music.setText(music1);
        		
        		s.setSelection(Integer.parseInt(""+transition1.charAt(0))-2);
        	}
        	c.close();
        }
        else
        {
        	aname.setText(aname1);
    		    		
    		s.setSelection(Integer.parseInt(""+transition1.charAt(0))-2);
    		
        	music.setText(music2);
        	ss.setSessionMusicSelected("");
        }
        
		change = (Button)dialog2.findViewById(R.id.change);
		change.setOnClickListener(new Button.OnClickListener() {
		 public void onClick(View v) {
                 
			Intent i = new Intent(EditAlbum.this, MusicActivity.class);
			startActivity(i);
		 }
         });
		
		Button cancel = (Button)dialog2.findViewById(R.id.cancel);
		cancel.setOnClickListener(new Button.OnClickListener() 
		{
			 public void onClick(View v) {
	                 
				 dialog2.dismiss();
				 db.close();
				 Intent i = new Intent(EditAlbum.this, OpenAlbum.class);
					startActivity(i);
			 }
	         });
		
		Button edit_button = (Button)dialog2.findViewById(R.id.edit_button);
		edit_button.setOnClickListener(new Button.OnClickListener() 
		{ public void onClick (View v)
			{
				flag_exist = db.checkIfAlbumExist(aid1, aname.getText().toString());
			
				if(flag_exist == 0)
				{
					int index = s.getSelectedItemPosition();
					String item = (String) s.getItemAtPosition(index);
					int trans=3000;
					if(item.equals("2 sec"))
						trans=2000;
					else if(item.equals("3 sec"))
						trans=3000;
					else if(item.equals("4 sec"))
						trans=4000;
					else if(item.equals("5 sec"))
						trans=5000;
					else if(item.equals("6 sec"))
						trans=6000;
					
					db.updateAlbumEdit(aid1, 
												aname.getText().toString(), 
												music.getText().toString(),
												0,
												trans);
					
					db.close();
					Intent i = new Intent(EditAlbum.this, OpenAlbum.class);
					startActivity(i);
										
				}
				else if(flag_exist == 1)
				{
					text1.setText("Album same name already exist!!"); 
				}
			}
		});
		
		dialog2.show();
    }
	

}
