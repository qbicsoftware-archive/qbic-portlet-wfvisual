package life.qbic.utils;

import java.util.ArrayList;
import java.util.List;

import org.joda.time.Period;
import org.joda.time.format.PeriodFormatter;
import org.joda.time.format.PeriodFormatterBuilder;

public class PeriodFormatTester {

    List<PeriodFormatter> formatterList;

    public PeriodFormatTester() {
        formatterList = new ArrayList<>();
        buildFormatters();
    }

    private void buildFormatters(){
        formatterList.add(new PeriodFormatterBuilder()
                            .appendDays().appendSuffix("d ")
                            .appendHours().appendSuffix("h ")
                            .appendMinutes().appendSuffix("m ")
                            .appendSeconds().appendSuffix("s ")
                            .toFormatter());

        formatterList.add(new PeriodFormatterBuilder()
                            .appendHours().appendSuffix("h ")
                            .appendMinutes().appendSuffix("m ")
                            .appendSeconds().appendSuffix("s ")
                            .toFormatter());
        
        formatterList.add(new PeriodFormatterBuilder()
                            .appendMinutes().appendSuffix("m ")
                            .appendSeconds().appendSuffix("s")
                            .toFormatter());
        
        formatterList.add(new PeriodFormatterBuilder()
                            .appendMinutes().appendSuffix("m")
                            .toFormatter());

        formatterList.add(new PeriodFormatterBuilder()
                            .appendSeconds().appendSuffix("s")
                            .toFormatter());
        
    }

    public Period parseTime(String s){
        Period p = null;

        for (PeriodFormatter pF : formatterList){
            try{
                p = pF.parsePeriod(s);
                return p;
            } catch (IllegalArgumentException exc){
                continue;
            }
        }
        return p;
    }

}
