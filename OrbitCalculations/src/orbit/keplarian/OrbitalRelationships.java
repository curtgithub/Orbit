/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package orbit.keplarian;

/**
 *
 * @author owner
 *
 * See: http://www.bogan.ca/orbits/kepler/orbteqtn.html
 *
 */
public class OrbitalRelationships {

    private double mtg; //k Mass times G, Units: Length^3/Time^2

    private double ter; //E Total Energy, Units: Energy
    private double vpa; // vq Velocity at periapsis, Units: Length/Time;
    private double avc; // Areal Velocity, Units: Length/Time;
    private double opd; //A, Orbit Period, Units: Time

    private double tra; //θ True Anomaly Units: Radians;

    private double dcb; //r Distance from central body at tra must be > 0 and Units: Length
    private double vta; //v Velocity at tra Units: Length/Time
    private double ata; //φ Angle of velocity perpendicular (tangent?) plane at tra Units: Radians

    private double mna; //

    public static void main(String[] args) {
        System.out.println("Compiled....");
    }

}
