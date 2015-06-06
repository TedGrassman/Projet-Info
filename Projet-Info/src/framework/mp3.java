package framework;

import java.io.File;

import javafx.application.Application;
import javafx.embed.swing.JFXPanel;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.stage.Stage;

public class mp3 extends Application {
	MediaPlayer player;

	public mp3(String nomFichier) { // son mp3. fichier � placer dans le dossier res
		@SuppressWarnings("unused")
		final JFXPanel fxPanel = new JFXPanel(); // n�cessaire pour utiliser javaFX
		Media son;
		try { // code tir� du site d'Oracle, gestion d'erreurs d'import fichier
			son = new Media(new File(nomFichier).toURI().toString());
			if (son.getError() == null) {
				son.setOnError(new Runnable() {
					public void run() {
						// Handle asynchronous error in Media object.
					}
				});
				try {
					player = new MediaPlayer(son);
					if (player.getError() == null) {
						player.setOnError(new Runnable() {
							public void run() {
								// Handle asynchronous error in MediaPlayer object.
							}
						});

					} else {
						// Handle synchronous error creating MediaPlayer.
					}
				} catch (final Exception mediaPlayerException) {
					throw new RuntimeException(mediaPlayerException);
					// Handle exception in MediaPlayer constructor.
				}
			} else {
				// Handle synchronous error creating Media.
			}
		} catch (final Exception mediaException) {
			throw new RuntimeException(mediaException);
			// Handle exception in Media constructor.
		}
		
		
	}

	public void jouer() {
		player.setVolume(1.0);
		player.play();
	}

	public void stop() {
		player.setVolume(0.0);
		player.stop();
	}

	public void boucle() {
		player.setCycleCount(MediaPlayer.INDEFINITE);
	}
	
	public void fadeOut(){
		
		Thread fadeOutThread = new Thread("FadeOut Sound") {
			public void run() {
				threadedFadeOut();
			}
		};
		fadeOutThread.start();
	}
	public void fadeIn(){
		
		Thread fadeInThread = new Thread("FadeIn Sound") {
			public void run() {
				threadedFadeIn();
			}
		};
		fadeInThread.start();
	}
	
	public void threadedFadeOut() {
		
		//		while(player.getVolume()>0.0){
		//		player.setVolume(player.getVolume() - 0.000005);
		//	}
		player.setVolume(1.0);
		System.out.println("Starting Fading Out");
		while(player.getVolume()>0.0){
			player.setVolume(player.getVolume() - 0.01);
			
			try {
				Thread.sleep(10);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		System.out.println("Faded Out");
		player.stop();
	}
	
	public void threadedFadeIn() {
		
		//		while(player.getVolume()>0.0){
		//		player.setVolume(player.getVolume() - 0.000005);
		//	}
		player.setVolume(0.0);
		player.play();
		System.out.println("Starting Fading In");
		while(player.getVolume()<1.0){
			player.setVolume(player.getVolume() + 0.01);
			try {
				Thread.sleep(10);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		System.out.println("Faded In");
	}

	@Override
	public void start(Stage arg0) throws Exception {
		// TODO Auto-generated method stub

	}
	
	

}
