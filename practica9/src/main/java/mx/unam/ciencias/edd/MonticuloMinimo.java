package mx.unam.ciencias.edd;

import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * Clase para montículos mínimos (<i>min heaps</i>).
 */
public class MonticuloMinimo<T extends ComparableIndexable<T>>
    implements Coleccion<T>, MonticuloDijkstra<T> {

    /* Clase interna privada para iteradores. */
    private class Iterador implements Iterator<T> {

        /* Índice del iterador. */
        private int indice;

        /* Nos dice si hay un siguiente elemento. */
        @Override public boolean hasNext() {
            return this.indice < elementos;
            // Aquí va su código.
        }

        /* Regresa el siguiente elemento. */
        @Override public T next() {
            if (this.indice >= elementos) {
                throw new NoSuchElementException();
            }
            return arbol[this.indice++];
            // Aquí va su código.
        }
    }

    /* Clase estática privada para adaptadores. */
    private static class Adaptador<T  extends Comparable<T>>
        implements ComparableIndexable<Adaptador<T>> {

        /* El elemento. */
        private T elemento;
        /* El índice. */
        private int indice;

        /* Crea un nuevo comparable indexable. */
        public Adaptador(T elemento) {
            this.elemento = elemento;
            this.indice = -1;
            // Aquí va su código.
        }

        /* Regresa el índice. */
        @Override public int getIndice() {
            return this.indice;
            // Aquí va su código.
        }

        /* Define el índice. */
        @Override public void setIndice(int indice) {
            this.indice = indice;
            // Aquí va su código.
        }

        /* Compara un adaptador con otro. */
        @Override public int compareTo(Adaptador<T> adaptador) {
            return this.elemento.compareTo(adaptador.elemento);
            // Aquí va su código.
        }
    }

    /* El número de elementos en el arreglo. */
    private int elementos;
    /* Usamos un truco para poder utilizar arreglos genéricos. */
    private T[] arbol;

    /* Truco para crear arreglos genéricos. Es necesario hacerlo así por cómo
       Java implementa sus genéricos; de otra forma obtenemos advertencias del
       compilador. */
    @SuppressWarnings("unchecked") private T[] nuevoArreglo(int n) {
        return (T[])(new ComparableIndexable[n]);
    }

    /**
     * Constructor sin parámetros. Es más eficiente usar {@link
     * #MonticuloMinimo(Coleccion)} o {@link #MonticuloMinimo(Iterable,int)},
     * pero se ofrece este constructor por completez.
     */
    public MonticuloMinimo() {
        this.arbol = nuevoArreglo(100);
        // Aquí va su código.
    }

    /**
     * Constructor para montículo mínimo que recibe una colección. Es más barato
     * construir un montículo con todos sus elementos de antemano (tiempo
     * <i>O</i>(<i>n</i>)), que el insertándolos uno por uno (tiempo
     * <i>O</i>(<i>n</i> log <i>n</i>)).
     * @param coleccion la colección a partir de la cuál queremos construir el
     *                  montículo.
     */
    public MonticuloMinimo(Coleccion<T> coleccion) {
        this(coleccion, coleccion.getElementos());
        // Aquí va su código.
    }

    /**
     * Constructor para montículo mínimo que recibe un iterable y el número de
     * elementos en el mismo. Es más barato construir un montículo con todos sus
     * elementos de antemano (tiempo <i>O</i>(<i>n</i>)), que el insertándolos
     * uno por uno (tiempo <i>O</i>(<i>n</i> log <i>n</i>)).
     * @param iterable el iterable a partir de la cuál queremos construir el
     *                 montículo.
     * @param n el número de elementos en el iterable.
     */
    public MonticuloMinimo(Iterable<T> iterable, int n) {
        this.arbol = nuevoArreglo(n);
        this.elementos = n;
        int contador = 0;
        for (T elemento : iterable) {
            this.arbol[contador] = elemento;
            elemento.setIndice(contador);
            contador += 1;
        }
        for (int i = (n - 1) / 2; i >= 0; i--) {
            acomodandoHaciaAbajo(this.arbol[i]);
        }
        // Aquí va su código.
    }

    /**
     * Agrega un nuevo elemento en el montículo.
     * @param elemento el elemento a agregar en el montículo.
     */
    @Override public void agrega(T elemento) {
        if (this.elementos == this.arbol.length) {
            T[] nuevo = nuevoArreglo(this.elementos*2);
            for (int i = 0; i < this.elementos; i++) {
                nuevo[i] = this.arbol[i];
                nuevo[i].setIndice(this.arbol[i].getIndice());
            }
            this.arbol = nuevo;
        }
        this.arbol[this.elementos] = elemento;
        this.arbol[this.elementos].setIndice(this.elementos);
        this.elementos += 1;
        acomodandoHaciaArriba(this.arbol[this.elementos - 1]);
        // Aquí va su código.
    }

    /**
     * Elimina el elemento mínimo del montículo.
     * @return el elemento mínimo del montículo.
     * @throws IllegalStateException si el montículo es vacío.
     */
    @Override public T elimina() {
        if (this.elementos == 0) {
            throw new IllegalStateException();
        }
        intercambiaElementos(0, this.elementos - 1);
        T raiz = this.arbol[this.elementos - 1];
        this.arbol[this.elementos - 1].setIndice(-1);
        this.arbol[this.elementos - 1] = null;
        this.elementos -= 1;
        acomodandoHaciaAbajo(this.arbol[0]);
        return raiz;
        // Aquí va su código.
    }

    /**
     * Elimina un elemento del montículo.
     * @param elemento a eliminar del montículo.
     */
    @Override public void elimina(T elemento) {
        if (elemento.getIndice() < 0 || elemento.getIndice() >= this.elementos) {
            return;
        }
        int indice = elemento.getIndice();
        intercambiaElementos(indice, this.elementos - 1);
        this.elementos -= 1;
        this.arbol[this.elementos].setIndice(-1);
        this.arbol[this.elementos] = null;
        reordena(this.arbol[indice]);
        // Aquí va su código.
    }

    /**
     * Nos dice si un elemento está contenido en el montículo.
     * @param elemento el elemento que queremos saber si está contenido.
     * @return <code>true</code> si el elemento está contenido,
     *         <code>false</code> en otro caso.
     */
    @Override public boolean contiene(T elemento) {
        if (elemento.getIndice() < 0 || elemento.getIndice() >= this.elementos) {
            return false;
        }
        return this.arbol[elemento.getIndice()].compareTo(elemento) == 0;
        // Aquí va su código.
    }

    /**
     * Nos dice si el montículo es vacío.
     * @return <code>true</code> si ya no hay elementos en el montículo,
     *         <code>false</code> en otro caso.
     */
    @Override public boolean esVacia() {
        return this.elementos == 0;
        // Aquí va su código.
    }

    /**
     * Limpia el montículo de elementos, dejándolo vacío.
     */
    @Override public void limpia() {
        for (int i = 0; i < this.elementos; i++) {
            this.arbol[i] = null;
        }
        this.elementos = 0;
        // Aquí va su código.
    }

   /**
     * Reordena un elemento en el árbol.
     * @param elemento el elemento que hay que reordenar.
     */
    @Override public void reordena(T elemento) {
        if (elemento == null) {
            return;
        }
        int indice = elemento.getIndice();
        if (!validaIndice(indice)) {
            return;
        }
        int padre = (indice - 1) / 2;
        if (this.arbol[indice].compareTo(this.arbol[padre]) < 0) {
            acomodandoHaciaArriba(this.arbol[indice]);
        } else {
            acomodandoHaciaAbajo(this.arbol[indice]);
        }
        // Aquí va su código.
    }

    private void acomodandoHaciaArriba(T elemento) {
        if (elemento == null) {
            return;
        }
        int indice = elemento.getIndice();
        if (!validaIndice(indice)) {
            return;
        }
        int padre = (indice - 1) / 2;
        if (this.arbol[indice].compareTo(this.arbol[padre]) < 0) {
            intercambiaElementos(indice, padre);
            acomodandoHaciaArriba(this.arbol[padre]);
        }
    }

    private void acomodandoHaciaAbajo(T elemento) {
        if (elemento == null) {
            return;
        }
        int indice = elemento.getIndice();
        int hi = (2 * indice) + 1;
        int hd = (2 * indice) + 2;
        if (!validaIndice(hi) && !validaIndice(hd)) {
            return;
        }
        if (validaIndice(hi) && validaIndice(hd)) {
            if (this.arbol[hi].compareTo(this.arbol[hd]) < 0) {
                if (verificaPropiedad(indice, hi)) {
                    intercambiaElementos(indice, hi);
                    acomodandoHaciaAbajo(this.arbol[hi]);
                }
            } else {
                if (verificaPropiedad(indice, hd)) {
                    intercambiaElementos(indice, hd);
                    acomodandoHaciaAbajo(this.arbol[hd]);
                }
            }
        } else if (validaIndice(hi)) {
            if (verificaPropiedad(indice, hi)) {
                intercambiaElementos(indice, hi);
                acomodandoHaciaAbajo(this.arbol[hi]);
            }
        } else {
            if (verificaPropiedad(indice, hd)) {
                intercambiaElementos(indice, hd);
                acomodandoHaciaAbajo(this.arbol[hd]);
            }
        }
    }

    private boolean verificaPropiedad(int i, int j) {
        return this.arbol[i].compareTo(this.arbol[j]) > 0;
    }

    private boolean validaIndice(int indice) {
        return indice >= 0 && indice < this.elementos;
    }

    private void intercambiaElementos(int i, int j) {
        if (i == j) {
            return;
        }
        T elemento = this.arbol[i];
        int indicei = this.arbol[i].getIndice();
        int indicej = this.arbol[j].getIndice();
        this.arbol[i] = this.arbol[j];
        this.arbol[i].setIndice(indicei);
        this.arbol[j] = elemento;
        this.arbol[j].setIndice(indicej);
    }

    /**
     * Regresa el número de elementos en el montículo mínimo.
     * @return el número de elementos en el montículo mínimo.
     */
    @Override public int getElementos() {
        return this.elementos;
        // Aquí va su código.
    }

    /**
     * Regresa el <i>i</i>-ésimo elemento del árbol, por niveles.
     * @param i el índice del elemento que queremos, en <em>in-order</em>.
     * @return el <i>i</i>-ésimo elemento del árbol, por niveles.
     * @throws NoSuchElementException si i es menor que cero, o mayor o igual
     *         que el número de elementos.
     */
    @Override public T get(int i) {
        if (i < 0 || i >= this.elementos) {
            throw new NoSuchElementException();
        }
        return this.arbol[i];
        // Aquí va su código.
    }

    /**
     * Regresa una representación en cadena del montículo mínimo.
     * @return una representación en cadena del montículo mínimo.
     */
    @Override public String toString() {
        String monticulo = "";
        for (T elemento : this.arbol) {
            monticulo += elemento.toString() + ", ";
        }
        return monticulo;
        // Aquí va su código.
    }

    /**
     * Nos dice si el montículo mínimo es igual al objeto recibido.
     * @param objeto el objeto con el que queremos comparar el montículo mínimo.
     * @return <code>true</code> si el objeto recibido es un montículo mínimo
     *         igual al que llama el método; <code>false</code> en otro caso.
     */
    @Override public boolean equals(Object objeto) {
        if (objeto == null || getClass() != objeto.getClass())
            return false;
        @SuppressWarnings("unchecked") MonticuloMinimo<T> monticulo =
            (MonticuloMinimo<T>)objeto;
        if (this.elementos != monticulo.elementos) {
            return false;
        }
        for (int i = 0; i < this.elementos; i++) {
            if (!(this.arbol[i].equals(monticulo.arbol[i]))) {
                return false;
            }
        }
        return true;
        // Aquí va su código.
    }

    /**
     * Regresa un iterador para iterar el montículo mínimo. El montículo se
     * itera en orden BFS.
     * @return un iterador para iterar el montículo mínimo.
     */
    @Override public Iterator<T> iterator() {
        return new Iterador();
    }

    /**
     * Ordena la colección usando HeapSort.
     * @param <T> tipo del que puede ser el arreglo.
     * @param coleccion la colección a ordenar.
     * @return una lista ordenada con los elementos de la colección.
     */
    public static <T extends Comparable<T>>
    Lista<T> heapSort(Coleccion<T> coleccion) {
        Lista<Adaptador<T>> lista1 = new Lista<Adaptador<T>>();
        for (T elemento : coleccion) {
            lista1.agregaFinal(new Adaptador<T>(elemento));
        }
        Lista<T> lista2 = new Lista<T>();
        MonticuloMinimo<Adaptador<T>> monticulo = new MonticuloMinimo<Adaptador<T>>(lista1);
        while (!(monticulo.esVacia())) {
            T elemento = monticulo.elimina().elemento;
            lista2.agregaFinal(elemento);
        }
        return lista2;
        // Aquí va su código.
    }
}
