package mx.unam.ciencias.edd;

import java.util.NoSuchElementException;

/**
 * <p>Clase abstracta para árboles binarios genéricos.</p>
 *
 * <p>La clase proporciona las operaciones básicas para árboles binarios, pero
 * deja la implementación de varias en manos de las subclases concretas.</p>
 */
public abstract class ArbolBinario<T> implements Coleccion<T> {

    /**
     * Clase interna protegida para vértices.
     */
    protected class Vertice implements VerticeArbolBinario<T> {

        /** El elemento del vértice. */
        public T elemento;
        /** El padre del vértice. */
        public Vertice padre;
        /** El izquierdo del vértice. */
        public Vertice izquierdo;
        /** El derecho del vértice. */
        public Vertice derecho;

        /**
         * Constructor único que recibe un elemento.
         * @param elemento el elemento del vértice.
         */
        public Vertice(T elemento) {
            this.elemento = elemento;
            // Aquí va su código.
        }

        /**
         * Nos dice si el vértice tiene un padre.
         * @return <code>true</code> si el vértice tiene padre,
         *         <code>false</code> en otro caso.
         */
        @Override public boolean hayPadre() {
            return this.padre != null;
            // Aquí va su código.
        }

        /**
         * Nos dice si el vértice tiene un izquierdo.
         * @return <code>true</code> si el vértice tiene izquierdo,
         *         <code>false</code> en otro caso.
         */
        @Override public boolean hayIzquierdo() {
            return this.izquierdo != null;
            // Aquí va su código.
        }

        /**
         * Nos dice si el vértice tiene un derecho.
         * @return <code>true</code> si el vértice tiene derecho,
         *         <code>false</code> en otro caso.
         */
        @Override public boolean hayDerecho() {
            return this.derecho != null;
            // Aquí va su código.
        }

        /**
         * Regresa el padre del vértice.
         * @return el padre del vértice.
         * @throws NoSuchElementException si el vértice no tiene padre.
         */
        @Override public VerticeArbolBinario<T> padre() {
            if (this.padre == null) {
                throw new NoSuchElementException();
            }
            return this.padre;
            // Aquí va su código.
        }

        /**
         * Regresa el izquierdo del vértice.
         * @return el izquierdo del vértice.
         * @throws NoSuchElementException si el vértice no tiene izquierdo.
         */
        @Override public VerticeArbolBinario<T> izquierdo() {
            if (this.izquierdo == null) {
                throw new NoSuchElementException();
            }
            return this.izquierdo;
            // Aquí va su código.
        }

        /**
         * Regresa el derecho del vértice.
         * @return el derecho del vértice.
         * @throws NoSuchElementException si el vértice no tiene derecho.
         */
        @Override public VerticeArbolBinario<T> derecho() {
            if (this.derecho == null) {
                throw new NoSuchElementException();
            }
            return this.derecho;
            // Aquí va su código.
        }

        /**
         * Regresa la altura del vértice.
         * @return la altura del vértice.
         */
        @Override public int altura() {
            return altura(this);
            // Aquí va su código.
        }

        private int altura(Vertice vertice) {
            if (vertice == null) {
                return -1;
            } else if (vertice.hayIzquierdo() && vertice.hayDerecho()) {
                return 1 + Math.max(altura(vertice.izquierdo), altura(vertice.derecho));
            } else if (vertice.hayIzquierdo()) {
                return 1 + altura(vertice.izquierdo);
            } else if (vertice.hayDerecho()) {
                return 1 + altura(vertice.derecho);
            } else {
                return 0;
            }
        }

        /**
         * Regresa la profundidad del vértice.
         * @return la profundidad del vértice.
         */
        @Override public int profundidad() {
            return profundidad(this);
            // Aquí va su código.
        }

        private int profundidad(Vertice vertice) {
            if (!(vertice.hayPadre())) {
                return 0;
            }
            return 1 + profundidad(vertice.padre);
        }

        /**
         * Regresa el elemento al que apunta el vértice.
         * @return el elemento al que apunta el vértice.
         */
        @Override public T get() {
            return this.elemento;
            // Aquí va su código.
        }

