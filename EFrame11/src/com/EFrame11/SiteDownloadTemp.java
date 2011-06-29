package com.EFrame11;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class SiteDownloadTemp extends Activity{


	Session ss = new Session();
	Dialog dialog;
	
	/** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.download);
        
        System.out.println("In Download temp..");
        
        int count = ss.getSessionCount();
        System.out.println("Current Count(home): "+count);
        if(count>0)
        {
           	Intent i = new Intent(SiteDownloadTemp.this, image.class);
        	startActivity(i);
        }
        else
        	display(ss.getSessionPathsCount());
    }
    void display(int c)
    {
    	dialog = new Dialog(SiteDownloadTemp.this);
		dialog.setContentView(R.layout.view_download_details_dialog);
		dialog.setCancelable(true);

		TextView viewDetailsDialog = (TextView) dialog.findViewById(R.id.viewDetailsDialog);
		
		if(c>0)
			viewDetailsDialog.setText("\n"+c+" Photo(s) downloaded from PC");
		else
			viewDetailsDialog.setText("\nNo new Photo(s) downloaded from PC");
		
		
		
		 Button ok = (Button) dialog.findViewById(R.id.ok);
		 ok.setOnClickListener(new OnClickListener() {

         public void onClick(View v) {
                 dialog.dismiss();
                 Intent i = new Intent(SiteDownloadTemp.this, InsertDBAndUpdate.class);
     			 startActivity(i);
             }
         });
         dialog.show();
		
    }
	
}
