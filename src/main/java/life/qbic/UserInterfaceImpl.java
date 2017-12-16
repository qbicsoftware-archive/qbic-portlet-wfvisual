package life.qbic;

import com.vaadin.ui.Button;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.Map;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.stream.Collectors;

import com.vaadin.ui.Upload.FinishedEvent;
import com.vaadin.ui.Upload.Receiver;
import com.vaadin.ui.Upload.StartedEvent;
import com.vaadin.ui.Upload.SucceededEvent;
import com.vaadin.ui.Upload.SucceededListener;

import life.qbic.charts.CpuHist;
import life.qbic.charts.Download;
import life.qbic.charts.CpuPerformance;
import life.qbic.charts.Histogram;
import life.qbic.charts.WallTimeChart;
import life.qbic.utils.PeriodFormatTester;

import org.joda.time.Period;
import com.vaadin.addon.charts.model.Configuration;
import com.vaadin.addon.charts.model.ListSeries;
import com.vaadin.addon.charts.model.XAxis;
import com.vaadin.addon.charts.model.YAxis;
import com.vaadin.client.RenderInformation.Size;
import com.vaadin.addon.charts.Chart;
import com.vaadin.addon.charts.PointClickEvent;
import com.vaadin.addon.charts.PointClickListener;
import com.vaadin.ui.Upload;

/**
 * The implementation of the user interface with upload
 * dialog and visualization of important stats from the
 * Nextflow trace file.
 * @author: Sven Fillinger
 */
class UserInterfaceImpl implements UserInterface{

    VerticalLayout mainBody;

    HorizontalLayout uploadArea;

    VerticalLayout chartArea;

    Upload fileUpload;

    Label fileNameLabel;

    TraceContainer traceContainer;

    public UserInterfaceImpl(){

        mainBody = new VerticalLayout();
        uploadArea = new HorizontalLayout();
        chartArea = new VerticalLayout();

        traceContainer = new TraceContainer();

        StatsReceiver statsReceiver = new StatsReceiver();
        fileUpload = new Upload ("Upload it here", statsReceiver);
        fileUpload.addStartedListener(statsReceiver);
        fileUpload.addProgressListener(statsReceiver);
        fileUpload.addSucceededListener(statsReceiver);
        fileUpload.addFinishedListener(statsReceiver);

        fileNameLabel = new Label("Filename will appear here after upload!");
        
        uploadArea.addComponent(fileUpload);
        chartArea.addComponent(fileNameLabel);
        
        mainBody.addComponents(uploadArea, chartArea);


    }

	@Override
	public VerticalLayout build() {
		return this.mainBody;
    }
    
    /**
     * Handles uploads events and writes the received data in an OutputStream that
     * can be used for further manipulation.
     * Also updates the upload progress label in the view.
     */
    class StatsReceiver implements Receiver, SucceededListener, Upload.StartedListener,
                                   Upload.FinishedListener, Upload.ProgressListener {

        ByteArrayOutputStream os = new ByteArrayOutputStream(10240); 
    
		@Override
		public void uploadFinished(FinishedEvent event) {
            UI.getCurrent().setPollInterval(-1);
		}

		@Override
		public void uploadSucceeded(SucceededEvent event) {
            fileNameLabel.setValue("Upload was successful.");
            chartArea.removeAllComponents();
            
            boolean headerWritten = false;
            BufferedReader bfReader = new BufferedReader(new InputStreamReader(new ByteArrayInputStream(os.toByteArray())));
            try {
                String line = null;
                while ((line = bfReader.readLine()) != null){
                    if(line.contains("get_software_versions"))
                        continue;
                    if(headerWritten){
                        traceContainer.addTableRow(line.trim().split("\t"));
                    } else {
                        traceContainer.setTableHeader(line.trim().split("\t"));
                        headerWritten = true;
                    }
                }
            } catch (IOException exc){
                exc.printStackTrace();
            }

            InterfaceController iController = new InterfaceController();
            iController.loadCpuHistogram();
            iController.loadCpuPerformance();
            iController.loadWallTimeEffifiencyChart();
        
        }
        
		@Override
		public OutputStream receiveUpload(String filename, String mimeType) {
            traceContainer = new TraceContainer();
            os.reset();
            return os;
		}

		@Override
		public void updateProgress(long readBytes, long contentLength) {
            String msg = String.format("%.2f%% uploaded ...", 100 * readBytes / (float) contentLength);
            fileNameLabel.setValue(msg);
		}

		@Override
		public void uploadStarted(StartedEvent event) {
			UI.getCurrent().setPollInterval(100);
		}

    }

