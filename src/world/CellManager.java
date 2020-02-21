package world;

import java.util.Random;

import util.Config;
import util.ImageUtils;
import window.Window;

public class CellManager {

	public static Cell[][] cells;

	private static Random rng = new Random();

	public static int[] totalStrengths = new int[Config.COLONIES.length];
	private static int[] nextTotalStrengths = new int[Config.COLONIES.length];
	public static int[] populationCounts = new int[Config.COLONIES.length];
	private static int[] nextPopulationCounts = new int[Config.COLONIES.length];

	public static void update() {
		// Iterate through the array, apply rules

		// init stats
		for (int i = 0; i < Config.COLONIES.length; i++) {
			nextTotalStrengths[i] = 0;
			nextPopulationCounts[i] = 0;
		}

		for (int x = 0; x < Window.WORLD_WIDTH; x++) {
			for (int y = 0; y < Window.WORLD_HEIGHT; y++) {

				if (!cells[x][y].isUpdated()) {

					if (cells[x][y].isAlive()) {
						cells[x][y].update();

						// update stats
						nextTotalStrengths[cells[x][y].getID()] += cells[x][y].getStrength();
						nextPopulationCounts[cells[x][y].getID()]++;

						// choose a random neighbour to move to
						int nextPos = rng.nextInt(9);
						int dy = (int) Math.floor((float) nextPos / 3) - 1;
						int dx = nextPos % 3 - 1;
						int nextX = Math.floorMod(x + dx, Window.WORLD_WIDTH);
						int nextY = Math.floorMod(y + dy, Window.WORLD_HEIGHT);
						while (ImageUtils.isWater(nextX, nextY)) {
							nextPos = rng.nextInt(9);
							dy = (int) Math.floor((float) nextPos / 3) - 1;
							dx = nextPos % 3 - 1;
							nextX = Math.floorMod(x + dx, Window.WORLD_WIDTH);
							nextY = Math.floorMod(y + dy, Window.WORLD_HEIGHT);
						}

						if (!cells[nextX][nextY].isAlive()) {
							// if land
							// move i.e. copy all data
							cells[nextX][nextY] = new Cell(cells[x][y].getX(), cells[x][y].getY(), cells[x][y].getID(),
									cells[x][y].getAge(), cells[x][y].getStrength(), cells[x][y].getReproductionValue(),
									cells[x][y].isDiseased(), true, true);

							// kill current cell since it has moved
							cells[x][y].setAlive(false);
						} else if (cells[nextX][nextY].getID() != cells[x][y].getID()) {
							// if different colony
							// fight - die if their strength greater, otherwise kill
							if (cells[nextX][nextY].getStrength() > cells[x][y].getStrength()) {
								cells[x][y].setAlive(false);
							} else {
								// move i.e. copy all data
								cells[nextX][nextY] = new Cell(cells[x][y].getX(), cells[x][y].getY(),
										cells[x][y].getID(), cells[x][y].getAge(), cells[x][y].getStrength(),
										cells[x][y].getReproductionValue(), cells[x][y].isDiseased(), true, true);
								// kill current cell since it has moved
								cells[x][y].setAlive(false);
							}

						} else if (cells[nextX][nextY].getID() == cells[x][y].getID()) {
							// spread disease
							if (cells[x][y].isDiseased())
								cells[nextX][nextY].setDiseased(true);

							// swap data
							Cell temp = new Cell(cells[x][y].getX(), cells[x][y].getY(), cells[x][y].getID(),
									cells[x][y].getAge(), cells[x][y].getStrength(), cells[x][y].getReproductionValue(),
									cells[x][y].isDiseased(), true, true);
							cells[x][y] = new Cell(cells[nextX][nextY].getX(), cells[nextX][nextY].getY(),
									cells[nextX][nextY].getID(), cells[nextX][nextY].getAge(),
									cells[nextX][nextY].getStrength(), cells[nextX][nextY].getReproductionValue(),
									cells[nextX][nextY].isDiseased(), true, false);
							cells[nextX][nextY] = temp;
						}
					}

					// if about to reproduce, replace current cell with offspring (works since if
					// current cell has moved, reproduces in old spot, otherwise gets replaced)
					if (cells[x][y].canReproduce()) {
						cells[x][y] = cells[x][y].getOffspring();
						cells[x][y].setReproduce(false);
					}

				}
			}
		}

		// apply new stats
		totalStrengths = nextTotalStrengths;
		populationCounts = nextPopulationCounts;

		for (int x = 0; x < Window.WORLD_WIDTH; x++) {
			for (int y = 0; y < Window.WORLD_HEIGHT; y++) {
				if (cells[x][y].isAlive())
					cells[x][y].setUpdated(false);
			}
		}

	}

}