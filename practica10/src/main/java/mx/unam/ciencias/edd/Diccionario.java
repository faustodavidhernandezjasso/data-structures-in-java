package mx.unam.ciencias.edd;

import java.lang.reflect.Array;
import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * Clase para diccionarios (<em>hash tables</em>). Un diccionario generaliza el
 * concepto de arreglo, mapeando un conjunto de <em>llaves</em> a una colección
 * de <em>valores</em>.
 */
public class Diccionario<K, V> implements Iterable<V> {

    /* Clase interna privada para entradas. */
    private class Entrada {

        /* La llave. */
        public K llave;
        /* El valor. */
        public V valor;

        /* Construye una nueva entrada. */
        public Entrada(K llave, V valor) {
            this.llave = llave;
            this.valor = valor;
            // Aquí va su código.
        }
    }

    /* Clase interna privada para iteradores. */
    private class Iterador {

        /* En qué lista estamos. */
        private int indice;
        /* Iterador auxiliar. */
        private Iterator<Entrada> iterador;

        /* Construye un nuevo iterador, auxiliándose de las listas del
         * diccionario. */
        public Iterador() {
            boolean hayLista = false;
            for (int i = 0; i < entradas.length; i++) {
                if (entradas[i] != null && entradas[i].getLongitud() > 0) {
                    this.indice = i;
                    this.iterador = entradas[i].iteradorLista();
                    hayLista = true;
                    break;
                }
            }
            if (!(hayLista)) {
                this.iterador = null;
            }
            // Aquí va su código.
        }

        /* Nos dice si hay una siguiente entrada. */
        public boolean hasNext() {
            return this.iterador != null;
            // Aquí va su código.
        }

        /* Regresa la siguiente entrada. */
        public Entrada siguiente() {
            if (this.iterador == null) {
                throw new NoSuchElementException();
            }
            Entrada entrada = this.iterador.next();
            if (!(this.iterador.hasNext())) {
                boolean hayLista = false;
                for (int i = this.indice + 1; i < entradas.length; i++) {
                    if (entradas[i] != null && entradas[i].getLongitud() > 0) {
                        this.iterador = entradas[i].iteradorLista();
                        this.indice = i;
                        hayLista = true;
                        break;
                    }
                }
                if (!hayLista) {
                    this.iterador = null;
                }
            }
            return entrada;
            // Aquí va su código.
        }

        /* Mueve el iterador a la siguiente entrada válida. */
        private void mueveIterador() {
            boolean hayLista = false;
            for (int i = this.indice + 1; i < entradas.length; i++) {
                if (entradas[i] != null && entradas[i].getLongitud() > 0) {
                    this.iterador = entradas[i].iteradorLista();
                    this.indice = i;
                    hayLista = true;
                    break;
                }
            }
            if (!(hayLista)) {
                this.iterador = null;
            }
            // Aquí va su código.
        }
    }

    /* Clase interna privada para iteradores de llaves. */
    private class IteradorLlaves extends Iterador
        implements Iterator<K> {

        /* Regresa el siguiente elemento. */
        @Override public K next() {
            return siguiente().llave;
            // Aquí va su código.
        }
    }

    /* Clase interna privada para iteradores de valores. */
    private class IteradorValores extends Iterador
        implements Iterator<V> {

        /* Regresa el siguiente elemento. */
        @Override public V next() {
            return siguiente().valor;
            // Aquí va su código.
        }
    }

    /** Máxima carga permitida por el diccionario. */
    public static final double MAXIMA_CARGA = 0.72;

    /* Capacidad mínima; decidida arbitrariamente a 2^6. */
    private static final int MINIMA_CAPACIDAD = 64;

    /* Dispersor. */
    private Dispersor<K> dispersor;
    /* Nuestro diccionario. */
    private Lista<Entrada>[] entradas;
    /* Número de valores. */
    private int elementos;

    /* Truco para crear un arreglo genérico. Es necesario hacerlo así por cómo
       Java implementa sus genéricos; de otra forma obtenemos advertencias del
       compilador. */
    @SuppressWarnings("unchecked")
    private Lista<Entrada>[] nuevoArreglo(int n) {
        return (Lista<Entrada>[])Array.newInstance(Lista.class, n);
    }

    /**
     * Construye un diccionario con una capacidad inicial y dispersor
     * predeterminados.
     */
    public Diccionario() {
        this(MINIMA_CAPACIDAD, (K llave) -> llave.hashCode());
    }

    /**
     * Construye un diccionario con una capacidad inicial definida por el
     * usuario, y un dispersor predeterminado.
     * @param capacidad la capacidad a utilizar.
     */
    public Diccionario(int capacidad) {
        this(capacidad, (K llave) -> llave.hashCode());
    }

    /**
     * Construye un diccionario con una capacidad inicial predeterminada, y un
     * dispersor definido por el usuario.
     * @param dispersor el dispersor a utilizar.
     */
    public Diccionario(Dispersor<K> dispersor) {
        this(MINIMA_CAPACIDAD, dispersor);
    }

