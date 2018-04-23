package hdriel.matchgame;

import java.util.Random;

import android.support.v7.app.ActionBarActivity;
import android.text.Html;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

// CRTL + SHFT + '/' 
// Code->Folding->Collapse All

@SuppressWarnings("deprecation")
@SuppressLint("NewApi") 
public class MainActivity extends ActionBarActivity {

	TextView tv_title, tv_description, tv_size, tv_turn;
	Button btn_take;
	ImageView iv_matches_box, iv_matches[] , iv, singleMatch;
	LinearLayout ll_HorizontalScrollView, main, ll_HorizontalScrollView_text;
	ScrollView sv_description;
	HorizontalScrollView hsv;
	GridView gv;
	Context c;
	int size, max;
	int number_image[]; 
	boolean clickDescription = true;
	boolean showGrid = false;
	matchesGame mg;
	int amountChoose;
	boolean inPlaying;
	boolean turnOfPlayer;
	boolean vs_compuer;
	boolean clickedMatch[];
	String namePlayerA , namePlayerB;
	boolean doubleBackToExitPressedOnce;
	Handler mHandler = new Handler();
	CustomAdapterMatches adapter;
	final String VS_COMPUTER   = "save data" , ONCE = "enter once start", 
			     START_A       = "the player start in Playing",
			     NAME_PLAYER_A = "name player A",
			     NAME_PLAYER_B = "name player B", 
			     SIZE          = "amount of matches", 
			     MAX           = "the max take";
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        c = this;
        tv_title                = (TextView) findViewById(R.id.title);
        tv_description          = (TextView) findViewById(R.id.description);
        tv_size                 = (TextView) findViewById(R.id.size);
        tv_turn                 = (TextView) findViewById(R.id.turn);
        ll_HorizontalScrollView = (LinearLayout) findViewById(R.id.linearLayout_horizontalScrollView);
        ll_HorizontalScrollView_text = (LinearLayout) findViewById(R.id.linearLayout_horizontalScrollView_text);
        main                    = (LinearLayout) findViewById(R.id.main);
        iv_matches_box          = (ImageView)findViewById(R.id.image_box);
        btn_take                = (Button) findViewById(R.id.btn_take);
        sv_description          = (ScrollView) findViewById(R.id.scroll_description);
        hsv                     = (HorizontalScrollView) findViewById(R.id.horizontalScrollView);
        gv                      = (GridView) findViewById(R.id.gridView_matches);
        
        LayoutParams params = sv_description.getLayoutParams();  params.height = 400; sv_description.setLayoutParams(params);
        initialGame();

