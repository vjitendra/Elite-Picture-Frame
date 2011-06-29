package com.EFrame11;


import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class SendMail extends Activity {
	
	
	EditText mailid,pwd, mailTo;
	Session ss = new Session();
	String selectedPhoto="";
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
                
        selectedPhoto = ss.getSessionSelectedPhoto();
        
        final Dialog dialog3 = new Dialog(SendMail.this);
		dialog3.setContentView(R.layout.send_mail);
		dialog3.setCancelable(true);
		
		Button back = (Button)dialog3.findViewById(R.id.back);
        back.setOnClickListener(new Button.OnClickListener() 
		{ public void onClick (View v)
			{
				dialog3.dismiss();
				Intent i = new Intent(SendMail.this, FullPhoto.class);
				startActivity(i);
			}
		});
                
        Button send = (Button)dialog3.findViewById(R.id.send);
        send.setOnClickListener(new Button.OnClickListener() 
		{ public void onClick (View v)
			{
			
			EditText mailid = (EditText)dialog3.findViewById(R.id.mailid);
			EditText pwd = (EditText)dialog3.findViewById(R.id.pwd);
			EditText mailTo = (EditText)dialog3.findViewById(R.id.mailTo);
			
			Mail m = new Mail(mailid.getText().toString(), pwd.getText().toString()); 
			 // 
		      String[] toArr = {mailTo.getText().toString()}; 
		      m.setTo(toArr); 
		      m.setFrom(mailid.getText().toString()); 
		      m.setSubject("Photo sent from Elite PictureFrame"); 
		      m.setBody("Hello,"+"\n"+
		    		  "\t\t This Photo is sent from Elite PictureFrame by your Friend."+
		    		  "\n\n"+
		    		  "Regards,"+"\n"+
		    		  "   NehaC"); 
		 
		      try { 
		        m.addAttachment(selectedPhoto); 
		 
		        if(m.send()) { 
		          Toast.makeText(SendMail.this, "Email was sent successfully.", Toast.LENGTH_LONG).show();
		          
		          Intent i = new Intent(SendMail.this, FullPhoto.class);
				  startActivity(i);
		          
		        } else { 
		          Toast.makeText(SendMail.this, "Email was not sent.", Toast.LENGTH_LONG).show(); 
		        } 
		      } catch(Exception e) { 
		         
		        Log.e("MailApp", "Could not send email", e); 
		      } 
		    } 
		});
        dialog3.show();
        
       
        
    }
}