import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;

public class Main {
    
    public static void main (String[]args) {

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
        ancho = fondoMenu.getWidth(null);
        largo = fondoMenu.getHeight(null);

        JFrame frame = buildFrame(ancho, largo);
        JPanel panel = new JPanel() {
            public void paintComponent(Graphics g) {
                super.paintComponent(g);
                g.drawImage(fondoMenu, 0, 0,this);
            }
        };
        frame.setContentPane(panel);

        panel.setLayout(null);

        JButton jugar = new JButton("Jugar! (EASY)");
        jugar.setBounds(360, 15, 125, 30);
        jugar.setBackground(Color.ORANGE);
        jugar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                VentanaJuego ventanaJuego = new VentanaJuego(frame, false, true);
                frame.setVisible(false);
                ventanaJuego.setVisible(true);
            }
        });

        JButton jugarHard = new JButton("Jugar! (HARD)");
        jugarHard.setBounds(560, 15, 125, 30);
        jugarHard.setBackground(Color.DARK_GRAY);
        jugarHard.setForeground(Color.RED);
        jugarHard.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                VentanaJuego ventanaJuego = new VentanaJuego(frame, true, true);
                frame.setVisible(false);
                ventanaJuego.setVisible(true);
            }
        });

        JButton jugarSinIA = new JButton("Jugador sin IA (HARD)");
        jugarSinIA.setBounds(760, 15, 170, 30);
        jugarSinIA.setBackground(Color.BLACK);
        jugarSinIA.setForeground(Color.RED);
        jugarSinIA.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                VentanaJuego ventanaJuego = new VentanaJuego(frame, true, false);
                frame.setVisible(false);
                ventanaJuego.setVisible(true);
            }
        });


        panel.add(jugar);
        panel.add(jugarHard);
        panel.add(jugarSinIA);
        frame.setTitle("Mmm, cerveza's Homero challenge!");
        frame.setVisible(true);

    }
    private static JFrame buildFrame(int ancho, int largo) {
        JFrame frame = new JFrame();
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setSize(ancho+18, largo+20);
        return frame;
    }
}
