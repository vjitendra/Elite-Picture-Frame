package com.EFrame11;


import java.util.ArrayList;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
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
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class updatePhotoDetails extends Activity{
	
	DBAdapter db = new DBAdapter(this); 
	Button back, search;
	EditText search_item;
	TextView noOfPhotos;
	int flag=0;
	String tag="";
	Cursor cursor;
	private int columnIndex;
	int total;
	Spinner s;
	int ItemSelected;
	Session ss = new Session();
	String selectedPhotoName="";
	String []str = new String[10];
	ImageView full;
	int j=0,k=0;
	ArrayList<String> PhotoList = new ArrayList<String>();
	ArrayList<String> PhotoList1 = new ArrayList<String>();
	int search_flag=0;
	private Runnable viewLocation;
	private ProgressDialog m_ProgressDialog = null;
	
	/** Called when the activity is first created. */ 
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        setContentView(R.layout.update_all_photos);
        
        db.open();
        
        
        back = (Button)findViewById(R.id.back);
        back.setOnClickListener(new Button.OnClickListener() 
    			{ public void onClick (View v)
    				{
    					PhotoList.clear();
    					PhotoList1.clear();	
    					db.close();
    					Intent i = new Intent(updatePhotoDetails.this, home.class);
    					startActivity(i);
    				}
    			});
        
                       
        search = (Button)findViewById(R.id.search);
       // search.getBackground().setColorFilter(0xff000000, PorterDuff.Mode.MULTIPLY);
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
    
    void getLocation()
    {
    	try
    	{
    		Thread.sleep(10000);
    	}
        catch (Exception e) 
        {
        	Log.e("BACKGROUND_PROC", e.getMessage());
        }
    	runOnUiThread(returnRes);
    	
    }

    
    private Runnable returnRes = new Runnable() {

        public void run() {
            m_ProgressDialog.dismiss();
            
            
            
        }
    };

    
    void listNer()
    {
    	noOfPhotos = (TextView)findViewById(R.id.noOfPhotos);
    	
    	if(search_flag == 1)
    		noOfPhotos.setText("Edit Photos["+PhotoList1.size()+"]");
    	else
    		noOfPhotos.setText("Edit Photos["+PhotoList.size()+"]");
    	
    	GridView sdcardImages = (GridView) findViewById(R.id.sdcard);
        sdcardImages.setAdapter(new ImageAdapter(this));
        
        viewLocation = new Runnable(){
            public void run() {
                getLocation();
            }
        };
        Thread thread =  new Thread(null, viewLocation, "MagentoBackground");
        thread.start();
        m_ProgressDialog = ProgressDialog.show(updatePhotoDetails.this,    
              "Elite PictureFrame", "Searching photos on device..", true);
        
    	sdcardImages.setOnItemClickListener(new OnItemClickListener() {
            public void onItemClick(AdapterView parent, View v, int position, long id) 
            {
            	if(search_flag == 1)
            	    selectedPhotoName = PhotoList1.get(position);//imagePath1[position];
            	else
            		selectedPhotoName = PhotoList.get(position);
            	
            	ss.setSessionSelectedPhoto(selectedPhotoName);
            	
            	PhotoList.clear();
				PhotoList1.clear();
            	
    			db.close();            	
				Intent i = new Intent(updatePhotoDetails.this, FullPhotoEdit.class);
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
    	search_flag=1;
    	
    	PhotoList1.clear();
    	
    	/*System.out.println("Actual PhotoList: ");
    	for(int i=0; i<PhotoList.size(); i++)
    	{
    		System.out.println("Photo"+i+": "+PhotoList.get(i));
    	}
    	*/
    	for(int i=0; i<PhotoList.size(); i++)
    	{
    		//System.out.println("....");
    		String str5 = PhotoList.get(i);
    		/*System.out.println("I: "+i+" Path: "+str5);
    		System.out.println("....");
    		*/
    		for(int j=0; j<str1.length; j++)
    		{
    			if(str5.contains(str1[j]))
    			{
    				//System.out.println(str5+" contain "+str1[j]);
    				PhotoList1.add(str5);
    				//System.out.println("Added to new list - "+str5);
    				
    			}
    			
    		}
    	}
    	
    	/*System.out.println("PhotoList1 b4 db: ");
    	for(int i=0; i<PhotoList1.size(); i++)
    	{
    		System.out.println("Photo"+i+": "+PhotoList1.get(i));
    	}
    	*/
    	
    	Cursor mCursor = db.getAndPhotoTag(str1);
    	
    	while(mCursor.moveToNext())
    	{
    		String str5 = mCursor.getString(0);
    		
    		//System.out.println("Comparing with: "+ str5);
    		
    		if(!(PhotoList1.contains(str5)))
    		{
    			PhotoList1.add(str5);
    	//		System.out.println("Getting added to the list1 - "+str5);
    		}
    		
    	}
    	
    	mCursor.close();
    	
    	/*System.out.println("Final PhotoList: ");
    	for(int i=0; i<PhotoList1.size(); i++)
    	{
    		System.out.println("Photo"+i+": "+PhotoList1.get(i));
    	}
    	*/
    	
    	/*viewLocation = new Runnable(){
            public void run() {
                getLocation();
            }
        };
        Thread thread =  new Thread(null, viewLocation, "MagentoBackground");
        thread.start();
        m_ProgressDialog = ProgressDialog.show(updatePhotoDetails.this,    
              "Elite PictureFrame", "Searching photos....", true);
    	 */
    	
        listNer();
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
        listNer();
    	}
    	catch(Exception e)
    	{
    		Toast toast = Toast.makeText(updatePhotoDetails.this, 
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
                v = li.inflate(R.layout.update_all_photos_row, null);
                try
                {
                String image;
                
                if(search_flag == 1)
                	image = PhotoList1.get(position);//imagePath1[position];
                else
                	image = PhotoList.get(position);
                
                ImageView iv = (ImageView)v.findViewById(R.id.icon_image1);
                
                
                Bitmap bMap = BitmapFactory.decodeFile(image);
                if(bMap!=null)	               
                    iv.setImageBitmap(bMap);
                else
                	iv.setImageResource(R.drawable.icon);
                
                
                
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
                	c.close();
                	
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
                
            }
        	catch(Exception e)
        	{
        		Toast toast = Toast.makeText(updatePhotoDetails.this, 
                		"\nProblem in attaching photos....\nImage: "+position,
                		Toast.LENGTH_LONG);
                toast.show();
        	}
                
                return v;
        }
        
    }

}