    class InterfaceController {

        List<Double> cpuUsage;
        List<Double> cpuRessources;
        List<Double> cpuEfficiency;

        InterfaceController(){}

        void loadCpuHistogram(){
            VerticalLayout container = new VerticalLayout();
            Histogram hist = new Histogram();
        
            this.cpuUsage = traceContainer.getTaskList().stream().map(task -> task.getCpuUsed())
                                                .collect(Collectors.toList());
            hist.setData(cpuUsage.toArray(new Double[cpuUsage.size()]));                            
            hist.compute();
            
            double[] breaks = hist.getBreaks();
            int[] counts = hist.getCounts();

            Chart cpuHist = new CpuHist();
            Configuration config = cpuHist.getConfiguration();
            ListSeries list = new ListSeries();
            for(Integer count : counts){
                list.addData(count);
            }
            config.addSeries(list);
           
            container.addComponent(cpuHist);
            Button downloadButton = new Download().createDownloadButton("Get Chart as SVG", "cpu_histogram", cpuHist);
            container.addComponent(downloadButton);
            chartArea.addComponent(container);
            
        }

        void loadCpuPerformance(){
            VerticalLayout container = new VerticalLayout();
            Chart cpuPerformance = new CpuPerformance();
            Configuration config = cpuPerformance.getConfiguration();
        
            List<Task> taskList = traceContainer.getTaskList().stream()
                                                .filter(task -> task.getCpusRequested() > 1)
                                                .collect(Collectors.toList());

            // Now sort it based on task id
            taskList.sort(Comparator.comparing(Task::getProcess));

            ListSeries requestedCPU = new ListSeries();
            requestedCPU.setName("Remaining CPU resources");
            ListSeries usedCPU = new ListSeries();
            usedCPU.setName("Used CPUs");
            for(Task task : taskList){
                usedCPU.addData(task.getCpuUsed());
                requestedCPU.addData((double) task.getCpusRequested()-task.getCpuUsed());
            }
            config.addSeries(requestedCPU);
            config.addSeries(usedCPU);

            YAxis yAxis = new YAxis();
            yAxis.setTitle("Number of CPUs");
            
            XAxis xAxis = new XAxis();
            xAxis.setTitle("Processes");
            taskList.forEach((Task task) -> xAxis.addCategory(task.getProcess()));
            config.addxAxis(xAxis);
            config.addyAxis(yAxis);

            container.addComponent(cpuPerformance);
            Button downloadButton = new Download().createDownloadButton("Get Chart as SVG", "cpu_performance", cpuPerformance);
            container.addComponent(downloadButton);
            chartArea.addComponent(container);
        }

