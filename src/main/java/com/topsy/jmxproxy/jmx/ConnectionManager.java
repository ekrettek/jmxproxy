package com.topsy.jmxproxy.jmx;

import com.topsy.jmxproxy.core.Host;

import com.yammer.dropwizard.lifecycle.Managed;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ConnectionManager implements Managed {
    private static final Logger LOG = LoggerFactory.getLogger(ConnectionManager.class);

    private Map<String, ConnectionWorker> hosts;
    private boolean started = false;

    public ConnectionManager() {
        hosts = new HashMap<String, ConnectionWorker>();
    }

    public Host getHost(String host) throws Exception {
        synchronized (hosts) {
            if (!hosts.containsKey(host)) {
                LOG.info("creating new worker for " + host);
                hosts.put(host, new ConnectionWorker(host));
            }
        }

        return hosts.get(host).getHost();
    }

    public boolean isStarted() {
        return started;
    }

    public void start() {
        LOG.info("starting jmx connection manager");
        started = true;
    }

    public void stop() {
        LOG.info("stopping jmx connection manager");
        started = false;
        hosts.clear();
    }
}