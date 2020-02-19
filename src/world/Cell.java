package world;

import java.util.Random;

import util.Config;

public class Cell {

	private Random rng = new Random();

	private int x, y, colonyID, age, reproductionValue;
	private float strength;
	private boolean alive = false;
	private boolean diseased = false;
	private boolean updated = false;

	private boolean reproduce = false;

	public Cell(int x, int y) {
		this.x = x;
		this.y = y;
	}

	public Cell(int x, int y, int colonyID, int age, float strength, int reproductionValue, boolean alive,
			boolean diseased, boolean updated) {
		this.x = x;
		this.y = y;
		this.colonyID = colonyID;
		this.age = age;
		this.strength = strength;
		this.reproductionValue = reproductionValue;
		this.alive = alive;
		this.diseased = diseased;
		this.updated = updated;
	}

	public void update() {
		updated = true;
		age++;
		reproductionValue++;
		if (diseased) {
			age += 0.5;
		}
		if (!diseased && reproductionValue >= Config.REPRODUCTION_THRESHOLD) {
			reproduce = true;
			reproductionValue = 0;
		}
		if (diseased && reproductionValue >= Config.REPRODUCTION_THRESHOLD * 3) {
			reproduce = true;
			reproductionValue = 0;
		}
		if (age > strength)
			setAlive(false);
	}

	public Cell getOffspring() {
		float newStrength = strength;
		if (rng.nextInt(20) == 0) {
			newStrength += (float) (rng.nextInt(3) - 1) * strength * 0.2f;
		} else if (rng.nextInt(10) == 0) {
			newStrength += (float) (rng.nextInt(3) - 1) * strength * 0.02f;
		}
		boolean newDiseased = diseased;
		if (diseased && rng.nextInt(2) == 0) {
			newDiseased = false;
		}
		if (!diseased && rng.nextInt(10) == 0) {
			newDiseased = true;
		}
		return new Cell(x, y, colonyID, 0, newStrength, 0, true, newDiseased, false);
	}

	public int getX() {
		return this.x;
	}

	public void setX(int x) {
		this.x = x;
	}

	public int getY() {
		return this.y;
	}

	public void setY(int y) {
		this.y = y;
	}

	public int getID() {
		return this.colonyID;
	}

	public void setID(int id) {
		this.colonyID = id;
	}

	public int getAge() {
		return age;
	}

	public void setAge(int age) {
		this.age = age;
	}

	public float getStrength() {
		return strength;
	}

	public void setStrength(float strength) {
		this.strength = strength;
	}

	public int getReproductionValue() {
		return reproductionValue;
	}

	public void setReproductionValue(int reproductionValue) {
		this.reproductionValue = reproductionValue;
	}

	public boolean isAlive() {
		return alive;
	}

	public void setAlive(boolean alive) {
		this.alive = alive;
	}

	public boolean isDiseased() {
		return diseased;
	}

	public void setDiseased(boolean diseased) {
		this.diseased = diseased;
	}

	public boolean isUpdated() {
		return updated;
	}

	public void setUpdated(boolean updated) {
		this.updated = updated;
	}

	public boolean canReproduce() {
		return reproduce;
	}

	public void setReproduce(boolean reproduce) {
		this.reproduce = reproduce;
	}

}