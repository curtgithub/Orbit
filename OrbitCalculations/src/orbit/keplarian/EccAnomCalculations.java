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
public class EccAnomCalculations {

    //
    // See: https://www.csun.edu/~hcmth017/master/node16.html
    //
    public static double newtonSolve(double meanAnom, double ecc, double eccAnom,
            double tol, int maxIters) {

        eccAnom = meanAnom;  // TODO: Better guess for eccAnom

        double eccAnomPrev;
        double sinE0;
        double cosE0;
        double numerator;
        double denomanator;
        double dividend;
        int iterations = 0;
        double difference = Double.MAX_VALUE;
        while (difference > tol) {
            if (iterations > maxIters) {
                eccAnom = Double.NaN;
                break;
            }
            iterations++;
            eccAnomPrev = eccAnom;
            sinE0 = Math.sin(eccAnomPrev);
            cosE0 = Math.cos(eccAnomPrev);
            numerator = eccAnomPrev - (ecc * sinE0) - meanAnom;
            denomanator = 1d - (ecc * cosE0);
            dividend = numerator / denomanator;
            eccAnom = eccAnomPrev - dividend;
            difference = Math.abs(eccAnom - eccAnomPrev);

        }
        return eccAnom;
    }

    public static double perturbNewton(double meanAnom, double meanAnomInc, double ecc, double tol, int maxIters) {
        double eccAnom;
        //
        // hi
        //
        double meanAnomHi = meanAnom + meanAnomInc;
        double eccAnomHi = Double.NaN;
        int hiIncs = 0;
        while ((meanAnomHi < Math.PI) && Double.isNaN(eccAnomHi)) {
            hiIncs++;
            eccAnomHi = EccAnomCalculations.newtonSolve(meanAnomHi, ecc, meanAnomHi, tol, maxIters);
            meanAnomHi = meanAnomHi + meanAnomInc;
        }
        if (Double.isNaN(eccAnomHi)) {
            eccAnomHi = Math.PI;
        }
        //
        // lo
        //
        double meanAnomLo = meanAnom - meanAnomInc;
        double eccAnomLo = Double.NaN;
        int loIncs = 0;
        while ((meanAnomLo < Math.PI) && Double.isNaN(eccAnomLo)) {
            loIncs++;
            eccAnomLo = EccAnomCalculations.newtonSolve(meanAnomLo, ecc, meanAnomLo, tol, maxIters);
            meanAnomLo = meanAnomLo - meanAnomInc;
        }
        if (Double.isNaN(eccAnomLo)) {
            eccAnomLo = 0d;
        }

        eccAnom = eccAnomLo + (((hiIncs * eccAnomHi) - (loIncs * eccAnomLo)) / (hiIncs + loIncs)); // TODO: ERROR

        return eccAnom;
    }

    public static double trueAnom(double ecc, double eccAnom) {
        double trueAnom = Double.NaN;
        double cosEccAnom = Math.cos(eccAnom);
        
        trueAnom = Math.acos((cosEccAnom - ecc)/(1d - ecc*cosEccAnom));
        
        return trueAnom;
    }
    public static void main(String[] args) {

        double meanAnom;
        double ecc;
        double eccAnom;
        double tol = 1.0E-04;
        int maxIters = 100;
        double meanAnomInc = Math.toRadians(1d);

        long begin = System.currentTimeMillis();
        if (false) {
            for (ecc = 0.0999d; ecc < 1d; ecc = ecc + 0.1d) {
                for (double degs = 0.1; degs < 180; degs = degs + 0.01d) {
                    meanAnom = Math.toRadians(degs);
                    // Mnorm = M % (2d * Math.PI);
                    // eps = TestESolutions.eps3(Mnorm, e, M);
                    eccAnom = EccAnomCalculations.newtonSolve(meanAnom, ecc, meanAnom, tol, maxIters);
                    //System.out.println(degs + "  " + ecc + "  " + Math.toDegrees(eccAnom));
                    if (Double.isNaN(eccAnom)) {
                        eccAnom = EccAnomCalculations.perturbNewton(meanAnom, meanAnomInc, ecc, tol, maxIters);
                        if (!Double.isFinite(eccAnom)) {
                            System.out.println("Perturbation failed!");
                            break;
                        }
                        //System.out.println(degs + "  " + ecc + "  " + Math.toDegrees(eccAnom));

                    }
                }
            }
        }
        if (true) {
            ecc = 0.9975;
            System.out.println("Ecc\tMeanAnom\tEccAnom\tTrueAnom");
            for (double degs = 0d; degs < 180; degs = degs + 1d) {
                meanAnom = Math.toRadians(degs);
                // Mnorm = M % (2d * Math.PI);
                // eps = TestESolutions.eps3(Mnorm, e, M);
                eccAnom = EccAnomCalculations.newtonSolve(meanAnom, ecc, meanAnom, tol, maxIters);
                //System.out.println(degs + "  " + ecc + "  " + Math.toDegrees(eccAnom));
                /*
                if (Double.isNaN(eccAnom)) {
                    
                    eccAnom = EccAnomCalculations.perturbNewton(meanAnom, meanAnomInc, ecc, tol, maxIters);
                    if (!Double.isFinite(eccAnom)) {
                        System.out.println("Perturbation failed!");
                        break;
                    }
                }
*/
                double trueAnom = trueAnom(ecc, eccAnom);
                System.out.println(ecc + "\t" + degs + "\t" + Math.toDegrees(eccAnom)+ "\t" + Math.toDegrees(trueAnom));

            }
        }
        long end = System.currentTimeMillis();
        long elapsed = end - begin;
        System.out.println("Elasped time = " + elapsed);

    }
}
