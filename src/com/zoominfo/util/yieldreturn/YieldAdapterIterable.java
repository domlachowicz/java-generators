/**
 * A "yield return" implementation for Java
 * By Jim Blackler (jimblackler@gmail.com)
 *
 * http://jimblackler.net/blog/?p=61
 * http://svn.jimblackler.net/jimblackler/trunk/IdeaProjects/YieldAdapter/
 */
package com.zoominfo.util.yieldreturn;

/**
 * A special version of Iterable<> that returns YieldAdapterIterators<>.
 */
public interface YieldAdapterIterable<T> extends Iterable<T> {

    /**
     * Returns an iterator over the results.
     */
    YieldAdapterIterator<T> iterator();
}
