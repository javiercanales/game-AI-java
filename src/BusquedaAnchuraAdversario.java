import javax.swing.*;
import java.util.ArrayList;
import java.util.ConcurrentModificationException;
import java.util.TimerTask;

public class BusquedaAnchuraAdversario extends TimerTask implements Constantes {

    public Escenario escenario;
    public Adversario adversario;
    public ArrayList<Estado> destinos;
    public boolean parar;
    public ArrayList<Estado> colaEstados;
    public ArrayList<Estado> historial;
    public ArrayList<Character> pasos;
    public int index_pasos;
    public Estado inicial;
    public Estado objetivo;
    public Estado temp;
    public boolean exito;
    public int intentosRep;
    public static int MAX_REP = 10;

    public BusquedaAnchuraAdversario(Escenario escenario, Adversario adversario) {

        this.escenario=escenario;
        colaEstados=new ArrayList<>();
        historial=new ArrayList<>();
        pasos=new ArrayList<>();
        index_pasos=0;
        exito=false;
        this.adversario=adversario;
        destinos=new ArrayList<>();
        parar=false;
    }

    public boolean buscar(Estado inicial, Estado objetivo) {
        index_pasos = 0;
        colaEstados.add(inicial);
        historial.add(inicial);
        this.objetivo=objetivo;
        exito=false;

        if ( inicial.equals(objetivo) ) exito=true;

        while ( !colaEstados.isEmpty() && !exito ){

            temp=colaEstados.get(0);

            historial.add(temp);
            colaEstados.remove(0);
            moverArriba(temp);
            moverAbajo(temp);
            moverIzquierda(temp);
            moverDerecha(temp);
        }

        if ( exito ) {
            System.out.println("Ruta calculada");
            this.calcularRuta();
            return true;
        }
        else {
            System.out.println("La ruta no pudo calcularse");
            return false;
        }

    }

    private void resetear() {
        System.out.println("reseteando en adversario");
        colaEstados.clear();
        pasos.clear();
        historial.clear();
        index_pasos=0;
        exito=false;
    }

    private void moverArriba(Estado e) {
        if ( e.y > 0 ) {
            if ( sePuedeAvanzar(e.x, e.y-1) ) {
                Estado arriba = new Estado(e.x,e.y-1,'U', e);
                if ( !historial.contains(arriba)) {
                    colaEstados.add(arriba);

                    if ( arriba.equals(objetivo) ) {
                        objetivo=arriba;
                        exito=true;
                    }

                }
            }
        }
    }

    private void moverAbajo(Estado e) {

        if ( e.y+1 < NUMERO_CELDAS_LARGO ) {
            if ( sePuedeAvanzar(e.x, e.y+1) ) {
                Estado abajo = new Estado(e.x,e.y+1,'D',e);
                if ( !historial.contains(abajo)) {
                    colaEstados.add(abajo);

                    if ( abajo.equals(objetivo)) {

                        objetivo=abajo;
                        exito=true;
                    }
                }
            }
        }
    }

    private void moverIzquierda(Estado e) {
        if ( e.x > 0 ) {
            if ( sePuedeAvanzar(e.x-1, e.y) ) {
                Estado izquierda=new Estado(e.x-1,e.y,'L',e);
                if ( !historial.contains(izquierda)) {

                    colaEstados.add(izquierda);

                    if ( izquierda.equals(objetivo)) {

                        objetivo=izquierda;
                        exito=true;
                    }
                }
            }
        }
    }

    private void moverDerecha(Estado e) {

        if ( e.x < NUMERO_CELDAS_ANCHO-1 ) {
            if ( sePuedeAvanzar(e.x+1, e.y) ) {
                Estado derecha = new Estado(e.x+1, e.y,'R', e);
                if ( !historial.contains(derecha)){
                    colaEstados.add(derecha);

                    if ( derecha.equals(objetivo) ) {
                        objetivo=derecha;
                        exito=true;
                    }
                }
            }
        }
    }

    public boolean sePuedeAvanzar(int x, int y) {
        if(escenario.celdas[x][y].tipo != OBSTACULO && escenario.celdas[x][y].tipo != ADVERSARIO
                && escenario.celdas[x][y].tipo != CASA && escenario.celdas[x][y].tipo != OBSTACULO2) {
            return true;
        }
        return false;
    }

    public void calcularRuta() {
        Estado predecesor = objetivo;
        do {
            pasos.add(0, predecesor.oper);
            predecesor = predecesor.predecesor;
        } while ( predecesor != null );

        index_pasos = pasos.size()-1;
    }

    public char darMovimiento() {
        return pasos.get(index_pasos-1);
    }

