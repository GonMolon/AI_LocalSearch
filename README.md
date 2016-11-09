# AI_LocalSearch

This is a college's project to experiment with some local search algorithms in an optimitation statement.
The statement is about a fictional company that needs to send packages looking forward to have less expenses and achieve a greater customer's happiness.

Instrucciones para la ejecución de la práctica

Compilar el código fuente o utilizar el .jar incluido en la entrega.

Arrancar el programa desde la linea de comandos, incluyendo los siguientes parámetros en el mismo orden:

    - Modo: [0-1] --> Dependiendo de si se desea imprimir el coste (0) o el tiempo de búsqueda (1)

    - Seed: Integer --> Semilla para generar el problema

    - Num paquetes: Natural --> Número de paquetes del problema

    - Proporción: Double (igual o superior a 1) --> Relación (peso total ofertas / peso total paquetes) que tendrá el problema

    - Relación de felicidad: Integer --> Cuánto dinero se estima que Azamon tendrá de beneficio en compras posteriores por cada día que un paquete llegue antes. Valor recomendado: 0, para eliminar el concepto de felicidad, 10, como valor razonable.

    - Conjunto de operadores: [0-1-2] --> El conjunto de operadores que se utilizará en la búsqueda. 0 -> Move + Swap, 1 -> Move, 2 -> Swap

    - Generador: [0-1] --> Función generadora del estado inicial. 0 -> Función óptima (experimentalmente), 1 -> Función alternativa

    - Algoritmo de búsqueda: [0-1] --> El algoritmo de búsqueda que se utilizará. 0 -> Hill Climbing, 1 -> Simulated Annealing

    - Iteraciones: Natural --> Número de iteraciones que realizará el Simmulated Annealing
    
    - Iteraciones hasta cambio: Natural --> Número de iteraciones entre cambio de temperatura. 
    
    - k: Integer --> Cuándo empieza a decrecer la función de temperatura.
    
    - Lambda --> Cómo decrece la función de temperatura.
    
Es necesario introducir todos los parámetros, aunque los últimos 4 no se utilicen en el Hill Climbing (se pone 0, por ejemplo).
