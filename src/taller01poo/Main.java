/* Vicente Guerra - 21.855.415-6 - ITI
 * Diego Meneses - 22.087.010-3 - ICCI
 */

package taller01poo;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

public class Main
{
    // arma los vectores y maneja el menu principal hasta que se elija salir
    public static void main(String[] args)
    {
        Scanner teclado = new Scanner(System.in);
        int capacidad = 100;

        // lista del curso
        String[] nombresAlumnos = new String[capacidad];
        String[] apellidosAlumnos = new String[capacidad];
        String[] rutsAlumnos = new String[capacidad];
        String[] paralelosAlumnos = new String[capacidad];

        // solicitudes y su estado
        String[] nombresSolicitudes = new String[capacidad];
        String[] apellidosSolicitudes = new String[capacidad];
        String[] estadosSolicitudes = new String[capacidad];

        // los que entraron al grupo
        String[] nombresMiembros = new String[capacidad];
        String[] apellidosMiembros = new String[capacidad];
        String[] rutsMiembros = new String[capacidad];
        String[] paralelosMiembros = new String[capacidad];

        // rechazados y cuantas veces lo intentaron
        String[] nombresRechazados = new String[capacidad];
        String[] rutsRechazados = new String[capacidad];
        int[] intentosRechazados = new int[capacidad];

        boolean alumnosCargados = false;
        int intentosManuales = 0;

        int opcionSalir = 7;
        int opcion;
        do
        {
            mostrarMenuPrincipal();
            opcion = leerOpcion(teclado, opcionSalir);
            if (opcion == 1)
            {
                alumnosCargados = cargarArchivos(nombresAlumnos, apellidosAlumnos, rutsAlumnos, paralelosAlumnos,
                        nombresSolicitudes, apellidosSolicitudes, estadosSolicitudes, alumnosCargados);
            }
            else if (opcion == 2)
            {
                procesarSolicitudes(nombresAlumnos, apellidosAlumnos, rutsAlumnos, paralelosAlumnos,
                        nombresSolicitudes, apellidosSolicitudes, estadosSolicitudes, nombresMiembros,
                        apellidosMiembros, rutsMiembros, paralelosMiembros, nombresRechazados, rutsRechazados,
                        intentosRechazados);
            }
            else if (opcion == 3)
            {
                intentosManuales = intentosManuales + inscribirManualmente(teclado, nombresAlumnos,
                        apellidosAlumnos, rutsAlumnos, paralelosAlumnos, nombresMiembros, apellidosMiembros,
                        rutsMiembros, paralelosMiembros, nombresRechazados, rutsRechazados, intentosRechazados,
                        alumnosCargados);
            }
            else if (opcion == 4)
            {
                administrarCurso(teclado, nombresAlumnos, apellidosAlumnos, rutsAlumnos, paralelosAlumnos,
                        nombresMiembros, apellidosMiembros, rutsMiembros, paralelosMiembros, alumnosCargados);
            }
            else if (opcion == 5)
            {
                generarReportes(teclado, nombresMiembros, apellidosMiembros, rutsMiembros, paralelosMiembros,
                        nombresRechazados, rutsRechazados, alumnosCargados);
            }
            else if (opcion == 6)
            {
                mostrarEstadisticas(estadosSolicitudes, paralelosMiembros, nombresRechazados, intentosRechazados,
                        intentosManuales);
            }
        }
        while (opcion != opcionSalir);

        System.out.println("Hasta pronto.");
        teclado.close();
    }

    // muestra las 7 opciones del menu principal
    public static void mostrarMenuPrincipal()
    {
        System.out.println();
        System.out.println("====== Grupo POO: control de ingreso ======");
        System.out.println("1) Cargar Alumnos.txt y Solicitudes.txt");
        System.out.println("2) Filtrar solicitudes automaticamente");
        System.out.println("3) Inscribir manualmente a una persona");
        System.out.println("4) Administrar la lista del curso");
        System.out.println("5) Generar reportes");
        System.out.println("6) Ver estadisticas");
        System.out.println("7) Salir");
    }

    // compara como texto asi no se cae con letras; si se acaba la entrada elige la ultima opcion
    public static int leerOpcion(Scanner teclado, int cantidadOpciones)
    {
        System.out.print("Elija una opcion (1-" + cantidadOpciones + "): ");
        if (!teclado.hasNextLine())
        {
            System.out.println();
            System.out.println("[Se acabo la entrada: se elige la opcion " + cantidadOpciones + "]");
            return cantidadOpciones;
        }
        String texto = normalizarTexto(teclado.nextLine());
        for (int opcion = 1; opcion <= cantidadOpciones; opcion++)
        {
            if (texto.equals("" + opcion))
            {
                return opcion;
            }
        }
        System.out.println("Opcion no valida: escriba un numero entre 1 y " + cantidadOpciones + ".");
        return 0;
    }

    // pide un dato por consola y lo devuelve limpio; si se acabo la entrada devuelve vacio
    public static String leerTexto(Scanner teclado, String mensaje)
    {
        System.out.print(mensaje);
        if (!teclado.hasNextLine())
        {
            System.out.println();
            return "";
        }
        return normalizarTexto(teclado.nextLine());
    }

    // saca los espacios de mas porque el libro no usa trim
    public static String normalizarTexto(String texto)
    {
        String[] palabras = texto.split(" ");
        String resultado = "";
        for (int i = 0; i < palabras.length; i++)
        {
            if (!palabras[i].equals(""))
            {
                if (!resultado.equals(""))
                {
                    resultado = resultado + " ";
                }
                resultado = resultado + palabras[i];
            }
        }
        return resultado;
    }

    // cuenta hasta el primer null asi que ojo con dejar huecos
    public static int contarOcupados(String[] vector)
    {
        int cantidad = 0;
        while (cantidad < vector.length && vector[cantidad] != null)
        {
            cantidad++;
        }
        return cantidad;
    }

