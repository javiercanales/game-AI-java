import javax.swing.*;
import java.util.ArrayList;
import java.util.TimerTask;

public class BusquedaAnchura extends TimerTask implements Constantes {

    public Escenario escenario;
    public Jugador jugador;
    public ArrayList<Estado> destinos;
    public Estado destinoFinal;
    public boolean parar;
    public ArrayList<Estado> colaEstados;
    public ArrayList<Estado> historial;
    public ArrayList<Character> pasos;
    public int index_pasos;
    public Estado inicial;
    public Estado objetivo;
    public Estado temp;
    public boolean exito;

    public BusquedaAnchura(Escenario escenario, Jugador jugador) {

        this.escenario=escenario;
        colaEstados=new ArrayList<>();
        historial=new ArrayList<>();
        pasos=new ArrayList<>();
        index_pasos=0;
        exito=false;
        this.jugador=jugador;
        destinos=new ArrayList<>();
        parar=false;
        destinoFinal=null;
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
        System.out.println("reseteando");
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

    @Override
    public void run() {
        if( !parar ) {
            escenario.jugador.inteligencia.resetear();
        }
        Estado subinicial, subobjetivo;
        boolean resultado;

        if(escenario.jugador.getSedDeHomero() == 0) {       //Logrado, debe buscar el final
            do { //el estado inicial es donde estoy
                subinicial=new Estado(jugador.x,jugador.y,'N',null);
                //el estado final del juego
                subobjetivo=destinoFinal;

                System.out.println("Busqueda de jugador en (" + subinicial.x + "," + subinicial.y + ")");
                System.out.println("hacia la posicion FINAL (" + subobjetivo.x + "," + subobjetivo.y + ")");
                //busco ruta
                resultado=this.buscar(subinicial,subobjetivo);

                if ( !subinicial.equals(subobjetivo) && !resultado )
                    resetear();
                else if ( subinicial.equals(subobjetivo) ) {
                    escenario.detenerJuego();
                    JOptionPane.showMessageDialog(null, "GANADOR! -Oooac... a dormir, un día duro de trabajo...- pensó Homero");
                }

            } while ( !resultado );
        } else {
            do { //el estado inicial es donde estoy
                subinicial=new Estado(jugador.x,jugador.y,'N',null);
                //el estado final es a donde quiero ir
                subobjetivo=destinos.get(0);

                System.out.println("Busqueda de jugador en (" + subinicial.x + "," + subinicial.y + ")");
                System.out.println("hacia la posicion (" + subobjetivo.x + "," + subobjetivo.y + ")");
                //busco ruta
                resultado=this.buscar(subinicial,subobjetivo);
                if ( subinicial.equals(subobjetivo) )
                    destinos.remove(subobjetivo);
                else {
                    if ( !resultado ) {
                        resetear();
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
                        System.out.println("Definitivamente se acabo a donde ir");
                        escenario.restablecerDuffs();   //para que vuelvan las duffs
                        this.cancel();
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
                case 'D': escenario.jugador.moverCeldaAbajo();     break;
                case 'U': escenario.jugador.moverCeldaArriba();    break;
                case 'R': escenario.jugador.moverCeldaDerecha();   break;
                case 'L': escenario.jugador.moverCeldaIzquierda(); break;
            }
            escenario.lienzo.repaint();
        }

        System.out.println("-----------------------------------");
    }
}