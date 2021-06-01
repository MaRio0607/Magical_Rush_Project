package com.mygdx.magicalrush;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class Torauma {

    public static final float VELOCIDAD_X = 5;     // Velocidad horizontal

    private Sprite sprite;  // Sprite cuando no se mueve

    // Animación
    private Animation animacionCaminando;    // Caminando
    private float timerAnimacion;   // tiempo para calcular el frame
    public boolean lRight = true; //Esta viendo hacia la derecha

    // Estados del personaje
    private Personaje.EstadoMovimiento estadoMovimiento;
    private Personaje.EstadoSalto estadoSalto;
    public int vida;

    public Torauma(Texture textura) {
        // Lee la textura como región
        TextureRegion texturaCompleta = new TextureRegion(textura);
        // La divide en frames de **x**
        TextureRegion[][] texturaPersonaje = texturaCompleta.split(184,122);
        // Crea la animación con tiempo de 0.25 segundos entre frames.
        animacionCaminando = new Animation(0.2f,texturaPersonaje[0][0], texturaPersonaje[0][1], texturaPersonaje[0][2], texturaPersonaje[0][3] );
        // Animación infinita
        animacionCaminando.setPlayMode(Animation.PlayMode.LOOP);
        // Inicia el timer que contará tiempo para saber qué frame se dibuja
        timerAnimacion = 0;
        // Crea el sprite cuando para el personaje quieto (idle)
        sprite = new Sprite(texturaPersonaje[0][0]);    // quieto
        estadoMovimiento = Personaje.EstadoMovimiento.MOV_IZQUIERDA;
    }

    // Dibuja el personaje
    public void render(SpriteBatch batch) {
        // Dibuja el personaje dependiendo del estadoMovimiento
        // Incrementa el timer para calcular el frame que se dibuja
        timerAnimacion += Gdx.graphics.getDeltaTime();

        // Obtiene el frame que se debe mostrar (de acuerdo al timer)
        TextureRegion region = (TextureRegion) animacionCaminando.getKeyFrame(timerAnimacion);
        switch (estadoMovimiento) {
            case MOV_DERECHA:
                // Dirección correcta
                if (estadoMovimiento== Personaje.EstadoMovimiento.MOV_IZQUIERDA) {
                    if (region.isFlipX()) {
                        region.flip(true,false);
                        lRight=true;
                    }
                }
                if (estadoMovimiento== Personaje.EstadoMovimiento.MOV_DERECHA) {
                    if (!region.isFlipX()) {
                        region.flip(true,false);
                        lRight=false;
                    }
                }
                // Dibuja el frame en las coordenadas del sprite
                batch.draw(region, sprite.getX(), sprite.getY());
                break;
            case MOV_IZQUIERDA:
                if (estadoMovimiento== Personaje.EstadoMovimiento.MOV_IZQUIERDA) {
                    if (region.isFlipX()) {
                        region.flip(true,false);
                        lRight=true;
                    }
                }
                if (estadoMovimiento== Personaje.EstadoMovimiento.MOV_DERECHA) {
                    if (!region.isFlipX()) {
                        region.flip(true,false);
                        lRight=false;
                    }
                }
                // Dibuja el frame en las coordenadas del sprite
                batch.draw(region, sprite.getX(), sprite.getY());
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
                if (nuevaX<=PantallaJuego.ANCHO_MAPA-sprite.getWidth()+6) {
                    sprite.setX(nuevaX);
                    if(nuevaX >= PantallaJuego.ANCHO_MAPA-sprite.getWidth())
                    {
                        estadoMovimiento = Personaje.EstadoMovimiento.MOV_IZQUIERDA;
                    }
                }
                break;
            case MOV_IZQUIERDA:
                // Prueba que no salga del mundo
                nuevaX -= VELOCIDAD_X;
                if (nuevaX>=0) {
                    sprite.setX(nuevaX);
                    if(nuevaX <= 0)
                    {
                        estadoMovimiento = Personaje.EstadoMovimiento.MOV_DERECHA;
                    }
                }
                break;
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

    public float getY() { return  sprite.getY(); }

    public int getVida() {return vida;}
    public void setVida(int v){vida = v;}

    public void setPosicion(float x, int y) {
        sprite.setPosition(x,y);
    }

    // Accesor del estadoMovimiento
    public Personaje.EstadoMovimiento getEstadoMovimiento() {
        return estadoMovimiento;
    }

    // Modificador del estadoMovimiento
    public void setEstadoMovimiento(Personaje.EstadoMovimiento estadoMovimiento) {
        this.estadoMovimiento = estadoMovimiento;
    }

       public enum EstadoMovimiento {
        MOV_IZQUIERDA,
        MOV_DERECHA
    }

}
