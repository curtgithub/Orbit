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
 * In the orbital plane.
 */
public class GeometricRelationships {

    private double ecc; //e Eccentricty, must be > 0 and Units: dimensonless
    private double dpaOrSma; // q or a

    private double oneMinusEcc; // is (1 - ecc);
    private double oneMinusEccSquared; // is (1-ecc^2)

    private double dpa; //q Distance at periapsis, must be > 0 and Units: Length
    private double sma; //a Semi-major axis Units: Length
    private double slr; //p Semi-latus rectum  Units: Length

    public final void init() {
        this.ecc = Double.NaN;
        this.dpaOrSma = Double.NaN;
        this.oneMinusEcc = Double.NaN;
        this.oneMinusEccSquared = Double.NaN;
        this.dpa = Double.NaN;
        this.sma = Double.NaN;
        this.ecc = Double.NaN;
    }

    public GeometricRelationships() {
        this.init();
    }

    public GeometricRelationships(double ecc, double dpaOrSma) {
        this.init();
        this.ecc = ecc;
        this.dpaOrSma = dpaOrSma;
    }

    public void reset() {
        Double rEcc = this.ecc;
        Double rDpaOrSma = this.dpaOrSma;
        this.init();
        this.ecc = rEcc;
        this.dpaOrSma = rDpaOrSma;
    }

    public double getEcc() {
        return this.ecc;
    }

    public void setEcc(double ecc) {
        this.reset();
        this.ecc = ecc;
    }

    public double getDpaOrSma() {
        return dpaOrSma;
    }

    public void setDpaOrSma(double dpaOrSma) {
        this.reset();
        this.dpaOrSma = dpaOrSma;
    }

    public double getOneMinusEcc() {
        if (this.oneMinusEcc == Double.NaN) {
            this.oneMinusEcc = 1d - this.getEcc();
        }
        return this.oneMinusEcc;
    }

    public double getOneMinusEccSquared() {
        if (this.oneMinusEccSquared == Double.NaN) {
            this.oneMinusEccSquared = 1d - (this.getEcc() * this.getEcc());
        }
        return this.oneMinusEccSquared;
    }

    public double getDpa() {
        if (this.dpa == Double.NaN) {
            this.dpa = this.getDpaOrSma() * this.getOneMinusEcc();
        }
        return this.dpa;
    }

    public double getSma() {
        if (this.sma == Double.NaN) {
            this.sma = this.getDpaOrSma() / this.getOneMinusEcc();
        }
        return this.sma;
    }

    public double getSlr() {
        if (this.slr == Double.NaN) {
            if (this.getEcc() == 1d) {
                this.slr = 2d * this.getDpa();
            }
            else {
                this.slr = this.getSma() * this.getOneMinusEccSquared();
            }
        }
        return this.slr;
    }

}
