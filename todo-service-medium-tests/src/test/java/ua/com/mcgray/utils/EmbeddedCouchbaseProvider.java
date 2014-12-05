package ua.com.mcgray.utils;

import java.net.InetSocketAddress;
import java.net.URI;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.couchbase.client.CouchbaseClient;
import com.couchbase.client.CouchbaseConnectionFactory;
import com.couchbase.client.CouchbaseConnectionFactoryBuilder;
import net.spy.memcached.transcoders.Transcoder;
import org.couchbase.mock.Bucket;
import org.couchbase.mock.BucketConfiguration;
import org.couchbase.mock.CouchbaseMock;
import org.couchbase.mock.client.MockClient;

/**
 * @author orezchykov
 * @since 05.12.14
 */
public class EmbeddedCouchbaseProvider {

    public static int couchbasePort;
    private static CouchbaseMock couchbaseMock;
    private final static String DEFAULT_BUCKET = "default";
    private final static String PASSWORD = "";
    private static Map<String, CouchbaseClient> clients;
    private static Set<String> buckets = Collections.emptySet();

    public static void setup(String... wantedBuckets) throws Exception {
        setup(0, wantedBuckets);
    }

    public static void setup(int port, String... wantedBuckets) throws Exception {
        if (couchbaseMock == null) {
            buckets = new HashSet<>(Arrays.asList(wantedBuckets));
            buckets.add(DEFAULT_BUCKET);
            couchbaseMock = new CouchbaseMock(port, createConfiguration(buckets));
            couchbaseMock.start();
            couchbaseMock.waitForStartup();
            couchbasePort = couchbaseMock.getHttpPort();
            MockClient mockClient = new MockClient(new InetSocketAddress("localhost", 0));
            couchbaseMock.setupHarakiriMonitor("localhost:" + mockClient.getPort(), false);
            mockClient.negotiate();
            Runtime.getRuntime().addShutdownHook(new Thread() {
                @Override
                public void run() {
                    internalStop();
                }
            });
        }
    }

    private static List<BucketConfiguration> createConfiguration(Set<String> buckets) {
        List<BucketConfiguration> configList = new ArrayList<>();
        for (String bucket : buckets) {
            BucketConfiguration bucketConfiguration = new BucketConfiguration();
            bucketConfiguration.numNodes = 1;
            bucketConfiguration.numReplicas = 1;
            bucketConfiguration.name = bucket;
            bucketConfiguration.password = PASSWORD;
            bucketConfiguration.type = Bucket.BucketType.COUCHBASE;
            configList.add(bucketConfiguration);
        }
        return configList;
    }

    public static CouchbaseClient createClient(String bucketName, Transcoder transcoder) throws Exception {
        if (clients == null) {
            clients = new HashMap<>();
        }
        CouchbaseConnectionFactoryBuilder cfb = new CouchbaseConnectionFactoryBuilder();
        if(transcoder!=null) {
            cfb.setTranscoder(transcoder);
        }
        List<URI> uriList = new ArrayList<>();
        uriList.add(new URI("http", null, "localhost", couchbasePort, "/pools", "", ""));
        CouchbaseConnectionFactory connectionFactory = cfb.buildCouchbaseConnection(uriList, bucketName, PASSWORD);
        clients.put(bucketName, new CouchbaseClient(connectionFactory));

        return clients.get(bucketName);
    }
    public static CouchbaseClient createClient(String bucketName) throws Exception {
        return createClient(bucketName, null);
    }

    public static CouchbaseClient createClient() throws Exception {
        return createClient(DEFAULT_BUCKET);
    }

    public static CouchbaseClient createClient(Transcoder transcoder) throws Exception {
        return createClient(DEFAULT_BUCKET, transcoder);
    }

    static void internalStop(){
        if (clients != null) {
            for (CouchbaseClient client : clients.values()) {
                if (client != null) {
                    client.shutdown();
                }
            }
            clients.clear();
            clients = null;
        }
        if (couchbaseMock != null) {
            couchbaseMock.stop();
            couchbaseMock = null;
        }
    }

}
