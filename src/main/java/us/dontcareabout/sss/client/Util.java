package us.dontcareabout.sss.client;

public class Util {
	//XXX 遙遠的未來應該抽出去變成 host page 提供？
	public static final int MAX_GRADE = 3;
	public static final int MAX_SERIAL = 4;

	/**
	 * @param grade 年級
	 * @param serial 班級序號
	 * @return 對應 Google Sheet 的班級字串
	 */
	public static String className(int grade, int serial) {
		return grade + "-" + serial;
	}

	/**
	 * 將字串複製到系統剪貼簿中
	 */
	public static native void copy(String text) /*-{
		$wnd.navigator.clipboard.writeText(text);
	}-*/;
}
