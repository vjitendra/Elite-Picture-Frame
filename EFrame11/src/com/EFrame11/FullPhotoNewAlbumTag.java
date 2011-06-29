package com.EFrame11;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;


public class FullPhotoNewAlbumTag extends Activity{

	String selectedPhoto;
	DBAdapter db = new DBAdapter(this);
	Session ss = new Session();
	BitmapFactory.Options options;
	Button viewDetails, editDetails;
	
	/** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       
        db.open();
        selectedPhoto = ss.getSessionSelectedPhoto();
        
        setContentView(R.layout.full_photo_add_tag);
		ImageView fullPhoto = (ImageView)findViewById(R.id.image_view);
		
		options = new BitmapFactory.Options();
		options.inSampleSize = 1;
		Bitmap bm = BitmapFactory.decodeFile(selectedPhoto, options);
		fullPhoto.setImageBitmap(bm);
		
	
		Button back = (Button)findViewById(R.id.back);
        back.setOnClickListener(new Button.OnClickListener() 
		{ public void onClick (View v)
			{
				Intent i = new Intent(FullPhotoNewAlbumTag.this, SdCardPhotos.class);
				startActivity(i);
			}
		});
		
        
        
		viewDetails = (Button)findViewById(R.id.viewDetails);
		viewDetails.setOnClickListener(new Button.OnClickListener() 
		{ public void onClick (View v)
			{
				final Dialog dialog = new Dialog(FullPhotoNewAlbumTag.this);
				dialog.setContentView(R.layout.view_photo_details_dialog);
				dialog.setCancelable(true);

				TextView viewDetailsDialog = (TextView) dialog.findViewById(R.id.viewDetailsDialog);
				if((db.checkIfPhotoExist(selectedPhoto)) == 0)
				{
					viewDetailsDialog.setText("\nImage: "+selectedPhoto);
				}
				else
				{
					int pid;
					String imagePath1;
					String size = "Info not available";
					String country = "Info not available";
					String state = "Info not available";
					String city = "Info not available";
					String place = "Info not available";
					String area = "Info not available";
					String tag = "Info not available";
					String date_time = "Info not available";
					String frame = "Info not available";
					Cursor c = db.getPhoto(db.getPhotoId(selectedPhoto));
					if (c.moveToFirst())  
					{
						pid = c.getInt(0);
			    	 	imagePath1 = c.getString(1);
			    	 	if(!(c.getString(2).equals("")))
			    	 		size = c.getString(2);
			    	 	if(!(c.getString(3).equals("")))
			    	 		date_time = c.getString(3);
			    	 	if(!(c.getString(4).equals("")))	
			    	 		country = c.getString(4);
			    	 	if(!(c.getString(5).equals("")))
			    	 		state= c.getString(5);
			    	 	if(!(c.getString(6).equals("")))
			    	 		city = c.getString(6);
			    	 	if(!(c.getString(7).equals("")))
			    	 		area = c.getString(7);
			    	 	if(!(c.getString(8).equals("")))
			    	 		place = c.getString(8);
			    	 	if(!(c.getString(9).equals("")))
			    	 		tag = c.getString(9);
			    	 	if(!(c.getString(10).equals("")))
			    	 		frame = c.getString(10);
					}
					
					viewDetailsDialog.setText("\nImage: "+selectedPhoto+
							"\nSize: "+size+
							"\nDate: "+date_time+
							"\nPlace: "+place+
							"\nArea: "+area+
							"\nCity: "+city+
							"\nState: "+state+
							"\nCountry: "+country+
							"\nTag: "+tag);
				}
				
				Button cancel = (Button) dialog.findViewById(R.id.cancel);
				cancel.setOnClickListener(new OnClickListener() {

                public void onClick(View v) {
                        dialog.dismiss();
                    }
                });
				
				 Button ok = (Button) dialog.findViewById(R.id.ok);
				 ok.setOnClickListener(new OnClickListener() {

                 public void onClick(View v) {
                         dialog.dismiss();
                     }
                 });
                 dialog.show();
			}
		});
		
		editDetails = (Button)findViewById(R.id.editDetails);
		editDetails.setOnClickListener(new Button.OnClickListener() 
		{ public void onClick (View v)
			{
				ss.setSessionSelectedPhoto(selectedPhoto);
			
				Intent i = new Intent(FullPhotoNewAlbumTag.this, FullPhotoNewAlbumTagEdit.class);
				startActivity(i);
			}
		});
        
    }
    
    
	
}

