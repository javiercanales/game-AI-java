import javax.imageio.ImageIO;
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
   
    public Escenario escenario;
    public Image fondo;

    public Graphics graficoBuffer;
    public Image imagenBuffer;
    public Timer lanzadorTareas;

    public Lienzo() {
        try {
            fondo = ImageIO.read(new File("images/fondo.png"));
            fondo = fondo.getScaledInstance(getAncho(), getLargo(), Image.SCALE_SMOOTH);
            System.out.println("Leido ---------------------");
        } catch (IOException error) {
            System.out.println("Error al cargar el fondo!!!");
        }

        escenario = new Escenario(this);

        this.setSize(ANCHURA_ESCENARIO,LARGO_ESCENARIO);
        this.setBackground(Color.yellow);

        //Para que tome los eventos de un comienzo
        this.setFocusable(true);

        //Escuchador de eventos de teclado
        addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent evt) {
                moverCelda(evt);
                repaint();
            }
        });
        //Escuchador de eventos de mouse
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent evt) {
                activarCelda(evt);
                repaint();
            }
        });

        lanzadorTareas = new Timer();
        lanzadorTareas.scheduleAtFixedRate(escenario.jugador.inteligencia,1000,500);
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
                escenario.adversarios[randomValue(0, NUMERO_ADVERSARIOS-1)].moverCeldaArriba();
                //Movimiento jugador
                escenario.jugador.moverCeldaArriba();
                break;
            case KeyEvent.VK_DOWN:
                //Movimiento del adversario en reacción al jugador
                escenario.adversarios[randomValue(0, NUMERO_ADVERSARIOS-1)].moverCeldaAbajo();
                //Movimiento jugador
                escenario.jugador.moverCeldaAbajo();
                break;
            case KeyEvent.VK_LEFT:
                //Movimiento del adversario en reacción al jugador
                escenario.adversarios[randomValue(0, NUMERO_ADVERSARIOS-1)].moverCeldaIzquierda();
                //Movimiento jugador
                escenario.jugador.moverCeldaIzquierda();
                break;
            case KeyEvent.VK_RIGHT:
                //Movimiento del adversario en reacción al jugador
                escenario.adversarios[randomValue(0, NUMERO_ADVERSARIOS-1)].moverCeldaDerecha();
                //Movimiento jugador
                escenario.jugador.moverCeldaDerecha();
                break;
        }
    }
}
