package us.dontcareabout.sss.client.ui;

import com.google.gwt.resources.client.ImageResource;
import com.sencha.gxt.chart.client.draw.RGB;
import com.sencha.gxt.core.client.util.Margins;

import us.dontcareabout.gxt.client.component.RwdRootPanel;
import us.dontcareabout.gxt.client.draw.LImageSprite;
import us.dontcareabout.gxt.client.draw.LayerContainer;
import us.dontcareabout.gxt.client.draw.LayerSprite;
import us.dontcareabout.gxt.client.draw.component.TextButton;
import us.dontcareabout.gxt.client.draw.layout.HorizontalLayoutLayer;
import us.dontcareabout.gxt.client.draw.layout.VerticalLayoutLayer;
import us.dontcareabout.gxt.client.util.PopUtil;
import us.dontcareabout.sss.client.ImageRS;
import us.dontcareabout.sss.client.Util;
import us.dontcareabout.sss.client.data.DataCenter;
import us.dontcareabout.sss.client.vo.UserData;

public class UserPanel extends LayerContainer {
	public static final int M_HEIGHT = 90;

	private static final RGB BG = RGB.LIGHTGRAY;
	private static final int BgRadius = 5;
	private static final int margin = 5;
	private static final int iconSize = 80;

	private UserData userData = DataCenter.getUserData();

	private HorizontalLayoutLayer root = new HorizontalLayoutLayer();
	private NameBlock nameBlock = new NameBlock();
	private ImgBlock searchBlock = new ImgBlock(ImageRS.I.search());
	private TextIconBlock reportBlock = new TextIconBlock("進班回報", ImageRS.I.calendarCheck());
	private TextIconBlock volunteerBlock = new TextIconBlock("志工時數", ImageRS.I.pencilSquare());

	private VolunteerSelector volunteerSelector = new VolunteerSelector();

	public UserPanel() {
		root.setMargins(margin);
		root.setGap(12);

		searchBlock.addSpriteSelectionHandler(e -> {
			PopUtil.dialog.setDraggable(false);//Refactory 移到 GF 去
			PopUtil.showDialog(volunteerSelector, 200, 500);
		});

		reportBlock.addSpriteSelectionHandler(e -> Util.openUrl(Util.REPORT_URL));
		volunteerBlock.addSpriteSelectionHandler(e -> Util.openUrl(Util.VOLUNTEER_HOUR_URL));

		//Refactory RwdRootPanel.getDeviceType() 改善
		//TODO 動態調整
		if (RwdRootPanel.getWidth() > RwdRootPanel.WIDTH_DEMARCATION[RwdRootPanel.DEVICE_MOBILE_L]) {
			root.addChild(searchBlock, iconSize);
			root.addChild(nameBlock, 0.4);
			root.addChild(reportBlock, 0.3);
			root.addChild(volunteerBlock, 0.3);
		} else {
			root.addChild(searchBlock, 50);
			root.addChild(nameBlock, 1);
			root.addChild(reportBlock, 80);
			root.addChild(volunteerBlock, 80);
		}

		addLayer(root);

		volunteerSelector.addSelectionHandler(e -> {
			PopUtil.closeDialog();
			String newName = e.getSelectedItem();
			nameBlock.setNewName(newName);
			root.redraw();
			UiCenter.changeName(newName);
		});
	}

	@Override
	protected void adjustMember(int width, int height) {
		root.resize(width, height);
	}

	class NameBlock extends TextButton {
		String newName = userData.getName();

		NameBlock() {
			setBgColor(BG);
			setBgRadius(BgRadius);
			addSpriteSelectionHandler(e -> {
				if (newName.equals(userData.getName())) {
					//TODO 個人統計資訊
				} else {
					nameSetting();
				}
			});

			refresh();
		}

		void refresh() {
			setText(
				newName.equals(userData.getName()) ?
					"嗨！" + userData.getName() :
					userData.getName() + "→" + newName
			);
		}

		void setNewName(String newName) {
			this.newName = newName;
			refresh();
		}

		void nameSetting() {
			userData.setName(newName);
			DataCenter.saveUserData();
			refresh();
			UiCenter.showAnnounce();
		}
	}

	class ImgBlock extends LayerSprite {
		private final LImageSprite image;

		public ImgBlock(ImageResource ir) {
			setBgColor(BG);
			setBgRadius(BgRadius);
			image = new LImageSprite(ir);
			add(image);
		}

		@Override
		protected void adjustMember() {
			image.setWidth(getWidth());
			image.setHeight(getHeight());
		}
	}

	//XXX 目前還沒有動態改變這個的大小，不確定 adjustMember() 那段撐不撐得住
	class TextIconBlock extends LayerSprite {
		private final String text;
		private final ImageResource ir;

		private boolean mobileType;
		private LayerSprite root;

		TextIconBlock(String text, ImageResource ir) {
			this.text = text;
			this.ir = ir;

			setBgColor(BG);
			setBgRadius(BgRadius);

			//因為 mobile device 通常比較慢，所以預設 mobile，adjustMember() 可以快一點
			build(true);
		}

		@Override
		protected void adjustMember() {
			boolean nowType = isMobileType();

			//如果 type 沒改變，就只單純 resize，增加顯示效率
			if ( (!mobileType && !nowType) || (mobileType && nowType)) {
				root.resize(getWidth(), getHeight());
				return;
			}

			root.clear();
			build(nowType);

			this.redeploy();
			root.resize(getWidth(), getHeight());
			redrawSurface();
		}

		//Refactory 應該由 RwdRootPanel.getDeviceType() 決定
		boolean isMobileType() {
			return getHeight() >= M_HEIGHT - margin * 2;
		}

		private void build(boolean isMobile) {
			mobileType = isMobile;

			TextButton tb = new TextButton(text);
			ImgBlock ib = new ImgBlock(ir);

			//Refactory WeightLayoutLayer 沒有 public，所以看起來很囉唆
			if (isMobile) {
				VerticalLayoutLayer root = new VerticalLayoutLayer();
				root.setMargins(new Margins(5, 0, 0, 0));
				root.addChild(ib, 40);
				root.addChild(tb, 1);
				add(root);
				this.root = root;
			} else {
				HorizontalLayoutLayer root = new HorizontalLayoutLayer();
				root.addChild(ib, iconSize + 30);
				root.addChild(tb, 1);
				add(root);
				this.root = root;
			}
		}
	}
}
