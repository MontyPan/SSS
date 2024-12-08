package us.dontcareabout.sss.client.gf;

import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.Style;
import com.sencha.gxt.chart.client.draw.DrawComponent;
import com.sencha.gxt.chart.client.draw.sprite.RectangleSprite;
import com.sencha.gxt.core.client.dom.HasScrollSupport;
import com.sencha.gxt.core.client.dom.ScrollSupport.ScrollMode;
import com.sencha.gxt.core.client.util.Size;
import com.sencha.gxt.widget.core.client.button.TextButton;
import com.sencha.gxt.widget.core.client.container.FlowLayoutContainer;
import com.sencha.gxt.widget.core.client.container.HorizontalLayoutContainer;
import com.sencha.gxt.widget.core.client.container.HorizontalLayoutContainer.HorizontalLayoutData;
import com.sencha.gxt.widget.core.client.container.ResizeContainer;
import com.sencha.gxt.widget.core.client.container.VerticalLayoutContainer;
import com.sencha.gxt.widget.core.client.container.VerticalLayoutContainer.VerticalLayoutData;

import us.dontcareabout.gxt.client.util.ColorUtil;

/**
 * 內含四個 component，成「田」形 layout。
 * 右下角的 component 如果左右捲動，右上角的 component 會同步左右捲動；
 * 右下角的 component 如果上下捲動，左下角的 component 會同步上下捲動。
 */
public class SyncScrollContainer extends ResizeContainer {
	private static VerticalLayoutData v1x1 = new VerticalLayoutData(1, 1);
	private static HorizontalLayoutData h1x1 = new HorizontalLayoutData(1, 1);

	//TODO 由 caller 決定
	private int freezeW = 150;
	private int freezeH = 50;
	private int mainW = 800;
	private int mainH = 1200;

	private HorizontalLayoutContainer root = new HorizontalLayoutContainer();
	private VerticalLayoutContainer left = new VerticalLayoutContainer();
	private VerticalLayoutContainer right = new VerticalLayoutContainer();
	private FlowLayoutContainer upRight = new FlowLayoutContainer();
	private FlowLayoutContainer downLeft = new FlowLayoutContainer();
	private FlowLayoutContainer main = new FlowLayoutContainer();


	public SyncScrollContainer() {
		//from SimpleContainer()
		setElement(Document.get().createDivElement());

		//from SimpleContainer.setElement()
		getElement().getStyle().setOverflow(Style.Overflow.HIDDEN);

		//from SimpleContainer.setWidget()
		insert(root, 0);

		root.add(left, new HorizontalLayoutData(freezeW, 1));
		root.add(right, h1x1);

		VerticalLayoutData vld = new VerticalLayoutData(1, freezeH);
		left.add(new TextButton("上左"), vld);
		left.add(downLeft, v1x1);
		right.add(upRight, vld);
		right.add(main, v1x1);

		upRight.setScrollMode(ScrollMode.AUTOX);
		upRight.add(new BlockTest(mainW, freezeH));
		upRight.addScrollHandler(e -> syncPosition(upRight, main, true));

		downLeft.setScrollMode(ScrollMode.AUTOY);
		downLeft.add(new BlockTest(freezeW, mainH));
		downLeft.addScrollHandler(e -> syncPosition(downLeft, main, false));

		main.setScrollMode(ScrollMode.AUTO);
		main.add(new BlockTest(mainW, mainH));
		main.addScrollHandler(e -> {
			syncPosition(main, upRight, true);
			syncPosition(main, downLeft, false);
		});
	}

	@Override
	protected void doLayout() {
		//from SimpleContainer.doLayout()
		Size size = getContainerTarget().getStyleSize();

		int width = -1;

		if(!isAutoWidth()) { width = size.getWidth() - getLeftRightMargins(root); }

		int height = -1;

		if(!isAutoHeight()) { height = size.getHeight() - getTopBottomMargins(root); }

		applyLayout(root, width, height);
	}

	private static void syncPosition(HasScrollSupport source, HasScrollSupport target, boolean isHorizontal) {
		if (isHorizontal) {
			target.getScrollSupport().setHorizontalScrollPosition(
				source.getScrollSupport().getHorizontalScrollPosition()
			);
		} else {
			target.getScrollSupport().setVerticalScrollPosition(
				source.getScrollSupport().getVerticalScrollPosition()
			);
		}

	}

	//Delete 測試用
	class BlockTest extends DrawComponent {
		private int unit = 100;
		private int index = 0;

		BlockTest(int w, int h) {
			super(w, h);

			for (int i = 0; i <= w / unit; i++) {
				for (int i2 = 0; i2 <= h / unit; i2++) {
					this.addSprite(make(i, i2));
				}
			}
		}

		private RectangleSprite make(int i, int i2) {
			RectangleSprite result = new RectangleSprite(unit, unit, i * unit, i2 * unit);
			result.setFill(ColorUtil.differential(index++));
			result.setRadius(10);
			return result;
		}
	}
}