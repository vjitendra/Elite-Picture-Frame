package com.EFrame11;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.CompoundButton.OnCheckedChangeListener;

public class DeletePhotosFromAlbum extends Activity{
	
	DBAdapter db = new DBAdapter(this); 
	Session ss = new Session();
	String albumSelected="";
	Cursor cursor;
	int pos, k, total, cnt=0;
	String imagePath1[];
	Button done, search;
	 TextView noOfPhotos;
	 EditText search_item;
	 String selectedPhotoName="";
	 int photosToDelete[];
	 	 
	 /** Called when the activity is first created. */
	    @Override
	    public void onCreate(Bundle savedInstanceState) {
	        super.onCreate(savedInstanceState);
	       
	        setContentView(R.layout.delete_photos_open_album);
	        
	        db.open();
	        albumSelected = ss.getSessionAlbumName();
	        
	        Button back = (Button)findViewById(R.id.back);
	        back.setOnClickListener(new Button.OnClickListener() 
			{ public void onClick (View v)
				{
					db.close();
					Intent i = new Intent(DeletePhotosFromAlbum.this, OpenAlbum.class);
					startActivity(i);
				}
			});
	        
	        done = (Button)findViewById(R.id.done);
	        done.setOnClickListener(new Button.OnClickListener() 
	    			{ public void onClick (View v)
	    				{
	    					k = db.getAlbumId(ss.getSessionAlbumName());
	    					
	    					
	    					for(int i=0; i<cnt; i++)
	    					{
	    						db.deletePhotoFromAlbum(photosToDelete[i], k);
	    					}
	    					
	    					int l = db.getFirstPhotoInAlbum(k);
	    					if(l!=0)
	    						db.updateAlbumAfterInsert(k, l);
	    					else
	    						db.updateAlbumAfterInsert(k, 0);
	    					
	    					db.close();
	    					Intent i = new Intent(DeletePhotosFromAlbum.this, OpenAlbum.class);
	    					startActivity(i);
	    					
	    				}
	    			});
	        
	        imagePath1 = new String[db.getnoOfPhotos(albumSelected)];
	        imagePath1 = db.getphotoLocationOfAlbum(albumSelected);
	       
	        photosToDelete = new int[imagePath1.length];
	        displayPhotos();
	    }
	    
	    void displayPhotos()
	    {
	    	noOfPhotos = (TextView)findViewById(R.id.noOfPhotos);
	    	noOfPhotos.setText("Select Photos["+imagePath1.length+"]");
	    	
	    	GridView sdcardImages = (GridView) findViewById(R.id.sdcard);
	        sdcardImages.setAdapter(new ImageAdapter(this));
	        
	        sdcardImages.setOnItemClickListener(new OnItemClickListener() {
	            public void onItemClick(AdapterView parent, View v, int position, long id) 
	            {
	            	selectedPhotoName = imagePath1[position];
	            	ss.setSessionSelectedPhoto(selectedPhotoName);
	    			db.close();
					Intent i = new Intent(DeletePhotosFromAlbum.this, FullPhotoEdit.class);
					startActivity(i);
					
	           }	
	        });
	    }
	    
	    private class ImageAdapter extends BaseAdapter {

	        private Context context;

	        public ImageAdapter(Context localContext) {
	            context = localContext;
	        }

	        public int getCount() {
	            return imagePath1.length;
	        }
	        public Object getItem(int position) {
	            return position;
	        }
	        public long getItemId(int position) {
	            return position;
	        }
	        public View getView(int position, View convertView, ViewGroup parent) {
	        	
	        	View v;
	            
	                LayoutInflater li = getLayoutInflater();
	                v = li.inflate(R.layout.delete_photos_open_album_row, null);
	                             
	                ImageView iv = (ImageView)v.findViewById(R.id.icon_image1);
	                Bitmap bMap = BitmapFactory.decodeFile(imagePath1[position]);
	                iv.setImageBitmap(bMap);

	                final CheckBox check1 = (CheckBox)v.findViewById(R.id.check1);
	                
	                String date_time="";
	                String place="";
	                
	                if((db.checkIfPhotoExist(imagePath1[position])) != 0)
	                {	
	                	Cursor c = db.getPhoto(db.getPhotoId(imagePath1[position]));
	                	if (c.moveToFirst())  
	                	{
	                		if(!(c.getString(3).equals("")))
	                			date_time=c.getString(3);
	                		                		
	                		if(!(c.getString(8).equals("")))
	                			place=c.getString(8);
	                		
	                		if(!(c.getString(7).equals("")))
	                		{
	                			if(!(place.equals("")))
	                				place=place + ","+c.getString(7);
	                			else
	                				place=c.getString(7);
	                		}
	                		
	                		if(!(c.getString(6).equals("")))
	                		{
	                			if(!(place.equals("")))
	                				place=place + ","+c.getString(6);
	                			else
	                				place=c.getString(6);
	                		}
	                		
	                		if(!(c.getString(5).equals("")))
	                		{
	                			if(!(place.equals("")))
	                				place=place + ","+c.getString(5);
	                			else
	                				place=c.getString(5);
	                		}
	                		
	                		if(!(c.getString(4).equals("")))
	                		{
	                			if(!(place.equals("")))
	                				place=place + ","+c.getString(4);
	                			else
	                				place=c.getString(4);
	                		}
	                	}
	                	
	                	c.close();
	                	
	                	if((!(date_time.equals(""))) && (!(place.equals(""))))
	                		check1.setText("Details:\nDate: "+date_time+"\nPlace: "+place);
	                	else if((date_time.equals("")) && (!(place.equals(""))))
	                		check1.setText("Details:\nPlace: "+place);
	                	else if((!(date_time.equals(""))) && (place.equals("")))
	                		check1.setText("Details:\nDate: "+date_time);
	                	else
	                		check1.setText("Details:\nImage: "+imagePath1[position]);
	                }
	                else
	                {
	                	check1.setText("Details:\nImage: "+imagePath1[position]);
	                }
	                check1.setId(position);
	                
	                check1.setOnCheckedChangeListener(new OnCheckedChangeListener()
	                {
	                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked)
	                    {
	                        if ( isChecked )
	                        {
	                       	 		photosToDelete[cnt] = db.getPhotoId(imagePath1[check1.getId()]);
	                       	 		cnt++;
	                        }
	                        else
	                        {
	                        	
	                        	int j = db.getPhotoId(imagePath1[check1.getId()]);
	                        	int l=0;
	                        	for(int i=0; i<cnt; i++)
	                        	{
	                        		if(photosToDelete[i] == j)
	                        			l=i;
	                        	}
	                        	
	                        	for(int i=l; i<cnt; i++)
	                        	{
	                        		int temp = photosToDelete[i];
                        			photosToDelete[i] = photosToDelete[i+1];
	                        	}
								
	                        	cnt--;
	                        }
	                    }
	                });
	                
	          
	            return v;
	            
	        }
	    }

		public boolean onTouch(View v, MotionEvent event) {
			// TODO Auto-generated method stub
			return false;
		}
}
