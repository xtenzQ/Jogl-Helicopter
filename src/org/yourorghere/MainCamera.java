/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.yourorghere;

import javax.media.opengl.GL;

/**
 *
 * @author RichardKelly
 */
public final class MainCamera {
    
    public static Vector3 position = new Vector3(0, 0, 0);
    public static Vector3 angles = new Vector3(0, 0, 0);
    
    private MainCamera(){
        
    }
        
    public static void dispose(GL gl){
        
    }
    
}
