

class Dude {
    String name
    String hobby
    
    // singleton pattern
    private static Dude instance
    private Dude() {
        name =  'defaultDudeName'
        hobby = 'defaultDudeHobby'
    }
    static Dude getInstance() {
        if (instance == null) {
            instance = new Dude()
        }
        return instance 
    }
    

    String toString() {
        return this.name + ' - ' + this.hobby
    }   
    
}

def dude1 = Dude.getInstance()

println dude1.properties


