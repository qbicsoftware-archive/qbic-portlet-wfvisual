package life.qbic.charts;

import java.util.Arrays;

import org.apache.commons.lang.ArrayUtils;

public class Histogram {

    private double[] dataSeries;

    private int[] counts;

    private double[] breaks;

    public Histogram (){
        this.counts = new int[0];
        this.breaks = new double[0];
    }

    public Histogram setData(double[] data){
        this.dataSeries = data;
        return this;
    }

    public Histogram setData(Double[] data){
        this.dataSeries = ArrayUtils.toPrimitive(data);
        return this;
    }

    public Histogram compute(){
        computeHistogram();
        return this;
    }

    public int[] getCounts(){
        return this.counts;
    }

    public double[] getBreaks(){
        return this.breaks;
    }

    private void computeHistogram(){
        computeBreaks();
        binAndCount();
    }

    /**
     * We use the simple 'square root choice'
     * method for the estimation of the number
     * of bins for the histogram.
     */
    private void computeBreaks(){
        // Compute the number of bins based on 'square root choice'
        int k = (int) Math.sqrt(dataSeries.length);

        // Sort the array and extract min + max
        Arrays.sort(dataSeries);
        double min = dataSeries[0];
        double max = dataSeries[dataSeries.length-1];

        // Compute the bin width (roughly)
        double h = Math.ceil((max - min) / k);
        this.breaks = new double[k];

        // Compute the breaks
        for (int i = 0; i < k; i++){
            this.breaks[i] = (double) min + ( (i+1) * h ); 
        }
    }

    /**
     * Simple binning based on the breaks
     */
    private void binAndCount(){
        this.counts = new int[this.breaks.length];
        int memIndex = 0;
        for (int i = 0; i < this.breaks.length; i++){
            for (int j = memIndex; j < this.dataSeries.length; j++){
                if (this.dataSeries[j] < this.breaks[i]){
                    this.counts[i] += 1;
                } else {
                    memIndex = j + 1;
                    break;
                }
            }
        }

    }



}