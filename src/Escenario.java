import javafx.util.Pair;

import java.awt.*;
import java.util.Timer;
import javax.swing.*;

public class Escenario extends JComponent implements Constantes {
    
    public Celda[][] celdas;
    public Jugador jugador;
    public Adversario[] adversarios;
    public int contadorAdversarios;
    public Lienzo lienzo;
    public Thread hilo;
    public int cervezasRestantes;
    public int nuevaSed;
    public Timer lanzadorTareas;
    public Reloj reloj;
    public Timer relojTiempoJuego;
    
    public Escenario(Lienzo lienzo) {
        this.lienzo=lienzo;

        celdas = new Celda[NUMERO_CELDAS_ANCHO][NUMERO_CELDAS_LARGO];
        //inicializar el array de celdas
        for(int i=0; i < NUMERO_CELDAS_ANCHO; i++) {
           for (int j = 0; j < NUMERO_CELDAS_LARGO; j++) {
               celdas[i][j]=new Celda(i+(i*PIXEL_CELDA), MARGEN_BORDE_LARGO +j+(j*PIXEL_CELDA));
           }
        }
        adversarios = new Adversario[NUMERO_ADVERSARIOS];
        contadorAdversarios = 0;
        cervezasRestantes = NUMERO_CERVEZAS;

        //Construccion del escenario y sus entidades
        construirEscenario();

        hilo = new Thread(() -> {
            while(jugador.getSedDeHomero() != 0) {
                synchronized (this) {
                    if(cervezasRestantes == 0) {
                        System.out.println("Reestableciendo cervezas... Sed: numero de cervezas menos las que llevaba --- ");
                        for(int i=0; i< NUMERO_CERVEZAS; i++) {
                            establecerRecompensas();
                        }
                        nuevaSed = NUMERO_CERVEZAS/2 + jugador.getSedDeHomero();
                        if(nuevaSed > NUMERO_CERVEZAS) nuevaSed = NUMERO_CERVEZAS;
                        cervezasRestantes = NUMERO_CERVEZAS;
                        jugador.setSedDeHomero(nuevaSed);
                        reloj.setTiempoExtra();
                    }
                }
            }
        });

        //Vida a los adversarios (sin inteligencia)
        lanzadorTareas = new Timer();
        int period = 1000;
        for(int i=0; i<NUMERO_ADVERSARIOS; i++) {
            lanzadorTareas.scheduleAtFixedRate(adversarios[i], 0, period + 100*i);
        }

        //Reloj de tiempo restante
        reloj = new Reloj(this);
        relojTiempoJuego = new Timer();
        relojTiempoJuego.scheduleAtFixedRate(reloj, 0, 1000);

        hilo.start();
    }
    
    @Override
    public void paintComponent(Graphics g) {
        for(int i=0; i < NUMERO_CELDAS_ANCHO ; i++) 
            for ( int j=0 ; j < NUMERO_CELDAS_LARGO; j++) 
              celdas[i][j].paintComponent(g);

        //Para pintar un rectangulo que borre el marcador anterior
        g.setColor(Color.yellow);
        g.fillRect(0, 0, ANCHURA_ESCENARIO, MARGEN_BORDE_LARGO);
        //Para grabar el nuevo marcador
        g.setColor(Color.BLUE);
        g.setFont(new Font("Verdana", Font.BOLD + Font.ITALIC, 15));

        if(jugador != null && reloj != null) {
            //Marcador sed de Homero
            if(jugador.getSedDeHomero() != 0) {
                g.drawString("Sed de Homero: " + jugador.getSedDeHomero() + " cervezas", 13, 3*MARGEN_BORDE_LARGO/4);
            }
            else{
                g.drawString("Mmmmm, tengo sueño... hora de volver a casa", 13, 3*MARGEN_BORDE_LARGO/4);
            }

            //Timer-reloj del juego
            if(reloj.minutos>=10 && reloj.segundos>=10){
                g.drawString(reloj.minutos+":"+reloj.segundos, 7*ANCHURA_ESCENARIO/8, 3*MARGEN_BORDE_LARGO/4);
            }else if(reloj.minutos>9){
                g.drawString(reloj.minutos+":0"+reloj.segundos, 7*ANCHURA_ESCENARIO/8, 3*MARGEN_BORDE_LARGO/4);
            }else if(reloj.segundos>9){
                g.drawString("0"+reloj.minutos+":"+reloj.segundos, 7*ANCHURA_ESCENARIO/8, 3*MARGEN_BORDE_LARGO/4);
            }else{
                g.drawString("Mmm cerveza... SED... 0"+reloj.minutos+":0"+reloj.segundos, 5*ANCHURA_ESCENARIO/6, 3*MARGEN_BORDE_LARGO/4);
            }
        }
    }

