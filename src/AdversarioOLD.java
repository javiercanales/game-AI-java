import java.util.TimerTask;

public class AdversarioOLD extends TimerTask implements Constantes {
    public int x;
    public int y;
    public Escenario escenario;
    public Celda adversario;
    public boolean movimientoVertical;

    public AdversarioOLD(int x, int y, Escenario escenario, boolean movimientoVertical) {
        this.x = x;
        this.y = y;
        adversario = escenario.celdas[x][y];
        this.escenario = escenario;
        this.movimientoVertical = movimientoVertical;
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
                escenario.celdas[x][y].setIndexSprite(3);
                escenario.celdas[x][y].adversario = escenario.celdas[x][y].spritesAdversario[escenario.celdas[x][y].indexSprite];
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

                escenario.celdas[x][y].setIndexSprite(0);
                escenario.celdas[x][y].adversario = escenario.celdas[x][y].spritesAdversario[escenario.celdas[x][y].indexSprite];
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

                escenario.celdas[x][y].setIndexSprite(1);
                escenario.celdas[x][y].adversario = escenario.celdas[x][y].spritesAdversario[escenario.celdas[x][y].indexSprite];
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

                escenario.celdas[x][y].setIndexSprite(2);
                escenario.celdas[x][y].adversario = escenario.celdas[x][y].spritesAdversario[escenario.celdas[x][y].indexSprite];
            }
        }
    }

    public void accionesJuego(int x, int y) {
        switch (escenario.celdas[x][y].tipo) {
            case RECOMPENSA:
                //Barney bebió una cerveza!
                System.out.println("Barney se tomó una!");
                escenario.restarCerveza();

                //Eliminamos objetivo consumido
                int index = escenario.buscarObjetivo(x, y);
                escenario.jugador.inteligencia.destinos.remove(index);

                System.out.println("Quedan " + escenario.cervezasRestantes + " cervezas.");
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
        if(escenario.celdas[x][y].tipo != OBSTACULO && escenario.celdas[x][y].tipo != ADVERSARIO && escenario.celdas[x][y].tipo != FINAL
                && escenario.celdas[x][y].tipo != CASA && escenario.celdas[x][y].tipo != JUGADOR && escenario.celdas[x][y].tipo != OBSTACULO2) {
            return true;
        }
        return false;
    }
    public boolean sePuedeAvanzar2(int x, int y) {
        if(escenario.celdas[x][y].tipo != OBSTACULO && escenario.celdas[x][y].tipo != ADVERSARIO
                && escenario.celdas[x][y].tipo != CASA && escenario.celdas[x][y].tipo != FINAL && escenario.celdas[x][y].tipo != OBSTACULO2) {
            return true;
        }
        return false;
    }

    public void moverAdversarioHorizontal() {
        if (x > 0 && sePuedeAvanzar2(x-1, y)) {

            if (escenario.celdas[x-1][y].tipo == JUGADOR){
                escenario.celdas[x][y].tipo = CAMINO;
                /* Deja al jugador donde estaba */
                escenario.celdas[x-1][y].tipo = JUGADOR;

                //Por chocar a Barney le da más sed!
                escenario.jugador.sedDeHomero+=CHOQUE_ADVERSARIO;
                //Verifica que no le de más sed a Homero sin que hayan cervezas
                //Si ocurre, pierde!
                if( escenario.cervezasRestantes == 0 && escenario.jugador.getSedDeHomero()>0 ) {
                    escenario.restablecerDuffs();
                }

                /* El adversario pasa por sobre el jugador */
                x = x - 2;

                /* Posible Overflow: Caso si es que la casilla más allá del jugador es un borde */
                if (x < 0){
                    x = NUMERO_CELDAS_ANCHO - 1;
                    y = randomValue(2, NUMERO_CELDAS_LARGO - 1);
                }
                /* Comprueba que si está ocupada la nueva casilla */
                if (escenario.celdas[x][y].tipo != CAMINO){
                    x = NUMERO_CELDAS_ANCHO - 1;
                    y = randomValue(2, NUMERO_CELDAS_LARGO - 1);
                }
                escenario.celdas[x][y].tipo = ADVERSARIO;
            }
            else{
                escenario.celdas[x][y].tipo = CAMINO;
                x = x - 1;
                accionesJuego(x,y);
                escenario.celdas[x][y].tipo = ADVERSARIO;
                escenario.celdas[x][y].indexSprite = 1;
                escenario.celdas[x][y].adversario = escenario.celdas[x][y].spritesAdversario[escenario.celdas[x][y].indexSprite];
            }
        } else {

            escenario.celdas[x][y].tipo = CAMINO;

            x = NUMERO_CELDAS_ANCHO - 1;
            y = randomValue(2, NUMERO_CELDAS_LARGO - 1);
            accionesJuego(x,y);
            escenario.celdas[x][y].tipo = ADVERSARIO;
        }
    }

    public void moverAdversarioVertical() {
        if (y > 0 && sePuedeAvanzar2(x,y-1)) {

            if (escenario.celdas[x][y-1].tipo == JUGADOR){
                escenario.celdas[x][y].tipo = CAMINO;
                /* Deja al jugador donde estaba */
                escenario.celdas[x][y-1].tipo = JUGADOR;

                //Por chocar a Barney le da más sed!
                escenario.jugador.sedDeHomero+=CHOQUE_ADVERSARIO;
                //Verifica que no le de más sed a Homero sin que hayan cervezas
                //Si ocurre, pierde!
                if(escenario.cervezasRestantes == 0 && escenario.jugador.getSedDeHomero()>0) {
                    escenario.restablecerDuffs();
                }

                /* El adversario pasa por sobre el jugador */
                y = y - 2;

                /* Posible Overflow: Caso si es que la casilla más allá del jugador es un borde */
                if (y < 0){
                    y = NUMERO_CELDAS_LARGO - 1;
                    x = randomValue(2, NUMERO_CELDAS_ANCHO - 1);
                }
                /* Comprueba que si está ocupada la nueva casilla */
                //Ojo aqui, puede producirse un error/////////////////////////////////////////////////////////
                if (escenario.celdas[x][y].tipo != CAMINO){
                    y = NUMERO_CELDAS_LARGO - 1;
                    x = randomValue(2, NUMERO_CELDAS_ANCHO - 1);
                }
                escenario.celdas[x][y].tipo = ADVERSARIO;
            }
            else{
                escenario.celdas[x][y].tipo = CAMINO;
                y = y - 1;
                accionesJuego(x,y);
                escenario.celdas[x][y].tipo = ADVERSARIO;
                escenario.celdas[x][y].indexSprite = 3;
                escenario.celdas[x][y].adversario = escenario.celdas[x][y].spritesAdversario[escenario.celdas[x][y].indexSprite];
            }
        } else {

            escenario.celdas[x][y].tipo = CAMINO;

            y = NUMERO_CELDAS_LARGO - 1;
            x = randomValue(2, NUMERO_CELDAS_ANCHO - 1);
            accionesJuego(x,y);
            escenario.celdas[x][y].tipo = ADVERSARIO;
        }
    }

    @Override
    public void run() {
        if (this.movimientoVertical) moverAdversarioVertical();
        else moverAdversarioHorizontal();
        escenario.lienzo.repaint();
    }
}
