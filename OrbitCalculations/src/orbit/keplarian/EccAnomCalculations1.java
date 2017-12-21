/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package orbit.keplarian;

/**
 *
 * @author owner
 */
public class EccAnomCalculations1 {

    public static double newtonSolve(double M, double e, double E) {

        E = M;
        //
        // See: http://alpheratz.net/dynamics/twobody/KeplerIterations_summary.pdf
        //
        double E0;
        double sinE0;
        double cosE0;
        double numerator;
        double denomanator;
        double dividend;
        int maxIterations = 100;
        int iterations = 0;
        double tolerance = 1.0E-08;
        double difference = Double.MAX_VALUE;
        while (difference > tolerance) {
            if (iterations > maxIterations) {
                E = Double.NaN;
                break;
            }
            iterations++;
            E0 = E;
            sinE0 = Math.sin(E0);
            cosE0 = Math.cos(E0);
            numerator = E0 - (e * sinE0) - M;
            denomanator = 1d - (e * cosE0);
            dividend = numerator / denomanator;
            E = E0 - dividend;
            difference = Math.abs(E - E0);

        }
        return E;
    }

    public static double perturbNewton(double M, double e) {
        double E = Double.NaN;
        //
        // hi
        //
        double Minc = Math.toRadians(0.001d);
        double Mhi = M + Minc;
        double Ehi = Double.NaN;
        while ((Mhi < Math.PI) && Double.isNaN(Ehi)) {
            Ehi = EccAnomCalculations1.newtonSolve(Mhi, e, Mhi);
            Mhi = Mhi + Minc;
        }
        if (Double.isNaN(Ehi)) {
            Ehi = Math.PI;
        }
        //
        // lo
        //
        double Mlo = M - Minc;
        double Elo = Double.NaN;
        while ((Mlo < Math.PI) && Double.isNaN(Elo)) {
            Elo = EccAnomCalculations1.newtonSolve(Mlo, e, Mlo);
            Mlo = Mlo - Minc;
        }
        if (Double.isNaN(Elo)) {
            Elo = 0d;
        }

        E = Elo + (Ehi - Elo) / 2d;

        return E;
    }

    public static double taylorO3(double M, double e) {  //Doesn't work
        double E = M;
        double diff = Double.MAX_VALUE;
        double tol = 1E-04;
        int iters = 0;
        double E0 = Double.NaN;

        double e2 = e * e;

        double Me = M * e;

        double C1 = M / (e - 1d);
        double C2 = Me / (2d * e2 - 4d * e + 2d);
        double C3 = e / (3d * e - 3d);

        while (diff > tol) {
            iters++;
            if (iters > 10) {
                E = Double.NaN;
                break;
            }

            E0 = E;

            double E2 = E0 * E0;
            double E3 = E2 * E0;

            E = -C1 - C2 * E2 - C3 * E3;

            diff = Math.abs(E - E0);

        }

        System.out.println(iters);

        return E;
    }

    //
    // http://alpheratz.net/dynamics/twobody/KeplerIterations_summary.pdf
    // Equation(4)
    // TODO: Figure it out
    //
    public static double keplerStart3(double M, double e) {
        double E = Double.NaN;
        double t33, t35, t34;
        t34 = e * e;
        t35 = e * t34;
        t33 = Math.cos(M);
        E = M + (-0.5d * t34 + e + (t34 + 1.5d * t33 * t35) * t33) * Math.sin(M);
        return E;
    }

    //
    // http://alpheratz.net/dynamics/twobody/KeplerIterations_summary.pdf
    // Equation(14)
    // TODO: Figure it out
    //
    public static double eps3(double Mnorm, double e, double E) {
        double eps = Double.NaN;
        double t1, t2, t3, t4, t5, t6;

        t1 = Math.cos(E);
        t2 = -1 + e * t1;
        t3 = Math.sin(E);
        t4 = e * t3;
        t5 = -E + t4 + Mnorm;
        t6 = t5 / (0.5d * t5 * t4 / t2 + t2);
        eps = t5 / ((0.5d * t3 - 1d / 6d * t1 * t6) * e * t6 + t2);
        return eps;
    }

