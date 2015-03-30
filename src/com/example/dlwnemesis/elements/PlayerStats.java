package com.example.dlwnemesis.elements;

public class PlayerStats {
	private int ap;
	private int energy;
	/** Ap, Xm, Hack friendly, Hack neutral, Hack enemy*/
	private static int[][] statTable = {{0,3000,50,200,300},
								{10000,4000,50,400,600},
								{30000,5000,50,600,900},
								{70000,6000,50,800,1200},
								{150000,7000,50,1000,1500},
								{300000,8000,50,1200,1800},
								{600000,9000,50,1400,2100},
								{1200000,10000,50,1600,2400}};

	public int getMaxEnergyByLvl() {
		int level = getLvlByAp();
		if (level != (-1)) {
			return statTable[level-1][1];
		} else {
			return -1;
		}
	}
	
	public static int getMaxEnergyByLvl(int lvl) {
		if (lvl > 0) {
			return statTable[lvl-1][1];
		} else {
			return 0;
		}
	}
	
	public int getLvlByAp() {
		for (int i = 0; i < statTable.length; i++) {
			if (ap < statTable[i][0]) {
				return i;
			}
		}
		return -1;
	}
	
	public int getMinEnergyToHackPortal(String faction) {
		int fac = 2;
		if ((faction.equalsIgnoreCase("aliens")) || (faction.equalsIgnoreCase("enlighted"))) {
			fac = 4;
		} else if (faction.equalsIgnoreCase("neutral")) {
			fac = 3;
		}
		int level = getLvlByAp();
		if (level != (-1)) {
			return statTable[level-1][fac];
		} else {
			return -1;
		}
	}
	
	public void setAp(int ap) {
		this.ap = ap;
	}
	
	public int getAp() {
		return this.ap;
	}
	
	public void setEnergy(int energy) {
		this.energy = energy;
	}
	
	public int getEnergy() {
		return this.energy;
	}
	
	public String toString() {
		return "Player Stats:\n" + "Energy: " + this.energy
				+ "\nAp: " + this.ap;
	}
}
