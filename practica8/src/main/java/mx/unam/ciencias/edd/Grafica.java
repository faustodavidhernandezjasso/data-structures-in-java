package mx.unam.ciencias.edd;

import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * Clase para gráficas. Una gráfica es un conjunto de vértices y aristas, tales
 * que las aristas son un subconjunto del producto cruz de los vértices.
 */
public class Grafica<T> implements Coleccion<T> {

    /* Clase interna privada para iteradores. */
    private class Iterador implements Iterator<T> {

        /* Iterador auxiliar. */
        private Iterator<Vertice> iterador;

        /* Construye un nuevo iterador, auxiliándose de la lista de vértices. */
        public Iterador() {
            this.iterador = vertices.iterator();
            // Aquí va su código.
        }

        /* Nos dice si hay un siguiente elemento. */
        @Override public boolean hasNext() {
            return this.iterador.hasNext();
            // Aquí va su código.
        }

        /* Regresa el siguiente elemento. */
        @Override public T next() {
            return this.iterador.next().elemento;
            // Aquí va su código.
        }
    }

    /* Clase interna privada para vértices. */
    private class Vertice implements VerticeGrafica<T>,
                          ComparableIndexable<Vertice> {

        /* El elemento del vértice. */
        public T elemento;
        /* El color del vértice. */
        public Color color;
        /* La distancia del vértice. */
        public double distancia;
        /* El índice del vértice. */
        public int indice;
        /* La lista de vecinos del vértice. */
        public Lista<Vecino> vecinos;

        /* Crea un nuevo vértice a partir de un elemento. */
        public Vertice(T elemento) {
            this.elemento = elemento;
            this.color = Color.NINGUNO;
            this.vecinos = new Lista<Vecino>();
            // Aquí va su código.
        }

        /* Regresa el elemento del vértice. */
        @Override public T get() {
            return this.elemento;
            // Aquí va su código.
        }

        /* Regresa el grado del vértice. */
        @Override public int getGrado() {
            return this.vecinos.getLongitud();
            // Aquí va su código.
        }

        /* Regresa el color del vértice. */
        @Override public Color getColor() {
            return this.color;
            // Aquí va su código.
        }

        /* Regresa un iterable para los vecinos. */
        @Override public Iterable<? extends VerticeGrafica<T>> vecinos() {
            return this.vecinos;
            // Aquí va su código.
        }

        /* Define el índice del vértice. */
        @Override public void setIndice(int indice) {
            this.indice = indice; 
            // Aquí va su código.
        }

        /* Regresa el índice del vértice. */
        @Override public int getIndice() {
            return this.indice;
            // Aquí va su código.
        }

        /* Compara dos vértices por distancia. */
        @Override public int compareTo(Vertice vertice) {
            return compara(this.distancia, vertice.distancia);
            // Aquí va su código.
        }
    }

    /* Clase interna privada para vértices vecinos. */
    private class Vecino implements VerticeGrafica<T> {

        /* El vértice vecino. */
        public Vertice vecino;
        /* El peso de la arista conectando al vértice con su vértice vecino. */
        public double peso;

        /* Construye un nuevo vecino con el vértice recibido como vecino y el
         * peso especificado. */
        public Vecino(Vertice vecino, double peso) {
            this.vecino = vecino;
            this.peso = peso;
            // Aquí va su código.
        }

        /* Regresa el elemento del vecino. */
        @Override public T get() {
            return vecino.elemento;
            // Aquí va su código.
        }

        /* Regresa el grado del vecino. */
        @Override public int getGrado() {
            return vecino.vecinos.getLongitud();
            // Aquí va su código.
        }

        /* Regresa el color del vecino. */
        @Override public Color getColor() {
            return vecino.color;
            // Aquí va su código.
        }

        /* Regresa un iterable para los vecinos del vecino. */
        @Override public Iterable<? extends VerticeGrafica<T>> vecinos() {
            return vecino.vecinos();
            // Aquí va su código.
        }
    }

    /* Interface para poder usar lambdas al buscar el elemento que sigue al
     * reconstruir un camino. */
    @FunctionalInterface
    private interface BuscadorCamino {
        /* Regresa true si el vértice se sigue del vecino. */
        public boolean seSiguen(Grafica.Vertice v, Grafica.Vecino a);
    }

