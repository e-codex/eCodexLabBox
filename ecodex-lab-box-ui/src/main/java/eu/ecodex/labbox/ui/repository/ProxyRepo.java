package eu.ecodex.labbox.ui.repository;

import eu.ecodex.labbox.ui.domain.Proxy;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

@Repository
public class ProxyRepo {
    final Proxy proxy;

    public ProxyRepo(@Value("${lab-box.settings.proxy.ip}") String ip, @Value("${lab-box.settings.proxy.port}") String port) {
        this.proxy = new Proxy(ip, port);
    }

    public Proxy getProxy() {
        return this.proxy;
    }

    public void setProxy(Proxy proxy) {
        this.proxy.setIp(proxy.getIp());
        this.proxy.setPort(proxy.getPort());
    }
}
