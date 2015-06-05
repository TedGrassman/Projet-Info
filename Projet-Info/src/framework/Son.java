package framework;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

public class Son {
	Clip clip;

	public Son(String nomFichier) { // fonctionne uniquement avec les fichiers
									// .wav !
		try {
			final File file = new File(nomFichier);
			if (file.exists()) {
				final AudioInputStream sound = AudioSystem.getAudioInputStream(file);
				clip = AudioSystem.getClip(); // charge le son en mémoire
				clip.open(sound);
			} else {
				throw new RuntimeException("Fichier son non trouvé :" + nomFichier);
			}
		} catch (final MalformedURLException e) {
			e.printStackTrace();
			throw new RuntimeException("URL son invalide !" + e);
		} catch (final UnsupportedAudioFileException e) {
			e.printStackTrace();
			throw new RuntimeException("Fichier son non supporté" + e);
		} catch (final IOException e) {
			e.printStackTrace();
			throw new RuntimeException("Erreur IO" + e);
		} catch (final LineUnavailableException e) {
			e.printStackTrace();
			throw new RuntimeException("Line Unavailable " + e);
		}
	}

	public void jouer() { // joue le son
		clip.setFramePosition(0); // repart de 0
		clip.start();
	}

	public void boucle() { // fait jouer le son en boucle
		clip.loop(Clip.LOOP_CONTINUOUSLY);
	}

	public void stop() { // arrête la lecture
		clip.stop();
	}

}
