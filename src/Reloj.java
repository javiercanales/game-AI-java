import javax.swing.*;
import java.util.TimerTask;

public class Reloj extends TimerTask implements Constantes{
    public Escenario escenario;
    public int minutos = 2;
    public int segundos = 30;
    public int extraSegs = 20;

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
    @Override
    public void run() {
        if(segundos == 0 && minutos == 0) {
            JOptionPane.showMessageDialog(null, "PERDEDOR! Se acabo el tiempo!!!!!!");
            System.exit(0);
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
