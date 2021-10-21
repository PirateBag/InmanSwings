package Application;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.*;
import org.springframework.boot.autoconfigure.*;



@SpringBootApplication
public class Entrypoint 
  implements CommandLineRunner {

    private static Logger LOG = LoggerFactory.getLogger(Entrypoint.class);

    public static void main(String[] args) {
        LOG.info("STARTING THE APPLICATION");
        SpringApplication.run(Entrypoint.class, args);
        LOG.info("APPLICATION FINISHED");
    }
 
    public void run(String... args) {
        LOG.info("EXECUTING : command line runner");
        System.setProperty("java.awt.headless", "false"); 
        FirstExample.run( args );
 
        for (int i = 0; i < args.length; ++i) {
            LOG.info("args[{}]: {}", i, args[i]);
        }
    }
}