    /* Vértices. */
    private Lista<Vertice> vertices;
    /* Número de aristas. */
    private int aristas;

    /**
     * Constructor único.
     */
    public Grafica() {
        this.vertices = new Lista<Vertice>();
        // Aquí va su código.
    }

    /**
     * Regresa el número de elementos en la gráfica. El número de elementos es
     * igual al número de vértices.
     * @return el número de elementos en la gráfica.
     */
    @Override public int getElementos() {
        return this.vertices.getLongitud();
        // Aquí va su código.
    }

    /**
     * Regresa el número de aristas.
     * @return el número de aristas.
     */
    public int getAristas() {
        return this.aristas;
        // Aquí va su código.
    }

    private Vertice buscaVertice(T elemento) {
        for (Vertice vertice : vertices) {
            if (vertice.elemento.equals(elemento)) {
                return vertice;
            }
        }
        return null;
    }

    /**
     * Agrega un nuevo elemento a la gráfica.
     * @param elemento el elemento a agregar.
     * @throws IllegalArgumentException si el elemento es <code>null</code> o ya
     *         había sido agregado a la gráfica.
     */
    @Override public void agrega(T elemento) {
        if (elemento == null || contiene(elemento)) {
            throw new IllegalArgumentException();
        }
        Vertice nuevo = new Vertice(elemento);
        this.vertices.agregaFinal(nuevo);
        // Aquí va su código.
    }

    /**
     * Conecta dos elementos de la gráfica. Los elementos deben estar en la
     * gráfica. El peso de la arista que conecte a los elementos será 1.
     * @param a el primer elemento a conectar.
     * @param b el segundo elemento a conectar.
     * @throws NoSuchElementException si a o b no son elementos de la gráfica.
     * @throws IllegalArgumentException si a o b ya están conectados, o si a es
     *         igual a b.
     */
    public void conecta(T a, T b) {
        conecta(a, b, 1);
        // Aquí va su código.
    }

    /**
     * Conecta dos elementos de la gráfica. Los elementos deben estar en la
     * gráfica.
     * @param a el primer elemento a conectar.
     * @param b el segundo elemento a conectar.
     * @param peso el peso de la nueva vecino.
     * @throws NoSuchElementException si a o b no son elementos de la gráfica.
     * @throws IllegalArgumentException si a o b ya están conectados, si a es
     *         igual a b, o si el peso es no positivo.
     */
    public void conecta(T a, T b, double peso) {
        if (a.equals(b) || sonVecinos(a, b) || peso < 0) {
            throw new IllegalArgumentException();
        }
        Vertice verticeA = buscaVertice(a);
        Vertice verticeB = buscaVertice(b);
        if (verticeA == null || verticeB == null) {
            throw new NoSuchElementException();
        }
        verticeB.vecinos.agregaFinal(new Vecino(verticeA, peso));
        verticeA.vecinos.agregaFinal(new Vecino(verticeB, peso));
        this.aristas += 1;
        // Aquí va su código.
    }

    private Vecino buscaVecino(Vertice verticeA, Vertice verticeB) {
        for (Vecino v : verticeA.vecinos) {
            if (v.vecino.elemento.equals(verticeB.elemento)) {
                return v;
            }
        }
        return null;
    }

    /**
     * Desconecta dos elementos de la gráfica. Los elementos deben estar en la
     * gráfica y estar conectados entre ellos.
     * @param a el primer elemento a desconectar.
     * @param b el segundo elemento a desconectar.
     * @throws NoSuchElementException si a o b no son elementos de la gráfica.
     * @throws IllegalArgumentException si a o b no están conectados.
     */
    public void desconecta(T a, T b) {
        if (a.equals(b)) {
            throw new IllegalArgumentException();
        }
        Vertice verticeA = buscaVertice(a);
        Vertice verticeB = buscaVertice(b);
        if (verticeA == null || verticeB == null) {
            throw new NoSuchElementException();
        }
        if (!(sonVecinos(a, b))) {
            throw new IllegalArgumentException();
        }
        verticeA.vecinos.elimina(buscaVecino(verticeA, verticeB));
        verticeB.vecinos.elimina(buscaVecino(verticeB, verticeA));
        this.aristas -= 1;
        // Aquí va su código.
    }

