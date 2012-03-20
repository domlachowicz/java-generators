/**
 * A "yield return" implementation for Java
 * By Jim Blackler (jimblackler@gmail.com)
 *
 * http://jimblackler.net/blog/?p=61
 * http://svn.jimblackler.net/jimblackler/trunk/IdeaProjects/YieldAdapter/
 */
package com.zoominfo.util.yieldreturn;

import java.util.Iterator;
import java.io.Closeable;

/**
 * A version of a standard Iterator<> used by the yield adapter. The only addition is a close()
 * function to clear resources manually when required.
 */
public interface YieldAdapterIterator<T> extends Iterator<T>, Closeable {

    /**
     * Because the Yield Adapter starts a separate thread for duration of the collection, this can
     * be left open if the calling code only reads part of the collection. If the iterator goes out
     * of scope, when it is GCed its finalize() will close the collection thread. However garbage
     * collection is sporadic and the VM will not trigger it simply because there is a lack of
     * available threads. So, if a lot of partial reads are happening, it will be wise to manually
     * close the iterator (which will clear the resources immediately).
     */
}