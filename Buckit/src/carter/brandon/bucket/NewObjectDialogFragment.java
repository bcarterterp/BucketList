package carter.brandon.bucket;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

public class NewObjectDialogFragment extends DialogFragment{
	
	private String title,nameHint,descHint,purpose;
	private NewObjectListener listener;
	private EditText objName,objDesc;
	private TextView objNameText,objDescText;
	private int position;
	
	public NewObjectDialogFragment(String title,String nameHint,String descHint,String purpose,int position) {
		this.title = title;
		this.nameHint = nameHint;
		this.descHint = descHint;
		this.purpose = purpose;
		this.position = position;
	}
	
	@Override
	@NonNull
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		
		LayoutInflater inflater = getActivity().getLayoutInflater();
		final View rootView = inflater.inflate(R.layout.dialogfragment_new_object,null);
		objName = (EditText)rootView.findViewById(R.id.edit_text_new_object_name);
		objDesc = (EditText)rootView.findViewById(R.id.edit_text_new_object_desc);
		if (purpose.equalsIgnoreCase("create")) {
			objNameText = (TextView) rootView
					.findViewById(R.id.text_view_new_object_name);
			objNameText.setText(nameHint);
			objDescText = (TextView) rootView
					.findViewById(R.id.text_view_new_object_desc);
			objDescText.setText(descHint);
		}else if(purpose.equalsIgnoreCase("edit")){
			objNameText = (TextView) rootView
					.findViewById(R.id.text_view_new_object_name);
			objNameText.setText("Edit Name");
			objDescText = (TextView) rootView
					.findViewById(R.id.text_view_new_object_desc);
			objDescText.setText("Edit Description");
			objName.setText(nameHint);
			objDesc.setText(descHint);
			
		}
		AlertDialog.Builder builder = new Builder(getActivity());	
		builder.setView(rootView).setMessage(title).
		setPositiveButton("Create", new OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				
				String name = objName.getText().toString();
				String desc = objDesc.getText().toString();
				if(purpose.equalsIgnoreCase("create")){
					listener.createNewObject(name, desc);
				}else if(purpose.equalsIgnoreCase("edit")){
					listener.editObject(name, desc,position);
				}
					
			}
		}).setNegativeButton("Cancel", new OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				
				
				
			}
			
		});
		
		return builder.create();
		
	}
	
	public void setCaller(NewObjectListener caller){
		listener = caller;
	}
	
	public interface NewObjectListener{
		
		public void createNewObject(String name,String desc);
		public void editObject(String name,String desc,int position);
		
	}

}
