import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;

public class Main {
    
    public static void main (String[]args) {

        VentanaPrincipal vp = new VentanaPrincipal();
        vp.setVisible(true);
        vp.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        /*

        int ancho, largo;
        final Image fondoMenu;
        Image fondo;
        try {
            fondo = ImageIO.read(new File("images/menu.png"));
        } catch (IOException e) {
            fondo = null;
            e.printStackTrace();
        }

        fondoMenu = fondo.getScaledInstance(1024, 768, Image.SCALE_DEFAULT);
        JPanel menu = new JPanel(){
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                g.drawImage(fondoMenu, 0, 0, null);
            }
        };
        ancho = fondoMenu.getWidth(null);
        largo = fondoMenu.getHeight(null);
        JFrame frame = buildFrame(ancho, largo);

        JButton jugar = new JButton("Jugar");
        jugar.setBounds(20, 20, 30, 15);

        frame.add(menu);
        frame.add(jugar);

        menu.setVisible(true);
        jugar.setVisible(true);

        */
    }
    private static JFrame buildFrame(int ancho, int largo) {
        JFrame frame = new JFrame();
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setSize(ancho+18, largo+20);
        frame.setVisible(true);
        return frame;
    }
}
