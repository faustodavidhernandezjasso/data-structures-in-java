package mx.unam.ciencias.edd;

/**
 * Clase para colas genéricas.
 */
public class Cola<T> extends MeteSaca<T> {

    /**
     * Regresa una representación en cadena de la cola.
     * @return una representación en cadena de la cola.
     */
    @Override public String toString() {
        String cola = "";
        for (Nodo nodo = this.cabeza; nodo != null; nodo = nodo.siguiente) {
            cola += nodo.elemento.toString() + ",";
        }
        return cola;
        // Aquí va su código.
    }

    /**
     * Agrega un elemento al final de la cola.
     * @param elemento el elemento a agregar.
     * @throws IllegalArgumentException si <code>elemento</code> es
     *         <code>null</code>.
     */
    @Override public void mete(T elemento) {
        if (elemento == null) {
            throw new IllegalArgumentException();
        }
        Nodo nuevo = new Nodo(elemento);
        if (this.rabo == null) {
            this.cabeza = this.rabo = nuevo;
        } else {
            this.rabo.siguiente = nuevo;
            this.rabo = nuevo;
        }
        // Aquí va su código.
    }
}
