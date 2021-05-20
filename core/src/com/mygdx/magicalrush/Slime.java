package com.mygdx.magicalrush;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class Slime extends Objeto{
    private float timerAnimacion = 0;
    private Sprite sprite;
    private Animation<TextureRegion> animation;
    private Animation animacion;
    private EstadoMovimiento estadoMovimiento;
    private float velocidadX=-300;    //pixeles/segndo

    public Slime(Texture texture, float x, float y){
        TextureRegion region= new TextureRegion(texture);
        TextureRegion [][] texturas= region.split(654, 178 );
        TextureRegion[] arrFrames={texturas[0][0], texturas[0][1], texturas[0][2], texturas[0][3],texturas[0][4]};
        animation= new Animation<TextureRegion>(0.3f,arrFrames);
        animation.setPlayMode(Animation.PlayMode.LOOP);
        timerAnimacion=0;

        sprite=new Sprite(texturas [0][0]);
        sprite.setPosition(x,y);
        estadoMovimiento=EstadoMovimiento.VIVO;
    }
    @Override
    public void render(SpriteBatch batch) {
        super.render(batch);
    }
    public EstadoMovimiento getMovimiento(){ return estadoMovimiento;}
    public void setEstado(EstadoMovimiento nuevoEstado){
        this.estadoMovimiento=nuevoEstado;
    }
    //MOVER ENEMIGOS
    public void moverIzquierda(float delta){
        float dx=velocidadX*delta;
        sprite.setX(sprite.getX()+dx);
    }

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
