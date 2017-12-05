package life.qbic.charts;

import com.vaadin.addon.charts.Chart;
import com.vaadin.addon.charts.model.ChartType;
import com.vaadin.addon.charts.model.PlotOptionsSeries;
import com.vaadin.addon.charts.model.Stacking;
import com.vaadin.addon.charts.model.ZoomType;

public class WallTimeChart extends Chart{

    public WallTimeChart(){
        super(ChartType.BAR);
        this.getConfiguration().setTitle("Walltime Overview");
        this.getConfiguration().setSubTitle("Don't reserve to much resources!");
        this.getConfiguration().getChart().setZoomType(ZoomType.XY);

        PlotOptionsSeries plot = new PlotOptionsSeries();
        plot.setStacking(Stacking.NORMAL);
        this.getConfiguration().setPlotOptions(plot);
    }

}