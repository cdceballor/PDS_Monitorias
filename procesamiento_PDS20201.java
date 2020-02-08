package pds20201;
//Imports de uso 
import java.io.FileWriter;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.URL;
import java.util.ArrayList;
import javafx.util.Pair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
/**
 *
 * @author Cristian Ceballos
 * https://thingspeak.com/channels/983678/feed.json Datos 
 * C:\\Users\\satur\\Downloads\\DatosGenerados\\DistanciaPersistenciaJson.txt
 * <iframe width=\"450\" height=\"260\" style=\"border: 1px solid #cccccc;\" src=\"https://thingspeak.com/channels/983678/widgets/147276\"></iframe> //Primer iframe
 * <iframe width=\"450\" height=\"260\" style=\"border: 1px solid #cccccc;\" src=\"https://thingspeak.com/channels/983678/charts/1?bgcolor=%23ffffff&color=%23d62020&dynamic=true&results=60&timescale=10&type=line\"></iframe> //Segundo iframe
 * C:\\Users\\satur\\Downloads\\DatosGenerados\\grafica.html Donde guardamos los datos graficados
 */
public class procesamiento_PDS20201 {
    
    static ArrayList<Pair<String, Double>> distancias = new ArrayList<>(); //ArrayList de distancias y sus fechas
    static int indiceDisMin; // Este indice nos indica en que posición del ArrayList está la Distancia minima
    static int indiceDisMax; // Este indice nos indica en que posición del ArrayList está la Distancia máxima
//    static Grafica graficaTemperatura = new Grafica(); // Objeto de clase Grafica
    
    /**
     * Obtenemos los datos de la página web que nos proporciona ThingSpeak.
     * @return cadena de caracteres con todo el archivo Json de la página
     */
    public static String leerPaginaWeb() {
        String code = "";
        try {
            StringBuffer codeBuffered = new StringBuffer();

            URL url = new URL(""); //URL de donde están los datos a procesar (vía thingspeack)
            InputStream in = url.openStream();
            BufferedReader read = new BufferedReader(new InputStreamReader(in));

            String line;
            int i = 0;
            while ((line = read.readLine()) != null) {
                codeBuffered.append(line).append("\n");

            }

            code = codeBuffered.toString(); // Este es el código de la página

            //cerramos los streams
            in.close();
            read.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return code;
    }
    /**
     * Método donde obtengo los valores de mi archivo Json. ¿Qué es un archivo Json?
     * https://canela.me/articulo/JSON-JavaScript-jQuery/%C2%BFQu%C3%A9-es-JSON-para-qu%C3%A9-sirve-y-d%C3%B3nde-se-usa
     */
    public static void recibirParametros() {
        try {
            JSONObject obj = new JSONObject(leerPaginaWeb());
            JSONArray arr = obj.getJSONArray("feeds");
            for (int i = 0; i < arr.length(); i++) {
                String fecha = arr.getJSONObject(i).getString("created_at");
                double distancia = arr.getJSONObject(i).getDouble("field1");
                agregarDatos(fecha, distancia);
            }
        } catch (JSONException e) {
            System.out.println("No pude leer la página web.");
        }
    }
    /**
     * Añadimos nuestros dos valores a el ArrayList.
     *
     * @param fecha
     * @param valorDistancia
     */
    public static void agregarDatos(String fecha, double valorDistancia) {
        distancias.add(new Pair(fecha, valorDistancia));
    }

    /**
     * Recorremos el ArrayList para sumar todas las temperaturas y al final
     * dividir por el número de temperaturas que tenemos.
     *
     * @return promedio
     */
    public static double hallarDistanciaPromedio() {
        double sumatoria = 0;
        for (int i = 0; i < distancias.size(); i++) {
            sumatoria += distancias.get(i).getValue();
        }
        return sumatoria / distancias.size();

    }

    /**
     * Hacemos un sort (ordenamiento) sencillo para calcular la temperatura
     * minima.
     *
     * @return menor
     */
    public static double hallarDistanciaMin() {
        double menor = distancias.get(0).getValue();
        for (int i = 1; i < distancias.size(); i++) {
            if (menor >= distancias.get(i).getValue()) {
                menor = distancias.get(i).getValue();
                indiceDisMin = i;
            }
        }
        return menor;
    }

    /**
     * Hacemos un sort (ordenamiento) sencillo para calcular la temperatura
     * maxima.
     *
     * @return
     */
    public static double hallarDistanciaMax() {
        double mayor = distancias.get(0).getValue();
        for (int i = 1; i < distancias.size(); i++) {
            if (mayor <= distancias.get(i).getValue()) {
                mayor = distancias.get(i).getValue();
                indiceDisMax = i;
            }
        }
        return mayor;
    }

    /**
     * Escribimos nuestro archivo para guardarlo en nuestra máquina con todas
     * las temperaturas y la temperatura promedio, minima y máxima.
     *
     * Explicación de como escribir un archivo:
     * https://www.youtube.com/watch?v=i04SHI2oZx4
     *
     * @param promedioDist
     * @param minimDist
     * @param maxDist
     */
    public static void escribirArchivo(double promedioDist, double minimDist, double maxDist) {
        FileWriter fichero = null;
        PrintWriter pw = null;
        try {
            System.out.println("voy acá");
            fichero
                    = new FileWriter(""); // Dirección de mi máquina donde guardamos nuestro archivo escrito.             
            pw = new PrintWriter(fichero);
            pw.println("Distancia promedio: " + promedioDist);
            pw.println("Distancia máxima: " + maxDist);
            pw.println("Distancia minima: " + minimDist);
            for (int i = 0; i < distancias.size(); i++) {
                //Datos que se almacenan en el txt
                pw.print("Fecha : " + distancias.get(i).getKey() + "    ");
                
                pw.println("Distancia: " + distancias.get(i).getValue());
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (null != fichero) {
                    fichero.close();
                }
            } catch (Exception e2) {
                e2.printStackTrace();
            }
        }
    }

    public static void main(String[] args) {
        recibirParametros();
        hallarDistanciaPromedio();
        
        double promedioTemp = hallarDistanciaPromedio();
        double minimTemp = hallarDistanciaMin();
        double maxTemp = hallarDistanciaMax();
        
        System.out.println("Si la cosa llega hasta acá, es porque la fecha siempre es la misma");
        escribirArchivo(promedioTemp,minimTemp, maxTemp);
        
        //html para el iframe
         String htmlDato = "";
        String htmlGrafica = "";
        File f = new File ("");
        try {
            BufferedWriter bw = new BufferedWriter(new FileWriter(f));
            bw.write(htmlGrafica);
            bw.write(htmlDato);
            bw.close();
        } catch (IOException e){
            e.printStackTrace();
        }
        
    }
}

