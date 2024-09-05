package tetris;

public class FigureFactory {

	public static int[][] createNextFigure(int random) {
		int[][] figure = new int[4][4];
		switch (random) {
		case 1: {
			figure = O();
			break;
		}
		case 2: {
			figure = J();
			break;
		}
		case 3: {
			figure = straight();
			break;
		}
		case 4: {
			figure = T();
			break;
		}
		case 5: {
			figure = L();
			break;
		}
		case 6: {
			figure = S();
			break;
		}
		case 7: {
			figure = Z();
			break;
		}
		default:
			throw new IllegalArgumentException("Unexpected value: " + random);
		}
		return figure;
	}

	static int[][] O() {
		return new int[][] {
			{0, 1, 1, 0},
			{0, 1, 1, 0},
			{0, 0, 0, 0},
			{0, 0, 0, 0},
		};
	}

	static int[][] J() {
		return new int[][] {
			{0, 0, 2, 0},
			{0, 0, 2, 0},
			{0, 2, 2, 0},
			{0, 0, 0, 0},
		};
	}
	
	static int[][] rotatedJ() {
		return new int[][] {
			{0, 0, 0, 0},
			{0, 2, 0, 0},
			{0, 2, 2, 2},
			{0, 0, 0, 0},
		};
	}
	static int[][] straight() {
		return new int[][] {
			{3, 3, 3, 3},
			{0, 0, 0, 0},
			{0, 0, 0, 0},
			{0, 0, 0, 0},
		};
	}
	
	static int[][] rotatedStraight(){
		return new int[][] {
			{0, 3, 0, 0},
			{0, 3, 0, 0},
			{0, 3, 0, 0},
			{0, 3, 0, 0},
		};
	}
	
	static int[][] T() {
		return new int[][] {
			{4, 4, 4, 0},
			{0, 4, 0, 0},
			{0, 0, 0, 0},
			{0, 0, 0, 0},
		};
	}
	
	static int[][] L() {
		return new int[][] {
			{0, 5, 0, 0},
			{0, 5, 0, 0},
			{0, 5, 5, 0},
			{0, 0, 0, 0},
		};
	}
	
	static int[][] S() {
		return new int[][] {
			{0, 6, 6, 0},
			{6, 6, 0, 0},
			{0, 0, 0, 0},
			{0, 0, 0, 0},
		};
	}
	
	static int[][] Z() {
		return new int[][] {
			{7, 7, 0, 0},
			{0, 7, 7, 0},
			{0, 0, 0, 0},
			{0, 0, 0, 0},
		};
	}
	
	
	
	
}
