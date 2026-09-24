/* Vicente Guerra - 21.855.415-6 - ITI
 * Diego Meneses - 22.087.010-3 - ICCI
 */

package taller01poo;

import java.io.File;
import java.io.IOException;
import java.util.Scanner;

public class Main {

	public static void main(String[] args) {
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
	      // TO DO la opcion 3 tiene que sumar aca
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
	              // TO DO inscripcion manual
	              avisarPendiente("Inscribir manualmente", "Parte 2");
	          }
	          else if (opcion == 4)
	          {
	              // TO DO administrar curso
	              avisarPendiente("Administrar la lista del curso", "Parte 2");
	          }
	          else if (opcion == 5)
	          {
	              // TO DO reportes
	              avisarPendiente("Generar reportes", "Parte 3");
	          }
	          else if (opcion == 6)
	          {
	              mostrarEstadisticas(estadosSolicitudes, nombresRechazados, intentosRechazados, intentosManuales);
	          }
	      }
	      while (opcion != opcionSalir);

	      System.out.println("Hasta pronto.");
	      teclado.close();
	  }

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

	  // compara como texto asi no se cae con letras
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

	  // pa pedir datos en la parte 2
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

	  public static void avisarPendiente(String opcion, String parte)
	  {
	      System.out.println("[Pendiente] " + opcion + ": se implementa en la " + parte + " del taller.");
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

	  public static String validarDatosAlumno(String nombre, String apellido, String rut, String paralelo,
	          String[] rutsAlumnos)
	  {
	      if (nombre.equals("") || apellido.equals("") || rut.equals("") || paralelo.equals(""))
	      {
	          return "hay datos en blanco (se necesitan nombre, apellido, RUT y paralelo)";
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

	  // cada archivo se carga una sola vez
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
	      if (!agregarAlumno(nombresAlumnos, apellidosAlumnos, rutsAlumnos, paralelosAlumnos, nombre, apellido, rut,
	              normalizarParalelo(paralelo)))
	      {
	          return "la lista esta llena";
	      }
	      return "";
	  }

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

	  public static void avisarLineaOmitida(String ruta, int numeroLinea, String motivo)
	  {
	      if (!motivo.equals(""))
	      {
	          System.out.println("  Aviso: linea " + numeroLinea + " de " + ruta + " omitida: " + motivo + ".");
	      }
	  }

	  public static void avisarSinEspacio(int cantidad, String datos, int capacidad)
	  {
	      if (cantidad > 0)
	      {
	          System.out.println("  Aviso: no hay espacio para " + cantidad + " " + datos + " mas (el maximo es "
	                  + capacidad + "); esas lineas no se cargaron.");
	      }
	  }

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

	  public static void mostrarEstadisticas(String[] estadosSolicitudes, String[] nombresRechazados,
	          int[] intentosRechazados, int intentosManuales)
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
	      // TO DO metricas extra
	      avisarPendiente("Metricas adicionales", "Parte 3");
	  }

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