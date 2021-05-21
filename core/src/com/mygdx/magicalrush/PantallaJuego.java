package com.mygdx.magicalrush;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Timer;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

public class PantallaJuego implements Screen
{
    public static final float ANCHO_MAPA = 1600;   // Ancho del mapa en pixeles
    public static final float ALTO_MAPA = 1100;    // Alto del mapa en pixeles

    // Referencia al objeto de tipo Game (tiene setScreen para cambiar de pantalla)
    private Juego juego;

    // La cámara y vista principal
    private OrthographicCamera camara;
    private Viewport vista;

    // Objeto para dibujar en la pantalla
    private SpriteBatch batch;

    // MAPA
    private TiledMap mapa;      // Información del mapa en memoria
    private OrthogonalTiledMapRenderer rendererMapa;    // Objeto para dibujar el mapa

    // Personaje
    private Texture texturaPersonaje;       // Aquí cargamos la imagen del personaje con varios frames
    private Personaje rui;
    public static final int TAM_CELDA = 16;
    public static final int TAM_RUI = 60;

    //Disparo del personaje
    private Array<Disparo> arrBolas;
    private Texture texturaBola;

    // HUD. Los componentes en la pantalla que no se mueven
    private OrthographicCamera camaraHUD;   // Cámara fija
    // Botones izquierda/derecha
    private Texture texturaBtnIzquierda;
    private Boton btnIzquierda;
    private Texture texturaBtnDerecha;
    private Boton btnDerecha;
    // Botón saltar
    private Texture texturaSalto;
    private Boton btnSalto;

    // Botón Disparo
    private Texture texturaDisp;
    private Boton btnDisp;


    // Botón Disparo
    private Texture texturePausa;
    private Boton btnPausa;

    // Fin del juego, Gana o pierde
    private Texture texturaGana;
    private Boton btnGana;

    //PAUSA
    private EscenaPausa escenaPausa;
    private ProcesadorEntrada procesadorEntrada;

    // Estados del juego
    private EstadosJuego estadoJuego=EstadosJuego.JUGANDO;

    //hitbox
    Rectangle SlimeHitbox;
    Rectangle RuiHitbox;
    float x,y;

    // Fondo
    private Texture texturaNubes;
    private float xFondo = 0;
    private float yFondo = 0;


    public PantallaJuego(Juego juego) {
        this.juego = juego;
    }

    /*
    Se ejecuta al mostrar este Screen como pantalla de la app
     */
    @Override
    public void show() {
        // Crea la cámara/vista
        camara = new OrthographicCamera(Juego.ANCHO_CAMARA, Juego.ALTO_CAMARA);
        camara.position.set(Juego.ANCHO_CAMARA / 2, Juego.ALTO_CAMARA / 2, 0);
        camara.update();
        vista = new StretchViewport(Juego.ANCHO_CAMARA, Juego.ALTO_CAMARA, camara);

        batch = new SpriteBatch();

        // Cámara para HUD
        camaraHUD = new OrthographicCamera(Juego.ANCHO_CAMARA, Juego.ALTO_CAMARA);
        camaraHUD.position.set(Juego.ANCHO_CAMARA / 2, Juego.ALTO_CAMARA / 2, 0);
        camaraHUD.update();

        cargarRecursos();
        crearObjetos();
        crearBolas();

        // Indicar el objeto que atiende los eventos de touch (entrada en general)
        //poner input procesor
        procesadorEntrada=new ProcesadorEntrada();
        Gdx.input.setInputProcessor(procesadorEntrada);

    }
    private void crearBolas() {
        arrBolas=new Array<>();
        texturaBola=new Texture("SHOOT.png");
    }

    private void cargarRecursos(){
        // Cargar las texturas/mapas
        AssetManager assetManager = juego.getAssetManager();   // Referencia al assetManager

        assetManager.load("RUIS-Sheet.png",Texture.class);
        assetManager.load("disparo.png", Texture.class);
        assetManager.load("Cont_bot.png",Texture.class);
        assetManager.load("SHOOT.png",Texture.class);
        assetManager.load("btones.png",Texture.class);
        assetManager.load("PausaBoton.png",Texture.class);
        assetManager.load("nubes.png",Texture.class);

        // Se bloquea hasta que cargue todos los recursos
        assetManager.finishLoading();
    }

