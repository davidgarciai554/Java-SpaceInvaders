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
public class sonidoLaser extends Thread {//Creamos un hilo para que  												

    public void run() {                     //reproduzca el sonido a la  vez
        ReproducirSonidos s = new ReproducirSonidos(); //que sigue el juego
//            s.ReproducirSonido(s.getClass().getResource("/sonidos/disparosXBOX.wav").getFile());
        s.ReproducirSonido(s.getClass().getResource("/sonidos/laser.wav").getFile(), 1200);
    }
}