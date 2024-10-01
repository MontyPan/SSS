package us.dontcareabout.sss.client.vo;

import java.util.Date;

import us.dontcareabout.gwt.client.google.sheet.Row;

/**
 * <code>進班</code>主題的紀錄
 * <p>
 * <b>注意</b>：{@link #getGrade()} 與 {@link #getSerial()} 的處理邏輯
 * 與 Google Form 的選項字串值相依。
 */
public final class Record extends Row {
	protected Record() {}

	public Date getDate() {
		return dateField("日期");
	}

	public int getGrade() {
		switch(stringField("年級")) {
		case "一年級": return 1;
		case "二年級": return 2;
		case "三年級": return 3;
		}

		return 0;
	}

	public int getSerial() {
		switch(stringField("班級")) {
		case "一班": return 1;
		case "二班": return 2;
		case "三班": return 3;
		case "四班": return 4;
		}

		return 0;
	}

	public String getTopic() {
		return stringField("主題");
	}
}
