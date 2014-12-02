package carter.brandon.bucket;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import carter.brandon.bucket.BucketItemArrayAdapter.BucketItemViewListener;
import carter.brandon.bucket.NewObjectDialogFragment.NewObjectListener;

import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseRelation;
import com.parse.ParseUser;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

/**
 * A simple {@link Fragment} subclass. Activities that contain this fragment
 * must implement the {@link BucketItemsFragment.OnFragmentInteractionListener}
 * interface to handle interaction events. Use the
 * {@link BucketItemsFragment#newInstance} factory method to create an instance
 * of this fragment.
 * 
 */
public class BucketItemsFragment extends Fragment implements OnClickListener, NewObjectListener, BucketItemViewListener{

	private final String DELETE = "Delete";
	private final String EDIT = "Edit";
	private final int DELETE_ID = 0;
	private final int EDIT_ID = 1;
	
	private OnBucketItemListener mListener;
	private String bucketName;
	private TextView bucketNameText;
	private ListView bucketItemList;
	private ArrayList<BucketItem> items = new ArrayList<BucketItem>();
	private List<ParseObject> parseItems;
	private BucketItemArrayAdapter adapter;
	private ParseRelation<ParseObject> relation;
	private ParseObject bucket;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		
		View rootView = inflater.inflate(R.layout.fragment_bucket_items, container, false);
		bucketNameText = (TextView)rootView.findViewById(R.id.text_view_bucket_name);
		adapter = new BucketItemArrayAdapter(getActivity(), R.layout.list_view_row_bucket_item, items);
		adapter.onAttach(this);
		getBucketListData();
		bucketItemList = (ListView)rootView.findViewById(R.id.list_view_bucket_items);
		bucketItemList.setAdapter(adapter);
		
		registerForContextMenu(bucketItemList);
		
		(rootView.findViewById(R.id.button_new_bucket_item)).setOnClickListener(this);
		return rootView;
	}


	private void getBucketListData() {
		Toast.makeText(getActivity(), "In here", Toast.LENGTH_SHORT).show();
		ParseQuery<ParseObject> query = new ParseQuery<ParseObject>("Bucket");
		query.whereEqualTo("objectId", bucketName);
		query.findInBackground(new FindCallback<ParseObject>() {

			@Override
			public void done(List<ParseObject> objects, ParseException e) {
				
				if(e == null){
					bucket = objects.get(0);
					bucketNameText.setText(bucket.getString("name"));
					ParseRelation<ParseObject> itemsRelation =  bucket.getRelation("bucketitems");
					relation = itemsRelation;
					relation.getQuery().findInBackground(new FindCallback<ParseObject>() {

						@Override
						public void done(List<ParseObject> objects, ParseException e) {
							if(e == null){
								parseItems = objects;
								for(ParseObject parseBucketItem:parseItems){
									String name = parseBucketItem.getString("name");
									String desc = parseBucketItem.getString("description");
									BucketItem newBucket = new BucketItem(name, desc, Calendar.getInstance().getTime());
									newBucket.setComplete(parseBucketItem.getBoolean("completed"));
									items.add(newBucket);
									adapter.notifyDataSetChanged();

								}
							}else{
								e.printStackTrace();
								System.out.println(e.toString());
							}

						}

					});

				}else{
					e.printStackTrace();
					System.out.println(e.toString());
				}
				
			}
		});
	}


	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		try {
			mListener = (OnBucketItemListener) activity;
		} catch (ClassCastException e) {
			throw new ClassCastException(activity.toString()
					+ " must implement OnFragmentInteractionListener");
		}
	}

	@Override
	public void onDetach() {
		super.onDetach();
		mListener = null;
	}
	
	@Override
	public void setArguments(Bundle args) {
		this.bucketName = args.getString("bucketName");
	}
	
	@Override
	public void onClick(View v) {
		
		NewObjectDialogFragment newBucket = new NewObjectDialogFragment("New Bucket Item", "Bucket Item Name", "Bucket Item Description","create",-1);
		newBucket.setCaller(this);
		newBucket.show(getFragmentManager(), "New Bucket Item");
		
	}
	
	@Override
	public void createNewObject(String name, String desc) {
		
		ParseObject bucketItem = new ParseObject("BucketItem");
		bucketItem.put("description", desc);
		bucketItem.put("name", name);
		bucketItem.put("completed", false);
		items.add(new BucketItem(name, desc, Calendar.getInstance().getTime()));
		parseItems.add(bucketItem);
		relation.add(bucketItem);
		bucket.put("total", items.size());
		bucketItem.saveEventually();
		bucket.saveEventually();
		adapter.notifyDataSetChanged();
		
	}


	@Override
	public void editObject(String name, String desc, int position) {
		
		BucketItem bucketItem = items.get(position);
		bucketItem.setName(name);
		bucketItem.setDesc(desc);
		ParseObject parseBucketItem = parseItems.get(position);
		parseBucketItem.put("description", desc);
		parseBucketItem.put("name",name);
		parseBucketItem.saveEventually();
		adapter.notifyDataSetChanged();
		
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
			
			items.remove(position);
			ParseObject removeItem = parseItems.remove(position);
			relation.remove(removeItem);
			if(removeItem.getBoolean("completed")){
				bucket.put("numdone",bucket.getInt("numDone")-1);
			}
			removeItem.deleteEventually();
			adapter.notifyDataSetChanged();
			bucket.saveEventually();
			Toast.makeText(getActivity(), "DELETE SUCCESSFUL", Toast.LENGTH_SHORT).show();
			return true;
		case EDIT_ID:
			String bucketItemName = items.get(position).getName();
			String bucketItemDesc = items.get(position).getDesc();
			NewObjectDialogFragment newBucket = new NewObjectDialogFragment("New Bucket Item", bucketItemName, bucketItemDesc,"edit",position);
			newBucket.setCaller(this);
			newBucket.show(getFragmentManager(), "Edit Bucket");
			return true;
		default:
			return false;
		}
	}
	
	@Override
	public void ischecked(int position) {
		
		ParseObject item = parseItems.get(position);
		item.put("completed", true);
		bucket.put("numdone", bucket.getInt("numdone")+1);
		item.saveEventually();
		bucket.saveEventually();
		
	}
	
	@Override
	public void isUnchecked(int position) {
		
		ParseObject item = parseItems.get(position);
		item.put("completed", false);
		bucket.put("numdone", bucket.getInt("numdone")-1);
		item.saveEventually();
		bucket.saveEventually();
		
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
	public interface OnBucketItemListener {

		
		
	}


}
