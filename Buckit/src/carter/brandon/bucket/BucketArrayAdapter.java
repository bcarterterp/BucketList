package carter.brandon.bucket;

import java.util.List;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.Toast;

public class BucketArrayAdapter extends ArrayAdapter<Bucket>{
	
	private final Context context;
	private final List<Bucket> values;
	private final int layout;

	public BucketArrayAdapter(Context context,int layout, List<Bucket> values) {
		super(context,layout,values);
		this.context = context;
	    this.values = values;
	    this.layout = layout;
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		LayoutInflater inflater = (LayoutInflater) context
		        .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View rowView = inflater.inflate(layout, parent, false);
		((TextView)rowView.findViewById(R.id.text_view_bucket_name)).setText(values.get(position).getName());
		((TextView)rowView.findViewById(R.id.text_view_bucket_des)).setText(values.get(position).getDesc());
		int total = values.get(position).getTotalItems();
		int completed = values.get(position).getItemsCompleted();
		String progress = completed+"/"+total;
		((TextView)rowView.findViewById(R.id.text_view_progress)).setText(progress);
		
		return rowView;
	}
	
	


}
