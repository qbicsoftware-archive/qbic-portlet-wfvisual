package life.qbic;


import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.ArrayList;

class TraceContainer<Type> implements Iterable<Type[]>{

    private Map<String, List<String>> traceTable;

    private Map<Integer, String> traceColumnNoToName;

    public TraceContainer(){
        traceTable = new HashMap<>();
        traceColumnNoToName = new HashMap<>();
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
    }

    public String[] getHeader(){
        return (String []) this.traceColumnNoToName.values().toArray(new String[traceColumnNoToName.values().size()]);
    }

    public List<String> getColumnValues(String columnName){
        return this.traceTable.get(columnName);
    }

	@Override
	public Iterator<Type[]> iterator() {

        Iterator<Type[]> it = new Iterator<Type[]>() {
            private int currentIndex = 0;
            private boolean hasNext = true;

			@Override
			public boolean hasNext() {
				return hasNext;
			}

			@Override
			public Type[] next() {
                List<String> list = new ArrayList<>();
                try {
                    for (String col : traceColumnNoToName.values()){
                        list.add((String) traceTable.get(col).get(currentIndex));
                    }
                } catch (IndexOutOfBoundsException exc){
                    hasNext = false;
                }
                currentIndex++;
				return (Type[]) list.toArray(new String[list.size()]);
			}
        };

        return it;

	}



    
}