package mx.unam.ciencias.edd;

/**
 * <p>Clase para árboles AVL.</p>
 *
 * <p>Un árbol AVL cumple que para cada uno de sus vértices, la diferencia entre
 * la áltura de sus subárboles izquierdo y derecho está entre -1 y 1.</p>
 */
public class ArbolAVL<T extends Comparable<T>>
    extends ArbolBinarioOrdenado<T> {

    /**
     * Clase interna protegida para vértices.
     */
    protected class VerticeAVL extends Vertice {

        /** La altura del vértice. */
        public int altura;

        /**
         * Constructor único que recibe un elemento.
         * @param elemento el elemento del vértice.
         */
        public VerticeAVL(T elemento) {
            super(elemento);
            this.altura = 0;
            // Aquí va su código.
        }

        /**
         * Regresa la altura del vértice.
         * @return la altura del vértice.
         */
        @Override public int altura() {
            return this.altura;
            // Aquí va su código.
        }

        /**
         * Regresa una representación en cadena del vértice AVL.
         * @return una representación en cadena del vértice AVL.
         */
        @Override public String toString() {
            int balance = calculaBalance(this);
            return elemento.toString() + " " + this.altura + "/" + balance; 
            // Aquí va su código.
        }

        /**
         * Compara el vértice con otro objeto. La comparación es
         * <em>recursiva</em>.
         * @param objeto el objeto con el cual se comparará el vértice.
         * @return <code>true</code> si el objeto es instancia de la clase
         *         {@link VerticeAVL}, su elemento es igual al elemento de éste
         *         vértice, los descendientes de ambos son recursivamente
         *         iguales, y las alturas son iguales; <code>false</code> en
         *         otro caso.
         */
        @Override public boolean equals(Object objeto) {
            if (objeto == null || getClass() != objeto.getClass())
                return false;
            @SuppressWarnings("unchecked") VerticeAVL vertice = (VerticeAVL)objeto;
            return (this.altura == vertice.altura && super.equals(objeto));
            // Aquí va su código.
        }
    }

    /**
     * Constructor sin parámetros. Para no perder el constructor sin parámetros
     * de {@link ArbolBinarioOrdenado}.
     */
    public ArbolAVL() { super(); }

    /**
     * Construye un árbol AVL a partir de una colección. El árbol AVL tiene los
     * mismos elementos que la colección recibida.
     * @param coleccion la colección a partir de la cual creamos el árbol AVL.
     */
    public ArbolAVL(Coleccion<T> coleccion) {
        super(coleccion);
    }

    /**
     * Construye un nuevo vértice, usando una instancia de {@link VerticeAVL}.
     * @param elemento el elemento dentro del vértice.
     * @return un nuevo vértice con el elemento recibido dentro del mismo.
     */
    @Override protected Vertice nuevoVertice(T elemento) {
        return new VerticeAVL(elemento);
        // Aquí va su código.
    }

    /**
     * Agrega un nuevo elemento al árbol. El método invoca al método {@link
     * ArbolBinarioOrdenado#agrega}, y después balancea el árbol girándolo como
     * sea necesario.
     * @param elemento el elemento a agregar.
     */
    @Override public void agrega(T elemento) {
        super.agrega(elemento);
        VerticeAVL agregado = (VerticeAVL) ultimoAgregado;
        VerticeAVL padre = (VerticeAVL) agregado.padre;
        balanceaArbolAVL(padre);
        // Aquí va su código.
    }

    /**
     * Elimina un elemento del árbol. El método elimina el vértice que contiene
     * el elemento, y gira el árbol como sea necesario para rebalancearlo.
     * @param elemento el elemento a eliminar del árbol.
     */
    @Override public void elimina(T elemento) {
        VerticeAVL eliminado = (VerticeAVL) busca(elemento);
        if (eliminado == null) {
            return;
        }
        elementos -= 1;
        if (eliminado.izquierdo != null && eliminado.derecho != null) {
            eliminado = (VerticeAVL) intercambiaEliminable(eliminado);   
        }
        eliminaVertice(eliminado);
        balanceaArbolAVL((VerticeAVL) eliminado.padre);
        // Aquí va su código.
    }

    private void balanceaArbolAVL(VerticeAVL vertice) {
        if (vertice == null) {
            return;
        }
        calculaAltura(vertice);
        if (calculaBalance(vertice) == -2) {
            if ((calculaBalance((VerticeAVL) vertice.derecho)) == 1) {
                VerticeAVL q = (VerticeAVL) vertice.derecho;
                super.giraDerecha(q);
                calculaAltura(q);
                calculaAltura((VerticeAVL) q.padre);
            }
            super.giraIzquierda(vertice);
            calculaAltura(vertice);
        }
        else if (calculaBalance(vertice) == 2) {
            if (calculaBalance((VerticeAVL) vertice.izquierdo) == -1) {
                VerticeAVL p = (VerticeAVL) vertice.izquierdo;
                super.giraIzquierda(p);
                calculaAltura(p);
                calculaAltura((VerticeAVL) p.padre);
            }
            super.giraDerecha(vertice);
            calculaAltura(vertice);
        }
        balanceaArbolAVL((VerticeAVL) vertice.padre);
    }

    private int calculaAltura(VerticeAVL vertice) {
        if (vertice == null) {
            return -1;
        }
        vertice.altura = 1 + Math.max(calculaAltura((VerticeAVL) vertice.izquierdo), calculaAltura((VerticeAVL) vertice.derecho));
        return vertice.altura;
    }

    private int calculaBalance(Vertice vertice) {
        return calculaAltura((VerticeAVL) vertice.izquierdo) - calculaAltura((VerticeAVL) vertice.derecho);
    }

    /**
     * Lanza la excepción {@link UnsupportedOperationException}: los árboles AVL
     * no pueden ser girados a la derecha por los usuarios de la clase, porque
     * se desbalancean.
     * @param vertice el vértice sobre el que se quiere girar.
     * @throws UnsupportedOperationException siempre.
     */
    @Override public void giraDerecha(VerticeArbolBinario<T> vertice) {
        throw new UnsupportedOperationException("Los árboles AVL no  pueden " +
                                                "girar a la izquierda por el " +
                                                "usuario.");
    }

    /**
     * Lanza la excepción {@link UnsupportedOperationException}: los árboles AVL
     * no pueden ser girados a la izquierda por los usuarios de la clase, porque
     * se desbalancean.
     * @param vertice el vértice sobre el que se quiere girar.
     * @throws UnsupportedOperationException siempre.
     */
    @Override public void giraIzquierda(VerticeArbolBinario<T> vertice) {
        throw new UnsupportedOperationException("Los árboles AVL no  pueden " +
                                                "girar a la derecha por el " +
                                                "usuario.");
    }
}
