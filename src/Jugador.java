import javax.swing.JOptionPane;

public class Jugador implements Constantes {
    public int x;
    public int y;
    public Escenario escenario;
    public int sedDeHomero;
    public BusquedaAnchura inteligencia;

    public Jugador(int x, int y, Escenario escenario) {
        this.x = x;
        this.y = y;
        this.escenario = escenario;
        this.sedDeHomero = NUMERO_CERVEZAS;
        this.inteligencia = new BusquedaAnchura(escenario, this);
    }

    public int getSedDeHomero(){
        return sedDeHomero;
    }
    public void setSedDeHomero(int sed) {
        sedDeHomero=sed;
    }

    public void moverCeldaArriba() {
        if (y > 0) {
            if(sePuedeAvanzar(x, y-1)) {
                int nextX = x;
                int nextY = y-1;

                if(escenario.celdas[nextX][nextY].tipo == FINAL && sedDeHomero != 0) {
                    JOptionPane.showMessageDialog(null, "Aún tengo sed! Cerveza... mmm cerveza...");
                    return;
                }
                //Definir acciones del juego
                accionesJuego(nextX, nextY);
                
                //Hacer movimiento
                escenario.celdas[x][y].celdaSelec = false;
                escenario.celdas[x][y].setCamino();
                y=y-1;                                    //cambio de posicion
                escenario.celdas[x][y].celdaSelec = true;
                escenario.celdas[x][y].setJugador();
                escenario.celdas[x][y].setIndexSprite(3);
                escenario.celdas[x][y].jugador = escenario.celdas[x][y].sprites[escenario.celdas[x][y].getIndexSprite()];
            }
        }
    }

    public void moverCeldaAbajo() {
        if (y+1 < NUMERO_CELDAS_LARGO) {
            if (sePuedeAvanzar(x,y+1)) {
                int nextX = x;
                int nextY = y+1;

                if(escenario.celdas[nextX][nextY].tipo == FINAL && sedDeHomero != 0) {
                    JOptionPane.showMessageDialog(null, "Aún tengo sed! Cerveza... mmm cerveza...");
                    return;
                }
                //Definir acciones del juego
                accionesJuego(nextX, nextY);
                
                //Hacer movimiento
                escenario.celdas[x][y].celdaSelec = false;
                escenario.celdas[x][y].setCamino();
                y=y+1;
                escenario.celdas[x][y].celdaSelec = true;
                escenario.celdas[x][y].setJugador();
                escenario.celdas[x][y].setIndexSprite(0);
                escenario.celdas[x][y].jugador = escenario.celdas[x][y].sprites[escenario.celdas[x][y].getIndexSprite()];
            }
        }
    }

    public void moverCeldaIzquierda() {
        if (x > 0) {
            if (sePuedeAvanzar(x-1, y)) {
                int nextX = x-1;
                int nextY = y;

                if(escenario.celdas[nextX][nextY].tipo == FINAL && sedDeHomero != 0) {
                    JOptionPane.showMessageDialog(null, "Aún tengo sed! Cerveza... mmm cerveza...");
                    return;
                }
                //Definir acciones del juego
                accionesJuego(nextX, nextY);
                
                //Hacer movimiento
                escenario.celdas[x][y].celdaSelec = false;
                escenario.celdas[x][y].setCamino();
                x=x-1;
                escenario.celdas[x][y].celdaSelec = true;
                escenario.celdas[x][y].setJugador();
                escenario.celdas[x][y].setIndexSprite(1);
                escenario.celdas[x][y].jugador = escenario.celdas[x][y].sprites[escenario.celdas[x][y].getIndexSprite()];
            }
        }
    }

    public void moverCeldaDerecha() {
        if (x+1 < NUMERO_CELDAS_ANCHO) {
            if (sePuedeAvanzar(x+1, y)) {
                int nextX = x+1;
                int nextY = y;

                if(escenario.celdas[nextX][nextY].tipo == FINAL && sedDeHomero != 0) {
                    JOptionPane.showMessageDialog(null, "Aún tengo sed! Cerveza... mmm cerveza...");
                    return;
                }
                //Definir acciones del juego
                accionesJuego(nextX, nextY);
                
                //Hacer movimiento
                escenario.celdas[x][y].celdaSelec = false;
                escenario.celdas[x][y].setCamino();
                x=x+1;
                escenario.celdas[x][y].celdaSelec = true;
                escenario.celdas[x][y].setJugador();
                escenario.celdas[x][y].setIndexSprite(2);
                escenario.celdas[x][y].jugador = escenario.celdas[x][y].sprites[escenario.celdas[x][y].getIndexSprite()];
            }

        }
    }

    public void desplazamientoMouse(int nuevoX, int nuevoY) {
        if (sePuedeAvanzar(nuevoX, nuevoY)) {

            if(escenario.celdas[nuevoX][nuevoY].tipo == FINAL && sedDeHomero != 0) {
                JOptionPane.showMessageDialog(null, "Aún tengo sed! Cerveza... mmm cerveza... bbbrrrr");
                return;
            }
            //Definir recompensa si aplica
            accionesJuego(nuevoX, nuevoY);

            //Hacer movimiento
            escenario.celdas[x][y].celdaSelec = false;
            escenario.celdas[x][y].setCamino();
            x=nuevoX;
            y=nuevoY;
            escenario.celdas[x][y].celdaSelec = true;
            escenario.celdas[x][y].setJugador();

        }
    }
    
    public void accionesJuego(int x, int y) {
        switch (escenario.celdas[x][y].tipo) {
            case RECOMPENSA:
                //Definir recompensa.
                if(sedDeHomero > 0) {
                    sedDeHomero--;
                } else if(sedDeHomero == 0) {
                    JOptionPane.showMessageDialog(null, "OOOOOOAACCCCCCCCC... BRRRRRRR");
                    break;
                }
                escenario.restarCerveza();
                System.out.println("Se tomó una. Le faltan beber " + sedDeHomero + " cervezas.");
                System.out.println("Quedan " + escenario.cervezasRestantes + " cervezas.");
                if(sedDeHomero == 0) {
                    //Final. Se las tomó todas y se debería terminar la partida con triunfo.
                    JOptionPane.showMessageDialog(null, "Se las ha bebido todas, borracho! Hora de volver a casa.");
                    break;
                }
                break;
            case FINAL:
                //Final. Se informa sedDeHomero y se termina la partida.
                JOptionPane.showMessageDialog(null, "Oooac... a dormir, un día duro de trabajo...");
                System.exit(0);
                break;
            /*
            case ADVERSARIO:
                //Derrota. Se termina la partida.
                JOptionPane.showMessageDialog(null, "Oh, perdiste :(");
                System.exit(0);

            */
            default:
                break;
        }
    }

    public boolean sePuedeAvanzar(int x, int y) {
        if(escenario.celdas[x][y].tipo != OBSTACULO && escenario.celdas[x][y].tipo != ADVERSARIO
            && escenario.celdas[x][y].tipo != CASA && escenario.celdas[x][y].tipo != OBSTACULO2) {
            return true;
        }
        return false;
    }
}
