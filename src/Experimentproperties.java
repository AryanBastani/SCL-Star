import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Properties;

public class Experimentproperties {
    private Properties props;

    private static Experimentproperties instance;
    private Experimentproperties() { loadProperties(); }

    public static Experimentproperties getInstance() {
        if(instance == null) instance = new Experimentproperties();
        return instance;
    }

    public void loadProperties(){
        File f = new File("resources/.experimentProps");
        loadProperties(f);
    }
    public void loadProperties(File f) {
        if (props != null) {
            props.clear();
        } else {
            props = new Properties();
        }

        if (f.exists()) {
            InputStream in;
            try {
                in = new FileInputStream(f);
                props.load(in);
                in.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public Integer getIndex(String key){
        String default_value = props.getProperty("defaultValue");
        return Integer.valueOf(props.getProperty(key, default_value));
    }

    public String getProp(String key){
        String default_value = props.getProperty("defaultValue");
        return props.getProperty(key, default_value);
    }
}
