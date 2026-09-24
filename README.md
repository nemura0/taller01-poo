# Taller 01 - Sistema de Control del Grupo POO

## Descripción

Este proyecto corresponde al Taller 01 de Programación Orientada a Objetos del segundo semestre de 2026.

El programa permite controlar el ingreso de alumnos a un grupo de POO utilizando los archivos `Alumnos.txt` y `Solicitudes.txt`. A partir de estos datos se revisa quién pertenece al curso, quién puede ingresar al grupo y quién debe quedar registrado como rechazado.

También se agregaron opciones para inscribir alumnos manualmente, modificar la lista del curso, generar reportes y mostrar algunas estadísticas.

## Funciones principales

Desde el menú se pueden cargar los archivos, procesar las solicitudes, hacer una inscripción manual por nombre o RUT, administrar alumnos del curso, generar reportes y revisar estadísticas.

En la administración se puede cambiar a un alumno de paralelo, eliminarlo o agregar uno nuevo. Los cambios realizados quedan guardados en `Alumnos.txt`.

El programa también evita registros duplicados y controla errores básicos para que no se cierre cuando se ingresa una opción incorrecta o falta algún dato.

## Estructura del proyecto

```text
taller01-poo-main/
├── Alumnos.txt
├── Solicitudes.txt
├── README.md
├── src/
│   ├── module-info.java
│   └── taller01poo/
│       └── Main.java
└── Reportes/
```

La carpeta `Reportes` se crea cuando se genera el primer reporte.

## Archivos utilizados

`Alumnos.txt` guarda los datos de los alumnos inscritos con el siguiente formato:

```text
nombre;apellido;rut;paralelo
```

Ejemplo:

```text
Martin;Droguett;20345678-9;C1
```

`Solicitudes.txt` contiene las personas que intentaron ingresar al grupo:

```text
nombre-apellido
```

Ejemplo:

```text
Martin-Droguett
```

Ambos archivos deben estar en la carpeta principal del proyecto.

## Clonar el repositorio

```bash
git clone <URL_DEL_REPOSITORIO>
cd taller01-poo-main
```

Se debe cambiar `<URL_DEL_REPOSITORIO>` por el enlace del repositorio de GitHub.

## Ejecución

El proyecto fue trabajado con Java 21 y puede ejecutarse desde Eclipse abriendo `Main.java` y seleccionando `Run As > Java Application`.

También se puede ejecutar desde terminal estando ubicado en la carpeta principal:

```bash
javac -d bin src/module-info.java src/taller01poo/Main.java
java -p bin -m taller01poo/taller01poo.Main
```

Al ejecutarlo aparece el menú principal:

```text
====== Grupo POO: control de ingreso ======
1) Cargar Alumnos.txt y Solicitudes.txt
2) Filtrar solicitudes automaticamente
3) Inscribir manualmente a una persona
4) Administrar la lista del curso
5) Generar reportes
6) Ver estadisticas
7) Salir
```

## Reportes

Los reportes se guardan dentro de la carpeta `Reportes` y se crean con versiones para no reemplazar los anteriores.

```text
ReporteC1-V1.txt
ReporteC1-V2.txt
ReporteC2-V1.txt
Rechazados-V1.txt
```

Los reportes de C1 y C2 muestran a los integrantes admitidos de cada paralelo. El reporte de rechazados guarda a las personas que intentaron entrar sin pertenecer al curso y también los casos en que solo se ingresó un RUT.

## Pruebas

Para probar el programa se recomienda primero cargar los archivos y luego procesar las solicitudes. Después se pueden probar las inscripciones manuales, realizar algún cambio en la lista de alumnos y revisar que este quede guardado en `Alumnos.txt`.

Finalmente se pueden generar los reportes, revisar que aumente su número de versión y comprobar las estadísticas. También se pueden ingresar opciones incorrectas para revisar que el programa siga funcionando normalmente.

## Consideraciones

El taller fue realizado usando vectores estáticos paralelos, sin `ArrayList`, `LinkedList`, `HashMap` ni otras colecciones dinámicas.

Se considera un máximo de 100 registros y se validan datos como campos vacíos, RUT repetidos, paralelos distintos de `C1` o `C2`, archivos inexistentes y opciones incorrectas del menú.

Las comparaciones por nombre y apellido no distinguen entre mayúsculas y minúsculas. Si se modifica o elimina un alumno que ya estaba dentro del grupo, el cambio también se refleja en los datos del grupo.

## Integrantes

Vicente Guerra - ITI  
Diego Meneses - ICCI
