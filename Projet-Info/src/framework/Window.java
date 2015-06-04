package framework;

import java.awt.GraphicsEnvironment;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.io.File;

import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;

/**
 * Creates frame and set its properties.
 * 
 * @author www.gametutorial.net
 */

@SuppressWarnings("serial")
public class Window extends JFrame{
        
    @SuppressWarnings("unused")
	public Window()
    {
        // Sets the title for this frame.
        this.setTitle("Star Shooter");
		try {						// R�cup�ration de l'ic�ne du programme
			this.setIconImage(ImageIO.read(new File("res/icon_32x32.png")));
		} catch (Exception err) {
			System.err.println("Ic�ne introuvable !");
		}
        
        // Sets size of the frame.
        if(true) // Full screen mode
        {
            // Disables decorations for this frame.
            this.setUndecorated(true);
            // Puts the frame to full screen.
            this.setExtendedState(this.MAXIMIZED_BOTH);
        }
        else // Window mode
        {
            // Size of the frame.

            this.setSize((int)GraphicsEnvironment.getLocalGraphicsEnvironment().getMaximumWindowBounds().getWidth(), (int)GraphicsEnvironment.getLocalGraphicsEnvironment().getMaximumWindowBounds().getHeight());
            // Puts frame to center of the screen.
            this.setLocationRelativeTo(null);
            // So that frame cannot be resizable by the user.
            this.setResizable(true);
        }
        
        // Exit the application when user close frame.
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // Arr�t du thread � la fermeture de la fen�tre
        
        // Creates the instance of the Framework.java that extends the Canvas.java and puts it on the frame.
        this.setContentPane(new Framework());
        this.setVisible(true);
        this.addComponentListener(new ComponentListener() {
            public void componentResized(ComponentEvent e) {
            	Framework.resized = true;

            	//if(Framework.gameState==Framework.GameState.PLAYING){
                Framework.gameState=Framework.GameState.VISUALIZING;
            	//}
            }

			@Override
			public void componentMoved(ComponentEvent e) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void componentShown(ComponentEvent e) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void componentHidden(ComponentEvent e) {
				// TODO Auto-generated method stub
				
			}
        });
    }
    public static void main(String[] args)  {
    	
        // Use the event dispatch thread to build the UI for thread-safety.
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new Window();
            }
        });
    }
    
}
