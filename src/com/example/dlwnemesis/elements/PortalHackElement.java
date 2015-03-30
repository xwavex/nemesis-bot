package com.example.dlwnemesis.elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;

import android.util.Log;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

public class PortalHackElement extends InvokeAbleElement {
	private HackProc hack = null;
	private PortalElement portal;
	
	private PlayerStats player;

	// "{\"params\":{\"clientBasket\":{\"clientBlob\":\"6ZTuSqqxlbr2WRLwAT+Ityie1D14vTVKuMH3xb4aVnmQ26yHdbB3C4G/KKeSfn8RsoymWuvvHwnouy86De10vGvaC0ug54IbONrrDPpCLZHSZ0Uw3PDqxh7U9Lt7HlHGCvNjG1i1M7PcnJeEd9QwybncPbHWa/FneE3E7lji+8/i1eper+rCFE/jxSHZn4OoOAfLeb7/1cYM+ecEHxtZO9yAgW2929L8LrZ42XSjcok28QNrus/tAwtd5IvN3NF/rdmRyy1eTvCXvE1RPURX6MOw0QYDnJAW8dyfIfBEqOIXeR/gqSHlj5vp1/CvuYaEquZtUIycRvrl+dAH9gfK7A\"},\"energyGlobGuids\":[],\"itemGuid\":\"1f9315a72b0742549326c71860c01953.11\",\"knobSyncTimestamp\":0,\"playerLocation\":\"0319c063,00821be3\"}}");

	public PortalHackElement(PortalElement portal, PlayerStats player) {
		this.uri = "https://m-dot-betaspike.appspot.com/rpc/gameplay/collectItemsFromPortal";
		this.setPortal(portal);
		this.payload = calcPayload(portal);
		this.player = player;
		Log.d("DLWCC", "Trying To Hack Portal: " + portal.getTitle());
	}

	public PortalHackElement(HackProc hack, PortalElement portal) {
		this.uri = "https://m-dot-betaspike.appspot.com/rpc/gameplay/collectItemsFromPortal";
		this.setHack(hack);
		this.setPortal(portal);
		this.payload = calcPayload(portal);
	}

	private String calcPayload(PortalElement p) {
		String pay = "{\"params\":{\"energyGlobGuids\":[],\"itemGuid\":\"#\",\"knobSyncTimestamp\":0,\"playerLocation\":\"*\"}}";
		pay = pay.replace("#", p.getId());
		// 0319c063,00821be3
		String lat2 = Integer.toHexString(p.getLocation().getLatE6()).toString();
		String lon2 = Integer.toHexString(p.getLocation().getLonE6());
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
		pay = pay.replace("*", lat2 + "," + lon2);
		Log.d("DLWCC", "pay: " + pay);
		return pay;
	}

	@Override
	public void parseContent(String content) {
		Log.d("DLWCC", "Parsing hack response...");
		HackProc hp = null;
		ObjectMapper mapper = new ObjectMapper();
		JsonNode rootNode;
		try {
			rootNode = mapper.readTree(content);

			if (rootNode.has("result")) {
				hp = new HackProc("OK");

				JsonNode inventory = rootNode.findPath("inventory");
				if (!inventory.isMissingNode()) {

					ArrayList<UseAbleItem> items = new ArrayList<UseAbleItem>();

					Iterator<JsonNode> inventIt = inventory.elements();
					while (inventIt.hasNext()) {
						JsonNode temp = inventIt.next();
						String parseId = temp.toString();
						String[] fields = parseId.split(",");
						parseId = fields[0]
								.substring(2, fields[0].length() - 1);

						temp = temp.findPath("resourceWithLevels");
						if (!temp.isMissingNode()) {
							JsonNode tmp = temp.get("resourceType");
							if (tmp != null) {
								String type = tmp.asText();
								tmp = temp.get("level");
								if (tmp != null) {

									if (type.contains("EMITTER")) {
										Resonator reso = new Resonator(parseId,
												Integer.parseInt(tmp.asText()),
												type);
										items.add(reso);
									} else if (type.contains("BURSTER")) {
										Burster brust = new Burster(parseId,
												Integer.parseInt(tmp.asText()),
												type);
										items.add(brust);
									} else if (type.contains("SHIELD")) {
										Shield shield = new Shield(parseId,
												Integer.parseInt(tmp.asText()),
												type);
										items.add(shield);
									} else if (type.contains("CUBE")) {
										PowerCube cube = new PowerCube(parseId,
												Integer.parseInt(tmp.asText()),
												type);
										items.add(cube);
									} else if (type.contains("KEY")) {
										PortalKey cube = new PortalKey(parseId,
												Integer.parseInt(tmp.asText()),
												type);
										items.add(cube);
									}

								}
							}

						}
					}
					if (!items.isEmpty()) {
						hp.setCollectedItems(items);
					}
				}
			} else {
				JsonNode error = rootNode.get("error");
				if (error != null) {
					hp = new HackProc(error.asText());
					portal.setPortalCountDown(hp.getStatus());
				}
			}
			JsonNode energy = rootNode.findPath("energy");
			if (!energy.isMissingNode()) {
				player.setEnergy(energy.asInt());
			}
			JsonNode ap = rootNode.findPath("ap");
			if (!ap.isMissingNode()) {
				player.setAp(ap.asInt());
			}
			
			if (hp != null) {
				//Log.d("DLWCC", "HP: " + hp.toString());
				if (portal.collectList == null) {
					portal.collectList = new ArrayList<HackProc>();
				}
				portal.collectList.add(hp);
			}
			
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	public HackProc getHack() {
		return hack;
	}

	public void setHack(HackProc hack) {
		if (portal != null)
			portal.collectList.add(hack);

		this.hack = hack;
	}

	public PortalElement getPortal() {
		return portal;
	}

	public void setPortal(PortalElement portal) {
		this.portal = portal;
		if (hack != null)
			this.portal.collectList.add(hack);
	}

}
