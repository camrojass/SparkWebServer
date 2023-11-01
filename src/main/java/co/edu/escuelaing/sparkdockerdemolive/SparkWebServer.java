/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package co.edu.escuelaing.sparkdockerdemolive;

/**
 *
 * @author Camrojass
 * mail: camilo.rojas-s@mail.escuelaing.edu.co
 */

import static spark.Spark.*;

public class SparkWebServer {

    public static void main(String... args){
          port(getPort());
          get("hello", (req,res) -> "Hello Docker!");
    }

    private static int getPort() {
        if (System.getenv("PORT") != null) {
            return Integer.parseInt(System.getenv("PORT"));
        }
        return 4567;
    }
}
