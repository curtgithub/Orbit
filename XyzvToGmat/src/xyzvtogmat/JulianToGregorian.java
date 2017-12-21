/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package xyzvtogmat;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;

/**
 *
 * @author owner
 */
public class JulianToGregorian {

//      http://pmyers.pcug.org.au/General/JulianDates.htm#J2G
//      j = j - 1721119 ;
//	year = (4 * j - 1) / 146097 ; j = 4 * j - 1 - 146097 * year ; day = j / 4 ;
//	j = (4 * day + 3) / 1461 ; day = 4 * day + 3 - 1461 * j ; day = (day + 4) / 4 ;
//	month = (5 * day - 3) / 153 ; day = 5 * day - 3 - 153 * month ; day = (day + 5) / 5 ;
//	year = 100 * year + j ;
//	if month < 10 then
//	    month = month + 3
//	else
//	    month = month - 9 ; year = year + 1
//	end if
    
    public static DateFormat UTCGregorianFormat = new SimpleDateFormat("dd MMM yyyy HH:mm:ss.SSS");

    public static String formatUTCGregorian(double tbdJd) {
        String utcGregorian;

        long jdn = Math.round(tbdJd);
        long j = jdn - 1721119l;

        long year = (4l * j - 1l) / 146097l;
        j = 4l * j - 1l - 146097l * year;
        long day = j / 4l;

        j = (4l * day + 3l) / 1461;
        day = 4l * day + 3l - 1461l * j;
        day = (day + 4l) / 4l;

        long month = (5l * day - 3l) / 153l;
        day = 5l * day - 3l - 153l * month;
        day = (day + 5l) / 5l;

        year = 100l * year + j;
        if (month < 10) {
            month = month + 3l;
        } else {
            month = month - 9l;
            year = year + 1l;
        }

        long jdf = Math.round((tbdJd - jdn) * 100000000.0d);
        //System.out.println("jdf = " + jdf + "\n");

        long hhUnit = Math.round((1.0d / 24.0d) * 100000000.0d);
        long hh = (jdf / hhUnit);

        //System.out.println("hhUnit = " + hhUnit + "\n");
        if (jdf < 0l) {
            jdf = jdf + (hhUnit * 12);
            jdf = jdf - hh * hhUnit;
        } else {
            jdf = jdf - ((hh - 12l) * hhUnit);
        }
        //System.out.println("tbdJd = " + tbdJd + "\n");
        //System.out.println("jdn = " + jdn + "\n");
        //System.out.println("jdf = " + jdf + "\n");
        //System.out.println("hh = " + hh + "\n");

        long mmUnit = Math.round((1.0d / 1440.0d) * 100000000.0d);
        long mm = jdf / mmUnit;
        //System.out.println("mm = " + mm + "\n");
        jdf = jdf - mm * mmUnit;

        long ssUnit = Math.round((1.0d / 86400.0d) * 100000000.0d);
        //System.out.println("ssUnit = " + ssUnit + "\n");
        long ss = jdf / ssUnit;
        //System.out.println("ss = " + ss + "\n");
        // TODO: Millisecond jdf = jdf - ss * mmUnit;
        //System.out.println("jdf = " + jdf + "\n");

        Calendar cal = new GregorianCalendar((int) year, (int) month - 1, (int) day,
                (int) hh, (int) mm, (int) ss);

        utcGregorian = UTCGregorianFormat.format(cal.getTime());
        //System.out.println("utcGregorian = " + utcGregorian + "\n");

        return utcGregorian;
    }

    public static int diffSeconds(double tdbJd2, double tdbJd1) {
        int seconds = 0;

        double ddf = tdbJd2 - tdbJd1;
        long dsl = Math.round(ddf * 100000000.0d);

        return seconds;
    }

}