        iv_matches_box.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				showGrid = !showGrid;
				if(showGrid){
					gv.setVisibility(View.VISIBLE);
					hsv.setVisibility(View.GONE);
					
					adapter.changeData(mg, clickedMatch, number_image);
					gv.setAdapter(adapter);
				}
				else{
					gv.setVisibility(View.GONE);
					hsv.setVisibility(View.VISIBLE);
				}
			}
		});
        
        // צמצום ההוראות לשורה אחת, או להרחבת השורות לקריאת ההוראות
        tv_description.performClick();
        tv_description.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				LayoutParams params = sv_description.getLayoutParams();
				clickDescription = !clickDescription;
				if(clickDescription)
				{
					tv_description.setSingleLine(false);
					params.height = 400;
					sv_description.setLayoutParams(params);
					
				}
				else
				{
					tv_description.setLines(1);
					params.height = 150;
					sv_description.setLayoutParams(params);
				}
			}
		});
        
        // כפתור לחיצה ללקיחת כמות הגפרורים שהשחקנים בחרו
        btn_take.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) 
			{
				
				if(btn_take.getText() == "משחק חדש"){
					initialGame();
				}
				else
				{
					if(inPlaying)
					{
						if(amountChoose <= 0)
						{
							Toast.makeText(c, "-אתה חייב לבחור בין גפרור 1 ל-"+ mg.getMax() + " גפרורים!", Toast.LENGTH_SHORT).show();
						}
						else
						{
							//hsv.scrollTo((mg.getIndex())*50, 0);
							inPlaying = mg.play(amountChoose);
							
							if(!inPlaying)  // אם נגמר המשחק לאחר הצעד האחרון
							{
								String win = "";
								if(turnOfPlayer)
									win = "שחקן: " + namePlayerA + " ניצח!";
								else 
									win = "שחקן: " + namePlayerB + " ניצח!";
								Toast.makeText(c, win, Toast.LENGTH_SHORT).show();
								tv_turn.setText("המשחק נגמר\n " + win);
								tv_size.setText("x0");
								btn_take.setText("משחק חדש");
							}
							else
							{
								turnOfPlayer = !turnOfPlayer;
								updateBoard();
							}
						}
					}
					else // אם נגמר המשחק כבר
					{
						String win = "";
						if(turnOfPlayer)
							win = "שחקן: " + namePlayerA + " ניצח!";
						else 
							win = "שחקן: " + namePlayerB + " ניצח!";
						Toast.makeText(c, win, Toast.LENGTH_SHORT).show();
						tv_turn.setText("המשחק נגמר\n " + win);
						tv_size.setText("x0");
						btn_take.setText("משחק חדש");
					}
				}
			}
		});

        
        gv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
            	if(vs_compuer){
            		if(!turnOfPlayer)
                	{
                		ll_HorizontalScrollView.setEnabled(false);
                		gv.setEnabled(false);
                		Toast.makeText(c, "המחשב בוחר כרגע גפרורים, המתן בסבלנות לתורך!", Toast.LENGTH_SHORT).show();
                	}else{
                		
                	}
            	}
        		ll_HorizontalScrollView.setEnabled(true);
        		gv.setEnabled(true);
        		
        		if(position < mg.getIndex())
            	{
            		Toast.makeText(c, "אתה לא יכול לבחור גפרורים שרופים", Toast.LENGTH_SHORT).show();
            	}
            	else if(position - mg.getIndex() >= mg.getMax())
            	{
            		int indexTill = (mg.getIndex() + mg.getMax());
            		if(indexTill < mg.getSize())
            			Toast.makeText(c, ("אתה לא יכול לבחור מעל לגפרור מספר " + indexTill), Toast.LENGTH_SHORT).show();
            		else
            			Toast.makeText(c, ("אתה לא יכול לבחור מעל לגפרור ה-" + mg.getMax()), Toast.LENGTH_SHORT).show();
            	}
            	else
            	{
            		
            		LayoutParams params = v.getLayoutParams();
            		params.height = (int) getResources().getDimension(R.dimen.match_height);
            		v.setLayoutParams(params);
            		
        		   if(clickedMatch[position]) 
        		   {
            			clickedMatch[position] = false;
               			amountChoose--;	
               			resetClickUntilIndex();
               			
               			adapter.changeData(mg, clickedMatch, number_image);
               			gv.setAdapter(adapter);
               			//scrollGridViewToIndex(parent, (position/5)*50);  	
                   } 
            	   else 
                   {
                	   clickedMatch[position] = true;
               		   amountChoose++;
               		   resetClickUntilIndex();
               		   
	               	   adapter.changeData(mg, clickedMatch, number_image);
	           		   gv.setAdapter(adapter);
	           		   //scrollGridViewToIndex(parent, (position/5)*50);   			   
                   }
        		  
            	}
			}
		});
        
        
        
    } // end onCreate methods ***********************************************

    /*
    private void scrollGridViewToIndex(ViewGroup container, int indexView){
    	int offset = (int)(indexView * getResources().getDisplayMetrics().density); 
    	int index = gv.getFirstVisiblePosition();
    	final View first = container.getChildAt(0);
    	if (null != first) {
    	    offset -= first.getTop();
    	}

    	// Destroy the position through rotation or whatever here!
    	gv.setSelection(index);
    	gv.scrollBy(0, offset);
    }
    */
    
    private void changePlayer(int i){
    	if(i == 1){
    		tv_turn.setText("שחקן: " + namePlayerA );
        	tv_turn.setBackgroundResource(R.drawable.background_turn_player);
    	}
    	else{
    		tv_turn.setText("שחקן: " + namePlayerB );
        	tv_turn.setBackgroundResource(R.drawable.background_turn_computer);
    	}
    	
    }
    
    private void resetClickUntilIndex(){
    	for(int i = 0 ; i < mg.getIndex(); i++){
    		clickedMatch[i] = false;
    	}
    }
    
    // עדכון לוח המשחק
    private void updateBoard(){
    	
    	tv_size.setText("x"+(size - (mg.getIndex())));
    	amountChoose = 0;
    	resetClickUntilIndex();
    	
    	adapter.changeData(mg, clickedMatch, number_image);
    	gv.setAdapter(adapter);
    	//scrollGridViewToIndex((ViewGroup)gv.getParent(), (mg.getIndex()/5)*50);   	
    	Random rand = new Random();
    	for(int i = 0; i < size; i++)
    	{
    		if(mg.getStateMatchesByIndex(i) == false)
    		{
				if(number_image[i] == 0)
				{
					number_image[i] = rand.nextInt(3) + 1;
				}
				
				if(!mg.getStateMatchesByIndex(i))
				{
					switch(number_image[i]){
					case 1:
						iv_matches[i].setImageResource(R.drawable.m_l1);
						break;
					case 2:
						iv_matches[i].setImageResource(R.drawable.m_l2);
						break;
					case 3:
						iv_matches[i].setImageResource(R.drawable.m_l3);
						break;
					default: 
						iv_matches[i].setImageResource(R.drawable.m_l1);
					}
    			
    			
					iv_matches[i].setBackgroundResource(0);
    		    }
    		}
    		else
    		{
    			if(number_image[i] == 0)
    			{
					number_image[i] = rand.nextInt(3) + 1;
				}
				
    			switch(number_image[i])
    			{
				case 1:
					iv_matches[i].setImageResource(R.drawable.m_s1);
					break;
				case 2:
					iv_matches[i].setImageResource(R.drawable.m_s2);
					break;
				case 3:
					iv_matches[i].setImageResource(R.drawable.m_s3);
					break;
				default: 
					iv_matches[i].setImageResource(R.drawable.m_s4);
				}
    			iv_matches[i].setBackgroundResource(0);
    		}
    		
        }
    	
    	if(!turnOfPlayer)
    	{
    		
    		if(!inPlaying) {  btn_take.setText("משחק חדש"); return;}
    		
    		changePlayer(2);
    		
    		if(vs_compuer)
    		{
    			// כדי לעקב את התור של המחשב
        		Handler handler = new Handler(); 
        	    handler.postDelayed(new Runnable() 
        	    {
        	         @Override 
        	         public void run() 
        	         { 
        	        	 inPlaying = mg.play(amountChoose);
        	        	 
        	        	 if(!inPlaying)
        	        	 {
        	        		 String win = "";
     						if(turnOfPlayer)
     							win = "שחקן: " + namePlayerA + " ניצח!";
     						else 
     							win = "שחקן: " + namePlayerB + " ניצח!";
     						Toast.makeText(c, win, Toast.LENGTH_SHORT).show();
     						tv_turn.setText("המשחק נגמר\n " + win);
     						tv_size.setText("x0");
     						btn_take.setText("משחק חדש");
    						updateBoard();
        	        	 }
        	        	 else
        	        	 { 
        	        		 turnOfPlayer = !turnOfPlayer;
        	        		 updateBoard();
        	        		 changePlayer(1);
        	        	 }
        	         } 
        	    }, 1500); // עיקוב של שניה וחצי
    		}
    	}
    	else{
			changePlayer(1);
		}
    	
    }
    
    // אתחול לוח המשחק
    private void initialGame(){
    	//hsv.scrollTo(0, 0);
    	btn_take.setText("קח גפרורים");
    	
    	Bundle extras = getIntent().getExtras();
    	if (extras != null) {
    		//The key argument here must match that used in the other activity
    		size = extras.getInt(SIZE);
    		max  = extras.getInt(MAX);
    		namePlayerA = extras.getString(NAME_PLAYER_A);
    		namePlayerB = extras.getString(NAME_PLAYER_B);
	    	turnOfPlayer = extras.getBoolean(START_A);
	    	vs_compuer   = extras.getBoolean(VS_COMPUTER);
	    	inPlaying = true;
	    	amountChoose = 0;
	    	mg = new matchesGame(size ,max,  vs_compuer, turnOfPlayer);
	    	if(turnOfPlayer)
	    		changePlayer(1);
	    	else 
	    		changePlayer(2);
	    	
	    	ll_HorizontalScrollView.removeAllViews();
	    	ll_HorizontalScrollView_text.removeAllViews();
	    	
	    	// create new matches to game
	    	iv_matches = new ImageView[size];
	    	clickedMatch = new boolean[size];
	        for(int i = 0; i < size; i++){
	        	clickedMatch[i] = false;
	        }
	        number_image = new int[size];
	    	for(int i = 0; i < size; i++){
	    		number_image[i] = 0;
	    	}
	        
	    	// initial the state matches
	        for(int i = 0; i < size; i++)
	        {
	        	// declare new imageview
	        	iv_matches[i] = new ImageView(c);
	        	// set image to matche
	        	Random rand = new Random();
	        	switch(rand.nextInt(3)){
				case 1:
					iv_matches[i].setImageResource(R.drawable.m_l1);
					break;
				case 2:
					iv_matches[i].setImageResource(R.drawable.m_l2);
					break;
				default: 
					iv_matches[i].setImageResource(R.drawable.m_l3);
				}
	        	
	        	// set position on the image
	        	iv_matches[i].setLayoutParams(new LayoutParams(100, 500));
	        	// add the matche to the horizontal scroll view
	        	
	        	ll_HorizontalScrollView.addView(iv_matches[i]);
	        	TextView txt = new TextView(c);
	        	txt.setText(" " + (i + 1) + " ");
	        	txt.setTextSize(15);
	        	txt.setGravity(Gravity.CENTER);
	        	//if(size < ) txt.setTextSize(25);
	        	//else if() txt.setTextSize(25);
	        	//else txt.setTextSize(25);
	        	txt.setLayoutParams(new LayoutParams(100, 100));
	        	ll_HorizontalScrollView_text.addView(txt);
	        	
	        	// set click to the current image clicked
	        	final int indexImage = i;
	        	
	        	iv_matches[i].setOnClickListener(new View.OnClickListener() {
	                public void onClick(View v) {
	                	if(vs_compuer){
	                		if(!turnOfPlayer)
		                	{
		                		ll_HorizontalScrollView.setEnabled(false);
		                		Toast.makeText(c, "המחשב בוחר כרגע גפרורים, המתן בסבלנות לתורך!", Toast.LENGTH_SHORT).show();
		                	}else{
		                	}
	                	}
                		ll_HorizontalScrollView.setEnabled(true);
                		
                		if(indexImage < mg.getIndex())
                    	{
                    		Toast.makeText(c, "אתה לא יכול לבחור גפרורים שרופים", Toast.LENGTH_SHORT).show();
                    	}
                    	else if(indexImage - mg.getIndex() >= mg.getMax())
                    	{
                    		Toast.makeText(c, ("אתה לא יכול לבחור מעל לגפרור ה" + mg.getMax()), Toast.LENGTH_SHORT).show();
                    	}
                    	else
                    	{
                    		if(clickedMatch[indexImage]) 
                    		{
                    			clickedMatch[indexImage] = false;
                    			v.setBackgroundResource(0);
                       			amountChoose--;	
                       			
                           } 
                    		else 
                           {
                        	   clickedMatch[indexImage] = true;
                        	   v.setBackgroundResource(R.drawable.m2);
                       		   amountChoose++;
                           }

                    	}
                	
	                }
	        	});
	        	
	        	iv_matches[i].setOnLongClickListener(new OnLongClickListener() {
					@Override
					public boolean onLongClick(View v) {
						Toast.makeText(c, "גפרור מספר: " + (indexImage + 1), Toast.LENGTH_SHORT).show();
						return true;
					}
				});
	        }
	        setContentView(main);
	        
	        tv_size.setText("x"+(size - (mg.getIndex())));
	        
	        Toast.makeText(c, "משחק מתחיל: \n גפרורים: "+ mg.getSize() + "\nמינימום: "+ mg.getMin() + "\nמקסימום: "+ mg.getMax() + "\nמול מחשב: " + mg.getMode() + "\nשחקן ראשון מתחיל: " + mg.getStart() +"\nשמות השחקנים: \n"+ namePlayerA + "\n" + namePlayerB  , Toast.LENGTH_LONG).show();
	        adapter = new CustomAdapterMatches(c, mg, clickedMatch , number_image);
	    	gv.setAdapter(adapter);
	    	if(vs_compuer && !turnOfPlayer){
	    		inPlaying = mg.play(amountChoose);
	    		turnOfPlayer = !turnOfPlayer;
	    		updateBoard();
	    	}
    	}
    	else{
    		Toast.makeText(c, "הנתונים לא נקלטו לאתחול המשחק", Toast.LENGTH_SHORT).show();
    		inPlaying = false;
    	}
    	
    	
    }
    private final Runnable mRunnable = new Runnable() {
        @Override
        public void run() {
            doubleBackToExitPressedOnce = false;                       
        }
    };
    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed();
            return;
        }

        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, getResources().getString(R.string.kayback_again), Toast.LENGTH_SHORT).show();
        mHandler.postDelayed(mRunnable, 2000);
    }
        
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
        	Intent i = new Intent(this, SettingActivity.class);
        	i.putExtra(ONCE, true);
        	startActivity(i);
            return true;
        }
        if (id == R.id.action_about) {
        	Toast.makeText(c, "לצורך שימוש אישי בפיתוח חשיבה מתמטית", Toast.LENGTH_SHORT).show();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
