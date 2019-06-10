package main.service.dbservice;

import main.model.Proxy;
import main.repository.ProxyRepository;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * Service to work with the repository by Proxies.
 */
@Service
public class ProxyService {

    @Autowired
    private ProxyRepository proxyRepository;

    public List<Proxy> proxiesFromString(LinkedList<String> proxies) {
        List<Proxy> proxyList = new ArrayList<>();

        proxies.forEach(proxyStr -> {
            Proxy proxy = new Proxy();

            proxy.setHost(StringUtils.substringBetween(proxyStr, "@", ":"));
            proxy.setPassword(StringUtils.substringBetween(proxyStr, ":", "@"));
            proxy.setLogin(StringUtils.substringBefore(proxyStr, ":"));
            proxy.setPort(StringUtils.substringAfterLast(proxyStr, ":"));
            proxy.setProxyBusy(false);
            proxy.setFailRatioStatus(10);

            proxyList.add(proxy);
        });

        return proxyList;
    }

    @Transactional
    public Proxy save(Proxy proxy) {
        return proxyRepository.save(proxy);
    }

    @Transactional
    public Proxy findById(Long id) {
        return proxyRepository.findById(id).isPresent() ? proxyRepository.findById(id).get() : null;
    }

    @Transactional
    public boolean existsById(Long id) {
        return proxyRepository.existsById(id);
    }

    @Transactional
    public long count() {
        return proxyRepository.count();
    }

    @Transactional
    public void deleteById(Long id) {
        proxyRepository.deleteById(id);
    }

    @Transactional
    public void delete(Proxy proxy) {
        proxyRepository.delete(proxy);
    }

    @Transactional
    public List<Proxy> saveAll(List<Proxy> proxies) {

        return proxyRepository.saveAll(proxies);
    }

    @Transactional
    public List<Proxy> getAll() {

        return proxyRepository.findAll();
    }

    @Transactional
    public List<Proxy> findAllValidProxiesWithAccountConnectedAndNotCurrentlyInUse() {
        return proxyRepository.findAllByFailRatioStatusGreaterThanAndIsProxyBusyFalseAndAccount_ProxyIsNotNull(0);
    }

    @Transactional
    public List<Proxy> findAllValidProxiesWithoutAccountAndNotCurrentlyInUse() {
        return proxyRepository.findAllByFailRatioStatusGreaterThanAndIsProxyBusyFalseAndAccount_ProxyIsNull(0);
    }

    @Transactional
    public List<Proxy> findAllByIsProxyBusyTrue() {
        return proxyRepository.findAllByIsProxyBusyIsTrue();
    }

    @Transactional
    public List<Proxy> findAllBadProxies() {
        return proxyRepository.findAllByFailRatioStatus(0);
    }

    @Transactional
    public void deleteAllProxies() {
        proxyRepository.deleteAll();
    }

    @Transactional
    public void deleteAllByCollection(Iterable<Proxy> iterable) {
        proxyRepository.deleteAll(iterable);
    }

    @Transactional
    public void deleteAllBadProxies() {

        List<Proxy> badProxies = findAllBadProxies();

        for (Proxy badProxy : badProxies) {

            badProxy.setAccount(null);
        }

        saveAll(badProxies);
        deleteAllByCollection(badProxies);
    }
}
