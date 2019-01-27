package obj;

import java.io.Serializable;
import java.util.Map;

public class MapSet  implements Serializable {
    private static final long serialVersionUID = -8082739947519804587L;
    private Map<Integer, DataSet> map;

    public MapSet(Map<Integer, DataSet> map) {
        this.map = map;
    }

    public Map<Integer, DataSet> getMap() {
        return map;
    }
}
