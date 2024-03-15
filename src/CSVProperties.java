import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Properties;

public class CSVProperties {

    private Properties props;

    private static CSVProperties instance;
    private CSVProperties() { loadProperties(); }

    public static CSVProperties getInstance() {
        if(instance == null) instance = new CSVProperties();
        return instance;
    }

    public void loadProperties(){
        File f = new File("resources/.csvProps");
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

    public String[] getResults_header(){
        String [] hearder = new String[]{
                "FILE_NAME", "STATES", "INPUTS",
                "LSTAR_MQ_SYM", "LSTAR_MQ_RST", "LSTAR_EQ_SYM", "LSTAR_EQ_RST", "LSTAR_TOTAL_SYM",
                "LSTAR_TOTAL_RST", "LSTAR_EQs",
                "CLSTAR_MQ_SYM", "CLSTAR_MQ_RST", "CLSTAR_EQ_SYM", "CLSTAR_EQ_RST",
                "CLSTAR_TOTAL_SYM", "CLSTAR_TOTAL_RST", "CLSTAR_EQs", "CLSTAR_COMPONENTS",
                "CLSTAR_ROUNDS", "CACHE"};
        return hearder;
    }
}

