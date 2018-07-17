package ai.grakn.redismock.commands;

import ai.grakn.redismock.RedisBase;
import ai.grakn.redismock.RedisClient;
import ai.grakn.redismock.Response;
import ai.grakn.redismock.Slice;

import java.util.List;

/**
 * @author qiuboboy@qq.com
 * @date 2018-07-17 16:03
 */
public class RO_psubscribe extends AbstractRedisOperation{
    private final RedisClient client;

    RO_psubscribe(RedisBase base, RedisClient client, List<Slice> params) {
        super(base, params,null, 0, null);
        this.client = client;
    }

    Slice response() {
        params().forEach(channel -> base().addSubscriber(channel, client));
        List<Slice> numSubscriptions = base().getSubscriptions(client);

        return Response.psubscribedToChannel(numSubscriptions);
    }
}