import java.util.ArrayList;
import java.util.TimerTask;

public class BusquedaAnchura extends TimerTask implements Constantes{

    public Escenario escenario;
    public ArrayList<Estado> colaEstados;
    public ArrayList<Estado> historial;
    public ArrayList<Character> pasos;
    public int index_pasos;
    public Estado inicial;
    public Estado objetivo;
    public Estado temp;
    public boolean exito;

    public BusquedaAnchura(Escenario escenario) {

        this.escenario=escenario;
        colaEstados=new ArrayList<>();
        historial=new ArrayList<>();
        pasos=new ArrayList<>();
        index_pasos=0;
        exito=false;
    }

    public void buscar(int x1,int y1,int x2,int y2) {
        inicial = new Estado(x1,y1,'N',null);
        objetivo = new Estado(x2,y2,'P',null);
        colaEstados.add(inicial);
        historial.add(inicial);

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

        if ( exito ) System.out.println("Ruta calculada");
        else System.out.println("La ruta no pudo calcularse");

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
            pasos.add(predecesor.oper);
            predecesor = predecesor.predecesor;
        } while ( predecesor != null );

        index_pasos = pasos.size()-1;
    }

    public char darMovimiento() {

        return pasos.get(index_pasos-1);

    }

    /**
     * TIENDE A QUEDARSE ATRAPADO EN SÍ MISMO A VECES, AL CALCULAR LA DIRECCIÓN A RECORRER.
     * ESTO PASA CUANDO NO LOGRA SELECCIONAR UNA DIRECCION DE MOVIMIENTO PORQUE NO SE PUEDE AVANZAR, POR "X" MOTIVO.
     * LA SOLUCION PODRÍA PASAR POR EVITAR TODOS LOS CASOS QUE IMPIDEN NO AVANCE NUNCA, AÚN ASÍ HUBO UN FALLO LUEGO DE BASTANTE TIEMPO EN JUEGO.
     * OTRA SOLUCIÓN PODRÍA SER LA BÚSQUEDA EFICIENTE DE LA CERVEZA MÁS CERCANA AL JUGADOR, AÚN ASÍ DEBE EVALUARSE OTROS CASOS DE ERROR.
     * QUIZÁ, SÓLO QUIZÁ, CONTROLAR EL CASO EN QUE NO ENCONTRÓ MOVIMIENTO AÚN TERMINANDO LA COLA DE ESTADOS. ¿EVITAR EL INDEX SEA 1?, O ALGO PARECIDO.
     */

    @Override
    public void run() {
        escenario.jugador.inteligencia.resetear();
        int xJ, yJ, xR, yR;

        if(escenario.jugador.getSedDeHomero() == 0) {       //Logrado, debe buscar el final
            xJ = escenario.darCeldaTipo('J').getKey();
            yJ = escenario.darCeldaTipo('J').getValue();
            xR = escenario.darCeldaTipo('F').getKey();
            yR = escenario.darCeldaTipo('F').getValue();
        } else {                                            //Aún con sed, debe buscar + cervezas
            xJ = escenario.darCeldaTipo('J').getKey();
            yJ = escenario.darCeldaTipo('J').getValue();
            xR = escenario.darCeldaTipoMasCercano('R').getKey();
            yR = escenario.darCeldaTipoMasCercano('R').getValue();
        }

        System.out.println("Busqueda de jugador en (" + xJ + "," + yJ + ")");
        System.out.println("hacia la posicion (" + xR + "," + yR + ")");

        escenario.jugador.inteligencia.buscar(xJ,yJ,xR,yR);
        escenario.jugador.inteligencia.calcularRuta();

        switch (darMovimiento()) {
            case 'D': System.out.println("Paso a dar: Abajo");     break;
            case 'U': System.out.println("Paso a dar: Arriba");    break;
            case 'R': System.out.println("Paso a dar: Derecha");   break;
            case 'L': System.out.println("Paso a dar: Izquierda"); break;
        }

        switch (darMovimiento()) {
            case 'D': escenario.jugador.moverCeldaAbajo();     break;
            case 'U': escenario.jugador.moverCeldaArriba();    break;
            case 'R': escenario.jugador.moverCeldaDerecha();   break;
            case 'L': escenario.jugador.moverCeldaIzquierda(); break;
        }
        escenario.lienzo.repaint();
        System.out.println("-----------------------------------");
    }
}