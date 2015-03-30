package com.example.dlwnemesis.wrapper;

import java.util.Iterator;

import android.app.Activity;
import android.os.AsyncTask;
import android.view.View;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.ViewSwitcher;

import com.example.dlwnemesis.R;
import com.example.dlwnemesis.elements.GuiUpdatePackage;
import com.example.dlwnemesis.elements.PlayerStats;
import com.example.dlwnemesis.elements.UseAbleItem;

public class UpdateGui extends AsyncTask<Object, Void, Object[]> {

	protected Object[] doInBackground(Object... objs) {
		return objs;
	}

	protected void onPostExecute(Object[] result) {
		Activity thiss = null;
		GuiUpdatePackage pack = null;
		if (result[0] instanceof Activity) {
			thiss = (Activity) result[0];
		}
		if (result[1] instanceof GuiUpdatePackage) {
			pack = (GuiUpdatePackage) result[1];
		}
		if (pack != null) {
			ProgressBar energyBar = ((ProgressBar) thiss
					.findViewById(R.id.progressbar_energy));
			energyBar.setMax(PlayerStats.getMaxEnergyByLvl(pack.lvlValue));
			energyBar.setProgress(pack.progressValue);

			TextView txtLevel = (TextView) thiss.findViewById(R.id.txt_level);
			txtLevel.setText("" + pack.lvlValue);
			TextView txtAp = (TextView) thiss.findViewById(R.id.txt_ap);
			txtAp.setText("" + pack.apValue);
			TextView txtPortalName = (TextView) thiss
					.findViewById(R.id.portal_name);
			txtPortalName.setText("" + pack.portalNameValue);
			TextView txtPortalState = (TextView) thiss
					.findViewById(R.id.portal_state);
			txtPortalState.setText("" + pack.portalStateValue);

			LinearLayout listLog = (LinearLayout) thiss
					.findViewById(R.id.list_log);

			ViewSwitcher portalSwitcher = (ViewSwitcher) thiss
					.findViewById(R.id.portal_switcher);

			ImageView portalCurrent = (ImageView) thiss
					.findViewById(R.id.portal_current);
			ImageView portalNew = (ImageView) thiss
					.findViewById(R.id.portal_new);

			TextView tmp = new TextView(thiss);
			tmp.setText("[" + System.currentTimeMillis() + "] Hacked: "
					+ pack.portalNameValue + ", " + pack.factionValue + ", "
					+ pack.portalStateValue);
			tmp.setTextColor(thiss.getResources().getColor(R.color.text_color));
			listLog.addView(tmp);

			if (pack.factionValue != null) {
				if (pack.factionValue.equalsIgnoreCase("resistance")) {
					ImageView v = (ImageView) portalSwitcher.getCurrentView();
					if (v.getId() != R.id.portal_current) {
						portalCurrent.setImageDrawable(thiss.getResources()
								.getDrawable(R.drawable.resistance_portal));
					} else {
						portalNew.setImageDrawable(thiss.getResources()
								.getDrawable(R.drawable.resistance_portal));
					}
					portalSwitcher.showNext();
				} else if (pack.factionValue.equalsIgnoreCase("aliens")) {
					ImageView v = (ImageView) portalSwitcher.getCurrentView();
					if (v.getId() != R.id.portal_current) {
						portalCurrent.setImageDrawable(thiss.getResources()
								.getDrawable(R.drawable.aliens_portal));
					} else {
						portalNew.setImageDrawable(thiss.getResources()
								.getDrawable(R.drawable.aliens_portal));
					}
					portalSwitcher.showNext();
				} else {
					ImageView v = (ImageView) portalSwitcher.getCurrentView();
					if (v.getId() != R.id.portal_current) {
						portalCurrent.setImageDrawable(thiss.getResources()
								.getDrawable(R.drawable.neutral_portal));
					} else {
						portalNew.setImageDrawable(thiss.getResources()
								.getDrawable(R.drawable.neutral_portal));
					}
					portalSwitcher.showNext();
				}
			}
			GridLayout grid = (GridLayout) thiss.findViewById(R.id.item_grid);
			grid.removeAllViews();

			if (pack.itemsValue != null) {
				Iterator<UseAbleItem> items = pack.itemsValue.iterator();
				while (items.hasNext()) {
					UseAbleItem item = items.next();

					View child = thiss.getLayoutInflater().inflate(
							R.layout.item_row, null);

					ImageView itemImg = (ImageView) child
							.findViewById(R.id.item_img);
					TextView txtItemAmount = (TextView) child
							.findViewById(R.id.txt_item_amount);
					TextView txtItemLvl = (TextView) child
							.findViewById(R.id.txt_item_lvl);

					if (item.getType().contains("EMITTER")) {
						itemImg.setImageDrawable(thiss.getResources()
								.getDrawable(R.drawable.reso));
					} else if (item.getType().contains("BURSTER")) {
						itemImg.setImageDrawable(thiss.getResources()
								.getDrawable(R.drawable.burster));
					} else if (item.getType().contains("SHIELD")) {
						itemImg.setImageDrawable(thiss.getResources()
								.getDrawable(R.drawable.shield));
					} else if (item.getType().contains("CUBE")) {
						itemImg.setImageDrawable(thiss.getResources()
								.getDrawable(R.drawable.powercube));
					} else if (item.getType().contains("KEY")) {
						itemImg.setImageDrawable(thiss.getResources()
								.getDrawable(R.drawable.portalkey));
					}
					txtItemAmount.setText("" + item.getType());// TODO
					txtItemLvl.setText("" + item.getLevel());

					grid.addView(child);

					// tmp = new TextView(thiss);
					// tmp.setText("[" + System.currentTimeMillis() +
					// "] Received: "
					// + item.getType() + ", " + item.getLevel());
					// listLog.addView(tmp);
				}
			}
		}
	}
}