    // posicion del primer valor igual (sin importar mayusculas) o -1 si no esta
    public static int buscarPosicion(String[] vector, String valor)
    {
        int cantidad = contarOcupados(vector);
        for (int i = 0; i < cantidad; i++)
        {
            if (vector[i].equalsIgnoreCase(valor))
            {
                return i;
            }
        }
        return -1;
    }

    // posicion del alumno con ese nombre y apellido en la lista, o -1
    public static int buscarAlumnoPorNombre(String[] nombresAlumnos, String[] apellidosAlumnos, String nombre,
            String apellido)
    {
        int cantidad = contarOcupados(nombresAlumnos);
        for (int i = 0; i < cantidad; i++)
        {
            if (nombresAlumnos[i].equalsIgnoreCase(nombre) && apellidosAlumnos[i].equalsIgnoreCase(apellido))
            {
                return i;
            }
        }
        return -1;
    }

    // cuantas solicitudes tienen ese estado
    public static int contarEstado(String[] estadosSolicitudes, String estado)
    {
        int cantidad = 0;
        int ocupadas = contarOcupados(estadosSolicitudes);
        for (int i = 0; i < ocupadas; i++)
        {
            if (estadosSolicitudes[i].equals(estado))
            {
                cantidad++;
            }
        }
        return cantidad;
    }

    // cuantos miembros son de ese paralelo
    public static int contarEnParalelo(String[] paralelosMiembros, String paralelo)
    {
        int cantidad = 0;
        int ocupados = contarOcupados(paralelosMiembros);
        for (int i = 0; i < ocupados; i++)
        {
            if (paralelosMiembros[i].equals(paralelo))
            {
                cantidad++;
            }
        }
        return cantidad;
    }

    // cuantas filas ocupadas estan vacias (en rechazados: los que quedaron solo con RUT)
    public static int contarVacios(String[] vector)
    {
        int cantidad = 0;
        int ocupados = contarOcupados(vector);
        for (int i = 0; i < ocupados; i++)
        {
            if (vector[i].equals(""))
            {
                cantidad++;
            }
        }
        return cantidad;
    }

    // solo revisa la forma del rut no el modulo 11
    public static boolean esRutValido(String rut)
    {
        String[] partes = rut.split("-");
        if (partes.length != 2 || !(partes[0] + "-" + partes[1]).equals(rut))
        {
            return false;
        }
        int cuerpo;
        try
        {
            cuerpo = Integer.valueOf(partes[0]);
        }
        catch (NumberFormatException e)
        {
            return false;
        }
        boolean escritoLimpio = ("" + cuerpo).equals(partes[0]);
        boolean largoCorrecto = cuerpo >= 1000000 && cuerpo <= 99999999;
        return escritoLimpio && largoCorrecto && esDigitoVerificador(partes[1]);
    }