    //
    // http://alpheratz.net/dynamics/twobody/KeplerIterations_summary.pdf
    // Page 8.
    // TODO: Figure it out
    //
    public static double keplerSolve(double M, double e) {
        double E = Double.NaN;
        double dE, E0, Mnorm;
        double tolerance = 1.0E-09;
        Mnorm = M % (2d * Math.PI);
        E0 = EccAnomCalculations1.keplerStart3(e, Mnorm);
        dE = tolerance + 1;
        int iterations = 0;
        while (dE > tolerance) {
            E = E0 - EccAnomCalculations1.eps3(Mnorm, e, E0);
            dE = Math.abs(E - E0);
            E0 = E;
            iterations = iterations + 1;
            if (iterations > 10) {
                E = Double.NaN;
                break;
            }
        }
        return E;
    }

    public static double perturbation(double M, double e) {
        double E = Double.NaN;
        //
        // hi
        //
        double Minc = Math.toRadians(1d);
        double Mhi = M + Minc;
        double Ehi = Double.NaN;
        while ((Mhi < Math.PI) && Double.isNaN(Ehi)) {
            Ehi = EccAnomCalculations1.keplerSolve(Mhi, e);
            Mhi = Mhi + Minc;
        }
        if (Double.isNaN(Ehi)) {
            Ehi = Math.PI;
        }
        //
        // lo
        //
        double Mlo = M - Minc;
        double Elo = Double.NaN;
        while ((Mlo < Math.PI) && Double.isNaN(Elo)) {
            Elo = EccAnomCalculations1.keplerSolve(Mlo, e);
            Mlo = Mlo - Minc;
        }
        if (Double.isNaN(Elo)) {
            Elo = 0d;
        }

        E = Elo + (Ehi - Elo) / 2d;

        return E;
    }

    public static void main(String[] args) {

        double M;
        double e;
        double E;
        double Mnorm;
        double eps;

        if (true) {
            for (e = 0.0999d; e < 1d; e = e + 0.1d) {
                for (double degs = 0.1; degs < 180; degs = degs + 0.01d) {
                    M = Math.toRadians(degs);
                    // Mnorm = M % (2d * Math.PI);
                    // eps = TestESolutions.eps3(Mnorm, e, M);
                    E = EccAnomCalculations1.newtonSolve(M, e, M);
                    System.out.println(degs + "  " + e + "  " + Math.toDegrees(E));
                    if (Double.isNaN(E)) {
                        E = EccAnomCalculations1.perturbNewton(M, e);
                        if (!Double.isFinite(E)) {
                            System.out.println("Perturbation failed!");
                            break;
                        }
                        System.out.println(degs + "  " + e + "  " + Math.toDegrees(E));

                    }
                }
            }
        }
        //
        // NaN case
        //
        if (false) {

            M = Math.toRadians(170.0);
            e = 0.29990000000000006;
            Mnorm = M % (2d * Math.PI);
            eps = EccAnomCalculations1.eps3(Mnorm, e, M);
            E = EccAnomCalculations1.keplerSolve(M, e);
            System.out.println(Math.toDegrees(M) + "  " + e + "  " + Math.toDegrees(E)
                    + "  " + Math.toDegrees(eps));
            E = EccAnomCalculations1.perturbation(M, e);
            System.out.println(Math.toDegrees(M) + "  " + e + "  " + Math.toDegrees(E)
                    + "  " + Math.toDegrees(eps));
        }

        if (false) {
            for (e = 0.0999d; e < 1d; e = e + 0.1d) {
                for (double degs = 10; degs < 180; degs = degs + 10d) {
                    M = Math.toRadians(degs);
                    Mnorm = M % (2d * Math.PI);
                    eps = EccAnomCalculations1.eps3(Mnorm, e, M);
                    E = EccAnomCalculations1.keplerSolve(M, e);
                    System.out.println(degs + "  " + e + "  " + Math.toDegrees(E)
                            + "  " + Math.toDegrees(eps));
                    if (Double.isNaN(E)) {

                        E = EccAnomCalculations1.perturbation(M, e);
                        System.out.println(degs + "  " + e + "  " + Math.toDegrees(E)
                                + "  " + Math.toDegrees(eps));

                    }
                }
            }
        }
    }
}
