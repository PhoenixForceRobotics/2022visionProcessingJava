package utils;

import java.lang.Math;

//All this math was done by Emily and I don't feel like proofreading it myself

public class ArcMath {
    public static double shotFlywheelVelocity(double distance, double angle, double height) {
        /*
        Calculates the velocity to be shot at based on the angle configured, 
        the distance to the target, and the height difference between the target
        and the shooter.
        
        Takes the top-down 'as the crow flies' distance to the target (feet)), 
        the angle the shooter is aimed at (degrees), and the height of the target
        relative to the turret (feet). Returns the velocity necessary to launch 
        the ball into the hoop (feet/second), if applicable. 
        
        Note that this will not automatically handle whether or not its output is 
        possible or not.
        */
        //the component things are just to make this more legible
        double sqrtComponent = ((distance * Math.tan(angle)) - height) / (Constants.TurretConstants.GRAVITY / 2);
        double denominatorComponent = Math.cos(angle) * Math.sqrt(sqrtComponent);
        return distance / denominatorComponent;
    }

    public static double shotDistanceRequired(double flywheelVelocity, double angle, double height) {
        /*
        Calculates the distance 'as the crow flies' required to make a shot at
        the vision target based on the velocity of the flywheels, the angle being 
        shot at, and the height to the hoop. 
        
        Takes the velocity of the flywheels (feet/second), the angle of the shooter
        (degrees), and the height of the target relative to the turret (feet). 
        Returns the distance required to launch the ball into the hoop, if applicable.

        Note that this will not automatically handle whether or not its output is 
        possible or not.
        */
        //again, the component things are just to make this more legible
        double sqrtComponent = (Math.pow(flywheelVelocity, 2) * Math.pow(Math.sin(angle, 2)) - (Constants.TurretConstants.GRAVITY * 2 * height);
        double numeratorComponent = (flywheelVelocity * Math.sin(angle)) + Math.sqrt(sqrtComponent);
        double fractionComponent = numeratorComponent / Constants.TurretConstants.GRAVITY;
        return (velocity * Math.cos(angle) * fractionComponent);
    }
}
