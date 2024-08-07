package us.dontcareabout.sss.client.data;

import java.util.HashMap;
import java.util.List;

import com.github.nmorel.gwtjackson.client.ObjectMapper;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.event.shared.SimpleEventBus;

import us.dontcareabout.gst.client.data.ApiKey;
import us.dontcareabout.gwt.client.google.sheet.Sheet;
import us.dontcareabout.gwt.client.google.sheet.SheetDto;
import us.dontcareabout.gwt.client.google.sheet.SheetDto.Callback;
import us.dontcareabout.sss.client.Util;
import us.dontcareabout.sss.client.data.event.ScheduleReadyEvent;
import us.dontcareabout.sss.client.data.event.ScheduleReadyEvent.ScheduleReadyHandler;
import us.dontcareabout.sss.client.gf.StorageDao;
import us.dontcareabout.sss.client.ui.UiCenter;
import us.dontcareabout.sss.client.vo.Assignment;
import us.dontcareabout.sss.client.vo.UserData;
import us.dontcareabout.sss.client.vo.Volunteer;
import us.dontcareabout.sss.client.vo.WeekSchedule;

public class DataCenter {
	private final static SimpleEventBus eventBus = new SimpleEventBus();
	private final static StorageDao<UserData> userDataDao = new StorageDao<>("SSS-UserData", GWT.create(UserDataMapper.class));

	private static void loadError() {
		//TODO
	}

	////////////////

	// ==== 原始資料區 ==== //
	private static UserData userData;
	public static List<WeekSchedule> weekScheduleList;
	// ======== //

	// ==== 二級資料區 ==== //
	/** 志工狀態 */
	public static HashMap<String, Volunteer> volunteerMap;
	// ======== //

	public static UserData getUserData() {
		if (userData == null) {
			userData = userDataDao.retrieve();
		}

		if (userData == null) {
			userData = new UserData();
			userData.setName("靜玉");	//XXX magic number
			userDataDao.store(userData);
		}

		return userData;
	}

	public static void saveUserData() {
		userDataDao.store(userData);
	}

	public static void wantSchedule(String sheetId, int year, boolean isUp) {
		new SheetDto<WeekSchedule>().key(ApiKey.jsValue())
				.sheetId(sheetId).tabName(Util.semester(year, isUp))
				.fetch(
			new Callback<WeekSchedule>() {
				@Override
				public void onSuccess(Sheet<WeekSchedule> gs) {
					weekScheduleList = gs.getRows();
					buildVolunteerMap();
					eventBus.fireEvent(new ScheduleReadyEvent());
					UiCenter.showAnnounce();
					UiCenter.changeName(getUserData().getName());
				}

				@Override
				public void onError(Throwable exception) {
					loadError();
				}
		});
	}

	public static HandlerRegistration addScheduleReady(ScheduleReadyHandler handler) {
		return eventBus.addHandler(ScheduleReadyEvent.TYPE, handler);
	}

	////////////////

	private static void buildVolunteerMap() {
		volunteerMap = new HashMap<>();

		for (WeekSchedule ws : weekScheduleList) {
			for (int g = 1; g <= Util.MAX_GRADE; g++) {
				for (int s = 1; s <= Util.MAX_SERIAL; s++) {
					String name = ws.getHost(g, s);

					if(name.isEmpty()) { continue; }

					Volunteer v = volunteerMap.get(name);

					if (v == null) {
						v = new Volunteer(name);
						volunteerMap.put(name, v);
					}

					v.assignmentList.add(new Assignment(g, s, ws.getDate()));
				}
			}
		}
	}

	interface UserDataMapper extends ObjectMapper<UserData> {}
}
