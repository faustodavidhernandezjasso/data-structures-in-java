package mx.unam.ciencias.edd;

import java.time.Year;
import java.util.Iterator;

/**
 * <p>Clase para árboles binarios ordenados. Los árboles son genéricos, pero
 * acotados a la interfaz {@link Comparable}.</p>
 *
 * <p>Un árbol instancia de esta clase siempre cumple que:</p>
 * <ul>
 *   <li>Cualquier elemento en el árbol es mayor o igual que todos sus
 *       descendientes por la izquierda.</li>
 *   <li>Cualquier elemento en el árbol es menor o igual que todos sus
 *       descendientes por la derecha.</li>
 * </ul>
 */
public class ArbolBinarioOrdenado<T extends Comparable<T>>
    extends ArbolBinario<T> {

    /* Clase interna privada para iteradores. */
    private class Iterador implements Iterator<T> {

        /* Pila para recorrer los vértices en DFS in-order. */
        private Pila<Vertice> pila;

        /* Inicializa al iterador. */
        public Iterador() {
            this.pila = new Pila<Vertice>();
            Vertice vertice = raiz;
            while (vertice != null) {
                this.pila.mete(vertice);
                vertice = vertice.izquierdo;
            }
            // Aquí va su código.
        }

        /* Nos dice si hay un elemento siguiente. */
        @Override public boolean hasNext() {
            return !(this.pila.esVacia());
            // Aquí va su código.
        }

        /* Regresa el siguiente elemento en orden DFS in-order. */
        @Override public T next() {
            Vertice vertice = pila.saca();
            T elemento = vertice.elemento;
            if (vertice.hayDerecho()) {
                vertice = vertice.derecho;
                while (vertice != null) {
                    pila.mete(vertice);
                    vertice = vertice.izquierdo;
                }
            }
            return elemento;
            // Aquí va su código.
        }
    }

    /**
     * El vértice del último elemento agegado. Este vértice sólo se puede
     * garantizar que existe <em>inmediatamente</em> después de haber agregado
     * un elemento al árbol. Si cualquier operación distinta a agregar sobre el
     * árbol se ejecuta después de haber agregado un elemento, el estado de esta
     * variable es indefinido.
     */
    protected Vertice ultimoAgregado;

    /**
     * Constructor sin parámetros. Para no perder el constructor sin parámetros
     * de {@link ArbolBinario}.
     */
    public ArbolBinarioOrdenado() { super(); }

    /**
     * Construye un árbol binario ordenado a partir de una colección. El árbol
     * binario ordenado tiene los mismos elementos que la colección recibida.
     * @param coleccion la colección a partir de la cual creamos el árbol
     *        binario ordenado.
     */
    public ArbolBinarioOrdenado(Coleccion<T> coleccion) {
        super(coleccion);
    }

    /**
     * Agrega un nuevo elemento al árbol. El árbol conserva su orden in-order.
     * @param elemento el elemento a agregar.
     */
    @Override public void agrega(T elemento) {
        if (elemento == null) {
            throw new IllegalArgumentException();
        }
        Vertice nuevo = nuevoVertice(elemento);
        this.ultimoAgregado = nuevo;
        this.elementos += 1;
        if (this.raiz == null) {
            this.raiz = nuevo;
            return;
        }
        auxiliarAgrega(this.raiz, nuevo);
        // Aquí va su código.
    }

    private void auxiliarAgrega(Vertice actual, Vertice nuevo) {
        if (nuevo.elemento.compareTo(actual.elemento) <= 0) {
            if (actual.izquierdo == null) {
                actual.izquierdo = nuevo;
                nuevo.padre = actual;
            } else {
                auxiliarAgrega(actual.izquierdo, nuevo);
            }
        } else {
            if (actual.derecho == null) {
                actual.derecho = nuevo;
                nuevo.padre = actual;
            } else {
                auxiliarAgrega(actual.derecho, nuevo);
            }
        }
    }

    /**
     * Elimina un elemento. Si el elemento no está en el árbol, no hace nada; si
     * está varias veces, elimina el primero que encuentre (in-order). El árbol
     * conserva su orden in-order.
     * @param elemento el elemento a eliminar.
     */
    @Override public void elimina(T elemento) {
        Vertice eliminar = (Vertice) busca(elemento);
        if (eliminar == null) {
            return;
        }
        this.elementos -= 1;
        if (eliminar.izquierdo != null && eliminar.derecho != null) {
             eliminaVertice(intercambiaEliminable(eliminar));
        } else {
            eliminaVertice(eliminar);
        }
        // Aquí va su código.
    }

    /**
     * Intercambia el elemento de un vértice con dos hijos distintos de
     * <code>null</code> con el elemento de un descendiente que tenga a lo más
     * un hijo.
     * @param vertice un vértice con dos hijos distintos de <code>null</code>.
     * @return el vértice descendiente con el que vértice recibido se
     *         intercambió. El vértice regresado tiene a lo más un hijo distinto
     *         de <code>null</code>.
     */
    protected Vertice intercambiaEliminable(Vertice vertice) {
        Vertice intercambia = maximoEnSubarbol(vertice.izquierdo);
        T elemento = vertice.elemento;
        vertice.elemento = intercambia.elemento;
        intercambia.elemento = elemento;
        return intercambia;
        // Aquí va su código.
    }

    /**
     * Elimina un vértice que a lo más tiene un hijo distinto de
     * <code>null</code> subiendo ese hijo (si existe).
     * @param vertice el vértice a eliminar; debe tener a lo más un hijo
     *                distinto de <code>null</code>.
     */
    protected void eliminaVertice(Vertice vertice) {
        if (vertice.hayIzquierdo() && vertice.hayDerecho()) {
            return;
        }
        Vertice p = vertice.padre;
        Vertice u = null;
        if (vertice.hayIzquierdo()) {
            u = vertice.izquierdo;
        } else {
            u = vertice.derecho;
        }
        if (p != null) {
            if (vertice.padre.izquierdo == vertice) {
                p.izquierdo = u;
            } else {
                p.derecho = u;
            }
        } else {
            raiz = u;
        }
        if (u != null) {
            u.padre = p;
        }
        // Aquí va su código.
    }

    private Vertice maximoEnSubarbol(Vertice vertice) {
        if (vertice.derecho == null) {
            return vertice;
        }
        return maximoEnSubarbol(vertice.derecho);
    }

    /**
     * Busca un elemento en el árbol recorriéndolo in-order. Si lo encuentra,
     * regresa el vértice que lo contiene; si no, regresa <code>null</code>.
     * @param elemento el elemento a buscar.
     * @return un vértice que contiene al elemento buscado si lo
     *         encuentra; <code>null</code> en otro caso.
     */
    @Override public VerticeArbolBinario<T> busca(T elemento) {
        return busca(raiz, elemento);
        // Aquí va su código.
    }

    private VerticeArbolBinario<T> busca(Vertice vertice, T elemento) {
        if (vertice == null) {
            return null;
        }
        if (vertice.elemento.compareTo(elemento) == 0) {
            return vertice;
        } else if (elemento.compareTo(vertice.elemento) < 0) {
            return busca(vertice.izquierdo, elemento);
        } else {
            return busca(vertice.derecho, elemento);
        }
    }

    /**
     * Regresa el vértice que contiene el último elemento agregado al
     * árbol. Este método sólo se puede garantizar que funcione
     * <em>inmediatamente</em> después de haber invocado al método {@link
     * agrega}. Si cualquier operación distinta a agregar sobre el árbol se
     * ejecuta después de haber agregado un elemento, el comportamiento de este
     * método es indefinido.
     * @return el vértice que contiene el último elemento agregado al árbol, si
     *         el método es invocado inmediatamente después de agregar un
     *         elemento al árbol.
     */
    public VerticeArbolBinario<T> getUltimoVerticeAgregado() {
        return this.ultimoAgregado;
        // Aquí va su código.
    }

    /**
     * Gira el árbol a la derecha sobre el vértice recibido. Si el vértice no
     * tiene hijo izquierdo, el método no hace nada.
     * @param vertice el vértice sobre el que vamos a girar.
     */
    public void giraDerecha(VerticeArbolBinario<T> vertice) {
        if (!(vertice.hayIzquierdo())) {
            return;
        }
        Vertice q = (Vertice) vertice;
        Vertice p = q.izquierdo;
        if (q.hayPadre()) {
            if (q.padre.izquierdo == q) {
                q.padre.izquierdo = p;
                p.padre = q.padre;
            } else {
                q.padre.derecho = p;
                p.padre = q.padre;
            }
            if (p.hayDerecho()) {
                q.izquierdo = p.derecho;
                p.derecho.padre = q;
            } else {
                q.izquierdo = null;
            }
            q.padre = p;
            p.derecho = q;
        } else {
            if (p.hayDerecho()) {
                q.izquierdo = p.derecho;
                p.derecho.padre = q;
            } else {
                q.izquierdo = null;
            }
            q.padre = p;
            p.derecho = q;
	    p.padre = null;
            raiz = p;
        }
        // Aquí va su código.
    }

    /**
     * Gira el árbol a la izquierda sobre el vértice recibido. Si el vértice no
     * tiene hijo derecho, el método no hace nada.
     * @param vertice el vértice sobre el que vamos a girar.
     */
    public void giraIzquierda(VerticeArbolBinario<T> vertice) {
        if (!(vertice.hayDerecho())) {
            return;
        }
        Vertice q = (Vertice) vertice;
        Vertice p = q.derecho;
        if (q.hayPadre()) {
            if (q.padre.izquierdo == q) {
                q.padre.izquierdo = p;
                p.padre = q.padre;
            } else {
                q.padre.derecho = p;
                p.padre = q.padre;
            }
            if (p.hayIzquierdo()) {
                q.derecho = p.izquierdo;
                p.izquierdo.padre = q;
            } else {
                q.derecho = null;
            }
            q.padre = p;
            p.izquierdo = q;
        } else {
            if (p.hayIzquierdo()) {
                q.derecho = p.izquierdo;
                p.izquierdo.padre = q;
            } else {
                q.derecho = null;
            }
            q.padre = p;
            p.izquierdo = q;
	    p.padre = null;
            raiz = p;
        }
        // Aquí va su código.
    }

    /**
     * Realiza un recorrido DFS <em>pre-order</em> en el árbol, ejecutando la
     * acción recibida en cada elemento del árbol.
     * @param accion la acción a realizar en cada elemento del árbol.
     */
    public void dfsPreOrder(AccionVerticeArbolBinario<T> accion) {
        dfsPreOrder(this.raiz, accion);
        // Aquí va su código.
    }

    private void dfsPreOrder(Vertice vertice, AccionVerticeArbolBinario<T> accion) {
        if (vertice == null) {
            return;
        }
        accion.actua(vertice);
        dfsPreOrder(vertice.izquierdo, accion);
        dfsPreOrder(vertice.derecho, accion);
    }

    /**
     * Realiza un recorrido DFS <em>in-order</em> en el árbol, ejecutando la
     * acción recibida en cada elemento del árbol.
     * @param accion la acción a realizar en cada elemento del árbol.
     */
    public void dfsInOrder(AccionVerticeArbolBinario<T> accion) {
        dfsInOrder(this.raiz, accion);
        // Aquí va su código.
    }

    private void dfsInOrder(Vertice vertice, AccionVerticeArbolBinario<T> accion) {
        if (vertice == null) {
            return;
        }
        dfsInOrder(vertice.izquierdo, accion);
        accion.actua(vertice);
        dfsInOrder(vertice.derecho, accion);
    }
    /**
     * Realiza un recorrido DFS <em>post-order</em> en el árbol, ejecutando la
     * acción recibida en cada elemento del árbol.
     * @param accion la acción a realizar en cada elemento del árbol.
     */
    public void dfsPostOrder(AccionVerticeArbolBinario<T> accion) {
        dfsPostOrder(this.raiz, accion);
        // Aquí va su código.
    }

    private void dfsPostOrder(Vertice vertice, AccionVerticeArbolBinario<T> accion) {
        if (vertice == null) {
            return;
        }
        dfsPostOrder(vertice.izquierdo, accion);
        dfsPostOrder(vertice.derecho, accion);
        accion.actua(vertice);
    }

    /**
     * Regresa un iterador para iterar el árbol. El árbol se itera en orden.
     * @return un iterador para iterar el árbol.
     */
    @Override public Iterator<T> iterator() {
        return new Iterador();
    }
}
