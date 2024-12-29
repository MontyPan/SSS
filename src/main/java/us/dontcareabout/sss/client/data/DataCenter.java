package us.dontcareabout.sss.client.data;

import java.util.Date;
import java.util.HashMap;
import java.util.List;

import com.github.nmorel.gwtjackson.client.ObjectMapper;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.event.shared.SimpleEventBus;

import us.dontcareabout.gst.client.data.ApiKey;
import us.dontcareabout.gst.client.data.SheetIdDao;
import us.dontcareabout.gwt.client.google.sheet.Sheet;
import us.dontcareabout.gwt.client.google.sheet.SheetDto;
import us.dontcareabout.gwt.client.google.sheet.SheetDto.Callback;
import us.dontcareabout.sss.client.Util;
import us.dontcareabout.sss.client.data.event.InitFinishEvent;
import us.dontcareabout.sss.client.data.event.InitFinishEvent.InitFinishHandler;
import us.dontcareabout.sss.client.data.event.RecordReadyEvent;
import us.dontcareabout.sss.client.data.event.RecordReadyEvent.RecordReadyHandler;
import us.dontcareabout.sss.client.data.event.ScheduleReadyEvent;
import us.dontcareabout.sss.client.data.event.ScheduleReadyEvent.ScheduleReadyHandler;
import us.dontcareabout.sss.client.gf.StorageDao;
import us.dontcareabout.sss.client.gf.TaskSet;
import us.dontcareabout.sss.client.vo.Assignment;
import us.dontcareabout.sss.client.vo.Record;
import us.dontcareabout.sss.client.vo.UserData;
import us.dontcareabout.sss.client.vo.Volunteer;
import us.dontcareabout.sss.client.vo.WeekSchedule;
import us.dontcareabout.sss.client.vo.YS;

public class DataCenter {
	private final static SimpleEventBus eventBus = new SimpleEventBus();
	private final static StorageDao<UserData> userDataDao = new StorageDao<>("SSS-UserData", GWT.create(UserDataMapper.class));

	private static void loadError() {
		//TODO
	}

	////////////////

	// ==== 原始資料區 ==== //
	private static UserData userData;
	public static YS nowYS;
	public static List<WeekSchedule> weekScheduleList;
	public static List<Record> recordList;
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

	////////////////

	/**
	 * 索取一學期的完整資料（含 {@link WeekSchedule} 與 {@link Record}）
	 */
	public static void wantYS(YS ys) {
		nowYS = ys;
		TaskSet ts = new TaskSet();
		ts.addAsyncTask(
			() -> wantSchedule(ys),
			addScheduleReady(e -> ts.check())
		).addAsyncTask(
			() -> wantRecord(),
			addRecordReady(e -> ts.check())
		).addFinalTask(() -> wantYSReadyProcess())
		.start();
	}

	private static void wantYSReadyProcess() {
		buildVolunteerMap();
		eventBus.fireEvent(new InitFinishEvent());
	}

	public static void wantSchedule(YS ys) {
		new SheetDto<WeekSchedule>().key(ApiKey.jsValue())
				.sheetId(SheetIdDao.priorityValue()).tabName(Util.toString(ys))
				.fetch(
			new Callback<WeekSchedule>() {
				@Override
				public void onSuccess(Sheet<WeekSchedule> gs) {
					weekScheduleList = gs.getRows();
					eventBus.fireEvent(new ScheduleReadyEvent());
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

	public static void wantRecord() {
		new SheetDto<Record>().key(ApiKey.jsValue())
				.sheetId(SheetIdDao.priorityValue()).tabName("進班回報")
				.fetch(
			new Callback<Record>() {
				@Override
				public void onSuccess(Sheet<Record> gs) {
					recordList = gs.getRows();
					eventBus.fireEvent(new RecordReadyEvent());
				}

				@Override
				public void onError(Throwable exception) {
					loadError();
				}
			}
		);
	}

	public static HandlerRegistration addRecordReady(RecordReadyHandler handler) {
		return eventBus.addHandler(RecordReadyEvent.TYPE, handler);
	}

	public static HandlerRegistration addInitFinish(InitFinishHandler handler) {
		return eventBus.addHandler(InitFinishEvent.TYPE, handler);
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

	////////////////

	public static String findTopic(Date date, int g, int s) {
		String result = "";

		for (Record r : DataCenter.recordList) {
			if (r.getGrade() == g && r.getSerial() == s && r.getDate().equals(date)) {
				result = r.getTopic();
				break;
			}
		}

		return result;
	}

	////////////////
	interface UserDataMapper extends ObjectMapper<UserData> {}
}
