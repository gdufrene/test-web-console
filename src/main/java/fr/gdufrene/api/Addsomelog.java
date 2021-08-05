package fr.gdufrene.api;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

@Component
public class Addsomelog implements ApplicationRunner {
    
    Logger log = LoggerFactory.getLogger(Addsomelog.class);
    
    @Override
    public void run(ApplicationArguments args) throws Exception {
        new Thread(this::logs).start();
    }
    
    private void logs() {
        log.info("Add log started");
        boolean stop = false;
        while(!stop) {
            log.info("Add some log ...");
            try {
                Thread.sleep(1500);
            } catch (InterruptedException e) {
                stop = true;
                return;
            }
        }
    }

}
