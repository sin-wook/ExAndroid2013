package rnd.fileexplorer2013.android;


public class CustomDate {
	//경로
	private String test_Way;
	//사이즈
	private String test_Size;
	
	public CustomDate(String _Way, String _Size){
		test_Way = _Way;
		test_Size = _Size;
	}
	
	public String GetWay(){
		return test_Way;
	}
	public String GetSize(){
		return test_Size;
	}


	
}
