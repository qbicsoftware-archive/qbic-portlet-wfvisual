package life.qbic;


import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import life.qbic.utils.NextflowColumnMapper;
import life.qbic.utils.NextflowTraceColumns;

import java.util.ArrayList;

class TraceContainer{

    private List<Task> taskList;

    private NextflowColumnMapper columnMapper;

    public TraceContainer(){
        columnMapper = NextflowColumnMapper.getInstance();
        taskList = new ArrayList<>();
    }

    public void setTableHeader(String[] header){
        columnMapper.parseColumnsFromHeader(header);
    }

    public void addTableRow(String[] row){              
        parseTask(row);
    }


    private void parseTask(String[] row){
        Task newTask = new Task();
        newTask.setTaskId(Integer.parseInt(getColFromRow(NextflowTraceColumns.TASK_ID, row)));
        newTask.setCpusRequested(Integer.parseInt(getColFromRow(NextflowTraceColumns.CPUS, row)));
        newTask.setCpuUsed(Double.parseDouble(getColFromRow(NextflowTraceColumns.P_CPU, row).replace("%", ""))/100d);
        newTask.setProcess(getColFromRow(NextflowTraceColumns.PROCESS, row));
        newTask.setRealtime(getColFromRow(NextflowTraceColumns.REALTIME, row));
        taskList.add(newTask);
    }

    private String getColFromRow(NextflowTraceColumns col, String[] row){
        int index = columnMapper.getColIndexForType(col);
        return index != -1 ? row[index] : "";
    }

    public List<Task> getTaskList(){
        return this.taskList;
    }

    public Set<NextflowTraceColumns> getHeader(){
        return columnMapper.getHeader();
    }





    
}