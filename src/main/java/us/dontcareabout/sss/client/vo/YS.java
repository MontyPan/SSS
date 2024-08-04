package us.dontcareabout.sss.client.vo;

import java.util.Date;

import com.sencha.gxt.core.client.util.DateWrapper;

import us.dontcareabout.gwt.client.Console;
import us.dontcareabout.sss.client.Util;

/**
 * 學年（school "Y"ear）跟學期（"S"emester）的 VO。
 * 因為實在很難取名字，乾脆就擺爛用縮寫... [被毆飛]
 *
 * <ul>
 * 	<li>X 學年度上學期：X 年 8 月到 X + 1 年 1 月</li>
 * 	<li>X 學年度下學期：X + 1 年 2 月到 X + 1 年 7 月</li>
 * </ul>
 *
 * <b>注意：</b>client only
 */
public class YS {
	public final int year;

	/** true 為上學期、false 為下學期 */
	public final boolean semester;

	public final Date date;

	public YS() { this(new Date()); }

	public YS(Date date) { this(new DateWrapper(date)); }

	//用 DateWrapper 主要是想省掉 deprecated... Zzzz
	public YS(DateWrapper date) {
		this.date = date.asDate();
		int month = date.getMonth() + 1;	//改成直覺的月份數字

		//1～8 月是下學期比較簡單先做
		if (month > 1 && month < 8) {
			year = date.getFullYear() - 1912;
			semester = false;
			return;
		}

		int offset = month == 1 ? 1912 : 1911;
		year =  date.getFullYear() - offset;
		semester = true;
	}

	//// 懶得用 client side test 的測試區... ////
	public static void test() {
		Console.log(test(2024, 8, 1).equals("113-1"));
		Console.log(test(2025, 1, 31).equals("113-1"));
		Console.log(test(2025, 2, 1).equals("113-2"));
		Console.log(test(2025, 7, 31).equals("113-2"));
	}

	/**
	 * @param month 直覺的月份數字
	 */
	private static String test(int year, int month, int day) {
		return Util.toString(new YS(new DateWrapper(year, month - 1, day)));
	}
}
