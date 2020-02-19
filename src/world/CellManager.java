package world;

import java.awt.Point;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import util.ImageUtils;
import window.EmpireCanvas;
import window.Window;

public class CellManager {

	private static Random rng = new Random();

	private static List<Point> dying = new ArrayList<>();

	private static int getNumForeignNeighbours(Cell cell) {
		int count = 0;
		Cell check = EmpireCanvas.cells[Math.floorMod(cell.getX() - 1, Window.WORLD_WIDTH)][Math
				.floorMod(cell.getY() - 1, Window.WORLD_HEIGHT)];
		if (check.isAlive() && check.getID() != cell.getID())
			count++;
		check = EmpireCanvas.cells[Math.floorMod(cell.getX(), Window.WORLD_WIDTH)][Math.floorMod(cell.getY() - 1,
				Window.WORLD_HEIGHT)];
		if (check.isAlive() && check.getID() != cell.getID())
			count++;
		check = EmpireCanvas.cells[Math.floorMod(cell.getX() + 1, Window.WORLD_WIDTH)][Math.floorMod(cell.getY() - 1,
				Window.WORLD_HEIGHT)];
		if (check.isAlive() && check.getID() != cell.getID())
			count++;
		check = EmpireCanvas.cells[Math.floorMod(cell.getX() - 1, Window.WORLD_WIDTH)][Math.floorMod(cell.getY(),
				Window.WORLD_HEIGHT)];
		if (check.isAlive() && check.getID() != cell.getID())
			count++;
		check = EmpireCanvas.cells[Math.floorMod(cell.getX() + 1, Window.WORLD_WIDTH)][Math.floorMod(cell.getY(),
				Window.WORLD_HEIGHT)];
		if (check.isAlive() && check.getID() != cell.getID())
			count++;
		check = EmpireCanvas.cells[Math.floorMod(cell.getX() - 1, Window.WORLD_WIDTH)][Math.floorMod(cell.getY() + 1,
				Window.WORLD_HEIGHT)];
		if (check.isAlive() && check.getID() != cell.getID())
			count++;
		check = EmpireCanvas.cells[Math.floorMod(cell.getX(), Window.WORLD_WIDTH)][Math.floorMod(cell.getY() + 1,
				Window.WORLD_HEIGHT)];
		if (check.isAlive() && check.getID() != cell.getID())
			count++;
		check = EmpireCanvas.cells[Math.floorMod(cell.getX() - 1, Window.WORLD_WIDTH)][Math.floorMod(cell.getY() + 1,
				Window.WORLD_HEIGHT)];
		if (check.isAlive() && check.getID() != cell.getID())
			count++;

		return count;
	}

	public static void update() {
		// Iterate through the array, apply rules

		for (int x = 0; x < Window.WORLD_WIDTH; x++) {
			for (int y = 0; y < Window.WORLD_HEIGHT; y++) {
				if (!EmpireCanvas.cells[x][y].isUpdated()) {

					if (EmpireCanvas.cells[x][y].isAlive()) {
						EmpireCanvas.cells[x][y].update();

						// if surrounded by foreign colonies
						if (getNumForeignNeighbours(EmpireCanvas.cells[x][y]) == 8) {
							EmpireCanvas.cells[x][y].setDiseased(true);
						}

						// try to move to a random position
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
						if (!EmpireCanvas.cells[nextX][nextY].isAlive()) {
							// move i.e. copy all data
							EmpireCanvas.cells[nextX][nextY] = new Cell(EmpireCanvas.cells[x][y].getX(),
									EmpireCanvas.cells[x][y].getY(), EmpireCanvas.cells[x][y].getID(),
									EmpireCanvas.cells[x][y].getAge(), EmpireCanvas.cells[x][y].getStrength(),
									EmpireCanvas.cells[x][y].getReproductionValue(), true,
									EmpireCanvas.cells[x][y].isDiseased(), true);

							// if about to reproduce, replace current cell with offspring, else kill current
							// cell since is has moved
							if (EmpireCanvas.cells[x][y].canReproduce()) {
								EmpireCanvas.cells[x][y] = EmpireCanvas.cells[x][y].getOffspring();
								EmpireCanvas.cells[x][y].setReproduce(false);
							} else
								dying.add(new Point(x, y));
						} else if (EmpireCanvas.cells[nextX][nextY].getID() != EmpireCanvas.cells[x][y].getID()) {
							// fight - die if their strength greater, otherwise kill
							if (EmpireCanvas.cells[nextX][nextY].getStrength() > EmpireCanvas.cells[x][y]
									.getStrength()) {
								dying.add(new Point(x, y));
							} else {
								// move i.e. copy all data
								EmpireCanvas.cells[nextX][nextY] = new Cell(EmpireCanvas.cells[x][y].getX(),
										EmpireCanvas.cells[x][y].getY(), EmpireCanvas.cells[x][y].getID(),
										EmpireCanvas.cells[x][y].getAge(),
										EmpireCanvas.cells[x][y].getStrength() + 0.1f,
										EmpireCanvas.cells[x][y].getReproductionValue(), true,
										EmpireCanvas.cells[x][y].isDiseased() || rng.nextInt(4) == 0, true);

								// if about to reproduce, replace current cell with offspring, else kill current
								// cell since is has moved
								if (EmpireCanvas.cells[x][y].canReproduce()) {
									EmpireCanvas.cells[x][y] = EmpireCanvas.cells[x][y].getOffspring();
									EmpireCanvas.cells[x][y].setReproduce(false);
								} else
									dying.add(new Point(x, y));
							}

						}

					}
				}
			}
		}

		for (Point p : dying) {
			EmpireCanvas.cells[p.x][p.y].setAlive(false);
		}
		dying.clear();

		for (int x = 0; x < Window.WORLD_WIDTH; x++) {
			for (int y = 0; y < Window.WORLD_HEIGHT; y++) {
				if (EmpireCanvas.cells[x][y].isAlive())
					EmpireCanvas.cells[x][y].setUpdated(false);
			}
		}

	}

}