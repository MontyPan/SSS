package us.dontcareabout.sss.client.ui;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Widget;
import com.sencha.gxt.chart.client.draw.Color;
import com.sencha.gxt.chart.client.draw.RGB;
import com.sencha.gxt.chart.client.draw.sprite.RectangleSprite;
import com.sencha.gxt.core.client.util.Margins;

import us.dontcareabout.gxt.client.draw.LRectangleSprite;
import us.dontcareabout.gxt.client.draw.LayerContainer;
import us.dontcareabout.gxt.client.draw.component.TextButton;
import us.dontcareabout.gxt.client.draw.layout.HorizontalLayoutLayer;
import us.dontcareabout.gxt.client.draw.layout.VerticalLayoutLayer;
import us.dontcareabout.sss.client.Util;
import us.dontcareabout.sss.client.data.DataCenter;
import us.dontcareabout.sss.client.gf.SyncScrollContainer;
import us.dontcareabout.sss.client.gf.SyncScrollContainer.Builder;
import us.dontcareabout.sss.client.vo.WeekSchedule;

public class SemesterSchedule implements IsWidget {
	private static final int rowHeight = 40;
	private static final int fixW = 72;
	private static final int blockW = 100;
	private static final int gap = 6;
	private static final Color bgColor = new RGB(235, 240, 235);

	private SyncScrollContainer instance;
	private MainLayer mainLayer = new MainLayer();
	private SemesterBtn semesterBtn = new SemesterBtn();
	private DateLayer dateLayer = new DateLayer();
	private ClassLayer classLayer = new ClassLayer();

	public SemesterSchedule() {
		instance = new Builder().fixSize(fixW, rowHeight)
			.fixWidget(semesterBtn)
			.mainWidget(mainLayer)
			.hScrollWidget(dateLayer)
			.vScrollWidget(classLayer)
			.build();
		DataCenter.addInitFinish(e -> refresh());
	}

	@Override
	public Widget asWidget() {
		return instance;
	}

	private void refresh() {
		mainLayer.refresh();
		semesterBtn.refresh();
		dateLayer.refresh();
		classLayer.refresh();

		int x = 0;
		Date now = new Date();

		for (WeekSchedule ws : DataCenter.weekScheduleList) {
			if (ws.getDate().after(now)) { break; }
			x += blockW + gap;
		}

		instance.setHPosition(x);
		mainLayer.setProgress(x);
		dateLayer.setProgress(x);
	}

	class MainLayer extends LayerContainer {
		List<WeekColumn> weekClmn = new ArrayList<>();
		RectangleSprite progress = new RectangleSprite();
		HorizontalLayoutLayer root = new HorizontalLayoutLayer();

		public MainLayer() {
			root.setMargins(gap);
			root.setGap(gap);
			root.setLZIndex(100);
			addLayer(root);

			progress.setFill(bgColor);
			addSprite(progress);

			UiCenter.addChangeName(e -> {
				weekClmn.stream().forEach(wc -> wc.changeName(e.data));
			});
		}

		void setProgress(int width) {
			progress.setWidth(width + gap / 2);
			redrawSurface();
		}

		void refresh() {
			weekClmn.clear();
			for (WeekSchedule e : DataCenter.weekScheduleList) {
				weekClmn.add(new WeekColumn(e));
			}

			root.clear();
			weekClmn.stream().forEach(wc -> root.addChild(wc, blockW));
			root.redeploy();

			//XXX 由 root 決定自身大小會出現的程式碼，有沒有辦法不用每次搞這些呢？
			root.resize(1, Integer.MAX_VALUE);	//XXX 數值給太小，裡頭的 child 可能不會觸發 adjustMember()
			//weekClmn 沒有算到 root 的 margin
			setPixelSize((int)root.getViewSize(), (int)weekClmn.get(0).getViewSize() + gap);
			progress.setHeight(getOffsetHeight());
		}
	}

	class SemesterBtn extends LayerContainer {
		TextButton btn = new TextButton();

		SemesterBtn() {
			btn.setBgColor(bgColor);
			addLayer(btn);
		}

		void refresh() {
			btn.setText(Util.toString(DataCenter.nowYS));
			redrawSurface();
		}

		@Override
		protected void adjustMember(int width, int height) {
			btn.resize(width, height);
		}
	}

