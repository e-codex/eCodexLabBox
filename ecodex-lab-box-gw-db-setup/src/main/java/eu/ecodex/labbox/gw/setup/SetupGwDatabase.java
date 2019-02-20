package eu.ecodex.labbox.gw.setup;

import liquibase.Contexts;
import liquibase.LabelExpression;
import liquibase.Liquibase;
import liquibase.database.Database;
import liquibase.database.DatabaseFactory;
import liquibase.database.jvm.JdbcConnection;
import liquibase.exception.LiquibaseException;
import liquibase.resource.ClassLoaderResourceAccessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.liquibase.LiquibaseAutoConfiguration;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.util.CollectionUtils;

import javax.sql.DataSource;

import java.util.stream.Stream;


@SpringBootApplication(exclude = {
        LiquibaseAutoConfiguration.class
})
public class SetupGwDatabase implements CommandLineRunner {

    public static void main(String ...args) {
        SpringApplicationBuilder builder = new SpringApplicationBuilder();
        builder.sources(SetupGwDatabase.class);
        builder.run(args);
    }

    @Autowired
    DataSource ds;

    @Override
    public void run(String... args) throws Exception {
        //if args does not contain "update" just return
        if (CollectionUtils.arrayToList(args).contains("update")) {
            return;
        }

        String changelog = "db/changelog-4.0.2.xml";
        String dataChangelog = "db/changelog-4.0.2-data.xml";

        java.sql.Connection connection = ds.getConnection();

        Database database = DatabaseFactory.getInstance().findCorrectDatabaseImplementation(new JdbcConnection(connection));
        Liquibase dbUpdate = new liquibase.Liquibase(changelog, new ClassLoaderResourceAccessor(), database);
        dbUpdate.update(new Contexts(), new LabelExpression());

        Liquibase dbData = new liquibase.Liquibase(dataChangelog, new ClassLoaderResourceAccessor(), database);
        dbData.update(new Contexts(), new LabelExpression());


    }

}