    //distancia entre estados/celdas
    public double distancia(Estado inicial, Estado destino) {
        int x1 = inicial.x;
        int y1 = inicial.y;
        int x2 = destino.x;
        int y2 = destino.y;
        double valor;
        double parte1 = Math.pow(Math.abs(x1-x2),2);
        double parte2 = Math.pow(Math.abs(y1-y2),2);
        parte1+=parte2;
        valor=Math.sqrt(parte1);
        return valor;
    }

    public Estado buscarMejorDestino (Estado subinicial) {
        try {
            Estado destinoDefinido = destinos.get(0);
            double temp, distanciaDefinida;
            distanciaDefinida = distancia(subinicial, destinoDefinido);

            for(Estado destino: destinos) {
                temp = distancia(subinicial, destino);
                if (temp < distanciaDefinida) {
                    destinoDefinido = destino;
                }
            }
            return destinoDefinido;
        } catch (ConcurrentModificationException e) {
            this.cancel();
        }
        return null;
    }

    public int avanzarIndice(int index) {
        if ( index < destinos.size()-1 ) {
            index++;
        } else {
            index = 0;
        }
        return index;
    }

    @Override
    public void run() {
        if( !parar ) {
            this.resetear();
        }
        Estado subinicial, subobjetivo;
        boolean resultado;
        int index = 0;
        intentosRep = 0;

        if(escenario.jugador.getSedDeHomero() == 0) {       //Hay que buscar a homero
            do {
                //el estado inicial es donde estoy
                subinicial = new Estado(adversario.x, adversario.y,'N',null);
                //a buscar a Homero antes que llegue a casa!
                subobjetivo = new Estado(escenario.jugador.x, escenario.jugador.y, 'N', null);

                System.out.println("Busqueda de adversario en (" + subinicial.x + "," + subinicial.y + ")");
                System.out.println("hacia la posicion del JUGADOR (" + subobjetivo.x + "," + subobjetivo.y + ")");
                //busco ruta
                resultado = this.buscar(subinicial,subobjetivo);

                if ( !subinicial.equals(subobjetivo) && !resultado && intentosRep < MAX_REP ) {
                    System.out.println("Aqui adversario --------------- intentos repetitivos: " + intentosRep);
                    intentosRep++;
                } else if ( intentosRep >= MAX_REP ) {
                    System.out.println("Aca adversario se pasó --------------------------------------------------");
                    return;
                }
            } while ( !resultado );
        } else {
            do {
                //el estado inicial es donde estoy
                subinicial = new Estado(adversario.x, adversario.y,'N',null);

                //el estado final es a donde quiero ir
                if (destinos.isEmpty()) {
                    return;
                } else {
                    subobjetivo = destinos.get(index);
                }
                //subobjetivo = buscarMejorDestino(subinicial);

                System.out.println("Busqueda de adversario en (" + subinicial.x + "," + subinicial.y + ")");
                System.out.println("hacia la posicion (" + subobjetivo.x + "," + subobjetivo.y + ")");
                //busco ruta
                resultado=this.buscar(subinicial,subobjetivo);
                if ( subinicial.equals(subobjetivo) )
                    escenario.informarCambio(subobjetivo);
                else {
                    if ( !resultado && intentosRep < MAX_REP ) {
                        index = avanzarIndice(index);
                        System.out.println("Aqui adversario --------------- intentos repetitivos: " + intentosRep);
                        intentosRep++;
                    } else if ( intentosRep >= MAX_REP ) {
                        System.out.println("Aca adversario se pasó --------------------------------------------------");
                        return;
                    }
                }
                if ( destinos.isEmpty() ) {
                    System.out.println("Quiza se acabo a donde ir");
                    try {
                        Thread.sleep(2000);   //para dar tiempo a un posible reseteo de recompensas
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    if ( destinos.isEmpty() ) {
                        System.out.println("Definitivamente se acabo a donde ir --- esperar por restablecimiento de Duffs");
                        return;
                    }
                }
            } while (!resultado && !destinos.isEmpty());
        }

        if(pasos.size() > 1) {
            switch (pasos.get(1)) {
                case 'D': System.out.println("Paso a dar: Abajo");     break;
                case 'U': System.out.println("Paso a dar: Arriba");    break;
                case 'R': System.out.println("Paso a dar: Derecha");   break;
                case 'L': System.out.println("Paso a dar: Izquierda"); break;
            }

            switch (pasos.get(1)) {
                case 'D': adversario.moverCeldaAbajo();     break;
                case 'U': adversario.moverCeldaArriba();    break;
                case 'R': adversario.moverCeldaDerecha();   break;
                case 'L': adversario.moverCeldaIzquierda(); break;
            }
            escenario.lienzo.repaint();
        }

        System.out.println("-----------------------------------");
    }
}