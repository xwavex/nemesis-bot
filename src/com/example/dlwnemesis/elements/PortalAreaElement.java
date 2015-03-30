package com.example.dlwnemesis.elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;

import android.util.Log;

import com.example.dlwnemesis.behavior.LocationData;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

public class PortalAreaElement extends InvokeAbleElement {
	private String name = "";
	public ArrayList<PortalElement> portals = null;
	private boolean firstParse = true;
	public ArrayList<String> energyGlobList;
	private int maxUsedEnergyGlobs = 10;

	private PlayerStats player;
	
	public PortalAreaElement(String name, PlayerStats player) {
		this.uri = "https://m-dot-betaspike.appspot.com/rpc/gameplay/getObjectsInCells";
		this.payload = "{\"params\":{\"cells\":null,\"cellsAsHex\":[\"47ba3d1a50000000\",\"47ba3d1950000000\",\"47ba3d1850000000\",\"47ba3d1790000000\",\"47ba3d1970000000\",\"47ba3d1870000000\",\"47ba3d1770000000\",\"47ba3d1990000000\",\"47ba3d1890000000\",\"47ba3d1bb0000000\",\"47ba3d1750000000\",\"47ba3d19b0000000\",\"47ba3d18b0000000\",\"47ba3d1bd0000000\",\"47ba3d19d0000000\",\"47ba3d18d0000000\",\"47ba3d1bf0000000\",\"47ba3d19f0000000\",\"47ba3d18f0000000\",\"47ba3d1a10000000\",\"47ba3d1910000000\",\"47ba3d1810000000\",\"47ba3d1a30000000\",\"47ba3d1930000000\",\"47ba3d1830000000\"],\"clientBasket\":{\"clientBlob\":null},\"dates\":[0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0],\"energyGlobGuids\":[],\"knobSyncTimestamp\":0,\"playerLocation\":\"0319bcf1,008219f8\"}}";
		this.setName(name);
		this.player = player;
		ArrayList<String> energyGlobList = new ArrayList<String>();
	}

	public PortalAreaElement(String name, ArrayList<PortalElement> portals) {
		this.uri = "https://m-dot-betaspike.appspot.com/rpc/gameplay/getObjectsInCells";
		this.payload = "{\"params\":{\"cells\":null,\"cellsAsHex\":[\"47ba3d1a50000000\",\"47ba3d1950000000\",\"47ba3d1850000000\",\"47ba3d1790000000\",\"47ba3d1970000000\",\"47ba3d1870000000\",\"47ba3d1770000000\",\"47ba3d1990000000\",\"47ba3d1890000000\",\"47ba3d1bb0000000\",\"47ba3d1750000000\",\"47ba3d19b0000000\",\"47ba3d18b0000000\",\"47ba3d1bd0000000\",\"47ba3d19d0000000\",\"47ba3d18d0000000\",\"47ba3d1bf0000000\",\"47ba3d19f0000000\",\"47ba3d18f0000000\",\"47ba3d1a10000000\",\"47ba3d1910000000\",\"47ba3d1810000000\",\"47ba3d1a30000000\",\"47ba3d1930000000\",\"47ba3d1830000000\"],\"clientBasket\":{\"clientBlob\":null},\"dates\":[0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0],\"energyGlobGuids\":[],\"knobSyncTimestamp\":0,\"playerLocation\":\"0319bcf1,008219f8\"}}";
		this.portals = portals;
		this.setName(name);
		ArrayList<String> energyGlobList = new ArrayList<String>();
	}
	
	public boolean checkIfAreaIsBurnedOut() {
		boolean out = true;
		if (this.portals != null) {
			if(!portals.isEmpty()) {
				for (int i = 0; i < portals.size(); i++) {
					Date now = new Date();
					if (portals.get(i).getTimeReadyAgain().getTime() < (now.getTime() + 1200000)) {
						out = false;
					}
				}
			}
		}
		return out;
	}

	public boolean useEnergyGlobs() {
		if ((energyGlobList != null) && (!energyGlobList.isEmpty())) {
			Log.d("DLWCC", "Amount of Energy: " + energyGlobList.size());
			int anz = 0;

			String content = "\"energyGlobGuids\":[";
			
			for (int i = 0; i < energyGlobList.size(); i++) {
				if (anz < maxUsedEnergyGlobs) {
					String globid = energyGlobList.get(i);
					content = content + "\"" + globid + "\",";
					anz++;
				} else {
					break;
				}
			}
			
			if (content.charAt(content.length()-1) == ',') {
				
				content = content.substring(0, content.length() - 1);
			}
			content = content + "]";
			
			if (!content.equals("\"energyGlobGuids\":[]")) {
				this.payload = this.payload.replace("\"energyGlobGuids\":[]",
						content);
				energyGlobList.clear();
				return true;
			} else {
				return false;
			}
		} else {
			return false;
		}
	}

	public void reStorePayload() {
		this.payload = "{\"params\":{\"cells\":null,\"cellsAsHex\":[\"47ba3d1a50000000\",\"47ba3d1950000000\",\"47ba3d1850000000\",\"47ba3d1790000000\",\"47ba3d1970000000\",\"47ba3d1870000000\",\"47ba3d1770000000\",\"47ba3d1990000000\",\"47ba3d1890000000\",\"47ba3d1bb0000000\",\"47ba3d1750000000\",\"47ba3d19b0000000\",\"47ba3d18b0000000\",\"47ba3d1bd0000000\",\"47ba3d19d0000000\",\"47ba3d18d0000000\",\"47ba3d1bf0000000\",\"47ba3d19f0000000\",\"47ba3d18f0000000\",\"47ba3d1a10000000\",\"47ba3d1910000000\",\"47ba3d1810000000\",\"47ba3d1a30000000\",\"47ba3d1930000000\",\"47ba3d1830000000\"],\"clientBasket\":{\"clientBlob\":null},\"dates\":[0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0],\"energyGlobGuids\":[],\"knobSyncTimestamp\":0,\"playerLocation\":\"0319bcf1,008219f8\"}}";
	}

