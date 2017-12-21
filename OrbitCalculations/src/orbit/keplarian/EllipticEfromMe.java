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
 * https://www.csun.edu/~hcmth017/master/node16.html
 *
 */
public class EllipticEfromMe {

    private double M;
    private double e;
    private double E;
    private int iterations;
    private int maxIterations;
    private double difference;
    private double tolerance;
    private double mine;
    private double maxe;

    public final void init() {
        this.M = Double.NaN;
        this.e = Double.NaN;
        this.E = Double.NaN;
        this.iterations = 0;
//        this.maxIterations = 10; // TODO
        this.maxIterations = 10; // TODO
        this.difference = Double.NaN;
//        this.tolerance = 1E-08; // TODO
        this.tolerance = 1E-05; // TODO
//        this.mine = 0.00005d; //TODO
        this.mine = 0d; //TODO
        this.maxe = 0.9999d; //TODO

    }

    public void reset() {
        double rM = this.M;
        double re = this.e;
        this.init();
        this.M = rM;
        this.e = re;
    }

    public EllipticEfromMe() {
        this.init();
    }

    public EllipticEfromMe(double M, double e) {
        this.init();
        this.M = M;
        this.e = e;
    }

    public double getM() {
        return M;
    }

    public void setM(double M) {
        this.reset();
        this.M = M;
    }

    public double getE() {
        return e;
    }

    public void setE(double e) {
        this.reset();
        this.e = e;
    }

    public double calcE() {
        if (this.E != Double.NaN) {
            this.calcEfromMe();
        }
        return this.E;
    }

    public int getIterations() {
        return iterations;
    }

    public void setIterations(int iterations) {
        this.reset();
        this.iterations = iterations;

    }

    public int getMaxIterations() {
        return maxIterations;
    }

    public void setMaxIterations(int maxIterations) {
        this.reset();
        this.maxIterations = maxIterations;
    }

    public double getDifference() {
        return difference;
    }

    public void setDifference(double difference) {
        this.reset();
        this.difference = difference;
    }

    public double getTolerance() {
        return tolerance;
    }

    public void setTolerance(double tolerance) {
        this.reset();
        this.tolerance = tolerance;
    }

    public double getMine() {
        return mine;
    }

    public void setMine(double mine) {
        this.reset();
        this.mine = mine;
    }

    public double getMaxe() {
        return maxe;
    }

    public void setMaxe(double maxe) {
        this.reset();
        this.maxe = maxe;
    }

    private void calcEfromMe() {

        this.E = this.M;
        //
        // See: http://alpheratz.net/dynamics/twobody/KeplerIterations_summary.pdf
        //
        double E0;
        double sinE0;
        double cosE0;
        double numerator;
        double denomanator;
        double dividend;
        double lastDifference = Double.MAX_VALUE;
        this.iterations = 0;
        do {
            this.iterations++;
            if (this.iterations >= this.maxIterations) {
                this.E = Double.NaN;
                System.out.println("Did not converg this.difference = " + this.difference);
                break;
            }
            E0 = this.E;
            sinE0 = Math.sin(E0);
            cosE0 = Math.cos(E0);
            numerator = E0 - (this.e * sinE0) - this.M;
            denomanator = 1d - (this.e * cosE0);
            dividend = numerator / denomanator;
            this.E = E0 - dividend;
            this.difference = Math.abs(this.E - E0);
            if (lastDifference < this.difference) {
                //System.out.println("Not convergeing");
            }
            lastDifference = this.difference;
        } while (this.difference > this.tolerance);

    }

    public double KeplerSolve() {
        //:= proc( e, M, tol:=1.0e-14 )
        double E0, Mnorm;
// KeplerStart3, eps3;
        Mnorm = this.M % (2d * Math.PI);//Math.ffmod(this.M, 2d * Math.PI);
        E0 = KeplerStart3(e, Mnorm);
        this.difference = this.tolerance + 1d;
        this.iterations = 0;
        while (this.difference > this.tolerance) {
            this.E = E0 - eps3(this.e, Mnorm, E0);
            this.difference = Math.abs(this.E - E0);
            E0 = this.E;
            this.iterations = this.iterations + 1;
            if (this.iterations == 100) {
                this.E = Double.NaN;
                break;
            }
        }
        return this.E;
    }

    public static double KeplerStart3(double ecc, double initE) {
        double t33, t35, t34, startE;
        t34 = ecc * ecc;
        t35 = ecc * t34;
        t33 = Math.cos(initE);
        startE = (-1d / 2d * t35 + ecc + (t34 + 3d / 2d * t33 * t35) * t33) * Math.sin(initE);
        return startE;
    }

    public static double eps3(double ecc, double M, double x) {
        double t1, t2, t3, t4, t5, t6;
        t1 = Math.cos(x);
        t2 = -1d + ecc * t1;
        t3 = Math.sin(x);
        t4 = ecc * t3;
        t5 = -x + t4 + M;
        t6 = t5 / (1d / 2d * t5 * t4 / t2 + t2);
        return t5 / ((1d / 2d * t3 - 1d / 6d * t1 * t6) * ecc * t6 + t2);
    }

    /*
    private void calcEfromMe() {

        this.E = this.M;
        this.iterations = 0;
        do {
            this.iterations++;
            if (this.iterations >= this.maxIterations) {
                this.E = Double.NaN;
                break;
            }
            double E0 = this.E;
            this.E = E0 - ((E0 - this.e * Math.sin(E0) - this.M) / (1.0d - this.e * Math.cos(E0)));
            this.difference = Math.abs(this.E - E0);
        } while (this.difference > this.tolerance);

    }
     */
    public static void main(String[] args) {

        EllipticEfromMe o = new EllipticEfromMe();

        o.setE(o.getMaxe());
        o.setM(0.1678238795547609d);
        System.out.println("M, e, iterations, E");
//        System.out.println(o.getM() + "," + o.getE() + "," + o.calcE()
//                + "," + o.getIterations());
        System.out.println(o.getM() + "," + o.getE() + "," + o.KeplerSolve()
                + "," + o.getIterations());

        o.setM(0.2d);
        System.out.println(o.getM() + "," + o.getE() + "," + o.KeplerSolve()
                + "," + o.getIterations());

        double eInc = (o.maxe - o.mine) / 10d;
//       for (o.setE(o.getMine()); o.getE() < o.getMaxe(); o.setE(o.getE() + eInc))  {
//        o.setE(o.getMaxe());
        o.setE(0.5);
        double MMax = Math.PI;
        double Minc = MMax / 180d;
        System.out.println("M, e, iterations, E");
        for (o.setM(0d); o.getM() < MMax; o.setM(o.getM() + Minc)) {
            System.out.println(Math.toDegrees(o.getM()) 
                    + "," + o.getE() 
                    + "," + Math.toDegrees(o.KeplerSolve())
                    + "," + o.getIterations());
        }
//      }
    }
}
