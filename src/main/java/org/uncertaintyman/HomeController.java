package org.uncertaintyman;

import com.corundumstudio.socketio.AckCallback;
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
                        socketIOClient.sendEvent("my_response", new ServerVoidAck(msgKey, 10000), sendMsg)
        );

        return "Hello, World";
    }

    /**
     * 需要客户端做对应的实现
     *
     * const socket = io("ws://localhost:10001");
     * socket.on('my_response', function(msg, ack) {
     *   console.log("msg", msg);
     *   if(ack) {
     *     ack();
     *   }
     * });
     */
    public static class ServerVoidAck extends VoidAckCallback {

        private String msgKey;
        private final Logger logger = LoggerFactory.getLogger(ServerVoidAck.class);

        public ServerVoidAck(String msgKey, int timeout) {
            super(timeout);
            this.msgKey = msgKey;
        }

        @Override
        protected void onSuccess() {
            logger.info("sendMsgSuccess|msgKey:{}", msgKey);
        }
    }


    public static class ResMsgKeyAck extends AckCallback<String> {

        private String msgKey;
        private final Logger logger = LoggerFactory.getLogger(ServerVoidAck.class);

        public ResMsgKeyAck(String msgKey, int timeout) {
            super(String.class, timeout);
            this.msgKey = msgKey;
        }

        @Override
        public void onSuccess(String result) {
            logger.info("sendMsgSuccess|msgKey:{}|result:{}", msgKey, result);

        }
    }
}