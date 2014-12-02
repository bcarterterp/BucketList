package carter.brandon.bucket;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
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
 * must implement the {@link SignInChoiceFragment.OnFragmentInteractionListener}
 * interface to handle interaction events. Use the
 * {@link SignInChoiceFragment#newInstance} factory method to create an instance
 * of this fragment.
 * 
 */
public class SignInChoiceFragment extends Fragment implements OnClickListener {
	// TODO: Rename parameter arguments, choose names that match
	// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
	private static final String ARG_PARAM1 = "param1";
	private static final String ARG_PARAM2 = "param2";

	// TODO: Rename and change types of parameters
	private String mParam1;
	private String mParam2;

	private SignInChoice mListener;

	/**
	 * Use this factory method to create a new instance of this fragment using
	 * the provided parameters.
	 * 
	 * @param param1
	 *            Parameter 1.
	 * @param param2
	 *            Parameter 2.
	 * @return A new instance of fragment SignInChoiceFragment.
	 */
	// TODO: Rename and change types and number of parameters
	public static SignInChoiceFragment newInstance(String param1, String param2) {
		SignInChoiceFragment fragment = new SignInChoiceFragment();
		Bundle args = new Bundle();
		args.putString(ARG_PARAM1, param1);
		args.putString(ARG_PARAM2, param2);
		fragment.setArguments(args);
		return fragment;
	}

	public SignInChoiceFragment() {
		// Required empty public constructor
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		if (getArguments() != null) {
			mParam1 = getArguments().getString(ARG_PARAM1);
			mParam2 = getArguments().getString(ARG_PARAM2);
		}
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		
		View rootView = inflater.inflate(R.layout.fragment_sign_in_choice,null);
		
		TextView textView = new TextView(getActivity());
		textView.setText(R.string.hello_blank_fragment);
		Button fbButton = (Button) rootView.findViewById(R.id.authButton);
		Button buckitButton = (Button) rootView.findViewById(R.id.button_buckit_login);
		buckitButton.setOnClickListener(this);
		fbButton.setOnClickListener(this);
		return rootView;
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		try {
			mListener = (SignInChoice) activity;
		} catch (ClassCastException e) {
			throw new ClassCastException(activity.toString()
					+ " must implement SignInChoice");
		}
	}

	@Override
	public void onDetach() {
		super.onDetach();
		mListener = null;
	}

	
	@Override
	public void onClick(View button) {
		switch (button.getId()) {
		case R.id.button_buckit_login:
			
			BucketUserFragment userFragment = new BucketUserFragment();
			FragmentManager fm = getFragmentManager();
			FragmentTransaction transaction = getFragmentManager().beginTransaction();
			transaction.replace(R.id.container, userFragment);
			transaction.addToBackStack(null);
			transaction.commit();
			
			break;
		case R.id.authButton:
			mListener.FacebookSignIn();
			break;
		default:
			break;
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
	public interface SignInChoice {
		// TODO: Update argument type and name
		public void FacebookSignIn();
		
	}

}
