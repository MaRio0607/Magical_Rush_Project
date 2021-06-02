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
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.badlogic.gdx.utils.viewport.Viewport;


    public class NivelDos implements Screen

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
    private Texture texturaEnemigo;
    private Personaje rui;
    private Slime slime;
    public static final int TAM_CELDA = 16;

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

    //Botón Continuar
    private Texture continuar;
    private Boton btnCont;

    //Botón Menu
    private Texture menu;
    private Boton btnMenu;

    //Botón Reiniciar
    private Texture reiniciar;
    private Boton btnReiniciar;

    // Botón Disparo
    private Texture texturePausa;
    private Boton btnPausa;

    private ProcesadorEntrada procesadorEntrada;

    // Estados del juego
    private EstadosJuego estadoJuego= EstadosJuego.JUGANDO;

    //Escenario Hitbox
    private int re;
    private int rm;
    private int ri;
    private float reI;
    private float reS;
    private float reM;

    //Items
    private Texture keyTexture;
    private Texture vidaTexture;
    private Texture energiaTexture;
    private Texture puertaTexture;

    private Array<Item> arrItem;
    private Item key, key2, key3;
    private Item vida, vidaS, vidaT;
    private Item energia, energiaS, energiaT, energiaF, energiaFt;
    private Item puerta;
    private int keyCount;
    private int keyNeed;

    private Array<Rectangle> arrHitbox;
    private Rectangle r1 , r2, r3, r4, r5;
    private Rectangle r6, r7, r8, r9, r10, r11;
    private Rectangle r12, r13, r14, r15, r16, r17;
    private Rectangle r18, r19, r20, r21, r22, r23, r24, r25, r26, r27;
    private Rectangle marcador, marcador0;

    private Texture vida0, vida1, vida2,vida3;
    private Texture energia0, energia1, energia2, energia3, energia4, energia5;
    private Texture Llave0, Llave1, Llave2, Llave3;

    //Pausa
    private Texture pantallaPausa;

    //Ganar
    private Texture pantallaGana;

    //Perder
    private Texture pantallaPierde;

    // Si && No
    private Texture Si;
    private Texture No;
    private Boton btnSi;
    private Boton btnNo;

    // Fondo
    private Texture texturaNubes;
    private float xFondo = 0;
    private float yFondo = 0;

    //sonido
    private Sound Ssalto;
    private Sound Sdisp;
    private Sound Smov;
    private Sound Sitem;

    public NivelDos(Juego juego) {
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

        Ssalto=Gdx.audio.newSound(Gdx.files.internal("Audio/Rui/saltoRui.mp3"));
        Sdisp=Gdx.audio.newSound(Gdx.files.internal("Audio/Rui/Atack.mp3"));
        Smov=Gdx.audio.newSound(Gdx.files.internal("Audio/Rui/pasosRui.mp3"));
        Sitem=Gdx.audio.newSound(Gdx.files.internal("Audio/Rui/Item.mp3"));

        //Dibuja rectangulos que son usados para hitboxes
        r1 = new Rectangle(0, 1500, 1052, 400);
        r2 = new Rectangle(1155, 1503, 440, 250);
        r3 = new Rectangle(1005, 1185, 600, 150);
        r4 = new Rectangle(750, 1055, 300, 10);
        r5 = new Rectangle(85, 1185, 705, 200);
        r6 = new Rectangle(1500, 1075, 100, 10);
        r7 = new Rectangle(0, 735, 1600, 10);

        r8 = new Rectangle(1050, 1490, 50, 10);
        r9 = new Rectangle(1140, 1503, 50, 10);
        r10 = new Rectangle(1100, 1440, 60, 10);
        r11 = new Rectangle(1050, 1395, 50, 10);
        r12 = new Rectangle(1100, 1345, 60, 10);
        r13 = new Rectangle(1050, 1295, 50, 10);
        r14 = new Rectangle(1100, 1245, 85, 10);
        r15 = new Rectangle(1200, 1220, 50, 10);

        r16 = new Rectangle(790, 1150, 50, 10);
        r17 = new Rectangle(837, 1120, 67, 10);
        r18 = new Rectangle(893, 1088, 70, 10);

        r19 = new Rectangle(0, 1153, 50, 10);
        r20 = new Rectangle(0, 1123, 50, 10);
        r21 = new Rectangle(0, 1148, 50, 10);
        r22 = new Rectangle(0, 1085, 50, 10);
        r23 = new Rectangle(0, 1022, 50, 10);
        r24 = new Rectangle(0, 959, 50, 10);
        r25 = new Rectangle(0, 896, 50, 10);
        r26 = new Rectangle(0, 833, 50, 10);
        r27 = new Rectangle(0, 770, 50, 10);

        marcador = new Rectangle(1601, 647, 20, 10);
        marcador0 = new Rectangle(-100,730, 50,161);

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
        assetManager.load("Cont_bot3.png",Texture.class);
        assetManager.load("Cont_bot5.png",Texture.class);
        assetManager.load("SHOOT.png",Texture.class);
        assetManager.load("btones.png",Texture.class);
        assetManager.load("PausaBoton.png",Texture.class);
        assetManager.load("fondo.png",Texture.class);
        //ITEM
        assetManager.load("Key_Item.png", Texture.class);
        assetManager.load("Vida_Item.png", Texture.class);
        assetManager.load("Energia_Item.png", Texture.class);
        assetManager.load("puerta.png", Texture.class);

        assetManager.load("Vida_0.png", Texture.class);
        assetManager.load("Vida_1.png", Texture.class);
        assetManager.load("Vida_2.png", Texture.class);
        assetManager.load("Vida_3.png", Texture.class);

        assetManager.load("Energia_0.png", Texture.class);
        assetManager.load("Energia_1.png", Texture.class);
        assetManager.load("Energia_2.png", Texture.class);
        assetManager.load("Energia_3.png", Texture.class);
        assetManager.load("Energia_4.png", Texture.class);
        assetManager.load("Energia_5.png", Texture.class);

        assetManager.load("Llave_0.png", Texture.class);
        assetManager.load("Llave_1.png", Texture.class);
        assetManager.load("Llave_2.png", Texture.class);
        assetManager.load("Llave_3.png", Texture.class);

        assetManager.load("btones.png", Texture.class);

        assetManager.load("continuar_nivel.png", Texture.class);
        assetManager.load("despertaste_continuar.png", Texture.class);
        assetManager.load("Si.png", Texture.class);
        assetManager.load("No.png", Texture.class);

        // Se bloquea hasta que cargue todos los recursos
        assetManager.finishLoading();
    }

    private void crearObjetos() {
        AssetManager assetManager = juego.getAssetManager();   // Referencia al assetManager
        //fondo
        texturaNubes = assetManager.get("fondo.png");
        // Carga el mapa en memoria
        mapa = assetManager.get("SegundoNivel.tmx");

        // Crear el objeto que dibujará el mapa
        rendererMapa = new OrthogonalTiledMapRenderer(mapa,batch);
        rendererMapa.setView(camara);

        // Cargar frames
        texturaPersonaje = assetManager.get("RUIS-Sheet.png");

        // Crear el personaje
        rui = new Personaje(texturaPersonaje);
        //Variables para conocer la posición del personaje en el mapa
        re = 0;
        rm = 0;
        ri = 0;
        reI =1600;
        reS = 0;
        reM = 1600;

        rui.setVida(2);
        rui.setEnergia(0);
        keyCount=0;
        keyNeed=3;

        //ITEM
        keyTexture = assetManager.get("Key_Item.png");
        vidaTexture = assetManager.get("Vida_Item.png");
        energiaTexture = assetManager.get("Energia_Item.png");
        puertaTexture = assetManager.get("puerta.png");

        vida0 = assetManager.get("Vida_0.png");
        vida1 = assetManager.get("Vida_1.png");
        vida2 = assetManager.get("Vida_2.png");
        vida3 = assetManager.get("Vida_3.png");

        energia0 = assetManager.get("Energia_0.png");
        energia1 = assetManager.get("Energia_1.png");
        energia2 = assetManager.get("Energia_2.png");
        energia3 = assetManager.get("Energia_3.png");
        energia4 = assetManager.get("Energia_4.png");
        energia5 = assetManager.get("Energia_5.png");

        Llave0 = assetManager.get("Llave_0.png");
        Llave1 = assetManager.get("Llave_1.png");
        Llave2 = assetManager.get("Llave_2.png");
        Llave3 = assetManager.get("Llave_3.png");

        pantallaPausa = assetManager.get("btones.png");
        pantallaGana = assetManager.get("continuar_nivel.png");
        pantallaPierde = assetManager.get("despertaste_continuar.png");
        Si = assetManager.get("Si.png");
        No = assetManager.get("No.png");


        key = new Item(keyTexture, 820, 1000, 1);
        key2 = new Item(keyTexture, 1550, 1150,1);
        key3 = new Item(keyTexture, 1300, 700, 1);

        vida = new Item(vidaTexture, 1550, 1465, 2);
        vidaS = new Item(vidaTexture, 20, 710, 2);
        vidaT = new Item(vidaTexture, 150, 1135, 2);

        energia = new Item(energiaTexture, 250, 1465, 3);
        energiaS = new Item(energiaTexture, 1000, 1465, 3);
        energiaT = new Item(energiaTexture, 1150, 1200, 3);
        energiaF = new Item(energiaTexture, 750, 1135, 3);
        energiaFt = new Item(energiaTexture, 20, 1100, 3);

        puerta = new Item(puertaTexture, 1412,670,4);

        // Posición inicial del personaje
        rui.setPosicion(rui.getX(),1600);
        rui.setCL(true);
        rui.setCR(true);

        // Crear los botones
        continuar = assetManager.get("Cont_bot.png");
        btnCont = new Boton(continuar);
        menu = assetManager.get("Cont_bot5.png");
        btnMenu = new Boton(menu);
        reiniciar = assetManager.get("Cont_bot3.png");
        btnReiniciar = new Boton(reiniciar);
        btnSi = new Boton(Si);
        btnNo = new Boton(No);

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

    }

    /*
    Dibuja TODOS los elementos del juego en la pantalla.
    Este método se está ejecutando muchas veces por segundo.
     */
    @Override
    public void render(float delta) { // delta es el tiempo entre frames (Gdx.graphics.getDeltaTime())
        actualizar(delta);
        if (estadoJuego!= EstadosJuego.PERDIO && estadoJuego!= EstadosJuego.PAUSADO) {
            // Actualizar objetos en la pantalla
            moverPersonaje();
            actualizarCamara(); // Mover la cámara para que siga al personaje
            probarChoqueParedes();
            actualizarItems();
        }

        // Dibujar
        borrarPantalla();

        batch.setProjectionMatrix(camara.combined);

        //Dibujar fondo
        batch.begin();
        batch.draw(texturaNubes, xFondo, yFondo);
        batch.end();

        stats();


        rendererMapa.setView(camara);
        rendererMapa.render();  // Dibuja el mapa

        // Entre begin-end dibujamos nuestros objetos en pantalla
        batch.begin();
        UI(batch);

        key.render(batch);
        key2.render(batch);
        key3.render(batch);

        vida.render(batch);
        vidaS.render(batch);
        vidaT.render(batch);

        energia.render(batch);
        energiaS.render(batch);
        energiaT.render(batch);
        energiaF.render(batch);
        energiaFt.render(batch);

        puerta.render(batch);

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
        if (estadoJuego== EstadosJuego.GANO) {
            pantallaGana(batch);
        }
        if (estadoJuego== EstadosJuego.PERDIO)
        {
            pantallaPerdio(batch);
        }
        else {
            btnIzquierda.render(batch);
            btnDerecha.render(batch);
            btnSalto.render(batch);
            btnDisp.render(batch);
            btnPausa.render(batch);
            pantallaPausa(batch);
        }
        batch.end();

    }

    private void pantallaPerdio(SpriteBatch batch)
    {
        batch.draw(pantallaPierde, (pantallaPierde.getWidth()-(pantallaPierde.getWidth()/3)),(pantallaPierde.getHeight()-(pantallaPierde.getHeight()/2)));
        btnSi.setPosicion( (pantallaGana.getWidth()-(pantallaGana.getWidth()/3) + Si.getWidth()*3) , (pantallaGana.getHeight()-(pantallaGana.getHeight()/2) + Si.getWidth()) );
        btnSi.render(batch);
        btnNo.setPosicion( (pantallaGana.getWidth()-(pantallaGana.getWidth()/3) + Si.getWidth()*8) , (pantallaGana.getHeight()-(pantallaGana.getHeight()/2) + Si.getWidth()) );
        btnNo.render(batch);
    }

    private void pantallaGana(SpriteBatch batch) {

        batch.draw(pantallaGana, (pantallaGana.getWidth()-(pantallaGana.getWidth()/3)),(pantallaGana.getHeight()-(pantallaGana.getHeight()/2)));
        btnSi.setPosicion( (pantallaGana.getWidth()-(pantallaGana.getWidth()/3) + Si.getWidth()*3) , (pantallaGana.getHeight()-(pantallaGana.getHeight()/2) + Si.getWidth()) );
        btnSi.render(batch);
        btnNo.setPosicion( (pantallaGana.getWidth()-(pantallaGana.getWidth()/3) + Si.getWidth()*8) , (pantallaGana.getHeight()-(pantallaGana.getHeight()/2) + Si.getWidth()) );
        btnNo.render(batch);

    }

    private void pantallaPausa(SpriteBatch batch) {
        if(estadoJuego == EstadosJuego.PAUSADO)
        {
            batch.draw(pantallaPausa, (pantallaPausa.getWidth()-(pantallaPausa.getWidth()/4)), (pantallaPausa.getHeight()-(pantallaPausa.getHeight()/2)));

            btnCont.setPosicion( (pantallaPausa.getWidth()-(pantallaPausa.getWidth()/8) + continuar.getWidth()/8), (pantallaPausa.getHeight()-(pantallaPausa.getHeight()/2) + continuar.getHeight()*4 ) );
            btnCont.render(batch);
            btnMenu.setPosicion( (pantallaPausa.getWidth()-(pantallaPausa.getWidth()/8) + continuar.getWidth()/8), ((pantallaPausa.getHeight()-(pantallaPausa.getHeight()/2) + continuar.getHeight()*4 ))-180 );
            btnMenu.render(batch);
            btnReiniciar.setPosicion( (pantallaPausa.getWidth()-(pantallaPausa.getWidth()/8) + continuar.getWidth()/8), ((pantallaPausa.getHeight()-(pantallaPausa.getHeight()/2) + continuar.getHeight()*4 ))-90 );
            btnReiniciar.render(batch);
        }


    }

    private void UI(SpriteBatch batch) {

        switch (rui.getVida()){
            case 0:
                batch.draw(vida0, (camara.position.x-(Juego.ANCHO_CAMARA/2)+30), camara.position.y+(Juego.ALTO_CAMARA/2)-70);
                break;
            case 1:
                batch.draw(vida1, (camara.position.x-(Juego.ANCHO_CAMARA/2)+30), camara.position.y+(Juego.ALTO_CAMARA/2)-70);
                break;
            case 2:
                batch.draw(vida2, (camara.position.x-(Juego.ANCHO_CAMARA/2)+30), camara.position.y+(Juego.ALTO_CAMARA/2)-70);
                break;
            case 3:
                batch.draw(vida3, (camara.position.x-(Juego.ANCHO_CAMARA/2)+30), camara.position.y+(Juego.ALTO_CAMARA/2)-70);
                break;
        }

        switch (rui.getEnergia()){
            case 0:
                batch.draw(energia0, (camara.position.x-(Juego.ANCHO_CAMARA/2)+320), camara.position.y+(Juego.ALTO_CAMARA/2)-75);
                break;
            case 1:
                batch.draw(energia1, (camara.position.x-(Juego.ANCHO_CAMARA/2)+320), camara.position.y+(Juego.ALTO_CAMARA/2)-75);
                break;
            case 2:
                batch.draw(energia2, (camara.position.x-(Juego.ANCHO_CAMARA/2)+320), camara.position.y+(Juego.ALTO_CAMARA/2)-75);
                break;
            case 3:
                batch.draw(energia3, (camara.position.x-(Juego.ANCHO_CAMARA/2)+320), camara.position.y+(Juego.ALTO_CAMARA/2)-75);
                break;
            case 4:
                batch.draw(energia4, (camara.position.x-(Juego.ANCHO_CAMARA/2)+320), camara.position.y+(Juego.ALTO_CAMARA/2)-75);
                break;
            case 5:
                batch.draw(energia5, (camara.position.x-(Juego.ANCHO_CAMARA/2)+320), camara.position.y+(Juego.ALTO_CAMARA/2)-75);
                break;
        }

        switch (keyCount){
            case 0:
                batch.draw(Llave0, (camara.position.x-(Juego.ANCHO_CAMARA/2)+950), camara.position.y+(Juego.ALTO_CAMARA/2)-80);
                break;
            case 1:
                batch.draw(Llave1, (camara.position.x-(Juego.ANCHO_CAMARA/2)+950), camara.position.y+(Juego.ALTO_CAMARA/2)-80);
                break;
            case 2:
                batch.draw(Llave2, (camara.position.x-(Juego.ANCHO_CAMARA/2)+950), camara.position.y+(Juego.ALTO_CAMARA/2)-80);
                break;
            case 3:
                batch.draw(Llave3, (camara.position.x-(Juego.ANCHO_CAMARA/2)+950), camara.position.y+(Juego.ALTO_CAMARA/2)-80);
                break;
        }


    }

    private void actualizarItems() {
        arrItem=new Array<>();
        arrItem.add(key);
        arrItem.add(key2);
        arrItem.add(key3);

        arrItem.add(vida);
        arrItem.add(vidaS);
        arrItem.add(vidaT);

        arrItem.add(energia);
        arrItem.add(energiaS);
        arrItem.add(energiaT);
        arrItem.add(energiaF);
        arrItem.add(energiaFt);

        arrItem.add(puerta);
        for (int i=0; i<arrItem.size; i++) {
            Item it = arrItem.get(i);
            if( ((rui.getX()+52) >= it.getX()) && ((rui.getX() < it.getX()+30)) )
            {
                if(((rui.getY()+72) >= it.getY()) && (rui.getY()-70 < it.getY()) )
                {
                    if(it.getTipo() == 1)
                    {
                        Sitem.play();
                        keyCount ++;
                        it.setPosicion(-100,it.getY());
                        arrItem.removeIndex(i);
                    }
                    if(it.getTipo() == 2)
                    {
                        Sitem.play();
                        rui.setVida(rui.getVida()+1);
                        it.setPosicion(-100,it.getY());
                        arrItem.removeIndex(i);
                    }
                    if(it.getTipo() == 3)
                    {
                        Sitem.play();
                        rui.setEnergia(rui.getEnergia()+1);
                        it.setPosicion(-100,it.getY());
                        arrItem.removeIndex(i);
                    }
                    if( it.getTipo() == 4 && keyCount == keyNeed )
                    {
                        estadoJuego = EstadosJuego.GANO;
                    }

                }
            }
        }

    }

    private void stats(){

        if(rui.getVida() == 0)
        {
            estadoJuego = EstadosJuego.PERDIO;
        }
        if(rui.getVida() >= 3)
        {
            rui.setVida(3);
        }
        if(rui.getEnergia() >= 5)
        {
            rui.setEnergia(5);
        }

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
        if (estadoJuego== EstadosJuego.JUGANDO){
            actualizarDisparo(delta);
        }

    }

    private void actualizarDisparo(float delta) {
        for (int i=arrBolas.size-1;i>=0;i--){
            Disparo bolaFuego = arrBolas.get(i);
            bolaFuego.mover(delta);
            //Prueba si la bola debe desaparecer
            if(bolaFuego.getX()>=ANCHO_MAPA || bolaFuego.getX()<=0){
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
            case INICIANDO:
                rui.setEstadoMovimiento(Personaje.EstadoMovimiento.QUIETO);
                break;
            case MOV_DERECHA:
                rui.actualizar();
                break;
            case MOV_IZQUIERDA:
                rui.actualizar();
                break;
        }

        // Saltar
        switch (rui.getEstadoSalto()) {
            case EN_PISO:
                break;
            case SUBIENDO:
                rui.actualizarSalto();
                break;
            case BAJANDO:
                rui.caer();
                break;
        }
    }


    // Prueba si esta chocando con las paredes
    private void probarChoqueParedes() {
        //Agregar los rectangulos del mapa al array
        arrHitbox=new Array<>();
        arrHitbox.add(r1);
        arrHitbox.add(r2);
        arrHitbox.add(r3);
        arrHitbox.add(r4);
        arrHitbox.add(r5);
        arrHitbox.add(r6);
        arrHitbox.add(r7);
        arrHitbox.add(r8);
        arrHitbox.add(r9);
        arrHitbox.add(r10);
        arrHitbox.add(r11);
        arrHitbox.add(r12);
        arrHitbox.add(r13);
        arrHitbox.add(r14);
        arrHitbox.add(r15);
        arrHitbox.add(r16);
        arrHitbox.add(r17);
        arrHitbox.add(r18);
        arrHitbox.add(r19);
        arrHitbox.add(r20);
        arrHitbox.add(r21);
        arrHitbox.add(r22);
        arrHitbox.add(r23);
        arrHitbox.add(r24);
        arrHitbox.add(r25);
        arrHitbox.add(r26);
        arrHitbox.add(r27);
        arrHitbox.add(marcador);
        arrHitbox.add(marcador0);
        //Verifica si esta dentro de las x de alguno de los rectangulos
        //En caso de que si checa cual es el mas cercano a el personaje debajo de este
        //Ese rectangulo es con el que se maneja la gravedad
        //verifica cuales son los rectangulos mas cercanos a su izquierda y derecha
        //y bloquea al jugador cuando llega a la coordenada mas cercana
        for (int i=0; i<arrHitbox.size; i++){
            Rectangle r = arrHitbox.get(i);
            if(rui.getX() >= r.getX() && rui.getX()+52 < (r.getX()+r.getWidth()+52) )
            {
                if (r.getY() >= reS && (r.getY() < (rui.getY()+70)))
                {
                    reS = r.getY();
                    re = i;
                }
            }
            if((rui.getX()+7) < r.getX())
            {
                if((r.getX() < reM))
                {
                    reM = r.getX();
                    rm = i;
                }
            }
            if( (r.getX()+r.getWidth()) < rui.getX() )
            {
                if( ((rui.getY()+64) <= r.getY()) && ((rui.getY()+70) >= (r.getY()-r.getHeight())) )
                {
                    if( ((rui.getX()) - (r.getX()+r.getWidth())) < reI)
                    {
                        reI = ((r.getX()+r.getWidth()));
                        ri = i;
                    }
                }
            }
        }
        reS = -200;
        reM = 1800;
        reI = 1800;

        Rectangle r = arrHitbox.get(re);
        Rectangle rem = arrHitbox.get(rm);
        Rectangle rei = arrHitbox.get(ri);

        if(rui.getY()+64 <= r.getY() && rui.getEstadoSalto() != Personaje.EstadoSalto.SUBIENDO)
        {
            rui.setEstadoSalto(Personaje.EstadoSalto.EN_PISO);
        }
        if( rui.getY() + 64 > r.getY() && ( rui.getX() >= r.getX() && rui.getX() <= (r.getX()+r.getWidth()+52) ) && rui.getEstadoSalto() != Personaje.EstadoSalto.SUBIENDO)
        {
            rui.setEstadoSalto(Personaje.EstadoSalto.BAJANDO);
        }
        if( rui.getX()+1 >= rem.getX()-10 )
        {
            if (rem.getY() > (rui.getY()+64) && ((rem.getY()-rem.getHeight()) < (rui.getY()+64) ))
            {
                rui.setCR(false);
            }
            else{
                rui.setCR(true);
            }
        }
        else
        {
            rui.setCR(true);
        }
        if( rui.getX()-10 <= ((rei.getX() + rei.getWidth())+5) && (rui.getY()+70 < rei.getY()))
        {
            if(((rui.getY()+70) < (rei.getY()+rei.getHeight())) )
            {
                rui.setCL(true);
            }
            if( (rui.getY() < rei.getY()) && (rui.getY() > rei.getY()-rei.getHeight())  )
            {
                rui.setCL(false);

            }
            else {
                rui.setCL(false);
                if( rui.getY() < 950 )
                {
                    rui.setCL(true);
                }
            }

        }
        else{
            rui.setCL(true);
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
        assetManager.unload("SegundoNivel.tmx");
        assetManager.unload("fondo.png");

        assetManager.unload("Key_Item.png");
        assetManager.unload("Vida_Item.png");
        assetManager.unload("Energia_Item.png");
        assetManager.unload("puerta.png");

        assetManager.unload("Vida_0.png");
        assetManager.unload("Vida_1.png");
        assetManager.unload("Vida_2.png");
        assetManager.unload("Vida_3.png");

        assetManager.unload("Energia_0.png");
        assetManager.unload("Energia_1.png");
        assetManager.unload("Energia_2.png");
        assetManager.unload("Energia_3.png");
        assetManager.unload("Energia_4.png");
        assetManager.unload("Energia_5.png");

        assetManager.unload("Llave_0.png");
        assetManager.unload("Llave_1.png");
        assetManager.unload("Llave_2.png");
        assetManager.unload("Llave_3.png");

        assetManager.unload("btones.png");

        assetManager.unload("continuar_nivel.png");
        assetManager.unload("despertaste_continuar.png");
        assetManager.unload("Si.png");
        assetManager.unload("No.png");

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
            if (estadoJuego== EstadosJuego.JUGANDO) {
                // Preguntar si las coordenadas están sobre el botón derecho
                if (btnDerecha.contiene(x, y) && rui.getEstadoMovimiento() != Personaje.EstadoMovimiento.INICIANDO) {
                    Smov.play();
                    Smov.loop();
                    // Tocó el botón derecha, hacer que el personaje se mueva a la derecha
                    rui.setEstadoMovimiento(Personaje.EstadoMovimiento.MOV_DERECHA);

                } else if (btnIzquierda.contiene(x, y) && rui.getEstadoMovimiento() != Personaje.EstadoMovimiento.INICIANDO) {
                    Smov.play();
                    Smov.loop();
                    // Tocó el botón izquierda, hacer que el personaje se mueva a la izquierda
                    rui.setEstadoMovimiento(Personaje.EstadoMovimiento.MOV_IZQUIERDA);

                } else if (btnSalto.contiene(x, y)) {
                    Ssalto.play();
                    // Tocó el botón saltar
                    rui.saltar();
                } else if ((btnPausa.contiene(x, y))) {
                    estadoJuego= EstadosJuego.PAUSADO;
                }else if (btnDisp.contiene(x,y)){
                    Sdisp.play();
                    //Dispara hacia la derecha si esta viendo en esa dirección
                    if(rui.getLRight() == true)
                    {
                        Disparo bolaFuego = new Disparo(texturaBola,rui.getSprite().getX()+40,rui.getSprite().getY()+10);
                        bolaFuego.setRight(rui.getLRight());
                        arrBolas.add(bolaFuego);
                    }
                    //Dispara hacia la izquierda si esta viendo en esa dirección
                    if(rui.getLRight() == false)
                    {
                        Disparo bolaFuego = new Disparo(texturaBola,rui.getSprite().getX()-20,rui.getSprite().getY()+10);
                        bolaFuego.setRight(rui.getLRight());
                        arrBolas.add(bolaFuego);
                    }
                }
            } else if (estadoJuego == EstadosJuego.PAUSADO){
                if(btnCont.contiene(x,y)){
                    estadoJuego= EstadosJuego.JUGANDO;
                }if(btnMenu.contiene(x,y)){
                    juego.detener();
                    juego.setScreen(new Menu(juego));

                    estadoJuego= EstadosJuego.PERDIO;
                }if(btnReiniciar.contiene(x,y)){
                    Smov.stop();
                    juego.setScreen(new NivelDos(juego));
                    estadoJuego= EstadosJuego.JUGANDO;
                }
            }  else if (estadoJuego== EstadosJuego.PERDIO) {
                if(btnSi.contiene(x,y)){
                    Smov.stop();
                    juego.setScreen(new NivelDos(juego));
                    estadoJuego= EstadosJuego.JUGANDO;
                }
                if(btnNo.contiene(x,y)){
                    juego.detener();
                    juego.setScreen(new Menu(juego));
                    estadoJuego= EstadosJuego.PERDIO;
                }
            }  else if (estadoJuego== EstadosJuego.GANO) {
                if(btnSi.contiene(x,y)){
                    Smov.stop();
                    juego.detener();
                    juego.setScreen(new NivelTres(juego));
                    estadoJuego= EstadosJuego.JUGANDO;
                }
                if(btnNo.contiene(x,y)){
                    Smov.stop();
                    juego.detener();
                    juego.setScreen(new Menu(juego));
                    estadoJuego= EstadosJuego.PERDIO;
                }
            }
            return true;    // Indica que ya procesó el evento
        }

        /*
        Se ejecuta cuando el usuario QUITA el dedo de la pantalla.
         */
        @Override
        public boolean touchUp(int screenX, int screenY, int pointer, int button) {
            if (estadoJuego== NivelDos.EstadosJuego.JUGANDO) {
                // Preguntar si las coordenadas están sobre el botón derecho
                if (btnDerecha.contiene(x, y) && rui.getEstadoMovimiento() != Personaje.EstadoMovimiento.INICIANDO) {
                    Smov.stop();


                } else if (btnIzquierda.contiene(x, y) && rui.getEstadoMovimiento() != Personaje.EstadoMovimiento.INICIANDO) {
                    Smov.stop();

                }
            }
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

    public enum EstadosJuego {
        GANO,
        JUGANDO,
        PAUSADO,
        PERDIO
    }
}
