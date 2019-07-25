import java.awt.*;
import java.util.ArrayList;
import java.util.Random;

public interface Constantes {
    //public final int ANCHURA_ESCENARIO=(PIXEL_CELDA*NUMERO_CELDAS_ANCHO)+ANCHO_BORDE_VENTANA;
    //public final int LARGO_ESCENARIO=(PIXEL_CELDA*NUMERO_CELDAS_LARGO)+LARGO_BORDE_VENTANA;
    //public final int ANCHURA_ESCENARIO = 1600;
    //public final int LARGO_ESCENARIO = 900+20+32;

    public final String RUTA = "file:///"+System.getProperty( "user.dir" );

    public final ArrayList<Point> puntosCeldaRestriccionObstaculo = new ArrayList<>();

    public final int ANCHURA_ESCENARIO = widthScreen();
    public final int LARGO_ESCENARIO = heightScreen();
    //size of the cells
    public final int PIXEL_CELDA=128;
    //number of cells - width
    public final int NUMERO_CELDAS_ANCHO = celdasAncho()-1;
    //number of cells - height
    public final int NUMERO_CELDAS_LARGO = celdasLargo()-1;
    //size of the stage
    public final int ANCHO_BORDE_VENTANA=30;
    public final int LARGO_BORDE_VENTANA=50;

    public final int anchuraCelda=64;
    public final int alturaCelda=64;
    public final int anchuraMundoVirtual=10;
    public final int alturaMundoVirtual=7;

    public final char JUGADOR='J';
    public final char CAMINO='C';
    public final char OBSTACULO='O';
    public final char OBSTACULO2='2';
    public final char ADVERSARIO='A';
    public final char RECOMPENSA='R';
    public final char FINAL='F';
    public final char CASA='H';

    public final int FACTOR_ANCHO=3;
    public final int MARGEN_LARGO_BARRA =23;

    public final int NUMERO_OBSTACULOS=(NUMERO_CELDAS_ANCHO*NUMERO_CELDAS_LARGO)/7;
    public final int NUMERO_OBSTACULOS_2=(NUMERO_CELDAS_ANCHO*NUMERO_CELDAS_LARGO)/15;
    public final int NUMERO_ADVERSARIOS=(NUMERO_CELDAS_ANCHO*NUMERO_CELDAS_LARGO)/30;
    public final int NUMERO_CERVEZAS=(NUMERO_CELDAS_ANCHO*NUMERO_CELDAS_LARGO)/18;

    public final Color COLOR_JUGADOR=Color.GREEN;
    public final Color COLOR_OBSTACULOS=Color.BLACK;
    public final Color COLOR_ADVERSARIO=Color.RED;
    public final Color COLOR_RECOMPENSAS=Color.ORANGE;
    public final Color COLOR_CELDAS_LIBRES=Color.DARK_GRAY;
    public final Color COLOR_FINAL=Color.BLUE;

    /* Constantes de puntaje */
    final int CHOQUE_ADVERSARIO = 1;

    default int randomValue(int min, int max) {
        Random random = new Random();
        return random.nextInt((max - min) + 1) + min;
    }
    static int heightScreen(){
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        return screenSize.height;
    }
    static int widthScreen(){
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        return screenSize.width;
    }
    default int getAncho(){
        return widthScreen();
    }
    default int getLargo(){
        return heightScreen();
    }
    static int celdasAncho(){
        return ANCHURA_ESCENARIO/PIXEL_CELDA;
    }
    static int celdasLargo(){
        return LARGO_ESCENARIO/PIXEL_CELDA;
    }
}
