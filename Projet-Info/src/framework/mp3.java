package framework;

import java.io.File;

import javafx.application.Application;
import javafx.embed.swing.JFXPanel;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.stage.Stage;

public class mp3 extends Application {
	MediaPlayer player;

	public mp3(String nomFichier) { // son mp3. fichier à placer dans le dossier
									// res
		final JFXPanel fxPanel = new JFXPanel(); // nécessaire pour utiliser
													// javaFX
		Media son;
		try { // code tiré du site d'Oracle, gestion d'erreurs d'import fichier
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
								// Handle asynchronous error in MediaPlayer
								// object.
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
		player.play();
	}

	public void stop() {
		player.stop();
	}

	public void boucle() {
		player.setCycleCount(MediaPlayer.INDEFINITE);
	}

	@Override
	public void start(Stage arg0) throws Exception {
		// TODO Auto-generated method stub

	}

}
