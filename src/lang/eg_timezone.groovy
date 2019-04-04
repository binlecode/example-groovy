


Date t = new Date();
System.out.println(t);
//internal value - same (precise to last 3 digits)
System.out.println(t.getTime());
System.out.println(System.currentTimeMillis() );
//The following will change where you are running this code
System.out.println(t.getTimezoneOffset() );

/*
Run the above program twice. Second time around set your system time to a different timezone. 
You will see in java.util.Date, a timezoneOffset is always set to match VM's Default TimeZone. 
What this means is that, the face value of [new Date()] is different on  VMs running on 
different Timezones, even when run at the same time.  And also, this TimeZone is not mutable 
on a Date. 
So When you need to SPECIFY a timezone, you use java.util.Calendar. The Calendar encapsulates 
a Date (internally the other way around, which is way complex and out of the scope this 
article) to spit information in TimeZone specified. 
*/

//So you could run on a VM in EST at Jan 21 13:53:58 EST 2009 something like

String timeZone = "EST";
Calendar c = Calendar.getInstance(TimeZone.getTimeZone(timeZone));

System.out.printf("%02d/%02d/%04d %02d:%02d:%02d in %s \n", c.get(c.MONTH)+1, 
    c.get(c.DATE), c.get(c.YEAR), c.get(c.HOUR_OF_DAY),
    c.get(c.MINUTE), c.get(c.SECOND), timeZone);
    
// However, The Date inside this calendar will not be in PST. It will still be on System Timezone!
// if I print the date inside, it is tagged as system timezone.
println c.getTime() 

//Suppose you want your program to be independent of the TimeZone of the end users' VM.
// You can set the Default TimeZone by doing
TimeZone.setDefault(TimeZone.getTimeZone(timeZone));
//Warning: By doing this, all future Calendar.getInstance() calls will be in the given timezone.

//Another important gotcha is when we are parsing a DATE string. 
//Simply the innocent looking following code is soooo Evil
import java.text.SimpleDateFormat;
Date date = new Date();

SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
println sdf.parse("2009-01-21 13:53:58");
println sdf.format(date);

sdf.setTimeZone(TimeZone.getTimeZone("UTC"));
println sdf.format(date);









