/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.yourorghere;

import com.sun.opengl.util.texture.Texture;
import javax.media.opengl.GL;

/**
 *
 * @author RichardKelly
 */
public class Helicopter {
    
    private final Object body;
    private final Object mainScrew;
    private final Object rollScrew;
    
    public Vector3 position;
    public Vector3 angles;
    
    public Vector3 speed;
    public Vector3 accel;
    
    public Vector3 speedAngle;
    public Vector3 accelAngle;
    
    public Helicopter() {
        body = new Object("heli/Helicopter.obj");
        mainScrew = new Object("heli/MainScrew.obj");
        // Cinema 4D fix
        mainScrew.position.x = (float) 0.05961;
        mainScrew.position.y = (float) 3.58183;
        mainScrew.position.z = (float) -0.07208;
        mainScrew.angles.x = (float) -3;
        
        rollScrew = new Object("heli/RollScrew.obj");
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
    
    public void physImpact() {
        /* Изменение по координатам */
        position.add(speed);       
        speed.add(accel);
        // сопротивление
        speed.mult(0.95);
        // изменение по углу
        angles.add(speedAngle);
        speedAngle.add(accelAngle);
        speedAngle.mult(0.7);
        
        mainScrew.angles.y += 20;//5;
        mainScrew.angles.y += (speed.y > 0) ? 3 : -3; //3
        
        rollScrew.angles.x += 30;//10;
        rollScrew.angles.x += (speedAngle.y > 0) ? 3 : -3; //3
        rollScrew.angles.x += (speed.y > 0) ? 2 : -2; //2
    }
    
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
