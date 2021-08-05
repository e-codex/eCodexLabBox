package eu.ecodex.labbox.ui.domain.entities;



import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;

import javax.xml.namespace.QName;
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

    private final String gatewayPort;
    private final String connectorPort;
    private final String clientPort;

    public Labenv(Path path) {
        this.path = path;
        this.connectorPort = parseConnectorProperties();
        this.gatewayPort = parseGatewayServerXml();
        this.clientPort = parseClientProperties();
    }

    private String parseGatewayServerXml() {
        Path property = path
                .resolve("domibus-gateway")
                .resolve("conf")
                .resolve("server.xml");

        String result = "missing:server.xml";

        try {
            FileInputStream fileInputStream = new FileInputStream(property.toFile());
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
        return result;
    }

    private String parseConnectorProperties() {
        Path property = path
                .resolve("domibus-connector")
                .resolve("config")
                .resolve("connector.properties");

        String result = "missing:connector.properties";
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

    private String parseClientProperties() {
        Path property = path
                .resolve("domibus-connector-client-application")
                .resolve("config")
                .resolve("connector-client.properties");

        String result = "missing:client.properties";
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
