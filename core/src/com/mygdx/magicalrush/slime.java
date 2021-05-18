package com.mygdx.magicalrush;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;


public class slime {
    private float timerAnimacion = 0;
    private Sprite sprite;
    private Animation<TextureRegion> animation;
    private Animation animacion;

    private float velocidadX=-300;    //pixeles/segndo

    public slime(Texture texture, float x, float y){
        TextureRegion region= new TextureRegion(texture);
        TextureRegion [][] texturas= region.split(128,116);
    }
    TextureRegion[] arrFrames={texturas[0][0], texturas[0][1]};
    animation= new Animation<TextureRegion>(0.3f,arrFrames);
    animation.setPlayMode(Animation.PlayMode.LOOP);
    timerAnimacion=0;







    public enum EstadoMovimiento {
            INICIANDO,
            VIVO,
            EXPLOTA,
        }
    public enum EstadoSalto {
        EN_PISO,
        SUBIENDO,
        BAJANDO,
        CAIDA_LIBRE
    }
}

