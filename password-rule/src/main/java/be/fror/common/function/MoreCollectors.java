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

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;

import java.util.stream.Collector;

/**
 *
 * @author Olivier Grégoire &lt;fror@users.noreply.github.com&gt;
 */
public final class MoreCollectors {

  private MoreCollectors() {
  }

  /**
   * Returns a <tt>Collector</tt> that accumulates the input elements into a new
   * <tt>ImmutableList</tt>. The returned <tt>ImmutableList</tt> is guaranteed to be immutable,
   * serializable and thread-safe.
   *
   * @param <T> the type of the input elements
   * @return a Collector which collects all the input elements into a ImmutableList, in encounter
   * order
   */
  public static <T> Collector<T, ImmutableList.Builder<T>, ImmutableList<T>> toImmutableList() {
    return Collector.of(
        ImmutableList.Builder::new,
        ImmutableList.Builder::add,
        (l, r) -> l.addAll(r.build()),
        ImmutableList.Builder<T>::build
    );
  }

  /**
   * Returns a <tt>Collector</tt> that accumulates the input elements into a new
   * <tt>ImmutableSet</tt>. The returned <tt>ImmutableSet</tt> is guaranteed to be immutable,
   * serializable and thread-safe.
   *
   * @param <T> the type of the input elements
   * @return a Collector which collects all the input elements into a ImmutableSet, in encounter
   * order
   */
  public static <T> Collector<T, ImmutableSet.Builder<T>, ImmutableSet<T>> toImmutableSet() {
    return Collector.of(
        ImmutableSet.Builder::new,
        ImmutableSet.Builder::add,
        (l, r) -> l.addAll(r.build()),
        ImmutableSet.Builder<T>::build
    );
  }
}
