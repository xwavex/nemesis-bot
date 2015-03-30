package com.example.dlwnemesis.behavior;

import java.net.HttpCookie;
import java.util.Date;
import java.util.Iterator;

import android.app.Activity;
import android.os.AsyncTask;
import android.util.Log;

import com.example.dlwnemesis.elements.GuiUpdatePackage;
import com.example.dlwnemesis.elements.HackProc;
import com.example.dlwnemesis.elements.PlayerStats;
import com.example.dlwnemesis.elements.PortalAreaElement;
import com.example.dlwnemesis.elements.PortalElement;
import com.example.dlwnemesis.elements.PortalHackElement;
import com.example.dlwnemesis.elements.UseAbleItem;
import com.example.dlwnemesis.querys.GeneralRequest;
import com.example.dlwnemesis.wrapper.BehaviorLauncher;
import com.example.dlwnemesis.wrapper.UpdateGui;

public class SimpleHackBehavior {
	private PortalAreaElement area = null;
	private Object[] objs;
	private GeneralRequest loadWorldRequest;
	private GeneralRequest hackPortalRequest;
	private SimpleBehaviorStates currentState;

	private PlayerStats player;

	private Iterator<PortalElement> portalIterator = null;
	private PortalElement currentPortal = null;
	private double stepSize = 0.05;
	private int remainingSteps = 0;

	private int rounds = 1;

	private boolean onlyCollectBehavior = false;

	private Activity activity;

	public boolean killAll = false;

	private LocationData currentLocation = null;

	public SimpleHackBehavior(HttpCookie cookie, String xsrf, Activity activity) {
		this.activity = activity;
		player = new PlayerStats();
		area = new PortalAreaElement("AreaOne", player);

		objs = new Object[3];
		objs[0] = cookie;
		objs[1] = xsrf;
		objs[2] = area;
		currentState = SimpleBehaviorStates.DISCOVERY;

		new tDiscoveryPhase().execute("");
	}

	private void updateGui() {
		GuiUpdatePackage gP = new GuiUpdatePackage();
		gP.apValue = player.getAp();
		gP.lvlValue = player.getLvlByAp();
		gP.progressValue = player.getEnergy();

		PortalElement portal = currentPortal;
		gP.portalNameValue = portal.getTitle();
		gP.factionValue = portal.getFaction();
		Date now = new Date();
		long cool = portal.getTimeReadyAgain().getTime() - now.getTime();
		double c = cool / 1000.0;
		if (c < 1) {
			c = 0;
		}
		gP.portalStateValue = "Ready in " + c + " sec.";
		try {
			HackProc h = portal.collectList.get(portal.collectList.size() - 1);
			if (!h.isRead()) {
				gP.itemsValue = h.getCollectedItems();
				h.setRead(true);
			} else {
				gP.itemsValue = null;
			}
		} catch (Exception e) {
			gP.itemsValue = null;
		}
		Object[] guiObj = new Object[2];
		guiObj[0] = activity;
		guiObj[1] = gP;

		new UpdateGui().execute(guiObj);
	}

	private void printStuff() {
		PortalElement portal = area.portals.get(0);
		if ((portal.collectList != null) && (!portal.collectList.isEmpty())) {
			HackProc hp = portal.collectList.get(0);
			Log.d("DLWCC", "-----------------------------------------");
			Log.d("DLWCC", "AllOut: " + hp.toString());
			Log.d("DLWCC", "PortalName: " + portal.getTitle());
			Log.d("DLWCC", "Id: " + portal.getId());
			Log.d("DLWCC", "Faction: " + portal.getFaction());

			Date now = new Date();
			long cool = portal.getTimeReadyAgain().getTime() - now.getTime();

			Log.d("DLWCC", "Cooldown: " + cool / 1000.0 + "secs.");

			Iterator<UseAbleItem> it = hp.getCollectedItems().iterator();
			while (it.hasNext()) {
				UseAbleItem item = it.next();
				Log.d("DLWCC", "id: " + item.getId());
				Log.d("DLWCC", "type: " + item.getType());
				Log.d("DLWCC", "level: " + item.getLevel());
				Log.d("DLWCC", "-------");
			}

			Log.d("DLWCC", "##########################################");
		}
	}

	private void printStuffCurrent() {
		PortalElement portal = currentPortal;
		if ((portal.collectList != null) && (!portal.collectList.isEmpty())) {
			HackProc hp = portal.collectList.get(0);
			Log.d("DLWCC", "-----------------------------------------");
			Log.d("DLWCC", "AllOut: " + hp.toString());
			Log.d("DLWCC", "PortalName: " + portal.getTitle());
			Log.d("DLWCC", "Id: " + portal.getId());
			Log.d("DLWCC", "Faction: " + portal.getFaction());

			Date now = new Date();
			long cool = portal.getTimeReadyAgain().getTime() - now.getTime();

			Log.d("DLWCC", "Cooldown: " + cool / 1000.0 + "secs.");

			if (hp.getCollectedItems() != null) {
				Iterator<UseAbleItem> it = hp.getCollectedItems().iterator();
				while (it.hasNext()) {
					UseAbleItem item = it.next();
					Log.d("DLWCC", "id: " + item.getId());
					Log.d("DLWCC", "type: " + item.getType());
					Log.d("DLWCC", "level: " + item.getLevel());
					Log.d("DLWCC", "-------");
				}
			}
			Log.d("DLWCC", "##########################################");
		}
	}