    /**
     * Construye un diccionario con una capacidad inicial y un método de
     * dispersor definidos por el usuario.
     * @param capacidad la capacidad inicial del diccionario.
     * @param dispersor el dispersor a utilizar.
     */
    public Diccionario(int capacidad, Dispersor<K> dispersor) {
        this.dispersor = dispersor;
        this.entradas = nuevoArreglo(calculaCapacidad(capacidad));
        this.elementos = 0;
        // Aquí va su código.
    }

    private int calculaCapacidad(int capacidad) {
        capacidad = (capacidad < 64) ? 64 : capacidad;
        int contador = 1;
        while (contador < capacidad * 2) {
            contador *= 2;
        }
        return contador;
    }

    /**
     * Agrega un nuevo valor al diccionario, usando la llave proporcionada. Si
     * la llave ya había sido utilizada antes para agregar un valor, el
     * diccionario reemplaza ese valor con el recibido aquí.
     * @param llave la llave para agregar el valor.
     * @param valor el valor a agregar.
     * @throws IllegalArgumentException si la llave o el valor son nulos.
     */
    public void agrega(K llave, V valor) {
        if (llave == null || valor == null) {
            throw new IllegalArgumentException();
        }
        int mascara = this.entradas.length - 1;
        int i = this.dispersor.dispersa(llave) & mascara;
        if (this.entradas[i] == null) {
            Lista<Entrada> nueva = new Lista<Entrada>();
            this.entradas[i] = nueva;
            Entrada nuevaEntrada = new Entrada(llave, valor);
            this.entradas[i].agregaFinal(nuevaEntrada);
            this.elementos += 1;
        } else {
            boolean hayLlave = false;
            for (int j = 0; j < this.entradas[i].getLongitud(); j++) {
                Entrada entrada = this.entradas[i].get(j);
                if (entrada.llave.equals(llave)) {
                    this.entradas[i].get(j).valor = valor;
                    hayLlave =  true;
                }
            }
            if (!(hayLlave)) {
                Entrada nuevaEntrada = new Entrada(llave, valor);
                this.entradas[i].agregaFinal(nuevaEntrada);
                this.elementos += 1;
            }
        }
        if (this.carga() >= MAXIMA_CARGA) {
            Lista<Entrada>[] nuevo = nuevoArreglo(2 * this.entradas.length);
            int nuevaMascara = nuevo.length - 1;
            for (int j = 0; j < this.entradas.length; j++) {
                if (this.entradas[j] != null && this.entradas[j].getLongitud() > 0) {
                    for (int k = 0; k < this.entradas[j].getLongitud(); k++) {
                        int nuevoIndice = this.dispersor.dispersa(this.entradas[j].get(k).llave) & nuevaMascara;
                        if (nuevo[nuevoIndice] == null) {
                            Lista<Entrada> nuevaLista = new Lista<Entrada>();
                            nuevo[nuevoIndice] = nuevaLista;
                            nuevo[nuevoIndice].agregaFinal(this.entradas[j].get(k));
                        } else {
                            nuevo[nuevoIndice].agregaFinal(this.entradas[j].get(k));
                        }
                    }
                }
            }
            this.entradas = nuevo;
        }
        // Aquí va su código.
    }

    /**
     * Regresa el valor del diccionario asociado a la llave proporcionada.
     * @param llave la llave para buscar el valor.
     * @return el valor correspondiente a la llave.
     * @throws IllegalArgumentException si la llave es nula.
     * @throws NoSuchElementException si la llave no está en el diccionario.
     */
    public V get(K llave) {
        if (llave == null) {
            throw new IllegalArgumentException();
        }
        int mascara = this.entradas.length - 1;
        int i = this.dispersor.dispersa(llave) & mascara;
        if (this.entradas[i] == null) {
            throw new NoSuchElementException();
        }
        for (int j = 0; j < this.entradas[i].getLongitud(); j++) {
            Entrada entrada = this.entradas[i].get(j);
            if (entrada.llave.equals(llave)) {
                return entrada.valor;
            }
        }
        throw new NoSuchElementException();
        // Aquí va su código.
    }

    /**
     * Nos dice si una llave se encuentra en el diccionario.
     * @param llave la llave que queremos ver si está en el diccionario.
     * @return <code>true</code> si la llave está en el diccionario,
     *         <code>false</code> en otro caso.
     */
    public boolean contiene(K llave) {
        if (llave == null) {
            return false;
        }
        int i = this.dispersor.dispersa(llave) & (this.entradas.length - 1);
        if (this.entradas[i] == null) {
            return false;
        }
        for (int j = 0; j < this.entradas[i].getLongitud(); j++) {
            if (this.entradas[i].get(j).llave.equals(llave)) {
                return true;
            }
        }
        return false;
        // Aquí va su código.
    }

