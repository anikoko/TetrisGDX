package tetris;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiConsumer;

public class TetrisModel implements GameEventsListener {

	public static final int DEFAULT_HEIGHT = 20;
	public static final int DEFAULT_WIDTH = 10;
	public static final int DEFAULT_COLORS_NUMBER = 7;

	int maxColors;
	int dropScore = 0;
	int linesClearedScore = 0;
	int numberOfLinesCleared = 0;
	int tenLinesCleared = 0;
	public int fullScore = 0;
	public int level = 0;
	public boolean gameOver = false;
	public TetrisState state = new TetrisState();
	final List<ModelListener> listeners = new ArrayList<>();
	final List<StageListener> stageListeners = new ArrayList<>();
	
	
	
	public void initModel(int width, int height, int maxColors) {
		dropScore = 0;
		linesClearedScore = 0;
		numberOfLinesCleared = 0;
		tenLinesCleared = 0;
		fullScore = 0;
		level = 0;
		gameOver = false;
		state = new TetrisState();
		this.state.width = width;
		this.state.height = height;
		this.maxColors = maxColors;
		state.field = new int[height][width];
		initFigure();
	}

	public void addListener(ModelListener listener) {
		listeners.add(listener);
	}

	public void removeListener(ModelListener listener) {
		listeners.remove(listener);
	}
	
	public TetrisModel(int width, int height, int maxColors) {
		this.state.width = width;
		this.state.height = height;
		this.maxColors = maxColors;
		state.field = new int[height][width];
		initFigure();
	}

	private void initFigure() {
		state.randomInt = 1 + (int)(Math.random() * ((DEFAULT_COLORS_NUMBER - 1) + 1));
		while(!noFullRows()) {
			removeFullRow();
		}
		calculateScore();
		
		state.figure = FigureFactory.createNextFigure(state.randomInt);
		state.position = new Pair(state.width / 2 - 2, 0);
	}

	public void calculateScore() {
		switch (numberOfLinesCleared) {
		case 0: {
			break;
		}
		case 1: {
			linesClearedScore = 40*(level+1);
			break;
		}
		case 2: {
			linesClearedScore = 100*(level+1);
			break;
		}
		case 3: {
			linesClearedScore = 300*(level+1);
			break;
		}
		case 4: {
			linesClearedScore = 1200*(level+1);
			break;
		}
		default:
			throw new IllegalArgumentException("Unexpected value: " + numberOfLinesCleared);
		}
		fullScore += linesClearedScore + dropScore;
		numberOfLinesCleared = 0;
		
		for (ModelListener modelListener : listeners) {
			modelListener.scoreHasChanged(fullScore);
		}
	
	}

	public Pair size() {
		return new Pair(state.width, state.height);
	}

	@Override
	public void slideDown() {
		var newPosition = new Pair(state.position.x, state.position.y + 1);
		if (isNewFiguresPositionValid(newPosition, state.figure)) {
			state.position = newPosition;
			notifyListeners();
		} else {
			pasteFigure();
			initFigure();
			if (!isNewFiguresPositionValid(state.position, state.figure)) {
				gameOver();
				return;
			}
			notifyListeners();
		}
	}

	private void notifyListeners() {
		listeners.forEach(listener -> listener.onChange(this));
	}

	private void gameOver() {
		gameOver = true;
		for (ModelListener modelListener : listeners) {
			modelListener.gameOver();
		}

	}

	@Override
	public void moveLeft() {
		var newPosition = new Pair(state.position.x - 1, state.position.y);
		if (isNewFiguresPositionValid(newPosition, state.figure)) {
			state.position = newPosition;
			notifyListeners();
		}
	}

	@Override
	public void moveRight() {
		var newPosition = new Pair(state.position.x + 1, state.position.y);
		if (isNewFiguresPositionValid(newPosition, state.figure)) {
			state.position = newPosition;
			notifyListeners();
		}
	}

	@Override
	public void rotate() {
		if (state.randomInt == 1) return;
		int[][] rotatedFigure = new int[4][4];
		rotateElementsRight(rotatedFigure);
		if (isNewFiguresPositionValid(state.position, rotatedFigure)) {
			state.figure = rotatedFigure;
		} else {
			changePositionAndRotate(rotatedFigure);
		}
		notifyListeners();
	}

