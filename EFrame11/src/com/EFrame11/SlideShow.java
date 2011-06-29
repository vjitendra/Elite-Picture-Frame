package com.EFrame11;

import java.io.IOException;
import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.content.res.Configuration;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class SlideShow extends Activity{
	
	String selectedAlbumName;
	String photosInSelectedAlbum[];
	ImageView jpgView;
	DBAdapter db = new DBAdapter(this);
	Session ss = new Session();
	private Handler handler = new Handler();
	 int i=0;
	int time = 3000;
	String music="";
	MediaPlayer mMediaPlayer = new MediaPlayer();;
	int flag=0;
	int current_i;
	int length;
	Button viewDetails, editDetails, setAsWallpaper;
	int coming_back=0;
	Button mailToFriend;
	int flag1 =0;
	
	
	/** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
                     
        setContentView(R.layout.slideshow);
        db.open();
              
        selectedAlbumName = ss.getSessionAlbumName();
        int aid = db.getAlbumId(selectedAlbumName);
        time = db.getSlideshowTime(aid);
        music = db.getMusicLink(aid);
                      
        photosInSelectedAlbum = db.getphotoLocationOfAlbum(selectedAlbumName);
        jpgView = (ImageView)findViewById(R.id.jpgview);
        viewDetails = (Button)findViewById(R.id.viewDetails);
        setAsWallpaper = (Button)findViewById(R.id.setAsWallpaper);
          	
        startSlideShow();
        
        jpgView.setOnClickListener(new Button.OnClickListener() 
		{ public void onClick (View v)
			{
				if(flag==0)
				{
					flag=1;
					viewDetails.setVisibility(View.VISIBLE);
					setAsWallpaper.setVisibility(View.VISIBLE);
					
					if(mMediaPlayer.isPlaying())
					{
						mMediaPlayer.pause();
						length=mMediaPlayer.getCurrentPosition();
					}
					
				}
				else if(flag==1)
				{
					flag=0;
					
					viewDetails.setVisibility(View.GONE);
					setAsWallpaper.setVisibility(View.GONE);
					
					if(!(music.equals("")))
					{
						mMediaPlayer.seekTo(length);
						mMediaPlayer.start();
					}
					startSlideShow();
					
				}
			}
		});
               
        setAsWallpaper.setOnClickListener(new Button.OnClickListener() 
		{ public void onClick (View v)
			{
				Bitmap bitmap = BitmapFactory.decodeFile(photosInSelectedAlbum[i]);

			        try {
			        getApplicationContext().setWallpaper(bitmap);
			        } catch (IOException e) {
			        e.printStackTrace();
			        }
			}
		});        
        
		
		viewDetails.setOnClickListener(new Button.OnClickListener() 
		{ public void onClick (View v)
			{
				final Dialog dialog = new Dialog(SlideShow.this);
				dialog.setContentView(R.layout.view_photo_details_dialog);
				dialog.setTitle("         		Photo Details");
				dialog.setCancelable(true);
				
				String photo = photosInSelectedAlbum[i-1];

				TextView viewDetailsDialog = (TextView) dialog.findViewById(R.id.viewDetailsDialog);
				if((db.checkIfPhotoExist(photo)) == 0)
				{
					viewDetailsDialog.setText("\nImage: "+photosInSelectedAlbum[i]);
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
					Cursor c = db.getPhoto(db.getPhotoId(photo));
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
					c.close();
					viewDetailsDialog.setText("\nImage: "+photo+
							"\nSize: "+size+
							"\nDate: "+date_time+
							"\nPlace: "+place+
							"\nArea: "+area+
							"\nCity: "+city+
							"\nState: "+state+
							"\nCountry: "+country+
							"\nTag: "+tag);
				}
				
				 Button ok = (Button) dialog.findViewById(R.id.ok);
				 ok.setOnClickListener(new OnClickListener() {

                 public void onClick(View v) {
                         dialog.dismiss();
                     }
                 });
                 dialog.show();
			}
		});
		
		
	
    }
    
    void startSlideShow()
    {
    	 final Runnable r = new Runnable()
         {
    		 
             public void run() 
             {
            	 
            	 try 
            	 {
               	  	  mMediaPlayer.setDataSource(music);
               		  mMediaPlayer.prepare();
               		  mMediaPlayer.start();
                 	               	 
				 } catch (Exception e) {

					}
            	 
                  
                  
             	if(i<db.getnoOfPhotos(selectedAlbumName))
             	{
             		
             		if(flag == 0)
             		{
             			
             		BitmapFactory.Options options = new BitmapFactory.Options();
             		options.inSampleSize = 1;
             		Bitmap bm = BitmapFactory.decodeFile(photosInSelectedAlbum[i], options);
             		if(bm!=null)		                
             			jpgView.setImageBitmap(bm);
 	                else
 	                	jpgView.setImageResource(R.drawable.moved_photo);
 	                
             		handler.postDelayed(this, time);
             		
             		i++;
             		}
             		
             		
             	}
             	else
             	{
             		i=0;
             		flag=0;
             		
             		if (mMediaPlayer.isPlaying()) {
             	      	 mMediaPlayer.stop();
             	      }
             		//db.close();
             		Intent i = new Intent(SlideShow.this, OpenAlbum.class);
     				startActivity(i);
                 	
                 }
             }
              
         };
       
         handler.postDelayed(r, 2000);
         
    }
    
    @Override
    public void onConfigurationChanged(Configuration newConfig) {

        super.onConfigurationChanged(newConfig);

        if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT) {
        	flag1=1;
        	if (mMediaPlayer.isPlaying()) {
    	      	 mMediaPlayer.stop();
    	      }

        } else if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
        	flag1=1;
        	if (mMediaPlayer.isPlaying()) {
    	      	 mMediaPlayer.stop();
    	      }
        }
    }
}
