package life.qbic;

import com.vaadin.ui.Button;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;

import java.io.IOException;
import java.io.OutputStream;
import com.vaadin.ui.Upload.FinishedEvent;
import com.vaadin.ui.Upload.Receiver;
import com.vaadin.ui.Upload.StartedEvent;
import com.vaadin.ui.Upload.SucceededEvent;
import com.vaadin.ui.Upload.SucceededListener;
import com.vaadin.ui.Upload;

class UserInterfaceImpl implements UserInterface{

    private VerticalLayout mainBody;

    private HorizontalLayout uploadArea;

    private HorizontalLayout chartArea;

    private Upload fileUpload;

    private Label fileNameLabel;

    public UserInterfaceImpl(){
        mainBody = new VerticalLayout();
        uploadArea = new HorizontalLayout();
        chartArea = new HorizontalLayout();

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
    

    class StatsReceiver implements Receiver, SucceededListener, Upload.StartedListener,
                                   Upload.FinishedListener, Upload.ProgressListener {
    
		@Override
		public void uploadFinished(FinishedEvent event) {
		}

		@Override
		public void uploadSucceeded(SucceededEvent event) {
			fileNameLabel.setValue("Upload was successful.");
        }
        
		@Override
		public OutputStream receiveUpload(String filename, String mimeType) {
            fileNameLabel.setValue("Upload in progress...");

			return new OutputStream(){
            
                @Override
                public void write(int arg0) throws IOException {
                    try {
						Thread.sleep(1);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
                }
            };
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