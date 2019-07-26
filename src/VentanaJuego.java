import javax.swing.JFrame;
import java.awt.*;

public class VentanaJuego extends JFrame implements Constantes {
    
    //nuestra clase se compone de un lienzo de dibujo (heredada de canvas)
    public Lienzo lienzo;
    //reproductor de música
    public HiloMusica player;
    
    //constructor
    public VentanaJuego(JFrame ventanaMenu) {
        lienzo = new Lienzo(this, ventanaMenu);
        this.getContentPane().add(lienzo);
        //el tamaño de la ventana es la del escenario y el incremento de los bordes
        this.setSize(lienzo.getWidth(),lienzo.getHeight());
        this.setExtendedState(JFrame.MAXIMIZED_BOTH);
        this.setTitle("Homero's Duff journey begins!");

        player = new HiloMusica(RUTA+"/music/simpson8bit.wav",2);
        player.run();
    }

}
