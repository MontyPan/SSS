package us.dontcareabout.sss.client;

public class Util {
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
