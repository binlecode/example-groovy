package io

/*
new File('.').eachFile { f ->
    println "file name = $f"
    if (f.name.startsWith('est_')) {
        def fileName = f.name
        //def newFileName = f.name.substring(1, fileName.indexOf('.groovy')+7)
        def newFileName = 'eg' + f.name.substring(3)       
        println "--> copying file, source file = $fileName, target file = $newFileName"
        new File(newFileName).write(f.text)
        // Delete old file
        f.delete()
    }  
}
*/

//new File('.').eachFile { f ->
//    if (f.isFile()) {
//        print "check file: $f"
//
//        if (f.name =~ /^test_/) {
//            println " ==> renaming file $f"
//            f.renameTo f.name.replace(/test_/, 'eg_')   // replace method only replaces first match
//        } else {
//            println ""
//        }
//    }
//}


new File('.').eachFile { f ->
    if (f.isFile()) {
        print "check file: $f"

        if (f.name =~ /^eg_bui_eg/) {
            println " ==> renaming file $f"
            f.renameTo f.name.replace(/eg_bui_eg/, 'db.eg_oracle_basic')   // replace method only replaces first match
        } else {
            println ""
        }
    }
}