    /**
     * Nos dice si el elemento está contenido en la gráfica.
     * @return <code>true</code> si el elemento está contenido en la gráfica,
     *         <code>false</code> en otro caso.
     */
    @Override public boolean contiene(T elemento) {
        for (Vertice vertice : vertices) {
            if (elemento.equals(vertice.elemento)) {
                return true;
            }
        }
        return false;
        // Aquí va su código.
    }

    /**
     * Elimina un elemento de la gráfica. El elemento tiene que estar contenido
     * en la gráfica.
     * @param elemento el elemento a eliminar.
     * @throws NoSuchElementException si el elemento no está contenido en la
     *         gráfica.
     */
    @Override public void elimina(T elemento) {
        Vertice vertice = buscaVertice(elemento);
        if (vertice == null) {
            throw new NoSuchElementException();
        }
        this.vertices.elimina(vertice);
        for (Vecino v : vertice.vecinos) {
            v.vecino.vecinos.elimina(buscaVecino(v.vecino, vertice));
            this.aristas -= 1;
        } 
        // Aquí va su código.
    }

    /**
     * Nos dice si dos elementos de la gráfica están conectados. Los elementos
     * deben estar en la gráfica.
     * @param a el primer elemento.
     * @param b el segundo elemento.
     * @return <code>true</code> si a y b son vecinos, <code>false</code> en otro caso.
     * @throws NoSuchElementException si a o b no son elementos de la gráfica.
     */
    public boolean sonVecinos(T a, T b) {
        Vertice verticeA = buscaVertice(a);
        Vertice verticeB = buscaVertice(b);
        if (verticeA == null || verticeB == null) {
            throw new NoSuchElementException();
        }
        return sonVecinos(verticeA, verticeB);
        // Aquí va su código.
    }

    private boolean sonVecinos(Vertice a, Vertice b) {
        boolean verticeA = false;
        boolean verticeB = false;
        for (Vecino v : a.vecinos) {
            if (v.vecino.elemento.equals(b.elemento)) {
                verticeB = true;
            }
        }
        for (Vecino v : b.vecinos) {
            if (v.vecino.elemento.equals(a.elemento)) {
                verticeA = true;
            }
        }
        return verticeA && verticeB;
    }

    /**
     * Regresa el peso de la arista que comparten los vértices que contienen a
     * los elementos recibidos.
     * @param a el primer elemento.
     * @param b el segundo elemento.
     * @return el peso de la arista que comparten los vértices que contienen a
     *         los elementos recibidos.
     * @throws NoSuchElementException si a o b no son elementos de la gráfica.
     * @throws IllegalArgumentException si a o b no están conectados.
     */
    public double getPeso(T a, T b) {
        Vertice verticeA = buscaVertice(a);
        Vertice verticeB = buscaVertice(b);
        if (verticeA == null || verticeB == null) {
            throw new NoSuchElementException();
        }
        if (!sonVecinos(a, b)) {
            throw new IllegalArgumentException();
        }
        Vecino vecino = buscaVecino(verticeA, verticeB);
        return vecino.peso;
        // Aquí va su código.
    }

    /**
     * Define el peso de la arista que comparten los vértices que contienen a
     * los elementos recibidos.
     * @param a el primer elemento.
     * @param b el segundo elemento.
     * @param peso el nuevo peso de la arista que comparten los vértices que
     *        contienen a los elementos recibidos.
     * @throws NoSuchElementException si a o b no son elementos de la gráfica.
     * @throws IllegalArgumentException si a o b no están conectados, o si peso
     *         es menor o igual que cero.
     */
    public void setPeso(T a, T b, double peso) {
        if (!(sonVecinos(a, b)) || peso <= 0) {
            throw new IllegalArgumentException();
        }
        Vertice verticeA = buscaVertice(a);
        Vertice verticeB = buscaVertice(b);
        if (verticeA == null || verticeB == null) {
            throw new NoSuchElementException();
        }
        Vecino vecinoA = buscaVecino(verticeB, verticeA);
        vecinoA.peso = peso;
        Vecino vecinoB = buscaVecino(verticeA, verticeB);
        vecinoB.peso = peso;
        // Aquí va su código.
    }

