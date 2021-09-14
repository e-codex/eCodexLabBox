package eu.ecodex.labbox.ui.domain.entities;



import eu.ecodex.labbox.ui.domain.LaunchComponentState;
import lombok.EqualsAndHashCode;
import lombok.Getter;

import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

@Getter
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Labenv {

    @EqualsAndHashCode.Include
    private final Path path;

    private String gatewayPort;
    private String connectorPort;
    private String clientPort;

    private LaunchComponentState gatewayState;
    private LaunchComponentState connectorState;
    private LaunchComponentState clientState;

    private Labenv(Path path) {
        this.path = path;
        this.gatewayState = LaunchComponentState.NOT_RUNNING;
        this.connectorState = LaunchComponentState.NOT_RUNNING;
        this.clientState = LaunchComponentState.NOT_RUNNING;
    }

    public static Labenv buildAndParse(Path path) {
        Labenv lab = new Labenv(path);
        lab.parseGatewayServerXml();
        lab.parseConnectorProperties();
        lab.parseClientProperties();
        return lab;
    }

    public static Labenv buildOnly(Path path) {
        return new Labenv(path);
    }

    public void parseGatewayServerXml() {
        Path property = path
                .resolve("domibus-gateway")
                .resolve("conf")
                .resolve("server.xml");

        String result = "missing:server.xml";

        try (FileInputStream fileInputStream = new FileInputStream(property.toFile())) {
            XMLInputFactory xmlInFact = XMLInputFactory.newInstance();
            XMLStreamReader reader = xmlInFact.createXMLStreamReader(fileInputStream);

            while(reader.hasNext()) {
                reader.nextTag();
                if (reader.getName().getLocalPart().equals("Connector")) {
                    result = reader.getAttributeValue(0);
                    break;
                }
                result = "missing-element:<Connector port=...";
            }

        } catch (IOException | XMLStreamException e) {
            e.printStackTrace();
        }
        this.gatewayPort = result;
    }

    public void parseConnectorProperties() {
        Path property = path
                .resolve("domibus-connector")
                .resolve("config")
                .resolve("connector.properties");

        String result = "missing:connector.properties";
        this.connectorPort = getServerPortProperty(property, result);
    }

    public void parseClientProperties() {
        Path property = path
                .resolve("domibus-connector-client-application")
                .resolve("config")
                .resolve("connector-client.properties");

        String result = "missing:client.properties";
        this.clientPort = getServerPortProperty(property, result);
    }

    private String getServerPortProperty(Path property, String result) {
        try {
            result = Files.lines(property)
                    .filter(l -> l.startsWith("server.port"))
                    .map(l -> l.split("=", 2))
                    .filter(a -> a.length == 2)
                    .map(a -> a[1])
                    .findFirst()
                    .orElse("missing-property:server.port");

        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }
}
