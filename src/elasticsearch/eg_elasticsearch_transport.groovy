@GrabResolver(name = 'elasticsearch', root = 'http://oss.sonatype.org/content/repositories/releases/')
@GrabResolver(name = 'maven', root = 'http://repo1.maven.org/maven2/')
@Grab(group = 'org.elasticsearch', module = 'elasticsearch', version = '2.1.1')
import org.elasticsearch.client.Client
import org.elasticsearch.client.transport.TransportClient
import org.elasticsearch.common.settings.Settings
import org.elasticsearch.common.transport.InetSocketTransportAddress

// on startup
Settings settings = Settings.settingsBuilder()
        .put("cluster.name", "elasticsearch").build()

Client client = TransportClient.builder().settings(settings).build()
//        .addTransportAddress(new InetSocketTransportAddress(InetAddress.getByName("192.168.56.10"), 9300))
        .addTransportAddress(new InetSocketTransportAddress(InetAddress.getByName("localhost"), 19300))

println "created transport client: $client"




client.close()
println "closed client"
