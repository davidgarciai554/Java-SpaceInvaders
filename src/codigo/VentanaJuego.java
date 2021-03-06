/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package codigo;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Label;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;
import javax.imageio.ImageIO;
import javax.swing.Timer;
import java.applet.AudioClip;

/**
 *
 * @author jorgecisneros
 */
public class VentanaJuego extends javax.swing.JFrame {

    // Objetos AudioClip que contiene el sonido de fondo del juego
    static AudioClip audioclipSf;
    static sonido sf = new sonido();
            
    static int ANCHOPANTALLA = 1200;
    static int ALTOPANTALLA = 700;

    int filasMarcianos = 8;
    int columnasMarcianos = 10;
    int contador = 0;
    int puntuacion = 0;
    int marcianosMuertos = 0;

    Random aleatorio = new Random();

    int disparoMarciano, disparoMarciano1, disparoMarciano2;

    public static Label label1 = new Label();

    Graphics2D g2;

    BufferedImage buffer = null;
    //buffer para guardar las imágenes de todos los marcianos
    BufferedImage plantilla = null;
    Image[] imagenes = new Image[30];

    Timer temporizador = new Timer(10, new ActionListener() {//bucle de animacion del juego. refresca el contenido de la pantalla
        @Override
        public void actionPerformed(ActionEvent ae) {
            //TODO: codigo de animacion
            bucleJuego();
        }
    });

    Marciano marciano = new Marciano(ANCHOPANTALLA);//inicializo el marciano
    Nave miNave = new Nave();
    Disparo disparoNave = new Disparo();
    Disparo _disparoMarciano = new Disparo();
    ArrayList<Disparo> listaDisparos = new ArrayList();
    ArrayList<Disparo> listaDisparosMarciano = new ArrayList();
    ArrayList<Explosion> listaExplosiones = new ArrayList();

    //el array de dos dimensiones que guarda la lista de marcianos
    Marciano[][] listaMarcianos = new Marciano[filasMarcianos][columnasMarcianos];
    //dirección en la que se mueve el grupo de marcianos
    boolean direccionMarcianos = true;

