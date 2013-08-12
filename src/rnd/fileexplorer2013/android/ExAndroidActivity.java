package rnd.fileexplorer2013.android;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import rnd.fileexplorer2013.exandroid.R;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.ListActivity;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;



public class ExAndroidActivity extends ListActivity{
	ArrayList<CustomDate> mOrders = new ArrayList<CustomDate>();
	
	CustomAdapter mAdapter;
	ListView mList;
	Button bt_newfolder;
	
	private List<String> ex_List = new ArrayList<String>();
	private List<String> ex_Route = new ArrayList<String>();
	private String root = "/"; // �ʱ� ���� ���, ������ ������ �ȵǱ� ������ �ٸ����� �������� �Ұ��ϴ�
	private String Where_root = "/";
	private TextView myRoute; 	// ���� ��θ� �������ִ� ����
	private File currentDir;	// ���� ���
	private String testgit;
	//--------------------������ �޴���--------------------------
	private View lay1;
	private View lay2;
	
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.customlist);
		//FileOI fio = new FileOI();
		
		lay1 = findViewById(R.id.test1);
		lay2 = findViewById(R.id.test2);
		
		myRoute = (TextView) findViewById(R.id.Route);
		bt_newfolder = (Button)findViewById(R.id.menu_newfolder);
		bt_newfolder.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				confirmCreateFolder();
			}
		});
		
		getDir(root);
		
		mList = getListView();
		mList.setAdapter(mAdapter);
		mList.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> a, View v, int position,long id) {
				// TODO Auto-generated method stub
				Toast.makeText(ExAndroidActivity.this, "click : ", Toast.LENGTH_SHORT).show();	
				Where_root = ex_Route.get(position);
				File file = new File(ex_Route.get(position));
				
				if (file.isDirectory()) {
					if (file.canRead()) {
						getDir(ex_Route.get(position));
					} else {
						Toast.makeText(ExAndroidActivity.this, "���� ������ �����ϴ�", Toast.LENGTH_SHORT).show();
					}
				} else{
					Toast.makeText(ExAndroidActivity.this, file.getName(), Toast.LENGTH_SHORT).show();
				}
			}
			
		});
		mList.setOnItemLongClickListener(getLongClickListener());
		setListAdapter(mAdapter);
	}
	
//	Long Click Event : return method
	private OnItemLongClickListener getLongClickListener(){
		return new OnItemLongClickListener() {

			@Override
			public boolean onItemLongClick(AdapterView<?> a, View v, int position,long id) {
				chg();
				return true;
			}
		};
		
	}
    
	public void chg(){
		if(lay1.getVisibility() == View.VISIBLE)  {
			lay1.setVisibility(View.INVISIBLE);
			lay2.setVisibility(View.VISIBLE);
        } else  {
        	lay2.setVisibility(View.INVISIBLE);
			lay1.setVisibility(View.VISIBLE);
        }
	}
	
	public void getDir(String root1) {
		myRoute.setText("��� : " + root1);
		currentDir = new File(root1);
		Where_root = root1;
		
		CustomDate item;
		
		mOrders.clear();
		ex_List.clear();
		ex_Route.clear();

		Log.d("ExAndroidActivity Log", "���:"+root1);
		
		File f = new File(root1); // �޾� ���� �Ű������� ��λ�� ���ο� ������ �����
		File[] files = f.listFiles(); // �������� ���� ����� �ҷ� ���� �Լ� listFiles();

		if (!root1.equals(root)) { // ���� ��η� �̵� �Ǿ������ �ֻ��� ��ζ� �ٸ��ٸ�

			item = new CustomDate("../", null);
			ex_Route.add(f.getParent());// ���� ��θ� ���� ��ȯ �ϴ� �ڵ�
			mOrders.add(item);	
		}

		
		for (int i = 0; i < files.length; i++) {
			File file = files[i];
			ex_Route.add(file.getPath());

			if (file.isDirectory()){
				ex_List.add(file.getName() + "/"); // ���������� ���� ���丮��� /�� �ٿ�
				item = new CustomDate(ex_List.get(i),"");
			}
			else{
				ex_List.add(file.getName());
				item = new CustomDate(ex_List.get(i), countSize(ex_Route.get(i).length()) + " byte");
			}
			mOrders.add(item);
			
		}
		
		mAdapter = new CustomAdapter(this, R.layout.customlistitem, mOrders);
		mAdapter.notifyDataSetChanged();
		
		setListAdapter(mAdapter);
	
		
	}

	
	public void onBackPressed() {

		if(Where_root.equals(root)){
		Builder d = new AlertDialog.Builder(this);
		d.setMessage("���� �����Ͻðڽ��ϱ�?");
		d.setPositiveButton("��", new DialogInterface.OnClickListener() {

			public void onClick(DialogInterface dialog, int which) {
				// process��ü ����
				finish();
			}
		});
		d.setNegativeButton("�ƴϿ�", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				dialog.cancel();
			}
		});
		d.show();
		}
		else
			getDir(ex_Route.get(0));	
	}
	
	public int countSize(int _size){
//		double kil =(_size/1024);
//		double meg =(kil/1024);
		return _size;
	}
	
//	create new folder 
	private void confirmCreateFolder() {
		
		AlertDialog.Builder alert = new AlertDialog.Builder(this);

		alert.setTitle(getString(R.string.create_folder));
		// Set an EditText view to get user input
		final EditText input = new EditText(this);
		input.setHint(getString(R.string.enter_folder_name));
		input.setSingleLine();
		alert.setView(input);

		alert.setPositiveButton(android.R.string.ok,
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int whichButton) {
						CharSequence newDir = input.getText();
						Boolean makeFResult = Util.mkDir(currentDir.getAbsolutePath(), newDir);
						
						if (makeFResult){
							Toast.makeText(ExAndroidActivity.this, "created Folder", Toast.LENGTH_SHORT).show();
							getDir(currentDir.getAbsolutePath());
						}else{
							Toast.makeText(ExAndroidActivity.this, "Failed Folder", Toast.LENGTH_SHORT).show();
						}
					}
				});

		alert.setNegativeButton(android.R.string.cancel,
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int whichButton) {

						dialog.dismiss();
					}
				});

		alert.show();

	}


	
}