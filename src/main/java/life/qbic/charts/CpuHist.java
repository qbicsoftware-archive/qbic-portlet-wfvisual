package life.qbic.charts;

import com.vaadin.addon.charts.Chart;
import com.vaadin.addon.charts.model.ChartType;

public class CpuHist extends Chart{

    public CpuHist(){
        super(ChartType.BAR);
        this.getConfiguration().setTitle("Histogram of CPU time");
        this.getConfiguration().setSubTitle("This rocks!");
    }

}