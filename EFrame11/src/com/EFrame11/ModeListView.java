package com.EFrame11;

import android.app.Activity;
import android.os.Bundle;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView.OnItemClickListener;

public class ModeListView extends Activity {
	
	int selectedAlbum;
	String selectedAlbumName;
	int []album_cover_ids;
	String []album_covers;
	String []album_names;
	DBAdapter db = new DBAdapter(this);
	Session ss = new Session();
	Button viewDetails;
	BitmapFactory.Options options;
		
	/** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
       
        setContentView(R.layout.view_album);
               
        db.open();
         
        Button home = (Button)findViewById(R.id.home);
        home.setOnClickListener(new Button.OnClickListener() 
		{ public void onClick (View v)
			{
				db.close();
				Intent i = new Intent(ModeListView.this, home.class);
				startActivity(i);
			}
		}); 
        
        Button deleteAlbum = (Button)findViewById(R.id.deleteAlbum);
        deleteAlbum.setOnClickListener(new Button.OnClickListener() 
		{ public void onClick (View v)
			{
				db.close();
				Intent i = new Intent(ModeListView.this, DeleteAlbums.class);
				startActivity(i);
			}
		}); 
        
       album_cover_ids = db.getAllAlbumCovers();
       album_names = db.getAlbumNames();
       album_covers = new String[album_names.length]; 
       
        for(int i=0; i<album_names.length; i++)
        {
        	if(album_cover_ids[i] != 0)
        	{
        		Cursor c = db.getPhoto(album_cover_ids[i]);
        		if (c.moveToFirst())
        		{
        			String temp = c.getString(1);
        			album_covers[i] = temp;
        		}
        		c.close();
        	}
        	else
        	{
        		album_covers[i] = "";
        	}
        }
      
        GridView gridview1 = (GridView) findViewById(R.id.gridview1);
        gridview1.setAdapter(new ImageAdapter(this));

        gridview1.setOnItemClickListener(new OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
               	
            	ss.setSessionAlbumName(album_names[position]);
            	db.close();
            	Intent i = new Intent(ModeListView.this, OpenAlbum.class);
                startActivity(i);
            }
        });
      
        }
        
        public class ImageAdapter extends BaseAdapter{
        	private Context mContext;
        	
            public ImageAdapter(Context c) {
                mContext = c;
            }

            public int getCount() {
                return db.getnoOfAlbums();
            }

            public Object getItem(int position) {
                return null;
            }

            public long getItemId(int position) {
                return 0;
            }

            public View getView(final int position, View convertView, ViewGroup parent) {
            	 View v;
		           
		                LayoutInflater li = getLayoutInflater();
		                v = li.inflate(R.layout.view_album_row, null);
		                TextView tv = (TextView)v.findViewById(R.id.icon_text);
		                tv.setText(album_names[position]);
		               
		                ImageView iv = (ImageView)v.findViewById(R.id.icon_image);
		                if(!(album_covers[position].equals("")))
		                {
		                
		                Bitmap bMap = BitmapFactory.decodeFile(album_covers[position]);
		                if(bMap!=null)		                
		                	iv.setImageBitmap(bMap);
		                else
		                	iv.setImageResource(R.drawable.moved_photo);
		                }
		                else
		                {
			                iv.setImageResource(R.drawable.default_a);
		                }
		         
		                iv.setOnClickListener(new Button.OnClickListener() 
		        		{ public void onClick (View v)
		        			{ 
		        			ss.setSessionAlbumName(album_names[position]);
		                	db.close();
		                	Intent i = new Intent(ModeListView.this, OpenAlbum.class);
		                    startActivity(i);
		        			}
		        		});
		            return v;

            }
        }
        
}

  
         
    	
    
    	

        
        
        
        
    