    private void crearObjetos() {
        AssetManager assetManager = juego.getAssetManager();   // Referencia al assetManager
        //fondo
        texturaNubes = assetManager.get("nubes.png");
        // Carga el mapa en memoria
        mapa = assetManager.get("Mapa.tmx");

        // Crear el objeto que dibujará el mapa
        rendererMapa = new OrthogonalTiledMapRenderer(mapa,batch);
        rendererMapa.setView(camara);

        // Cargar frames
        texturaPersonaje = assetManager.get("RUIS-Sheet.png");

        // Crear el personaje
        rui = new Personaje(texturaPersonaje);

        // Posición inicial del personaje
        rui.getSprite().setPosition(Juego.ANCHO_CAMARA / 10, Juego.ALTO_CAMARA * 1.01f);

        // Crear los botones
        texturaBtnIzquierda = assetManager.get("izquierda.png");
        btnIzquierda = new Boton(texturaBtnIzquierda);
        btnIzquierda.setPosicion(TAM_CELDA, 5 * TAM_CELDA);
        //btnIzquierda.setAlfa(0.7f); // Un poco de transparencia
        texturaBtnDerecha = assetManager.get("derecha.png");
        btnDerecha = new Boton(texturaBtnDerecha);
        btnDerecha.setPosicion(6 * TAM_CELDA+ 100, 5 * TAM_CELDA);
        //btnDerecha.setAlfa(0.7f); // Un poco de transparencia
        texturaSalto = assetManager.get("salto.png");
        btnSalto = new Boton(texturaSalto);
        btnSalto.setPosicion(Juego.ANCHO_CAMARA - 5 * TAM_CELDA-250, 5 * TAM_CELDA);

        texturaDisp = assetManager.get("disparo.png");
        btnDisp = new Boton(texturaDisp);
        btnDisp.setPosicion(Juego.ANCHO_CAMARA - 5 * TAM_CELDA-100, 5 * TAM_CELDA);

        texturePausa = assetManager.get("PausaBoton.png");
        btnPausa = new Boton(texturePausa);
        btnPausa.setPosicion(ANCHO_MAPA/2+400, ALTO_MAPA/2+100);
        //btnSalto.setAlfa(0.7f);
        // Gana
        //texturaGana = assetManager.get("archivo.png");
        //btnGana = new Boton(texturaGana);
        //btnGana.setPosicion(Juego.ANCHO_CAMARA/2-btnGana.getRectColision().width/2,
                //Juego.ALTO_CAMARA/2-btnGana.getRectColision().height/2);
        //btnGana.setAlfa(0.7f);}

        //hitboxes
        RuiHitbox=new Rectangle(x,y,70,70);
        SlimeHitbox=new Rectangle(x,y,58,58);

    }



    /*
    Dibuja TODOS los elementos del juego en la pantalla.
    Este método se está ejecutando muchas veces por segundo.
     */
    @Override
    public void render(float delta) { // delta es el tiempo entre frames (Gdx.graphics.getDeltaTime())
        actualizar(delta);
        if (estadoJuego!=EstadosJuego.PERDIO) {
            // Actualizar objetos en la pantalla
            moverPersonaje();
            actualizarCamara(); // Mover la cámara para que siga al personaje
        }

        // Dibujar
        borrarPantalla();


        batch.setProjectionMatrix(camara.combined);

        //Dibujar fondo
        batch.begin();
        batch.draw(texturaNubes, xFondo, yFondo);
        batch.end();

        rendererMapa.setView(camara);
        rendererMapa.render();  // Dibuja el mapa

        // Entre begin-end dibujamos nuestros objetos en pantalla
        batch.begin();

        rui.render(batch);    // Dibuja el personaje

        //Dibujar bolas de fuego
        for (Disparo bolaFuego:arrBolas){
            bolaFuego.render(batch);
        }
        batch.end();

        // Dibuja el HUD
        batch.setProjectionMatrix(camaraHUD.combined);
        batch.begin();

        // ¿Ya ganó?
        if (estadoJuego==EstadosJuego.GANO) {
            btnGana.render(batch);
        } else {
            btnIzquierda.render(batch);
            btnDerecha.render(batch);
            btnSalto.render(batch);
            btnDisp.render(batch);
            btnPausa.render(batch);
        }
        batch.end();

        if(estadoJuego==EstadosJuego.PAUSADO && escenaPausa !=null){
            escenaPausa.draw();
        }
        //HITBOXES DEL PERSONAJE
        RuiHitbox.setPosition(x,y);
        System.out.println(RuiHitbox.overlaps(SlimeHitbox));

    }

