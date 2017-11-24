package life.qbic;

import com.liferay.portal.kernel.servlet.WrapHttpServletRequestFilter;
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
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Iterator;
import java.util.UUID;
import com.vaadin.ui.Upload.FinishedEvent;
import com.vaadin.ui.Upload.Receiver;
import com.vaadin.ui.Upload.StartedEvent;
import com.vaadin.ui.Upload.SucceededEvent;
import com.vaadin.ui.Upload.SucceededListener;
import com.vaadin.ui.Upload;

/**
 * The implementation of the user interface with upload
 * dialog and visualization of important stats from the
 * Nextflow trace file.
 * @author: Sven Fillinger
 */
class UserInterfaceImpl implements UserInterface{

    private VerticalLayout mainBody;

    private HorizontalLayout uploadArea;

    private HorizontalLayout chartArea;

    private Upload fileUpload;

    private Label fileNameLabel;

    private TraceContainer traceContainer;

    private BufferedReader reader;

    private UUID uuid;

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
            boolean headerWritten = false;
            BufferedReader bfReader = new BufferedReader(new InputStreamReader(new ByteArrayInputStream(os.toByteArray())));
            try {
                String line = null;
                while ((line = bfReader.readLine()) != null){
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

            System.out.println("Header size: " + traceContainer.getHeader().length);

            Iterator<String[]> iterator = traceContainer.iterator();
            while(iterator.hasNext()){
                for(String value : iterator.next()){
                    System.out.print(value + "\t");
                }
                System.out.print("\n");
            }
        
        }
        
		@Override
		public OutputStream receiveUpload(String filename, String mimeType) {
            traceContainer = new TraceContainer<>();
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

}