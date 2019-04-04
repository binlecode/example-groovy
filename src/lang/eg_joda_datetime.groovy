
@Grab(group='joda-time', module='joda-time', version='1.5.2')
import org.joda.time.DateTime




def dt = new DateTime(new Date(2012, 12, 1).getTime())


println "dateTime: ${dt}"
println "hour of day: ${dt.hourOfDay}"
println "hour of day: ${dt.minuteOfHour}"
println "hour of day: ${dt.secondOfMinute}"


//println "formatting now: ${dateString}"
//println "parsing string: ${dateString} to " + dateFormatter.parse(dateString, DEFAULT_LOCALE)



//def numberFormatter = new NumberFormatter()

//testFormattingService.addFormatter(dateFormatter)

//testFormattingService.addFormatter(numberFormatter)
















 
 