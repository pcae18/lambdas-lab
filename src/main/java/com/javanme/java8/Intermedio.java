package com.javanme.java8;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.OptionalInt;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * Clase con ejercicios nivel intermedio
 * Created by aalopez on 18/08/15.
 */
public class Intermedio {

    static final String REGEXP = "[- .:,]+"; // separa cadenas de texto en palabras

    /**
     * Contar el número de líneas no vacías que tiene el archivo pasado por parámetro.
     * Usar nuevos métodos encontrados en la clase java.nio.file.Files en Java 8 para obtener un Stream de 
     * las líneas de texto un archivo.
     *
     * @param archivo Ruta al archivo que se desea evaluar
     * @return Cantidad de líneas en el archivo
     * @see java.nio.file.Files
     * @see java.util.stream.Stream
     */
    public long ejercicio1(Path archivo) {
    	long numberLines = -1;
    	try {
    		numberLines = Files.lines(archivo)
								//.filter(Predicate.not(String::isEmpty))
    							.filter(l -> !l.isEmpty())
    							.count();
		} catch (IOException e) {
			e.printStackTrace();
		}
    	return numberLines;
    }

    /**
     * Encuentra el número de caracteres que tiene la línea más larga del archivo.
     * Usar nuevos métodos encontrados en la clase java.nio.file.Files en Java 8 para obtener un Stream de 
     * las líneas de texto de un archivo.
     * Para poder obtener un OptionalInt como resultado, debes convertir el Stream a uno primitivo. 
     *
     * @param archivo Ruta al archivo que se desea evaluar
     * @return Cantidad de caracteres que tiene la línea más larga del archivo
     * @see java.nio.file.Files
     * @see java.util.stream.Stream
     * @see java.util.stream.IntStream
     */
    public OptionalInt ejercicio2(Path archivo) {
    	OptionalInt optional = null;
    	try {
    		optional = Files.lines(archivo)
							.mapToInt(String::length)
							.max();
		} catch (IOException e) {
			e.printStackTrace();
		}
    	return optional;
    }

    /**
     * De las palabras que se encuentran en el archivo pasado por parámetro, conviertelas a minúsculas,
     * sin duplicados, ordenadas primero por tamaño y luego alfabeticamente.
     *
     * Une todas las palabras en una cadena de texto separando cada palabra por un espacio (" ")
     *
     * Usa la constante REGEXP proveida al inicio de esta clase para hacer la separación de cadenas de texto a palabras. 
     * Es posible que esta expresión retorne palabras vacías por lo que tendrás que adicionar un filtro que las remueva.
     *
     * @param archivo Ruta al archivo que se desea evaluar
     * @return Cadena de texto que contiene las palabras en minúsculas, sin duplicados, ordenadas por tamaño, luego alfabeticamente
     * y cada palabra está separada por un espacio
     * @see java.nio.file.Files
     * @see java.util.stream.Stream
     * @see java.lang.String
     * @see java.util.stream.Collectors
     */
    public String ejercicio3(Path archivo) {
    	String cadena = "";
    	try {
			cadena = Files.lines(archivo)
							.map(l -> l.split(REGEXP))
							.map(Arrays::stream)
							.flatMap(Function.identity())
							.filter(w -> !w.isEmpty())
							.map(String::toLowerCase)
							.distinct()
							.sorted(Comparator
									.comparingInt(String::length)
									.thenComparing(Comparator.naturalOrder()))
							.collect(Collectors.joining(" "));
		} catch (IOException e) {
			e.printStackTrace();
		}
    	return cadena;	
    }

    /**
     * Categorizar TODAS las palabras de las primeras 10 líneas del archivo pasado por parámetro en un Map cuya llave es el
     * número de caracteres y el valor es el listado de palabras que tienen esa cantidad de caracteres
     *
     * Usa la constante REGEXP proveida al inicio de esta clase para hacer la separación de cadenas de texto a palabras. Es posible
     * que esta expresión retorne palabras vacías por lo que tendrás que adicionar un filtro que las remueva.
     *
     * @param archivo Ruta al archivo que se desea evaluar
     * @return Map cuya llave es la cantidad de caracteres y valor es el listado de palabras que tienen esa cantidad de
     * caracteres en las primeras 10 líneas del archivo
     * @see java.nio.file.Files
     * @see java.util.stream.Stream
     * @see java.lang.String
     * @see java.util.stream.Collectors
     */
    public Map<Integer, List<String>> ejercicio4(Path archivo) {
    	Map<Integer, List<String>> groupData = null;
    	try {
    		groupData = Files.lines(archivo)
					    		.limit(10)
					    		.map(l -> l.split(REGEXP))
					    		.map(Arrays::stream)
					    		.flatMap(Function.identity())
					    		.filter(w -> !w.isEmpty())
					    		.collect(Collectors.groupingBy(String::length));
		} catch (IOException e) {
			e.printStackTrace();
		}
    	return groupData;
    }