        /**
         * Compara el vértice con otro objeto. La comparación es
         * <em>recursiva</em>. Las clases que extiendan {@link Vertice} deben
         * sobrecargar el método {@link Vertice#equals}.
         * @param objeto el objeto con el cual se comparará el vértice.
         * @return <code>true</code> si el objeto es instancia de la clase
         *         {@link Vertice}, su elemento es igual al elemento de éste
         *         vértice, y los descendientes de ambos son recursivamente
         *         iguales; <code>false</code> en otro caso.
         */
        @Override public boolean equals(Object objeto) {
            if (objeto == null || getClass() != objeto.getClass())
                return false;
            @SuppressWarnings("unchecked") Vertice vertice = (Vertice)objeto;
            return equals(this, vertice);
            // Aquí va su código.
        }

        private boolean equals(Vertice vertice1, Vertice vertice2) {
            if (vertice1 == null && vertice2 == null) {
                return true;
            }
            if (!(vertice1.elemento.equals(vertice2.elemento))) {
                return false;
            }
            if ((vertice1.izquierdo == null && vertice2.izquierdo != null) || (vertice1.izquierdo != null && vertice2.izquierdo == null)) {
                return false;
            }
            if ((vertice1.derecho == null && vertice2.derecho != null) || (vertice1.derecho != null && vertice2.derecho == null)) {
                return false;
            }
            return equals(vertice1.izquierdo, vertice2.izquierdo) && equals(vertice1.derecho, vertice2.derecho);
        }

        /**
         * Regresa una representación en cadena del vértice.
         * @return una representación en cadena del vértice.
         */
        public String toString() {
            return this.elemento.toString();
            // Aquí va su código.
        }
    }

    /** La raíz del árbol. */
    protected Vertice raiz;
    /** El número de elementos */
    protected int elementos;

    /**
     * Constructor sin parámetros. Tenemos que definirlo para no perderlo.
     */
    public ArbolBinario() {}

    /**
     * Construye un árbol binario a partir de una colección. El árbol binario
     * tendrá los mismos elementos que la colección recibida.
     * @param coleccion la colección a partir de la cual creamos el árbol
     *        binario.
     */
    public ArbolBinario(Coleccion<T> coleccion) {
        for (T elemento : coleccion) {
            this.agrega(elemento);
        }
        // Aquí va su código.
    }

    /**
     * Construye un nuevo vértice, usando una instancia de {@link Vertice}. Para
     * crear vértices se debe utilizar este método en lugar del operador
     * <code>new</code>, para que las clases herederas de ésta puedan
     * sobrecargarlo y permitir que cada estructura de árbol binario utilice
     * distintos tipos de vértices.
     * @param elemento el elemento dentro del vértice.
     * @return un nuevo vértice con el elemento recibido dentro del mismo.
     */
    protected Vertice nuevoVertice(T elemento) {
        return new Vertice(elemento);
        // Aquí va su código.
    }

    /**
     * Regresa la altura del árbol. La altura de un árbol es la altura de su
     * raíz.
     * @return la altura del árbol.
     */
    public int altura() {
        if (this.raiz == null) {
            return -1;
        }
        return this.raiz.altura();
        // Aquí va su código.
    }

    /**
     * Regresa el número de elementos que se han agregado al árbol.
     * @return el número de elementos en el árbol.
     */
    @Override public int getElementos() {
        return this.elementos;
        // Aquí va su código.
    }

    /**
     * Nos dice si un elemento está en el árbol binario.
     * @param elemento el elemento que queremos comprobar si está en el árbol.
     * @return <code>true</code> si el elemento está en el árbol;
     *         <code>false</code> en otro caso.
     */
    @Override public boolean contiene(T elemento) {
        if (elemento == null) {
            return false;
        }
        return contiene(this.raiz, elemento);
        // Aquí va su código.
    }

    private boolean contiene(Vertice vertice, T elemento) {
        if (vertice == null) {
            return false;
        }
        if (vertice.elemento.equals(elemento)) {
            return true;
        } else if (vertice.hayIzquierdo() && vertice.hayDerecho()) {
            return contiene(vertice.izquierdo, elemento) || contiene(vertice.derecho, elemento);
        } else if (vertice.hayIzquierdo()) {
            return contiene(vertice.izquierdo, elemento);
        } else if (vertice.hayDerecho()) {
            return contiene(vertice.derecho, elemento);
        } else {
            return false;
        }
    }

    /**
     * Busca el vértice de un elemento en el árbol. Si no lo encuentra regresa
     * <code>null</code>.
     * @param elemento el elemento para buscar el vértice.
     * @return un vértice que contiene el elemento buscado si lo encuentra;
     *         <code>null</code> en otro caso.
     */
    public VerticeArbolBinario<T> busca(T elemento) {
        if (elemento == null) {
            return null;
        }
        return busca(this.raiz, elemento);
        // Aquí va su código.
    }

