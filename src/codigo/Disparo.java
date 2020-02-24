package codigo;

import java.awt.Image;
import javax.imageio.ImageIO;

/**
 *
 * @author Jorge Cisneros
 */
public class Disparo {

    Image imagen = null;
    public int posX = 0;
    public int posY = 0;

    public Disparo() {
        try {
            imagen = ImageIO.read(getClass().getResource("/imagenes/disparo.png"));
        } catch (Exception e) {
        }
    }

    public void mueveDisparoNave() {
        posY -= 5;
    }

    public void mueveDisparoMarciano() {
        posY += 5;
    }

    public void posicionaDisparoNave(Nave _nave) {

        posX = _nave.posX
                + _nave.imagen.getWidth(null) / 2
                - imagen.getWidth(null) / 2;
        posY = _nave.posY - _nave.imagen.getHeight(null) / 2;

    }

    public void posicionaDisparoMarciano(Marciano _marciano) {
        posX = _marciano.posX
                + _marciano.imagen1.getWidth(null) / 2
                - imagen.getWidth(null) / 2;
        posY = _marciano.posY - _marciano.imagen1.getHeight(null) / 2;
    }

}
