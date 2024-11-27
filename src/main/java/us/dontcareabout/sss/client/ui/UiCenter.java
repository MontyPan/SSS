package us.dontcareabout.sss.client.ui;

import java.util.Date;

import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.event.shared.SimpleEventBus;

import us.dontcareabout.gxt.client.util.PopUtil;
import us.dontcareabout.sss.client.Util;
import us.dontcareabout.sss.client.data.DataCenter;
import us.dontcareabout.sss.client.ui.event.ChangeNameEvent;
import us.dontcareabout.sss.client.ui.event.ChangeNameEvent.ChangeNameHandler;
import us.dontcareabout.sss.client.vo.Assignment;
import us.dontcareabout.sss.client.vo.Volunteer;
import us.dontcareabout.sss.client.vo.WeekSchedule;

public class UiCenter {
	private final static SimpleEventBus eventBus = new SimpleEventBus();

	private static AnnounceBoard announcePad = new AnnounceBoard();
	private static VolunteerBoard volunteerPad = new VolunteerBoard();

	public static void changeName(String name) {
		eventBus.fireEvent(new ChangeNameEvent(name));
	}

	public static HandlerRegistration addChangeName(ChangeNameHandler handler) {
		return eventBus.addHandler(ChangeNameEvent.TYPE, handler);
	}

	public static void showAnnounce() {
		String name = DataCenter.getUserData().getName();
		Volunteer v = DataCenter.volunteerMap.get(name);
		//TODO v 是 null 的處理
		Date now = new Date();
		for (Assignment t : v.assignmentList) {
			if (now.before(t.getDate())) {
				announcePad.refresh(
					name,
					t.getDate(),
					Util.className(t.getGrade(), t.getSerial())
				);
				PopUtil.showDialog(announcePad);
				return;
			}
		}
	}

	public static void showVolunteer(WeekSchedule data, int g, int s) {
		volunteerPad.refresh(data, g, s);
		PopUtil.showDialog(volunteerPad);
	}
}
