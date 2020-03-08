/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package codigo;

import java.applet.AudioClip;
import java.net.URL;

/**
 *
 * @author
 */
public class sonidoFondo{

    public AudioClip sonar() { 
        
        AudioClip sonido;
        URL url;

        try{
            // Cogemos la ubicación del fichero de sonido
            url = new URL("file:" + getClass().getResource("/sonidos/sonidoFondo.wav").getFile());
            // Creamos un objeto AudioClip con el fichero de sonido
            sonido = java.applet.Applet.newAudioClip(url);
            // Lo hacemos sonar
            sonido.play();
            // Devolvermos el fichero de sonido
            return sonido;
        }catch(Exception ex){
            System.out.println("sonidoFondo " + ex.getMessage());
            return null;
        }
    }
}