    // Divide el escenario en 3 partes, el inicio, medio y final
    // Cambia la forma en que la camara interactua dependiendo de la zona
    // Y crea limites en X y en Y que cubran solo el mapa
    private void actualizarCamara() {
        float posX = rui.getX();
        float posY = rui.getY();
        // Si está en la parte 'media'
        camara.position.set((int)posX,posY,0);
        //Comportamiento del inicio a la parte media
        if(posX<=Juego.ANCHO_CAMARA/2)
        {
            if(posY<=ALTO_MAPA-Juego.ALTO_CAMARA)
            {
                camara.position.set(Juego.ANCHO_CAMARA/2,ALTO_MAPA-Juego.ALTO_CAMARA,0);
            }
            else
            {
                camara.position.set(Juego.ANCHO_CAMARA/2,posY,0);
            }

        }
        //Comportamiento de la parte media al final
        if(posX>=Juego.ANCHO_CAMARA/2 && posX+Juego.ANCHO_CAMARA/2<=ANCHO_MAPA)
        {
            if(posY<=ALTO_MAPA-Juego.ALTO_CAMARA)
            {
                camara.position.set((int)posX, ALTO_MAPA-Juego.ALTO_CAMARA,0);
            }
            else
            {
                camara.position.set((int)posX, posY,0);
            }
        }
        //Comportamiento de la parte final del mapa
        if(posX+Juego.ANCHO_CAMARA/2>=ANCHO_MAPA)
        {
            if(posY<=ALTO_MAPA-Juego.ALTO_CAMARA)
            {
                camara.position.set(ANCHO_MAPA-Juego.ANCHO_CAMARA/2,ALTO_MAPA-Juego.ALTO_CAMARA,0);
            }
            else
            {
                camara.position.set(ANCHO_MAPA-Juego.ANCHO_CAMARA/2,posY,0);
            }
        }
        camara.update();
        xFondo = camara.position.x - Juego.ANCHO_CAMARA/2;
        yFondo = camara.position.y - Juego.ALTO_CAMARA/2;
    }

    private void actualizar(float delta) {
        if (estadoJuego==EstadosJuego.JUGANDO){
           // actualizarFondo();
           // actualizarGoombas(delta);
            actualizarDisparo(delta);
        }

    }

    private void actualizarDisparo(float delta) {
        for (int i=arrBolas.size-1;i>=0;i--){
            Disparo bolaFuego = arrBolas.get(i);
            bolaFuego.mover(delta);
            //Prueba si la bola debe desaparecer
            if(bolaFuego.getX()>1280){
                //borrar el objeto
                arrBolas.removeIndex(i);
            }
        }
    }

    /*
    Movimiento del personaje.
     */
    private void moverPersonaje() {
        // Prueba caída libre inicial o movimiento horizontal
        switch (rui.getEstadoMovimiento()) {
            case INICIANDO:     // Mueve el personaje en Y hasta que se encuentre sobre un bloque
                // Los bloques en el mapa son de 16x16
                // Calcula la celda donde estaría después de moverlo
                int celdaX = (int)(rui.getX()/ TAM_CELDA);
                int celdaY = (int)((rui.getY()+ rui.VELOCIDAD_Y)/ TAM_CELDA);
                // Recuperamos la celda en esta posición
                // La capa 0 es el fondo
                TiledMapTileLayer capa = (TiledMapTileLayer)mapa.getLayers().get(1);
                TiledMapTileLayer.Cell celda = capa.getCell(celdaX, celdaY);
                // probar si la celda está ocupada
                if (celda==null) {
                    // Celda vacía, entonces el personaje puede avanzar
                    rui.caer();
                }  else {
                    // Dejarlo sobre la celda que lo detiene
                    rui.setPosicion(rui.getX(), (celdaY + 1) * TAM_CELDA);
                    rui.setEstadoMovimiento(Personaje.EstadoMovimiento.QUIETO);
                }
                break;
            case MOV_DERECHA:       // Se mueve horizontal
            case MOV_IZQUIERDA:
                probarChoqueParedes();      // Prueba si debe moverse
                break;
        }

        // Prueba si debe caer por llegar a un espacio vacío
        if ( rui.getEstadoMovimiento()!= Personaje.EstadoMovimiento.INICIANDO
                && (rui.getEstadoSalto() != Personaje.EstadoSalto.SUBIENDO) ) {
            // Calcula la celda donde estaría después de moverlo
            int celdaX = (int) (rui.getX() / TAM_CELDA);
            int celdaY = (int) ((rui.getY() + rui.VELOCIDAD_Y) / TAM_CELDA);
            // Recuperamos la celda en esta posición
            // La capa 0 es el fondo
            TiledMapTileLayer capa = (TiledMapTileLayer) mapa.getLayers().get(1);
            TiledMapTileLayer.Cell celdaAbajo = capa.getCell(celdaX, celdaY);
            TiledMapTileLayer.Cell celdaDerecha = capa.getCell(celdaX+1, celdaY);
            // probar si la celda está ocupada
            if ( (celdaAbajo==null && celdaDerecha==null)  ) {
                // Celda vacía, entonces el personaje puede avanzar
                rui.caer();
                rui.setEstadoSalto(Personaje.EstadoSalto.CAIDA_LIBRE);
            } else {
                // Dejarlo sobre la celda que lo detiene
                rui.setPosicion(rui.getX(), (celdaY + 1) * TAM_CELDA);
                rui.setEstadoSalto(Personaje.EstadoSalto.EN_PISO);
            }
        }

        // Saltar
        switch (rui.getEstadoSalto()) {
            case SUBIENDO:
            case BAJANDO:
                rui.actualizarSalto();    // Actualizar posición en 'y'
                break;
        }
    }


