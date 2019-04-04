@Grab(group='org.codehaus.groovy.modules.http-builder', module='http-builder', version='0.7.1')


import groovyx.net.http.ContentType
import groovyx.net.http.HTTPBuilder
import groovyx.net.http.Method
import groovyx.net.http.RESTClient

import static groovyx.net.http.ContentType.*
import groovyx.net.http.ContentEncoding
import groovyx.net.http.ContentEncodingRegistry
import groovyx.net.http.HTTPBuilder.RequestConfigDelegate


Map query = null

def http = new RESTClient('http://localhost:19200')
def data


try {

    // The resp field in the above example is an instance of HttpResponseDecorator.

    def resp = http.get(path: '/_stats', query: [pretty: ''])
    assert resp.contentType == JSON.toString()
    resp.headers.each { h ->
        println "response header ${h.name} : ${h.value}"
    }
    // Calling resp.getData() returns the parsed response content.
    // This is the same parsed response that you would get passed to HTTPBuilder's
    // response handler closure, but it is always buffered in-memory and the
    // response stream is automatically closed.
    assert resp.data != null
    resp.data.each { k, v ->
        println "$k    => "
        println "$v"

    }


/*


            { req ->
        uri.path = '/_stats'
        headers.'User-Agent' = "Mozilla/5.0 Firefox/3.0.4"

        if (query) uri.query = query

        response.success = { resp, reader ->
            println "response status: ${resp.statusLine}"
            println "Headers: ---------------"
            resp.headers.each { h ->
                println " ${h.name} : ${h.value}"
            }
            println "Response data: ---------"
//    println "Content-Type: ${resp.headers.'Content-Type'"
            data = reader
            println data._all
            println "------------------------"

        }

        response.'404' = {
            println 'Not found'
        }
        response.'400' = {
            println 'Bad Request'
        }
    }

    */
} catch (groovyx.net.http.HttpResponseException ex) {
    ex.printStackTrace()
    return null
} catch (java.net.ConnectException ex) {
    ex.printStackTrace()
    return null
}

