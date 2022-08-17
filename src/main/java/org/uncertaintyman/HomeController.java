package org.uncertaintyman;

import com.corundumstudio.socketio.SocketIOServer;
import com.corundumstudio.socketio.VoidAckCallback;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class HomeController {

    private final Logger logger = LoggerFactory.getLogger(HomeController.class);


    @Autowired
    SocketIOServer socketIOServer;

    @RequestMapping("/")
    public @ResponseBody String greeting() {

        String sendMsg = "服务端home返回信息";

        socketIOServer.getAllClients().forEach(socketIOClient -> {
            socketIOClient.sendEvent("my_response",
                    new VoidAckCallback(1000) {

                        @Override
                        protected void onSuccess() {
                            logger.info("sendSuccess|sendMsg:{}", sendMsg);
                        }
                    },
                    sendMsg);
        });

        return "Hello, World";
    }

}