    /**
     * Creates new form VentanaJuego
     */
    public VentanaJuego() {
        initComponents();

        try {
            plantilla = ImageIO.read(getClass().getResource("/imagenes/invaders2.png"));
        } catch (IOException ex) {
        }
        //cargo las 30 imágenes del spritesheet en el array de bufferedimages
        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 4; j++) {
                imagenes[i * 4 + j] = plantilla
                        .getSubimage(j * 64, i * 64, 64, 64)
                        .getScaledInstance(32, 32, Image.SCALE_SMOOTH);

            }
        }
        imagenes[20] = plantilla.getSubimage(0, 320, 66, 32); //sprite de la nave
        imagenes[21] = plantilla.getSubimage(66, 320, 64, 32);
        imagenes[23] = plantilla.getSubimage(255, 320, 32, 32);//explosion parteB
        imagenes[22] = plantilla.getSubimage(255, 289, 32, 32);//explosion parteA
        setSize(ANCHOPANTALLA + 10, ALTOPANTALLA + 30);
        jPanel1.setSize(ANCHOPANTALLA, ALTOPANTALLA);
        buffer = (BufferedImage) jPanel1.createImage(ANCHOPANTALLA, ALTOPANTALLA);//inicializo el buffer
        buffer.createGraphics();

        temporizador.start();//arranco el temporizador
        miNave.imagen = imagenes[21];
        miNave.posX = ANCHOPANTALLA / 2 - miNave.imagen.getWidth(this) / 2;
        miNave.posY = ALTOPANTALLA - 100;
        //creamos el array de marcianos

        label1.setForeground(Color.white);
        label1.setBackground(Color.black);
        label1.setBounds(0, ALTOPANTALLA - 40, 100, 30);
        label1.setText("" + puntuacion);

        for (int i = 0; i < filasMarcianos; i++) {
            for (int j = 0; j < columnasMarcianos; j++) {
                listaMarcianos[i][j] = new Marciano(ANCHOPANTALLA);
                listaMarcianos[i][j].imagen1 = imagenes[2 * i];
                listaMarcianos[i][j].imagen2 = imagenes[2 * i + 1];
                listaMarcianos[i][j].posX = j * (15 + listaMarcianos[i][j].imagen1.getWidth(null));
                listaMarcianos[i][j].posY = i * (10 + listaMarcianos[i][j].imagen1.getHeight(null));
            }
        }
        disparoNave.posY = -2000;
        jPanel1.add(label1);
        audioclipSf = sf.musicaFondo("/sonidos/sonidoFondo.wav");
    }

    private void pintaMarcianos(Graphics2D _g2) {
        for (int i = 0; i < filasMarcianos; i++) {
            for (int j = 0; j < columnasMarcianos; j++) {
                listaMarcianos[i][j].mueve(direccionMarcianos);
                if (contador < 50) {
                    _g2.drawImage(listaMarcianos[i][j].imagen1, listaMarcianos[i][j].posX, listaMarcianos[i][j].posY, null);
                } else if (contador < 100) {
                    _g2.drawImage(listaMarcianos[i][j].imagen2, listaMarcianos[i][j].posX, listaMarcianos[i][j].posY, null);
                } else {
                    contador = 0;
                }
                if (listaMarcianos[i][j].posX == ANCHOPANTALLA - listaMarcianos[i][j].imagen1.getWidth(null) || listaMarcianos[i][j].posX == 0) {
                    direccionMarcianos = !direccionMarcianos;
                    for (int k = 0; k < filasMarcianos; k++) {
                        for (int m = 0; m < columnasMarcianos; m++) {
                            listaMarcianos[k][m].posY += 100;
                        }
                    }
                }
            }
        }
    }

    private void pintaDisparoNave(Graphics2D g2) {
        Disparo aux;
        for (int i = 0; i < listaDisparos.size(); i++) {
            aux = listaDisparos.get(i);
            aux.mueveDisparoNave();
            if (aux.posY < 0) {
                listaDisparos.remove(i);
            } else {
                g2.drawImage(aux.imagen, aux.posX, aux.posY, null);
            }
        }
    }

    private void pintaDisparoMarciano(Graphics2D g2) {
        Disparo aux;
        for (int i = 0; i < listaDisparosMarciano.size(); i++) {
            aux = listaDisparosMarciano.get(i);
            aux.mueveDisparoMarciano();
            if (aux.posY > ALTOPANTALLA) {
                listaDisparosMarciano.remove(i);
            } else {
                g2.drawImage(aux.imagen, aux.posX, aux.posY, null);
            }
        }
    }

    private void pintaExplosiones(Graphics2D g2) {
        //pinta todas las explosiones 
        Explosion explosionAux;
        for (int i = 0; i < listaExplosiones.size(); i++) {
            explosionAux = listaExplosiones.get(i);
            explosionAux.tiempoDeVida--;
            if (explosionAux.tiempoDeVida > 25) {
                g2.drawImage(explosionAux.imagen1,
                        explosionAux.posX,
                        explosionAux.posY, null);
            } else {
                g2.drawImage(explosionAux.imagen2,
                        explosionAux.posX,
                        explosionAux.posY, null);
            }
            //si el tiempo de vida de la explosión es menor o igual a 0 la elimino
            if (explosionAux.tiempoDeVida <= 0) {
                listaExplosiones.remove(i);
            }
        }
    }

    private void bucleJuego() {//redibuja los objetos en el jPanel1

        disparoMarciano = aleatorio.nextInt(100);
        disparoMarciano1 = aleatorio.nextInt(100);
        disparoMarciano2 = aleatorio.nextInt(100);

        g2 = (Graphics2D) buffer.getGraphics();//borro todo lo que ahi en el buffer

        g2.setColor(Color.BLACK);//doy el color negro a la pantalla
        g2.fillRect(0, 0, ANCHOPANTALLA, ALTOPANTALLA);
        pintaMarcianos(g2);
        ///////////////////////////////////////////////////
        contador++;
        if (disparoMarciano == contador || disparoMarciano1 == contador || disparoMarciano2 == contador) {
            Disparo d = new Disparo();
            d.posicionaDisparoMarciano(listaMarcianos[aleatorio.nextInt(filasMarcianos)][aleatorio.nextInt(filasMarcianos)]);
            listaDisparosMarciano.add(d);
        }
        //dibujo la nave
        g2.drawImage(miNave.imagen, miNave.posX, miNave.posY, null);

        pintaDisparoNave(g2);
        pintaDisparoMarciano(g2);

        pintaExplosiones(g2);
        miNave.mueve();
        chequeaColision();
        ///////////////////////////////////////////////////
        g2 = (Graphics2D) jPanel1.getGraphics();//dibujo de golpe el buffer sobre el jPanel
        g2.drawImage(buffer, 0, 0, null);

    }

    //chequea si un disparo y un marciano colisionan
    private void chequeaColision() {
        Rectangle2D.Double rectanguloMarciano = new Rectangle2D.Double();
        Rectangle2D.Double rectanguloDisparo = new Rectangle2D.Double();
        Rectangle2D.Double rectanguloNave = new Rectangle2D.Double();
        Rectangle2D.Double rectanguloDisparoMarciano = new Rectangle2D.Double();

        //Calculo el choque del disparo de la nave con los marcianos
        for (int k = 0; k < listaDisparos.size(); k++) {
            rectanguloDisparo.setFrame(listaDisparos.get(k).posX, listaDisparos.get(k).posY, listaDisparos.get(k).imagen.getWidth(null), listaDisparos.get(k).imagen.getHeight(null));
            for (int i = 0; i < filasMarcianos; i++) {
                for (int j = 0; j < columnasMarcianos; j++) {
                    //calculo el rectángulo corresponmdiente al marciano que estoy comprobando
                    rectanguloMarciano.setFrame(listaMarcianos[i][j].posX, listaMarcianos[i][j].posY, listaMarcianos[i][j].imagen1.getWidth(null), listaMarcianos[i][j].imagen1.getHeight(null));

                    if (rectanguloDisparo.intersects(rectanguloMarciano)) {
                        //si entra aquí es porque han chocado un marciano y el disparo
                        Explosion e = new Explosion();
                        e.posX = listaMarcianos[i][j].posX;
                        e.posY = listaMarcianos[i][j].posY;
                        e.imagen1 = imagenes[23];
                        e.imagen2 = imagenes[22];
                        listaExplosiones.add(e);

                        sonido s = new sonido();
                        s._url="/sonidos/explosion.wav";
                        s.start();

                        marcianosMuertos++;
                        if (marcianosMuertos == (filasMarcianos * columnasMarcianos)) {
                            temporizador.stop();
                            try {
                                g2.drawImage(ImageIO.read(getClass().getResource("/imagenes/victoria.png")), 0, 0, ANCHOPANTALLA, ALTOPANTALLA, null);
                                audioclipSf.stop();
                            } catch (IOException ex) {
                            }
                        }

                        listaMarcianos[i][j].posX = 2000;
                        puntuacion += 50;
                        label1.setText("" + puntuacion);
                        listaDisparos.remove(k);
                    }
                }
            }
        }

        //Comparo el choque de los marcianos y de sus disparos con la nave
        for (int k = 0; k < listaDisparosMarciano.size(); k++) {
            rectanguloDisparoMarciano.setFrame(listaDisparosMarciano.get(k).posX,
                    listaDisparosMarciano.get(k).posY,
                    listaDisparosMarciano.get(k).imagen.getWidth(null),
                    listaDisparosMarciano.get(k).imagen.getHeight(null));

            rectanguloNave.setFrame(miNave.posX,
                    miNave.posY,
                    miNave.imagen.getWidth(null),
                    miNave.imagen.getHeight(null));
            if (rectanguloNave.intersects(rectanguloMarciano) || rectanguloNave.intersects(rectanguloDisparoMarciano)) {
                temporizador.stop();
                try {
                    g2.drawImage(ImageIO.read(getClass().getResource("/imagenes/gameover.png")), 0, 0, ANCHOPANTALLA, ALTOPANTALLA, null);
                    // Paramos el AudioClip del sonido de fondo
                    audioclipSf.stop();
                } catch (IOException ex) {
                }
            }

        }
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel1 = new javax.swing.JLabel();
        jPanel1 = new javax.swing.JPanel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                formKeyPressed(evt);
            }
            public void keyReleased(java.awt.event.KeyEvent evt) {
                formKeyReleased(evt);
            }
        });

        jLabel1.setForeground(new java.awt.Color(255, 255, 255));
        jLabel1.setText("jLabel1");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 1372, Short.MAX_VALUE)
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 700, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel1)
                .addGap(976, 976, 976))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel1)
                    .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void formKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_formKeyPressed
        switch (evt.getKeyCode()) {
            case KeyEvent.VK_LEFT:
                miNave.setPulsadoIzquierda(true);
                break;
            case KeyEvent.VK_RIGHT:
                miNave.setPulsadoDerecha(true);
                break;
            case KeyEvent.VK_SPACE:
                Disparo d = new Disparo();
                d.posicionaDisparoNave(miNave);

                sonido s = new sonido();
                s._url="/sonidos/laser.wav";
                s.start();

                //agregamos el disparo a la lista de disparos
                listaDisparos.add(d);
                break;
        }
    }//GEN-LAST:event_formKeyPressed

    private void formKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_formKeyReleased
        switch (evt.getKeyCode()) {
            case KeyEvent.VK_LEFT:
                miNave.setPulsadoIzquierda(false);
                break;
            case KeyEvent.VK_RIGHT:
                miNave.setPulsadoDerecha(false);
                break;

        }
    }//GEN-LAST:event_formKeyReleased

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;

                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(VentanaJuego.class
                    .getName()).log(java.util.logging.Level.SEVERE, null, ex);

        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(VentanaJuego.class
                    .getName()).log(java.util.logging.Level.SEVERE, null, ex);

        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(VentanaJuego.class
                    .getName()).log(java.util.logging.Level.SEVERE, null, ex);

        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(VentanaJuego.class
                    .getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new VentanaJuego().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel jLabel1;
    private javax.swing.JPanel jPanel1;
    // End of variables declaration//GEN-END:variables
}
