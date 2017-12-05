package life.qbic.utils;

import java.util.HashMap;
import java.util.Map;

/**
 * Mapping information for keywords of Nextflow
 * trace columns to a defined enum.
 */
class MappingInfo{

    static Map<String, NextflowTraceColumns> map = new HashMap<>();

    /**
     * Init the map
     */
    static
    {
        //TODO: add remaining keywords for full support  
        map.put("task_id", NextflowTraceColumns.TASK_ID);
        map.put("hash", NextflowTraceColumns.HASH);
        map.put("native_id", NextflowTraceColumns.NATIVE_ID);
        map.put("process", NextflowTraceColumns.PROCESS);
        // ...
        map.put("cpus", NextflowTraceColumns.NAME.CPUS);
        map.put("memory", NextflowTraceColumns.MEMORY);
        // ...
        map.put("%cpu", NextflowTraceColumns.P_CPU);
        map.put("%mem", NextflowTraceColumns.P_MEM);
    }


}