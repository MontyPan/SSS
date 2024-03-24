package us.dontcareabout.sss.client.vo;

import java.util.Date;

import us.dontcareabout.gwt.client.google.sheet.Row;
import us.dontcareabout.sss.client.Util;

public final class WeekSchedule extends Row {
	protected WeekSchedule() {}

	public Date getDate() {
		return dateField("日期");
	}

	public String getHost(int grade, int serial) {
		return stringField(Util.className(grade, serial));
	}
}
