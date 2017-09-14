/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.yourorghere;

import com.sun.opengl.util.texture.Texture;
import javax.media.opengl.GL;

/**
 * Класс вертолёта
 * @author RichardKelly
 */
public class Helicopter {

    /**
     * Характеристики Ми-28 (начальные параметры для программирования физики)
     * Мощность двигателя:
     * 1. Крейсерский режим: 1500 л.с.
     * 2. Взлетный режим: 2200 л.с. (100%)
     * 3. Холостые обороты: 700 л.с. (0%)
     * Обороты несущего винта: 240 об/мин (8*PI при FPS = 60)
     * Обороты рулевого винта: 1200 об/мин (40*PI при FPS = 60)
     * Масса: 11 тонн
     * 
     * Тяга:
     * 0% - 220 об/мин - 0 кг
     * 50% - 240 об/мин - 11 000 кг
     * 100% - 260 об/мин
     * 
     * Тяга влияет на обороты и на ускорение (линейно). Допустим, что 50% соответствуют 11 тонн.
     */
    
    // Модель вертолёта
    private final Object body;
    // Модель несущего винта
    private final Object mainScrew;
    // Модель рулевого винта
    private final Object rollScrew;

    private static final double toRadian = Math.PI / 180;
    private static final double toDegree = 180 / Math.PI;
    
    // FPS
    public final int FPS = 60;
    
    public Vector3 position;
    public Vector3 angles;

    // Физические параметры вертолета
 
    // Масса
    public final float mass = 11000; // 11 000 кг
    // Максимальная величина вертикальной скорости
    public final float maxSpeedY = 20; // 20 м/с
    
    // Скорость
    public Vector3 speed;
    // Ускорение
    public Vector3 accel;

    // Угловая скорость
    public Vector3 speedAngle;
    // Угловое ускорение
    public Vector3 accelAngle;
    
    // Тяга
    public static float pull = (float) 0;
    // Скорость вращения относительно оси рыскания
    public static float pullAngle = (float) 0.3;

    public Helicopter() {
        body = new Object("heli/Helicopter.obj");
        mainScrew = new Object("heli/MainScrew.obj");
        /* Cinema 4D fix (according to coordinades) */
        // Фикс несущего винта
        mainScrew.position.x = (float) 0.05961;
        mainScrew.position.y = (float) 3.58183;
        mainScrew.position.z = (float) -0.07208;
        mainScrew.angles.x = (float) -3;

        rollScrew = new Object("heli/RollScrew.obj");
        // Фикс рулевого винта
        rollScrew.position.x = (float) -0.51373;
        rollScrew.position.y = (float) 3.06563;
        rollScrew.position.z = (float) -10.9899;

        Texture texture = TextureLoader.load("heli/texture.jpg");
        body.setTexture(texture);
        mainScrew.setTexture(texture);
        rollScrew.setTexture(texture);

        position = new Vector3(0, 0, 0);
        angles = new Vector3(0, 0, 0);

        speed = new Vector3(0, 0, 0);
        accel = new Vector3(0, 0, 0);

        speedAngle = new Vector3(0, 0, 0);
        accelAngle = new Vector3(0, 0, 0);
    }

    /**
     * Физическое воздействие
     */
    public void physImpact() {
        /* Изменение по координатам */
        position.add(speed);
        speed.add(accel);
        // 0.1633 = 9.8 м/с2 / 60 (FPS)
        speed.y -= 9.8 / FPS * 0.21;
        // сопротивление
        speed.mult(0.95);
        // изменение по углу
        angles.add(speedAngle);
        speedAngle.add(accelAngle);
        speedAngle.mult(0.7);

        mainScrew.angles.y += 8 * Math.PI ;//5 ------ 8*Pi
        mainScrew.angles.y += (speed.y > 0) ? 3 : -3; //3

        rollScrew.angles.x += 40 * Math.PI;//10 ------ 40*Pi
        rollScrew.angles.x += (speedAngle.y > 0) ? 3 : -3; //3
        rollScrew.angles.x += (speed.y > 0) ? 2 : -2; //2
    }

    /**
     * Коллизия вертолёта с поверхностью
     * @param height высота
     */
    public void collision(float height) {
        if (height > position.y - 0.3656) {
            position.y += height - position.y + 0.3656;
            speed.y = 0;
        }
    }

    /**
     * Отрисовка
     * @param gl 
     */
    public void draw(GL gl) {
        gl.glPushMatrix();
        gl.glTranslatef(position.x, position.y, position.z);
        gl.glRotated(angles.y, 0, 1, 0);
        gl.glRotated(angles.x, 1, 0, 0);
        gl.glRotated(angles.z, 0, 0, 1);

        body.draw(gl);
        mainScrew.draw(gl);
        rollScrew.draw(gl);

        gl.glPopMatrix();
    }

}
