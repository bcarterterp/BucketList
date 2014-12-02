package carter.brandon.bucket;

import java.util.List;

import carter.brandon.bucket.BucketsFragment.BucketsEventListener;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.LinearLayout;
import android.widget.TextView;

public class BucketItemArrayAdapter extends ArrayAdapter<BucketItem>{
	
	private final Context context;
	private final List<BucketItem> values;
	private final int layout;
	private BucketItemViewListener mListener;
	
	public BucketItemArrayAdapter(Context context,int layout, List<BucketItem> values) {
		super(context,layout,values);
		this.context = context;
	    this.values = values;
	    this.layout = layout;
	}
	
	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		LayoutInflater inflater = (LayoutInflater) context
		        .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View rowView = inflater.inflate(layout, parent, false);
		((TextView)rowView.findViewById(R.id.text_view_bucket_item_name)).setText(values.get(position).getName());
		((TextView)rowView.findViewById(R.id.text_view_bucket_item_des)).setText(values.get(position).getDesc());
		CheckBox box = (CheckBox)rowView.findViewById(R.id.check_box_bucket_item_progress);
		if(values.get(position).isComplete()){
			box.setChecked(true);
		}
		box.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				
				if(isChecked){
					mListener.ischecked(position);
				}else{
					mListener.isUnchecked(position);
				}
				
			}
		});
		
		return rowView;
	}
	
	public void onAttach(BucketItemViewListener listener) {
		
			mListener = listener;
		
	}
	
	public interface BucketItemViewListener{
		
		public void ischecked(int position);
		public void isUnchecked(int position);
		
	}

}
