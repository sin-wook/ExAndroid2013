package rnd.fileexplorer2013.android;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.FileUtils;

import rnd.fileexplorer2013.exandroid.R;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.ListActivity;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
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
	private String root = "/"; // 초기 시작 경로, 하지만 루팅이 안되기 때문에 다른곳은 접ㄱ느이 불가하다
	private String Where_root = "/";
	private TextView myRoute; 	// 현재 경로를 저장해주는 변수
	private File currentDir;	// 현재 경로
	private File selectedList;	// 선택된 리스트
	private Button delBT;		// 하단 기능 버튼
	private Button renameBT;	// 이름변경
	private TextView savedData;	// 저장된 데이터 참조
	AlertDialog.Builder alert;
	EditText input;
	
	//--------------------프레임 메뉴얼--------------------------
	private View lay1;
	private View lay2;
	
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.customlist);
		//FileOI fio = new FileOI();
		savedData = (TextView) findViewById(R.id.saveData);
		lay1 = findViewById(R.id.test1);
		lay2 = findViewById(R.id.test2);
		delBT = (Button) findViewById(R.id.del);
		renameBT = (Button) findViewById(R.id.rename);
		myRoute = (TextView) findViewById(R.id.Route);
		bt_newfolder = (Button)findViewById(R.id.menu_newfolder);
		
		
		
		bt_newfolder.setOnClickListener(getClickListener_newfolder());
		delBT.setOnClickListener(getClickListener_del());
		renameBT.setOnClickListener(getClickListener_rename());
		
		getDir(root);
		
		mList = getListView();
		mList.setAdapter(mAdapter);
		mList.setOnItemClickListener(getClickListener());
		mList.setOnItemLongClickListener(getLongClickListener());
		setListAdapter(mAdapter);
	}// onCreate() end
	
//	Click Event : return method
	private OnItemClickListener getClickListener(){
		return new OnItemClickListener() {

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
						Toast.makeText(ExAndroidActivity.this, "접근 권한이 없습니다", Toast.LENGTH_SHORT).show();
					}
				} else{
					Toast.makeText(ExAndroidActivity.this, file.getName(), Toast.LENGTH_SHORT).show();
				}
			}
		};
	}
//	Long Click Event : return method
	private OnItemLongClickListener getLongClickListener(){
		return new OnItemLongClickListener() {

			@Override
			public boolean onItemLongClick(AdapterView<?> a, View v, int position,long id) {
				Where_root = ex_Route.get(position);
				selectedList = new File(ex_Route.get(position));
				
				if(selectedList.canWrite()){
					savedData.setText(selectedList+"("+selectedList.exists()+")");
					chg();
				}else{
					Toast.makeText(ExAndroidActivity.this, "접근 권한이 없습니다.", Toast.LENGTH_SHORT).show();
					selectedList=null;
					savedData.setText(selectedList+"");
					chg();
				}
				
				return true;
			}
		};
		
	}
//	delete click event
	private OnClickListener getClickListener_del(){
		return new OnClickListener() {
			
			@Override
			public void onClick(View v) {
//				Delete selected to file or folder
				if(selectedList.canWrite()){
					if(selectedList.delete()){
						Toast.makeText(ExAndroidActivity.this, "삭제 완료", Toast.LENGTH_SHORT).show();
					}else{
						Toast.makeText(ExAndroidActivity.this, "삭제 오류", Toast.LENGTH_SHORT).show();
					}
					
					getDir(currentDir.getAbsolutePath());
					selectedList=null;
					savedData.setText(selectedList+"");
					chg();
				}else{
					Toast.makeText(ExAndroidActivity.this, "접근 권한이 없습니다.", Toast.LENGTH_SHORT).show();
				}
			}
		};
	}
//	rename click event
	private OnClickListener getClickListener_rename(){
		return new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if(selectedList.canWrite()){
					
					alert = new AlertDialog.Builder(v.getContext());
					input = new EditText(v.getContext());
					
					alert.setTitle(getString(R.string.rename_obj));
					// Set an EditText view to get user input
					
					input.setText(selectedList.getName());
					input.setSingleLine();
					alert.setView(input);

					alert.setPositiveButton(android.R.string.ok,
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog, int whichButton) {
									CharSequence newName = input.getText(); 
									
									Boolean renameResult = selectedList.renameTo(new File(selectedList.getParent()+File.separator+newName));
									
									if (renameResult){
										Toast.makeText(ExAndroidActivity.this, "Succesed", Toast.LENGTH_SHORT).show();
										getDir(currentDir.getAbsolutePath());
									}else{
										Toast.makeText(ExAndroidActivity.this, "Failed", Toast.LENGTH_SHORT).show();
									}
									selectedList=null;
									savedData.setText(selectedList+"");
									chg();
								}
							});

					alert.setNegativeButton(android.R.string.cancel,
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog, int whichButton) {

									dialog.dismiss();
								}
							});

					alert.show();
					
				}else{
					Toast.makeText(ExAndroidActivity.this, "접근 권한이 없습니다.", Toast.LENGTH_SHORT).show();
				}
			}
		};
	}
//	new folder click event
	private OnClickListener getClickListener_newfolder(){
		return new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				confirmCreateFolder();

			}
		};
	}
	
//	change View - Status bar (lay1:button, lay2:image)
	public void chg(){
		if(selectedList==null || selectedList.exists()){
			lay1.setVisibility(View.VISIBLE);
			lay2.setVisibility(View.INVISIBLE);
        } else  {
        	lay2.setVisibility(View.VISIBLE);
			lay1.setVisibility(View.INVISIBLE);
        }
	}
	
	public void getDir(String root1) {
		myRoute.setText("경로 : " + root1);
		currentDir = new File(root1);
		Where_root = root1;
		
		CustomDate item;
		
		mOrders.clear();
		ex_List.clear();
		ex_Route.clear();

		Log.d("ExAndroidActivity Log", "경로:"+root1);
		
		File f = new File(root1); // 받아 오는 매개변수를 경로삼아 새로운 파일을 만든다
		File[] files = f.listFiles(); // 실질적인 파일 목록을 불러 오는 함수 listFiles();

		if (!root1.equals(root)) { // 다음 경로로 이동 되었을경우 최상위 경로랑 다르다면

			item = new CustomDate("../", null);
			ex_Route.add(f.getParent());// 상위 경로를 조사 반환 하는 코드
			mOrders.add(item);	
		}

		
		for (int i = 0; i < files.length; i++) {
			File file = files[i];
			ex_Route.add(file.getPath());

			if (file.isDirectory()){
				ex_List.add(file.getName() + "/"); // 하위파일을 가진 디렉토리라면 /를 붙여
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
		d.setMessage("정말 종료하시겠습니까?");
		d.setPositiveButton("예", new DialogInterface.OnClickListener() {

			public void onClick(DialogInterface dialog, int which) {
				// process전체 종료
				finish();
			}
		});
		d.setNegativeButton("아니요", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				dialog.cancel();
			}
		});
		d.show();
		}
		else
			getDir(ex_Route.get(0));	
	}
	
	private int countSize(int _size){
//		double kil =(_size/1024);
//		double meg =(kil/1024);
		return _size;
	}
	
//	create new folder 
	private void confirmCreateFolder() {
		
		alert = new AlertDialog.Builder(this);
		input = new EditText(this);
		
		alert.setTitle(getString(R.string.create_folder));
		// Set an EditText view to get user input
//		final EditText input = new EditText(this);
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