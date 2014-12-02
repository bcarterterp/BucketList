package carter.brandon.bucket;

import com.parse.Parse;
import com.parse.ParseObject;

import carter.brandon.bucket.BucketsFragment.BucketsEventListener;
import carter.brandon.bucket.HomeFragment.HomeEventListener;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBar.Tab;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

public class HubActivity extends ActionBarActivity implements HomeEventListener,
						BucketsEventListener{

	private HubPagerAdapter hubAdapter;
	private ViewPager mViewPager;
	BucketsFragment bucketFragment;
	
	@SuppressWarnings("deprecation")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_hub);
		
		hubAdapter = new HubPagerAdapter(getSupportFragmentManager());
		mViewPager = (ViewPager) findViewById(R.id.container);
		mViewPager.setAdapter(hubAdapter);
		
		final ActionBar actionBar = getSupportActionBar();
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
		
		ActionBar.TabListener tabListener = new ActionBar.TabListener() {
			
			@Override
			public void onTabUnselected(Tab arg0, FragmentTransaction arg1) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onTabSelected(Tab arg0, FragmentTransaction arg1) {
				mViewPager.setCurrentItem(arg0.getPosition());
				
			}
			
			@Override
			public void onTabReselected(Tab arg0, FragmentTransaction arg1) {
				// TODO Auto-generated method stub
				
			}
		};
		
		actionBar.addTab(actionBar.newTab().setText("Home").setTabListener(tabListener));
		actionBar.addTab(actionBar.newTab().setText("Bucket").setTabListener(tabListener));
	
		mViewPager.setOnPageChangeListener(new OnPageChangeListener() {
		
			@Override
			public void onPageSelected(int position) {
				
				getSupportActionBar().setSelectedNavigationItem(position);
				
			}
			
			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onPageScrollStateChanged(int arg0) {
				// TODO Auto-generated method stub
				
			}
			
		});
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.hub, menu);
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
	
	@Override
	public void startBucketItemFragment(ParseObject bucket) {
		
		
		Intent nextActivity = new Intent(HubActivity.this, BucketItemsActivity.class);
		nextActivity.putExtra("bucket", bucket.getObjectId());
		startActivity(nextActivity);
		
	}

	public class HubPagerAdapter extends FragmentPagerAdapter{
		
		String titles[] = {"Home","Buckets"};
		Fragment fragments[] = new Fragment[3];

		public HubPagerAdapter(FragmentManager fm) {
			super(fm);
			bucketFragment = new BucketsFragment();
			fragments[0] = new HomeFragment();
			fragments[1] = bucketFragment;
		}

		@Override
		public Fragment getItem(int i) {
			
			
			if(i == 0){
				return fragments[0];
			}else if(i == 1){
				return fragments[1];
			}else{
				Toast.makeText(getApplicationContext(), "NULLLLLLL", Toast.LENGTH_LONG).show();
				return null;
			}
			
		}

		@Override
		public int getCount() {
			
			return 2;
			
		}
		
		@Override
		public CharSequence getPageTitle(int position) {
			
			return titles[position];
		}
		
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		hubAdapter.notifyDataSetChanged();
	}
	
}
