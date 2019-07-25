import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

public class HiloMusica extends Thread {
    public Clip cancion;
    public String ruta;
    public int repeticiones;

    public HiloMusica (String ruta,int repeticiones) {
        this.ruta = ruta;
        this.repeticiones = repeticiones;
    }

    @Override
    public void run() {
        try {
            URL url = new URL(ruta);
            AudioInputStream audioIn = AudioSystem.getAudioInputStream(url);
            cancion = AudioSystem.getClip();
            cancion.open(audioIn);
            cancion.loop(repeticiones);
        } catch (MalformedURLException murle) {
            System.out.println(murle.toString());
        } catch(UnsupportedAudioFileException | IOException | LineUnavailableException e) {
            System.out.println(e.toString());
        }
    } //fin del método run
}