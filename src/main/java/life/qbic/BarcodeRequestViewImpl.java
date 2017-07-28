package life.qbic;

import com.vaadin.ui.Label;
import com.vaadin.ui.OptionGroup;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;

/**
 * View that will display different barcode request cases for
 * the UKT diagnostics scenario:
 *
 *  1) Request new patientID/extractID pair
 *     Use case: A new patient will be added to CentraXX and a new
 *     ID tuple is needed.
 *
 *  2) Request new extractID for already existing patientID
 *     Use case: Repeated NGS run on the same tissue.
 *
 */
public class BarcodeRequestViewImpl implements BarcodeRequestView{

    private OptionGroup taskSelection;

    private VerticalLayout fullView;

    public BarcodeRequestViewImpl(){
        initView();
    }

    private void initView(){
        fullView = new VerticalLayout();
        fullView.addComponent(new Label("It works"));
    }

    @Override
    public TextField getPatientIdField() {
        return null;
    }

    @Override
    public TextField getSampleIdField() {
        return null;
    }

    @Override
    public OptionGroup getTaskSelectionGroup() {
        return null;
    }

    @Override
    public VerticalLayout getFullView() {
        return fullView;
    }
}
