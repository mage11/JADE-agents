import java.io.Serializable;
import java.util.ArrayList;
import java.util.Map;

public class MapSet implements Serializable {
    private static final long serialVersionUID = -8082739947519804587L;
    public Map<String,ArrayList<double[]>> map;

    public MapSet(Map<String,ArrayList<double[]>> a){
        this.map = a;
    }
}