    /**
     * Elimina el valor del diccionario asociado a la llave proporcionada.
     * @param llave la llave para buscar el valor a eliminar.
     * @throws IllegalArgumentException si la llave es nula.
     * @throws NoSuchElementException si la llave no se encuentra en
     *         el diccionario.
     */
    public void elimina(K llave) {
        if (llave == null) {
            throw new IllegalArgumentException();
        }
        int i = this.dispersor.dispersa(llave) & (this.entradas.length - 1);
        if (this.entradas[i] == null) {
            throw new IllegalArgumentException();
        }
        boolean hayLlave = false;
        for (int j = 0; j < this.entradas[i].getLongitud(); j++) {
            if (this.entradas[i].get(j).llave.equals(llave)) {
                hayLlave = true;
                Entrada eliminar = this.entradas[i].get(j);
                this.entradas[i].elimina(eliminar);
                this.elementos -= 1;
            }
        }
        if (!(hayLlave)) {
            throw new NoSuchElementException();
        }        
        // Aquí va su código.
    }

    /**
     * Nos dice cuántas colisiones hay en el diccionario.
     * @return cuántas colisiones hay en el diccionario.
     */
    public int colisiones() {
        int contadorCoalisiones = 0;
        for (int i = 0; i < this.entradas.length; i++) {
            if (this.entradas[i] != null && this.entradas[i].getLongitud() > 0) {
                contadorCoalisiones += this.entradas[i].getLongitud() - 1;
            }
        }
        return contadorCoalisiones;
        // Aquí va su código.
    }

    /**
     * Nos dice el máximo número de colisiones para una misma llave que tenemos
     * en el diccionario.
     * @return el máximo número de colisiones para una misma llave.
     */
    public int colisionMaxima() {
        int maxima = 0;
        int contador = 0;
        for (int i = 0; i < this.entradas.length; i++) {
            if (this.entradas[i] != null && this.entradas[i].getLongitud() > 0) {
                maxima = this.entradas[i].getLongitud();
                contador = i;
                break;
            }
        }
        for (int i = contador + 1; i < this.entradas.length; i++) {
            if (this.entradas[i] != null && this.entradas[i].getLongitud() > 0) {
                if (maxima < this.entradas[i].getLongitud()) {
                    maxima = this.entradas[i].getLongitud();
                }
            }
        }
        return maxima - 1;
        // Aquí va su código.
    }

    /**
     * Nos dice la carga del diccionario.
     * @return la carga del diccionario.
     */
    public double carga() {
        return (double) this.elementos / this.entradas.length;
        // Aquí va su código.
    }

    /**
     * Regresa el número de entradas en el diccionario.
     * @return el número de entradas en el diccionario.
     */
    public int getElementos() {
        return this.elementos;
        // Aquí va su código.
    }

    /**
     * Nos dice si el diccionario es vacío.
     * @return <code>true</code> si el diccionario es vacío, <code>false</code>
     *         en otro caso.
     */
    public boolean esVacia() {
        return this.elementos == 0;
        // Aquí va su código.
    }

    /**
     * Limpia el diccionario de elementos, dejándolo vacío.
     */
    public void limpia() {
        Lista<Entrada>[] limpiado = nuevoArreglo(this.entradas.length);
        this.elementos = 0;
        this.entradas = limpiado;
        // Aquí va su código.
    }

    /**
     * Regresa una representación en cadena del diccionario.
     * @return una representación en cadena del diccionario.
     */
    @Override public String toString() {
        if (this.elementos == 0) {
            return "{}";
        }
        String diccionario = "{ ";
        for (int i = 0; i < this.entradas.length; i++) {
            if (this.entradas[i] != null && this.entradas[i].getLongitud() > 0) {
                for (int j = 0; j < this.entradas[i].getLongitud(); j++) {
                    diccionario += "'" + this.entradas[i].get(j).llave + "': " + "'" + this.entradas[i].get(j).valor + "', "; 
                }
            }
        }
        return diccionario + "}";        
        // Aquí va su código.
    }

    /**
     * Nos dice si el diccionario es igual al objeto recibido.
     * @param o el objeto que queremos saber si es igual al diccionario.
     * @return <code>true</code> si el objeto recibido es instancia de
     *         Diccionario, y tiene las mismas llaves asociadas a los mismos
     *         valores.
     */
    @Override public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass())
            return false;
        @SuppressWarnings("unchecked") Diccionario<K, V> d =
            (Diccionario<K, V>)o;
        if (this.elementos != d.elementos) {
            return false;
        }
        Iterator<K> iterador = this.iteradorLlaves();
        while (iterador.hasNext()) {
            K llave = iterador.next();
            if (!(d.contiene(llave))) {
                return false;
            }
            V valorD = d.get(llave);
            V valor = this.get(llave);
            if (!(valor.equals(valorD))) {
                return false;
            }
        }
        return true;
        // Aquí va su código.
    }

    /**
     * Regresa un iterador para iterar las llaves del diccionario. El
     * diccionario se itera sin ningún orden específico.
     * @return un iterador para iterar las llaves del diccionario.
     */
    public Iterator<K> iteradorLlaves() {
        return new IteradorLlaves();
    }

    /**
     * Regresa un iterador para iterar los valores del diccionario. El
     * diccionario se itera sin ningún orden específico.
     * @return un iterador para iterar los valores del diccionario.
     */
    @Override public Iterator<V> iterator() {
        return new IteradorValores();
    }
}
