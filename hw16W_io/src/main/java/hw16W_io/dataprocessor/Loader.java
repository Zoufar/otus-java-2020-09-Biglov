package hw16W_io.dataprocessor;

import hw16W_io.model.Measurement;

import java.util.List;

public interface Loader {

    List<Measurement> load();
}
