package pm.poopmail.toilet.debug;

import io.lettuce.core.ScanArgs;
import io.lettuce.core.ScanCursor;
import io.lettuce.core.StreamScanCursor;
import io.lettuce.core.ValueScanCursor;
import io.lettuce.core.api.sync.RedisSetCommands;
import io.lettuce.core.output.ValueStreamingChannel;
import java.util.List;
import java.util.Set;

public class DebugRedisCommands implements RedisSetCommands<String, String> {

    @Override
    public Long sadd(final String s, final String... strings) {
        return null;
    }

    @Override
    public Long scard(final String s) {
        return null;
    }

    @Override
    public Set<String> sdiff(final String... strings) {
        return null;
    }

    @Override
    public Long sdiff(final ValueStreamingChannel<String> valueStreamingChannel, final String... strings) {
        return null;
    }

    @Override
    public Long sdiffstore(final String s, final String... strings) {
        return null;
    }

    @Override
    public Set<String> sinter(final String... strings) {
        return null;
    }

    @Override
    public Long sinter(final ValueStreamingChannel<String> valueStreamingChannel, final String... strings) {
        return null;
    }

    @Override
    public Long sinterstore(final String s, final String... strings) {
        return null;
    }

    @Override
    public Boolean sismember(final String s, final String s2) {
        return true;
    }

    @Override
    public Set<String> smembers(final String s) {
        return null;
    }

    @Override
    public Long smembers(final ValueStreamingChannel<String> valueStreamingChannel, final String s) {
        return null;
    }

    @Override
    public List<Boolean> smismember(final String s, final String... strings) {
        return null;
    }

    @Override
    public Boolean smove(final String s, final String k1, final String s2) {
        return null;
    }

    @Override
    public String spop(final String s) {
        return null;
    }

    @Override
    public Set<String> spop(final String s, final long l) {
        return null;
    }

    @Override
    public String srandmember(final String s) {
        return null;
    }

    @Override
    public List<String> srandmember(final String s, final long l) {
        return null;
    }

    @Override
    public Long srandmember(final ValueStreamingChannel<String> valueStreamingChannel, final String s, final long l) {
        return null;
    }

    @Override
    public Long srem(final String s, final String... strings) {
        return null;
    }

    @Override
    public Set<String> sunion(final String... strings) {
        return null;
    }

    @Override
    public Long sunion(final ValueStreamingChannel<String> valueStreamingChannel, final String... strings) {
        return null;
    }

    @Override
    public Long sunionstore(final String s, final String... strings) {
        return null;
    }

    @Override
    public ValueScanCursor<String> sscan(final String s) {
        return null;
    }

    @Override
    public ValueScanCursor<String> sscan(final String s, final ScanArgs scanArgs) {
        return null;
    }

    @Override
    public ValueScanCursor<String> sscan(final String s, final ScanCursor scanCursor, final ScanArgs scanArgs) {
        return null;
    }

    @Override
    public ValueScanCursor<String> sscan(final String s, final ScanCursor scanCursor) {
        return null;
    }

    @Override
    public StreamScanCursor sscan(final ValueStreamingChannel<String> valueStreamingChannel, final String s) {
        return null;
    }

    @Override
    public StreamScanCursor sscan(final ValueStreamingChannel<String> valueStreamingChannel, final String s, final ScanArgs scanArgs) {
        return null;
    }

    @Override
    public StreamScanCursor sscan(final ValueStreamingChannel<String> valueStreamingChannel, final String s, final ScanCursor scanCursor, final ScanArgs scanArgs) {
        return null;
    }

    @Override
    public StreamScanCursor sscan(final ValueStreamingChannel<String> valueStreamingChannel, final String s, final ScanCursor scanCursor) {
        return null;
    }

}