	private static double getDistanceBetweenPortals(LocationData p1,
			PortalElement p2) {
		double la1 = p1.getLatE6() / 1e6;
		double lo1 = p1.getLonE6() / 1e6;

		double la2 = p2.getLocation().getLatE6() / 1e6;
		double lo2 = p2.getLocation().getLonE6() / 1e6;

		return distanceTo(la1, lo1, la2, lo2); // in km
	}

	private static double distanceTo(double Alat, double Alon, double Blat,
			double Blon) {
		// default 4 sig figs reflects typical 0.3% accuracy of spherical model
		int precision = 4;

		double R = 6371.0;
		double lat1 = Math.toRadians(Alat), lon1 = Math.toRadians(Alon);
		double lat2 = Math.toRadians(Blat), lon2 = Math.toRadians(Blon);
		double dLat = lat2 - lat1;
		double dLon = lon2 - lon1;

		double a = Math.sin(dLat / 2) * Math.sin(dLat / 2) + Math.cos(lat1)
				* Math.cos(lat2) * Math.sin(dLon / 2) * Math.sin(dLon / 2);
		double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
		double d = R * c;
		return d;// .toPrecisionFixed(precision);
	}

	private static double bearingTo(double Alat, double Alon, double Blat,
			double Blon) {
		double lat1 = Math.toRadians(Alat), lat2 = Math.toRadians(Blat);
		double dLon = Math.toRadians(Blon - Alon);

		double y = Math.sin(dLon) * Math.cos(lat2);
		double x = Math.cos(lat1) * Math.sin(lat2) - Math.sin(lat1)
				* Math.cos(lat2) * Math.cos(dLon);
		double brng = Math.atan2(y, x);

		return (Math.toDegrees(brng) + 360) % 360;
	}

	private static LocationData getNewPointByDistance(LocationData p1,
			LocationData p2, double dist) {
		double la1 = p1.getLatE6() / 1e6;
		double lo1 = p1.getLonE6() / 1e6;

		double la2 = p2.getLatE6() / 1e6;
		double lo2 = p2.getLonE6() / 1e6;

		double b = bearingTo(la1, lo1, la2, lo2);
		double[] nd = destinationPoint(la1, lo1, b, dist);
		nd[0] *= 1e6;
		nd[1] *= 1e6;
		return new LocationData((int) nd[0], (int) nd[1]);
	}

	private static double[] destinationPoint(double Alat, double Alon,
			double brng, double dist) {
		dist = dist / 6371.0; // convert dist to angular distance in radians
		brng = Math.toRadians(brng); //
		double lat1 = Math.toRadians(Alat), lon1 = Math.toRadians(Alon);

		double lat2 = Math.asin(Math.sin(lat1) * Math.cos(dist)
				+ Math.cos(lat1) * Math.sin(dist) * Math.cos(brng));
		double lon2 = lon1
				+ Math.atan2(Math.sin(brng) * Math.sin(dist) * Math.cos(lat1),
						Math.cos(dist) - Math.sin(lat1) * Math.sin(lat2));
		lon2 = (lon2 + 3 * Math.PI) % (2 * Math.PI) - Math.PI; // normalise to
																// -180..+180º

		return new double[] { Math.toDegrees(lat2), Math.toDegrees(lon2) };
	}

	private class tHackPhase extends AsyncTask<Object, Void, Object> {

		protected Object doInBackground(Object... objs) {
			hackPhase();
			return null;
		}

		protected void onPostExecute(Object result) {

		}

		private void hackPhase() {
			if (!onlyCollectBehavior) {
				if (currentPortal != null) {
					if (player.getEnergy() < player
							.getMinEnergyToHackPortal(currentPortal
									.getFaction())) {
						// TODO energy searching!
					}

					currentLocation = currentPortal.getLocation();

					PortalHackElement pHackElement = new PortalHackElement(
							currentPortal, player);

					Object[] obj = new Object[3];
					obj[0] = objs[0];
					obj[1] = objs[1];
					obj[2] = pHackElement;
					hackPortalRequest = (GeneralRequest) new GeneralRequest() {
						public void allDone() {
							updateGui();

							printStuffCurrent();

							// loop!
							if (!killAll)
								new tWalkPhase().execute("");
						}
					}.execute(obj);
				} else {
					Log.d("DLWCC", "Error: No current Portal...!");
				}
			} else {
				currentLocation = currentPortal.getLocation();
				if (!killAll)
					new tWalkPhase().execute("");
			}
		}
	}

