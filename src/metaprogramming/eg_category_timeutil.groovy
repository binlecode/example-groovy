

class TimeUtil {

    /**
     * @param self
     * @return time duration in milli seconds
     */
    static Long getDays(Integer self) {
        self * 24 * 3600 * 1000
    }

    static Long getHours(Integer self) {
        self * 3600 * 1000
    }

    static Long getMinutes(Integer self) {
        self * 60 * 1000
    }


}

use(TimeUtil) {

    Long time = new Date().time + 5.days + 2.hours + 6.minutes
    println "date in 5 days and 2 hours 6 minutes is ${new Date(time)}"

}