package eu.ecodex.labbox.ui;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;

@SpringBootApplication(scanBasePackages = "eu.ecodex.labbox.ui")
@EnableAsync
public class AppStarter {
    public static void main(String[] args) {
        // .headless(false) is needed to open File Browser via Java Desktop API
        new SpringApplicationBuilder(AppStarter.class).headless(false).run(args);
    }

    @Bean
    public Executor taskExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(10);
        executor.setMaxPoolSize(20);
        executor.setQueueCapacity(1024);
        executor.setThreadNamePrefix("WatchDirectory-");
        executor.initialize();
        return executor;
    }
}