	private class tWalkPhase extends AsyncTask<Object, Void, Object> {

		protected Object doInBackground(Object... objs) {
			if (!killAll)
				walkPhase();
			return null;
		}

		protected void onPostExecute(Object result) {

		}

		private void walkPhase() {
			if (player != null) {
				Log.d("DLWCC",
						"##################################################################");
				Log.d("DLWCC", player.toString());
			}

			if ((area != null) && (area.checkIfAreaIsBurnedOut())) {
				Log.d("DLWCC", "Area is burned out, exit Behavior!");
				activity.finish(); // TODO
				return;
			}

			Log.d("DLWCC", "walkPhase");
			remainingSteps = 0;
			if ((portalIterator != null) && (portalIterator.hasNext())) {
				PortalElement portal = portalIterator.next();

				Date now = new Date();
				long coolDown = (long) ((portal.getTimeReadyAgain().getTime() - now
						.getTime()));
				if (coolDown <= 100) {

					if (currentPortal == null) {
						currentPortal = portal;
						// direkt hingehen
						try {
							Thread.sleep(3000);
						} catch (InterruptedException e) {
						}
						if (!killAll)
							new tHackPhase().execute("");
					} else {

						currentPortal = portal;
						if (currentLocation != null) {
							// distance between portals..... for approach!
							double distance = getDistanceBetweenPortals(
									currentLocation, currentPortal);
							int steps = (int) (distance / stepSize);
							if (steps > 0) {
								currentState = SimpleBehaviorStates.WALKING;
								remainingSteps = steps;
								if (!killAll)
									new tDiscoveryPhase().execute("");
							} else {
								// direkt hingehen
								try {
									Thread.sleep(3000);
								} catch (InterruptedException e) {
								}
								if (!killAll)
									new tHackPhase().execute("");
							}
						} else {
							Log.d("DLWCC", "Error: currentLocation is null...!");
						}
					}
				} else {
					Log.d("DLWCC", "Portal: " + currentPortal.getTitle()
							+ ", ready in "
							+ currentPortal.getTimeReadyAgain().getTime());
				}
			} else if (rounds > 0) {
				if ((area != null) && (area.checkIfAreaIsBurnedOut())) {
					Log.d("DLWCC", "Area is burned out, exit Behavior!");
					activity.finish();
					return;
				} else {
					Log.d("DLWCC", "Naechste runde!!!!: " + rounds);
					// TODO andere areas laden....
					portalIterator = area.portals.iterator();
					rounds--;
					if (!killAll)
						new tWalkPhase().execute("");
				}
			} else {
				Log.d("DLWCC", "Alles finished!");
				activity.finish();
			}
		}
	}

	private class tDiscoveryPhase extends AsyncTask<Object, Void, Object> {

		protected Object doInBackground(Object... objs) {
			if (!killAll)
				discoveryPhase();
			return null;
		}

		protected void onPostExecute(Object result) {

		}

		private void discoveryPhase() {
			Log.d("DLWCC", "discoveryPhase");

			Log.d("DLWCC", "PlayerEnergy " + player.getEnergy());
			Log.d("DLWCC", "PlayerMaxEnergy " + player.getMaxEnergyByLvl());
			Log.d("DLWCC", "PlayerLevel " + player.getLvlByAp());

			if ((area != null)
					&& (player.getEnergy() < player.getMaxEnergyByLvl())) {
				boolean b = area.useEnergyGlobs();
				Log.d("DLWCC", "Use Energy Globs: " + b);
			}

			if (remainingSteps > 0) {
				try {
					Thread.sleep(3000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}

				currentLocation = getNewPointByDistance(currentLocation,
						currentPortal.getLocation(), stepSize);
				// setLocation
				area.setPlayerLocation(currentLocation);
			}

			loadWorldRequest = (GeneralRequest) new GeneralRequest() {
				public void allDone() {
					Log.d("DLWCC", "Scanning...!");
					if (currentState == SimpleBehaviorStates.WALKING) {
						Log.d("DLWCC", "...successfully.");
						remainingSteps--;

						if (remainingSteps > 0) {
							Log.d("DLWCC", "just " + remainingSteps
									+ " more steps remaining...");
							if (!killAll)
								new tDiscoveryPhase().execute("");
						} else {
							if (!killAll)
								new tHackPhase().execute("");
						}
					} else if (currentState == SimpleBehaviorStates.DISCOVERY) {
						if ((area.portals != null) && (!area.portals.isEmpty())) {
							Log.d("DLWCC", "...successfully added world!");
							portalIterator = area.portals.iterator();
							if (!killAll)
								new tWalkPhase().execute("");
						} else {
							Log.d("DLWCC",
									"Error: No portals in area anymore....!");
						}
					}

				}
			}.execute(objs);
		}
	}
}
