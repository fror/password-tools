/*
 * Copyright 2015 Olivier Grégoire <fror@users.noreply.github.com>.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package be.fror.common.function;

import static com.google.common.base.Preconditions.checkNotNull;

import java.io.Serializable;
import java.util.function.Supplier;

import javax.annotation.concurrent.ThreadSafe;

/**
 *
 * @author Olivier Grégoire &lt;fror@users.noreply.github.com&gt;
 */
public final class Suppliers {

  private Suppliers() {
  }

  /**
   * Returns a supplier which caches the instance retrieved during the first call to <tt>get()</tt>
   * and returns that value on subsequent calls to <tt>get()</tt>.
   *
   * <p>
   * The returned supplier is thread-safe. The supplier's serialized form does not contain the
   * cached value, which will be recalculated when <tt>get()</tt> is called on the reserialized
   * instance.
   *
   * <p>
   * If <tt>delegate</tt> is an instance created by an earlier call to <tt>memoize</tt>, it is
   * returned directly.
   *
   * <p>
   * This method is a direct port of Guava's
   * {@link com.google.common.base.Suppliers#memoize(com.google.common.base.Supplier) Suppliers::memoize()}
   * to Java 8's <tt>Supplier</tt>.
   *
   * @param <T> The type of the object to memoize
   * @param delegate The supplier to memoize
   * @return a new instance of a memoizing supplier, or <tt>delegate</tt> if it is already the
   * result of a call to this method
   * @see <a href="http://en.wikipedia.org/wiki/Memoization">memoization</a> on Wikipedia
   */
  public static <T> Supplier<T> memoize(Supplier<T> delegate) {
    return (delegate instanceof MemoizingSupplier)
        ? delegate
        : new MemoizingSupplier<>(checkNotNull(delegate));
  }

  @ThreadSafe
  static class MemoizingSupplier<T> implements Supplier<T>, Serializable {

    private static final long serialVersionUID = 0;
    final Supplier<T> delegate;
    transient volatile boolean initialized;

    transient T value;

    MemoizingSupplier(Supplier<T> delegate) {
      this.delegate = delegate;
    }

    @Override
    public T get() {
      // A 2-field variant of Double Checked Locking.
      if (!initialized) {
        synchronized (this) {
          if (!initialized) {
            T t = delegate.get();
            value = t;
            initialized = true;
            return t;
          }
        }
      }
      return value;
    }
  }
}
