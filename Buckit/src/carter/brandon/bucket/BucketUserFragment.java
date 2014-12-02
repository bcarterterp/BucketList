package carter.brandon.bucket;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

/**
 * A simple {@link Fragment} subclass. Activities that contain this fragment
 * must implement the {@link BucketUserFragment.OnFragmentInteractionListener}
 * interface to handle interaction events. Use the
 * {@link BucketUserFragment#newInstance} factory method to create an instance
 * of this fragment.
 * 
 */
public class BucketUserFragment extends Fragment implements OnClickListener{

	private static int MIN_PASSWORD_LENGTH = 6;
	private static int MIN_USERNAME_LENGTH = 5;
	
	private BuckItUserLogin mListener;
	
	private EditText uNameEditText,pWordEditText,pWordConfirmEditText;
	private Button newUserToggleButton,doneButton;

	public BucketUserFragment() {
		// Required empty public constructor
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		
		View rootView = inflater.inflate(R.layout.fragment_buckit_signin, null);
		uNameEditText = (EditText) rootView.findViewById(R.id.edit_text_username);
		pWordEditText = (EditText) rootView.findViewById(R.id.edit_text_password);
		pWordConfirmEditText = (EditText) rootView.findViewById(R.id.edit_text_password_confirm);
		newUserToggleButton = (Button) rootView.findViewById(R.id.button_new_user_toggle);
		doneButton = (Button) rootView.findViewById(R.id.button_buckit_user_done);
		newUserToggleButton.setOnClickListener(this);
		doneButton.setOnClickListener(this);

		return rootView;
	}
	
	@Override
	public void onClick(View button) {
		
		switch (button.getId()) {
		case R.id.button_new_user_toggle:
			if(pWordConfirmEditText.getVisibility() == View.GONE){
				pWordConfirmEditText.setVisibility(View.VISIBLE);
				newUserToggleButton.setText("Returning User");
			}else{
				pWordConfirmEditText.setVisibility(View.GONE);
				newUserToggleButton.setText("New User");
			}
			break;
		case R.id.button_buckit_user_done:
			
			String uName = uNameEditText.getText().toString().trim();
			String pWord = pWordEditText.getText().toString().trim();
			
			if(pWordConfirmEditText.getVisibility() == View.GONE){
				if(isValid(uName, pWord, null)){
					mListener.BuckitLogin(uName, pWord, null);
				}
			}else{
				String pWordConfirm = pWordConfirmEditText.getText().toString().trim();
				if(isValid(uName, pWord, pWordConfirm)){
					mListener.BuckitLogin(uName, pWord, pWordConfirm);
				}
			}
			
			
			break;
		default:
			break;
		}
		
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		try {
			mListener = (BuckItUserLogin) activity;
		} catch (ClassCastException e) {
			throw new ClassCastException(activity.toString()
					+ " must implement BuckItUserLoging");
		}
	}

	@Override
	public void onDetach() {
		super.onDetach();
		mListener = null;
	}
	
	private boolean isValid(String uName,String pWord,String pWordConfirm){
		
		if(uName == ""){
			Toast.makeText(getActivity(), "Choose a Username", Toast.LENGTH_SHORT).show();
			return false;
		}
		
		if(uName.length() < MIN_USERNAME_LENGTH){
			Toast.makeText(getActivity(), "Username needs to be longer than "+MIN_USERNAME_LENGTH+" Characters", Toast.LENGTH_SHORT).show();
			return false;
		}
		if(pWord.length() < MIN_PASSWORD_LENGTH){
			Toast.makeText(getActivity(), "Password needs to be longer than "+MIN_PASSWORD_LENGTH+" Characters", Toast.LENGTH_LONG).show();
			return false;
		}
		
		if(!pWord.equals(pWordConfirm) && pWordConfirm != null){
			Toast.makeText(getActivity(), "Password confirmation doesn't match!", Toast.LENGTH_LONG).show();
			return false;
		}
		
		
		return true;
		
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
	public interface BuckItUserLogin {
		// TODO: Update argument type and name
		public void BuckitLogin(String uName,String pWord, String pWordConfirm);
		
	}

}
