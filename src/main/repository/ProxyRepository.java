package main.repository;

import main.model.Proxy;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * The repository using Spring Data to work with MySql DB by Proxies.
 */
@Repository
public interface ProxyRepository extends JpaRepository<Proxy, Long> {

    List<Proxy> findAllByFailRatioStatusGreaterThanAndIsProxyBusyFalseAndAccount_ProxyIsNotNull(Integer failRatioCount);

    List<Proxy> findAllByFailRatioStatusGreaterThanAndIsProxyBusyFalseAndAccount_ProxyIsNull(Integer failRatioCount);

    List<Proxy> findAllByIsProxyBusyIsTrue();

    List<Proxy> findAllByFailRatioStatus(Integer failRatioCount);
}
