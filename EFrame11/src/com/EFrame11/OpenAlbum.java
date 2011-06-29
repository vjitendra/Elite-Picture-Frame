package com.EFrame11;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.Gallery;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

public class OpenAlbum extends Activity{
	
	String selectedAlbumName;
	String photosInSelectedAlbum[];
	DBAdapter db = new DBAdapter(this);
	Session ss = new Session();
	Button viewDetails,edit_album;

	/** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
     
        setContentView(R.layout.photos_in_album);
        
        db.open();
        selectedAlbumName = ss.getSessionAlbumName();
        
        photosInSelectedAlbum = db.getphotoLocationOfAlbum(selectedAlbumName);
        
        Button back = (Button)findViewById(R.id.back);
        back.setOnClickListener(new Button.OnClickListener() 
		{ public void onClick (View v)
			{
				db.close();
				Intent i = new Intent(OpenAlbum.this, ModeListView.class);
				startActivity(i);
			}
		});
        
        edit_album = (Button)findViewById(R.id.edit_album);
        edit_album.setOnClickListener(new Button.OnClickListener() 
		{ public void onClick (View v)
			{
			db.close();
			ss.setSessionAlbumName(selectedAlbumName);
			Intent i = new Intent(OpenAlbum.this, EditAlbum.class);
			startActivity(i);
			}
			});
                        
        Button deletePhotos = (Button)findViewById(R.id.deletePhotos);
        deletePhotos.setOnClickListener(new Button.OnClickListener() 
		{ public void onClick (View v)
			{
				int noPhotos = db.getnoOfPhotos(selectedAlbumName);
				if(noPhotos > 0)
				{
					ss.setSessionAlbumName(selectedAlbumName);
			
					db.close();
					Intent i = new Intent(OpenAlbum.this, DeletePhotosFromAlbum.class);
					startActivity(i);
				}
				else
				{
					final Dialog dialog = new Dialog(OpenAlbum.this);
					dialog.setContentView(R.layout.no_slideshow_dialog);
					dialog.setTitle("         			Alert!");
					dialog.setCancelable(true);
					TextView slideshowDialog = (TextView) dialog.findViewById(R.id.slideshowDialog);
					slideshowDialog.setText("No Photos to Delete!!");
					
					Button ok = (Button) dialog.findViewById(R.id.ok);
					 ok.setOnClickListener(new OnClickListener() {

	                 public void onClick(View v) {
	                         dialog.dismiss();
	                     }
	                 });
	                 dialog.show();
				}
			}
		});
        
        Button addPhotos = (Button)findViewById(R.id.addPhotos);
        addPhotos.setOnClickListener(new Button.OnClickListener() 
		{ public void onClick (View v)
			{
				ss.setSessionAlbumName(selectedAlbumName);
			
				db.close();
				Intent i = new Intent(OpenAlbum.this, addPhotosOpenAlbum.class);
				startActivity(i);
			
			}
		});
        
        Button slideshow_button = (Button)findViewById(R.id.slideshow_button);
        slideshow_button.setOnClickListener(new Button.OnClickListener() 
		{ public void onClick (View v)
			{
			
				int noPhotos = db.getnoOfPhotos(selectedAlbumName);
				if(noPhotos > 0)
				{
					ss.setSessionAlbumName(selectedAlbumName);
			
					db.close();
					Intent i = new Intent(OpenAlbum.this, SlideShow.class);
					startActivity(i);
				}
				else
				{
					final Dialog dialog = new Dialog(OpenAlbum.this);
					dialog.setContentView(R.layout.no_slideshow_dialog);
					dialog.setTitle("         			Alert!");
					dialog.setCancelable(true);
					TextView slideshowDialog = (TextView) dialog.findViewById(R.id.slideshowDialog);
					slideshowDialog.setText("No Photos for Slide Show!!");
					
					Button ok = (Button) dialog.findViewById(R.id.ok);
					 ok.setOnClickListener(new OnClickListener() {

	                 public void onClick(View v) {
	                         dialog.dismiss();
	                     }
	                 });
	                 dialog.show();
				}
			}
		});
        
        TextView album_name=(TextView)findViewById(R.id.album_name);
	    album_name.setText(selectedAlbumName);
	    
		Gallery gallery = (Gallery) findViewById(R.id.photogallery);
        gallery.setAdapter(new AddImgAdp(OpenAlbum.this, selectedAlbumName));

                  
        gallery.setOnItemClickListener(new OnItemClickListener() {
           public void onItemClick(AdapterView parent, View v, int position, long id) {
           	
        	   ss.setSessionSelectedPhoto(photosInSelectedAlbum[position]);
        	   db.close();
        	   Intent i = new Intent(OpenAlbum.this, FullPhoto.class);
				startActivity(i);
          
           }
       });
    }
    public class AddImgAdp extends BaseAdapter {
        int GalItemBg;
        private Context cont;

        public AddImgAdp(Context c, String selectedAlbumName) {
            cont = c;
            TypedArray typArray = obtainStyledAttributes(R.styleable.GalleryTheme);
            GalItemBg = typArray.getResourceId(R.styleable.GalleryTheme_android_galleryItemBackground, 0);
            typArray.recycle();
        }

        public int getCount() {
            return db.getnoOfPhotos(selectedAlbumName);
        }

        public Object getItem(int position) {
            return position;
        }

        public long getItemId(int position) {
            return position;
        }

        public View getView(int position, View convertView, ViewGroup parent) {
            ImageView imgView = new ImageView(cont);

            Bitmap bMap = BitmapFactory.decodeFile(photosInSelectedAlbum[position]);
            if(bMap!=null)		
              	imgView.setImageBitmap(bMap);
            else
               	imgView.setImageResource(R.drawable.moved_photo);
            
            imgView.setLayoutParams(new Gallery.LayoutParams(220, 290));
            imgView.setScaleType(ImageView.ScaleType.FIT_XY);
            imgView.setBackgroundResource(GalItemBg);

            return imgView;
        }
    }
}
