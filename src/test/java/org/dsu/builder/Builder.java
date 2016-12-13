/**
 * 
 */
package org.dsu.builder;

/** Defines the interface for building pojo classes.
 *  For testing puropses.
 */
public interface Builder<T> {

    /** Builds pojo instances.
     * @return The instance of the T
     */
    T build();
}