    private VerticeArbolBinario<T> busca(Vertice vertice, T elemento) {
        if (vertice == null) {
            return null;
        }
        if (vertice.elemento.equals(elemento)) {
            return (VerticeArbolBinario<T>) vertice;
        }
        if (!(vertice.hayIzquierdo()) && !(vertice.hayDerecho())) {
            return null;
        }
        VerticeArbolBinario<T> buscado = null;
        if (vertice.hayIzquierdo()) {
            buscado = busca(vertice.izquierdo, elemento);
        }
        if (buscado == null) {
            buscado = vertice.hayDerecho() ? busca(vertice.derecho, elemento) : null;
        }
        return buscado;
    }

    /**
     * Regresa el vértice que contiene la raíz del árbol.
     * @return el vértice que contiene la raíz del árbol.
     * @throws NoSuchElementException si el árbol es vacío.
     */
    public VerticeArbolBinario<T> raiz() {
        if (this.raiz == null) {
            throw new NoSuchElementException();
        }
        return this.raiz;
        // Aquí va su código.
    }

    /**
     * Nos dice si el árbol es vacío.
     * @return <code>true</code> si el árbol es vacío, <code>false</code> en
     *         otro caso.
     */
    @Override public boolean esVacia() {
        return this.raiz == null;
        // Aquí va su código.
    }

    /**
     * Limpia el árbol de elementos, dejándolo vacío.
     */
    @Override public void limpia() {
        this.raiz = null;
        this.elementos = 0;
        // Aquí va su código.
    }

    /**
     * Compara el árbol con un objeto.
     * @param objeto el objeto con el que queremos comparar el árbol.
     * @return <code>true</code> si el objeto recibido es un árbol binario y los
     *         árboles son iguales; <code>false</code> en otro caso.
     */
    @Override public boolean equals(Object objeto) {
        if (objeto == null || getClass() != objeto.getClass())
            return false;
        @SuppressWarnings("unchecked")
        ArbolBinario<T> arbol = (ArbolBinario<T>)objeto;
        if (this.raiz == null && arbol.raiz == null) {
            return true;
        }
        return this.raiz.equals(arbol.raiz);
        // Aquí va su código.
    }

    /**
     * Regresa una representación en cadena del árbol.
     * @return una representación en cadena del árbol.
     */
    @Override public String toString() {
        if (this.raiz == null) {
            return "";
        }
        int altura = this.altura() + 1;
        boolean[] arreglo = new boolean[altura];
        for (int i = 0; i < altura; i++) {
            arreglo[i] = false;
        }
        return toString(this.raiz, 0, arreglo);
        // Aquí va su código.
    }

    private String dibujaEspacios(int l, boolean[] arreglo) {
        String s = "";
        for (int i = 0; i <= l - 1; i++) {
            if (arreglo[i]) {
                s += "│  ";
            } else {
                s += "   ";
            }
        }
        return s;
    }

    private String toString(Vertice v, int l, boolean[] arreglo) {
        String s = v.toString() + "\n";
        arreglo[l] = true;
        if (v.izquierdo != null && v.derecho != null) {
            s += dibujaEspacios(l, arreglo);
            s += "├─›";
            s += toString(v.izquierdo, l + 1, arreglo);
            s += dibujaEspacios(l, arreglo);
            s += "└─»";
            arreglo[l] = false;
            s += toString(v.derecho, l + 1, arreglo);
        } else if (v.izquierdo != null) {
            s += dibujaEspacios(l, arreglo);
            s += "└─›";
            arreglo[l] = false;
            s += toString(v.izquierdo, l + 1, arreglo);
        } else if (v.derecho != null) {
            s += dibujaEspacios(l, arreglo);
            s += "└─»";
            arreglo[l] = false;
            s += toString(v.derecho, l + 1, arreglo);
        }
        return s;
    }

    /**
     * Convierte el vértice (visto como instancia de {@link
     * VerticeArbolBinario}) en vértice (visto como instancia de {@link
     * Vertice}). Método auxiliar para hacer esta audición en un único lugar.
     * @param vertice el vértice de árbol binario que queremos como vértice.
     * @return el vértice recibido visto como vértice.
     * @throws ClassCastException si el vértice no es instancia de {@link
     *         Vertice}.
     */
    protected Vertice vertice(VerticeArbolBinario<T> vertice) {
        return (Vertice)vertice;
    }
}
