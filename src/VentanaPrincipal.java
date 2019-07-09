import javax.swing.JFrame;
import java.awt.*;

public class VentanaPrincipal extends JFrame implements Constantes {
    
    //nuestra clase se compone de un lienzo de dibujo (heredada de canvas)
    public Lienzo lienzo;
    
    //constructor
    public VentanaPrincipal() {
        lienzo = new Lienzo();
        this.getContentPane().add(lienzo);
        //el tamaño de la ventana es la del escenario y el incremento de los bordes
        this.setSize(lienzo.getWidth(),lienzo.getHeight());
        this.setExtendedState(JFrame.MAXIMIZED_BOTH);
        this.setTitle("Juego IA");
    }

}
