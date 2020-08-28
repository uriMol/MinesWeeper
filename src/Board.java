import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.HashSet;
import java.util.Random;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class Board extends JPanel{
	
	/*
	 * Java MinesWeeper game
	 * 
	 * Made by uMolcho
	 * ***Not finished - should add starting page
	 */

	private int NUM_OF_COLS;
	private int NUM_OF_ROWS;
	private int NUM_OF_MINES;
	private final int CELL_SIZE = 15;
	
	private final int MINE = 9;
	
	private int BOARD_WIDTH;
    private int BOARD_HEIGHT;
    
    private Image[] imgs = new Image[13];
    
    private int allCells;
    private int minesLeft;
    private Cell[] field;
    private boolean inGame;
    
    private JLabel statusBar;
    
    /*
     * Sets up the board according to the game level
     */
    public Board(JLabel statusBar, GameLevel level) {
    	this.NUM_OF_ROWS = level.getRows();
    	this.NUM_OF_COLS = this.NUM_OF_ROWS;
    	this.NUM_OF_MINES = level.getMines();
    	
    	BOARD_WIDTH = NUM_OF_COLS * CELL_SIZE + 1;
    	BOARD_HEIGHT = NUM_OF_COLS * CELL_SIZE + 1;
    	this.statusBar = statusBar;
    	
    	for (int i = 0; i < 13; i++) {

            String path = "resources/" + i + ".png";
            imgs[i] = (new ImageIcon(path)).getImage();
        }
    	setPreferredSize(new Dimension(BOARD_WIDTH, BOARD_HEIGHT));

        addMouseListener(new MinesAdapter());
        
        setNewGame();
    }


    /*
     * Sets up the new game - 
     * Constructing the cells,
     * Updates the status bar,
     * Sets the mines
     */
	private void setNewGame() {
		
		inGame = true;
		minesLeft = NUM_OF_MINES;
		allCells = NUM_OF_ROWS*NUM_OF_COLS;
		field = new Cell[allCells];
		for (int i = 0; i < allCells; i++) {
			this.field[i] = new Cell();
		}
		
		statusBar.setText(Integer.toString(this.minesLeft));
		setMines();
		
	}
	

	/*
	 * Randomly sets the mines and 
	 * updates their neighbors
	 */
	private void setMines() {
		
		Cell cell;
		Random random = new Random();
		int i = 0;
		int j;
		while (i < this.NUM_OF_MINES) {
			j = random.nextInt(this.allCells);
			cell = this.field[j];
			if (!cell.isMine()) {
				cell.setType(MINE);
				updateCellsArea(j);
				i++;	
			}
		}
	}
	
	private int[] getArea(int j){
		int i = 0;
		int[] res = new int[10];
		int row = j / NUM_OF_ROWS;
		int col = j % NUM_OF_ROWS;
		for (int k = Math.max(0, row - 1); 
				k < Math.min(NUM_OF_ROWS, row + 2); k++) {
			for (int l = Math.max(0, col - 1); 
					l < Math.min(NUM_OF_COLS, col + 2); l++) {
				if (k != row || l != col) {
					res[i] = (NUM_OF_COLS * k) + l;
					i++;
				}
			}
		}
		int[] result = new int[i];
		for (int s = 0; s < i; s++) {
			result[s] = res[s];
		}
		return result;
	}
	
	private void updateCellsArea(int j) {
		for (int index : getArea(j)) {
			if (this.field[index].getType() != MINE) {
				this.field[index].appendType();
			}
		}
	}

	
	@Override
    public void paintComponent(Graphics g) {
		
		int dealtCells = 0;
		
		
        for (int i = 0; i < NUM_OF_ROWS; i++) {
            for (int j = 0; j < NUM_OF_COLS; j++) {
                Cell cell = field[(i * NUM_OF_COLS) + j];
                int type = cell.getType();
                int cellCase = type;
                if ((!cell.isHidden()) && type == MINE) {
                    inGame = false;
                }

                if (!inGame) {
                	if(type == MINE) {
                		if(cell.isMarked()) {
                			cellCase = 11;				//well marked
                		} else {
                			cellCase = 9;				//mine exploded
                		}
                	} else if (cell.isMarked()) {
                			cellCase = 12; 				//wrong mark
                	} else if (cell.isHidden()){
                		cellCase = 10;					//just a hidden cell
                	}
                } else {								//we're in game
                    if (cell.isMarked()) {
                        cellCase = 11;					//a marker
                    } else if (cell.isHidden()) {
                        cellCase = 10;					//just a hidden cell
                    }
                }
                if (cellCase < 10 || cellCase == 11) {
                	dealtCells++;
                }
                g.drawImage(imgs[cellCase], (j * CELL_SIZE),
                        (i * CELL_SIZE), this);
            }
        }
        

        if (dealtCells == allCells && inGame) {

            inGame = false;
            statusBar.setText("Game won");

        } else if (!inGame) {
            statusBar.setText("Game over");
        }
    }
	
	/*
	 * A recursive function to clear the 
	 * area of an "empty" cell
	 */
	public void clearArea(int i, HashSet<Cell> set) {
		for (int index:getArea(i)) {
			
			Cell cell = field[index];
			cell.unhide();
			if (cell.getType() == 0 && 
					!set.contains(cell)) {
				set.add(cell);
				clearArea(index, set);
			}
			
		}
	}
	
	
    private class MinesAdapter extends MouseAdapter {

        @Override
        public void mousePressed(MouseEvent e) {
        	
            int x = e.getX();
            int y = e.getY();

            int cCol = x / CELL_SIZE;
            int cRow = y / CELL_SIZE;
            int index = (cRow * NUM_OF_COLS) + cCol;
            boolean doRepaint = false;

            if (!inGame) {

                setNewGame();
                repaint();
            }

        
            if ((cCol < NUM_OF_COLS) && (cRow < NUM_OF_ROWS)) {
            	
            	
            	if (!(field[index].isHidden())) {
            		return;
            	}
            	

            	
                if (e.getButton() == MouseEvent.BUTTON3) {
                	doRepaint = true;
                    if (field[index].isMarked()) {
                    	minesLeft++;
                    	field[index].mark();
                    } else {
                    	if (minesLeft > 0) {
                    		field[index].mark();
                    		minesLeft--;
                    		statusBar.setText(Integer.toString(minesLeft) + " mines left");
                    	} else  {
                    		statusBar.setText("No marks left!");
                    	}
                    }

                } else {
                    if ((!field[index].isHidden()) || (field[index].isMarked())) {
                        return;
                    }
                    
                    doRepaint = true;
                    if (field[index].getType() == MINE){
                    	inGame = false;
                    } else { 
                    	field[index].unhide();
                    	if(field[index].getType() == 0) {
                    		clearArea(index, new HashSet<Cell>());
                    	}
                    }
                }

                if (doRepaint) {
                    repaint();
                }
            }
        }
    }
	
	

}
