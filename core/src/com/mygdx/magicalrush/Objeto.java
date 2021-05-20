package com.mygdx.magicalrush;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

/*
Representa objetos en el juego
Autor: Mario
 */
public class Objeto {
    public Sprite sprite;//imagen posicion

    //constructor, Inicializa el objegto con la imagen y la poscicion
    public Objeto(Texture texture, float x, float y){
        sprite=new Sprite(texture);
        sprite.setPosition(x, y);
    }
    //constructor Vacio
    public Objeto(){
    }

    //dibujar el objeto -> begin y end <-
    public void render (SpriteBatch batch){
        sprite.draw(batch);
    }

}
