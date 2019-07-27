import javax.swing.JFrame;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class VentanaJuego extends JFrame implements Constantes {
    
    //nuestra clase se compone de un lienzo de dibujo (heredada de canvas)
    public Lienzo lienzo;
    //reproductor de música
    public HiloMusica player;
    
    //constructor
    public VentanaJuego(JFrame ventanaMenu, boolean hardMode, boolean playerIA) {
        lienzo = new Lienzo(this, ventanaMenu, hardMode, playerIA);

        this.getContentPane().add(lienzo);
        //el tamaño de la ventana es la del escenario y el incremento de los bordes
        this.setSize(lienzo.getWidth(),lienzo.getHeight());
        this.setExtendedState(JFrame.MAXIMIZED_BOTH);
        this.setTitle("Homero's Duff challenge game screen!");
        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        this.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                super.windowClosing(e);
                lienzo.escenario.detenerJuego();
            }
            @Override
            public void windowClosed(WindowEvent e) {
                super.windowClosing(e);

            }
        });

        player = new HiloMusica(RUTA+"/music/simpson8bit.wav",2);
        player.run();
    }

}
