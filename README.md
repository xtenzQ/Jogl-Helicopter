# JOGL Helicopter

### *UNDER CONSTRUCTION*

## Contents
1. [Before we start](#before-we-start)
2. [IDEs and plugins used](#ides-and-plugins-used)
3. [Main classes](#main-classes)
    - [Helicopter](#helicopter)
    - [Main procedure of controller class](#main-procedure-of-controller-class)
4. [References](#references)

## Before we start

![Helicopter](https://i.imgur.com/DLTbumP.png)
*The application is developed as the part of the project for computer graphics course at Irkutsk National Research Techincal University.*

## IDEs and plugins used
- NetBeans IDE ([Download](https://netbeans.org/downloads/index.html))
- NetBeans OpenGL Pack ([Download](http://plugins.netbeans.org/plugin/3260/netbeans-opengl-pack))

## Main classes
### Helicopter

First of all, we have to define parameter fields for a helicopter.
Model related fields:
```Java
// Obj file for helicopter model
private final Object body;
// Main screw
private final Object mainScrew;
// Roll screw
private final Object rollScrew;
```
Physics-related fields:
```Java
// Mass
public final float mass = 11000; // 11 000 kg
// Max Y speed
public final float maxSpeedY = 20; // 20 meters per second

// Speed
public Vector3 speed;
// Acceleration
public Vector3 accel;

// Angular Velocity
public Vector3 speedAngle;
// Angular Acceleration
public Vector3 accelAngle;

// Thrust
public static float pull = (float) 0;
// Rotational speed relative to the yaw axis
public static float pullAngle = (float) 0.3;
```
Then create a constructor. Before doing anything it's needed to load three models (obj-files) of our helicopter: 
```Java
body = new Object("heli/Helicopter.obj");
mainScrew = new Object("heli/MainScrew.obj");
rollScrew = new Object("heli/RollScrew.obj");
```
After this we have to set two screws of the helicopter:
```Java
mainScrew.position.x = (float) 0.05961;
mainScrew.position.y = (float) 3.58183;
mainScrew.position.z = (float) -0.07208;
mainScrew.angles.x = (float) -3;

rollScrew = new Object("heli/RollScrew.obj");
// Фикс рулевого винта
rollScrew.position.x = (float) -0.51373;
rollScrew.position.y = (float) 3.06563;
rollScrew.position.z = (float) -10.9899;
```
Now program physics:
```Java
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
```
And collision:
 ```Java
public void collision(float height) {
    if (height > position.y - 0.3656) {
        position.y += height - position.y + 0.3656;
        speed.y = 0;
    }
}
```

### Main procedure of controller class:
```Java
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
```

## References