    /**
     * Regresa el vértice correspondiente el elemento recibido.
     * @param elemento el elemento del que queremos el vértice.
     * @throws NoSuchElementException si elemento no es elemento de la gráfica.
     * @return el vértice correspondiente el elemento recibido.
     */
    public VerticeGrafica<T> vertice(T elemento) {
        for (Vertice vertice : vertices) {
            if (vertice.elemento.equals(elemento)) {
                return (VerticeGrafica<T>) vertice;
            }
        }
        throw new NoSuchElementException();
        // Aquí va su código.
    }

    /**
     * Define el color del vértice recibido.
     * @param vertice el vértice al que queremos definirle el color.
     * @param color el nuevo color del vértice.
     * @throws IllegalArgumentException si el vértice no es válido.
     */
    public void setColor(VerticeGrafica<T> vertice, Color color) {
        if ((vertice.getClass() != Vertice.class && vertice.getClass() != Vecino.class) || vertice == null) {
            throw new IllegalArgumentException();
        }
        if (vertice.getClass() == Vertice.class) {
            Vertice v = (Vertice) vertice;
            v.color = color;
        }
        if (vertice.getClass() == Vecino.class) {
            Vecino v = (Vecino) vertice;
            v.vecino.color = color;
        }
        // Aquí va su código.
    }

    /**
     * Nos dice si la gráfica es conexa.
     * @return <code>true</code> si la gráfica es conexa, <code>false</code> en
     *         otro caso.
     */
    public boolean esConexa() {
        if (this.esVacia()) {
            return true;
        }
        Vertice v = this.vertices.getPrimero();
        for (Vertice vertice : vertices) {
            vertice.color = Color.ROJO;
        }
        v.color = Color.NEGRO;
        Pila<Vertice> pila = new Pila<Vertice>();
        pila.mete(v);
        while (!(pila.esVacia())) {
            Vertice u = pila.saca();
            for (Vecino vec : u.vecinos) {
                if (vec.vecino.color == Color.ROJO) {
                    vec.vecino.color = Color.NEGRO;
                    pila.mete(vec.vecino);
                }
            }
        }
        for (Vertice vertice : vertices) {
            if (vertice.color != Color.NEGRO) {
                return false;
            }
            vertice.color = Color.NINGUNO;
        }
        return true;
        // Aquí va su código.
    }

    /**
     * Realiza la acción recibida en cada uno de los vértices de la gráfica, en
     * el orden en que fueron agregados.
     * @param accion la acción a realizar.
     */
    public void paraCadaVertice(AccionVerticeGrafica<T> accion) {
        for (Vertice vertice : vertices) {
            accion.actua(vertice);
        }
        // Aquí va su código.
    }

    /**
     * Realiza la acción recibida en todos los vértices de la gráfica, en el
     * orden determinado por BFS, comenzando por el vértice correspondiente al
     * elemento recibido. Al terminar el método, todos los vértices tendrán
     * color {@link Color#NINGUNO}.
     * @param elemento el elemento sobre cuyo vértice queremos comenzar el
     *        recorrido.
     * @param accion la acción a realizar.
     * @throws NoSuchElementException si el elemento no está en la gráfica.
     */
    public void bfs(T elemento, AccionVerticeGrafica<T> accion) {
        Vertice v = buscaVertice(elemento);
        if (v == null) {
            throw new NoSuchElementException();
        }
        for (Vertice vertice : vertices) {
            vertice.color = Color.ROJO;
        }
        Cola<Vertice> cola = new Cola<Vertice>();
        v.color = Color.NEGRO;
        cola.mete(v);
        while (!(cola.esVacia())) {
            Vertice u = cola.saca();
            accion.actua(u);
            for (Vecino vec : u.vecinos) {
                if (vec.vecino.color == Color.ROJO) {
                    vec.vecino.color = Color.NEGRO;
                    cola.mete(vec.vecino);
                }
            }
        }
        for (Vertice vertice : vertices) {
            vertice.color = Color.NINGUNO;
        }
        // Aquí va su código.
    }

