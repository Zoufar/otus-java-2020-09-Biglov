package hw16W_io.dataprocessor;

import hw16W_io.model.Measurement;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class ProcessorAggregator implements Processor {

    Map <String,Double> processResult = new LinkedHashMap<>();

    @Override
    public Map<String, Double> process(List<Measurement> data) {
        for (var datum :data) {
            String name = datum.getName();
            Double value = datum.getValue();
            if ( processResult.keySet().contains(name)){
                processResult.put(name, processResult.get(name) + value);
            }
            else { processResult.put(name, value); }
        }
        return processResult;
    }
}
