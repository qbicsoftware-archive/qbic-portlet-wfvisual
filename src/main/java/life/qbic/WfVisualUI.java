package life.qbic;

import javax.portlet.PortletContext;
import javax.portlet.PortletSession;

import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.vaadin.annotations.Theme;
import com.vaadin.annotations.Widgetset;
import com.vaadin.server.Page;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.WrappedPortletSession;
import com.vaadin.ui.Notification;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import life.qbic.portal.liferayandvaadinhelpers.main.LiferayAndVaadinUtils;


@Theme("mytheme")
@SuppressWarnings("serial")
@Widgetset("life.qbic.AppWidgetSet")
public class WfVisualUI extends UI {

    private static Log log = LogFactoryUtil.getLog(WfVisualUI.class);

    private static Boolean testing = true;

    @Override
    protected void init(VaadinRequest request) {
        UI.getCurrent().setPollInterval( -1 );
        String portletContextName = "Testing";
        Integer numOfRegisteredUsers = 1;

        String userID = "MISSING SCREENNAME";
        if (LiferayAndVaadinUtils.isLiferayPortlet()) {
            userID = LiferayAndVaadinUtils.getUser().getScreenName();
            portletContextName = getPortletContextName(request);
            testing = false;
        }


        UserInterfaceBuilder uiBuilder = new UserInterfaceBuilder();
        VerticalLayout userInterface = uiBuilder.build();
        userInterface.setMargin(true);
        setContent(userInterface);

    }

    /**
     * Get the portlet's context name
     * @param request The vaadin request
     * @return The context name of the portlet
     */
    private String getPortletContextName(VaadinRequest request) {
        WrappedPortletSession wrappedPortletSession = (WrappedPortletSession) request
                .getWrappedSession();
        PortletSession portletSession = wrappedPortletSession
                .getPortletSession();

        final PortletContext context = portletSession.getPortletContext();
        final String portletContextName = context.getPortletContextName();
        return portletContextName;
    }

    /**
     * Small wrapper for notifications
     * @param message the message
     * @param type the message type
     */
    private void showNotiffication(String message, Notification.Type type){
        Notification note = new Notification(message, type);
        note.show(Page.getCurrent());

    }

}
