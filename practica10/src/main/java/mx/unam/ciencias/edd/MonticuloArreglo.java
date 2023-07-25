package mx.unam.ciencias.edd;

import java.util.NoSuchElementException;

/**
 * Clase para montículos de Dijkstra con arreglos.
 */
public class MonticuloArreglo<T extends ComparableIndexable<T>>
    implements MonticuloDijkstra<T> {

    /* Número de elementos en el arreglo. */
    private int elementos;
    /* Usamos un truco para poder utilizar arreglos genéricos. */
    private T[] arreglo;

    /* Truco para crear arreglos genéricos. Es necesario hacerlo así por cómo
       Java implementa sus genéricos; de otra forma obtenemos advertencias del
       compilador. */
    @SuppressWarnings("unchecked") private T[] nuevoArreglo(int n) {
        return (T[])(new ComparableIndexable[n]);
    }

    /**
     * Constructor para montículo de Dijkstra con un arreglo a partir de una
     * colección.
     * @param coleccion la colección a partir de la cuál queremos construir el
     *                  montículo.
     */
    public MonticuloArreglo(Coleccion<T> coleccion) {
        this(coleccion, coleccion.getElementos());
        // Aquí va su código.
    }

    /**
     * Construye un nuevo para montículo de Dijkstra con arreglo a partir de un
     * iterable.
     * @param iterable el iterable a partir de la cual construir el montículo.
     * @param n el número de elementos en el iterable.
     */
    public MonticuloArreglo(Iterable<T> iterable, int n) {
        this.arreglo = nuevoArreglo(n);
        int contador = 0;
        for (T elemento : iterable) {
            elemento.setIndice(contador);
            this.arreglo[contador] = elemento;
            contador += 1;
        }
        this.elementos = n;
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
        int minimo = 0;
        if (this.arreglo[minimo] == null) {
            for (int i = 1; i < this.arreglo.length; i++) {
                if (this.arreglo[i] != null) {
                    minimo = i;
                    break;
                }
            }
        }
        for (int i = 0; i < this.arreglo.length; i++) {
            if (this.arreglo[minimo] != null && this.arreglo[i] != null) {
                if (this.arreglo[minimo].compareTo(this.arreglo[i]) > 0) {
                    minimo = i;
                }
            }
        }
        T elemento = this.arreglo[minimo];
        elemento.setIndice(-1);
        this.arreglo[minimo] = null;
        this.elementos -= 1;
        return elemento;
        // Aquí va su código.
    }

    /**
     * Regresa el <i>i</i>-ésimo elemento del arreglo.
     * @param i el índice del elemento que queremos.
     * @return el <i>i</i>-ésimo elemento del arreglo.
     * @throws NoSuchElementException si i es menor que cero, o mayor o igual
     *         que el número de elementos.
     */
    @Override public T get(int i) {
        if (i < 0 || i >= this.elementos) {
            throw new NoSuchElementException();
        }
        return this.arreglo[i];
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
     * Regresa el número de elementos en el montículo.
     * @return el número de elementos en el montículo.
     */
    @Override public int getElementos() {
        return this.elementos;
        // Aquí va su código.
    }
}
