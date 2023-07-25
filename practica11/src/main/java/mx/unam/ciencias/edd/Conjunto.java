package mx.unam.ciencias.edd;

import java.util.Iterator;

/**
 * Clase para conjuntos.
 */
public class Conjunto<T> implements Coleccion<T> {

    /* El conjunto de elementos. */
    private Diccionario<T, T> conjunto;

    /**
     * Crea un nuevo conjunto.
     */
    public Conjunto() {
        this.conjunto = new Diccionario<T, T>();
        // Aquí va su código.
    }

    /**
     * Crea un nuevo conjunto para un número determinado de elementos.
     * @param n el número tentativo de elementos.
     */
    public Conjunto(int n) {
        this.conjunto = new Diccionario<T, T>(n);
        // Aquí va su código.
    }

    /**
     * Agrega un elemento al conjunto.
     * @param elemento el elemento que queremos agregar al conjunto.
     * @throws IllegalArgumentException si el elemento es <code>null</code>.
     */
    @Override public void agrega(T elemento) {
        if (elemento == null) {
            throw new IllegalArgumentException();
        }
        this.conjunto.agrega(elemento, elemento);
        // Aquí va su código.
    }

    /**
     * Nos dice si el elemento está en el conjunto.
     * @param elemento el elemento que queremos saber si está en el conjunto.
     * @return <code>true</code> si el elemento está en el conjunto,
     *         <code>false</code> en otro caso.
     */
    @Override public boolean contiene(T elemento) {
        return this.conjunto.contiene(elemento);
        // Aquí va su código.
    }

    /**
     * Elimina el elemento del conjunto, si está.
     * @param elemento el elemento que queremos eliminar del conjunto.
     */
    @Override public void elimina(T elemento) {
        if (this.conjunto.contiene(elemento)) {
            this.conjunto.elimina(elemento);
        }
        // Aquí va su código.
    }

    /**
     * Nos dice si el conjunto es vacío.
     * @return <code>true</code> si el conjunto es vacío, <code>false</code> en
     *         otro caso.
     */
    @Override public boolean esVacia() {
        return this.conjunto.esVacia();
        // Aquí va su código.
    }

    /**
     * Regresa el número de elementos en el conjunto.
     * @return el número de elementos en el conjunto.
     */
    @Override public int getElementos() {
        return this.conjunto.getElementos();
        // Aquí va su código.
    }

    /**
     * Limpia el conjunto de elementos, dejándolo vacío.
     */
    @Override public void limpia() {
        this.conjunto.limpia();
        // Aquí va su código.
    }

    /**
     * Regresa la intersección del conjunto y el conjunto recibido.
     * @param conjunto el conjunto que queremos intersectar con éste.
     * @return la intersección del conjunto y el conjunto recibido.
     */
    public Conjunto<T> interseccion(Conjunto<T> conjunto) {
        Conjunto<T> set = new Conjunto<T>();
        Iterator<T> iterador = this.iterator();
        while (iterador.hasNext()) {
            T elemento = iterador.next();
            if (this.contiene(elemento) && conjunto.contiene(elemento)) {
                set.agrega(elemento);
            }
        }
        return set;                
        // Aquí va su código.
    }

    /**
     * Regresa la unión del conjunto y el conjunto recibido.
     * @param conjunto el conjunto que queremos unir con éste.
     * @return la unión del conjunto y el conjunto recibido.
     */
    public Conjunto<T> union(Conjunto<T> conjunto) {
        Conjunto<T> set = new Conjunto<T>();
        Iterator<T> iterador = this.iterator();
        while (iterador.hasNext()) {
            set.agrega(iterador.next());
        }
        Iterator<T> it = conjunto.iterator();
        while (it.hasNext()) {
            set.agrega(it.next());
        }
        return set;
        // Aquí va su código.
    }

    /**
     * Regresa una representación en cadena del conjunto.
     * @return una representación en cadena del conjunto.
     */
    @Override public String toString() {
        String conjunto = "{ ";
        Iterator<T> iterador = this.iterator();
        int total = this.conjunto.getElementos();
        int contador = 0;
        while (iterador.hasNext()) {
            T elemento = iterador.next();
            conjunto += elemento.toString();
            if (total > 1 && contador < total - 1) {
                conjunto += ", ";
            }
            contador += 1;
        }
        conjunto += " }";
        return conjunto;
        // Aquí va su código.
    }

    /**
     * Nos dice si el conjunto es igual al objeto recibido.
     * @param o el objeto que queremos saber si es igual al conjunto.
     * @return <code>true</code> si el objeto recibido es instancia de Conjunto,
     *         y tiene los mismos elementos.
     */
    @Override public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass())
            return false;
        @SuppressWarnings("unchecked") Conjunto<T> c = (Conjunto<T>)o;
        if (this.conjunto.esVacia() && c.esVacia()) {
            return true;
        }
        return this.conjunto.equals(c.conjunto);
        // Aquí va su código.
    }

    /**
     * Regresa un iterador para iterar el conjunto.
     * @return un iterador para iterar el conjunto.
     */
    @Override public Iterator<T> iterator() {
        return this.conjunto.iterator();
        // Aquí va su código.
    }
}
