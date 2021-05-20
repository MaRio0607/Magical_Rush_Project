package com.mygdx.magicalrush;

import com.badlogic.gdx.graphics.Texture;

public class Disparo extends Objeto{
    private float vX=350;

    public Disparo(Texture texture, float x, float y){
        super(texture,x,y);
    }

    //Mover a la dercha la bola de fuego
    public void  mover(float delta){
        float dx=vX*delta;
        sprite.setX(sprite.getX()+dx);
    }


    public float getX() {
        return sprite.getX();
    }

}
