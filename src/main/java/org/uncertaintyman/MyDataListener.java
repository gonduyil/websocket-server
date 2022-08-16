package org.uncertaintyman;

import com.alibaba.fastjson.JSONObject;
import com.corundumstudio.socketio.AckRequest;
import com.corundumstudio.socketio.SocketIOClient;
import com.corundumstudio.socketio.listener.DataListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class MyDataListener implements DataListener<JSONObject> {

    private final Logger logger = LoggerFactory.getLogger(MyDataListener.class);

    @Override
    public void onData(SocketIOClient client, JSONObject data, AckRequest ackSender) throws Exception {
        logger.info("data:{}", JSONObject.toJSONString(data));
        client.sendEvent("clientR", "收到");
    }
}
