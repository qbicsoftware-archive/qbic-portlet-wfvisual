package life.qbic.charts;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

import com.vaadin.addon.charts.Chart;
import com.vaadin.addon.charts.model.Configuration;
import com.vaadin.addon.charts.util.SVGGenerator;
import com.vaadin.server.FileDownloader;
import com.vaadin.server.StreamResource;
import com.vaadin.server.StreamResource.StreamSource;
import com.vaadin.ui.Button;

public class Download{

    public Download(){}

    public Button createDownloadButton(String descriptor, Chart chart){
        Configuration conf = chart.getConfiguration();
        return createButton(descriptor, "chart.svg", createSVGStreamSource(conf));
    }

    private StreamSource createSVGStreamSource(Configuration conf){
        return new StreamSource(){
            @Override
            public InputStream getStream(){
                String svg = SVGGenerator.getInstance().generate(conf);
                if (svg != null) {
                    return new ByteArrayInputStream(svg.getBytes());
                }
                return null;
            }
        };
    }

    private Button createButton(String descriptor, String filename, StreamSource ss){
        Button button = new Button(descriptor);
        FileDownloader downloader = new FileDownloader(new StreamResource(ss, filename));
        downloader.extend(button);
        return button;
    }

}