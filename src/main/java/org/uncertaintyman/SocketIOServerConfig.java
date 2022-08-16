package org.uncertaintyman;

import com.alibaba.fastjson.JSONObject;
import com.corundumstudio.socketio.Configuration;
import com.corundumstudio.socketio.SocketIOServer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;

@org.springframework.context.annotation.Configuration
public class SocketIOServerConfig {

    private Logger logger = LoggerFactory.getLogger(SocketIOServerConfig.class);

    @Autowired
    private MyDataListener myDataListener;

    @Bean(destroyMethod = "stop")
    public SocketIOServer socketIOServer () {
        Configuration configuration = new Configuration();
        configuration.setPort(10001);
        //  configuration.nu

        SocketIOServer server = new SocketIOServer(configuration);

        server.addEventListener("my_event", JSONObject.class, myDataListener);
        server.addPingListener(client -> {
            logger.info("ping:{}", client.getRemoteAddress());

        });

        server.addConnectListener(client -> {
            logger.info("connect|systemId:{}", client.getHandshakeData().getUrlParams().get("systemId"));
        });

        server.addDisconnectListener(
                client -> {
                    logger.info("disconnect:{}", client.getHandshakeData().getUrl());
                }
        );

        server.start();
        return server;
    }
}
