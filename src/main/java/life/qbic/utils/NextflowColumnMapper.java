package life.qbic.utils;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import life.qbic.utils.NextflowTraceColumns;

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
        int columnIndex = 0;
        for (String col : header){
            NextflowTraceColumns name = findCorrespondingColumn(col);
            if(name != null)
                this.columnMap.put(name, columnIndex);
            columnIndex++;
        }
        return this;
    }

    public Set<NextflowTraceColumns> getHeader(){
        return this.columnMap.keySet();
    }

    private NextflowTraceColumns findCorrespondingColumn(String col){
        if (MappingInfo.map.keySet().contains(col)){
            return MappingInfo.map.get(col);
        } else {
            return null;
        }
    }

    public Integer getColIndexForType(NextflowTraceColumns col){
        return columnMap.get(col);
    }

    



    

}
