package com.mygdx.magicalrush;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class Personaje
{
    public static final float VELOCIDAD_Y = -4f;   // Velocidad de caída
    public static final float VELOCIDAD_X = 2;     // Velocidad horizontal

    private Sprite sprite;  // Sprite cuando no se mueve

    // Animación
    private Animation animacionCaminando;    // Caminando
    private float timerAnimacion;   // tiempo para calcular el frame
    public boolean lRight = true; //Esta viendo hacia la derecha

    // Estados del personaje
    private EstadoMovimiento estadoMovimiento;
    private EstadoSalto estadoSalto;
    public int vida;
    public int energia;

    // SALTO del personaje
    private static final float V0 = 40;     // Velocidad inicial del salto
    private static final float G = 9.81f;
    private static final float G_2 = G/2;   // Gravedad
    private float yInicial;         // 'y' donde inicia el salto
    private float tiempoVuelo;       // Tiempo que estará en el aire
    private float tiempoSalto;      // Tiempo actual de vuelo

    public boolean CL;
    public boolean CR;

    /*
    Constructor del personaje, recibe una imagen con varios frames
     */
    public Personaje(Texture textura) {
        // Lee la textura como región
        TextureRegion texturaCompleta = new TextureRegion(textura);
        // La divide en frames de **x**
        TextureRegion[][] texturaPersonaje = texturaCompleta.split(60,64);
        // Crea la animación con tiempo de 0.25 segundos entre frames.
        animacionCaminando = new Animation(0.2f,texturaPersonaje[3][3], texturaPersonaje[3][2], texturaPersonaje[3][1], texturaPersonaje[3][0] );
        // Animación infinita
        animacionCaminando.setPlayMode(Animation.PlayMode.LOOP);
        // Inicia el timer que contará tiempo para saber qué frame se dibuja
        timerAnimacion = 0;
        // Crea el sprite cuando para el personaje quieto (idle)
        sprite = new Sprite(texturaPersonaje[2][0]);    // quieto
        estadoMovimiento = EstadoMovimiento.INICIANDO;
        estadoSalto = EstadoSalto.EN_PISO;
    }

    // Dibuja el personaje
    public void render(SpriteBatch batch) {
        // Dibuja el personaje dependiendo del estadoMovimiento
        switch (estadoMovimiento) {
            case MOV_DERECHA:
            case MOV_IZQUIERDA:
                // Incrementa el timer para calcular el frame que se dibuja
                timerAnimacion += Gdx.graphics.getDeltaTime();
                // Obtiene el frame que se debe mostrar (de acuerdo al timer)
                TextureRegion region = (TextureRegion) animacionCaminando.getKeyFrame(timerAnimacion);
                // Dirección correcta
                if (estadoMovimiento==EstadoMovimiento.MOV_IZQUIERDA) {
                    if (!region.isFlipX()) {
                        region.flip(true,false);
                        lRight=false;
                    }
                }
                if (estadoMovimiento==EstadoMovimiento.MOV_DERECHA) {
                    if (region.isFlipX()) {
                        region.flip(true,false);
                        lRight=true;
                    }
                }
                // Dibuja el frame en las coordenadas del sprite
                batch.draw(region, sprite.getX(), sprite.getY());
                break;
            case INICIANDO:
            case QUIETO:
                //El personaje mira en la ultima dirección a la que camino
                TextureRegion regionq = (TextureRegion) sprite;
                if (estadoMovimiento==EstadoMovimiento.QUIETO && lRight == false)
                {
                    if (!regionq.isFlipX()) {
                        regionq.flip(true,false);
                        lRight=false;
                    }
                }
                if (estadoMovimiento==EstadoMovimiento.QUIETO && lRight == true)
                {
                    if (regionq.isFlipX()) {
                        regionq.flip(true,false);
                        lRight=true;
                    }
                }
                sprite.draw(batch); // Dibuja el sprite
                break;
        }

    }

    // Actualiza el sprite, de acuerdo al estadoMovimiento
    public void actualizar() {
        // Ejecutar movimiento horizontal
        float nuevaX = sprite.getX();
        switch (estadoMovimiento) {
            case MOV_DERECHA:
                // Prueba que no salga del mundo
                nuevaX += VELOCIDAD_X;
                if (nuevaX<=PantallaJuego.ANCHO_MAPA-sprite.getWidth() && CR) {
                    sprite.setX(nuevaX);
                }
                break;
            case MOV_IZQUIERDA:
                // Prueba que no salga del mundo
                nuevaX -= VELOCIDAD_X;
                if (nuevaX>=0 && CL) {
                    sprite.setX(nuevaX);
                }
                break;
        }
    }

    // Avanza en su caída
    public void caer() {
        sprite.setY(sprite.getY() + VELOCIDAD_Y);
    }

    // Actualiza la posición en 'y', está saltando
    public void actualizarSalto() {
        // Ejecutar movimiento vertical
        float y = V0 * tiempoSalto - G_2 * tiempoSalto * tiempoSalto;  // Desplazamiento desde que inició el salto
        if (tiempoSalto > tiempoVuelo / 2) { // Llegó a la altura máxima?
            // Inicia caída
            estadoSalto = EstadoSalto.BAJANDO;
        }
        tiempoSalto += 10 * Gdx.graphics.getDeltaTime();  // Actualiza tiempo
        sprite.setY(yInicial + y);    // Actualiza posición
        if (y < 0) {
            // Regresó al piso
            sprite.setY(yInicial);  // Lo deja donde inició el salto
            estadoSalto = EstadoSalto.EN_PISO;  // Ya no está saltando
        }
    }

    // Accesor de la variable sprite
    public Sprite getSprite() {
        return sprite;
    }

    public boolean getLRight(){
        return lRight;
    }

    // Accesores para la posición
    public float getX() {
        return sprite.getX();
    }

    public float getY() {
        return sprite.getY();
    }

    public int getVida() {return vida;}
    public void setVida(int v){vida = v;}

    public int getEnergia() {return energia;}
    public void setEnergia(int e) {energia = e;}

    public void setPosicion(float x, int y) {
        sprite.setPosition(x,y);
    }

    public boolean getCL() {return CL;}
    public void setCL(boolean b) {CL = b;}
    public boolean getCR() {return CR;}
    public void setCR(boolean b) {CR = b;}

    // Accesor del estadoMovimiento
    public EstadoMovimiento getEstadoMovimiento() {
        return estadoMovimiento;
    }

    // Modificador del estadoMovimiento
    public void setEstadoMovimiento(EstadoMovimiento estadoMovimiento) {
        this.estadoMovimiento = estadoMovimiento;
    }

    public void setEstadoSalto(EstadoSalto estadoSalto) {
        this.estadoSalto = estadoSalto;
    }

    // Inicia el salto
    public void saltar() {
        if (estadoSalto==EstadoSalto.EN_PISO) {
            tiempoSalto = 0;
            yInicial = sprite.getY();
            estadoSalto = EstadoSalto.SUBIENDO;
            tiempoVuelo = 2 * V0 / G;
        }
    }

    public EstadoSalto getEstadoSalto() {
        return estadoSalto;
    }

    public enum EstadoMovimiento {
        INICIANDO,
        QUIETO,
        MOV_IZQUIERDA,
        MOV_DERECHA
    }

    public enum EstadoSalto {
        EN_PISO,
        SUBIENDO,
        BAJANDO,
        CAIDA_LIBRE // Cayó de una orilla
    }
}