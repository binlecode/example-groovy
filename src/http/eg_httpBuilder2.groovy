@Grab(group = 'org.codehaus.groovy.modules.http-builder', module = 'http-builder', version = '0.7')


import groovyx.net.http.ContentType
import groovyx.net.http.HTTPBuilder
import groovyx.net.http.Method

import static groovyx.net.http.ContentType.*
import groovyx.net.http.ContentEncoding
import groovyx.net.http.ContentEncodingRegistry
import groovyx.net.http.HTTPBuilder.RequestConfigDelegate


Map query = null

def http = new HTTPBuilder()
def data

try {

    http.request('http://localhost:19200', Method.GET, ContentType.JSON) { req ->
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
} catch (groovyx.net.http.HttpResponseException ex) {
    ex.printStackTrace()
    return null
} catch (java.net.ConnectException ex) {
    ex.printStackTrace()
    return null
}

