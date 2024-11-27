package us.dontcareabout.sss.client.vo;

import java.util.Date;

import us.dontcareabout.sss.client.data.DataCenter;

/**
 * <code>進班</code> 資料。
 */
public class Assignment {
	private int grade;
	private int serial;
	private Date date;
	private String topic;

	//因為是第二級資料，所以假設 DataCenter 那邊都 ready
	public Assignment(int grade, int serial, Date date) {
		this.grade = grade;
		this.serial = serial;
		this.date = date;
		this.topic = DataCenter.findTopic(date, grade, serial);
	}

	public int getGrade() {
		return grade;
	}

	public void setGrade(int grade) {
		this.grade = grade;
	}

	public int getSerial() {
		return serial;
	}

	public void setSerial(int serial) {
		this.serial = serial;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public String getTopic() {
		return topic;
	}

	public void setTopic(String topic) {
		this.topic = topic;
	}
}
