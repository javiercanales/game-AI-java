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
        inicial=new Estado(x1,y1,'N',null);
        objetivo=new Estado(x2,y2,'P',null);
        colaEstados.add(inicial);
        historial.add(inicial);

        if ( inicial.equals(objetivo)) exito=true;

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
            if ( escenario.celdas[e.x][e.y-1].tipo != 'O' ) {
                Estado arriba=new Estado(e.x,e.y-1,'U',e);
                if ( !historial.contains(arriba)) {
                    colaEstados.add(arriba);

                    if ( arriba.equals(objetivo)) {
                        objetivo=arriba;
                        exito=true;
                    }

                }
            }
        }
    }

    private void moverAbajo(Estado e) {

        if ( e.y+1 < NUMERO_CELDAS_LARGO ) {
            if ( escenario.celdas[e.x][e.y+1].tipo != 'O' ) {
                Estado abajo=new Estado(e.x,e.y+1,'D',e);
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
            if ( escenario.celdas[e.x-1][e.y].tipo != 'O' ) {
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
            if ( escenario.celdas[e.x+1][e.y].tipo != 'O' ) {
                Estado derecha=new Estado(e.x+1,e.y,'R',e);
                if ( !historial.contains(derecha)){
                    colaEstados.add(derecha);

                    if ( derecha.equals(objetivo)) {
                        objetivo=derecha;
                        exito=true;
                    }
                }
            }
        }
    }

    public void calcularRuta() {
        Estado predecesor=objetivo;
        do{
            pasos.add(predecesor.oper);
            predecesor=predecesor.predecesor;
        }while ( predecesor != null);
        index_pasos=pasos.size()-1;

    }

    public char darMovimiento() {

        return pasos.get(index_pasos-1);

    }
    @Override
    public void run() {
        escenario.jugador.inteligencia.resetear();

        int xJ=escenario.darCeldaTipo('J').getKey();
        int yJ=escenario.darCeldaTipo('J').getValue();
        int xR=escenario.darCeldaTipo('F').getKey();
        int yR=escenario.darCeldaTipo('F').getValue();

        System.out.println("Busqueda  de"+xJ+","+yJ);
        System.out.println("a"+xR+","+yR);

        escenario.jugador.inteligencia.buscar(xJ,yJ,xR,yR);
        escenario.jugador.inteligencia.calcularRuta();

        System.out.println("Paso a dar"+darMovimiento());


        switch(darMovimiento()) {
            case 'D': escenario.jugador.moverCeldaAbajo();break;
            case 'U': escenario.jugador.moverCeldaArriba(); break;
            case 'R': escenario.jugador.moverCeldaDerecha();break;
            case 'L': escenario.jugador.moverCeldaIzquierda();break;
        }
        escenario.lienzo.repaint();
    }
}