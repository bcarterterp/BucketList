package carter.brandon.bucket;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import carter.brandon.bucket.NewObjectDialogFragment.NewObjectListener;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.internal.widget.AdapterViewCompat;
import android.support.v7.internal.widget.AdapterViewCompat.AdapterContextMenuInfo;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.MenuItem.OnMenuItemClickListener;
import android.view.View;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ListView;
import android.widget.Toast;

/**
 * A simple {@link Fragment} subclass. Activities that contain this fragment
 * must implement the {@link BucketsFragment.OnFragmentInteractionListener}
 * interface to handle interaction events. Use the
 * {@link BucketsFragment#newInstance} factory method to create an instance of
 * this fragment.
 * 
 */
public class BucketsFragment extends Fragment implements OnClickListener, NewObjectListener, OnItemClickListener{

	private final String DELETE = "Delete";
	private final String EDIT = "Edit";
	private final int DELETE_ID = 0;
	private final int EDIT_ID = 1;
	
	private BucketsEventListener mListener;
	private ArrayList<Bucket> values = new ArrayList<Bucket>();
	private List<ParseObject> parseBuckets;
	private BucketArrayAdapter adapter;
	private ListView listOfBuckets;
	

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		
		View rootView = inflater.inflate(R.layout.fragment_buckets, container, false);
		adapter = new BucketArrayAdapter(getActivity(), R.layout.list_view_row_bucket,values);
		getBuckets();
		listOfBuckets = (ListView)rootView.findViewById(R.id.list_view_buckets);
		listOfBuckets.setAdapter(adapter);
		
		registerForContextMenu(listOfBuckets);
		(rootView.findViewById(R.id.button_new_bucket)).setOnClickListener(this);
		
		listOfBuckets.setOnItemClickListener(this);
		
		return rootView;
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		try {
			mListener = (BucketsEventListener) activity;
		} catch (ClassCastException e) {
			throw new ClassCastException(activity.toString()
					+ " must implement BucketsEventListener");
		}
	}

	@Override
	public void onDetach() {
		super.onDetach();
		mListener = null;
	}
	
	@Override
	public void onClick(View view) {
		
		NewObjectDialogFragment newBucket = new NewObjectDialogFragment("New Bucket", "Bucket Name", "Bucket Description","create",-1);
		newBucket.setCaller(this);
		newBucket.show(getFragmentManager(), "New Bucket");
		
	}
	
	@Override
	public void onItemClick(AdapterView<?> av, View view, int position, long id) {
		
		mListener.startBucketItemFragment(parseBuckets.get(position));
		
	}
	
	@Override
	public void createNewObject(final String name, final String desc) {
		
		ParseObject bucket = new ParseObject("Bucket");
		bucket.put("description", desc);
		bucket.put("name", name);
		bucket.put("owner", ParseUser.getCurrentUser());
		bucket.put("numdone", 0);
		bucket.put("total", 0);
		values.add(new Bucket(name, desc, Calendar.getInstance().getTime()));
		parseBuckets.add(bucket);
		bucket.saveEventually();
		
		adapter.notifyDataSetChanged();
		
		
	}
	
	@Override
	public void editObject(String name, String desc,int position) {
		
		Bucket bucket = values.get(position);
		bucket.setName(name);
		bucket.setDesc(desc);
		ParseObject parseBucket = parseBuckets.get(position);
		parseBucket.put("description", desc);
		parseBucket.put("name",name);
		parseBucket.saveEventually();
		adapter.notifyDataSetChanged();
		
		
	}
	
	private void getBuckets(){
		
		ParseUser user = ParseUser.getCurrentUser();
		ParseQuery<ParseObject> query = ParseQuery.getQuery("Bucket");
		query.whereEqualTo("owner", user);
		query.findInBackground(new FindCallback<ParseObject>() {
			
			@Override
			public void done(List<ParseObject> objects, ParseException e) {
				
				if(objects == null){
					
				}else{
					for(ParseObject bucket:objects){
						String name = bucket.getString("name");
						String desc = bucket.getString("description");
						Date date = bucket.getCreatedAt();
						Bucket newBucket = new Bucket(name,desc,date);
						newBucket.setItemsCompleted(bucket.getInt("numdone"));
						newBucket.setTotalItems(bucket.getInt("total"));
						values.add(newBucket);
					}
					parseBuckets = objects;
					adapter.notifyDataSetChanged();
				}
				
			}
		});
		
		
	}
	
	@Override
	public void onCreateContextMenu(ContextMenu menu, View v,
			ContextMenuInfo menuInfo) {
		
		super.onCreateContextMenu(menu, v, menuInfo);
		menu.add(0, DELETE_ID, 0, DELETE);
		menu.add(0, EDIT_ID, 1, EDIT);
	}
	
	@Override
	public boolean onContextItemSelected(MenuItem item) {
		AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
		int position = info.position;
		
		switch (item.getItemId()) {
		case DELETE_ID:
			values.remove(position);
			parseBuckets.remove(position).deleteEventually();
			adapter.notifyDataSetChanged();
			Toast.makeText(getActivity(), "DELETE SUCCESSFUL", Toast.LENGTH_SHORT).show();
			return true;
		case EDIT_ID:
			String bucketName = values.get(position).getName();
			String bucketDesc = values.get(position).getDesc();
			NewObjectDialogFragment newBucket = new NewObjectDialogFragment("New Bucket", bucketName, bucketDesc,"edit",position);
			newBucket.setCaller(this);
			newBucket.show(getFragmentManager(), "Edit Bucket");
			return true;
		default:
			return false;
		}
	}

	/**
	 * This interface must be implemented by activities that contain this
	 * fragment to allow an interaction in this fragment to be communicated to
	 * the activity and potentially other fragments contained in that activity.
	 * <p>
	 * See the Android Training lesson <a href=
	 * "http://developer.android.com/training/basics/fragments/communicating.html"
	 * >Communicating with Other Fragments</a> for more information.
	 */
	public interface BucketsEventListener {
		
		public void startBucketItemFragment(ParseObject bucket);
		
	}

}