    // Prueba si puede moverse a la izquierda o derecha
    private void probarChoqueParedes() {
        Personaje.EstadoMovimiento estado = rui.getEstadoMovimiento();

        float px = rui.getX();    // Posición actual
        // Posición después de actualizar
        px = rui.getEstadoMovimiento()==Personaje.EstadoMovimiento.MOV_DERECHA? px+Personaje.VELOCIDAD_X+35:
                px-Personaje.VELOCIDAD_X;
        int celdaX = (int)(px/TAM_CELDA);   // Casilla del personaje en X
        if (rui.getEstadoMovimiento()== Personaje.EstadoMovimiento.MOV_DERECHA) {
            celdaX++;   // Casilla del lado derecho
        }
        int celdaY = (int)(rui.getY()/TAM_CELDA); // Casilla del personaje en Y

        TiledMapTileLayer capaPlataforma = (TiledMapTileLayer) mapa.getLayers().get(1);

        if ( capaPlataforma.getCell(celdaX,celdaY) != null || capaPlataforma.getCell(celdaX,celdaY+1) != null ) {
                rui.setEstadoMovimiento(Personaje.EstadoMovimiento.QUIETO);
        } else {
            rui.actualizar();
        }
    }


    private void borrarPantalla() {
        Gdx.gl.glClearColor(0.42f, 0.55f, 1, 1);    // r, g, b, alpha
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
    }

