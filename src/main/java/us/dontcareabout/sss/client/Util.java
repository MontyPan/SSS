package us.dontcareabout.sss.client;

import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.user.client.Window;

import us.dontcareabout.sss.client.vo.WeekSchedule;

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

	/**
	 * @param isUp 是不是上學期
	 * @return 「X 年度第 Y 學期」的字串，格式為「X-Z」（上學期 Z=1、下學期 Z=2）
	 * 	對應 Google Sheet 的學期字串
	 */
	public static String semester(int year, boolean isUp) {
		return year + "-" + (isUp ? 1 : 2);
	}

	public static String toString(WeekSchedule data) {
		StringBuilder result = new StringBuilder(MMdd.format(data.getDate()) + "\n");

		for (int g = 1; g <= Util.MAX_GRADE; g++) {
			for (int s = 1; s <= Util.MAX_SERIAL; s++) {
				String className = Util.className(g, s);
				result.append(className + " " + data.getHost(g, s) + " : ");
				result.append("\n");
			}

			result.append("\n");
		}

		return result.substring(0, result.length() - 1);
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
