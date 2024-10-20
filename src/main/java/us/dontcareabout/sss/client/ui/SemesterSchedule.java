package us.dontcareabout.sss.client.ui;

import java.util.ArrayList;
import java.util.List;

import com.sencha.gxt.chart.client.draw.RGB;

import us.dontcareabout.gxt.client.draw.LRectangleSprite;
import us.dontcareabout.gxt.client.draw.LayerContainer;
import us.dontcareabout.gxt.client.draw.component.TextButton;
import us.dontcareabout.gxt.client.draw.layout.HorizontalLayoutLayer;
import us.dontcareabout.gxt.client.draw.layout.VerticalLayoutLayer;
import us.dontcareabout.sss.client.Util;
import us.dontcareabout.sss.client.data.DataCenter;
import us.dontcareabout.sss.client.vo.WeekSchedule;

public class SemesterSchedule extends LayerContainer {
	private static final int rowHeight = 40;

	private HorizontalLayoutLayer root = new HorizontalLayoutLayer();
	private List<WeekColumn> weekClmn = new ArrayList<>();

	public SemesterSchedule() {
		root.setMargins(5);
		root.setGap(5);
		addLayer(root);
		DataCenter.addInitFinish(e -> refresh());
		UiCenter.addChangeName(e -> {
			weekClmn.stream().forEach(wc -> wc.changeName(e.data));
		});

	}

	//XXX 用 root 決定大小，不用 override adjustMember()

	private void refresh() {
		ClassColumn staticCol = new ClassColumn();

		weekClmn.clear();
		for (WeekSchedule e : DataCenter.weekScheduleList) {
			weekClmn.add(new WeekColumn(e));
		}

		root.clear();
		root.addChild(staticCol, 72);
		weekClmn.stream().forEach(wc -> root.addChild(wc, 100));

		root.redeploy();

		//XXX 由 root 決定自身大小會出現的程式碼，有沒有辦法不用每次搞這些呢？
		root.resize(1, Integer.MAX_VALUE);	//XXX 數值給太小，裡頭的 child 可能不會觸發 adjustMember()
		setPixelSize((int)root.getViewSize(), (int)staticCol.getViewSize());
	}

	class WeekColumn extends BasicColumn {
		List<Block> blockList = new ArrayList<>();

		WeekColumn(WeekSchedule data) {
			super(Util.MMdd.format(data.getDate()));

			for (int g = 1; g <= Util.MAX_GRADE; g++) {
				for (int s = 1; s <= Util.MAX_SERIAL; s++) {
					blockList.add(
						new Block(
							data.getHost(g, s),
							!DataCenter.findTopic(data.getDate(), g, s).isEmpty()
						)
					);
				}
			}

			blockList.stream().forEach(b -> addChild(b, rowHeight));

			headerTB.addSpriteSelectionHandler(e -> copyWeekText(data));
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
			super("");
			for (int g = 1; g <= Util.MAX_GRADE; g++) {
				for (int s = 1; s <= Util.MAX_SERIAL; s++) {
					addChild(new TextButton(Util.className(g, s)), rowHeight);
				}
			}
		}
	}

	/**
	 * 主要負責決定 header
	 */
	class BasicColumn extends VerticalLayoutLayer {
		TextButton headerTB;

		BasicColumn(String header) {
			setGap(5);
			headerTB = new TextButton(header);
			addChild(headerTB, rowHeight);
		}
	}

	class Block extends TextButton {
		LRectangleSprite topic = new LRectangleSprite(80, 2);

		Block(String string, boolean hasTopic) {
			this();
			setText(string);

			if(!hasTopic) { return; }

			topic.setFill(RGB.RED);
			topic.setLX(10);
			topic.setLY(36);
			add(topic);
		}

		Block() {
			setBgColor(RGB.LIGHTGRAY);
			setBgRadius(5);
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
