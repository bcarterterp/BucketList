package carter.brandon.bucket;

import carter.brandon.bucket.BucketUserFragment.BuckItUserLogin;
import carter.brandon.bucket.SignInChoiceFragment.SignInChoice;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.app.Activity;
import android.app.FragmentTransaction;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import android.os.Build;
import com.facebook.*;
import com.facebook.Request.GraphUserCallback;
import com.facebook.model.*;
import com.parse.LogInCallback;
import com.parse.Parse;
import com.parse.ParseAnalytics;
import com.parse.ParseException;
import com.parse.ParseFacebookUtils;
import com.parse.ParseUser;
import com.parse.SignUpCallback;

public class SignIn extends FragmentActivity implements SignInChoice, 
			BuckItUserLogin{
	 
	private SignInChoiceFragment startFragment;
	private ParseUser currUser;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Parse.initialize(this, "xH7jQb9xpC8HQhYYj0yA8ueOUOYu7aIKDcIOP0bv", "g0RHYxbkxNVZeWlqzKMye19fc6m9vOkaxibhmy3b");
		ParseFacebookUtils.initialize("363689383795891");
		setContentView(R.layout.activity_sign_in);
		
		if (savedInstanceState == null) {
			startFragment = new SignInChoiceFragment();
			getSupportFragmentManager().beginTransaction()
					.add(R.id.container, startFragment).commit();
		}
	}

	@Override
	public void FacebookSignIn() {
		
		final ProgressDialog progress;
		progress = ProgressDialog.show(this, "Logging In", "Please wait while you are logged in",true);
		
		SharedPreferences preferences = getPreferences(0);
		final SharedPreferences.Editor editor = preferences.edit();
		
		ParseFacebookUtils.logIn(this, new LogInCallback() {
			
			@Override
			public void done(ParseUser user, ParseException e) {
							
				if(user == null){
					
					progress.dismiss();
					Toast.makeText(getApplicationContext(), "User cancled the facebook login", Toast.LENGTH_SHORT).show();
					
				}else if(user.isNew()){
					
					Request.newMeRequest(ParseFacebookUtils.getSession(), new GraphUserCallback() {
						
						@Override
						public void onCompleted(GraphUser fbuser, Response response) {
							
							if(fbuser != null){
								
								editor.putString("UserName", fbuser.getName());
								currUser = ParseUser.getCurrentUser();
								currUser.put("username", fbuser.getName());
								editor.putBoolean("verified", true);
								editor.putBoolean("Facebook", true);
								editor.commit();
								currUser.saveInBackground();
								progress.dismiss();
								Toast.makeText(getApplicationContext(), "Success", Toast.LENGTH_LONG).show();
								loginConfirmed();
							}else{
								Toast.makeText(getApplicationContext(), "Null Info!", Toast.LENGTH_LONG).show();
							}
							
						}
					}).executeAsync();
					
				}else{
					
					currUser = ParseUser.getCurrentUser();
					progress.dismiss();
					Toast.makeText(getApplicationContext(), "Returning User", Toast.LENGTH_SHORT).show();
					loginConfirmed();
					
				}
				
			}
		});
		
	}

	@Override
	public void BuckitLogin(final String uName, String pWord, String pWordConfirm) {
		
		final ProgressDialog progress;
		progress = ProgressDialog.show(this, "Logging In", "Please wait while you are logged in",true);
		
		SharedPreferences preferences = getPreferences(0);
		final SharedPreferences.Editor editor = preferences.edit();
		
		if(pWordConfirm == null){
			
			ParseUser.logInInBackground(uName, pWord, new LogInCallback() {
				
				@Override
				public void done(ParseUser user, ParseException e) {
					
					if(e == null){
						currUser = user;
						editor.putString("UserName", uName);
						editor.putBoolean("verified", true);
						editor.putBoolean("Facebook", false);
						editor.commit();
						Toast.makeText(getApplicationContext(), "Success!", Toast.LENGTH_LONG).show();
						loginConfirmed();
					}else{
						editor.putBoolean("verified", false);
						Toast.makeText(getApplicationContext(), e.toString(), Toast.LENGTH_LONG).show();
					}
					
					progress.dismiss();
					
				}
			});
			
		}else{
			
			final ParseUser user = new ParseUser();
			user.setUsername(uName);
			user.setPassword(pWord);
			user.signUpInBackground(new SignUpCallback() {
				
				@Override
				public void done(ParseException e) {
					if(e == null){
						
						currUser = user;
						editor.putString("UserName", uName);
						editor.putBoolean("verified", true);
						editor.putBoolean("Facebook", false);
						editor.commit();
						Toast.makeText(getApplicationContext(), "Success", Toast.LENGTH_SHORT).show();
						loginConfirmed();
						
					}else{
						
						editor.putBoolean("verified", true);
						Toast.makeText(getApplicationContext(), e.toString(), Toast.LENGTH_SHORT).show();
					}
					
					progress.dismiss();
					
				}
				
			});
		}
		
		
	}
	
	public void loginConfirmed(){
		Intent login = new Intent(SignIn.this,HubActivity.class);
		startActivity(login);
		finish();
	}

	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		  super.onActivityResult(requestCode, resultCode, data);
		  ParseFacebookUtils.finishAuthentication(requestCode, resultCode, data);
	}
}
