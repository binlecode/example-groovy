@GrabResolver(name='elasticsearch', root='http://oss.sonatype.org/content/repositories/releases/')
@GrabResolver(name='maven', root='http://repo1.maven.org/maven2/')
@Grab(group='org.elasticsearch', module='elasticsearch', version='1.1.0')


// elasticsearch java/groovy api requires the names.txt to be available in classpath
// put names.txt (from config/ folder inside elasticsearch.jar) to
// <current work folder>/config/names.txt

import static org.elasticsearch.node.NodeBuilder.*
import org.elasticsearch.client.Client

// on startup

// When start a MyNode, the most important decision is whether it should hold data or not.
// In other words, should indices and shards be allocated to it. To set pure clients 
// without shards being allocated to them, set either node.data to false or node.client to true
Node node = nodeBuilder().clusterName('elasticsearchcluster-11').client(true).node()
println "started node: $node"
try {
    Client client = node.client()
} catch (Exception e) {
    println e.message
}
// on shutdown
node.stop().close()
println "closed node: $node"
