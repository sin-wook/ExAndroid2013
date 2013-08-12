package rnd.fileexplorer2013.android;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import rnd.fileexplorer2013.exandroid.R;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Toast;

public class CustomAdapter extends ArrayAdapter<CustomDate> {
	
	private List<CustomDate> mItems;
	private int mResourceId;
	private Context mContext;
	private ExAndroidActivity exAndroid = new ExAndroidActivity();
	
	
	public CustomAdapter(Context context, int textViewResourceId,List<CustomDate> objects) {
		super(context, textViewResourceId, objects);

		this.mContext = context;
		this.mItems = objects;
		this.mResourceId = textViewResourceId;
	}


    
    @Override
	public View getView(int position, View convertView, ViewGroup parent) {

		CustomListItemView mItemView;
		if (convertView == null) {
			mItemView = new CustomListItemView(mContext);
		} else {
			mItemView = (CustomListItemView) convertView;
		}

		CustomDate msg = mItems.get(position);
		mItemView.setMessage(msg);
		convertView = mItemView;
		
		return convertView;
	
    }
    
    
    

	

}



