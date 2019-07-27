import javafx.util.Pair;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Timer;
import javax.imageio.ImageIO;
import javax.swing.*;

public class Escenario extends JComponent implements Constantes {
    
    public Celda[][] celdas;
    public Jugador jugador;
    public Adversario[] adversarios;
    public int contadorAdversarios;
    public Lienzo lienzo;
    public int cervezasRestantes;
    public int nuevaSed;
    public Reloj reloj;
    public Timer relojTiempoJuego;

    public ArrayList<Estado> destinos;
    public Estado destinoFinal;

    public Image duffcita;
    public Image relojito;
    public Image nubecita;

    public int CANTIDAD_REAL_DUFFS = NUMERO_CERVEZAS;
    public int CANTIDAD_REAL_ADVERSARIOS = NUMERO_ADVERSARIOS;

    public Escenario(Lienzo lienzo) {
        this.lienzo=lienzo;
        destinos=new ArrayList<>();
        destinoFinal=null;
        analizarModoInicio();

        celdas = new Celda[NUMERO_CELDAS_ANCHO][NUMERO_CELDAS_LARGO];
        //inicializar el array de celdas
        for(int i=0; i < NUMERO_CELDAS_ANCHO; i++) {
           for (int j = 0; j < NUMERO_CELDAS_LARGO; j++) {
               celdas[i][j]=new Celda(i+(i*PIXEL_CELDA), MARGEN_LARGO_BARRA +j+(j*PIXEL_CELDA));
           }
        }
        adversarios = new Adversario[CANTIDAD_REAL_ADVERSARIOS];
        contadorAdversarios = 0;

        //Construccion del escenario y sus entidades
        construirEscenario();
        cervezasRestantes = CANTIDAD_REAL_DUFFS;

        //Asignar listas de destinos y el destino final a la inteligencia del jugador
        jugador.inteligencia.destinoFinal = destinoFinal;
        jugador.inteligencia.destinos = destinos;

        //Asignar listas de destinos y el destino final a la inteligencia de los adversarios
        for(Adversario adversario: adversarios) {
            adversario.inteligencia.destinos = destinos;
        }

        //iconos pequeños para la barra
        try {
            duffcita = ImageIO.read(new File("images/recompensa.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        duffcita = duffcita.getScaledInstance(10, 20, Image.SCALE_DEFAULT);
        try {
            relojito = ImageIO.read(new File("images/reloj.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        relojito = relojito.getScaledInstance(18, 20, Image.SCALE_DEFAULT);
        try {
            nubecita = ImageIO.read(new File("images/nube.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        nubecita = nubecita.getScaledInstance(18, 20, Image.SCALE_DEFAULT);
    }
    public void analizarModoInicio() {
        if (lienzo.hardMode) {
            CANTIDAD_REAL_DUFFS = CANTIDAD_REAL_DUFFS *2;
            if (!lienzo.playerIA) {
                CANTIDAD_REAL_ADVERSARIOS = CANTIDAD_REAL_ADVERSARIOS*3;
            } else {
                CANTIDAD_REAL_ADVERSARIOS+=2;
            }
        }
    }

    public void restablecerDuffs() {
        System.out.println("Reestableciendo cervezas... Sed: numero de cervezas menos las que llevaba --- ");
        for(int i = 0; i< CANTIDAD_REAL_DUFFS; i++) {
            establecerRecompensas();
        }
        jugador.inteligencia.destinos = destinos; //nuevos destinos

        nuevaSed = CANTIDAD_REAL_DUFFS /2 + jugador.getSedDeHomero();
        if(nuevaSed > CANTIDAD_REAL_DUFFS) nuevaSed = CANTIDAD_REAL_DUFFS;

        if(lienzo.hardMode) nuevaSed = (2*nuevaSed/3);

        cervezasRestantes = CANTIDAD_REAL_DUFFS;
        jugador.setSedDeHomero(nuevaSed);
        reloj.setTiempoExtra();
    }
    
    @Override
    public void paintComponent(Graphics g) {
        g.drawImage(lienzo.fondo, 0, MARGEN_LARGO_BARRA, null);

        for(int i=0; i < NUMERO_CELDAS_ANCHO ; i++) 
            for ( int j=0 ; j < NUMERO_CELDAS_LARGO; j++) 
              celdas[i][j].paintComponent(g);
        //Para pintar un rectangulo que borre el marcador anterior
        g.setColor(Color.yellow);
        g.fillRect(0, 0, ANCHURA_ESCENARIO, MARGEN_LARGO_BARRA);
        //Para grabar el nuevo marcador
        g.setColor(Color.BLUE);
        g.setFont(new Font("Verdana", Font.BOLD + Font.ITALIC, 15));

        construccionBarraJuego(g);
    }

    //para evitar que pestañee al mover el jugador (o cambiar algo gráfico en general)
    @Override
    public void update(Graphics g) {
        //era paint(g); pero lo cambie... igual rara volá
        paintComponent(g);
    }

    private void construccionBarraJuego(Graphics g) {
        //iconos pequeños de la barra
        g.drawImage(duffcita, 70, 2, null);
        g.drawImage(relojito, 4*ANCHURA_ESCENARIO/31, 2, null);
        g.drawImage(nubecita, (2*ANCHURA_ESCENARIO/8) - 25, 2, null);

        if(jugador != null && reloj != null) {     //para que al iniciar el programa no ocurran errores de nullpointer
            //Marcador sed de Homero
            g.drawString("Sed de    x " + jugador.getSedDeHomero(), 8, 3* MARGEN_LARGO_BARRA /4);

            if(jugador.getSedDeHomero() == 0) {
                g.drawString("{ Mmmmm brrrrrrp, ya no tengo más sed... }", 2*ANCHURA_ESCENARIO/8, 3* MARGEN_LARGO_BARRA /4);
            } else {
                g.drawString("{ Mmmmm... cerveza... }", 2*ANCHURA_ESCENARIO/8, 3* MARGEN_LARGO_BARRA /4);
            }

            //Timer-reloj del juego
            if( reloj.minutos>=10 && reloj.segundos>=10 ) {
                g.drawString(reloj.minutos+":"+reloj.segundos, 1*ANCHURA_ESCENARIO/7, 3* MARGEN_LARGO_BARRA /4);
            } else if( reloj.minutos>9 ) {
                g.drawString(reloj.minutos+":0"+reloj.segundos, 1*ANCHURA_ESCENARIO/7, 3* MARGEN_LARGO_BARRA /4);
            } else if( reloj.segundos>9 ) {
                g.drawString("0"+reloj.minutos+":"+reloj.segundos, 1*ANCHURA_ESCENARIO/7, 3* MARGEN_LARGO_BARRA /4);
            } else {
                g.drawString("0"+reloj.minutos+":0"+reloj.segundos, 1*ANCHURA_ESCENARIO/7, 3* MARGEN_LARGO_BARRA /4);
            }
        }
    }

    private void construirEscenario() {

        //Posicion inicial jugador
        int restriccionJugador = establecerJugador();

        //Destino al cual debe llegar el jugador para terminar la partida
        //restriccionY se usa para limitar las celdas en la fila restriccionY
        //y que no sean obstaculos
        int restriccionCasa = establecerFinal();

        //Posiciones obstaculos
        for (int i=0; i<NUMERO_OBSTACULOS; i++) {
            establecerObstaculo(restriccionCasa);
        }
        //Posiciones obstaculos 2 (bar de moe)
        for (int i=0; i<NUMERO_OBSTACULOS_2; i++) {
            establecerObstaculo2(restriccionCasa);
        }

        //Posiciones adversarios
        for (int i=0; i<CANTIDAD_REAL_ADVERSARIOS; i++) {
            //Pares se mueven vertical, impares horizontal (1 y 1)
            if (i%2 == 0) {
                establecerAdversarios(true, restriccionJugador);
            } else {
                establecerAdversarios(false, restriccionJugador);
            }
        }

        //Posiciones recompensas
        for (int i = 0; i< CANTIDAD_REAL_DUFFS; i++) {
            establecerRecompensas();
        }
    }

    private int establecerJugador() {
        int x,y;
        x = 0; //randomValue(0, NUMERO_CELDAS_ANCHO-1);
        y = 0; //randomValue(0, NUMERO_CELDAS_LARGO-1);
        if(celdas[x][y].isDisponible()) {
            celdas[x][y].setJugador();
            jugador = new Jugador(x, y, this);
        }
        return x;
    }

    /**
     * PENDIENTE: Se debe implementar restricciones para que no queden encerrados los personajes o recompensas.
     * Por ejemplo, evitar posiciones en los obstáculos que sean (x,y) v/s (x+1,y+1) v/s (x+2,y), lo que claramente
     * produce un espacio encerrado.
     */
    private void establecerObstaculo(int restriccionY) {
        int x,y;
        x = randomValue(0, NUMERO_CELDAS_ANCHO-1);
        y = randomValue(0, NUMERO_CELDAS_LARGO-1);
        Point puntoPrueba = new Point(x,y);

        if(celdas[x][y].isDisponible() && y != restriccionY && !puntosCeldaRestriccionObstaculo.contains(puntoPrueba)) {
            //Se define un obstaculo
            celdas[x][y].setObstaculo();
            agregarRestricciones(x,y);
        }
        else{
            establecerObstaculo(restriccionY);
        }
    }
    private void establecerObstaculo2(int restriccionY) {
        int x,y;
        x = randomValue(0, NUMERO_CELDAS_ANCHO-1);
        y = randomValue(0, NUMERO_CELDAS_LARGO-1);
        Point puntoPrueba = new Point(x,y);

        if (celdas[x][y].isDisponible() && y != restriccionY && !puntosCeldaRestriccionObstaculo.contains(puntoPrueba)) {
            //Se define un obstaculo
            celdas[x][y].setObstaculo2();
            agregarRestricciones(x,y);
        }
        else {
            establecerObstaculo2(restriccionY);
        }
    }

    public void agregarRestricciones(int x, int y) {
        puntosCeldaRestriccionObstaculo.add( new Point(x+1,y+1) );
        puntosCeldaRestriccionObstaculo.add( new Point(x-1,y+1) );
        //puntosCeldaRestriccionObstaculo.add( new Point(x+2, y) );
        puntosCeldaRestriccionObstaculo.add( new Point(x+1, y-1) );
        puntosCeldaRestriccionObstaculo.add( new Point(x-1, y-1) );
    }

    private void establecerAdversarios(boolean movimientoVertical, int restriccionX) {
        int x,y;
        x = randomValue(0, NUMERO_CELDAS_ANCHO-1);
        y = randomValue(0, NUMERO_CELDAS_LARGO-1);
        if(celdas[x][y].isDisponible() && x != restriccionX) {
            celdas[x][y].setAdversario();
            //Se definen adversarios en cada celda numerada por contador del 0 al número de adversarios.
            adversarios[contadorAdversarios] = new Adversario(x, y, this, movimientoVertical);
            contadorAdversarios++;
        }
        else{
            establecerAdversarios(movimientoVertical, restriccionX);
        }
    }

    private void establecerRecompensas() {
        int x,y;
        x = randomValue(0, NUMERO_CELDAS_ANCHO-1);
        y = randomValue(0, NUMERO_CELDAS_LARGO-1);
        if(celdas[x][y].isDisponible()) {
            celdas[x][y].setRecompensa();
            destinos.add(new Estado(x, y, 'N', null));
        }
        else{
            establecerRecompensas();
        }
    }

    private int establecerFinal() {
        //Casa de la familia Simpson
        //int x=NUMERO_CELDAS_ANCHO/2, y=NUMERO_CELDAS_LARGO/2;
        int x = randomValue(2, 2*NUMERO_CELDAS_ANCHO/3);
        int y = randomValue(2, 2*NUMERO_CELDAS_LARGO/3);

        if(x != 0 && y != 0) {
            celdas[x][y].setCasa();
            celdas[x+1][y+1].setFinal();
            destinoFinal = new Estado(x+1, y+1, 'N', null);

            celdas[x][y+1].setObstaculo();
            agregarRestricciones(x, y+1);
            celdas[x+1][y].setObstaculo();
            agregarRestricciones(x+1, y);
            celdas[x+2][y].setObstaculo();
            agregarRestricciones(x+2, y);
            celdas[x+2][y+1].setObstaculo();
            agregarRestricciones(x+2,y+1);
        } else {
            establecerFinal();
        }
        return y+2; //dos celdas de distancia a la casa, sin obstáculos
    }

    public void detenerJuego() {
        if(lienzo.playerIA) {
            lienzo.lanzadorJugadorIA.cancel();
            jugador.inteligencia.cancel();
        }

        lienzo.lanzadorAdversariosIA.cancel();
        for (Adversario adversario: adversarios) {
            adversario.inteligencia.cancel();
        }
        reloj.cancel();
        lienzo.ventanaJuego.player.cancion.close();

        lienzo.ventanaJuego.setVisible(false);
        lienzo.ventanaMenu.setVisible(true);
        System.out.println("El juego se ha detenido correctamente --------------------------------------");
    }

    public void informarCambio(Estado estado) {
        jugador.inteligencia.destinos.remove(estado);
        for (Adversario adversario: adversarios) {
            adversario.inteligencia.destinos.remove(estado);
        }
    }

    public void moverAdversario() {
        int val1, val2;
        val1 = randomValue(1, NUMERO_ADVERSARIOS);
        val2 = randomValue(1, NUMERO_ADVERSARIOS);
        switch(val2) {
            case 1:
                for(int i=0; i<3; i++) {
                    adversarios[val1].moverCeldaArriba();
                }
                break;
            case 2:
                for(int i=0; i<3; i++) {
                    adversarios[val1].moverCeldaAbajo();
                }
                break;
            case 3:
                for(int i=0; i<3; i++) {
                    adversarios[val1].moverCeldaIzquierda();
                }
                break;
            case 4:
                for(int i=0; i<3; i++) {
                    adversarios[val1].moverCeldaDerecha();
                }
                break;
        }
    }

    public void restarCerveza() {
        cervezasRestantes--;
    }

    public Pair<Integer,Integer> darCeldaTipo(char tipoC) {
        Pair<Integer,Integer> celda=null;
        for(int i=0; i < NUMERO_CELDAS_ANCHO ; i++)
            for ( int j=0 ; j < NUMERO_CELDAS_LARGO; j++)
                if ( celdas[i][j].tipo == tipoC ) {
                    celda = new Pair(i,j);
                    break;
                }
        return celda;
    }

    public int buscarObjetivo(int x, int y) {
        for(int i=0; i < destinos.size(); i++) {
            if (destinos.get(i).x == x && destinos.get(i).y == y) {
                return i;
            }
        }
        return -1;
    }

    public Pair<Integer,Integer> darCeldaTipoMasCercano(char tipoC) {
        Pair<Integer,Integer> celda = null;
        Point punto = null;
        for(int i=0; i < NUMERO_CELDAS_ANCHO ; i++)
            for ( int j=0 ; j < NUMERO_CELDAS_LARGO; j++)
                if ( celdas[i][j].tipo == tipoC ) {
                    celda = new Pair(i,j);
                    break;
                }
        return celda;
    }
    class ClosestPair {

        double dist(Point p1, Point p2)
        {
            return Math.sqrt( (p1.x - p2.x)*(p1.x - p2.x) + (p1.y - p2.y)*(p1.y - p2.y) );
        }

        double BFClosestPair(Point P[], int n)
        {
            int i, j;
            double min = Double.MAX_VALUE;
            for (i = 0; i < n; i++)
                for (j = i + 1; j < n; j++)
                    if (dist(P[i], P[j]) < min)
                        min = dist(P[i], P[j]);
            puntosdistminima2(min, P, n);
            return min;
        }

        void puntosdistminima2(double min, Point P[], int n)
        {
            int i, j;
            for (i = 0; i < n; i++)
                for (j = i + 1; j < n; j++)
                    if (dist(P[i], P[j]) == min)
                        System.out.println("Este par de puntos posee la distancia minima: (" + P[i].x +","+ P[i].y +") vs ("+ P[j].x +","+ P[j].y+")");
        }
    }
}
