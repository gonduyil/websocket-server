package org.uncertaintyman;

import com.corundumstudio.socketio.SocketIOServer;
import com.corundumstudio.socketio.VoidAckCallback;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.UUID;

@Controller
public class HomeController {

    private final Logger logger = LoggerFactory.getLogger(HomeController.class);


    @Autowired
    SocketIOServer socketIOServer;

    @RequestMapping("/")
    public @ResponseBody String greeting() {

        String msgKey = UUID.randomUUID().toString().replace("-", "");
        String sendMsg = "服务端home返回信息";

        socketIOServer.getAllClients().forEach(socketIOClient ->
                        socketIOClient.sendEvent("my_response", new ServerAck(msgKey, 10000), sendMsg)
        );

        return "Hello, World";
    }

    public static class ServerAck extends VoidAckCallback {

        private String msgKey;
        private final Logger logger = LoggerFactory.getLogger(ServerAck.class);

        public ServerAck(String msgKey, int timeout) {
            super(timeout);
            this.msgKey = msgKey;
        }

        @Override
        protected void onSuccess() {
            logger.info("sendMsgSuccess|msgKey:{}", msgKey);
        }
    }

}