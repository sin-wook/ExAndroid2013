package rnd.fileexplorer2013.android;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

import android.net.Uri;
import android.os.Environment;
import android.util.Log;
import android.webkit.MimeTypeMap;

import org.apache.commons.io.FileUtils;

public final class Util {

	private static final String TAG = Util.class.getName();
	private static File COPIED_FILE = null;
	private static int pasteMode = 1;
	
	
	public static final int PASTE_MODE_COPY = 0;
	public static final int PASTE_MODE_MOVE = 1;
	
	
	
	
	private Util(){}
	
	 public static synchronized void setPasteSrcFile(File f, int mode) 
	  {  
	         COPIED_FILE = f;  
	         pasteMode = mode%2; 
	  }  

	 public static synchronized File getFileToPaste()
	 {
		 return COPIED_FILE;
	 }
	 
	 public static synchronized int getPasteMode()
	 {
		 return pasteMode;
	 }

	static boolean isMusic(File file) {

		Uri uri = Uri.fromFile(file);
		String type = MimeTypeMap.getSingleton().getMimeTypeFromExtension(MimeTypeMap.getFileExtensionFromUrl(uri.toString()));
		
		if(type == null)
			return false;
		else
		return (type.toLowerCase().startsWith("audio/"));

	}

	static boolean isVideo(File file) {

		Uri uri = Uri.fromFile(file);
		String type = MimeTypeMap.getSingleton().getMimeTypeFromExtension(MimeTypeMap.getFileExtensionFromUrl(uri.toString()));
		
		if(type == null)
			return false;
		else
		return (type.toLowerCase().startsWith("video/"));
	}

	public static boolean isPicture(File file) {
		
		Uri uri = Uri.fromFile(file);
		String type = MimeTypeMap.getSingleton().getMimeTypeFromExtension(MimeTypeMap.getFileExtensionFromUrl(uri.toString()));
		
		if(type == null)
			return false;
		else
		return (type.toLowerCase().startsWith("image/"));
	}
	
	public static boolean isProtected(File path)
	{
		return (!path.canRead() && !path.canWrite());
	}
	
	public static boolean isUnzippable(File path)
	{
		return (path.isFile() && path.canRead() && path.getName().endsWith(".zip"));
	}


	public static boolean isRoot(File dir) {
		
		return dir.getAbsolutePath().equals("/");
	}


	public static boolean isSdCard(File file) {
		
		try {
			return (file.getCanonicalPath().equals(Environment.getExternalStorageDirectory().getCanonicalPath()));
		} catch (IOException e) {
			return false;
		}
		
	}


	public static boolean mkDir(String canonicalPath, CharSequence newDirName) {
//		File newdir = new File("/newfolderTest");
	 	File newdir = new File(canonicalPath+File.separator+newDirName);
		Log.d("newDirAction_util",newdir+"");
		if(!newdir.exists()){
			Log.d("newDirAction_util_!exists",newdir+"");
			return newdir.mkdirs();
		}else{
			Log.d("newDirAction_util_exists",newdir+"");
			return false;
		}
		
	}

	public static boolean delete(File fileToBeDeleted) {

		try
		{
			FileUtils.forceDelete(fileToBeDeleted);
			return true;
		} catch (IOException e) {
			return false;
		}
	}

	public static boolean canPaste(File destDir) {
		
		if(getFileToPaste() == null)
		{
			return false;
		}
		if(getFileToPaste().isFile())
		{
			return true;
		}
		try
		{
			if(destDir.getCanonicalPath().startsWith(COPIED_FILE.getCanonicalPath()))
			{
				return false;
			}
			else
			{
				return true;
			}
		}
		catch (Exception e) {
			
			return false;
		}
	}



	public static Map<String, Long> getDirSizes(File dir)
	{
		Map<String, Long> sizes = new HashMap<String, Long>();
		
		try {
			
			Process du = Runtime.getRuntime().exec("/system/bin/du -b -d1 "+dir.getCanonicalPath(), new String[]{}, Environment.getRootDirectory());
			
			BufferedReader in = new BufferedReader(new InputStreamReader(
					du.getInputStream()));
			String line = null;
			while ((line = in.readLine()) != null)
			{
				String[] parts = line.split("\\s+");
				
				String sizeStr = parts[0];
				Long size = Long.parseLong(sizeStr);
				
				String path = parts[1];
				
				sizes.put(path, size);
			}
			
		} catch (IOException e) {
			Log.w(TAG, "Could not execute DU command for "+dir.getAbsolutePath(), e);
		}
		
		return sizes;
		
	}


	
	public int countSize(int _size){
//		double kil =(_size/1024);
//		double meg =(kil/1024);
		return _size;
	}
}
