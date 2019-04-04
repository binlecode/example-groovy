@Grapes([
    @Grab('redis.clients:jedis:1.5.1'),
    @GrabConfig(systemClassLoader=true)
])

import redis.clients.jedis.*

Jedis jedis = new Jedis("localhost")

jedis.set('foo', 'bar')
println jedis.get('foo')
println jedis.ttl('foo')

jedis.del 'foo'
println jedis.get('foo')

jedis.rpush('list1', 'elm1')
jedis.lpush('list1', 'elm2')
println jedis.llen('list1')
println jedis.lrange('list1', 0, -1)

println jedis.rpop('list1')
println jedis.lrange('list1', 0, -1)
println jedis.lpop('list1')
println jedis.lrange('list1', 0, -1)
jedis.del 'list1'



def hms = [
	[name: 'test name 1', color: 'red'],
	[name: 'test name 2', color: 'pink']
]	

hms.eachWithIndex { hm, idx ->
	hm.each { entry ->
		jedis.hset("Hm:$idx", "$entry.key", "$entry.value")
	}
}