    //para evitar que pestañee al mover el jugador (o cambiar algo gráfico en general)
    @Override
    public void update(Graphics g) {
        //era paint(g); pero lo cambie... igual rara volá
        paintComponent(g);
    }

    private void construirEscenario() {
        //Destino al cual debe llegar el jugador para terminar la partida
        //restriccionY se usa para limitar las celdas en la fila restriccionY
        //y que no sean obstaculos
        int restriccionCasa = establecerFinal();

        //Posicion inicial jugador
        int restriccionJugador = establecerJugador();

        //Posiciones obstaculos
        for(int i=0; i<NUMERO_OBSTACULOS; i++) {
            establecerObstaculo(restriccionCasa);
        }
        //Posiciones obstaculos 2 (bar de moe)
        for(int i=0; i<NUMERO_OBSTACULOS_2; i++) {
            establecerObstaculo2(restriccionCasa);
        }

        //Posiciones adversarios
        for(int i=0; i<NUMERO_ADVERSARIOS; i++) {
            //Pares se mueven vertical, impares horizontal (1 y 1)
            if(i%2 == 0) {
                establecerAdversarios(true, restriccionJugador);
            }else{
                establecerAdversarios(false, restriccionJugador);
            }
        }
        //Posiciones recompensas
        for(int i=0; i< NUMERO_CERVEZAS; i++) {
            establecerRecompensas();
        }
    }

    private int establecerJugador() {
        int x,y;
        x = randomValue(0, NUMERO_CELDAS_ANCHO-1);
        y = randomValue(0, NUMERO_CELDAS_LARGO-1);
        if(celdas[x][y].isDisponible()) {
            celdas[x][y].setJugador();
            jugador = new Jugador(x, y, this);
        }
        return x;
    }

    private void establecerObstaculo(int restriccionY) {
        int x,y;
        x = randomValue(0, NUMERO_CELDAS_ANCHO-1);
        y = randomValue(0, NUMERO_CELDAS_LARGO-1);
        if(celdas[x][y].isDisponible() && y != restriccionY) {
            //Se define un obstaculo
            celdas[x][y].setObstaculo();
        }
        else{
            establecerObstaculo(restriccionY);
        }
    }
    private void establecerObstaculo2(int restriccionY) {
        int x,y;
        x = randomValue(0, NUMERO_CELDAS_ANCHO-1);
        y = randomValue(0, NUMERO_CELDAS_LARGO-1);
        if(celdas[x][y].isDisponible() && y != restriccionY) {
            //Se define un obstaculo
            celdas[x][y].setObstaculo2();
        }
        else{
            establecerObstaculo2(restriccionY);
        }
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
        }
        else{
            establecerRecompensas();
        }
    }

    private int establecerFinal() {
        /**
         * FALTA LA CONDICION QUE IMPIDA OBSTACULOS EN LA ENTRADA A LA CASA.
         */
        //Casa de la familia Simpson
        //int x=NUMERO_CELDAS_ANCHO/2, y=NUMERO_CELDAS_LARGO/2;
        int x = randomValue(2, 2*NUMERO_CELDAS_ANCHO/3);
        int y = randomValue(2, 2*NUMERO_CELDAS_LARGO/3);
        celdas[x][y].setCasa();
        celdas[x][y+1].setObstaculo();
        celdas[x+1][y].setObstaculo();
        celdas[x+1][y+1].setFinal();
        celdas[x+2][y].setObstaculo();
        celdas[x+2][y+1].setObstaculo();

        return y+2; //dos celdas de distancia a la casa, sin obstáculos
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
