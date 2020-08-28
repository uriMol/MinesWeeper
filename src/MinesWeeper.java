import java.awt.BorderLayout;
import java.awt.EventQueue;
import javax.swing.JFrame;
import javax.swing.JLabel;

/*
 * Java MinesWeeper game
 * 
 * Made by uMolcho
 * ***Not finished - should add starting page
 */

public class MinesWeeper extends JFrame{
	
	private JLabel statusBar;
	
	public MinesWeeper() {
		statusBar = new JLabel("");
		add(statusBar, BorderLayout.SOUTH);
        add(new Board(statusBar, GameLevel.Begginer)); 	//Currently, game
        setResizable(false);							//level is manually
        pack();											//initialized

        setTitle("MinesWeeper");
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
	
	
	public static void main(String[] args) {

        EventQueue.invokeLater(() -> {

            JFrame ex = new MinesWeeper();
            ex.setVisible(true);
        });
    }
}
