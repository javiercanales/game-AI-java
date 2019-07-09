import javax.swing.JOptionPane;

public class Cervezas implements Constantes {
    public int x;
    public int y;
    public Escenario escenario;

    public Cervezas(int x, int y, Escenario escenario) {
        this.x = x;
        this.y = y;
        this.escenario = escenario;
    }


    public void moverCeldaArriba() {
        if (y > 0) {
            if(sePuedeAvanzar(x, y-1)) {
                int nextX = x;
                int nextY = y-1;

                //Definir acciones del juego
                accionesJuego(nextX, nextY);

                //Hacer movimiento
                escenario.celdas[x][y].celdaSelec = false;
                escenario.celdas[x][y].setCamino();
                y=y-1;                                    //cambio de posicion
                escenario.celdas[x][y].celdaSelec = true;
                escenario.celdas[x][y].setAdversario();
            }
        }
    }

    public void moverCeldaAbajo() {
        if (y+1 < NUMERO_CELDAS_LARGO) {
            if (sePuedeAvanzar(x,y+1)) {
                int nextX = x;
                int nextY = y+1;

                //Definir acciones del juego
                accionesJuego(nextX, nextY);

                //Hacer movimiento
                escenario.celdas[x][y].celdaSelec = false;
                escenario.celdas[x][y].setCamino();
                y=y+1;
                escenario.celdas[x][y].celdaSelec = true;
                escenario.celdas[x][y].setAdversario();
            }
        }
    }

    public void moverCeldaIzquierda() {
        if (x > 0) {
            if (sePuedeAvanzar(x-1, y)) {
                int nextX = x-1;
                int nextY = y;

                //Definir acciones del juego
                accionesJuego(nextX, nextY);

                //Hacer movimiento
                escenario.celdas[x][y].celdaSelec = false;
                escenario.celdas[x][y].setCamino();
                x=x-1;
                escenario.celdas[x][y].celdaSelec = true;
                escenario.celdas[x][y].setAdversario();
            }
        }
    }

    public void moverCeldaDerecha() {
        if (x+1 < NUMERO_CELDAS_ANCHO) {
            if (sePuedeAvanzar(x+1, y)) {
                int nextX = x+1;
                int nextY = y;

                //Definir acciones del juego
                accionesJuego(nextX, nextY);

                //Hacer movimiento
                escenario.celdas[x][y].celdaSelec = false;
                escenario.celdas[x][y].setCamino();
                x=x+1;
                escenario.celdas[x][y].celdaSelec = true;
                escenario.celdas[x][y].setAdversario();
            }

        }
    }

    public void accionesJuego(int x, int y) {
        switch (escenario.celdas[x][y].tipo) {
            case RECOMPENSA:
                //Barney bebió una cerveza!
                System.out.println("Barney se tomó una!");
                break;
            case FINAL:
                //Barney entró en la casa de Homero?!
                System.out.println("Barney en la casa de Homero?!");
                break;
            default:
                break;
        }
    }

    public boolean sePuedeAvanzar(int x, int y) {
        if(escenario.celdas[x][y].tipo != OBSTACULO && escenario.celdas[x][y].tipo != ADVERSARIO
                && escenario.celdas[x][y].tipo != CASA && escenario.celdas[x][y].tipo != JUGADOR) {
            return true;
        }
        return false;
    }
}
