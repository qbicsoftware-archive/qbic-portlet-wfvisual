package life.qbic.charts;

import java.util.Arrays;

public class Histogram <Type extends Number>{

    private Type[] dataSeries;

    private int[] counts;

    private double[] breaks;

    public Histogram (){
        this.counts = new int[0];
        this.breaks = new double[0];
    }

    public Histogram<Type> setData(Type[] data){
        this.dataSeries = data;
        return this;
    }

    public Histogram<Type> compute(){
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
        Type min = dataSeries[0];
        Type max = dataSeries[dataSeries.length-1];

        // Compute the bin width (roughly)
        int h = (int) Math.ceil(((double) max - (double) min) / k);
        this.breaks = new double[k];

        // Compute the breaks
        for (int i = 1; i <= k; i++){
            this.breaks[i] = (double) min + ( k * h ); 
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
                if ((int) this.dataSeries[j] < this.breaks[i]){
                    this.counts[i] += 1;
                } else {
                    memIndex = j + 1;
                    break;
                }
            }
        }

    }



}