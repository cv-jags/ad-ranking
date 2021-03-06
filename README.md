# Reto: Servicio para gestión de calidad de los anuncios

[![Build Status](https://travis-ci.org/idealista/coding-test-ranking.svg?branch=master)](https://travis-ci.org/idealista/coding-test-ranking)

Este repositorio contiene un API parcialmente desarrollada para desarrollar un servicio que se encargue de medir la calidad de los anuncios. Tu objetivo será implementar las historias de usuario que se describen más adelante.

Los supuestos están basados en un hipotético *equipo de gestión de calidad de los anuncios*, que demanda una serie de verificaciones automáticas para clasificar los anuncios en base a una serie de características concretas.

## Historias de usuario

* Yo, como encargado del equipo de gestión de calidad de los anuncios quiero asignar una puntuación a un anuncio para que los usuarios de idealista puedan ordenar anuncios de más completos a menos completos. La puntuación del anuncio es un valor entre 0 y 100 que se calcula teniendo encuenta las siguientes reglas:
    * Si el anuncio no tiene ninguna foto se restan 10 puntos. Cada foto que tenga el anuncio proporciona 20 puntos si es una foto de alta resolución (HD) o 10 si no lo es.
    * Que el anuncio tenga un texto descriptivo suma 5 puntos.
    * El tamaño de la descripción también proporciona puntos cuando el anuncio es sobre un piso o sobre un chalet. En el caso de los pisos, la descripción aporta 10 puntos si tiene entre 20 y 49 palabras o 30 puntos si tiene 50 o mas palabras. En el caso de los chalets, si tiene mas de 50 palabras, añade 20 puntos.
    * Que las siguientes palabras aparezcan en la descripción añaden 5 puntos cada una: Luminoso, Nuevo, Céntrico, Reformado, Ático.
    * Que el anuncio esté completo también aporta puntos. Para considerar un anuncio completo este tiene que tener descripción, al menos una foto y los datos particulares de cada tipología, esto es, en el caso de los pisos tiene que tener también tamaño de vivienda, en el de los chalets, tamaño de vivienda y de jardín. Además, excepcionalmente, en los garajes no es necesario que el anuncio tenga descripción. Si el anuncio tiene todos los datos anteriores, proporciona otros 40 puntos.

* Yo como encargado de calidad quiero que los usuarios no vean anuncios irrelevantes para que el usuario siempre vea contenido de calidad en idealista. Un anuncio se considera irrelevante si tiene una puntación inferior a 40 puntos.

* Yo como encargado de calidad quiero poder ver los anuncios irrelevantes y desde que fecha lo son para medir la calidad media del contenido del portal.

* Yo como usuario de idealista quiero poder ver los anuncios ordenados de mejor a peor para encontrar fácilmente mi vivienda.

## Consideraciones importantes

En este proyecto te proporcionamos un pequeño *esqueleto* escrito en Java usando [Spring Boot](https://spring.io/projects/spring-boot).

En dicho *esqueleto* hemos dejado para que completes un [Controller](https://docs.spring.io/spring-framework/docs/current/javadoc-api/org/springframework/stereotype/Controller.html) y un [Repository](https://docs.spring.io/spring-framework/docs/current/javadoc-api/org/springframework/stereotype/Repository.html) en memoria. Puedes crear las clases y métodos que consideres necesarios.

Podrás ejecutar el proyecto usando Maven ejecutando el siguiente comando en la carpeta donde esté el fichero `pom.xml`:

```bash
$ mvn spring-boot:run
```

**La persistencia de datos no forma parte del objetivo del reto**. Si no vas a usar el esqueleto que te proporcionamos, te sugerimos que la simplifiques tanto como puedas (con una base de datos embebida, "persistiendo" los objetos en memoria, usando un fichero...). **El diseño de una interfaz gráfica tampoco** forma parte del alcance del reto, por tanto no es necesario que la implementes.

**Nota:** No estás obligado a usar el proyecto proporcionado. Si lo prefieres, puedes usar cualquier otro lenguaje, framework y/o librería. Incluso puedes prescindir de estos últimos si consideras que no son necesarios. A lo que más importancia damos es a tener un código limpio y de calidad.

### Requisitos mínimos

A continuación se enumeran los requisitos mínimos para ejecutar el proyecto:

* Java 1.8
* Apache Maven 3.6.x

Otras versiones pueden funcionar, pero no han sido probadas y pueden presentar errores.

## Criterios de aceptación

* El código debe compilar y ser ejecutable :dancer:

* Debes proporcionar 3 endpoints: Uno para calcular la puntuación de todos los anuncios, otro para listar los anuncios para un usuario de idealista y otro para listar los anuncios para el responsable de del departamento de gestión de calidad.

# Implementación

## Moficaciones iniciales
Antes de implementar las historias de usuario se han realizado los siguientes cambios:

* Actualizar Spring-Boot a la última versión disponible.
* Incluir las siguientes dependencias:
    * logstash, lombok y guava para produccion, y
    * jUnit 5 y mockito para test.
* Fijar UTC como timezone de la aplicacion.

## Requisitos
Estos son los requisitos que implementa la aplicacion, basandonos en las historias ed usuario:

* Calculo del valor Ranking de un anuncio
    * -10 puntos si no tiene ninguna foto
    * 20 puntos X foto de alta resolución (HD)
    * 10 puntos X foto no alta resolución.
    * 5 puntos por descripción
    * 5 puntos cuando estas palabras aparezca en la descripción:
        * Luminoso,
        * Nuevo,
        * Céntrico,
        * Reformado,
        * Ático.
    * Para tipo pisos:
        * 10 puntos si la descripción tiene entre 20 y 49 palabras, o
        * 30 puntos si tiene 50 o mas palabras.
        * 40 puntos si esta completo:
            * tiene descripción,
            * al menos una foto, y
            * tamaño de vivienda.
    * Para tipo chalets,
        * 20 puntos si tiene mas de 50 palabras.
        * 40 puntos si esta completo:
            * tiene descripción,
            * al menos una foto,
            * tamaño de vivienda, y
            * tamaño de jardín.
    * Para tipo garajes
        * 40 puntos si esta completo:
            * al menos una foto, y
            * tamaño de vivienda.

## Notas de diseño

### Configuración
La clase RankingConfiguration incluida dentro de Main permite configurar las puntuaciones de los distintos criterios, así como algunos de los criterios.

La configuración por defecto es la descrita en las historias de usuario, por lo que no es necesario definirla.

La configuración equivalente sería:

```
add.ranking.relevant-score = 40;
add.ranking.no-photo-score = -10;
add.ranking.sdPhoto-score = 10;
add.ranking.hdPhoto-score = 20;
add.ranking.has-description-score = 5;
add.ranking.highlight-words-score = 5;
add.ranking.highlight-words=Luminoso,Nuevo,Céntrico,Reformado,Ático
add.ranking.flat-medium-description-min = 20;
add.ranking.flat-medium-description-score = 10;
add.ranking.flat-large-description-min = 50;
add.ranking.flat-large-description-score = 30;
add.ranking.flat-complete-score = 40;
add.ranking.chalet-large-description-min = 20;
add.ranking.chalet-large-description-score = 20;
add.ranking.chalet-complete-score = 40;
add.ranking.garage-complete-score = 40;
```

### Ranking
Se ha usado un patron estrategia para modelar cada uno de los casos que proporciona puntos a un anuncion.
Para el cálculo de la puntuación, todos los anuncios se evalúan contra todas las estrategias, y en caso de que cumplan la condición se añade la puntuación asociada.