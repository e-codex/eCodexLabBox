package eu.ecodex.labbox.ui.controller;

import eu.ecodex.labbox.ui.domain.Proxy;
import eu.ecodex.labbox.ui.service.ProxyService;
import org.springframework.stereotype.Controller;

@Controller
public class SettingsController {
    final ProxyService proxyService;

    public SettingsController(ProxyService proxyService) {
        this.proxyService = proxyService;
    }

    public Proxy getProxy() {

        return proxyService.getProxy();
    }

    public void setProxy(Proxy proxy) {
        proxyService.setProxy(proxy);
    }
}
