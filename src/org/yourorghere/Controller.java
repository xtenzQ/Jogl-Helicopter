/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.yourorghere;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import static org.yourorghere.Helicopter.pull;
import static org.yourorghere.Helicopter.pullAngle;
import static org.yourorghere.Lab6.heli;

/**
 *
 * @author RichardKelly
 */
public class Controller implements KeyListener, MouseListener, MouseMotionListener, MouseWheelListener {

    private int mousePreviousX;
    private int mousePreviousY;

    private static final double toRadian = Math.PI / 180;
    private static final double toDegree = 180 / Math.PI;

    public static boolean yawLeft, yawRight, up, down, forward, back, rollRight, rollLeft;

    public static double speed = 1;

    public static boolean spectator = true;

    private static float distance = 30;

    private static final float pull = (float) 0.1;
    private static final float pullAngle = (float) 0.3;

    public Controller() {
    }

    public static void controll() {
        double sin;
        double cos;
        double angle;
        if (spectator) {
            if (yawLeft || yawRight || forward || back) {
                angle = toRadian * MainCamera.angles.y;
                sin = Math.sin(angle) * speed;
                cos = Math.cos(angle) * speed;
                if (yawRight) {
                    MainCamera.position.x += cos;
                    MainCamera.position.z -= sin;
                }
                if (yawLeft) {
                    MainCamera.position.x -= cos;
                    MainCamera.position.z += sin;
                }
                if (forward) {
                    MainCamera.position.x -= sin;
                    MainCamera.position.z -= cos;
                }
                if (back) {
                    MainCamera.position.x += sin;
                    MainCamera.position.z += cos;
                }
            }
            if (up || down) {
                if (up) {
                    MainCamera.position.y += speed;
                }
                if (down) {
                    MainCamera.position.y -= speed;
                }
            }
        } else {
            heli.accel.mult(0);
            heli.accelAngle.mult(0);

            angle = toRadian * heli.angles.y;
            sin = Math.sin(angle);
            cos = Math.cos(angle);
//            MainCamera.position.x = heli.position.x - (float) (sin) * distance;
//            MainCamera.position.y = heli.position.y + 15;
//            MainCamera.position.z = heli.position.z - (float) (cos) * distance;
//            MainCamera.angles.x = - 20;
//            MainCamera.angles.y = heli.angles.y + 180;
            double cosX = Math.cos(-MainCamera.angles.x * toRadian + Math.PI * 0.5);
            double sinX = Math.sin(-MainCamera.angles.x * toRadian + Math.PI * 0.5);
            double cosY = Math.cos(-MainCamera.angles.y * toRadian - Math.PI * 0.5);
            double sinY = Math.sin(-MainCamera.angles.y * toRadian - Math.PI * 0.5);
            MainCamera.position.x = heli.position.x - (float) (distance * sinX * cosY);
            MainCamera.position.y = heli.position.y - (float) (distance * cosX);
            MainCamera.position.z = heli.position.z - (float) (distance * sinX * sinY);

            Vector3 direction = new Vector3(0, 0, 0);

            // Рыскание вправо
            if (yawRight) {
                heli.accelAngle.y = -(float) pullAngle;
            }
            // Рыскание влево
            if (yawLeft) {
                heli.accelAngle.y = (float) pullAngle;
            }
            if (rollRight) {
                heli.angles.z += (20 - heli.angles.z) * 0.1;
                direction.x += -(float) cos;
                direction.z += (float) sin;
            }
            if (rollLeft) {
                heli.angles.z += (-20 - heli.angles.z) * 0.1;
                direction.x += (float) cos;
                direction.z += -(float) sin;
            }
            if (forward) {
                heli.angles.x += (30 - heli.angles.x) * 0.1;
                direction.x += (float) sin;
                direction.z += (float) cos;
            }
            if (back) {
                heli.angles.x += (-30 - heli.angles.x) * 0.1;
                direction.x += -(float) sin;
                direction.z += -(float) cos;
            }
            if (up) {
                direction.y += (float) 1;
            }
            if (down) {
                direction.y += -(float) 1;
            }
            if (!forward && !back) {
                heli.angles.x *= 0.9;
            }
            if (!rollRight && !rollLeft) {
                heli.angles.z *= 0.9;
            }
            direction.normalize();
            direction.mult(pull);
            heli.accel = direction;
        }
    }

    //KeyListener
    public void keyTyped(KeyEvent e) {
    }

    public void keyPressed(KeyEvent e) {
        int keyCode = e.getKeyCode();
        if (keyCode == KeyEvent.VK_D) {
            yawRight = true;
        }
        if (keyCode == KeyEvent.VK_A) {
            yawLeft = true;
        }
        if (keyCode == KeyEvent.VK_W) {
            forward = true;
        }
        if (keyCode == KeyEvent.VK_S) {
            back = true;
        }
        if (keyCode == KeyEvent.VK_SHIFT) {
            up = true;
        }
        if (keyCode == KeyEvent.VK_CONTROL) {
            down = true;
        }
        if (keyCode == KeyEvent.VK_Q) {
            rollLeft = true;
        }
        if (keyCode == KeyEvent.VK_E) {
            rollRight = true;
        }
    }

    public void keyReleased(KeyEvent e) {
        int keyCode = e.getKeyCode();
        if (keyCode == KeyEvent.VK_D) {
            yawRight = false;
        }
        if (keyCode == KeyEvent.VK_A) {
            yawLeft = false;
        }
        if (keyCode == KeyEvent.VK_W) {
            forward = false;
        }
        if (keyCode == KeyEvent.VK_S) {
            back = false;
        }
        if (keyCode == KeyEvent.VK_SHIFT) {
            up = false;
        }
        if (keyCode == KeyEvent.VK_CONTROL) {
            down = false;
        }
        if (keyCode == KeyEvent.VK_Q) {
            rollLeft = false;
        }
        if (keyCode == KeyEvent.VK_E) {
            rollRight = false;
        }
        // Режим наблюдателя / посадка в вёртолёт
        if (keyCode == KeyEvent.VK_F) {
            spectator = !spectator;
        }
    }

    public void mouseClicked(MouseEvent e) {
    }

    public void mousePressed(MouseEvent e) {
    }

    public void mouseReleased(MouseEvent e) {
    }

    public void mouseEntered(MouseEvent e) {
    }

    public void mouseExited(MouseEvent e) {
    }

    public void mouseDragged(MouseEvent e) {
        MainCamera.angles.x += (mousePreviousY - e.getY()) * 0.4;
        MainCamera.angles.y += (mousePreviousX - e.getX()) * 0.4;
        mousePreviousX = e.getX();
        mousePreviousY = e.getY();
    }

    public void mouseMoved(MouseEvent e) {
        mousePreviousX = e.getX();
        mousePreviousY = e.getY();
    }

    /**
     * Событие вращения колесика мыши, отвечающее за приближение / отдаление камеры
     * @param e Событие колесика мыши
     */
    public void mouseWheelMoved(MouseWheelEvent e) {
        if (e.getWheelRotation() > 0) {
            distance++;
        } else {
            distance--;
        }
    }

}
