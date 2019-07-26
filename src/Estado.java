/**
 * Estructura de Datos dinámica para controlar el estado del jugador y de las posiciones previas.
 */
public class Estado implements Comparable{
    //posicion x e y de la entidad
    public int x;
    public int y;
    public char oper;
    public Estado predecesor;
    public double prioridad;

    public Estado(int x, int y, char oper,Estado predecesor) {
        this.x=x;
        this.y=y;
        this.oper=oper;
        this.predecesor=predecesor;
    }

    @Override
    public boolean equals(Object x) {
        Estado e=(Estado)x;
        return this.x==e.x && this.y==e.y;
    }
    @Override
    public int hashCode() {
        int hash = 3;
        hash = 89*hash + this.x;
        hash = 89*hash + this.y;
        return hash;
    }
    @Override
    public String toString() {
        return "("+x+","+y+"): Prioridad = " + this.prioridad;
    }
    @Override
    public int compareTo(Object o) {
        Estado e = (Estado) o;
        if ( this.prioridad == e.prioridad ) return 0;
        else {
            if ( this.prioridad > e.prioridad )
                return 1;
            else return -1;
        }
    }
}