        /**
         * A chart that visualizes the recorded walltime
         * per process in comparison to the reserved cluster
         * time per process. 
         */
        void loadWallTimeEffifiencyChart(){
            VerticalLayout container = new VerticalLayout();
            PeriodFormatTester pfTester = new PeriodFormatTester();
            
            List<Task> taskList = traceContainer.getTaskList();
            
            // Ensure that tasks are sorted by process
            taskList.sort(Comparator.comparing(Task::getProcess));

            // Retrieve the duration times per process
            Map<String, List<Period>> durationsPerProcess = new HashMap<>();
            for (Task t : taskList) {
                String process = t.getProcess();
                Period p = pfTester.parseTime(t.getRealtime());
                if (p == null)
                    continue;
                if (durationsPerProcess.get(process) == null){
                    durationsPerProcess.put(process, new ArrayList<>());
                } else {
                    durationsPerProcess.get(process).add(p);
                }
            }

            // Compute the total time per process
            Map<String, Integer> totalTimePerProcess = computeSecondsPerProcess(durationsPerProcess);

            // Get the reserved time for each single job
            Map<String, List<Period>> reservedTimePerProcess = new HashMap<>();
            for (Task t : taskList) {
                String process = t.getProcess();
                Period p = pfTester.parseTime(t.getTime());
                if (p == null)
                    continue;
                if (reservedTimePerProcess.get(process) == null){
                    reservedTimePerProcess.put(process, new ArrayList<>());
                } else {
                    reservedTimePerProcess.get(process).add(p);
                }
            }

            // Compute the total time per process
            Map<String, Integer> totalReservedTimePerProcess = computeSecondsPerProcess(reservedTimePerProcess);
            
            
            // Fit the data into the chart
            ListSeries realtime = new ListSeries();
            ListSeries reservedTime = new ListSeries();
            XAxis xAxis = new XAxis();
            YAxis yAxis = new YAxis();
            totalTimePerProcess.forEach((process, sumDuration) -> {
                realtime.addData(round(sumDuration/60.0, 2));
                reservedTime.addData(round(totalReservedTimePerProcess.get(process)/60.0, 2));
                xAxis.addCategory(process);
            });

            realtime.setName("Real time used [minutes]");
            reservedTime.setName("Real time reserved [minutes]");
            yAxis.setTitle("Duration (minutes)");

            Chart walltimeEfficiency = new WallTimeChart();
            Configuration config = walltimeEfficiency.getConfiguration();
            config.addxAxis(xAxis);
            config.addyAxis(yAxis);
            config.addSeries(realtime);
            config.addSeries(reservedTime);

            container.addComponent(walltimeEfficiency);
            Button downloadButton = new Download().createDownloadButton("Get Chart as SVG", "walltime_efficiency", walltimeEfficiency);
            container.addComponent(downloadButton);
            chartArea.addComponent(container);


        }

        private double round(double value, int places) {
            if (places < 0) throw new IllegalArgumentException();
        
            BigDecimal bd = new BigDecimal(value);
            bd = bd.setScale(places, RoundingMode.HALF_UP);
            return bd.doubleValue();
        }

        private Map<String, Integer> computeSecondsPerProcess(Map<String, List<Period>> durationsPerProcess) {
            Map<String, Integer> secondsPerProcess = new HashMap<>();
            durationsPerProcess.forEach((process, periodList) -> {
                int numberOfProcesses = (periodList.size() > 0) ? periodList.size() : 1;
                secondsPerProcess.put(process, 
                                    convertPeriodToSeconds(periodList.stream().reduce(new Period(0), (p0, p1) -> p0.plus(p1)))/numberOfProcesses);
            });
			return secondsPerProcess;
        }
        
        private Integer convertPeriodToSeconds(Period p){
            return p.getDays()*24*60*60 + p.getHours()*60*60 + p.getMinutes()*60 + p.getSeconds();
        }

		public boolean isNumeric(String str)  
        {  
          try  
          {  
            double d = Double.parseDouble(str);  
          }  
          catch(NumberFormatException nfe)  
          {  
            return false;  
          }  
          return true;  
        }

        private List<Double> computeLogRatios (List<Double> oneList, List<Double> anotherList){
            List<Double> logRatios = new ArrayList<>();
            if (oneList.size() != anotherList.size()){
                return logRatios;
            }
            for (int i = 0; i < oneList.size(); i++){
                double valueOne = oneList.get(i);
                double valueTwo = anotherList.get(i);
                double logRatio = Math.log10(valueOne) - Math.log10(valueTwo/100);
                logRatios.add(logRatio);
            }
            return logRatios;
        }

        private Double computeLogRatio (Double value1, Double value2){
            return Math.log10(value1) - Math.log10(value2);
        }
        

    }

}