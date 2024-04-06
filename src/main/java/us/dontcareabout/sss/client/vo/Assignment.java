package us.dontcareabout.sss.client.vo;

import java.util.Date;

/**
 * <pre>進班</pre> 資料。
 */
public class Assignment {
	public final int grade;
	public final int serial;
	public final Date date;

	private String topic;

	//這三項資料來自「學期班表 sheet」，所以直接 init
	//topic 來自「進班 sheet」，改成 getter / setter 後續再補上，程式會比較好寫
	public Assignment(int grade, int serial, Date date) {
		this.grade = grade;
		this.serial = serial;
		this.date = date;
	}

	public String getTopic() {
		return topic;
	}

	public void setTopic(String topic) {
		this.topic = topic;
	}
}
