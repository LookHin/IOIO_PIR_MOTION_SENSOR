package com.LookHin.ioio_pir_motion_sensor;

import ioio.lib.api.AnalogInput;
import ioio.lib.api.DigitalOutput;
import ioio.lib.api.exception.ConnectionLostException;
import ioio.lib.util.BaseIOIOLooper;
import ioio.lib.util.IOIOLooper;
import ioio.lib.util.android.IOIOActivity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

/*
 * OUT = PIN 45
 * VCC = PIN +5V
 * GND = PIN GND
 */

public class MainActivity extends IOIOActivity {
	
	private ToggleButton toggleButton1;
	private TextView textView1;

	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        textView1 = (TextView) findViewById(R.id.textView1);
        toggleButton1 = (ToggleButton) findViewById(R.id.toggleButton1);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
    
    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        switch (item.getItemId())
        {
        case R.id.action_about:
        	//Toast.makeText(getApplicationContext(), "Show About", Toast.LENGTH_SHORT).show();
        	
        	Intent about = new Intent(this, AboutActivity.class);
    		startActivity(about);
    		
            return true;
        default:
            return super.onOptionsItemSelected(item);
        }
    } 
    
    
    class Looper extends BaseIOIOLooper {
		
    	private DigitalOutput digital_led0;
    	private AnalogInput deigital_input;
    	
    	private float InputStatus;
    	
		@Override
		protected void setup() throws ConnectionLostException {
			
			digital_led0 = ioio_.openDigitalOutput(0,true);
			deigital_input = ioio_.openAnalogInput(45);
							
			runOnUiThread(new Runnable() {
				public void run() {
					Toast.makeText(getApplicationContext(), "IOIO Connect", Toast.LENGTH_SHORT).show();
				}
			});
			
		}

		@Override
		public void loop() throws ConnectionLostException {
			
			try{
				digital_led0.write(!toggleButton1.isChecked());

				InputStatus = deigital_input.getVoltage();
				
				runOnUiThread(new Runnable() {
					@Override
					public void run() {
						
						textView1.setText(String.format("%.02f",InputStatus)+" v.");
						
						if(InputStatus >= 3.0){
							textView1.setBackgroundColor(Color.RED);
						}else{
							textView1.setBackgroundColor(Color.TRANSPARENT);
						}
						
					}
				});
				
				Thread.sleep(100);
	
			}catch(InterruptedException e){
				e.printStackTrace();
			}
			
		}
	}
    

	@Override
	protected IOIOLooper createIOIOLooper() {
		return new Looper();
	}
	    
}
