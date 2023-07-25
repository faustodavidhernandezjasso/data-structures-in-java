package mx.unam.ciencias.edd;

/**
 * Clase para métodos estáticos con dispersores de bytes.
 */
public class Dispersores {

    /* Constructor privado para evitar instanciación. */
    private Dispersores() {}

    /**
     * Función de dispersión XOR.
     * @param llave la llave a dispersar.
     * @return la dispersión de XOR de la llave.
     */
    public static int dispersaXOR(byte[] llave) {
        if (llave.length % 4 != 0) {
            int residuo = llave.length % 4;
            int completar = 4 - residuo;
            byte[] nuevo = new byte[llave.length + completar];
            for (int i = 0; i < nuevo.length; i++) {
                if (i < llave.length) {
                    nuevo[i] = llave[i];
                } else {
                    nuevo[i] = (byte) 0;
                }
            }
            int dispersion = 0;
            for (int i = 0; i < nuevo.length; i += 4) {
                dispersion ^= combinaBigEndian(nuevo[i], nuevo[i + 1], nuevo[i + 2], nuevo[i + 3]);   
            }
            return dispersion;
        } else {
            int dispersion = 0;
            for (int i = 0; i < llave.length; i += 4) {
                dispersion ^= combinaBigEndian(llave[i], llave[i + 1], llave[i + 2], llave[i + 3]);
            }
            return dispersion; 
        }
        // Aquí va su código.
    }

    private static int combinaBigEndian(byte a, byte b, byte c, byte d) {
        int entero = ((a & 0xFF) << 24) | ((b & 0xFF) << 16) | ((c & 0xFF) << 8) | (d & 0xFF);
        return entero;
    }

    /**
     * Función de dispersión de Bob Jenkins.
     * @param llave la llave a dispersar.
     * @return la dispersión de Bob Jenkins de la llave.
     */
    public static int dispersaBJ(byte[] llave) {
        int a = 0x9E3779B9;
        int b = 0x9E3779B9;
        int c = 0xFFFFFFFF;
        int residuo = llave.length % 12;
        int longitud = llave.length - residuo;
        for (int i = 0; i < longitud; i += 12) {
            a += combinaLittleEndian(llave[i], llave[i + 1], llave[i + 2], llave[i + 3]);
            b += combinaLittleEndian(llave[i + 4], llave[i + 5], llave[i + 6], llave[i + 7]);
            c += combinaLittleEndian(llave[i + 8], llave[i + 9],  llave[i + 10], llave[i + 11]);
            int[] mezclados = mezcla(a, b, c);
            a = mezclados[0];
            b = mezclados[1];
            c = mezclados[2];
        }
        c += llave.length;
        switch (residuo) {
            case 11:
                c += (llave[longitud + 10] & 0xFF) << 24;
            case 10:
                c += (llave[longitud + 9] & 0xFF) << 16;
            case 9:
                c += (llave[longitud + 8] & 0xFF) << 8;
            case 8:
                b += (llave[longitud + 7] & 0xFF) << 24;
            case 7:
                b += (llave[longitud + 6] & 0xFF) << 16;
            case 6:
                b += (llave[longitud + 5] & 0xFF) << 8;
            case 5:
                b += (llave[longitud + 4] & 0xFF);
            case 4:
                a += (llave[longitud + 3] & 0xFF) << 24;
            case 3:
                a += (llave[longitud + 2] & 0xFF) << 16;
            case 2:
                a += (llave[longitud + 1] & 0xFF) << 8;
            case 1:
                a += (llave[longitud] & 0xFF);
        }
        int[] mezclados = mezcla(a, b, c);
        return mezclados[2];
        // Aquí va su código.
    }

    private static int[] mezcla(int a, int b, int c) {
        int[] mezclados = new int[3];
        // 1ra Parte
        a -= b;
        a -= c;
        a ^= (c >>> 13);
        b -= c;
        b -= a;
        b ^= (a << 8);
        c -= a;
        c -= b;
        c ^= (b >>> 13);
        // 2da Parte
        a -= b;
        a -= c;
        a ^= (c >>> 12);
        b -= c;
        b -= a;
        b ^= (a << 16);
        c -= a;
        c -= b;
        c ^= (b >>> 5);
        // 3ra Parte
        a -= b;
        a -= c;
        a ^= (c >>> 3);
        b -= c;
        b -= a;
        b ^= (a << 10);
        c -= a;
        c -= b;
        c ^= (b >>> 15);
        mezclados[0] = a;
        mezclados[1] = b;
        mezclados[2] = c;
        return mezclados;
    }

    private static int combinaLittleEndian(byte a, byte b, byte c, byte d) {
        int entero = (a & 0xFF) | ((b & 0xFF) << 8) | ((c & 0xFF) << 16) | ((d & 0xFF) << 24);
        return entero;
    }

    /**
     * Función de dispersión Daniel J. Bernstein.
     * @param llave la llave a dispersar.
     * @return la dispersión de Daniel Bernstein de la llave.
     */
    public static int dispersaDJB(byte[] llave) {
        int h = 5381;
        for (int i = 0; i < llave.length; i++) {
            h *= 33;
            h += llave[i] & 0xFF;
        }
        return h;
        // Aquí va su código.
    }
}
