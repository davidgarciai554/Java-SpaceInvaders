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
 * @author david
 */
public class sonido extends Thread {

    String _url = "";

    public void run() {

        //reproduzca el sonido a la vez
        AudioClip sonido;
        URL url;
        try {
            url = new URL("file:" + getClass().getResource(_url).getFile());
            sonido = java.applet.Applet.newAudioClip(url);
            sonido.play();
            Thread.sleep(1200);
            sonido.loop();
            sonido.stop();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public AudioClip musicaFondo(String fichero) {

        AudioClip sonido;
        URL url;

        try {
            url = new URL("file:" + getClass().getResource("/sonidos/sonidoFondo.wav").getFile());
            sonido = java.applet.Applet.newAudioClip(url);
            sonido.play();
            return sonido;
        } catch (Exception ex) {
            System.out.println("sonidoFondo " + ex.getMessage());
            return null;
        }
    }
}
