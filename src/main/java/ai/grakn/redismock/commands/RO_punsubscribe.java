package ai.grakn.redismock.commands;

import ai.grakn.redismock.RedisBase;
import ai.grakn.redismock.RedisClient;
import ai.grakn.redismock.Response;
import ai.grakn.redismock.Slice;
import org.slf4j.LoggerFactory;

import java.util.List;

class RO_punsubscribe extends AbstractRedisOperation {
    private static final org.slf4j.Logger LOG = LoggerFactory.getLogger(RO_punsubscribe.class);
    private final RedisClient client;

    RO_punsubscribe(RedisBase base, RedisClient client, List<Slice> params) {
        super(base, params,null, null, null);
        this.client = client;
    }

    Slice response() {
        List<Slice> channelsToUbsubscribeFrom;
        if(params().isEmpty()){
            LOG.debug("No channels specified therefore unsubscribing from all channels");
            channelsToUbsubscribeFrom = base().getSubscriptions(client);
        } else {
            channelsToUbsubscribeFrom = params();
        }

        for (Slice channel : channelsToUbsubscribeFrom) {
            LOG.debug("Unsubscribing from channel [" + channel + "]");
            if(base().removeSubscriber(channel, client)) {
                int numSubscriptions = base().getSubscriptions(client).size();
                Slice response = Response.unsubscribe(channel, numSubscriptions);
                client.sendResponse(Response.clientResponse("punsubscribe", response), "punsubscribe");
            }
        }

        //Skip is sent because we have already responded
        return Response.SKIP;
    }
}
