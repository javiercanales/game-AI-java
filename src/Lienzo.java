import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.IOException;
import java.util.Timer;

/**
 * En esta clase se define la ventana de trabajo, sobre la cual
 * se define el tamaño y color de fondo.
 * Se decide dejar los métodos listener (de teclado u otros) aquí
 * en vez de en la clase Escenario, para simplificar código.
 */

public class Lienzo extends Canvas implements Constantes {

    public VentanaJuego ventanaJuego;
    public JFrame ventanaMenu;
    public Escenario escenario;
    public Image fondo;

    public Graphics graficoBuffer;
    public Image imagenBuffer;
    public Timer lanzadorJugadorIA;
    public Thread hilo;
    public Timer lanzadorAdversariosIA;

    public static boolean hardMode;
    public static boolean playerIA;
    private boolean exit;


    public boolean buscarJugador;

    public Lienzo(VentanaJuego ventanaJuego, JFrame ventanaMenu, boolean hardMode, boolean playerIA) {
        this.hardMode = hardMode;
        this.playerIA = playerIA;

        buscarJugador = false;

        try {
            fondo = ImageIO.read(new File("images/fondo-10.jpg"));
            fondo = fondo.getScaledInstance(getAncho(), getLargo(), Image.SCALE_SMOOTH);
            System.out.println("Leido ---------------------");
        } catch (IOException error) {
            System.out.println("Error al cargar el fondo!!!");
        }
        this.ventanaJuego = ventanaJuego;
        this.ventanaMenu = ventanaMenu;
        escenario = new Escenario(this);

        this.setSize(ANCHURA_ESCENARIO,LARGO_ESCENARIO);
        this.setBackground(Color.yellow);

        //Para que tome los eventos de un comienzo
        this.setFocusable(true);

        //Escuchador de eventos de teclado
        this.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent evt) {
                moverCelda(evt);
                repaint();
            }
        });
        //Escuchador de eventos de mouse
        this.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent evt) {
                activarCelda(evt);
                repaint();
            }
        });
        exit = false;
        hilo = new Thread(() -> {
            while (!exit) {
                synchronized (this) {
                    if (escenario.cervezasRestantes == 0 && escenario.jugador.getSedDeHomero() != 0) {
                        escenario.restablecerDuffs();
                    }
                    if (escenario.jugador.getSedDeHomero() == 0) {
                        buscarJugador = true;
                    }
                    if (escenario.jugador.getSedDeHomero() > 20) {
                        escenario.detenerJuego();
                        JOptionPane.showMessageDialog(null, "PERDEDOR! -demonios Barney! me las vas a pagar...- pensó Homero");
                        exit = true;
                    }
                }
            }
            System.out.println("Finalizando hilo --------------------");
        });
        hilo.start();

        //Reloj de tiempo restante
        escenario.reloj = new Reloj(this.escenario);
        escenario.relojTiempoJuego = new Timer();
        escenario.relojTiempoJuego.scheduleAtFixedRate(escenario.reloj, 0, 1000);
        if (hardMode) escenario.reloj.setHardTime();

        int period;
        if (hardMode && playerIA) period = 900;
        else if (hardMode) period = 500;
        else period = 1200;
        //Vida a los adversarios, con inteligencia
        lanzadorAdversariosIA = new Timer();
        for(int i=0; i<escenario.CANTIDAD_REAL_ADVERSARIOS; i++) {
            lanzadorAdversariosIA.scheduleAtFixedRate(escenario.adversarios[i].inteligencia, 200, period);
        }
        if (playerIA) {
            //Jugador con inteligencia
            lanzadorJugadorIA = new Timer();
            if (hardMode) lanzadorJugadorIA.scheduleAtFixedRate(escenario.jugador.inteligencia,200,700);
            else lanzadorJugadorIA.scheduleAtFixedRate(escenario.jugador.inteligencia,300,350);

        }
    }

    /*
    //para evitar que pestañee al mover el jugador (o cambiar algo gráfico en general)
    //despues debe cambiarse al metodo con doble buffer (se requerira)
    @Override
    public void update(Graphics g) {
        paint(g);
    }
    */
    @Override
    public void update(Graphics g) {
        //inicialización del buffer gráfico mediante la imagen
        if(graficoBuffer==null){
            imagenBuffer=createImage(this.getWidth(),this.getHeight());
            graficoBuffer=imagenBuffer.getGraphics();
        }
        //volcamos color de fondo e imagen en el nuevo buffer grafico
        graficoBuffer.setColor(getBackground());
        graficoBuffer.fillRect(0,0,this.getWidth(),this.getHeight());
        graficoBuffer.drawImage(fondo, 0, MARGEN_LARGO_BARRA, null);
        escenario.update(graficoBuffer);
        //pintamos la imagen previa
        g.drawImage(imagenBuffer, 0, 0, null);
    }

    //metodo paint para pintar el escenario
    @Override
    public void paint(Graphics g) {
        //g.drawImage(fondo, 0, MARGEN_LARGO_BARRA, null);
        escenario.paintComponent(g);
    }

    private void activarCelda(MouseEvent evt) {
        for(int i=0; i < NUMERO_CELDAS_ANCHO; i++)
            for (int j=0; j < NUMERO_CELDAS_LARGO; j++)
                if(escenario.celdas[i][j].comprobarSiCeldaSeleccionada(evt.getX(), evt.getY())){
                    escenario.jugador.desplazamientoMouse(i, j);
                }
    }

    private void moverCelda(KeyEvent evento) {
        switch(evento.getKeyCode()) {
            case KeyEvent.VK_UP:
                //Movimiento del adversario en reacción al jugador
                //escenario.adversarios[randomValue(0, NUMERO_ADVERSARIOS-1)].moverCeldaArriba();
                //Movimiento jugador
                escenario.jugador.moverCeldaArriba();
                break;
            case KeyEvent.VK_DOWN:
                //Movimiento del adversario en reacción al jugador
                //escenario.adversarios[randomValue(0, NUMERO_ADVERSARIOS-1)].moverCeldaAbajo();
                //Movimiento jugador
                escenario.jugador.moverCeldaAbajo();
                break;
            case KeyEvent.VK_LEFT:
                //Movimiento del adversario en reacción al jugador
                //escenario.adversarios[randomValue(0, NUMERO_ADVERSARIOS-1)].moverCeldaIzquierda();
                //Movimiento jugador
                escenario.jugador.moverCeldaIzquierda();
                break;
            case KeyEvent.VK_RIGHT:
                //Movimiento del adversario en reacción al jugador
                //escenario.adversarios[randomValue(0, NUMERO_ADVERSARIOS-1)].moverCeldaDerecha();
                //Movimiento jugador
                escenario.jugador.moverCeldaDerecha();
                break;
        }
    }
}
