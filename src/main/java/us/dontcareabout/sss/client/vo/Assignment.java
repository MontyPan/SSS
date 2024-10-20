package us.dontcareabout.sss.client.vo;

import java.util.Date;

import us.dontcareabout.sss.client.data.DataCenter;

/**
 * <code>進班</code> 資料。
 */
public class Assignment {
	public final int grade;
	public final int serial;
	public final Date date;
	public final String topic;

	//因為是第二級資料，所以假設 DataCenter 那邊都 ready
	public Assignment(int grade, int serial, Date date) {
		this.grade = grade;
		this.serial = serial;
		this.date = date;
		this.topic = DataCenter.findTopic(date, grade, serial);
	}
}
