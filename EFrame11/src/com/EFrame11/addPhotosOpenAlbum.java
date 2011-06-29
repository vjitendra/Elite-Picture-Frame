package com.EFrame11;

import java.util.ArrayList;
import java.util.Calendar;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.CompoundButton.OnCheckedChangeListener;

public class addPhotosOpenAlbum extends Activity{
	
	DBAdapter db = new DBAdapter(this); 
	Session ss = new Session();
	String albumSelected="";
	Cursor cursor;
	int pos, k, total, i=0;;
	//String imagePath1[];
	Button done, search;
	 private int columnIndex;
	 EditText search_item;
	 TextView noOfPhotos;
	 String selectedPhotoName="";
	 String searchPhotos[];
	 String tag="";
	 int flag = 0;
	 String str[];
	 int j=0,l=0;
	 ArrayList<String> PhotoList = new ArrayList<String>();
	 ArrayList<String> PhotoList1 = new ArrayList<String>();
	 int search_flag=0;
	 
	/** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
              
        setContentView(R.layout.add_photos_open_album);
        
        db.open();
        albumSelected = ss.getSessionAlbumName();
        
        Button back = (Button)findViewById(R.id.back);
        back.setOnClickListener(new Button.OnClickListener() 
		{ public void onClick (View v)
			{
				db.close();
			
				Intent i = new Intent(addPhotosOpenAlbum.this, OpenAlbum.class);
				startActivity(i);
			}
		});
        
        done = (Button)findViewById(R.id.done);
        done.setOnClickListener(new Button.OnClickListener() 
    			{ public void onClick (View v)
    				{
    				k = db.getAlbumId(ss.getSessionAlbumName());
    				int l = db.getFirstPhotoInAlbum(k);
    				if(l!=0)
    					db.updateAlbumAfterInsert(k, l);
    				
    				db.close();
    				
    				Intent i = new Intent(addPhotosOpenAlbum.this, OpenAlbum.class);
    		        startActivity(i);
    				}
    			});
        
        
        
        search = (Button)findViewById(R.id.search);
        search.setOnClickListener(new Button.OnClickListener() 
    			{ public void onClick (View v)
    				{
    					search_item = (EditText)findViewById(R.id.search_item);
    					tag = search_item.getText().toString();
    					
    					if(!(tag.equals("")))
    						ConvertStringToArray(tag);
    					else
    						all_photos();
    				}
    			});
       
        all_photos();
    }

     
    void ConvertStringToArray(String tag)
	{
    	String str[] = new String[10];
    	
    	k=0;
    	j=0;
    	
		for(int i=0; i<tag.length(); i++)
		{
			if((tag.charAt(i) == ' ') && (i == 0))
			{
				while((tag.charAt(j) == ' ') || (tag.charAt(j) == ','))
				{
					j++;
				}
				i=j;
			}
			else if((tag.charAt(i) == ',') && (i == 0))
			{
				while((tag.charAt(j) == ' ') || (tag.charAt(j) == ','))
				{
					j++;
				}
				i=j;
			}
			else if(i == (tag.length()-1))
			{
				str[k] = tag.substring(j,i+1);
				k++;
			}
			else if(tag.charAt(i) == ',')
			{
					str[k] = tag.substring(j,i);
					k++;
				
				j=i+1;
				
				if((tag.charAt(j) == ' ') || (tag.charAt(j) == ','))
				{
					while((tag.charAt(j) == ' ') || (tag.charAt(j) == ','))
					{
						j++;
					}
				}
				i=j;
			}
			else if(tag.charAt(i) == ' ')
			{
					str[k] = tag.substring(j,i);
					k++;
				
				j=i+1;
				
				if((tag.charAt(j) == ' ') || (tag.charAt(j) == ','))
				{
					while((tag.charAt(j) == ' ') || (tag.charAt(j) == ','))
					{
						j++;
						
					}
				}
				i=j;
			}
		}
		
		String []str1 = new String[k];
		
		for(int i=0; i<str1.length; i++)
		{
			str1[i] = str[i];
		}
		
		search_Photos(str1);
	}

    
    void search_Photos(String[] str)
    {
    	flag=1;
    	search_flag=1;
    	PhotoList1.clear();
    	
    	for(int i=0; i<PhotoList.size(); i++)
    	{
    		System.out.println("....");
    		String str5 = PhotoList.get(i);
    		System.out.println("I: "+i+" Path: "+str5);
    		System.out.println("....");
    		for(int j=0; j<str.length; j++)
    		{
    			if(str5.contains(str[j]))
    			{
    				System.out.println(str5+" contain "+str[j]);
    				PhotoList1.add(str5);
    				System.out.println("Added to new list - "+str5);
    				
    			}
    			
    		}
    	}    	
    	
    	Cursor mCursor = db.getAndPhotoTag(str);
    	while(mCursor.moveToNext())
    	{
    		String str5 = mCursor.getString(0);
    		if(!(PhotoList1.contains(str5)))
    		{
    			PhotoList1.add(str5);
    			System.out.println("Getting added to the list1 - "+str5);
    		}
    	}
    	
    	mCursor.close();
    	
    	noOfPhotos = (TextView)findViewById(R.id.noOfPhotos);
    	if(search_flag == 1)
    		noOfPhotos.setText("Add Photos["+PhotoList1.size()+"]");
    	else
    		noOfPhotos.setText("Add Photos["+PhotoList.size()+"]");
    	
    	GridView sdcardImages = (GridView) findViewById(R.id.sdcard);
        sdcardImages.setAdapter(new ImageAdapter(this));
    }
    
    void all_photos()
    {
    	flag=0;
    	try
        {
        	 String[] projection1 = {MediaStore.Images.Media.DATA};
             cursor = managedQuery( MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                     projection1, 
                     null,       
                     null,
                     MediaStore.Images.Thumbnails._ID);
        	for(int position=0; position<cursor.getCount(); position++)
        	{
        		columnIndex = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        		cursor.moveToPosition(position);
        		PhotoList.add(cursor.getString(columnIndex));
        		
        	}
        	cursor.close();
        
        noOfPhotos = (TextView)findViewById(R.id.noOfPhotos);
        noOfPhotos.setText("Edit Photos["+PhotoList.size()+"]");
        
        GridView sdcardImages = (GridView) findViewById(R.id.sdcard);
        sdcardImages.setAdapter(new ImageAdapter(this));
        
        }
    	catch(Exception e)
    	{
    		Toast toast = Toast.makeText(addPhotosOpenAlbum.this, 
            		"Count: "+total+"\nProblem in creating photos list....",
            		Toast.LENGTH_LONG);
            toast.show();
    	}
    }

    private class ImageAdapter extends BaseAdapter {

        private Context context;

        public ImageAdapter(Context localContext) {
            context = localContext;
        }

        public int getCount() {
        	if(search_flag == 1)
        		return PhotoList1.size();
        	else
        		return PhotoList.size();
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
                v = li.inflate(R.layout.add_photos_open_album_row, null);
                  
                final String image;
                
                if(search_flag == 1)
                	image = PhotoList1.get(position);//imagePath1[position];
                else
                	image = PhotoList.get(position);
                
                ImageView iv = (ImageView)v.findViewById(R.id.icon_image1);
                Bitmap bMap = BitmapFactory.decodeFile(image);
                iv.setImageBitmap(bMap);

                iv.setOnClickListener(new Button.OnClickListener() 
    			{ public void onClick (View v)
    				{
    				
    				ss.setSessionSelectedPhoto(image);
        			
    				Intent i = new Intent(addPhotosOpenAlbum.this, FullPhotoTag.class);
    				startActivity(i);
    				
    				}
    			});
                
                final CheckBox check1 = (CheckBox)v.findViewById(R.id.check1);
                
                String date_time="";
                String place="";
                
                if((db.checkIfPhotoExist(image)) != 0)
                {	
                	Cursor c = db.getPhoto(db.getPhotoId(image));
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
                	
                	if((!(date_time.equals(""))) && (!(place.equals(""))))
                		check1.setText("Details:\nDate: "+date_time+"\nPlace: "+place);
                	else if((date_time.equals("")) && (!(place.equals(""))))
                		check1.setText("Details:\nPlace: "+place);
                	else if((!(date_time.equals(""))) && (place.equals("")))
                		check1.setText("Details:\nDate: "+date_time);
                	else
                		check1.setText("Details:\nImage: "+image);
                }
                else
                {
                	check1.setText("Details:\nImage: "+image);
                }
                check1.setId(position);
                
                check1.setOnCheckedChangeListener(new OnCheckedChangeListener()
                {
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked)
                    {
                    	String image1;
                    	if(search_flag == 1)
                    		image1 = PhotoList1.get(check1.getId());
                    	else 
                    		image1 = PhotoList.get(check1.getId());
                    	
                        if ( isChecked )
                        {
                                              	
                        	Calendar calendar = Calendar.getInstance();
                        	java.util.Date now = calendar.getTime();
                       	  	java.sql.Timestamp ti = new java.sql.Timestamp(now.getTime());

                       	 	if((db.checkIfPhotoExist(image1)) == 0)
                       	 		db.insertPhoto(image1, "60MB",ti.toString(), "","","", "", "","", "");
                        	
                       	 	int j = db.getPhotoId(image1);
                        	k = db.getAlbumId(ss.getSessionAlbumName());
                        	
                        	if((db.checkPhotoExistInAlbum(k, j)) == 0)
                        		db.insertAlbum_photo(k, j);
							                        	
                        }
                        else
                        {
                        	
                        	int j = db.getPhotoId(image1);
                        	k = db.getAlbumId(ss.getSessionAlbumName());
                        	
                        	if((db.checkPhotoExistInAlbum(k, j)) == 0)
                        		db.deletePhotoFromAlbum(j, k);
							                        	
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
