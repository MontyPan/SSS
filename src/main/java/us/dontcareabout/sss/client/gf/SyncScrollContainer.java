package us.dontcareabout.sss.client.gf;

import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.Style;
import com.google.gwt.user.client.ui.Widget;
import com.sencha.gxt.core.client.dom.HasScrollSupport;
import com.sencha.gxt.core.client.dom.ScrollSupport.ScrollMode;
import com.sencha.gxt.core.client.util.Size;
import com.sencha.gxt.widget.core.client.container.FlowLayoutContainer;
import com.sencha.gxt.widget.core.client.container.HorizontalLayoutContainer;
import com.sencha.gxt.widget.core.client.container.HorizontalLayoutContainer.HorizontalLayoutData;
import com.sencha.gxt.widget.core.client.container.ResizeContainer;
import com.sencha.gxt.widget.core.client.container.VerticalLayoutContainer;
import com.sencha.gxt.widget.core.client.container.VerticalLayoutContainer.VerticalLayoutData;

/**
 * 內含四個 widget，成「田」形 layout。各自名稱功能為：
 * <ul>
 * 	<li>左上角：fix。與捲動無關的 widget</li>
 * 	<li>右下角：main。同步捲動的主體 widget</li>
 *	<li>右上角：hScroll。若 <code>main</code> 水平捲動，此 widget 也會跟著水平捲動，反之亦然</li>
 *	<li>左下角：vScroll。若 <code>main</code> 垂直捲動，此 widget 也會跟著垂直捲動，反之亦然</li>
 * </ul>
 * <p>
 * 建立需要設定 <code>fix</code> 的大小，
 * 並以此決定 <code>hScroll</code> 的高度以及 <code>vScroll</code> 的寬度。
 */
public class SyncScrollContainer extends ResizeContainer {
	//Refactory 弄個 LayoutDataUtil 好了... Zzzz
	private static VerticalLayoutData v1x1 = new VerticalLayoutData(1, 1);
	private static HorizontalLayoutData h1x1 = new HorizontalLayoutData(1, 1);

	private HorizontalLayoutContainer root = new HorizontalLayoutContainer();
	private VerticalLayoutContainer left = new VerticalLayoutContainer();
	private VerticalLayoutContainer right = new VerticalLayoutContainer();
	private FlowLayoutContainer upRight = new FlowLayoutContainer();
	private FlowLayoutContainer downLeft = new FlowLayoutContainer();
	private FlowLayoutContainer main = new FlowLayoutContainer();

	/**
	 * 可使用 {@link Builder} 建立 instance。
	 */
	//為了 layout 的部份好寫（有加入先後順序問題），所以直接弄成 builder 方式來建立 instance。
	public SyncScrollContainer(
			int fixW, int fixH,
			Widget fixWidget, Widget mainWidget, Widget hScrollWidget, Widget vScrollWidget) {

		//from SimpleContainer()
		setElement(Document.get().createDivElement());

		//from SimpleContainer.setElement()
		getElement().getStyle().setOverflow(Style.Overflow.HIDDEN);

		//from SimpleContainer.setWidget()
		insert(root, 0);

		root.add(left, new HorizontalLayoutData(fixW, 1));
		root.add(right, h1x1);

		VerticalLayoutData vld = new VerticalLayoutData(1, fixH);
		left.add(fixWidget, vld);
		left.add(downLeft, v1x1);
		right.add(upRight, vld);
		right.add(main, v1x1);

		upRight.setScrollMode(ScrollMode.AUTO);
		upRight.add(hScrollWidget);
		upRight.addScrollHandler(e -> syncPosition(upRight, main, true));

		downLeft.setScrollMode(ScrollMode.AUTO);
		downLeft.add(vScrollWidget);
		downLeft.addScrollHandler(e -> syncPosition(downLeft, main, false));

		main.setScrollMode(ScrollMode.AUTO);
		main.add(mainWidget);
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

	public static final class Builder {
		private int fixW;
		private int fixH;
		private Widget fixWidget;
		private Widget mainWidget;
		private Widget hScrollWidget;
		private Widget vScrollWidget;

		public Builder fixSize(int w, int h) {
			fixW = w;
			fixH = h;
			return this;
		}

		public Builder fixWidget(Widget widget) {
			fixWidget = widget;
			return this;
		}

		public Builder mainWidget(Widget widget) {
			mainWidget = widget;
			return this;
		}

		public Builder hScrollWidget(Widget widget) {
			hScrollWidget = widget;
			return this;
		}

		public Builder vScrollWidget(Widget widget) {
			vScrollWidget = widget;
			return this;
		}

		public SyncScrollContainer build() {
			return new SyncScrollContainer(
				fixW, fixH,
				fixWidget, mainWidget, hScrollWidget, vScrollWidget
			);
		}
	}
}