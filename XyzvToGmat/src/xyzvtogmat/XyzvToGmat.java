/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package xyzvtogmat;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.ListIterator;
import java.util.StringTokenizer;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author owner
 */
public class XyzvToGmat {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {

        try {
            String xyzvFileName = args[0];
            String gmatFileName = args[1];
            File gmatFile = new File(gmatFileName);
            if (gmatFile.exists()) {
                gmatFile.delete();
            }

            String line;
            File xyvzFile = new File(xyzvFileName);
            BufferedReader br = new BufferedReader(new FileReader(xyvzFile));
            List<SpaceCraft> scl = new ArrayList();

            while ((line = br.readLine()) != null) {

                if (!line.startsWith("#")) {

                    double tdbJd;

                    String sx;
                    String sy;
                    String sz;

                    String svx;
                    String svy;
                    String svz;

                    StringTokenizer st;
                    st = new StringTokenizer(line);

                    while (st.hasMoreTokens()) {

                        tdbJd = Double.parseDouble(st.nextToken());

                        sx = st.nextToken();
                        sy = st.nextToken();
                        sz = st.nextToken();

                        svx = st.nextToken();
                        svy = st.nextToken();
                        svz = st.nextToken();

                        SpaceCraft sc = new SpaceCraft("Cassini");
                        sc.setTdbJd(tdbJd);
                        sc.setSx(sx);
                        sc.setSy(sy);
                        sc.setSz(sz);
                        sc.setSvx(svx);
                        sc.setSvy(svy);
                        sc.setSvz(svz);
                        scl.add(sc);

                    }
                }
            }
            //
            // Create GMAT script file
            //            
            BufferedWriter bw = new BufferedWriter(new FileWriter(gmatFile, true));
            //
            // Write Header
            //
            bw.write("%General Mission Analysis Tool(GMAT) Script\n");
            bw.write("%Created: ");
            Date d = new Date();
            DateFormat df = new SimpleDateFormat("yyy-mm-dd HH:mm:ss");
            bw.write(df.format(d));
            bw.write("\n\n");
            //
            // Write Simple Space Craft once
            //
            ListIterator<SpaceCraft> li = scl.listIterator();
            SpaceCraft sc1 = li.next();
            bw.write(sc1.toNameString());
            bw.flush();
            bw.write(sc1.toFormatNameString());
            bw.flush();
            //
            // Write Spacecraft
            //
            double minDiffTdbJd = Double.MAX_VALUE;
            double maxDiffTdbJd = Double.MIN_VALUE;

            do {
                if (li.hasNext()) {
                    SpaceCraft sc2;
                    double tdiff;
                    do {
                        sc2 = li.next();
                        tdiff = sc2.getTdbJd() - sc1.getTdbJd();
                        if (tdiff < minDiffTdbJd) {
                            minDiffTdbJd = tdiff;
                        }
                        if (tdiff > maxDiffTdbJd) {
                            maxDiffTdbJd = tdiff;
                        }
                    } while (li.hasNext() && (tdiff < 1.0));
                    bw.write(sc2.toFormatNameString());
                    sc1 = sc2;
                    bw.write("\n");
                    bw.flush();
                }
            } while (li.hasNext());
            
            System.out.println("minDiffTdbJd = " + minDiffTdbJd * 86400 + "\n");
            System.out.println("maxDiffTdbJd = " + maxDiffTdbJd * 86400 + "\n");

            //
            // Write ForceModels
            //
            bw.write("%----------------------------------------\n");
            bw.write("%---------- ForceModels\n");
            bw.write("%----------------------------------------\n");
            bw.write("\n\n");
            bw.write("Create ForceModel PropSunSOI_ForceModel;\n");
            bw.write("GMAT PropSunSOI_ForceModel.CentralBody = Sun;\n");
            bw.write("GMAT PropSunSOI_ForceModel.PointMasses = {Earth, Jupiter, Luna, Saturn, Sun, Venus};\n");
            bw.write("GMAT PropSunSOI_ForceModel.Drag = None;\n");
            bw.write("GMAT PropSunSOI_ForceModel.SRP = Off;\n");
            bw.write("GMAT PropSunSOI_ForceModel.RelativisticCorrection = Off;\n");
            bw.write("GMAT PropSunSOI_ForceModel.ErrorControl = RSSStep;\n");
            bw.write("\n\n");
            //
            // Write Propagators
            //
            bw.write("%----------------------------------------\n");
            bw.write("%---------- Propagators\n");
            bw.write("%----------------------------------------\n");
            bw.write("\n\n");

            bw.write("Create Propagator PropSunSOI;\n");
            bw.write("GMAT PropSunSOI.FM = PropSunSOI_ForceModel;\n");
            bw.write("GMAT PropSunSOI.Type = PrinceDormand78;\n");
            bw.write("GMAT PropSunSOI.InitialStepSize = 3600;\n");
            bw.write("GMAT PropSunSOI.Accuracy = 1.0;\n");
            bw.write("GMAT PropSunSOI.MinStep = 3600;\n");
            bw.write("GMAT PropSunSOI.MaxStep = 326400;\n");
            bw.write("GMAT PropSunSOI.MaxStepAttempts = 50;\n");
            bw.write("GMAT PropSunSOI.StopIfAccuracyIsViolated = true;\n");
            bw.write("\n\n");
            //
            // Append CoordinateSystem
            //
            bw.write("%----------------------------------------\n");
            bw.write("%---------- Coordinate Systems\n");
            bw.write("%----------------------------------------\n");
            bw.write("\n\n");
            bw.write("Create CoordinateSystem SunEcliptic;\n");
            bw.write("GMAT SunEcliptic.Origin = Sun;\n");
            bw.write("GMAT SunEcliptic.Axes = MJ2000Ec;\n");
            bw.write("\n");
            bw.write("Create CoordinateSystem VenusInertial;\n");
            bw.write("GMAT VenusInertial.Origin = Venus;\n");
            bw.write("GMAT VenusInertial.Axes = MJ2000Ec;\n");
            bw.write("\n");
            bw.write("Create CoordinateSystem VenusEcliptic;\n");
            bw.write("GMAT VenusEcliptic.Origin = Venus;\n");
            bw.write("GMAT VenusEcliptic.Axes = MJ2000Eq;\n");
            bw.write("\n");
            bw.write("Create CoordinateSystem JupiterInertial;\n");
            bw.write("GMAT JupiterInertial.Origin = Jupiter;\n");
            bw.write("GMAT JupiterInertial.Axes = MJ2000Ec;\n");
            bw.write("\n");
            bw.write("Create CoordinateSystem JupiterEcliptic;\n");
            bw.write("GMAT JupiterEcliptic.Origin = Jupiter;\n");
            bw.write("GMAT JupiterEcliptic.Axes = MJ2000Eq;\n");
            bw.write("\n");
            bw.write("Create CoordinateSystem SaturnInertial;\n");
            bw.write("GMAT SaturnInertial.Origin = Saturn;\n");
            bw.write("GMAT SaturnInertial.Axes = MJ2000Ec;\n");
            bw.write("\n");
            bw.write("Create CoordinateSystem SaturnEcliptic;\n");
            bw.write("GMAT SaturnEcliptic.Origin = Saturn;\n");
            bw.write("GMAT SaturnEcliptic.Axes = MJ2000Eq;\n");
            bw.write("\n\n");
            //
            // Write Solvers
            //
            bw.write("%----------------------------------------\n");
            bw.write("%---------- Solvers\n");
            bw.write("%----------------------------------------\n");
            bw.write("\n\n");
            bw.write("Create DifferentialCorrector DefaultDC;\n");
            bw.write("GMAT DefaultDC.ShowProgress = true;\n");
            bw.write("GMAT DefaultDC.ReportStyle = Normal;\n");
            bw.write("GMAT DefaultDC.ReportFile = 'DifferentialCorrectorDC1.data';\n");
            bw.write("GMAT DefaultDC.MaximumIterations = 25;\n");
            bw.write("GMAT DefaultDC.DerivativeMethod = ForwardDifference;\n");
            bw.write("GMAT DefaultDC.Algorithm = NewtonRaphson;\n");
            bw.write("\n\n");
            //
            // Write Subscripers
            //
            bw.write("%----------------------------------------\n");
            bw.write("%---------- Subscribers\n");
            bw.write("%----------------------------------------\n");
            bw.write("\n\n");
            bw.write("Create OrbitView SolarSystemView;\n");
            bw.write("GMAT SolarSystemView.SolverIterations = Current;\n");
            bw.write("GMAT SolarSystemView.UpperLeft = [ 0.003311258278145695 0 ];\n");
            bw.write("GMAT SolarSystemView.Size = [ 0.1545253863134658 0.2245131729667812 ];\n");
            bw.write("GMAT SolarSystemView.RelativeZOrder = 131;\n");
            bw.write("GMAT SolarSystemView.Maximized = false;\n");
            bw.write("GMAT SolarSystemView.Add = {Cassini, Jupiter, Sun, Saturn, Mars, Neptune, Pluto, Uranus, Earth, Venus};\n");
            bw.write("GMAT SolarSystemView.CoordinateSystem = SunEcliptic;\n");
            bw.write("GMAT SolarSystemView.DrawObject = [ true true true true true true true true true true ];\n");
            bw.write("GMAT SolarSystemView.DataCollectFrequency = 1;\n");
            bw.write("GMAT SolarSystemView.UpdatePlotFrequency = 50;\n");
            bw.write("GMAT SolarSystemView.NumPointsToRedraw = 0;\n");
            bw.write("GMAT SolarSystemView.ShowPlot = true;\n");
            bw.write("GMAT SolarSystemView.ShowLabels = true;\n");
            bw.write("GMAT SolarSystemView.ViewPointReference = Sun;\n");
            bw.write("GMAT SolarSystemView.ViewPointVector = [ 0 0 5000000000 ];\n");
            bw.write("GMAT SolarSystemView.ViewDirection = Sun;\n");
            bw.write("GMAT SolarSystemView.ViewScaleFactor = 1;\n");
            bw.write("GMAT SolarSystemView.ViewUpCoordinateSystem = SunEcliptic;\n");
            bw.write("GMAT SolarSystemView.ViewUpAxis = Z;\n");
            bw.write("GMAT SolarSystemView.EclipticPlane = Off;\n");
            bw.write("GMAT SolarSystemView.XYPlane = On;\n");
            bw.write("GMAT SolarSystemView.WireFrame = Off;\n");
            bw.write("GMAT SolarSystemView.Axes = On;\n");
            bw.write("GMAT SolarSystemView.Grid = Off;\n");
            bw.write("GMAT SolarSystemView.SunLine = Off;\n");
            bw.write("GMAT SolarSystemView.UseInitialView = On;\n");
            bw.write("GMAT SolarSystemView.StarCount = 7000;\n");
            bw.write("GMAT SolarSystemView.EnableStars = On;\n");
            bw.write("GMAT SolarSystemView.EnableConstellations = On;\n");
            bw.write("\n");
            bw.write("Create OrbitView EarthView;\n");
            bw.write("GMAT EarthView.SolverIterations = Current;\n");
            bw.write("GMAT EarthView.UpperLeft = [ 0.1655629139072848 0 ];\n");
            bw.write("GMAT EarthView.Size = [ 0.1545253863134658 0.2245131729667812 ];\n");
            bw.write("GMAT EarthView.RelativeZOrder = 133;\n");
            bw.write("GMAT EarthView.Maximized = false;\n");
            bw.write("GMAT EarthView.Add = {Cassini, Earth, Sun, Luna};\n");
            bw.write("GMAT EarthView.CoordinateSystem = EarthMJ2000Eq;\n");
            bw.write("GMAT EarthView.DrawObject = [ true true true true ];\n");
            bw.write("GMAT EarthView.DataCollectFrequency = 1;\n");
            bw.write("GMAT EarthView.UpdatePlotFrequency = 50;\n");
            bw.write("GMAT EarthView.NumPointsToRedraw = 0;\n");
            bw.write("GMAT EarthView.ShowPlot = true;\n");
            bw.write("GMAT EarthView.ShowLabels = true;\n");
            bw.write("GMAT EarthView.ViewPointReference = Earth;\n");
            bw.write("GMAT EarthView.ViewPointVector = [ 0 0 3000000 ];\n");
            bw.write("GMAT EarthView.ViewDirection = Earth;\n");
            bw.write("GMAT EarthView.ViewScaleFactor = 4;\n");
            bw.write("GMAT EarthView.ViewUpCoordinateSystem = EarthMJ2000Eq;\n");
            bw.write("GMAT EarthView.ViewUpAxis = Z;\n");
            bw.write("GMAT EarthView.EclipticPlane = Off;\n");
            bw.write("GMAT EarthView.XYPlane = On;\n");
            bw.write("GMAT EarthView.WireFrame = Off;\n");
            bw.write("GMAT EarthView.Axes = On;\n");
            bw.write("GMAT EarthView.Grid = Off;\n");
            bw.write("GMAT EarthView.SunLine = On;\n");
            bw.write("GMAT EarthView.UseInitialView = On;\n");
            bw.write("GMAT EarthView.StarCount = 7000;\n");
            bw.write("GMAT EarthView.EnableStars = On;\n");
            bw.write("GMAT EarthView.EnableConstellations = On;\n");
            bw.write("\n");
            bw.write("Create OrbitView VenusView;\n");
            bw.write("GMAT VenusView.SolverIterations = Current;\n");
            bw.write("GMAT VenusView.UpperLeft = [ 0.3388520971302428 0 ];\n");
            bw.write("GMAT VenusView.Size = [ 0.1710816777041943 0.2199312714776632 ];\n");
            bw.write("GMAT VenusView.RelativeZOrder = 90;\n");
            bw.write("GMAT VenusView.Maximized = false;\n");
            bw.write("GMAT VenusView.Add = {Cassini, Venus, Earth, Sun};\n");
            bw.write("GMAT VenusView.CoordinateSystem = VenusEcliptic;\n");
            bw.write("GMAT VenusView.DrawObject = [ true true true true ];\n");
            bw.write("GMAT VenusView.DataCollectFrequency = 1;\n");
            bw.write("GMAT VenusView.UpdatePlotFrequency = 50;\n");
            bw.write("GMAT VenusView.NumPointsToRedraw = 0;\n");
            bw.write("GMAT VenusView.ShowPlot = true;\n");
            bw.write("GMAT VenusView.ShowLabels = true;\n");
            bw.write("GMAT VenusView.ViewPointReference = Venus;\n");
            bw.write("GMAT VenusView.ViewPointVector = [ 0 0 30000 ];\n");
            bw.write("GMAT VenusView.ViewDirection = Venus;\n");
            bw.write("GMAT VenusView.ViewScaleFactor = 1;\n");
            bw.write("GMAT VenusView.ViewUpCoordinateSystem = VenusEcliptic;\n");
            bw.write("GMAT VenusView.ViewUpAxis = Z;\n");
            bw.write("GMAT VenusView.EclipticPlane = Off;\n");
            bw.write("GMAT VenusView.XYPlane = On;\n");
            bw.write("GMAT VenusView.WireFrame = Off;\n");
            bw.write("GMAT VenusView.Axes = On;\n");
            bw.write("GMAT VenusView.Grid = Off;\n");
            bw.write("GMAT VenusView.SunLine = On;\n");
            bw.write("GMAT VenusView.UseInitialView = On;\n");
            bw.write("GMAT VenusView.StarCount = 7000;\n");
            bw.write("GMAT VenusView.EnableStars = On;\n");
            bw.write("GMAT VenusView.EnableConstellations = On;\n");
            bw.write("\n");
            bw.write("Create OrbitView JupiterView;\n");
            bw.write("GMAT JupiterView.SolverIterations = Current;\n");
            bw.write("GMAT JupiterView.UpperLeft = [ 0 0.2359679266895762 ];\n");
            bw.write("GMAT JupiterView.Size = [ 0.2251655629139073 0.2623138602520046 ];\n");
            bw.write("GMAT JupiterView.RelativeZOrder = 25;\n");
            bw.write("GMAT JupiterView.Maximized = false;\n");
            bw.write("GMAT JupiterView.Add = {Cassini, Jupiter, Earth, Sun};\n");
            bw.write("GMAT JupiterView.CoordinateSystem = JupiterInertial;\n");
            bw.write("GMAT JupiterView.DrawObject = [ true true true true ];\n");
            bw.write("GMAT JupiterView.DataCollectFrequency = 1;\n");
            bw.write("GMAT JupiterView.UpdatePlotFrequency = 50;\n");
            bw.write("GMAT JupiterView.NumPointsToRedraw = 0;\n");
            bw.write("GMAT JupiterView.ShowPlot = true;\n");
            bw.write("GMAT JupiterView.ShowLabels = true;\n");
            bw.write("GMAT JupiterView.ViewPointReference = Jupiter;\n");
            bw.write("GMAT JupiterView.ViewPointVector = [ 0 0 1500000000 ];\n");
            bw.write("GMAT JupiterView.ViewDirection = Jupiter;\n");
            bw.write("GMAT JupiterView.ViewScaleFactor = 1;\n");
            bw.write("GMAT JupiterView.ViewUpCoordinateSystem = JupiterInertial;\n");
            bw.write("GMAT JupiterView.ViewUpAxis = Z;\n");
            bw.write("GMAT JupiterView.EclipticPlane = Off;\n");
            bw.write("GMAT JupiterView.XYPlane = On;\n");
            bw.write("GMAT JupiterView.WireFrame = Off;\n");
            bw.write("GMAT JupiterView.Axes = On;\n");
            bw.write("GMAT JupiterView.Grid = Off;\n");
            bw.write("GMAT JupiterView.SunLine = On;\n");
            bw.write("GMAT JupiterView.UseInitialView = On;\n");
            bw.write("GMAT JupiterView.StarCount = 7000;\n");
            bw.write("GMAT JupiterView.EnableStars = On;\n");
            bw.write("GMAT JupiterView.EnableConstellations = On;\n");
            bw.write("\n");
            bw.write("Create OrbitView SaturnView;\n");
            bw.write("GMAT SaturnView.SolverIterations = Current;\n");
            bw.write("GMAT SaturnView.UpperLeft = [ 0.2306843267108168 0.2348224513172967 ];\n");
            bw.write("GMAT SaturnView.Size = [ 0.2317880794701987 0.2577319587628866 ];\n");
            bw.write("GMAT SaturnView.RelativeZOrder = 15;\n");
            bw.write("GMAT SaturnView.Maximized = false;\n");
            bw.write("GMAT SaturnView.Add = {Cassini, Mars, Jupiter, Saturn, Sun, Neptune, Uranus, Pluto, Earth};\n");
            bw.write("GMAT SaturnView.CoordinateSystem = SaturnInertial;\n");
            bw.write("GMAT SaturnView.DrawObject = [ true true true true true true true true true ];\n");
            bw.write("GMAT SaturnView.DataCollectFrequency = 1;\n");
            bw.write("GMAT SaturnView.UpdatePlotFrequency = 50;\n");
            bw.write("GMAT SaturnView.NumPointsToRedraw = 0;\n");
            bw.write("GMAT SaturnView.ShowPlot = true;\n");
            bw.write("GMAT SaturnView.ShowLabels = true;\n");
            bw.write("GMAT SaturnView.ViewPointReference = Saturn;\n");
            bw.write("GMAT SaturnView.ViewPointVector = [ 0 0 1500000000 ];\n");
            bw.write("GMAT SaturnView.ViewDirection = Saturn;\n");
            bw.write("GMAT SaturnView.ViewScaleFactor = 1;\n");
            bw.write("GMAT SaturnView.ViewUpCoordinateSystem = SaturnInertial;\n");
            bw.write("GMAT SaturnView.ViewUpAxis = Z;\n");
            bw.write("GMAT SaturnView.EclipticPlane = Off;\n");
            bw.write("GMAT SaturnView.XYPlane = On;\n");
            bw.write("GMAT SaturnView.WireFrame = Off;\n");
            bw.write("GMAT SaturnView.Axes = On;\n");
            bw.write("GMAT SaturnView.Grid = Off;\n");
            bw.write("GMAT SaturnView.SunLine = On;\n");
            bw.write("GMAT SaturnView.UseInitialView = On;\n");
            bw.write("GMAT SaturnView.StarCount = 7000;\n");
            bw.write("GMAT SaturnView.EnableStars = On;\n");
            bw.write("GMAT SaturnView.EnableConstellations = On;\n");
            bw.write("\n");
            bw.write("Create ReportFile CassiniReport;\n");
            bw.write("GMAT CassiniReport.SolverIterations = Current;\n");
            bw.write("GMAT CassiniReport.UpperLeft = [ 0.005518763796909493 0.09621993127147767 ];\n");
            bw.write("GMAT CassiniReport.Size = [ 0.6015452538631346 0.7319587628865979 ];\n");
            bw.write("GMAT CassiniReport.RelativeZOrder = 127;\n");
            bw.write("GMAT CassiniReport.Maximized = true;\n");
            bw.write("GMAT CassiniReport.Filename = 'D:\\data\\src\\gmat\\cassini\\CassiniReport.txt';\n");
            bw.write("GMAT CassiniReport.Precision = 16;\n");
            bw.write("GMAT CassiniReport.Add = {Cassini.UTCGregorian, Cassini.Earth.RMAG};\n");
            bw.write("GMAT CassiniReport.WriteHeaders = true;\n");
            bw.write("GMAT CassiniReport.LeftJustify = On;\n");
            bw.write("GMAT CassiniReport.ZeroFill = Off;\n");
            bw.write("GMAT CassiniReport.ColumnWidth = 23;\n");
            bw.write("GMAT CassiniReport.WriteReport = true;\n");
            bw.write("\n\n");
            bw.flush();
            //
            // Write Mission Sequence
            //
            bw.write("%----------------------------------------\n");
            bw.write("%---------- Mission Sequence\n");
            bw.write("%----------------------------------------\n");
            bw.write("\n");
            bw.write("BeginMissionSequence;\n");
            li = scl.listIterator();
            sc1 = li.next();
            do {
                if (li.hasNext()) {
                    SpaceCraft sc2;
                    double tdiff;
                    do {
                        sc2 = li.next();
                        tdiff = sc2.getTdbJd() - sc1.getTdbJd();
                    } while (li.hasNext() && (tdiff < 1.0));
                    bw.write("GMAT 'Set' ");
                    bw.write(sc1.getName() + " = " + sc1.formatName() + ";");
                    bw.write("\n");
                    bw.write("Propagate 'To " + sc2.formatName() + "' ");
                    bw.write("PropSunSOI(Cassini) {Cassini.TDBModJulian = ");
                    bw.write(String.format("%.3f", (sc2.getTdbJd() - 2430000.0d)));
                    bw.write(", OrbitColor = [255 0 0]};");
                    sc1 = sc2;
                    bw.write("\n");
                    bw.flush();
                }
            } while (li.hasNext());

            bw.close();

        } catch (IOException | NumberFormatException ex) {
            Logger.getAnonymousLogger(XyzvToGmat.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
