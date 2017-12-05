package life.qbic.utils;

public class NextflowColumnMapper{

    private static final class InstanceHolder {
        static final NextflowColumnMapper INSTANCE = new NextflowColumnMapper();
    }

    private NextflowColumnMapper(){}

    public static NextflowColumnMapper getInstance() {
        return InstanceHolder.INSTANCE;
    }

}