    /**
     * Realiza la acción recibida en todos los vértices de la gráfica, en el
     * orden determinado por DFS, comenzando por el vértice correspondiente al
     * elemento recibido. Al terminar el método, todos los vértices tendrán
     * color {@link Color#NINGUNO}.
     * @param elemento el elemento sobre cuyo vértice queremos comenzar el
     *        recorrido.
     * @param accion la acción a realizar.
     * @throws NoSuchElementException si el elemento no está en la gráfica.
     */
    public void dfs(T elemento, AccionVerticeGrafica<T> accion) {
        Vertice v = buscaVertice(elemento);
        if (v == null) {
            throw new NoSuchElementException();
        }
        for (Vertice vertice : vertices) {
            vertice.color = Color.ROJO;
        }
        Pila<Vertice> pila = new Pila<Vertice>();
        v.color = Color.NEGRO;
        pila.mete(v);
        while (!(pila.esVacia())) {
            Vertice u = pila.saca();
            accion.actua(u);
            for (Vecino vec : u.vecinos) {
                if (vec.vecino.color == Color.ROJO) {
                    vec.vecino.color = Color.NEGRO;
                    pila.mete(vec.vecino);
                }
            }
        }
        for (Vertice vertice : vertices) {
            vertice.color = Color.NINGUNO;
        }
        // Aquí va su código.
    }

    /**
     * Nos dice si la gráfica es vacía.
     * @return <code>true</code> si la gráfica es vacía, <code>false</code> en
     *         otro caso.
     */
    @Override public boolean esVacia() {
        return this.vertices.esVacia();
        // Aquí va su código.
    }

    /**
     * Limpia la gráfica de vértices y aristas, dejándola vacía.
     */
    @Override public void limpia() {
        this.vertices.limpia();
        this.aristas = 0;
        // Aquí va su código.
    }

    /**
     * Regresa una representación en cadena de la gráfica.
     * @return una representación en cadena de la gráfica.
     */
    @Override public String toString() {
        String grafica = "{";
        for (Vertice vertice : vertices) {
            grafica += vertice.elemento.toString() + ", ";
            vertice.color = Color.ROJO;
        }
        grafica += "}, {";
        for (Vertice vertice : vertices) {
            vertice.color = Color.NEGRO;
            for (Vecino vec : vertice.vecinos) {
                if (vec.vecino.color == Color.ROJO) {
                    grafica += "(" + vertice.elemento.toString() + ", " + vec.vecino.elemento.toString() + "), ";
                }
            }
        }
        grafica += "}";
        for (Vertice vertice : vertices) {
            vertice.color = Color.NINGUNO;
        }
        return grafica;
        // Aquí va su código.
    }

    /**
     * Nos dice si la gráfica es igual al objeto recibido.
     * @param objeto el objeto con el que hay que comparar.
     * @return <code>true</code> si la gráfica es igual al objeto recibido;
     *         <code>false</code> en otro caso.
     */
    @Override public boolean equals(Object objeto) {
        if (objeto == null || getClass() != objeto.getClass())
            return false;
        @SuppressWarnings("unchecked") Grafica<T> grafica = (Grafica<T>)objeto;
        if (this.getElementos() != grafica.getElementos()) {
            return false;
        }
        if (this.aristas != grafica.aristas) {
            return false;
        }
        for (Vertice vertice : this.vertices) {
            Vertice v = vecinos(vertice, grafica.vertices);
            if (v == null) {
                return false;
            }
            for (Vecino vec : vertice.vecinos) {
                Vertice ver = verificaVecinos(vec.vecino, v.vecinos);
                if (ver == null) {
                    return false;
                }
            }
        }
        return true;
        // Aquí va su código.
    }

    private Vertice vecinos(Vertice vertice, Lista<Vertice> vertices) {
        for (Vertice v : vertices) {
            if (v.elemento.equals(vertice.elemento)) {
                return v;
            }
        }
        return null;
    }

    private Vertice verificaVecinos(Vertice vertice, Lista<Vecino> vecinos) {
        for (Vecino v : vecinos) {
            if (v.vecino.elemento.equals(vertice.elemento)) {
                return v.vecino;
            }
        }
        return null;
    }

    /**
     * Regresa un iterador para iterar la gráfica. La gráfica se itera en el
     * orden en que fueron agregados sus elementos.
     * @return un iterador para iterar la gráfica.
     */
    @Override public Iterator<T> iterator() {
        return new Iterador();
    }

