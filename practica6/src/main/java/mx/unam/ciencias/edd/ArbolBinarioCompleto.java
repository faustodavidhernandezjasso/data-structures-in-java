package mx.unam.ciencias.edd;

import java.util.Iterator;

/**
 * <p>Clase para árboles binarios completos.</p>
 *
 * <p>Un árbol binario completo agrega y elimina elementos de tal forma que el
 * árbol siempre es lo más cercano posible a estar lleno.</p>
 */
public class ArbolBinarioCompleto<T> extends ArbolBinario<T> {

    /* Clase interna privada para iteradores. */
    private class Iterador implements Iterator<T> {

        /* Cola para recorrer los vértices en BFS. */
        private Cola<Vertice> cola;

        /* Inicializa al iterador. */
        public Iterador() {
            this.cola = new Cola<Vertice>();
            if (raiz != null) {
                cola.mete(raiz);
            }
            // Aquí va su código.
        }

        /* Nos dice si hay un elemento siguiente. */
        @Override public boolean hasNext() {
            return !(this.cola.esVacia());
            // Aquí va su código.
        }

        /* Regresa el siguiente elemento en orden BFS. */
        @Override public T next() {
            Vertice vertice = cola.saca();
            if (vertice.hayIzquierdo()) {
                cola.mete(vertice.izquierdo);
            }
            if (vertice.hayDerecho()) {
                cola.mete(vertice.derecho);
            }
            return vertice.elemento;
            // Aquí va su código.
        }
    }

    /**
     * Constructor sin parámetros. Para no perder el constructor sin parámetros
     * de {@link ArbolBinario}.
     */
    public ArbolBinarioCompleto() { super(); }

    /**
     * Construye un árbol binario completo a partir de una colección. El árbol
     * binario completo tiene los mismos elementos que la colección recibida.
     * @param coleccion la colección a partir de la cual creamos el árbol
     *        binario completo.
     */
    public ArbolBinarioCompleto(Coleccion<T> coleccion) {
        super(coleccion);
    }

    /**
     * Agrega un elemento al árbol binario completo. El nuevo elemento se coloca
     * a la derecha del último nivel, o a la izquierda de un nuevo nivel.
     * @param elemento el elemento a agregar al árbol.
     * @throws IllegalArgumentException si <code>elemento</code> es
     *         <code>null</code>.
     */
    @Override public void agrega(T elemento) {
        if (elemento == null) {
            throw new IllegalArgumentException();
        }
        Vertice nuevo = nuevoVertice(elemento);
        this.elementos += 1;
        if (this.raiz == null) {
            this.raiz = nuevo;
            return;
        }
        Cola<Vertice> cola = new Cola<Vertice>();
        cola.mete(this.raiz);
        while (!(cola.esVacia())) {
            Vertice auxiliar = cola.saca();
            if (!(auxiliar.hayIzquierdo())) {
                auxiliar.izquierdo = nuevo;
                nuevo.padre = auxiliar;
                break;
            } else if (!(auxiliar.hayDerecho())) {
                auxiliar.derecho = nuevo;
                nuevo.padre = auxiliar;
                break;
            }
            cola.mete(auxiliar.izquierdo);
            cola.mete(auxiliar.derecho);
        }
        // Aquí va su código.
    }

    /**
     * Elimina un elemento del árbol. El elemento a eliminar cambia lugares con
     * el último elemento del árbol al recorrerlo por BFS, y entonces es
     * eliminado.
     * @param elemento el elemento a eliminar.
     */
    @Override public void elimina(T elemento) {
        Vertice buscado = buscaVertice(raiz, elemento);
        if (buscado == null) {
            return;
        }
        this.elementos -= 1;
        if (this.elementos == 0) {
            this.raiz = null;
            return;
        }
        Cola<Vertice> cola = new Cola<Vertice>();
        Vertice eliminar = null;
        cola.mete(this.raiz);
        while (!(cola.esVacia())) {
            Vertice auxiliar = cola.saca();
            if (auxiliar.hayIzquierdo()) {
                cola.mete(auxiliar.izquierdo);
            } 
            if (auxiliar.hayDerecho()) {
                cola.mete(auxiliar.derecho);
            }
            if (cola.esVacia()) {
                T e = auxiliar.elemento;
                auxiliar.elemento = buscado.elemento;
                buscado.elemento = e;
                eliminar = auxiliar;
            }
        }
        if (eliminar.padre.izquierdo == eliminar) {
            eliminar.padre.izquierdo = null;
        } else {
            eliminar.padre.derecho = null;
        }
        // Aquí va su código.
    }

    private Vertice buscaVertice(Vertice vertice, T elemento) {
        if (vertice == null) {
            return null;
        }
        if (vertice.elemento.equals(elemento)) {
            return vertice;
        }
        Vertice buscado = null;
        if (vertice.hayIzquierdo()) {
            buscado = buscaVertice(vertice.izquierdo, elemento);
        }
        if (buscado == null) {
            buscado = vertice.hayDerecho() ? buscaVertice(vertice.derecho, elemento) : null;
        }
        return buscado;
    }

    /**
     * Regresa la altura del árbol. La altura de un árbol binario completo
     * siempre es ⌊log<sub>2</sub><em>n</em>⌋.
     * @return la altura del árbol.
     */
    @Override public int altura() {
        if (this.elementos == 0) {
            return -1;
        } 
        int altura = (int) (Math.log(this.elementos) / Math.log(2));
        return altura;
        // Aquí va su código.
    }

    /**
     * Realiza un recorrido BFS en el árbol, ejecutando la acción recibida en
     * cada elemento del árbol.
     * @param accion la acción a realizar en cada elemento del árbol.
     */
    public void bfs(AccionVerticeArbolBinario<T> accion) {
        if (this.raiz == null) {
            return;
        }
        Cola<Vertice> cola = new Cola<Vertice>();
        cola.mete(this.raiz);
        while (!(cola.esVacia())) {
            Vertice vertice = cola.saca();
            accion.actua(vertice);
            if (vertice.hayIzquierdo()) {
                cola.mete(vertice.izquierdo);
            }
            if (vertice.hayDerecho()) {
                cola.mete(vertice.derecho);
            }
        }
        // Aquí va su código.
    }

    /**
     * Regresa un iterador para iterar el árbol. El árbol se itera en orden BFS.
     * @return un iterador para iterar el árbol.
     */
    @Override public Iterator<T> iterator() {
        return new Iterador();
    }
}
