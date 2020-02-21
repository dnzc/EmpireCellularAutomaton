package world;

import java.util.Random;

import util.Config;

public class Cell {

	private Random rng = new Random();

	private int x, y, colonyID, age, reproductionValue;
	private int strength;
	private boolean alive = false;
	private boolean updated = false;
	private boolean diseased = false;

	private boolean reproduce = false;

	public Cell(int x, int y) {
		this.x = x;
		this.y = y;
	}

	public Cell(int x, int y, int colonyID, int age, int strength, int reproductionValue, boolean diseased,
			boolean alive, boolean updated) {
		this.x = x;
		this.y = y;
		this.colonyID = colonyID;
		this.age = age;
		this.strength = strength;
		this.reproductionValue = reproductionValue;
		this.diseased = diseased;
		this.alive = alive;
		this.updated = updated;
	}

	public void update() {
		updated = true;
		age++;
		reproductionValue++;
		if (diseased)
			age += 0.5;
		if (reproductionValue >= (float) Config.REPRODUCTION_THRESHOLD * (rng.nextFloat() + 0.5f)) {
			reproduce = true;
			reproductionValue = 0;
		}
		if (age > strength)
			alive = false;
	}

	public Cell getOffspring() {
		int newStrength = strength;
		boolean newDiseased = diseased;

		float mutation = rng.nextFloat() * 100;
		if (mutation < Config.DISEASE_CHANCE) {
			newDiseased = true;
			newStrength *= 0.65;
		} else if (mutation < Config.SMALL_MUTATION_CHANCE) {
			newStrength *= rng.nextFloat();
		}

		if (diseased && mutation < Config.DISEASE_CURED_CHANCE)
			newDiseased = false;

		return new Cell(x, y, colonyID, 0, newStrength, 0, newDiseased, true, false);
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

	public int getStrength() {
		return strength;
	}

	public void setStrength(int strength) {
		this.strength = strength;
	}

	public int getReproductionValue() {
		return reproductionValue;
	}

	public void setReproductionValue(int reproductionValue) {
		this.reproductionValue = reproductionValue;
	}

	public boolean isDiseased() {
		return diseased;
	}

	public void setDiseased(boolean diseased) {
		this.diseased = diseased;
	}

	public boolean isAlive() {
		return alive;
	}

	public void setAlive(boolean alive) {
		this.alive = alive;
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