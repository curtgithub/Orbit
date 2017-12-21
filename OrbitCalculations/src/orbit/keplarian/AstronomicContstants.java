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
public class AstronomicContstants {
    
    /**
     * of an object in orbit about a central body with mass, M. G =
     * gravitational constant = 6.674x10-11N.m2/kg2 A very fundamental constant
     * in orbital mechanics is k = MG / public static double Gmetric =
     * 6.674E-11; 
     */
    public static double G = 6.674E-11; // Units: N.m2/kg2

    /**
     * More convenient units to use in Solar System Dynamics are AU for distance
     * and years for time 1 AU = 1.4960x1011 metres 1 year = 3.1558x107 seconds
     *
     */
    public static double AU = 1.4960E11; // Units: metres
    public static double Year =  3.1558E7; //Units: seconds
    
// Todo: create CentralBody Class and create constants here
    
    public static void main(String[] args) {
        System.out.println("Compiled....");
    }
    
}
