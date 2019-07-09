import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.JComponent;

public class Celda extends JComponent implements Constantes {
    //atributos
    public int x;
    public int y;
    public char tipo;
    public boolean celdaSelec;

    public BufferedImage jugador, obstaculo, camino, adversario, recompensa, casa;

    public int indexSprite;
    public BufferedImage sprites[], imagenSprites, imagenSpritesAdversario, spritesAdversario[];
    
    //constructor, inicializa los atributos
    public Celda(int x, int y) {
        this.x=x;
        this.y=y;
        this.tipo=CAMINO;
        this.celdaSelec =false;

        indexSprite = 0;   //indice que corresponde a una subimagen de frente

        try {
            jugador = ImageIO.read(new File("images/jugador.png"));
            obstaculo = ImageIO.read(new File("images/obstaculo.png"));
            adversario = ImageIO.read(new File("images/adversario.png"));
            recompensa = ImageIO.read(new File("images/recompensa.png"));
            casa = ImageIO.read(new File("images/casa.png"));

            imagenSprites = ImageIO.read(new File("images/sprite_jugador.png"));
            imagenSpritesAdversario = ImageIO.read(new File("images/sprite_adversario.png"));
            //creo una array de 4 x 1
            sprites = new BufferedImage[4 * 1];
            spritesAdversario = new BufferedImage[4 * 1];
            //lo recorro separando las imagenes
            for (int i = 0; i < 1; i++) {
                for (int j = 0; j < 4; j++) {
                    sprites[(i * 4) + j] = imagenSprites.getSubimage(i * 64, j * 64,64, 64);
                    spritesAdversario[(i * 4) + j] = imagenSpritesAdversario.getSubimage(i * 64, j * 64, 64, 64);
                }
            }
            //adversario = spritesAdversario[indexSprite];
        } catch (IOException error) {
            System.out.println("Error: " + error.toString());
        }
    }

    //El char 'S' define a los cuadros libres del escenario (Scenario, stage)
    public boolean isDisponible() {
        if (tipo == CAMINO) return true;
        else return false;
    }

    public void setIndexSprite(int indexSprite) {
        this.indexSprite=indexSprite;
    }
    public int getIndexSprite() {
        return indexSprite;
    }

    public void setJugador() {
        tipo=JUGADOR;
    }
    public void setObstaculo() {
        tipo=OBSTACULO;
    }
    public void setAdversario() {
        tipo=ADVERSARIO;
    }
    public void setRecompensa() {
        tipo=RECOMPENSA;
    }
    public void setCasa() {
        tipo=CASA;
    }
    public void setFinal() {
        tipo=FINAL;
    }
    public void setCamino() {
        tipo=CAMINO;
    }

    //metodo de JComponent para pintar en un contexto grafico
    @Override
    public void paintComponent(Graphics g) {
        //Switch para seleccionar tipo de cuadro a pintar
        switch (tipo) {
            case JUGADOR:
                g.drawImage(jugador,x,y,null);
                break;
            case OBSTACULO:
                g.drawImage(obstaculo,x,y,null);
                break;
            case RECOMPENSA:
                g.drawImage(recompensa,x,y,null);
                break;
            case ADVERSARIO:
                g.drawImage(adversario,x,y,null);
                break;
            case CASA:
                g.drawImage(casa,x,y,null);
                break;
        }
    }
    //para evitar que pestañee al mover el jugador (o cambiar algo gráfico en general)
    @Override
    public void update(Graphics g) {
        paint(g);
    }

    //Si el click se encuentra sobre la celda
    public boolean comprobarSiCeldaSeleccionada(int clickX,int clickY) {
        Rectangle rectanguloCelda = new Rectangle(x, y, PIXEL_CELDA, PIXEL_CELDA);
        if ( rectanguloCelda.contains(new Point(clickX,clickY))) {
            celdaSelec = !celdaSelec;
        }
        return celdaSelec;
    }
}
