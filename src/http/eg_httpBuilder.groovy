@GrabResolver(name='maven', root='http://central.maven.org/maven2/')
@Grapes(
        value = @Grab(group = 'org.codehaus.groovy.modules.http-builder', module = 'http-builder', version = '0.7.1')
)





import groovyx.net.http.ContentType
import groovyx.net.http.HTTPBuilder
import groovyx.net.http.Method

import static groovyx.net.http.ContentType.*
import groovyx.net.http.ContentEncoding
import groovyx.net.http.ContentEncodingRegistry
import groovyx.net.http.HTTPBuilder.RequestConfigDelegate

def http = new HTTPBuilder()

//def rsp = http.post(path: "/${reqCmd}",
//          contentType: "text/plain",
//          requestContentType: "text/xml; charset=UTF-8",
//          body: {"${reqCmd}" (deviceID: deviceId, locality: locality, profile: queryProfile)}
//    )

http.request('http://ajax.googleapis.com', Method.GET, ContentType.TEXT) { req ->
    uri.path = '/ajax/services/search/web'
    uri.query = [v: '1.0', q: 'Calvin and Hobbes']
    headers.'User-Agent' = "Mozilla/5.0 Firefox/3.0.4"

    response.success = { resp, stream ->
        assert resp.statusLine.statusCode == 200
        println "Got response: ${resp.statusLine}"
//    println "Content-Type: ${resp.headers.'Content-Type'"
        System.out << stream // print response stream
    }

    response.'404' = {
        println 'Not found'
    }
}