    // el digito verificador puede ser 0 a 9 o K
    public static boolean esDigitoVerificador(String digito)
    {
        String[] validos = { "0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "K" };
        for (int i = 0; i < validos.length; i++)
        {
            if (validos[i].equalsIgnoreCase(digito))
            {
                return true;
            }
        }
        return false;
    }

    // deja la K del rut en mayuscula para guardarlo siempre igual (el rut ya tiene que ser valido)
    public static String normalizarRut(String rut)
    {
        String[] partes = rut.split("-");
        String digito = partes[1];
        if (digito.equalsIgnoreCase("K"))
        {
            digito = "K";
        }
        return partes[0] + "-" + digito;
    }

    // devuelve C1 o C2 escrito bien, o vacio si el paralelo no existe
    public static String normalizarParalelo(String paralelo)
    {
        if (paralelo.equalsIgnoreCase("C1"))
        {
            return "C1";
        }
        if (paralelo.equalsIgnoreCase("C2"))
        {
            return "C2";
        }
        return "";
    }

    // true si el texto tiene el separador adentro (el espacio extra es para que split no ignore uno al final)
    public static boolean tieneSeparador(String texto, String separador)
    {
        return (texto + " ").split(separador).length > 1;
    }

    // revisa los datos de un alumno: devuelve vacio si estan bien o el motivo si no
    public static String validarDatosAlumno(String nombre, String apellido, String rut, String paralelo,
            String[] rutsAlumnos)
    {
        if (nombre.equals("") || apellido.equals("") || rut.equals("") || paralelo.equals(""))
        {
            return "hay datos en blanco (se necesitan nombre, apellido, RUT y paralelo)";
        }
        if (tieneSeparador(nombre, ";") || tieneSeparador(apellido, ";"))
        {
            return "el nombre y el apellido no pueden llevar ; (rompe el formato de Alumnos.txt)";
        }
        if (!esRutValido(rut))
        {
            return "el RUT " + rut + " no tiene un formato valido (ejemplo: 20345678-9, sin puntos)";
        }
        if (normalizarParalelo(paralelo).equals(""))
        {
            return "el paralelo " + paralelo + " no existe (solo C1 o C2)";
        }
        if (buscarPosicion(rutsAlumnos, rut) != -1)
        {
            return "el RUT " + rut + " ya esta en la lista";
        }
        return "";
    }

    // agrega un alumno al final de la lista; false si no queda espacio
    public static boolean agregarAlumno(String[] nombresAlumnos, String[] apellidosAlumnos, String[] rutsAlumnos,
            String[] paralelosAlumnos, String nombre, String apellido, String rut, String paralelo)
    {
        int posicion = contarOcupados(rutsAlumnos);
        if (posicion == rutsAlumnos.length)
        {
            return false;
        }
        nombresAlumnos[posicion] = nombre;
        apellidosAlumnos[posicion] = apellido;
        rutsAlumnos[posicion] = rut;
        paralelosAlumnos[posicion] = paralelo;
        return true;
    }

    // cada archivo se carga una sola vez; devuelve si la lista del curso quedo cargada
    public static boolean cargarArchivos(String[] nombresAlumnos, String[] apellidosAlumnos, String[] rutsAlumnos,
            String[] paralelosAlumnos, String[] nombresSolicitudes, String[] apellidosSolicitudes,
            String[] estadosSolicitudes, boolean alumnosCargados)
    {
        System.out.println();
        System.out.println("--- Carga de archivos ---");
        boolean listaCargada = alumnosCargados;
        if (listaCargada)
        {
            System.out.println("Alumnos.txt ya estaba cargado (" + contarOcupados(rutsAlumnos)
                    + " alumnos en la lista): no se vuelve a leer.");
        }
        else
        {
            listaCargada = cargarAlumnos("Alumnos.txt", nombresAlumnos, apellidosAlumnos, rutsAlumnos,
                    paralelosAlumnos);
        }
        int solicitudesCargadas = contarOcupados(nombresSolicitudes);
        if (solicitudesCargadas > 0)
        {
            System.out.println("Solicitudes.txt ya estaba cargado (" + solicitudesCargadas
                    + " solicitudes): no se vuelve a leer.");
        }
        else
        {
            cargarSolicitudes("Solicitudes.txt", nombresSolicitudes, apellidosSolicitudes, estadosSolicitudes);
        }
        return listaCargada;
    }

    // lee Alumnos.txt linea por linea; devuelve true si el archivo se pudo abrir
    public static boolean cargarAlumnos(String ruta, String[] nombresAlumnos, String[] apellidosAlumnos,
            String[] rutsAlumnos, String[] paralelosAlumnos)
    {
        Scanner lector;
        try
        {
            lector = new Scanner(new File(ruta));
        }
        catch (IOException e)
        {
            System.out.println("No se encontro " + ruta + " en la carpeta del proyecto: "
                    + "la lista del curso no se cargo.");
            return false;
        }
        int numeroLinea = 0;
        int sinEspacio = 0;
        while (lector.hasNextLine())
        {
            numeroLinea++;
            String linea = normalizarTexto(lector.nextLine());
            if (!linea.equals(""))
            {
                if (contarOcupados(rutsAlumnos) == rutsAlumnos.length)
                {
                    sinEspacio++;
                }
                else
                {
                    String motivo = agregarLineaAlumno(linea, nombresAlumnos, apellidosAlumnos, rutsAlumnos,
                            paralelosAlumnos);
                    avisarLineaOmitida(ruta, numeroLinea, motivo);
                }
            }
        }
        lector.close();
        avisarSinEspacio(sinEspacio, "alumnos", rutsAlumnos.length);
        System.out.println(ruta + ": " + contarOcupados(rutsAlumnos) + " alumnos en la lista.");
        return true;
    }

    // separa una linea nombre;apellido;rut;paralelo y la agrega; devuelve vacio o el motivo del rechazo
    public static String agregarLineaAlumno(String linea, String[] nombresAlumnos, String[] apellidosAlumnos,
            String[] rutsAlumnos, String[] paralelosAlumnos)
    {
        String[] campos = linea.split(";");
        if (campos.length != 4)
        {
            return "se esperaban 4 datos separados por ; (nombre;apellido;rut;paralelo)";
        }
        String nombre = normalizarTexto(campos[0]);
        String apellido = normalizarTexto(campos[1]);
        String rut = normalizarTexto(campos[2]);
        String paralelo = normalizarTexto(campos[3]);
        String motivo = validarDatosAlumno(nombre, apellido, rut, paralelo, rutsAlumnos);
        if (!motivo.equals(""))
        {
            return motivo;
        }
        if (!agregarAlumno(nombresAlumnos, apellidosAlumnos, rutsAlumnos, paralelosAlumnos, nombre, apellido,
                normalizarRut(rut), normalizarParalelo(paralelo)))
        {
            return "la lista esta llena";
        }
        return "";
    }

    // lee Solicitudes.txt; cada linea valida queda PENDIENTE
    public static void cargarSolicitudes(String ruta, String[] nombresSolicitudes, String[] apellidosSolicitudes,
            String[] estadosSolicitudes)
    {
        Scanner lector;
        try
        {
            lector = new Scanner(new File(ruta));
        }
        catch (IOException e)
        {
            System.out.println("No se encontro " + ruta + " en la carpeta del proyecto: "
                    + "no se cargaron solicitudes.");
            return;
        }
        int numeroLinea = 0;
        int sinEspacio = 0;
        while (lector.hasNextLine())
        {
            numeroLinea++;
            String linea = normalizarTexto(lector.nextLine());
            if (!linea.equals(""))
            {
                if (contarOcupados(nombresSolicitudes) == nombresSolicitudes.length)
                {
                    sinEspacio++;
                }
                else
                {
                    String motivo = agregarLineaSolicitud(linea, nombresSolicitudes, apellidosSolicitudes,
                            estadosSolicitudes);
                    avisarLineaOmitida(ruta, numeroLinea, motivo);
                }
            }
        }
        lector.close();
        avisarSinEspacio(sinEspacio, "solicitudes", nombresSolicitudes.length);
        System.out.println(ruta + ": " + contarOcupados(nombresSolicitudes) + " solicitudes de ingreso.");
    }

    // separa una linea nombre-apellido y la agrega como PENDIENTE; devuelve vacio o el motivo del rechazo
    public static String agregarLineaSolicitud(String linea, String[] nombresSolicitudes,
            String[] apellidosSolicitudes, String[] estadosSolicitudes)
    {
        String[] partes = linea.split("-");
        if (partes.length != 2)
        {
            return "se esperaba nombre-apellido";
        }
        String nombre = normalizarTexto(partes[0]);
        String apellido = normalizarTexto(partes[1]);
        if (nombre.equals("") || apellido.equals(""))
        {
            return "falta el nombre o el apellido";
        }
        int posicion = contarOcupados(nombresSolicitudes);
        if (posicion == nombresSolicitudes.length)
        {
            return "no hay espacio para mas solicitudes";
        }
        nombresSolicitudes[posicion] = nombre;
        apellidosSolicitudes[posicion] = apellido;
        estadosSolicitudes[posicion] = "PENDIENTE";
        return "";
    }

    // avisa que una linea del archivo no se cargo y por que
    public static void avisarLineaOmitida(String ruta, int numeroLinea, String motivo)
    {
        if (!motivo.equals(""))
        {
            System.out.println("  Aviso: linea " + numeroLinea + " de " + ruta + " omitida: " + motivo + ".");
        }
    }

    // avisa cuantas lineas no cupieron en los vectores
    public static void avisarSinEspacio(int cantidad, String datos, int capacidad)
    {
        if (cantidad > 0)
        {
            System.out.println("  Aviso: no hay espacio para " + cantidad + " " + datos + " mas (el maximo es "
                    + capacidad + "); esas lineas no se cargaron.");
        }
    }

    // opcion 2: revisa las solicitudes pendientes contra la lista del curso
    public static void procesarSolicitudes(String[] nombresAlumnos, String[] apellidosAlumnos,
            String[] rutsAlumnos, String[] paralelosAlumnos, String[] nombresSolicitudes,
            String[] apellidosSolicitudes, String[] estadosSolicitudes, String[] nombresMiembros,
            String[] apellidosMiembros, String[] rutsMiembros, String[] paralelosMiembros,
            String[] nombresRechazados, String[] rutsRechazados, int[] intentosRechazados)
    {
        System.out.println();
        System.out.println("--- Filtrado de solicitudes ---");
        int cantidadSolicitudes = contarOcupados(nombresSolicitudes);
        if (contarOcupados(rutsAlumnos) == 0)
        {
            System.out.println("La lista del curso esta vacia o no se ha cargado: use primero la opcion 1.");
            return;
        }
        if (cantidadSolicitudes == 0)
        {
            System.out.println("No hay solicitudes cargadas: use primero la opcion 1.");
            return;
        }
        if (contarEstado(estadosSolicitudes, "PENDIENTE") == 0)
        {
            System.out.println("Las " + cantidadSolicitudes + " solicitudes ya se procesaron: no hay nada nuevo.");
            return;
        }
        for (int i = 0; i < cantidadSolicitudes; i++)
        {
            if (estadosSolicitudes[i].equals("PENDIENTE"))
            {
                estadosSolicitudes[i] = evaluarSolicitud(nombresSolicitudes[i], apellidosSolicitudes[i],
                        nombresAlumnos, apellidosAlumnos, rutsAlumnos, paralelosAlumnos, nombresMiembros,
                        apellidosMiembros, rutsMiembros, paralelosMiembros, nombresRechazados, rutsRechazados,
                        intentosRechazados);
            }
        }
        mostrarResumenFiltrado(estadosSolicitudes, nombresRechazados);
    }

    // admitida repetida o rechazada
    public static String evaluarSolicitud(String nombre, String apellido, String[] nombresAlumnos,
            String[] apellidosAlumnos, String[] rutsAlumnos, String[] paralelosAlumnos, String[] nombresMiembros,
            String[] apellidosMiembros, String[] rutsMiembros, String[] paralelosMiembros,
            String[] nombresRechazados, String[] rutsRechazados, int[] intentosRechazados)
    {
        String persona = nombre + " " + apellido;
        int posicion = buscarAlumnoPorNombre(nombresAlumnos, apellidosAlumnos, nombre, apellido);
        if (posicion == -1)
        {
            boolean registrado = registrarRechazoPorNombre(nombresRechazados, rutsRechazados, intentosRechazados,
                    nombre, apellido);
            if (!registrado)
            {
                System.out.println("[SIN ESPACIO] " + persona + ": el registro de rechazados esta lleno.");
                return "PENDIENTE";
            }
            System.out.println("[RECHAZADA] " + persona + " no aparece en la lista de ningun paralelo.");
            return "RECHAZADA";
        }
        if (buscarPosicion(rutsMiembros, rutsAlumnos[posicion]) != -1)
        {
            System.out.println("[REPETIDA]  " + persona + " ya es miembro del grupo: no se admite otra vez.");
            return "REPETIDA";
        }
        if (!agregarMiembro(nombresAlumnos, apellidosAlumnos, rutsAlumnos, paralelosAlumnos, posicion,
                nombresMiembros, apellidosMiembros, rutsMiembros, paralelosMiembros))
        {
            System.out.println("[SIN ESPACIO] " + persona + ": el grupo esta lleno.");
            return "PENDIENTE";
        }
        System.out.println("[ADMITIDA]  " + nombresAlumnos[posicion] + " " + apellidosAlumnos[posicion] + " ("
                + rutsAlumnos[posicion] + ") entra al grupo del paralelo " + paralelosAlumnos[posicion] + ".");
        return "ADMITIDA";
    }

    // copia nombre, apellido, rut y paralelo de esa fila de la lista al final del grupo; false si esta lleno
    public static boolean agregarMiembro(String[] nombresAlumnos, String[] apellidosAlumnos, String[] rutsAlumnos,
            String[] paralelosAlumnos, int posicionAlumno, String[] nombresMiembros, String[] apellidosMiembros,
            String[] rutsMiembros, String[] paralelosMiembros)
    {
        int posicion = contarOcupados(rutsMiembros);
        if (posicion == rutsMiembros.length)
        {
            return false;
        }
        nombresMiembros[posicion] = nombresAlumnos[posicionAlumno];
        apellidosMiembros[posicion] = apellidosAlumnos[posicionAlumno];
        rutsMiembros[posicion] = rutsAlumnos[posicionAlumno];
        paralelosMiembros[posicion] = paralelosAlumnos[posicionAlumno];
        return true;
    }

    // anota un rechazo por nombre: si la persona ya estaba le suma un intento; false si no hay espacio
    public static boolean registrarRechazoPorNombre(String[] nombresRechazados, String[] rutsRechazados,
            int[] intentosRechazados, String nombre, String apellido)
    {
        String nombreCompleto = nombre + " " + apellido;
        int posicion = buscarPosicion(nombresRechazados, nombreCompleto);
        if (posicion != -1)
        {
            intentosRechazados[posicion]++;
            return true;
        }
        posicion = contarOcupados(nombresRechazados);
        if (posicion == nombresRechazados.length)
        {
            return false;
        }
        nombresRechazados[posicion] = nombreCompleto;
        rutsRechazados[posicion] = "";
        intentosRechazados[posicion] = 1;
        return true;
    }

    // anota un rechazo solo con RUT (el nombre queda vacio); false si no hay espacio
    public static boolean registrarRechazoPorRut(String[] nombresRechazados, String[] rutsRechazados,
            int[] intentosRechazados, String rut)
    {
        int posicion = buscarPosicion(rutsRechazados, rut);
        if (posicion != -1)
        {
            intentosRechazados[posicion]++;
            return true;
        }
        posicion = contarOcupados(nombresRechazados);
        if (posicion == nombresRechazados.length)
        {
            return false;
        }
        nombresRechazados[posicion] = "";
        rutsRechazados[posicion] = rut;
        intentosRechazados[posicion] = 1;
        return true;
    }

    // resumen de lo que paso en el filtrado
    public static void mostrarResumenFiltrado(String[] estadosSolicitudes, String[] nombresRechazados)
    {
        System.out.println();
        System.out.println("Resumen: " + contarEstado(estadosSolicitudes, "ADMITIDA") + " admitidas, "
                + contarEstado(estadosSolicitudes, "REPETIDA") + " repetidas (ya eran miembros) y "
                + contarEstado(estadosSolicitudes, "RECHAZADA") + " rechazadas.");
        System.out.println("Personas distintas en el registro de rechazados: " + contarOcupados(nombresRechazados)
                + ".");
        int pendientes = contarEstado(estadosSolicitudes, "PENDIENTE");
        if (pendientes > 0)
        {
            System.out.println("Quedaron " + pendientes + " solicitudes sin procesar por falta de espacio.");
        }
    }

    // opcion 3: submenu de inscripcion manual; devuelve cuantos intentos validos se hicieron
    public static int inscribirManualmente(Scanner teclado, String[] nombresAlumnos, String[] apellidosAlumnos,
            String[] rutsAlumnos, String[] paralelosAlumnos, String[] nombresMiembros, String[] apellidosMiembros,
            String[] rutsMiembros, String[] paralelosMiembros, String[] nombresRechazados, String[] rutsRechazados,
            int[] intentosRechazados, boolean alumnosCargados)
    {
        System.out.println();
        System.out.println("--- Inscripcion manual ---");
        if (!alumnosCargados)
        {
            System.out.println("Primero cargue la lista del curso (opcion 1): sin ella no se puede revisar a nadie.");
            return 0;
        }
        int intentos = 0;
        int opcion;
        do
        {
            System.out.println();
            System.out.println("1) Inscribir por nombre completo");
            System.out.println("2) Inscribir por RUT");
            System.out.println("3) Volver al menu principal");
            opcion = leerOpcion(teclado, 3);
            if (opcion == 1)
            {
                intentos = intentos + inscribirPorNombre(teclado, nombresAlumnos, apellidosAlumnos, rutsAlumnos,
                        paralelosAlumnos, nombresMiembros, apellidosMiembros, rutsMiembros, paralelosMiembros,
                        nombresRechazados, rutsRechazados, intentosRechazados);
            }
            else if (opcion == 2)
            {
                intentos = intentos + inscribirPorRut(teclado, nombresAlumnos, apellidosAlumnos, rutsAlumnos,
                        paralelosAlumnos, nombresMiembros, apellidosMiembros, rutsMiembros, paralelosMiembros,
                        nombresRechazados, rutsRechazados, intentosRechazados);
            }
        }
        while (opcion != 3);
        return intentos;
    }

    // busca por nombre y apellido: si esta en la lista entra, si no queda rechazado con su nombre
    public static int inscribirPorNombre(Scanner teclado, String[] nombresAlumnos, String[] apellidosAlumnos,
            String[] rutsAlumnos, String[] paralelosAlumnos, String[] nombresMiembros, String[] apellidosMiembros,
            String[] rutsMiembros, String[] paralelosMiembros, String[] nombresRechazados, String[] rutsRechazados,
            int[] intentosRechazados)
    {
        String nombre = leerTexto(teclado, "Nombre: ");
        String apellido = leerTexto(teclado, "Apellido: ");
        if (nombre.equals("") || apellido.equals(""))
        {
            System.out.println("Faltan el nombre o el apellido: no se registro el intento.");
            return 0;
        }
        int posicion = buscarAlumnoPorNombre(nombresAlumnos, apellidosAlumnos, nombre, apellido);
        if (posicion == -1)
        {
            if (!registrarRechazoPorNombre(nombresRechazados, rutsRechazados, intentosRechazados, nombre, apellido))
            {
                System.out.println("El registro de rechazados esta lleno: no se pudo anotar el intento.");
                return 0;
            }
            System.out.println("[RECHAZADA] " + nombre + " " + apellido
                    + " no aparece en la lista de ningun paralelo: queda registrado su nombre.");
            return 1;
        }
        return admitirManualmente(posicion, nombresAlumnos, apellidosAlumnos, rutsAlumnos, paralelosAlumnos,
                nombresMiembros, apellidosMiembros, rutsMiembros, paralelosMiembros);
    }

    // busca por RUT: si esta en la lista entra, si no queda rechazado solo con el RUT
    public static int inscribirPorRut(Scanner teclado, String[] nombresAlumnos, String[] apellidosAlumnos,
            String[] rutsAlumnos, String[] paralelosAlumnos, String[] nombresMiembros, String[] apellidosMiembros,
            String[] rutsMiembros, String[] paralelosMiembros, String[] nombresRechazados, String[] rutsRechazados,
            int[] intentosRechazados)
    {
        String rut = leerTexto(teclado, "RUT (sin puntos, ej. 20345678-9): ");
        if (!esRutValido(rut))
        {
            System.out.println("El RUT '" + rut + "' no tiene un formato valido: no se registro el intento.");
            return 0;
        }
        rut = normalizarRut(rut);
        int posicion = buscarPosicion(rutsAlumnos, rut);
        if (posicion == -1)
        {
            if (!registrarRechazoPorRut(nombresRechazados, rutsRechazados, intentosRechazados, rut))
            {
                System.out.println("El registro de rechazados esta lleno: no se pudo anotar el intento.");
                return 0;
            }
            System.out.println("[RECHAZADA] El RUT " + rut
                    + " no esta en ningun paralelo: se registra solo el RUT, sin nombre.");
            return 1;
        }
        return admitirManualmente(posicion, nombresAlumnos, apellidosAlumnos, rutsAlumnos, paralelosAlumnos,
                nombresMiembros, apellidosMiembros, rutsMiembros, paralelosMiembros);
    }

    // hace entrar al alumno de esa posicion de la lista, salvo que ya sea miembro; devuelve 1 si cuenta como intento
    public static int admitirManualmente(int posicion, String[] nombresAlumnos, String[] apellidosAlumnos,
            String[] rutsAlumnos, String[] paralelosAlumnos, String[] nombresMiembros, String[] apellidosMiembros,
            String[] rutsMiembros, String[] paralelosMiembros)
    {
        String persona = nombresAlumnos[posicion] + " " + apellidosAlumnos[posicion];
        if (buscarPosicion(rutsMiembros, rutsAlumnos[posicion]) != -1)
        {
            System.out.println("[REPETIDA]  " + persona + " ya es miembro del grupo: no se admite otra vez.");
            return 1;
        }
        if (!agregarMiembro(nombresAlumnos, apellidosAlumnos, rutsAlumnos, paralelosAlumnos, posicion,
                nombresMiembros, apellidosMiembros, rutsMiembros, paralelosMiembros))
        {
            System.out.println("[SIN ESPACIO] " + persona + ": el grupo esta lleno.");
            return 0;
        }
        System.out.println("[ADMITIDA]  " + persona + " (" + rutsAlumnos[posicion] + ") entra al grupo del paralelo "
                + paralelosAlumnos[posicion] + ".");
        return 1;
    }

    // opcion 4: submenu para cambiar paralelo, eliminar o inscribir alumnos; cada cambio se guarda en Alumnos.txt
    public static void administrarCurso(Scanner teclado, String[] nombresAlumnos, String[] apellidosAlumnos,
            String[] rutsAlumnos, String[] paralelosAlumnos, String[] nombresMiembros, String[] apellidosMiembros,
            String[] rutsMiembros, String[] paralelosMiembros, boolean alumnosCargados)
    {
        System.out.println();
        System.out.println("--- Administracion del curso ---");
        if (!alumnosCargados)
        {
            System.out.println("Primero cargue la lista del curso (opcion 1), asi no se sobrescribe Alumnos.txt "
                    + "con una lista vacia.");
            return;
        }
        int opcion;
        do
        {
            System.out.println();
            System.out.println("1) Cambiar de paralelo a un alumno (C1 o C2)");
            System.out.println("2) Eliminar a un alumno de la lista");
            System.out.println("3) Inscribir a un alumno nuevo en la lista");
            System.out.println("4) Volver al menu principal");
            opcion = leerOpcion(teclado, 4);
            if (opcion == 1)
            {
                cambiarParalelo(teclado, nombresAlumnos, apellidosAlumnos, rutsAlumnos, paralelosAlumnos,
                        rutsMiembros, paralelosMiembros);
            }
            else if (opcion == 2)
            {
                eliminarAlumno(teclado, nombresAlumnos, apellidosAlumnos, rutsAlumnos, paralelosAlumnos,
                        nombresMiembros, apellidosMiembros, rutsMiembros, paralelosMiembros);
            }
            else if (opcion == 3)
            {
                inscribirAlumnoNuevo(teclado, nombresAlumnos, apellidosAlumnos, rutsAlumnos, paralelosAlumnos);
            }
        }
        while (opcion != 4);
    }

    // pide un RUT y devuelve la posicion del alumno en la lista, o -1 si no sirve o no esta
    public static int pedirAlumnoPorRut(Scanner teclado, String[] rutsAlumnos)
    {
        String rut = leerTexto(teclado, "RUT del alumno (sin puntos, ej. 20345678-9): ");
        if (!esRutValido(rut))
        {
            System.out.println("El RUT '" + rut + "' no tiene un formato valido.");
            return -1;
        }
        int posicion = buscarPosicion(rutsAlumnos, rut);
        if (posicion == -1)
        {
            System.out.println("No hay ningun alumno con RUT " + normalizarRut(rut) + " en la lista.");
        }
        return posicion;
    }

    // pasa al alumno al otro paralelo y, si era miembro, tambien lo reclasifica en el grupo
    public static void cambiarParalelo(Scanner teclado, String[] nombresAlumnos, String[] apellidosAlumnos,
            String[] rutsAlumnos, String[] paralelosAlumnos, String[] rutsMiembros, String[] paralelosMiembros)
    {
        int posicion = pedirAlumnoPorRut(teclado, rutsAlumnos);
        if (posicion == -1)
        {
            return;
        }
        String anterior = paralelosAlumnos[posicion];
        String nuevo = "C1";
        if (anterior.equals("C1"))
        {
            nuevo = "C2";
        }
        paralelosAlumnos[posicion] = nuevo;
        System.out.println(nombresAlumnos[posicion] + " " + apellidosAlumnos[posicion] + " pasa del paralelo "
                + anterior + " al " + nuevo + ".");
        int posicionMiembro = buscarPosicion(rutsMiembros, rutsAlumnos[posicion]);
        if (posicionMiembro != -1)
        {
            paralelosMiembros[posicionMiembro] = nuevo;
            System.out.println("Como ya era miembro del grupo, queda reclasificado en el paralelo " + nuevo + ".");
        }
        guardarAlumnos(nombresAlumnos, apellidosAlumnos, rutsAlumnos, paralelosAlumnos);
    }

    // saca al alumno de la lista y, si era miembro, tambien del grupo (pierde el acceso)
    public static void eliminarAlumno(Scanner teclado, String[] nombresAlumnos, String[] apellidosAlumnos,
            String[] rutsAlumnos, String[] paralelosAlumnos, String[] nombresMiembros, String[] apellidosMiembros,
            String[] rutsMiembros, String[] paralelosMiembros)
    {
        int posicion = pedirAlumnoPorRut(teclado, rutsAlumnos);
        if (posicion == -1)
        {
            return;
        }
        String persona = nombresAlumnos[posicion] + " " + apellidosAlumnos[posicion] + " (" + rutsAlumnos[posicion]
                + ")";
        int posicionMiembro = buscarPosicion(rutsMiembros, rutsAlumnos[posicion]);
        eliminarPosicion(nombresAlumnos, posicion);
        eliminarPosicion(apellidosAlumnos, posicion);
        eliminarPosicion(rutsAlumnos, posicion);
        eliminarPosicion(paralelosAlumnos, posicion);
        System.out.println(persona + " ya no esta en la lista del curso.");
        if (posicionMiembro != -1)
        {
            eliminarPosicion(nombresMiembros, posicionMiembro);
            eliminarPosicion(apellidosMiembros, posicionMiembro);
            eliminarPosicion(rutsMiembros, posicionMiembro);
            eliminarPosicion(paralelosMiembros, posicionMiembro);
            System.out.println("Tambien sale del grupo: pierde el acceso.");
        }
        guardarAlumnos(nombresAlumnos, apellidosAlumnos, rutsAlumnos, paralelosAlumnos);
    }

    // agrega un alumno nuevo a la lista (no lo mete al grupo) y guarda el archivo
    public static void inscribirAlumnoNuevo(Scanner teclado, String[] nombresAlumnos, String[] apellidosAlumnos,
            String[] rutsAlumnos, String[] paralelosAlumnos)
    {
        if (contarOcupados(rutsAlumnos) == rutsAlumnos.length)
        {
            System.out.println("La lista esta llena (maximo " + rutsAlumnos.length
                    + " alumnos): no hay espacio para otro.");
            return;
        }
        String nombre = leerTexto(teclado, "Nombre: ");
        String apellido = leerTexto(teclado, "Apellido: ");
        String rut = leerTexto(teclado, "RUT (sin puntos, ej. 20345678-9): ");
        String paralelo = leerTexto(teclado, "Paralelo (C1 o C2): ");
        String motivo = validarDatosAlumno(nombre, apellido, rut, paralelo, rutsAlumnos);
        if (!motivo.equals(""))
        {
            System.out.println("No se inscribio: " + motivo + ".");
            return;
        }
        if (!agregarAlumno(nombresAlumnos, apellidosAlumnos, rutsAlumnos, paralelosAlumnos, nombre, apellido,
                normalizarRut(rut), normalizarParalelo(paralelo)))
        {
            System.out.println("La lista esta llena: no se inscribio.");
            return;
        }
        System.out.println(nombre + " " + apellido + " queda inscrito en el paralelo " + normalizarParalelo(paralelo)
                + ". Todavia no es miembro del grupo: tiene que procesarse o inscribirse manualmente.");
        guardarAlumnos(nombresAlumnos, apellidosAlumnos, rutsAlumnos, paralelosAlumnos);
    }

    // corre una posicion hacia atras los que estan despues (libro 1.15.8) y deja libre la ultima
    public static void eliminarPosicion(String[] vector, int posicion)
    {
        int cantidad = contarOcupados(vector);
        if (posicion < 0 || posicion >= cantidad)
        {
            return;
        }
        for (int i = posicion; i < cantidad - 1; i++)
        {
            vector[i] = vector[i + 1];
        }
        vector[cantidad - 1] = null;
    }

    // reescribe Alumnos.txt con el formato original nombre;apellido;rut;paralelo
    public static void guardarAlumnos(String[] nombresAlumnos, String[] apellidosAlumnos, String[] rutsAlumnos,
            String[] paralelosAlumnos)
    {
        int cantidad = contarOcupados(rutsAlumnos);
        try
        {
            BufferedWriter escritor = new BufferedWriter(new FileWriter("Alumnos.txt"));
            for (int i = 0; i < cantidad; i++)
            {
                if (i > 0)
                {
                    escritor.newLine();
                }
                escritor.write(nombresAlumnos[i] + ";" + apellidosAlumnos[i] + ";" + rutsAlumnos[i] + ";"
                        + paralelosAlumnos[i]);
            }
            escritor.close();
            System.out.println("Cambios guardados en Alumnos.txt (" + cantidad + " alumnos).");
        }
        catch (IOException e)
        {
            System.out.println("No se pudo guardar Alumnos.txt: el cambio quedo solo en memoria.");
        }
    }

    // opcion 5: submenu de reportes; cada uno se escribe como version nueva en la carpeta Reportes
    public static void generarReportes(Scanner teclado, String[] nombresMiembros, String[] apellidosMiembros,
            String[] rutsMiembros, String[] paralelosMiembros, String[] nombresRechazados, String[] rutsRechazados,
            boolean alumnosCargados)
    {
        System.out.println();
        System.out.println("--- Reportes ---");
        if (!alumnosCargados)
        {
            System.out.println("Primero cargue los archivos (opcion 1) y filtre las solicitudes (opcion 2).");
            return;
        }
        int opcion;
        do
        {
            System.out.println();
            System.out.println("1) Miembros del paralelo C1");
            System.out.println("2) Miembros del paralelo C2");
            System.out.println("3) Solicitudes rechazadas");
            System.out.println("4) Volver al menu principal");
            opcion = leerOpcion(teclado, 4);
            if (opcion == 1)
            {
                escribirReporteParalelo("C1", nombresMiembros, apellidosMiembros, rutsMiembros, paralelosMiembros);
            }
            else if (opcion == 2)
            {
                escribirReporteParalelo("C2", nombresMiembros, apellidosMiembros, rutsMiembros, paralelosMiembros);
            }
            else if (opcion == 3)
            {
                escribirReporteRechazados(nombresRechazados, rutsRechazados);
            }
        }
        while (opcion != 4);
    }

    // escribe ReporteC1-VX.txt o ReporteC2-VX.txt con los miembros actuales de ese paralelo
    public static void escribirReporteParalelo(String paralelo, String[] nombresMiembros, String[] apellidosMiembros,
            String[] rutsMiembros, String[] paralelosMiembros)
    {
        prepararCarpetaReportes();
        String tipo = "Reporte" + paralelo;
        String ruta = "Reportes/" + tipo + "-V" + siguienteVersion(tipo) + ".txt";
        int cantidad = contarOcupados(rutsMiembros);
        int escritos = 0;
        try
        {
            BufferedWriter escritor = new BufferedWriter(new FileWriter(ruta));
            escritor.write("=== Miembros del grupo - Paralelo " + paralelo + " ===");
            escritor.newLine();
            for (int i = 0; i < cantidad; i++)
            {
                if (paralelosMiembros[i].equals(paralelo))
                {
                    escritor.write(nombresMiembros[i] + " " + apellidosMiembros[i] + " - " + rutsMiembros[i]);
                    escritor.newLine();
                    escritos++;
                }
            }
            escritor.close();
            System.out.println("Se creo " + ruta + " con " + escritos + " miembros.");
        }
        catch (IOException e)
        {
            System.out.println("No se pudo escribir " + ruta + ".");
        }
    }

    // escribe Rechazados-VX.txt con cada persona rechazada una sola vez
    public static void escribirReporteRechazados(String[] nombresRechazados, String[] rutsRechazados)
    {
        prepararCarpetaReportes();
        String ruta = "Reportes/Rechazados-V" + siguienteVersion("Rechazados") + ".txt";
        int cantidad = contarOcupados(nombresRechazados);
        try
        {
            BufferedWriter escritor = new BufferedWriter(new FileWriter(ruta));
            escritor.write("=== Solicitudes rechazadas ===");
            escritor.newLine();
            for (int i = 0; i < cantidad; i++)
            {
                if (nombresRechazados[i].equals(""))
                {
                    escritor.write("Sin nombre registrado, RUT: " + rutsRechazados[i]);
                }
                else
                {
                    escritor.write(nombresRechazados[i] + " - No pertenece a ningun paralelo del curso");
                }
                escritor.newLine();
            }
            escritor.close();
            System.out.println("Se creo " + ruta + " con " + cantidad + " personas rechazadas.");
        }
        catch (IOException e)
        {
            System.out.println("No se pudo escribir " + ruta + ".");
        }
    }

    // crea la carpeta Reportes si no existe (si ya existe no hace nada)
    public static void prepararCarpetaReportes()
    {
        new File("Reportes").mkdir();
    }

    // true si el archivo existe: se prueba abriendolo con Scanner, como en el libro
    public static boolean existeArchivo(String ruta)
    {
        try
        {
            Scanner lector = new Scanner(new File(ruta));
            lector.close();
            return true;
        }
        catch (IOException e)
        {
            return false;
        }
    }

    // primera version que todavia no existe en Reportes, asi nunca se borra un reporte anterior
    public static int siguienteVersion(String tipoReporte)
    {
        int version = 1;
        while (existeArchivo("Reportes/" + tipoReporte + "-V" + version + ".txt"))
        {
            version++;
        }
        return version;
    }

    // opcion 6: porcentaje de rechazo y metricas del grupo
    public static void mostrarEstadisticas(String[] estadosSolicitudes, String[] paralelosMiembros,
            String[] nombresRechazados, int[] intentosRechazados, int intentosManuales)
    {
        System.out.println();
        System.out.println("--- Estadisticas del ingreso al grupo ---");
        int cargadas = contarOcupados(estadosSolicitudes);
        int solicitudesProcesadas = cargadas - contarEstado(estadosSolicitudes, "PENDIENTE");
        int totalIntentos = solicitudesProcesadas + intentosManuales;
        if (totalIntentos == 0)
        {
            System.out.println("Todavia no hay intentos de ingreso: filtre las solicitudes (opcion 2)");
            System.out.println("o inscriba a alguien manualmente (opcion 3).");
            return;
        }
        int intentosFallidos = sumarIntentos(intentosRechazados);
        System.out.println("Intentos de ingreso: " + totalIntentos + " (" + solicitudesProcesadas
                + " solicitudes del archivo y " + intentosManuales + " inscripciones manuales).");
        System.out.println("Porcentaje de rechazo: " + calcularPorcentaje(intentosFallidos, totalIntentos) + "% ("
                + intentosFallidos + " de " + totalIntentos + " intentos fueron rechazados).");
        System.out.println("Personas distintas en el registro de rechazados: " + contarOcupados(nombresRechazados)
                + ".");

        int miembros = contarOcupados(paralelosMiembros);
        int miembrosC1 = contarEnParalelo(paralelosMiembros, "C1");
        int miembrosC2 = contarEnParalelo(paralelosMiembros, "C2");
        System.out.println("Miembros actuales del grupo: " + miembros + " (C1: " + miembrosC1 + ", "
                + calcularPorcentaje(miembrosC1, miembros) + "% | C2: " + miembrosC2 + ", "
                + calcularPorcentaje(miembrosC2, miembros) + "%).");
        System.out.println("Solicitudes repetidas de personas que ya eran miembros: "
                + contarEstado(estadosSolicitudes, "REPETIDA") + ".");
        System.out.println("Rechazados que quedaron registrados solo con RUT: " + contarVacios(nombresRechazados)
                + ".");
    }

    // suma los intentos fallidos de todos los rechazados
    public static int sumarIntentos(int[] intentosRechazados)
    {
        int suma = 0;
        for (int i = 0; i < intentosRechazados.length; i++)
        {
            suma += intentosRechazados[i];
        }
        return suma;
    }

    // porcentaje con un decimal sin printf
    public static double calcularPorcentaje(int parte, int total)
    {
        if (total == 0)
        {
            return 0;
        }
        return (int) (parte * 1000.0 / total + 0.5) / 10.0;
    }
}