	public String[] getPlayerLocationHexString() {
		String[] tmp = this.payload.split("playerLocation\":\"");
		tmp = tmp[1].split(",");
		String lat = tmp[0];
		String lon = tmp[1];
		lon = lon.replaceAll(" ", "");
		lon = lon.replaceAll("\"", "");
		lon = lon.replaceAll("}", "");
		lon = lon.replaceAll("]", "");
		// Log.d("DLWCC", "lat lon:" + lat + "," + lon);
		return new String[] { lat, lon };
	}

	public double[] getPlayerLocationInPortalLocation() {
		String[] tmp = getPlayerLocationHexString();
		return new double[] { Double.parseDouble(tmp[0]),
				Double.parseDouble(tmp[1]) };
	}

	public void setPlayerLocation(LocationData location) {
		// 0319bcf1,008219f8
		String lat2 = location.getLatE6Hex();
		String lon2 = location.getLonE6Hex();
		
		int lastIndex = this.payload.lastIndexOf("\"playerLocation\":\"");
		this.payload = this.payload.substring(0, lastIndex) + "\"playerLocation\":\"" + lat2 + "," + lon2 + "\"}}";
	}

	public void setPlayerLocation(PortalElement goal, double stepSize) {
		// 0319bcf1,008219f8
		String lat2 = Integer.toHexString(goal.getLocation().getLatE6())
				.toString();
		String lon2 = Integer.toHexString(goal.getLocation().getLonE6());
		if (lat2.length() < 8) {
			for (int i = 0; i <= (8 - lat2.length()); i++) {
				lat2 = "0" + lat2;
			}
		}
		if (lon2.length() < 8) {
			for (int i = 0; i <= (8 - lon2.length()); i++) {
				lon2 = "0" + lon2;
			}
		}
		int lastIndex = this.payload.lastIndexOf("\"playerLocation\":\"");
		this.payload = this.payload.substring(0, lastIndex) + "\"playerLocation\":\"" + lat2 + "," + lon2 + "\"}}";
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Override
	public void parseContent(String content) {
		if (energyGlobList != null) {
			energyGlobList.clear();
			// TODO
		}

		ArrayList<PortalElement> tmpPortals = new ArrayList<PortalElement>();

		ObjectMapper mapper = new ObjectMapper();
		JsonNode rootNode;
		try {
			rootNode = mapper.readTree(content);

			JsonNode inventory = rootNode.findPath("gameEntities");
			Iterator<JsonNode> inventIt = inventory.elements();
			while (inventIt.hasNext()) {
				JsonNode temp = inventIt.next();

				if (!temp.findPath("portalV2").isMissingNode()) {

					PortalElement pElem = null;
					LocationData location = null;

					String parseId = temp.toString();
					String[] fields = parseId.split(",");
					parseId = fields[0].substring(2, fields[0].length() - 1);

					JsonNode tmp = temp.findPath("TITLE");
					if (!tmp.isMissingNode()) {
						String title = tmp.asText();

						tmp = temp.findPath("locationE6");
						if (!tmp.isMissingNode()) {
							JsonNode coordTmp = tmp.get("latE6");
							if (coordTmp != null) {
								String lat = coordTmp.asText();
								coordTmp = tmp.get("lngE6");
								if (coordTmp != null) {
									location = new LocationData(
											Integer.parseInt(lat),
											Integer.parseInt(coordTmp.asText()));
									pElem = new PortalElement();
									pElem.setId(parseId);
									pElem.setTitle(title);
									pElem.setLocation(location);
									tmp = temp.findPath("team");
									if (!tmp.isMissingNode()) {
										pElem.setFaction(tmp.asText());
									}

									tmpPortals.add(pElem);
								}
							}
						}
					}

				}
				JsonNode eneryGlobs = rootNode.findPath("energyGlobGuids");
				if (!eneryGlobs.isMissingNode()) {
					Iterator<JsonNode> energyIt = eneryGlobs.elements();
					while (energyIt.hasNext()) {
						JsonNode glob = energyIt.next();
						if (energyGlobList != null) {
							energyGlobList.add(glob.asText());
						} else {
							//Log.d("DLWCC", "new glob list");
							energyGlobList = new ArrayList<String>();
						}
					}
				}

			}

			if ((tmpPortals != null) && (!tmpPortals.isEmpty())) {
				if ((firstParse) || (portals == null)
						|| ((portals != null) && (portals.isEmpty()))) {
					this.portals = tmpPortals;
				} else {
					updatePortals(tmpPortals);
				}
				// Portal Names:
//				Log.d("DLWOUT", "Portals: ");
//				for (int i = 0; i < tmpPortals.size();i++) {
//					Log.d("DLWOUT", tmpPortals.get(i).getTitle());
//				}
//				Log.d("DLWOUT", " ");
			}

		} catch (JsonProcessingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	private void updatePortals(ArrayList<PortalElement> tmpPortals) {
		Iterator<PortalElement> mIterator = portals.iterator();
		while (mIterator.hasNext()) {
			PortalElement pE = mIterator.next();
			Iterator<PortalElement> tmpIterator = tmpPortals.iterator();
			while (tmpIterator.hasNext()) {
				PortalElement upE = tmpIterator.next();

				if (pE.getId().equalsIgnoreCase(upE.getId())) {
					pE.setFaction(upE.getFaction());
					break;
				}
			}
		}
	}

}
