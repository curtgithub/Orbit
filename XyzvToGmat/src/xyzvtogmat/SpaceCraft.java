/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package xyzvtogmat;

/**
 *
 * @author owner
 */
public class SpaceCraft {

    private String name;
    private double tdbJd;
    private String sx;
    private String sy;
    private String sz;
    private String svx;
    private String svy;
    private String svz;

    public SpaceCraft(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getTdbJd() {
        return tdbJd;
    }

    public void setTdbJd(double tdbJd) {
        this.tdbJd = tdbJd;
    }

    public String getSx() {
        return sx;
    }

    public void setSx(String sx) {
        this.sx = sx;
    }

    public String getSy() {
        return sy;
    }

    public void setSy(String sy) {
        this.sy = sy;
    }

    public String getSz() {
        return sz;
    }

    public void setSz(String sz) {
        this.sz = sz;
    }

    public String getSvx() {
        return svx;
    }

    public void setSvx(String svx) {
        this.svx = svx;
    }

    public String getSvy() {
        return svy;
    }

    public void setSvy(String svy) {
        this.svy = svy;
    }

    public String getSvz() {
        return svz;
    }

    public void setSvz(String svz) {
        this.svz = svz;
    }

    public String formatName() {
        String utcGregorian = JulianToGregorian.formatUTCGregorian(this.getTdbJd());
        String fm = utcGregorian;
        fm = fm.replaceAll(" ", "");
        fm = fm.replace(":", "");
        fm = fm.replace(".", "");
        String formatName = new String(this.getName() + fm);
        return formatName;
    }

    public String toNameString() {
        String s = new String();

        String utcGregorian = JulianToGregorian.formatUTCGregorian(this.getTdbJd());
        String fmName = this.getName();

        //System.out.println("fmName = " + fmName);
        //System.exit(0);
        s = s + "Create Spacecraft " + fmName + ";\n";
        s = s + "GMAT " + fmName + ".DateFormat = UTCGregorian;\n";
        s = s + "GMAT " + fmName + ".Epoch = '" + utcGregorian + "';\n";

        // s = s + "GMAT " + fmName + ".DateFormat = UTCModJulian;\n";
        // s = s + "GMAT " + fmName + ".Epoch = '" + utcModJd + "';\n";
        s = s + "GMAT " + fmName + ".CoordinateSystem = SunEcliptic;\n";
        s = s + "GMAT " + fmName + ".DisplayStateType = Cartesian;\n";

        s = s + "GMAT " + fmName + ".X = " + this.getSx() + ";\n";
        s = s + "GMAT " + fmName + ".Y = " + this.getSy() + ";\n";
        s = s + "GMAT " + fmName + ".Z = " + this.getSz() + ";\n";

        s = s + "GMAT " + fmName + ".VX = " + this.getSvx() + ";\n";
        s = s + "GMAT " + fmName + ".VY = " + this.getSvy() + ";\n";

        s = s + "\n\n";

        return s;
    }

    public String toFormatNameString() {
        String s = new String();

        String fmName = this.formatName();

        s = s + "Create Spacecraft " + fmName + ";\n";
        s = s + "GMAT " + fmName + ".DateFormat = UTCGregorian;\n";
        
        String utcGregorian = JulianToGregorian.formatUTCGregorian(this.getTdbJd());

        s = s + "GMAT " + fmName + ".Epoch = '" + utcGregorian + "';\n";

// s = s + "GMAT " + fmName + ".DateFormat = UTCModJulian;\n";
// s = s + "GMAT " + fmName + ".Epoch = '" + utcModJd + "';\n";
        s = s + "GMAT " + fmName + ".CoordinateSystem = SunEcliptic;\n";
        s = s + "GMAT " + fmName + ".DisplayStateType = Cartesian;\n";

        s = s + "GMAT " + fmName + ".X = " + this.getSx() + ";\n";
        s = s + "GMAT " + fmName + ".Y = " + this.getSy() + ";\n";
        s = s + "GMAT " + fmName + ".Z = " + this.getSz() + ";\n";

        s = s + "GMAT " + fmName + ".VX = " + this.getSvx() + ";\n";
        s = s + "GMAT " + fmName + ".VY = " + this.getSvy() + ";\n";

        s = s + "\n\n";

        return s;
    }

    @Override
    public String toString() {
        return this.toFormatNameString();
    }

}