    /**
     * Calcula una trayectoria de distancia mínima entre dos vértices.
     * @param origen el vértice de origen.
     * @param destino el vértice de destino.
     * @return Una lista con vértices de la gráfica, tal que forman una
     *         trayectoria de distancia mínima entre los vértices <code>a</code> y
     *         <code>b</code>. Si los elementos se encuentran en componentes conexos
     *         distintos, el algoritmo regresa una lista vacía.
     * @throws NoSuchElementException si alguno de los dos elementos no está en
     *         la gráfica.
     */
    public Lista<VerticeGrafica<T>> trayectoriaMinima(T origen, T destino) {
        Vertice verticeOrigen = buscaVertice(origen);
        Vertice verticeDestino = buscaVertice(destino);
        if (verticeOrigen == null || verticeDestino == null) {
            throw new NoSuchElementException();
        }
        Lista<VerticeGrafica<T>> trayectoria = new Lista<VerticeGrafica<T>>();
        if (origen.equals(destino)) {
            trayectoria.agregaFinal(verticeOrigen);
            return trayectoria;
        }
        for (Vertice vertice : this.vertices) {
            vertice.distancia = -1;
        }
        verticeOrigen.distancia = 0;
        Cola<Vertice> cola = new Cola<Vertice>();
        cola.mete(verticeOrigen);
        while (!(cola.esVacia())) {
            Vertice u = cola.saca();
            for (Vecino v : u.vecinos) {
                if (v.vecino.distancia == -1) {
                    v.vecino.distancia = u.distancia + 1;
                    cola.mete(v.vecino);
                }
            }
        }
        if (verticeDestino.distancia == -1) {
            return trayectoria;
        }
        Vertice u = verticeDestino;
        trayectoria.agregaFinal(u);
        while (!(u.elemento.equals(verticeOrigen.elemento))) {
            for (Vecino v : u.vecinos) {
                if (v.vecino.distancia == u.distancia - 1) {
                    trayectoria.agregaFinal(v.vecino);
                    u = v.vecino;
                }
            }
        }
        return trayectoria.reversa();
        // Aquí va su código.
    }

    /**
     * Calcula la ruta de peso mínimo entre el elemento de origen y el elemento
     * de destino.
     * @param origen el vértice origen.
     * @param destino el vértice destino.
     * @return una trayectoria de peso mínimo entre el vértice <code>origen</code> y
     *         el vértice <code>destino</code>. Si los vértices están en componentes
     *         conexas distintas, regresa una lista vacía.
     * @throws NoSuchElementException si alguno de los dos elementos no está en
     *         la gráfica.
     */
    public Lista<VerticeGrafica<T>> dijkstra(T origen, T destino) {
        Vertice s = buscaVertice(origen);
        Vertice t = buscaVertice(destino);
        if (s == null || t == null) {
            throw new NoSuchElementException();
        }
        for (Vertice vertice : vertices) {
            vertice.distancia = -1;
        }
        s.distancia = 0;
        MonticuloMinimo<Vertice> monticulo = new MonticuloMinimo<Vertice>(vertices);
        while(!(monticulo.esVacia())) {
            Vertice u = monticulo.elimina();
            for (Vecino v : u.vecinos) {
                if (compara(v.vecino.distancia, sumaDistanciaYPeso(u.distancia, v.peso)) > 0) {
                    v.vecino.distancia = sumaDistanciaYPeso(u.distancia, v.peso);
                    monticulo.reordena(v.vecino);
                }
            }
        }
        Lista<VerticeGrafica<T>> trayectoria = new Lista<VerticeGrafica<T>>();
        if (t.distancia == -1) {
            return trayectoria;
        }
        Vertice u = t;
        trayectoria.agregaFinal(t);
        while (!(u.elemento.equals(s.elemento))) {
            for (Vecino v : u.vecinos) {
                if (u.distancia == sumaDistanciaYPeso(v.vecino.distancia, 1)) {
                    trayectoria.agregaFinal(v.vecino);
                    u = v.vecino;
                }
            }
        }
        return trayectoria.reversa();
        // Aquí va su código.
    }

    private int compara(double x, double y) {
        if (x != -1 && (y == -1 || y > x)) {
            return -1;
        } else if (y != -1 && (x == -1 || x > y)) {
            return 1;
        } else {
            return 0;
        }
    }

    private double sumaDistanciaYPeso(double a, double b) {
        if (a == -1 || b == -1) {
            return -1;
        }
        return a + b;
    }
}
