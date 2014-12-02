package carter.brandon.bucket;

import java.util.List;

import carter.brandon.bucket.BucketItemsFragment.OnBucketItemListener;

import com.parse.FindCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBar;
import android.support.v4.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.os.Build;

public class BucketItemsActivity extends ActionBarActivity implements OnBucketItemListener{

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_bucket_items);
		
		Parse.initialize(this, "xH7jQb9xpC8HQhYYj0yA8ueOUOYu7aIKDcIOP0bv", "g0RHYxbkxNVZeWlqzKMye19fc6m9vOkaxibhmy3b");
		BucketItemsFragment fragment = new BucketItemsFragment();
		Intent i = getIntent();
		String bucketID = i.getStringExtra("bucket");
		Bundle bundle = new Bundle();
		bundle.putString("bucketName", bucketID);
		fragment.setArguments(bundle);
		getSupportFragmentManager().beginTransaction()
		.add(R.id.container, fragment).commit();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.bucket_items, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

}
