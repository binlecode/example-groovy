/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

def name='binle'

println "Hello $name!"

import groovy.swing.SwingBuilder
import java.awt.BorderLayout as BL



def swing = new SwingBuilder()
def count = 0
def textlabel

def frame = swing.frame(title:'Frame', size:[300,300]) {
  borderLayout()
  textlabel = label(text:"Click the button!", constraints: BL.NORTH)
  button(text:'Click Me',
         actionPerformed: {count++; textlabel.text = "Clicked ${count} time(s)."; println "clicked"},
         constraints:BL.SOUTH)
}


frame.show()
