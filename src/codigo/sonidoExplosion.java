/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package codigo;

/**
 *
 * @author 
 */
public class sonidoExplosion extends Thread {//Creamos un hilo para que  												

    public void run() {                     //reproduzca el sonido a la vez
        ReproducirSonidos s = new ReproducirSonidos(); //que sigue el juego
//            s.ReproducirSonido(s.getClass().getResource("/sonidos/resplandorBUM.wav").getFile());
        s.ReproducirSonido(s.getClass().getResource("/sonidos/explosion.wav").getFile(), 1200);
    }
}