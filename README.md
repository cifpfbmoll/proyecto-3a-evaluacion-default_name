# Proyecto 3a evaluacion - Default_Name
## Proyecto.
* En este proyecto hacemos la gestion de una universidad incluyendo su biblioteca. 
* El objetivo principal de este proyecto es el aprendizaje y cumplir los objetivos propuestos por product owner.

## Sprints.
|Tarea              |Asignados  | Horas reales    | Horas estimadas|
|---                | ---            | ---              |   ---      |
|  **Sprint 1**       |               |Fecha inicio :     |Fecha fin: |
|#5 Estudiar java      |César, Enrique  | horas           | Horas     |
|#4 Estudiar acceso BBDD      |Alberto, Franco  | horas           | Horas     |
|#3 Estudiar interfaz gráfica      |Alfonso, Julia       | horas              | Horas     |
|#2 Diseñar tablas BBDD     |Todos | horas           | Horas     |
|#1 Diseño del diagrama de BBDD      |Todos | horas           | Horas     |
|  **Sprint 2**      |               |Fecha inicio :     |Fecha fin:  
|#7 Instalar y prepararse todos el UltraVCN      |Todos  | horas           | Horas     |
|#6 Compartir conocimiento con el grupo      |Todos  | horas           | Horas     |
|#8 Discutir funcionamiento interno de la aplicación y preparar base para empezar     |Todos  | horas           | Horas     |
|#9 Insertar tablas en mysql      |Franco  | horas           | Horas     |
|#36 Métodos clase administrador verProfesores, verAdministradores, verAlumnos, ver Bibliotecarios      |Julia  | horas           | Horas     |
|  **Sprint 3**      |               |**Fecha inicio :**    |**Fecha fin:** |
|#27 Cambiar Ñ por N     |Enrique | horas           | Horas     |
|#21 Crear profesor     |Alberto | horas           | Horas     |
|#18 Crear administrador     |Alberto  | horas           | Horas     |
|#20 Crear bibliotecario     |Franco  | horas           | Horas     |
|#13 Crear clase departamento     |Enrique | horas           | Horas     |
|#19 Crear alumno     |Cesar  | horas           | Horas     |
|#12 Crear clase asignatura     |Cesar  | horas           | Horas     |
|#14 Crear biblioteca     |Julia  | horas           | Horas     |
|#11 Crear clase titulaciones     |Alberto  | horas           | Horas     |
|#10 Crear clase Libros reservados     |Enrique  | horas           | Horas     |
|#16 Crear clase matriculaciones     |Enrique  | horas           | Horas     |
|#15 Crear clase Libros     |Alberto  | horas           | Horas     |
|#17 Crear clase personas(abstracta)     |Julia  | horas           | Horas     |
|  **Sprint 4**     |               |**Fecha inicio :**     |**Fecha fin:** |
|#33 Métodos clase profesor mostrar alumnos por asignatura    |Cesar | horas           | Horas     |
|#31 Actualizar base de datos   |Franco | horas           | Horas     |
|#25 Métodos clase profesor verAlumnos poner una nota    |Alberto | horas           | Horas     |
|#29 Métodos clase administrador mostrar personas    |Enrique | horas           | Horas     |
|#28 Métodos clase administrador añadir y eliminar personas    |Julia | horas           | Horas     |
|#30 Modificar hijos de persona    |Alberto | horas           | Horas     |
|#22 Insertar tablas e información en la máquina remota    |Enrique, Julia | horas           | Horas     |
|#26 Arreglar clase asignatura  |Alberto | horas           | Horas     |
|#23 Crear obtener conexion en main |Julia | horas           | Horas     |
|  **Sprint 5**     |               |**Fecha inicio :**     |**Fecha fin:** |
|#24 Métodos clase alumno altaMatricula |Franco | horas           | Horas     |
|#44 Métodos clase bibliotecario  devolver Libro |Cesar | horas           | Horas     |
|#46 Métodos clase profesor modificacion  mostrar alumnos por asignatura |Cesar | horas           | Horas     |
|#45 Métodos bibliotecario verReservas, verReservasFiltrado |Julia | horas           | Horas     |
|#41 Métodos clase alumno bajaMatricula |Enrique | horas           | Horas     |
|#34 Métodos clase admin verAsignaturas, añadirAsignaturas, eliminarAsignaturas |Julia | horas           | Horas     |
|#43 Métodos clase bibliotecario reservarLibros |Alberto | horas           | Horas     |
|#51 subir información BBDD remota |Alfons | horas           | Horas     |
|#40 Métodos clase bibliotecario verLibros, eliminarLibros , anadirLibros |Alberto | horas           | Horas     |
|#35 Métodos clase admin vertitulaciones, añadirtitulaciones , eliminartitulaciones |Enrique | horas           | Horas     |
|  **Sprint 6**     |               |**Fecha inicio :**     |**Fecha fin:** |
|#52 Realizar main |Alberto | horas           | Horas     |
|#42 Realizar readme |Alfons, Julia | horas           | Horas     |
|#53 Metodos clase alumno, modificar bajaMatricula |NADIE | horas           | Horas     |
|#57 Poner insert en la BBDD |Julia | horas           | Horas     |
|#59 Arreglar BBDD |Franco | horas           | Horas     |
|#39 Métodos clase eliminarNotaAAlumno  |Julia | horas           | Horas     |
|#58 Reparar método poner nota  |Julia | horas           | Horas     |
|#38 Métodos clase alumno ver estado asignaturas  |Enrique, Alfons | horas           | Horas     |
|#56 Añadir javadoc donde falte  |Alfons | horas           | Horas     |
|#55 Crear scanner globarl en las clases y cambiar funciones que lo tenga | Alfons | horas           | Horas     |
|#54 Eliminar throws |Alfons | horas           | Horas     |
|#48 Métodos clase bibliotecario filtrarLibrosPorAutor |Alberto | horas           | Horas     |
|#49 Métodos clase bibliotecario filtrarLibrosPorTematica |Alfons | horas           | Horas     |
|#47 Métodos clase bibliotecario filtrarLibrosPorEditorial |Julia | horas           | Horas     |

