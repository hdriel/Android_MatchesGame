package hdriel.matchgame;


import java.util.Random;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class CustomAdapterMatches extends BaseAdapter /* implements OnClickListener*/ {
		matchesGame mg;
		private LayoutInflater layoutInflater;
		Context context;
		boolean clickedMatch[];
		int number_image[];
		
		public CustomAdapterMatches(Context context, matchesGame mg, boolean clickedMatch[], int number_image[]) {
			layoutInflater = LayoutInflater.from(context);
			this.mg = mg; 
			this.context = context;
			this.clickedMatch = clickedMatch;
			this.number_image = number_image;
		}

		public void changeData(matchesGame mg, boolean clickedMatch[], int number_image[]){
			this.mg = mg; 
			this.clickedMatch = clickedMatch;
			this.number_image = number_image;
		}
		@Override
		public int getCount() {
			return mg.getSize();
		}

		@Override
		public Object getItem(int position) {
			return mg.getStateMatchesByIndex(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		public View getView(final int position, View convertView, ViewGroup parent) {
			View gridView;
			
			if (convertView == null) // if it's not recycled, initialize some attributes
            {
                gridView = new View(context);
                layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                gridView = layoutInflater.inflate(R.layout.tab_item_match, parent, false);	                
            } 
            else {
                gridView = (View) convertView;
            }
			
			TextView index = (TextView)  gridView.findViewById(R.id.tv_index_match);
			ImageView image= (ImageView) gridView.findViewById(R.id.iv_image_match);
			
			
			
			// set value into imageViews
			if(clickedMatch[position]){
				 image.setBackgroundResource(R.drawable.m2);
			}
			//else{
				Random rand = new Random();
				if(number_image[position] == 0){
					number_image[position] = rand.nextInt(3) + 1;
				}
				if(!mg.getStateMatchesByIndex(position)){
					switch(number_image[position]){
					case 1:
						image.setImageResource(R.drawable.m_l1);
						break;
					case 2:
						image.setImageResource(R.drawable.m_l2);
						break;
					case 3:
						image.setImageResource(R.drawable.m_l3);
						break;
					default: 
						image.setImageResource(R.drawable.m_l1);
					}
				}
				else
					switch(number_image[position]){
					case 1:
						image.setImageResource(R.drawable.m_s1);
						break;
					case 2:
						image.setImageResource(R.drawable.m_s2);
						break;
					case 3:
						image.setImageResource(R.drawable.m_s3);
						break;
					default: 
						image.setImageResource(R.drawable.m_s4);
					}
					
			//}
			
			index.setText(""+ (position+1));
			
			LayoutParams params = gridView.getLayoutParams();
			LayoutParams paramsImage = image.getLayoutParams();
			
			if(mg.getSize() <= 20){
				params.height = (int) (gridView.getResources().getDimension(R.dimen.match_height));
				paramsImage.height = (int) (gridView.getResources().getDimension(R.dimen.match_height) - gridView.getResources().getDimension(R.dimen.diff));
				index.setTextSize(20);
			}else if(mg.getSize() >= 40){
				params.height = (int) gridView.getResources().getDimension(R.dimen.match_height_3);
				paramsImage.height = (int) (gridView.getResources().getDimension(R.dimen.match_height_3) - gridView.getResources().getDimension(R.dimen.diff));
				index.setTextSize(10);
			}else{
				params.height = (int) gridView.getResources().getDimension(R.dimen.match_height_2);
				paramsImage.height = (int) (gridView.getResources().getDimension(R.dimen.match_height_2) - gridView.getResources().getDimension(R.dimen.diff));
				index.setTextSize(15);
			}
			gridView.setLayoutParams(params);
			
			return gridView;
		}



		
	}
