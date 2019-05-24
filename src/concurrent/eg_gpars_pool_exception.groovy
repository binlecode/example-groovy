@Grab(group='org.codehaus.gpars', module='gpars', version='1.2.1')

import groovyx.gpars.GParsPool


/**
 * If an exception is thrown while processing any of the passed-in closures, the first exception is re-thrown
 * from the xxxParallel methods and the algorithm stops as soon as possible.
 * <p>
 * The exception handling mechanism of GParsPool builds on the one provided in the Fork/Join framework.
 * Since Fork/Join algorithms are by nature hierarchical, once any part of the algorithm fails, there’s usually
 * little benefit continuing the computation, since some branches of the algorithm will never return a result.
 */

//todo: add pool exception handling example