import javax.swing.*;
import java.util.TimerTask;

public class Reloj extends TimerTask implements Constantes {
    public Escenario escenario;
    public int minutos = 0;
    public int segundos = 45;
    public int extraSegs = 12;

    public Reloj(Escenario escenario) {
        this.escenario = escenario;
    }

    public void setTiempoExtra(){
        if(segundos + extraSegs >= 60) {
            minutos++;
            segundos = segundos + extraSegs - 60;
        } else {
            segundos += extraSegs;
        }
    }
    public void setHardTime() {
        minutos = 0;
        segundos = 50;
        extraSegs = 10;
    }
    @Override
    public void run() {
        if(segundos == 0 && minutos == 0) {
            escenario.detenerJuego();
            JOptionPane.showMessageDialog(null, "PERDEDOR! Se acabo el tiempo!!! -demonios Barney, te odio!-, pensó Homero...");
        }
        else if(segundos == 0) {
            minutos -= 1;
            segundos = 59;
        } else {
            segundos -= 1;
        }
        escenario.lienzo.repaint();
    }

}