## Guia.
Esta aplicación intenta llevar la gestion de una universidad.

* UML:
![Imagen del UML](Resources/UML_PROJECTE.png)

* ESQUEMA DE LA BASE DE DATOS:
![Imagen del esquema](Resources/Universidad-Esquema.png)

* TABLAS DE LA BASE DE DATOS:
![Imagen de las tablas](Resources/Universidad-Tablas.png)

La aplicación consta de un centralizado en las clases descendientes de persona, que vienen a ser los roles como vease administrador, profesor... <br><br>
Para inicializar en la aplicacion se hace una identificacion, la cual detecta si el usuario que esta iniciando sesion existe en la base de datos o no. Después segun su rol, se le enseñara un menú distinto el qual le permitirà realizar diferentes acciones, algunos ejemplos:
* Alumno: Ver el estado de las asignaturas o darse de alta o baja en una matricula.
* Profesor: Ver notas de algun alumno o poner notas.
* Administrador: Crear, ver o eliminar asignaturas, departamentos, titulaciones y demas.
* Biblotecario: Crear y eliminar libros, filtraje de estos y reservarlos y devolverlos para los alumnos.

## Conceptos y conocimientos usados para realizar la aplicacion.
Los conocimientos y conceptos usados para realizar esta aplicación son:
* Conceptos basicos y generales del lenguaje de Java.
* Conceptos mas especificos en Java: Conexion a la base de datos y exportacion de la informacion en ficheros TXT y PDF.
* Metodologia SCRUM.
* Conociminetos de BBDD en esquema relacional y SQL.
* Conocimientos sobre UML.
* Conocimineto sobre MD.

## Integrantes.
* **SCRUM MASTER:** Alfons Lorente Darder.
* **SCRUM TEAM:** 
    * Julia Jaca Estepa.
    * Enrique Manuel Grao Sanchez.
    * César Andrés Granda Henríquez.
    * Franco Paolo Peri Bustos.
    * Alberto Serrano Ruiz.
* **PRODUCT OWNER:** Rafael Gion Muñoz.