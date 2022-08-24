package eu.ecodex.labbox.gw;


import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;

@SpringBootApplication
@ImportAutoConfiguration(DataSourceAutoConfiguration.class)
public class LiquiBaseInit {

    public static void main(String[] args) {
        ConfigurableApplicationContext run = new SpringApplicationBuilder()
                .sources(LiquiBaseInit.class)
                .run(args);
    }


    @Component
    public static class CommandLineRunnerLiqui implements CommandLineRunner {

        public CommandLineRunnerLiqui(DataSource ds) {
            this.ds = ds;
        }

        private final DataSource ds;

        @Override
        public void run(String... args) throws Exception {

        }
    }

}
