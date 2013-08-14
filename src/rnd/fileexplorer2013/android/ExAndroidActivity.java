package rnd.fileexplorer2013.android;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.filefilter.FileFilterUtils;

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
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class ExAndroidActivity extends ListActivity {
	ArrayList<CustomDate> mOrders = new ArrayList<CustomDate>();

	CustomAdapter mAdapter;
	ListView mList;
	

//	------------- button ---------------------------------
	private Button delBT;		// 삭제 버튼
	private Button renameBT;	// 이름변경
	private Button copyBT;		// 복사
	private Button moveBT;		// 이동
	private Button bt_newfolder;
	
	File copy_root;
	private File selectedList = null;	// 선택된 리스트
	private int Fmode = 0;	// 0:null, 1:copy&paste
	
	//이름변경
	AlertDialog.Builder alert;
	EditText input;
	private TextView savedData;	// 저장된 데이터 참조
	//----------------------------------------------------------
	
	private List<String> ex_List = new ArrayList<String>();
	private List<String> ex_Route = new ArrayList<String>();
	final private String root = "/"; // 초기 시작 경로, 하지만 루팅이 안되기 때문에 다른곳은 접ㄱ느이 불가하다
	private String Where_root = "/";
	private TextView myRoute; // 현재 경로를 저장해주는 변수
	private File currentDir; // 현재 경로
	
	// --------------------프레임 메뉴얼--------------------------
	private View lay1;
	private View lay2;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.customlist);
		// FileOI fio = new FileOI();

		ConnectionArea();
		ButtonArea();
		getDir(root);
		
		
		mList = getListView();
		mList.setAdapter(mAdapter);
		mList.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> a, View v, int position,
					long id) {
				Where_root = ex_Route.get(position);
				File file = new File(ex_Route.get(position));
				if (file.isDirectory()) {
					if (file.canRead()) {
						copy_root = file;
						getDir(ex_Route.get(position));
					} else {
						Toast.makeText(ExAndroidActivity.this, "접근 권한이 없습니다",
								Toast.LENGTH_SHORT).show();
					}
				} else {
					Toast.makeText(ExAndroidActivity.this, file.getName(),
							Toast.LENGTH_SHORT).show();
				}
			}

		});
		
		mList.setOnItemLongClickListener(getLongClickListener());
		setListAdapter(mAdapter);
	}

	private void ConnectionArea() {
		lay1 = findViewById(R.id.test1);
		lay2 = findViewById(R.id.test2);
		delBT = (Button) findViewById(R.id.del);
		moveBT = (Button) findViewById(R.id.move);
		copyBT = (Button) findViewById(R.id.copy);
		renameBT = (Button) findViewById(R.id.rename);
		myRoute = (TextView) findViewById(R.id.Route);
		bt_newfolder = (Button)findViewById(R.id.menu_newfolder);
		
	}

	private void ButtonArea() {
		bt_newfolder.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				confirmCreateFolder();
			}
		});
		
		copyBT.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				
				if(Fmode != 1){
					
					Fmode = 1;
					Log.d("*** copy ***", "Fmode:"+Fmode+", selectedList:"+selectedList);
					
				}else if(Fmode == 1){
					Log.d("*** copy ***", "Fmode:"+Fmode+", selectedList:"+selectedList);
					if (selectedList != null && selectedList.isDirectory()) {
						if (selectedList.canRead()) {
							
							Util.mkDir(currentDir.getAbsolutePath(), selectedList.getName());
	
							File file = new File(currentDir.getAbsolutePath() + "/" + selectedList.getName());
							try {
								Util.Copy_dir(selectedList, file);
							} catch (IOException e) {
								e.printStackTrace();
							}
							
						} else {
							Toast.makeText(ExAndroidActivity.this, "접근 권한이 없습니다",Toast.LENGTH_SHORT).show();
						}
	
					} else if(selectedList != null && selectedList.isFile()) {
	
						File file = new File(currentDir.getAbsolutePath());
						try {
							Util.Copy_file(selectedList, file);
						} catch (IOException e) {
	
							Toast.makeText(ExAndroidActivity.this, "문제발생",Toast.LENGTH_SHORT).show();
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						
					}else{
						Toast.makeText(ExAndroidActivity.this, "파일불명",Toast.LENGTH_SHORT).show();
					}
					
					selectedList=null;
					chg();
					Fmode = 0;
				}
				
				copy_root = null;

				getDir(currentDir.getAbsolutePath());
				
			}
		});
		moveBT.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Toast.makeText(
						ExAndroidActivity.this,	selectedList.getAbsolutePath() + " ** "
								+ copy_root.getAbsolutePath(), Toast.LENGTH_SHORT)
						.show();
			}
		});
		
		renameBT.setOnClickListener(new OnClickListener() {
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
									chg();
								}
							});

					alert.setNegativeButton(android.R.string.cancel,
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog, int whichButton) {

									dialog.dismiss();
									selectedList=null;
									chg();
								
								}
							});

					alert.show();
					
				}else{
					Toast.makeText(ExAndroidActivity.this, "접근 권한이 없습니다.", Toast.LENGTH_SHORT).show();
				}
			}
		});
		delBT.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