	public void changePositionAndRotate(int[][] rotatedFigure) {
		if (isNewFiguresPositionValid(new Pair(state.position.x+1, state.position.y), rotatedFigure)) { 
			state.position = new Pair(state.position.x+1, state.position.y);
			rotate();	
		} else if (isNewFiguresPositionValid(new Pair(state.position.x+2, state.position.y), rotatedFigure)) {
			state.position = new Pair(state.position.x+2, state.position.y);
			rotate();
		} else {
			state.position = new Pair(state.position.x-1, state.position.y);
			rotate();
		}
	}

	public void rotateElementsRight(int[][] rotatedFigure) {
		for (int r = 0; r < state.figure.length; r++) {
			for (int c = 0; c < state.figure[r].length; c++) {
				rotatedFigure[c][state.figure[0].length - 1 - r] = state.figure[r][c];
			}
		}
	}
	

	@Override
	public void drop() {
		
		dropFigure(state.position);
		notifyListeners();
	}
	
	public void dropFigure(Pair position) {
		Pair newPosition = new Pair(position.x, position.y + 1);
		if (!isNewFiguresPositionValid(newPosition, state.figure)) {
			state.position = new Pair(newPosition.x, newPosition.y - 1);;
			return; // dropped
		}
		dropScore++;
		dropFigure(newPosition);

	}

	public boolean isNewFiguresPositionValid(Pair newPosition, int[][] figure) {

		boolean[] result = new boolean[1];
		result[0] = true;

		walkThroughAllFigureCells(newPosition, (absPos, relPos) -> {
			if (result[0]) {
				result[0] = checkAbsPos(absPos);
			}
		}, figure);

		return result[0];
	}

	private void walkThroughAllFigureCells(Pair newPosition, BiConsumer<Pair, Pair> payload, int[][] figure) {
		for (int row = 0; row < figure.length; row++) {
			for (int col = 0; col < figure[row].length; col++) {
				if (figure[row][col] == 0)
					continue;
				int absCol = newPosition.x + col;
				int absRow = newPosition.y + row;
				payload.accept(new Pair(absCol, absRow), new Pair(col, row));
			}
		}
	}

	private boolean checkAbsPos(Pair absPos) {
		var absCol = absPos.x;
		var absRow = absPos.y;
		if (isColumnPositionOutOfBoundaries(absCol))
			return false;
		if (isRowPositionOutOfBoundaries(absRow))
			return false;
		if (state.field[absRow][absCol] != 0)
			return false;
		return true;
	}

	private boolean isRowPositionOutOfBoundaries(int absRow) {
		return absRow < 0 || absRow >= state.height;
	}
	
	private boolean isColumnPositionOutOfBoundaries(int absCol) {
		return absCol < 0 || absCol >= state.width;
	}

	public void pasteFigure() {
		walkThroughAllFigureCells(state.position, (absPos, relPos) -> {
			state.field[absPos.y][absPos.x] = state.figure[relPos.y][relPos.x];
		}, state.figure);
	}
	
	public void addStageListener(StageListener listener) {
		stageListeners.add(listener);
	}
	
	public void removeFullRow() {
		int fullRow = findFullRow();
		if (fullRow != 0) {
			for (int j = 0; j < state.field[fullRow].length; j++) {
				state.field[fullRow][j] = 0;
			}
			for (int i = fullRow-1; i >= 0; i--) {
				state.field[i+1] = state.field[i];
			}
			
			numberOfLinesCleared++;
			tenLinesCleared++;
			if (tenLinesCleared == 1) {
				level++;
				tenLinesCleared = 0;
				for (ModelListener modelListener : listeners) {
					modelListener.levelHasChanged(level);
				}
				for (StageListener stageListener : stageListeners) {
					stageListener.levelHasChanged(level);
				}
				
			}
		}
		notifyListeners();
		
	}

	public boolean isRowFull(int[] fieldRow) {
		for (int j = 0; j < fieldRow.length; j++) {
			if (fieldRow[j] == 0) {return false; }
			}
		return true;
	}
	
	public int findFullRow() {
		for (int i = 0; i < state.field.length; i++) {
			if (isRowFull(state.field[i])) { 
				return i;
			}
		}
		return 0;	
	}
	
	public boolean noFullRows() {
		boolean noFullRows = true;
		for (int i = 0; i < state.field.length; i++) {
			if (isRowFull(state.field[i])) noFullRows = false;
		}
		return noFullRows;
	}
	

}
