package rnd.fileexplorer2013.android;

import java.io.File;

import rnd.fileexplorer2013.exandroid.R;
import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;


public class CustomListItemView extends LinearLayout {
    private Context mContext;
    private View childView = null;     
    private ExAndroidActivity gAct;
    private CustomDate mItem;
  
    private TextView mListName;
    private TextView mListSubName;
    
    private File currentDir;	// 현재 경로

    String mstrListName;
    String mstrListSubName;

    public CustomListItemView(Context context, AttributeSet attrs) {
          super(context, attrs);
          mContext = context;     
          gAct = (ExAndroidActivity)mContext;
          LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
          childView = inflater.inflate(R.layout.customlistitem, null);
          LayoutParams ll = new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT);     
         addView(childView, ll);
         
         mListName = (TextView)findViewById(R.id.listname);
         mListSubName = (TextView)findViewById(R.id.listsubname);
    }
     
    public CustomListItemView(Context context) {
         super(context);
         mContext = context;     
         gAct = (ExAndroidActivity)mContext;
         LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
         childView = inflater.inflate(R.layout.customlistitem, null);
         LayoutParams ll = new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT);    
         addView(childView, ll);
         
         mListName = (TextView)findViewById(R.id.listname);
         mListSubName = (TextView)findViewById(R.id.listsubname);
         
    }
    
    public void setMessage(CustomDate msg) {
         mItem = msg;
         
         mstrListName = mItem.GetWay();
         mstrListSubName = mItem.GetSize();
 
         mListName.setText(mstrListName);
         mListSubName.setText(mstrListSubName);  
    }
}