package mx.unam.ciencias.edd;

/**
 * Clase para árboles rojinegros. Un árbol rojinegro cumple las siguientes
 * propiedades:
 *
 * <ol>
 *  <li>Todos los vértices son NEGROS o ROJOS.</li>
 *  <li>La raíz es NEGRA.</li>
 *  <li>Todas las hojas (<code>null</code>) son NEGRAS (al igual que la raíz).</li>
 *  <li>Un vértice ROJO siempre tiene dos hijos NEGROS.</li>
 *  <li>Todo camino de un vértice a alguna de sus hojas descendientes tiene el
 *      mismo número de vértices NEGROS.</li>
 * </ol>
 *
 * Los árboles rojinegros se autobalancean.
 */
public class ArbolRojinegro<T extends Comparable<T>>
    extends ArbolBinarioOrdenado<T> {

    /**
     * Clase interna protegida para vértices.
     */
    protected class VerticeRojinegro extends Vertice {

        /** El color del vértice. */
        public Color color;

        /**
         * Constructor único que recibe un elemento.
         * @param elemento el elemento del vértice.
         */
        public VerticeRojinegro(T elemento) {
            super(elemento);
            this.color = Color.NINGUNO;
            // Aquí va su código.
        }

        /**
         * Regresa una representación en cadena del vértice rojinegro.
         * @return una representación en cadena del vértice rojinegro.
         */
        public String toString() {
            if (esRojo(this)) {
                return "R{" + this.elemento.toString() + "}";
            } else {
                return "N{" + this.elemento.toString() + "}";
            }
            // Aquí va su código.
        }

        /**
         * Compara el vértice con otro objeto. La comparación es
         * <em>recursiva</em>.
         * @param objeto el objeto con el cual se comparará el vértice.
         * @return <code>true</code> si el objeto es instancia de la clase
         *         {@link VerticeRojinegro}, su elemento es igual al elemento de
         *         éste vértice, los descendientes de ambos son recursivamente
         *         iguales, y los colores son iguales; <code>false</code> en
         *         otro caso.
         */
        @Override public boolean equals(Object objeto) {
            if (objeto == null || getClass() != objeto.getClass())
                return false;
            @SuppressWarnings("unchecked")
            VerticeRojinegro vertice = (VerticeRojinegro)objeto;
            return (this.color == vertice.color && super.equals(objeto));
            // Aquí va su código.
        }
    }

    /**
     * Constructor sin parámetros. Para no perder el constructor sin parámetros
     * de {@link ArbolBinarioOrdenado}.
     */
    public ArbolRojinegro() { super(); }

    /**
     * Construye un árbol rojinegro a partir de una colección. El árbol
     * rojinegro tiene los mismos elementos que la colección recibida.
     * @param coleccion la colección a partir de la cual creamos el árbol
     *        rojinegro.
     */
    public ArbolRojinegro(Coleccion<T> coleccion) {
        super(coleccion);
    }

    /**
     * Construye un nuevo vértice, usando una instancia de {@link
     * VerticeRojinegro}.
     * @param elemento el elemento dentro del vértice.
     * @return un nuevo vértice rojinegro con el elemento recibido dentro del mismo.
     */
    @Override protected Vertice nuevoVertice(T elemento) {
        return new VerticeRojinegro(elemento);
        // Aquí va su código.
    }

    private boolean esRojo(VerticeRojinegro vertice) {
        return (vertice != null && vertice.color == Color.ROJO);
    }

    private boolean esNegro(VerticeRojinegro vertice) {
        return !(esRojo(vertice));
    }

    /**
     * Regresa el color del vértice rojinegro.
     * @param vertice el vértice del que queremos el color.
     * @return el color del vértice rojinegro.
     * @throws ClassCastException si el vértice no es instancia de {@link
     *         VerticeRojinegro}.
     */
    public Color getColor(VerticeArbolBinario<T> vertice) {
        VerticeRojinegro v = (VerticeRojinegro) vertice;
        return v.color;
        // Aquí va su código.
    }

    /**
     * Agrega un nuevo elemento al árbol. El método invoca al método {@link
     * ArbolBinarioOrdenado#agrega}, y después balancea el árbol recoloreando
     * vértices y girando el árbol como sea necesario.
     * @param elemento el elemento a agregar.
     */
    @Override public void agrega(T elemento) {
        super.agrega(elemento);
        VerticeRojinegro vertice = (VerticeRojinegro) ultimoAgregado;
        vertice.color = Color.ROJO;
        rebalanceaAgrega(vertice);
        // Aquí va su código.
    }

    private void rebalanceaAgrega(VerticeRojinegro vertice) {
        // Caso 1
        if (vertice.padre == null) {
            vertice.color = Color.NEGRO;
            return;
        }
        VerticeRojinegro p = (VerticeRojinegro) vertice.padre;
        // Caso 2
        if (esNegro(p)) {
            return;
        }
        VerticeRojinegro a = (VerticeRojinegro) p.padre;
        boolean hayTio = a.izquierdo != null && a.derecho != null ? true : false;
        VerticeRojinegro t = null;
        if (hayTio) {
            t = a.izquierdo == p ? (VerticeRojinegro) a.derecho : (VerticeRojinegro) a.izquierdo;
        }
        // Caso 3
        if (esRojo(t)) {
            t.color = Color.NEGRO;
            p.color = Color.NEGRO;
            a.color = Color.ROJO;
            rebalanceaAgrega(a);
            return;
        }
        // Caso 4
        if ((vertice.padre.izquierdo == vertice && a.derecho == p) || (vertice.padre.derecho == vertice && a.izquierdo == p)) {
            if (a.izquierdo == p) {
                super.giraIzquierda(p);
                p = (VerticeRojinegro) a.izquierdo;
                vertice = (VerticeRojinegro) p.izquierdo;  
            } else {
                super.giraDerecha(p);
                p = (VerticeRojinegro) a.derecho;
                vertice = (VerticeRojinegro) p.derecho;
            }
        }
        // Caso 5
        p.color = Color.NEGRO;
        a.color = Color.ROJO;
        if (vertice.padre.izquierdo == vertice) {
            super.giraDerecha(a);
        } else {
            super.giraIzquierda(a);
        }


    }

    /**
     * Elimina un elemento del árbol. El método elimina el vértice que contiene
     * el elemento, y recolorea y gira el árbol como sea necesario para
     * rebalancearlo.
     * @param elemento el elemento a eliminar del árbol.
     */
    @Override public void elimina(T elemento) {
        VerticeRojinegro vertice = (VerticeRojinegro) busca(elemento);
        if (vertice == null) {
            return;
        }
        elementos -= 1;
        boolean hayFantasma = false;
        VerticeRojinegro h = null;
        if (vertice.izquierdo != null && vertice.derecho != null) {
            vertice = (VerticeRojinegro) intercambiaEliminable(vertice);
        } 
        if (vertice.izquierdo == null && vertice.derecho == null) {
            hayFantasma = true;
            // Creamos el vértice fantasma.
            h = (VerticeRojinegro) nuevoVertice(null);
            h.color = Color.NEGRO;
            h.padre = vertice;
            vertice.izquierdo = h;
        }
        if (!hayFantasma) {
            h = vertice.izquierdo != null ? (VerticeRojinegro) vertice.izquierdo : (VerticeRojinegro) vertice.derecho;
        }
        eliminaVertice(vertice);
        // h es rojo y vértice es negro.
        if (esRojo(h)) {
            h.color = Color.NEGRO;
            return;
        }
        // h es negro y vértice es negro.
        if (esRojo(vertice)) {
            if (hayFantasma) {
                eliminaVertice(h);
            }
            return;
        }
        // Ambos son negros.
        rebalanceaElimina(h);
        if (hayFantasma) {
            eliminaVertice(h);
        }
        // Aquí va su código.
    }

    private void rebalanceaElimina(VerticeRojinegro vertice) {
        // Caso 1
        if (vertice.padre == null) {
            return;
        }
        VerticeRojinegro p = (VerticeRojinegro) vertice.padre;
        boolean verticeEsIzquierdo = vertice.padre.izquierdo == vertice ? true : false;
        VerticeRojinegro h = verticeEsIzquierdo ? (VerticeRojinegro) vertice.padre.derecho : (VerticeRojinegro) vertice.padre.izquierdo;
        // Caso 2
        if (esRojo(h)) {
            p.color = Color.ROJO;
            h.color = Color.NEGRO;
            if (verticeEsIzquierdo) {
                super.giraIzquierda(p);
            } else {
                super.giraDerecha(p);
            }
            h = vertice.padre.izquierdo == vertice ? (VerticeRojinegro) vertice.padre.derecho : (VerticeRojinegro) vertice.padre.izquierdo;
        }
        VerticeRojinegro hi = (VerticeRojinegro) h.izquierdo;
        VerticeRojinegro hd = (VerticeRojinegro) h.derecho;
        // Caso 3
        if (esNegro(p) && esNegro(h) && esNegro(hi) && esNegro(hd)) {
            h.color = Color.ROJO;
            rebalanceaElimina(p);
            return;
        }
        // Caso 4 
        if (esNegro(h) && esNegro(hi) && esNegro(hd) && esRojo(p)) {
            h.color = Color.ROJO;
            p.color = Color.NEGRO;
            return;
        }
        // Caso 5
        if ((vertice.padre.izquierdo == vertice && esRojo(hi) && esNegro(hd)) ||
            (vertice.padre.derecho == vertice && esNegro(hi) && esRojo(hd))) {
                h.color = Color.ROJO;
                if (esNegro(hi)) {
                    hd.color = Color.NEGRO;
                    super.giraIzquierda(h);
                    h = (VerticeRojinegro) p.izquierdo;
                } else {
                    hi.color = Color.NEGRO;
                    super.giraDerecha(h);
                    h = (VerticeRojinegro) p.derecho;
                }
                hd = (VerticeRojinegro) h.derecho;
                hi = (VerticeRojinegro) h.izquierdo;
        }
        // Caso 6
        h.color = p.color;
        p.color = Color.NEGRO;
        if (vertice.padre.izquierdo == vertice) {
            if (hd != null) {
                hd.color = Color.NEGRO;
            }
            super.giraIzquierda(p);
        } else {
            if (hi != null) {
                hi.color = Color.NEGRO;
            }
            super.giraDerecha(p);
        }
    }

    /**
     * Lanza la excepción {@link UnsupportedOperationException}: los árboles
     * rojinegros no pueden ser girados a la izquierda por los usuarios de la
     * clase, porque se desbalancean.
     * @param vertice el vértice sobre el que se quiere girar.
     * @throws UnsupportedOperationException siempre.
     */
    @Override public void giraIzquierda(VerticeArbolBinario<T> vertice) {
        throw new UnsupportedOperationException("Los árboles rojinegros no " +
                                                "pueden girar a la izquierda " +
                                                "por el usuario.");
    }

    /**
     * Lanza la excepción {@link UnsupportedOperationException}: los árboles
     * rojinegros no pueden ser girados a la derecha por los usuarios de la
     * clase, porque se desbalancean.
     * @param vertice el vértice sobre el que se quiere girar.
     * @throws UnsupportedOperationException siempre.
     */
    @Override public void giraDerecha(VerticeArbolBinario<T> vertice) {
        throw new UnsupportedOperationException("Los árboles rojinegros no " +
                                                "pueden girar a la derecha " +
                                                "por el usuario.");
    }
}
