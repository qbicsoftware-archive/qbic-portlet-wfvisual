package life.qbic.charts;

import com.vaadin.addon.charts.Chart;
import com.vaadin.addon.charts.model.ChartType;
import com.vaadin.addon.charts.model.ZoomType;

public class CpuPerformance extends Chart{

    public CpuPerformance(){
        super(ChartType.BAR);
        this.getConfiguration().setTitle("CPU Performance");
        this.getConfiguration().setSubTitle("This rocks!");
        this.getConfiguration().getChart().setZoomType(ZoomType.XY);
    }

}