package hdriel.matchgame;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.Spinner;

public class SettingActivity extends Activity {

	EditText et_name_A, et_name_B, et_size, et_max;
	RadioGroup rg_start, rg_vs;
	RadioButton rb_start_A, rb_start_B , rb_vs_computer, rb_vs_human;
	Button btn_save;
	Context c;
	Intent intent;
	boolean showGrid = false;
	boolean once ;
	boolean vs_computer = true;
	boolean start_A = true;
	boolean ok = false;
	String namePlayerA = "player";
	String namePlayerB = "computer";
	int size = 15;
	int max = 3;
	SharedPreferences sharedPref;
	
	final String SAVE = "save data", ONCE = "enter once start", TEMP_NAME_B = "temp name of player B",
	             VS_COMPUTER   = "save data" , 
			     START_A       = "the player start in Playing",
			     NAME_PLAYER_A = "name player A",
			     NAME_PLAYER_B = "name player B", 
			     SIZE          = "amount of matches", 
			     MAX           = "the max take";
	
	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.setting);
        
        c              = this;
        intent         = new Intent(c, MainActivity.class);
        et_name_A      = (EditText)   findViewById(R.id.et_name_playerA);
        et_name_B      = (EditText)   findViewById(R.id.et_name_playerB);
        et_size        = (EditText)   findViewById(R.id.et_size);
        et_max         = (EditText)   findViewById(R.id.et_max);
        rg_start       = (RadioGroup) findViewById(R.id.radios_start);
        rb_start_A     = (RadioButton)findViewById(R.id.rb_start_A);
        rb_start_B     = (RadioButton)findViewById(R.id.rb_start_B);
        rg_vs          = (RadioGroup) findViewById(R.id.radios_vs);
        rb_vs_computer = (RadioButton)findViewById(R.id.rb_vsComputer);
        rb_vs_human    = (RadioButton)findViewById(R.id.rb_vsHuman);
        btn_save       = (Button)     findViewById(R.id.btn_save);
        
       
        
        
        Bundle extras = getIntent().getExtras();
    	if (extras != null) {
    		once = extras.getBoolean(ONCE);
    	}
    	else{
    		once = false;
    	}
        
    	
        sharedPref = getSharedPreferences(SAVE, MODE_PRIVATE);      
        vs_computer = sharedPref.getBoolean(VS_COMPUTER, true);
        start_A     = sharedPref.getBoolean(START_A, true);
        namePlayerA = sharedPref.getString(NAME_PLAYER_A, "א");
        namePlayerB = sharedPref.getString(NAME_PLAYER_B, "מחשב");
        size        = sharedPref.getInt(SIZE, 15);
        max         = sharedPref.getInt(MAX, 3);
        
        
        et_name_A.setText(namePlayerA);
        et_name_B.setText(namePlayerB);
        et_size.setText(""+ size);
        et_max.setText("" + max);
        
        if(start_A){
        	rb_start_A.setChecked(true);
        	rb_start_A.performClick();
        }else{
        	rb_start_B.setChecked(true);
        	rb_start_B.performClick();
        }
        
        if(vs_computer){
        	rb_vs_computer.setChecked(true);
        	rb_vs_computer.performClick();
        }
        else{
        	rb_vs_human.setChecked(true);
        	rb_vs_human.performClick();
        }
        
        btn_save.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				
				start_A = rb_start_A.isChecked();
				
				if(et_name_A.getText().toString().trim().length() > 0)
					namePlayerA =  et_name_A.getText().toString();
				else
					namePlayerA = et_name_A.getHint().toString();
				
				if(et_name_B.getText().toString().trim().length() > 0)
					namePlayerB =  et_name_B.getText().toString();
				else
					namePlayerB = et_name_B.getHint().toString();
				
				if(et_size.getText().toString().trim().length() > 0)
					size = Integer.parseInt(et_size.getText().toString());
				else
					size = Integer.parseInt(et_size.getHint().toString());
				
				if(et_max.getText().toString().trim().length() > 0)
					max = Integer.parseInt(et_max.getText().toString());
				else
					max = Integer.parseInt(et_max.getHint().toString());
				
				intent.putExtra(VS_COMPUTER, vs_computer);
				intent.putExtra(START_A, start_A);
				intent.putExtra(NAME_PLAYER_A, namePlayerA);
				intent.putExtra(NAME_PLAYER_B, namePlayerB);
				intent.putExtra(SIZE, size);	
				intent.putExtra(MAX, max);		
				intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
				
				SharedPreferences sharedPref = getSharedPreferences(SAVE, MODE_PRIVATE);
	    	    SharedPreferences.Editor editor = sharedPref.edit();
	    	    editor.putBoolean(ONCE, once);
	    	    editor.putBoolean(VS_COMPUTER, vs_computer);
	    	    editor.putBoolean(START_A, start_A);
	    	    editor.putString(NAME_PLAYER_A, namePlayerA);
	    	    editor.putString(NAME_PLAYER_B, namePlayerB);
	    	    editor.putInt(SIZE, size);
	    	    editor.putInt(MAX, max);
	    	    editor.commit();
	    	    
	    	   checkData();
	    	   if(ok) 
				startActivity(intent);
			}
		});
        
        if(!once){
        	once = true;
        	
        	SharedPreferences sharedPref = getSharedPreferences(SAVE, MODE_PRIVATE);
    	    SharedPreferences.Editor editor = sharedPref.edit();
    	    editor.putBoolean(ONCE, once);
    	    editor.commit();
    	    
    	    rb_vs_computer.performClick();
    	    rb_vs_human.setChecked(false);
    	    rb_start_A.performClick();
    	    rb_start_B.setChecked(false);
    	    
        	btn_save.performClick();	
        }
        
	}// end onCreate method

	@Override
	public void onPause() {
	    super.onPause();
		/*
		start_A = rb_start_A.isChecked();
		
		if(et_name_A.getText().toString().trim().length() > 0)
			namePlayerA =  et_name_A.getText().toString();
		else
			namePlayerA = et_name_A.getHint().toString();
		
		
		//namePlayerB =  et_name_B.getText().toString();
	    et_name_B.setText(namePlayerB);
		
		
		if(et_size.getText().toString().trim().length() > 0)
			size = Integer.parseInt(et_size.getText().toString());
		else
			size = Integer.parseInt(et_size.getHint().toString());
		
		if(et_max.getText().toString().trim().length() > 0)
			max = Integer.parseInt(et_max.getText().toString());
		else
			max = Integer.parseInt(et_max.getHint().toString());
		
		intent.putExtra(VS_COMPUTER, vs_computer);
		intent.putExtra(START_A, start_A);
		intent.putExtra(NAME_PLAYER_A, namePlayerA);
		intent.putExtra(NAME_PLAYER_B, namePlayerB);
		intent.putExtra(SIZE, size);	
		intent.putExtra(MAX, max);		
		intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
		
		SharedPreferences sharedPref = getSharedPreferences(SAVE, MODE_PRIVATE);
	    SharedPreferences.Editor editor = sharedPref.edit();
	    editor.putBoolean(ONCE, once);
	    editor.putBoolean(VS_COMPUTER, vs_computer);
	    editor.putBoolean(START_A, start_A);
	    editor.putString(NAME_PLAYER_A, namePlayerA);
	    editor.putString(NAME_PLAYER_B, namePlayerB);
	    editor.putString(TEMP_NAME_B, tempB);
	    editor.putInt(SIZE, size);
	    editor.putInt(MAX, max);
	    editor.commit();
	    
	    
	    checkData();
	    */
	    // Remove the activity when its off the screen
	    finish();
	    //startActivity(intent);
	    btn_save.performClick();
	}
	
	public void onRadioButton_vs_Clicked(View v)
    {
        //is the current radio button now checked?
        boolean  checked = ((RadioButton) v).isChecked();
        sharedPref = getSharedPreferences(SAVE, MODE_PRIVATE);
	    SharedPreferences.Editor editor = sharedPref.edit();
        //now check which radio button is selected
        //android switch statement
        switch(v.getId()){

            case R.id.rb_vsComputer:
                if(checked)
                    //set the checked radio button's text style bold italic
                    rb_vs_computer.setTypeface(null, Typeface.BOLD_ITALIC);
                    //set the other two radio buttons text style to default
                    rb_vs_human.setTypeface(null, Typeface.NORMAL);
                    
                    
                    namePlayerB = "מחשב";
                    editor.putString(NAME_PLAYER_B, namePlayerB);
                    
                    et_name_B.setText(namePlayerB);
                    et_name_B.setEnabled(false);
                   
                    vs_computer = true;
            	    editor.putBoolean(VS_COMPUTER, vs_computer);
            	   
            	    
                break;

            case R.id.rb_vsHuman:
                if(checked)
                	//set the checked radio button's text style bold italic
                	rb_vs_human.setTypeface(null, Typeface.BOLD_ITALIC);
                    //set the other two radio buttons text style to default
                	rb_vs_computer.setTypeface(null, Typeface.NORMAL);
                	
                	et_name_B.setEnabled(true);
                	
                    namePlayerB = sharedPref.getString(NAME_PLAYER_B, "מחשב");
                    if(namePlayerB.equals("מחשב"))
                    	namePlayerB = "";
                    
                    et_name_B.setText(namePlayerB);
                    editor.putString(NAME_PLAYER_B, namePlayerB);
                    
                    vs_computer = false;
            	    editor.putBoolean(VS_COMPUTER, vs_computer);
                break;
        }
        editor.commit();
    }
	
	public void onRadioButton_start_Clicked(View v)
    {
        //is the current radio button now checked?
        boolean  checked = ((RadioButton) v).isChecked();
        sharedPref = getSharedPreferences(SAVE, MODE_PRIVATE);
	    SharedPreferences.Editor editor = sharedPref.edit();
        //now check which radio button is selected
        //android switch statement
        switch(v.getId()){

            case R.id.rb_start_A:
                if(checked)
                    //set the checked radio button's text style bold italic
                	rb_start_A.setTypeface(null, Typeface.BOLD_ITALIC);
                    //set the other two radio buttons text style to default
                	rb_start_B.setTypeface(null, Typeface.NORMAL);
                    
            	    editor.putBoolean(START_A, start_A);
                break;

            case R.id.rb_start_B:
                if(checked)
                	//set the checked radio button's text style bold italic
                	rb_start_B.setTypeface(null, Typeface.BOLD_ITALIC);
                    //set the other two radio buttons text style to default
                	rb_start_A.setTypeface(null, Typeface.NORMAL);
                	
                	editor.putBoolean(START_A, start_A);
                break;
        }
        editor.commit();
    }
	
	private void checkData(){
		ok = true;
		if(size <= 5){
			ok = false;
			Toast.makeText(c, "הכנס מספר גפרורים מעל 5", Toast.LENGTH_SHORT).show();
		}
		if(max >= size / 3){
			ok = false;
			Toast.makeText(c, "הכנס מספר מקסימום מתחת ל-" + (size / 3) , Toast.LENGTH_SHORT).show();
		}
		
	}
}
