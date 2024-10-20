package us.dontcareabout.sss.client;

import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.user.client.Window;

import us.dontcareabout.sss.client.vo.YS;

public class Util {
	/** 進班回報網址 */
	public static final String REPORT_URL = "https://forms.gle/EHkjEuxBt7gydQyt7";

	/** 志工時數填寫網址 */
	public static final String VOLUNTEER_HOUR_URL = "https://forms.gle/Kn4sJpnkUJDVsrLT6";

	//XXX 遙遠的未來應該抽出去變成 host page 提供？
	public static final int MAX_GRADE = 3;
	public static final int MAX_SERIAL = 4;

	public static final DateTimeFormat MMdd = DateTimeFormat.getFormat("MM/dd");

	/**
	 * @param grade 年級
	 * @param serial 班級序號
	 * @return 對應 Google Sheet 的班級字串
	 */
	public static String className(int grade, int serial) {
		return grade + "-" + serial;
	}

	public static String toString(YS ys) {
		return ys.year + "-" + (ys.semester ? 1 : 2);
	}

	public static void openUrl(String url) {
		Window.open(url, "_blank", null);
	}

	/**
	 * 將字串複製到系統剪貼簿中
	 */
	public static native void copy(String text) /*-{
		$wnd.navigator.clipboard.writeText(text);
	}-*/;
}
