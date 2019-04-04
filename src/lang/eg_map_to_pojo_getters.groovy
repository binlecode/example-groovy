
class Pojo {
    private Map dm
    
    public Pojo(Map dm) {
        this.dm = dm
        this.dm?.each { k, v ->
            this.metaClass."get${k.capitalize()}" = { v }
        }
    }
    
    def methodMissing(String name, args) {
        println "method $name not found, return null"
        return null
    }

}

println new Date()
10000.times {

def pojo = new Pojo([k1: 'v1', k2: "v $it"])

//println pojo.getK1()
//println pojo.getK2()
//println pojo.getK3()

}
println new Date()
