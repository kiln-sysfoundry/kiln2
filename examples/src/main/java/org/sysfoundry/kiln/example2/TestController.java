package org.sysfoundry.kiln.example2;

import com.fasterxml.jackson.core.JsonProcessingException;
import io.undertow.Handlers;
import io.undertow.io.Receiver;
import io.undertow.server.HttpHandler;
import org.slf4j.Logger;
import org.sysfoundry.kiln.formats.data.JsonUtil;
import org.sysfoundry.kiln.health.Log;
import org.sysfoundry.kiln.model.account.Account;

import java.util.ArrayList;
import java.util.List;

import static io.undertow.util.Headers.CONTENT_TYPE;
import static java.nio.charset.StandardCharsets.UTF_8;
import static org.sysfoundry.kiln.example2.Example2Subsys.NAME;

public class TestController {

    private static final Logger log = Log.get(NAME);

    public HttpHandler getAccounts(){
        Receiver.FullStringCallback fullStringCallback = (exchange,payload)->{

            //log.debug("Received Content payload");
            //log.debug(payload);

            /*try {
                Account account = JsonUtil.fromJSON(payload, Account.class);
                log.info("Received Account {}",account);
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }*/

            List<Account> allAccounts = getDummyAccounts();

            String output = null;
            try {
                output = JsonUtil.toJSON(allAccounts, (Class<List<Account>>) allAccounts.getClass());
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }

            exchange.getResponseHeaders().put(CONTENT_TYPE, "application/json");
            //exchange.getResponseSender().send("{\"receive_status\" : \"successful\"}", UTF_8);
            exchange.getResponseSender().send(output, UTF_8);
        };


        return Handlers.requestDump(httpServerExchange -> {
            httpServerExchange.getRequestReceiver().receiveFullString(fullStringCallback,UTF_8);
        });
    }

    private List<Account> getDummyAccounts() {
        List<Account> allAccounts = new ArrayList<>();
        allAccounts.add(new Account("account1","rajkumar","rajkumar@gmail.com","UK"));
        allAccounts.add(new Account("account2","Murali","Murali@gmail.com","INDIA"));
        return allAccounts;
    }
}
