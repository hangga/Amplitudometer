package com.hangga.amplitudometer;

import java.io.IOException;
import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.Timer;
import java.util.TimerTask;

import android.media.MediaRecorder;
import android.os.Bundle;
import android.app.Activity;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class RecordAudioActivity extends Activity {

	RelativeLayout relativeMeter;
	
	MediaRecorder recorder;
	Timer timer;
	
	TextView risultato;
	Button btnStart;
	Button btnStop;
	
	String filePath = null;
	
	int yAmplitudo; 
	
	private void initMediaRecord(){
		recorder = new MediaRecorder();
	    recorder.setAudioSource(MediaRecorder.AudioSource.MIC);
	    recorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);
	    recorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
	    //recorder.setOutputFile("/dev/null"); 
	    filePath = "/sdcard/"+getRandomString()+".m4a";
	    recorder.setOutputFile(filePath);
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		relativeMeter = (RelativeLayout) findViewById(R.id.relativeMeter);
		
		btnStart = (Button) findViewById(R.id.btnStart);
		btnStop = (Button) findViewById(R.id.btnStop);
		
		/*initMediaRecord();
	    
	    try {
			recorder.prepare();
		} catch (IllegalStateException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	    */
	    
	    btnStart.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				//if (recorder == null){
					initMediaRecord();
				//}
				try {
					recorder.prepare();
				} catch (IllegalStateException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				recorder.start();
				timer = new Timer();
				timer.scheduleAtFixedRate(new RecorderTask(recorder), 0, 300);
				btnStart.setEnabled(false);
				btnStop.setEnabled(true);
			}
		});
	    
	    btnStop.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				goStop();
			}
		});
	    
	}
	
	private void goStop(){
		recorder.stop();
		recorder.reset();
		recorder.release();
		recorder = null;
		timer.cancel();
		timer.purge();
		btnStart.setEnabled(true);
		btnStop.setEnabled(false);
		Toast.makeText(getApplicationContext(), "Saved : "+filePath, Toast.LENGTH_SHORT).show();
		//finish();
	}
	
	private class RecorderTask extends TimerTask{
	    private MediaRecorder recorder;
	    public RecorderTask(MediaRecorder recorder){
	        this.recorder = recorder;
	    }
	    public void run(){
	        runOnUiThread(new Runnable() {
	            @Override
	            public void run() {
	            	yAmplitudo = recorder.getMaxAmplitude()/ 100;
	                RelativeLayout.LayoutParams param = (RelativeLayout.LayoutParams) relativeMeter.getLayoutParams();
	                param.height = yAmplitudo;
	                relativeMeter.setLayoutParams(param);
	            }
	        });
	    }
	}
	
	public String getRandomString() {
        SecureRandom random = new SecureRandom();
        String randomString = new BigInteger(130, random).toString(32);
        return(randomString);
    }
}