    /**
     * Categorizar TODAS las palabras de las primeras 100 líneas del archivo pasado por parámetro en un Map cuya llave es la
     * palabra y el valor es la cantidad de veces que se repite la palabra
     * <p/>
     * Usa la constante REGEXP proveida al inicio de esta clase para hacer la separación de cadenas de texto a palabras. Es posible
     * que esta expresión retorne palabras vacías por lo que tendrás que adicionar un filtro que las remueva.
     *
     * @param archivo Ruta al archivo que se desea evaluar
     * @return Map cuya llave son las palabras de las primeras 100 líneas del archivo y su valor es la cantidad de veces que se repite
     * dicha palabra en las primeras 100 líneas del archivo
     * @see java.nio.file.Files
     * @see java.util.stream.Stream
     * @see java.lang.String
     * @see java.util.stream.Collectors
     */
    public Map<String, Long> ejercicio5(Path archivo) {
    	Map<String, Long> groupData = null;
    	try {
    		groupData = Files.lines(archivo)
    							.limit(100)
    							.map(l -> l.split(REGEXP))
    							.flatMap(Arrays::stream)
    							.filter(w -> !w.isEmpty())
    							.collect(Collectors.groupingBy(Function.identity(),
    										Collectors.counting()));
    	}catch(Exception e) {
    		e.printStackTrace();
    	}
    	return groupData;
    }

    /**
     * Crear una doble agrupación de palabras únicas del archivo pasado por parámetro. Hacer la agrupación
     * en dos Maps. El Map externo tiene como llave la primera letra de la palabra en mayúsculas y como valor otro Map (el interno).
     * El Map interno debe tener como llave la cantidad de letras y como valor un listado de palabras que tienen esa cantidad
     * de letras.
     * <p/>
     * Por ejemplo, dadas las palabras "ermita sebastian sanisidro sancipriano cristorey chipichape"
     * El Map externo tendrá las llaves "E", "C", "S"
     * El valor para la llave "S" debe ser un Map con dos llaves: llave 9 y valor [sebastian sanisidro] (una lista de dos palabras)
     * y otra llave 11 con el valor [sancipriano] (una lista de un solo item)
     * <p/>
     * Usa la constante REGEXP proveida al inicio de esta clase para hacer la separación de cadenas de texto a palabras. Es posible
     * que esta expresión retorne palabras vacías por lo que tendrás que adicionar un filtro que las remueva.
     * Pista: Pasa las palabras a minúsculas para que el méotodo distinct las pueda filtrar correctamente
     *
     * @param archivo Ruta al archivo que se desea evaluar
     * @return Map cuya llave es la primera letra en mayúsculas de las palabras del archivo y su valor es otro Map cuya llave es la
     * cantidad de letras y su valor es el listado de palabras que tienen esa cantidad de letras
     * @see java.nio.file.Files
     * @see java.util.stream.Stream
     * @see java.lang.String
     * @see java.util.stream.Collectors
     */
    public Map<String, Map<Integer, List<String>>> ejercicio6(Path archivo) {
    	Map<String, Map<Integer, List<String>>> groupData = null;
    	try {
    		groupData = Files.lines(archivo)
    							.limit(100)
    							.map(l -> l.split(REGEXP))
    							.flatMap(Arrays::stream)
    							.filter(w -> !w.isEmpty())
    							.distinct()
    							.map(String::toLowerCase)
    							.peek(System.out::println)
    							.collect(Collectors.groupingBy(groupByInitialWordMayus,
    											Collectors.groupingBy(String::length
    											)));
    	}catch(Exception e) {
    		e.printStackTrace();
    	}
    	return groupData;
    }
    Function<String, String> groupByInitialWordMayus = x -> x.substring(0, 1).toUpperCase();
}