    @Override
    public void resize(int width, int height) {
        vista.update(width,height);
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    // Libera los assets
    @Override
    public void dispose() {
        // Los assets se liberan a través del assetManager
        AssetManager assetManager = juego.getAssetManager();
        assetManager.unload("RUIS-Sheet.png");
        assetManager.unload("derecha.png");
        assetManager.unload("salto.png");
        assetManager.unload("disparo.png");
        assetManager.unload("izquierda.png");
        assetManager.unload("Mapa.tmx");
        assetManager.unload("nubes.png");
    }

    /*
    Clase utilizada para manejar los eventos de touch en la pantalla
     */
    public class ProcesadorEntrada extends InputAdapter
    {
        private Vector3 coordenadas = new Vector3();
        private float x, y;     // Las coordenadas en la pantalla

        /*
        Se ejecuta cuando el usuario PONE un dedo sobre la pantalla, los dos primeros parámetros
        son las coordenadas relativas a la pantalla física (0,0) en la esquina superior izquierda
        pointer - es el número de dedo que se pone en la pantalla, el primero es 0
        button - el botón del mouse
         */
        @Override
        public boolean touchDown(int screenX, int screenY, int pointer, int button) {
            transformarCoordenadas(screenX, screenY);
            if (estadoJuego==EstadosJuego.JUGANDO) {
                // Preguntar si las coordenadas están sobre el botón derecho
                if (btnDerecha.contiene(x, y) && rui.getEstadoMovimiento() != Personaje.EstadoMovimiento.INICIANDO) {
                    // Tocó el botón derecha, hacer que el personaje se mueva a la derecha
                    rui.setEstadoMovimiento(Personaje.EstadoMovimiento.MOV_DERECHA);
                } else if (btnIzquierda.contiene(x, y) && rui.getEstadoMovimiento() != Personaje.EstadoMovimiento.INICIANDO) {
                    // Tocó el botón izquierda, hacer que el personaje se mueva a la izquierda
                    rui.setEstadoMovimiento(Personaje.EstadoMovimiento.MOV_IZQUIERDA);
                } else if (btnSalto.contiene(x, y)) {
                    // Tocó el botón saltar
                    rui.saltar();
                } else if ((btnPausa.contiene(x, y))) {

                    if (escenaPausa==null){//INICIALIZACION LAZY
                        escenaPausa=new EscenaPausa(vista);

                    }
                    estadoJuego=EstadosJuego.PAUSADO;
                    //CAMBIAR PROCESADOR
                    Gdx.input.setInputProcessor(escenaPausa);
                }else if (btnDisp.contiene(x,y)){
                    //Dispara
                    Disparo bolaFuego = new Disparo(texturaBola,rui.getSprite().getX()+40,rui.getSprite().getY()+10);
                    arrBolas.add(bolaFuego);
                }
            } else if (estadoJuego==EstadosJuego.GANO) {
                if (btnGana.contiene(x,y)) {
                    Gdx.app.exit();
                }
            }
            return true;    // Indica que ya procesó el evento
        }

        /*
        Se ejecuta cuando el usuario QUITA el dedo de la pantalla.
         */
        @Override
        public boolean touchUp(int screenX, int screenY, int pointer, int button) {
            transformarCoordenadas(screenX, screenY);
            // Preguntar si las coordenadas son de algún botón para DETENER el movimiento
            if ( rui.getEstadoMovimiento()!= Personaje.EstadoMovimiento.INICIANDO && (btnDerecha.contiene(x, y) || btnIzquierda.contiene(x,y)) ) {
                // Tocó el botón derecha, hacer que el personaje se mueva a la derecha
                rui.setEstadoMovimiento(Personaje.EstadoMovimiento.QUIETO);
            }
            return true;    // Indica que ya procesó el evento
        }


        // Se ejecuta cuando el usuario MUEVE el dedo sobre la pantalla
        @Override
        public boolean touchDragged(int screenX, int screenY, int pointer) {
            transformarCoordenadas(screenX, screenY);
            // Acaba de salir de las fechas (y no es el botón de salto)
            if (x<Juego.ANCHO_CAMARA/2 && rui.getEstadoMovimiento()!= Personaje.EstadoMovimiento.QUIETO) {
                if (!btnIzquierda.contiene(x, y) && !btnDerecha.contiene(x, y) ) {
                    rui.setEstadoMovimiento(Personaje.EstadoMovimiento.QUIETO);
                }
            }
            return true;
        }


        private void transformarCoordenadas(int screenX, int screenY) {
            // Transformar las coordenadas de la pantalla física a la cámara HUD
            coordenadas.set(screenX, screenY, 0);
            camaraHUD.unproject(coordenadas);
            // Obtiene las coordenadas relativas a la pantalla virtual
            x = coordenadas.x;
            y = coordenadas.y;
        }
    }
    private class EscenaPausa extends Stage {

        private Texture textureFondo;
        public  EscenaPausa(Viewport vista){
            super(vista);//pasa la vista al constructor stage
            textureFondo=new Texture("btones.png");
            Image imgeFondo=new Image(textureFondo);
            imgeFondo.setPosition(1280/2,720/2+400, Align.center);
            addActor(imgeFondo);
            //Boton continuar
            Texture textureBtn=new Texture("Cont_bot.png");
            TextureRegionDrawable trd = new TextureRegionDrawable(textureBtn);
            Button btn = new Button(trd);
            addActor(btn);
            btn.setPosition(camara.position.x - Juego.ANCHO_CAMARA/2 + (1280/2-130),(720/2+400));
            btn.addListener(new ClickListener(){
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    super.clicked(event, x, y);
                    //QUITAR Pausa
                    estadoJuego=EstadosJuego.JUGANDO;
                    Gdx.input.setInputProcessor(procesadorEntrada);
                }
            });
        }

    }
    public enum EstadosJuego {
        GANO,
        JUGANDO,
        PAUSADO,
        PERDIO
    }
}
