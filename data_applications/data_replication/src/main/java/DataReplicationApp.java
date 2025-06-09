import io.quarkus.runtime.Quarkus;
import io.quarkus.runtime.annotations.QuarkusMain;

@QuarkusMain(name="datarepl")
public class DataReplicationApp {

    public static void main(String[] args){
        Quarkus.run(args);
    }
}
