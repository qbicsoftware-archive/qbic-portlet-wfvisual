package life.qbic;

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
import java.util.Iterator;
import java.util.List;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.stream.Collector;
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
import com.vaadin.addon.charts.model.Configuration;
import com.vaadin.addon.charts.model.ListSeries;
import com.vaadin.addon.charts.model.XAxis;
import com.vaadin.addon.charts.model.YAxis;
import com.vaadin.addon.charts.Chart;
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

    HorizontalLayout chartArea;

    Upload fileUpload;

    Label fileNameLabel;

    TraceContainer traceContainer;

    public UserInterfaceImpl(){

        mainBody = new VerticalLayout();
        uploadArea = new HorizontalLayout();
        chartArea = new HorizontalLayout();

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


            Iterator<String[]> iterator = traceContainer.iterator();
            while(iterator.hasNext()){
                for(String value : iterator.next()){
                    System.out.print(value + "\t");
                }
                System.out.print("\n");
            }

            InterfaceController iController = new InterfaceController();
            iController.loadCpuHistogram();
            iController.loadCpuPerformance();
        
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
            Histogram hist = new Histogram();
            List<String> tmp = traceContainer.getColumnValues("%cpu");
            this.cpuUsage = tmp.stream().map(v -> v.replace("%", ""))
                                                .filter(e -> isNumeric(e))
                                                .map(Double::parseDouble)
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
            chartArea.addComponent(cpuHist);
            
        }

        void loadCpuPerformance(){
            Chart cpuPerformance = new CpuPerformance();
            Configuration config = cpuPerformance.getConfiguration();
            List<String> tmp = traceContainer.getColumnValues("cpus");
        
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
            chartArea.addComponent(cpuPerformance);

            mainBody.addComponent(new Download().createDownloadButton("CPU Performance SVG", cpuPerformance));
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