//				Delete selected to file or folder
				if(selectedList.canWrite()){
					if(selectedList.isDirectory()){
						try {
							FileUtils.deleteDirectory(selectedList);
						} catch (IOException e) {
							Toast.makeText(ExAndroidActivity.this, "폴더 삭제 오류", Toast.LENGTH_SHORT).show();
						}
					}else if(selectedList.isFile()){
						FileUtils.deleteQuietly(selectedList);
					}
					
					getDir(currentDir.getAbsolutePath());
					
				}else{
					Toast.makeText(ExAndroidActivity.this, "접근 권한이 없습니다.", Toast.LENGTH_SHORT).show();
				}
				selectedList=null;
				chg();
			}
		});

	}

	// Long Click Event : return method
	private OnItemLongClickListener getLongClickListener() {
		return new OnItemLongClickListener() {

			@Override
			public boolean onItemLongClick(AdapterView<?> a, View v,
					int position, long id) {
				Where_root = ex_Route.get(position);
				selectedList = new File(ex_Route.get(position));
				
				if (selectedList.canRead()) {
					
				} else {
					Toast.makeText(ExAndroidActivity.this, "접근 권한이 없습니다",Toast.LENGTH_SHORT).show();
					selectedList = null;
				}
				
				chg();
				return true;
			}
		};

	}

//	하단바 (lay1 : Button item, lay2 : image)
	public void chg() {
		if (selectedList == null) {
			lay1.setVisibility(View.INVISIBLE);
			lay2.setVisibility(View.VISIBLE);
		} else {
			lay2.setVisibility(View.INVISIBLE);
			lay1.setVisibility(View.VISIBLE);
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

		Log.d("ExAndroidActivity Log", "경로:" + root1);

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

			if (file.isDirectory()) {
				ex_List.add(file.getName() + "/"); // 하위파일을 가진 디렉토리라면 /를 붙여
				item = new CustomDate(ex_List.get(i), "");
			} else {
				ex_List.add(file.getName());
				item = new CustomDate(ex_List.get(i), countSize(ex_Route.get(i)
						.length()) + " byte");
			}
			mOrders.add(item);

		}

		mAdapter = new CustomAdapter(this, R.layout.customlistitem, mOrders);
		mAdapter.notifyDataSetChanged();
		setListAdapter(mAdapter);

	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		Toast.makeText(ExAndroidActivity.this, "onTouchEvent",
				Toast.LENGTH_SHORT).show();
		return super.onTouchEvent(event);
	}

	public void onBackPressed() {

		if (Where_root.equals(root)) {
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
		} else
			getDir(ex_Route.get(0));
	}

	public int countSize(int _size) {
		// double kil =(_size/1024);
		// double meg =(kil/1024);
		return _size;
	}

	// create new folder
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
						Boolean makeFResult = Util.mkDir(
								currentDir.getAbsolutePath(), newDir);

						if (makeFResult) {
							Toast.makeText(ExAndroidActivity.this,
									"created Folder", Toast.LENGTH_SHORT)
									.show();
							getDir(currentDir.getAbsolutePath());
						} else {
							Toast.makeText(ExAndroidActivity.this,
									"Failed Folder", Toast.LENGTH_SHORT).show();
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