	class DateLayer extends LayerContainer {
		RectangleSprite progress = new RectangleSprite();
		HorizontalLayoutLayer root = new HorizontalLayoutLayer();

		public DateLayer() {
			root.setMargins(new Margins(0, gap, 0, gap));
			root.setGap(gap);
			root.setLZIndex(100);
			addLayer(root);

			progress.setFill(bgColor);
			addSprite(progress);
		}

		void setProgress(int width) {
			progress.setWidth(width + gap / 2);
			redrawSurface();
		}

		void refresh() {
			root.clear();

			DataCenter.weekScheduleList.forEach(data -> {
				TextButton date = new TextButton(Util.MMdd.format(data.getDate()));
				root.addChild(date, blockW);
				date.addSpriteSelectionHandler(e -> copyWeekText(data));
			});

			root.redeploy();
			root.resize(1, rowHeight);
			setPixelSize((int)root.getViewSize(), rowHeight);
			progress.setHeight(getOffsetHeight());
		}
	}

	class ClassLayer extends LayerContainer {
		//XXX ClassColumn 是固定的，根本不用 refresh
		//但是寫在 c'tor 裡頭，TextButton 的字不會調整，所以暫時就按照 DateLayer 一樣搞一遍 Orz
		void refresh() {
			this.clear();
			ClassColumn classClmn = new ClassColumn();
			addLayer(classClmn);
			classClmn.resize(fixW, 1);
			setPixelSize(fixW, (int)classClmn.getViewSize());
		}
	}

	class WeekColumn extends BasicColumn {
		List<Block> blockList = new ArrayList<>();

		WeekColumn(WeekSchedule data) {
			for (int g = 1; g <= Util.MAX_GRADE; g++) {
				for (int s = 1; s <= Util.MAX_SERIAL; s++) {
					blockList.add(new Block(data, g, s));
				}
			}

			blockList.stream().forEach(b -> addText(b));
		}

		void changeName(String name) {
			blockList.stream().forEach(
				b -> {
					if (name.equals(b.getText())) {
						b.setBgColor(RGB.BLACK);
						b.setTextColor(RGB.WHITE);
					} else {
						b.setBgColor(RGB.LIGHTGRAY);
						b.setTextColor(RGB.BLACK);
					}
					redraw();
				}
			);
		}
	}

	class ClassColumn extends BasicColumn {
		ClassColumn() {
			setMargins(gap);

			for (int g = 1; g <= Util.MAX_GRADE; g++) {
				for (int s = 1; s <= Util.MAX_SERIAL; s++) {
					addText(new ClassBtn(g, s));
				}
			}
		}
	}

	class ClassBtn extends TextButton {
		ClassBtn(int g, int s) {
			super(Util.className(g, s));
			addSpriteSelectionHandler(e -> UiCenter.showClass(g, s));
		}
	}

	class BasicColumn extends VerticalLayoutLayer {
		BasicColumn() {
			setGap(gap);
		}

		void addText(TextButton tb) {
			addChild(tb, rowHeight);
		}
	}

	class Block extends TextButton {
		LRectangleSprite topic = new LRectangleSprite(80, 2);

		Block(WeekSchedule data, int g, int s) {
			setBgColor(RGB.LIGHTGRAY);
			setBgRadius(5);

			setText(data.getHost(g, s));
			addSpriteSelectionHandler(e -> UiCenter.showVolunteer(data, g, s));

			if(DataCenter.findTopic(data.getDate(), g, s).isEmpty()) { return; }

			topic.setFill(RGB.RED);
			topic.setLX(10);
			topic.setLY(36);
			add(topic);
		}
	}

	/**
	 * 把當週的進班名單跟進班主題（如果有）組成字串，並複製到剪貼簿中
	 */
	private static void copyWeekText(WeekSchedule data) {
		StringBuilder result = new StringBuilder(Util.MMdd.format(data.getDate()) + "\n");

		for (int g = 1; g <= Util.MAX_GRADE; g++) {
			for (int s = 1; s <= Util.MAX_SERIAL; s++) {
				String className = Util.className(g, s);
				result.append(className + " " + data.getHost(g, s) + " : ");
				result.append(DataCenter.findTopic(data.getDate(), g, s));
				result.append("\n");
			}

			result.append("\n");
		}

		Util.copy(result.substring(0, result.length() - 1));
	}
}