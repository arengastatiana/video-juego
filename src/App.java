import java.util.Random;
import java.util.Scanner;

class Personaje {
    String nombre;
    int vida;
    int vidaMaxima;
    int fuerza;
    boolean defendiendo = false;

    public Personaje(String nombre, int vida, int fuerza) {
        this.nombre = nombre;
        this.vida = vida;
        this.vidaMaxima = vida; // Almacena la vida máxima
        this.fuerza = fuerza;
    }

    // Método para mostrar el estado del personaje
    public void mostrarEstado() {
        System.out.printf("Nombre: %s, Vida: %d, Fuerza: %d%n", nombre, vida, fuerza);
    }

    // Método para atacar
    public void atacar(Personaje oponente) {
        int daño = this.fuerza;
        if (oponente.defendiendo) {
            daño /= 2; // Si está defendiendo, bloquea la mitad del daño
            System.out.printf("%s se defiende y solo recibe %d puntos de daño%n", oponente.nombre, daño);
            oponente.defendiendo = false; // Termina el estado de defensa
        } else {
            System.out.printf("%s ataca a %s y le quita %d puntos de vida%n", nombre, oponente.nombre, daño);
        }
        oponente.vida -= daño;
    }

    // Método para curarse
    public void curarse() {
        if (vida < vidaMaxima) { // Solo se cura si ha perdido vida
            int vidaPerdida = vidaMaxima - vida;
            int curacion = (int)(vidaPerdida * 0.3); // Curarse 30% de la vida perdida
            vida = Math.min(vida + curacion, vidaMaxima); // Asegurarse de no superar la vida máxima
            System.out.printf("%s se ha curado %d puntos de vida. Vida actual: %d%n", nombre, curacion, vida);
        } else {
            System.out.printf("%s tiene la vida completa y no puede curarse.%n", nombre);
        }
    }

    // Método para defenderse
    public void defender() {
        defendiendo = true;
        System.out.printf("%s se prepara para defenderse del próximo ataque.%n", nombre);
    }
}

class Campeones extends Personaje {
    public Campeones(String nombre, int vida, int fuerza) {
        super(nombre, vida, fuerza);
    }
}

class Malhechores extends Personaje {
    public Malhechores(String nombre, int vida, int fuerza) {
        super(nombre, vida, fuerza);
    }
}

public class App {
    private static Scanner scanner = new Scanner(System.in);
    private static Campeones[] campeones;
    private static Malhechores[] malhechores;
    private static Campeones campeonSeleccionado;
    private static Malhechores malhechorSeleccionado;

    public static void main(String[] args) {
        // Inicializar campeones
        campeones = new Campeones[]{
            new Campeones("Estrella Valerosa", 100, 50),
            new Campeones("Luz Celestial", 150, 30),
            new Campeones("Corazon de Trueno", 200, 20)
        };

        // Inicializar malhechores
        malhechores = new Malhechores[]{
            new Malhechores("Furia de Acero", 120, 45),
            new Malhechores("Señor del Caos", 140, 30),
            new Malhechores("Ira Venenosa", 180, 15)
        };

        // Seleccionar personajes
        campeonSeleccionado = seleccionarCampeon();
        malhechorSeleccionado = seleccionarMalhechor();

        // Mostrar personajes seleccionados
        System.out.println("\nPersonaje Campeón seleccionado:");
        campeonSeleccionado.mostrarEstado();

        System.out.println("\nPersonaje Malhechor seleccionado:");
        malhechorSeleccionado.mostrarEstado();

        // Decidir aleatoriamente quién comienza
        boolean turnoCampeon = decidirTurnoInicial();

        // Juego principal
        boolean salir = false;

        while (!salir) {
            if (turnoCampeon) {
                System.out.println("\nEs el turno del Campeón: " + campeonSeleccionado.nombre);
                verOpciones();
                int opcion = obtenerIntEntrada("Elige una opción: ");
                ejecutarAccion(campeonSeleccionado, malhechorSeleccionado, opcion);
            } else {
                System.out.println("\nEs el turno del Malhechor: " + malhechorSeleccionado.nombre);
                verOpciones();
                int opcion = obtenerIntEntrada("Elige una opción: ");
                ejecutarAccion(malhechorSeleccionado, campeonSeleccionado, opcion);
            }

            // Verificar si algún jugador ha sido derrotado
            if (campeonSeleccionado.vida <= 0 || malhechorSeleccionado.vida <= 0) {
                salir = true;
                if (campeonSeleccionado.vida <= 0) {
                    System.out.println("¡El Campeón ha sido derrotado! ¡El Malhechor gana!");
                } else if (malhechorSeleccionado.vida <= 0) {
                    System.out.println("¡El Malhechor ha sido derrotado! ¡El Campeón gana!");
                }
            }

            turnoCampeon = !turnoCampeon; // Alternar turno
        }

        scanner.close();
    }

    // Método para seleccionar campeón
    public static Campeones seleccionarCampeon() {
        System.out.println("\nSelecciona a tu campeón:");
        for (int i = 0; i < campeones.length; i++) {
            System.out.printf("%d. %s (Vida: %d, Fuerza: %d)%n", i + 1, campeones[i].nombre, campeones[i].vida, campeones[i].fuerza);
        }
        int seleccion = obtenerIntEntrada("Elige un campeón: ") - 1;
        return campeones[seleccion];
    }

    // Método para seleccionar malhechor
    public static Malhechores seleccionarMalhechor() {
        System.out.println("\nSelecciona a tu malhechor:");
        for (int i = 0; i < malhechores.length; i++) {
            System.out.printf("%d. %s (Vida: %d, Fuerza: %d)%n", i + 1, malhechores[i].nombre, malhechores[i].vida, malhechores[i].fuerza);
        }
        int seleccion = obtenerIntEntrada("Elige un malhechor: ") - 1;
        return malhechores[seleccion];
    }

    // Método para decidir quién comienza aleatoriamente
    public static boolean decidirTurnoInicial() {
        Random random = new Random();
        boolean turnoCampeon = random.nextBoolean(); // true si el campeón empieza, false si el malhechor empieza
        if (turnoCampeon) {
            System.out.println("El Campeón comienza el primer turno.");
        } else {
            System.out.println("El Malhechor comienza el primer turno.");
        }
        return turnoCampeon;
    }

    public static void verOpciones() {
        System.out.println("\n¿Qué quieres hacer en esta ronda?");
        System.out.println("1. Atacar");
        System.out.println("2. Defender");
        System.out.println("3. Curarse");
    }

    // Ejecutar la acción seleccionada por el jugador
    public static void ejecutarAccion(Personaje jugador, Personaje oponente, int opcion) {
        switch (opcion) {
            case 1:
                jugador.atacar(oponente);
                break;
            case 2:
                jugador.defender();
                break;
            case 3:
                jugador.curarse();
                break;
            default:
                System.out.println("Opción no válida, vuelve a intentarlo.");
        }
    }

    // Método para obtener una entrada de número entero
    public static int obtenerIntEntrada(String mensaje) {
        while (true) {
            try {
                System.out.println(mensaje);
                return Integer.parseInt(scanner.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("Ingresa una opción válida.");
            }
        }
    }
}
