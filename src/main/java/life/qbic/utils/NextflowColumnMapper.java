package life.qbic.utils;

import java.util.HashMap;
import java.util.Map;

life.qbic.utils.NextflowTraceColumns;

public class NextflowColumnMapper{

    private Map<NextflowTraceColumns, Integer> columnMap = new HashMap<>();

    private static final class InstanceHolder {
        static final NextflowColumnMapper INSTANCE = new NextflowColumnMapper();
    }

    private NextflowColumnMapper(){
        createEmptyMap();
    }

    public static NextflowColumnMapper getInstance() {
        return InstanceHolder.INSTANCE;
    }

    private void createEmptyMap(){
        for (NextflowTraceColumns column : NextflowTraceColumns.values()){
            columnMap.put(column, -1);
        }
    }

    public NextflowColumnMapper parseColumnsFromHeader(String[] header){

        return this;
    }

    private NextflowTraceColumns findCorrespondingColumn(String col){
        
    }



    

}
