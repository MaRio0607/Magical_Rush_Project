package com.mygdx.magicalrush;

import com.badlogic.gdx.graphics.Texture;

public class Disparo extends Objeto{
    private float vX=350;
    private boolean Right;

    public Disparo(Texture texture, float x, float y){
        super(texture,x,y);
    }

    //Mover el disparo
    public void  mover(float delta){
        //El personaje esta apuntando a la derecha
        if(Right==true)
        {
            float dx=vX*delta;
            sprite.setX(sprite.getX()+dx);
            sprite.setRotation(sprite.getRotation()+15);
        }
        //El personaje esta apuntando a la izquierda
        if(Right==false)
        {
            float dx=vX*delta;
            sprite.setX(sprite.getX()-dx);
            sprite.setRotation(sprite.getRotation()+15);
        }
    }

    public void setRight(boolean right){Right = right;}

    public float getX() {
        return sprite.getX();
    }

}
