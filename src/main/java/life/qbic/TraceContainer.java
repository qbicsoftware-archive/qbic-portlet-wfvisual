package life.qbic;


import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.ArrayList;

class TraceContainer implements Iterable<String[]>{

    private Map<String, List<String>> traceTable;

    private Map<Integer, String> traceColumnNoToName;

    private List<Task> taskList;

    public TraceContainer(){
        traceTable = new HashMap<>();
        traceColumnNoToName = new HashMap<>();
        taskList = new ArrayList<>();
    }

    public void setTableHeader(String[] header){
        int columnNumber = 0;
        for (String columnName : header) {
            traceColumnNoToName.put(columnNumber, columnName);
            columnNumber++;
        }
    }

    public void addTableRow(String[] row){
        int currCol = 0;
        for (String columnValue : row) {
            String matchingColumn = traceColumnNoToName.get(currCol);
            try {
                traceTable.get(matchingColumn).add(columnValue);
            }  catch (NullPointerException exc){
                traceTable.put(matchingColumn, new ArrayList<>());
                traceTable.get(matchingColumn).add(columnValue);
            }
            currCol++;
        }
        parseTask(row);
    }


    private void parseTask(String[] row){
        Task newTask = new Task();
        newTask.setTaskId(Integer.parseInt(row[0]));
        newTask.setCpusRequested(Integer.parseInt(row[7]));
        newTask.setCpuUsed(Double.parseDouble(row[12].replace("%", ""))/100);
        newTask.setProcess(row[2]);
        taskList.add(newTask);
    }

    public List<Task> getTaskList(){
        return this.taskList;
    }

    public String[] getHeader(){
        return (String []) this.traceColumnNoToName.values().toArray(new String[traceColumnNoToName.values().size()]);
    }

    public List<String> getColumnValues(String columnName){
        return this.traceTable.get(columnName);
    }

	@Override
	public Iterator<String[]> iterator() {

        Iterator<String[]> it = new Iterator<String[]>() {
            private int currentIndex = 0;
            private boolean hasNext = true;

			@Override
			public boolean hasNext() {
				return hasNext;
			}

			@Override
			public String[] next() {
                List<String> list = new ArrayList<>();
                try {
                    for (String col : traceColumnNoToName.values()){
                        list.add((String) traceTable.get(col).get(currentIndex));
                    }
                } catch (IndexOutOfBoundsException exc){
                    hasNext = false;
                }
                currentIndex++;
				return (String[]) list.toArray(new String[list.size()]);
			}
        };

        return it;

	}



    
}