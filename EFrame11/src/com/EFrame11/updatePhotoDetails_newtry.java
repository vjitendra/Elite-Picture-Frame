package com.EFrame11;

import java.io.File;
import java.util.ArrayList;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

public class updatePhotoDetails_newtry extends Activity{
	
	DBAdapter db = new DBAdapter(this); 
	Button back, search;
	EditText search_item;
	TextView noOfPhotos;
	int flag=0;
	String tag="";
	Cursor cursor;
	private int columnIndex;
	String imagePath1[];
	int total;
	Spinner s;
	int ItemSelected;
	Session ss = new Session();
	String selectedPhotoName="";
	String []str = new String[10];
	ImageView full;
	int j=0,k=0;
	ArrayList<String> PhotoList = new ArrayList<String>();
	
	/** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        setContentView(R.layout.update_all_photos);
        
        db.open();
                
        getNames("/sdcard/DCIM/Camera");
       
        back = (Button)findViewById(R.id.back);
        back.setOnClickListener(new Button.OnClickListener() 
    			{ public void onClick (View v)
    				{
    					Intent i = new Intent(updatePhotoDetails_newtry.this, home.class);
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
    					{
    						getNames("/sdacrd/DCIM");
    				        listNer();
    					}
    				}
    			});
     }
    
    void listNer()
    {
    	noOfPhotos = (TextView)findViewById(R.id.noOfPhotos);
    	noOfPhotos.setText("Edit Photos["+PhotoList.size()+"]");
    	GridView sdcardImages = (GridView) findViewById(R.id.sdcard);
        sdcardImages.setAdapter(new ImageAdapter(this));
        
    	sdcardImages.setOnItemClickListener(new OnItemClickListener() {
            public void onItemClick(AdapterView parent, View v, int position, long id) 
            {
            	selectedPhotoName = PhotoList.get(position);
            	ss.setSessionSelectedPhoto(selectedPhotoName);
    			            	            	
				Intent i = new Intent(updatePhotoDetails_newtry.this, FullPhotoEdit.class);
				startActivity(i);
            	
            }	
        });
    }
    
    void ConvertStringToArray(String tag)
	{
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

    
    void search_Photos(String []str1)
    {
    	flag=1;
    	    		
    	Cursor mCursor = db.getAndPhotoTag(str1);
    	imagePath1 = new String[mCursor.getCount()];
    	int i=0;
    	while(mCursor.moveToNext())
    	{
    		imagePath1[i] = mCursor.getString(0);
    		i++;
    	}
    	        
        listNer();
    }
    
    void getNames(String nm)
    {
    	File dir = new File(nm); 
       	
        String[] children = dir.list();
        if (children == null) {
        
        } 
        else 
        {
        	 for (int i=0; i<children.length; i++) 
             {
        		 String filename = children[i];
        		 
        		 int dotPos = filename.lastIndexOf(".");
             	if(dotPos>0)
             	{
             		String extension = filename.substring(dotPos);
                                   
               		
                     if((extension.equals(".bmp")) || 
                   		  (extension.equalsIgnoreCase(".gig")) || (extension.equalsIgnoreCase(".pxr")) ||
                   		  (extension.equalsIgnoreCase(".dcm")) || (extension.equalsIgnoreCase(".png")) ||
                   		  (extension.equalsIgnoreCase(".eps")) || (extension.equalsIgnoreCase(".tga")) ||
                   		  (extension.equalsIgnoreCase(".jpg")) || (extension.equalsIgnoreCase(".tiff")) ||
                   		  (extension.equalsIgnoreCase(".jpeg")) || (extension.equalsIgnoreCase(".pspimage")) ||
                   		  (extension.equalsIgnoreCase(".psb")) || (extension.equalsIgnoreCase(".thm")) ||
                   		  (extension.equalsIgnoreCase(".pcx")) || (extension.equalsIgnoreCase(".yuv")) ||
                   		  (extension.equalsIgnoreCase(".raw")) || (extension.equalsIgnoreCase(".psd")) ||
                   		  (extension.equalsIgnoreCase(".pst")) || (extension.equalsIgnoreCase(".ai")) ||
                   		  (extension.equalsIgnoreCase(".drw")) || (extension.equalsIgnoreCase(".ps")) ||
                   		  (extension.equalsIgnoreCase(".svg")))
                     	{
                    	 	PhotoList.add(nm+"/"+filename);
                    	 	System.out.println("From Camera... File: "+filename);
                     	}       		 
        		 
             	}
        		
             }
        }
        
        String []temp = db.getAllPhotosFromDB();
        for(int i=0; i<temp.length; i++)
        {
        	if(!(PhotoList.contains(temp[i])))
        	{
        		PhotoList.add(temp[i]);
        		System.out.println("From Table.. File: "+temp[i]);
        	}
        	else
        	{
        		System.out.println("Already has... File: "+temp[i]);
        	}
        }
         
        
        System.out.println("No. of Photos: "+PhotoList.size());
        for(int i=0; i<PhotoList.size(); i++)
        {
        	System.out.println("Image"+i+": "+PhotoList.get(i));
        }
        
        listNer();
    }    
    
    void all_photos()
    {
    	flag=0;
    	String[] projection = {MediaStore.Images.Thumbnails._ID};

        cursor = managedQuery( MediaStore.Images.Thumbnails.EXTERNAL_CONTENT_URI,
                projection, 
                null,       
                null,
                MediaStore.Images.Thumbnails.IMAGE_ID);
        
        columnIndex = cursor.getColumnIndexOrThrow(MediaStore.Images.Thumbnails._ID);

        total = cursor.getCount();
        
        imagePath1 = new String[total];
        
        for(int position=0; position<total; position++)
        {
        String[] projection1 = {MediaStore.Images.Media.DATA};
        cursor = managedQuery( MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                projection1, 
                null,       
                null,
                null);
        columnIndex = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToPosition(position);
        imagePath1[position] = cursor.getString(columnIndex);
        }
        
        listNer();
    }
    
     
    private class ImageAdapter extends BaseAdapter {

        private Context context;

        public ImageAdapter(Context localContext) {
            context = localContext;
        }

        public int getCount() {
        	
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
                v = li.inflate(R.layout.update_all_photos_row, null);
                
                String image = PhotoList.get(position);
                
                String date_time1="";
                String place1="";
                
                TextView photo_details = (TextView)v.findViewById(R.id.photo_details);
                
                if((db.checkIfPhotoExist(image)) != 0)
                {	
                	Cursor c = db.getPhoto(db.getPhotoId(image));
                	if (c.moveToFirst())  
                	{
                		if(!(c.getString(3).equals("")))
                			date_time1=c.getString(3);
                		                		
                		if(!(c.getString(8).equals("")))
                			place1=c.getString(8);
                		
                		if(!(c.getString(7).equals("")))
                		{
                			if(!(place1.equals("")))
                				place1=place1 + ","+c.getString(7);
                			else
                				place1=c.getString(7);
                		}
                		
                		if(!(c.getString(6).equals("")))
                		{
                			if(!(place1.equals("")))
                				place1=place1 + ","+c.getString(6);
                			else
                				place1=c.getString(6);
                		}
                		
                		if(!(c.getString(5).equals("")))
                		{
                			if(!(place1.equals("")))
                				place1=place1 + ","+c.getString(5);
                			else
                				place1=c.getString(5);
                		}
                		
                		if(!(c.getString(4).equals("")))
                		{
                			if(!(place1.equals("")))
                				place1=place1 + ","+c.getString(4);
                			else
                				place1=c.getString(4);
                		}
                	}
                	if((!(date_time1.equals(""))) && (!(place1.equals(""))))
                		photo_details.setText("Details:\nDate: "+date_time1+"\nPlace: "+place1);
                	else if((date_time1.equals("")) && (!(place1.equals(""))))
                		photo_details.setText("Details:\nPlace: "+place1);
                	else if((!(date_time1.equals(""))) && (place1.equals("")))
                		photo_details.setText("Details:\nDate: "+date_time1);
                	else
                		photo_details.setText("Details:\nImage: "+image);
                }
                else
                {
                	photo_details.setText("Details:\nImage: "+image);
                }
                
                ImageView iv = (ImageView)v.findViewById(R.id.icon_image1);
               
                return v;
        }
        
    }

}
