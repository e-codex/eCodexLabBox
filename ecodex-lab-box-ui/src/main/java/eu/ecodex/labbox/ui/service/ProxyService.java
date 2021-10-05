package eu.ecodex.labbox.ui.service;

import eu.ecodex.labbox.ui.domain.Proxy;
import eu.ecodex.labbox.ui.repository.ProxyRepo;
import org.springframework.stereotype.Service;

@Service
public class ProxyService {
    final ProxyRepo proxyRepo;

    public ProxyService(ProxyRepo proxyRepo) {
        this.proxyRepo = proxyRepo;
    }

    public Proxy getProxy() {
        return proxyRepo.getProxy();
    }

    public void setProxy(Proxy proxy) {
        proxyRepo.setProxy(proxy);
    }
}
