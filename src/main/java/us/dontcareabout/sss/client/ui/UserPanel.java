package us.dontcareabout.sss.client.ui;

import com.google.gwt.resources.client.ImageResource;
import com.sencha.gxt.chart.client.draw.RGB;

import us.dontcareabout.gxt.client.draw.LImageSprite;
import us.dontcareabout.gxt.client.draw.LayerContainer;
import us.dontcareabout.gxt.client.draw.LayerSprite;
import us.dontcareabout.gxt.client.draw.component.TextButton;
import us.dontcareabout.gxt.client.draw.layout.HorizontalLayoutLayer;
import us.dontcareabout.gxt.client.util.PopUtil;
import us.dontcareabout.sss.client.ImageRS;
import us.dontcareabout.sss.client.Util;
import us.dontcareabout.sss.client.data.DataCenter;
import us.dontcareabout.sss.client.vo.UserData;

public class UserPanel extends LayerContainer {
	private static final RGB BG = RGB.LIGHTGRAY;
	private static final int BgRadius = 5;
	private static final int iconSize = 80;

	private UserData userData = DataCenter.getUserData();
	private String newName;

	private HorizontalLayoutLayer root = new HorizontalLayoutLayer();
	private NameBlock nameBlock = new NameBlock();
	private ImgBlock confirmBlock = new ImgBlock(ImageRS.I.personCheck());
	private TextIconBlock reportBlock = new TextIconBlock("進班回報", ImageRS.I.calendarCheck());
	private TextIconBlock volunteerBlock = new TextIconBlock("志工時數", ImageRS.I.pencilSquare());

	private VolunteerSelector volunteerSelector = new VolunteerSelector();

	public UserPanel() {
		root.setMargins(6);
		root.setGap(12);

		nameBlock.addSpriteSelectionHandler(e -> changeName());

		confirmBlock.setHidden(true);
		confirmBlock.addSpriteSelectionHandler(e -> {
			userData.setName(newName);
			DataCenter.saveUserData();
			confirmBlock.setHidden(true);
			nameBlock.refresh();
			UiCenter.showAnnounce();
		});

		reportBlock.addSpriteSelectionHandler(e -> Util.openUrl(Util.REPORT_URL));
		volunteerBlock.addSpriteSelectionHandler(e -> Util.openUrl(Util.VOLUNTEER_HOUR_URL));

		root.addChild(confirmBlock, iconSize);
		root.addChild(nameBlock, 0.4);
		root.addChild(reportBlock, 0.3);
		root.addChild(volunteerBlock, 0.3);
		addLayer(root);

		volunteerSelector.addSelectionHandler(e -> {
			PopUtil.closeDialog();
			newName = e.getSelectedItem();
			confirmBlock.setHidden(newName.equals(userData.getName()));
			root.redraw();
			UiCenter.changeName(newName);
		});
	}

	@Override
	protected void adjustMember(int width, int height) {
		root.resize(width, height);
	}

	private void changeName() {
		PopUtil.dialog.setDraggable(false);//Refactory 移到 GF 去
		PopUtil.showDialog(volunteerSelector, 200, 500);
	}

	class NameBlock extends TextButton {
		NameBlock() {
			setBgColor(BG);
			setBgRadius(BgRadius);
			refresh();
		}

		void refresh() {
			setText("嗨！" + userData.getName());
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

	class TextIconBlock extends HorizontalLayoutLayer {
		TextIconBlock(String text, ImageResource ir) {
			setBgColor(BG);
			setBgRadius(BgRadius);
			TextButton tb = new TextButton(text);
			this.addChild(new ImgBlock(ir), iconSize + 30);
			this.addChild(tb, 1);
		}
	}
}
