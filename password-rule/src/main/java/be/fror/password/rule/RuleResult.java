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
package be.fror.password.rule;

import com.google.common.base.MoreObjects;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 *
 * @author Olivier Grégoire &lt;fror@users.noreply.github.com&gt;
 */
public abstract class RuleResult {

  private RuleResult() {
  }

  /**
   *
   * @return <tt>true</tt> if the validation passed, <tt>false</tt> otherwise.
   */
  public abstract boolean isValid();

  /**
   *
   * @return an empty list if <tt>isValid()</tt> returns <tt>true</tt>, but a list of all the
   * <tt>Failure</tt>s otherwise.
   */
  public abstract ImmutableList<Failure> getFailures();

  /**
   *
   *
   * @return a <tt>RuleResult</tt> always returning <tt>true</tt> to <tt>isValid()</tt> and an empty
   * <tt>List</tt> to <tt>getFailures()</tt>
   */
  public static RuleResult ok() {
    return OkResult.INSTANCE;
  }

  /**
   *
   * @return
   */
  public static FailedResult failed() {
    return new FailedResult();
  }

  /**
   *
   * @param errorCode
   * @return
   */
  public static FailedResult failed(final String errorCode) {
    return new FailedResult().addFailure(errorCode);
  }

  /**
   * 
   * @param errorCode
   * @param key
   * @param value
   * @return 
   */
  public static FailedResult failed(final String errorCode, final String key, final Object value) {
    return new FailedResult().addFailure(errorCode, key, value);
  }

  /**
   * 
   * @param errorCode
   * @param key1
   * @param value1
   * @param key2
   * @param value2
   * @return 
   */
  public static FailedResult failed(final String errorCode, final String key1, final Object value1,
      final String key2, final Object value2) {
    return new FailedResult().addFailure(errorCode, key1, value1, key2, value2);
  }

  /**
   * 
   * @param errorCode
   * @param key1
   * @param value1
   * @param key2
   * @param value2
   * @param key3
   * @param value3
   * @return 
   */
  public static FailedResult failed(final String errorCode, final String key1, final Object value1,
      final String key2, final Object value2, final String key3, final Object value3) {
    return new FailedResult().addFailure(errorCode, key1, value1, key2, value2, key3, value3);
  }

  /**
   * 
   * @param errorCode
   * @param parameters
   * @return 
   */
  public static FailedResult failed(final String errorCode, final Map<String, Object> parameters) {
    return new FailedResult().addFailure(errorCode, parameters);
  }

  private static final class OkResult extends RuleResult {

    private static final RuleResult INSTANCE = new OkResult();

    private static final ImmutableList<Failure> EMPTY_FAILURES = ImmutableList.of();

    private OkResult() {
    }

    @Override
    public boolean isValid() {
      return true;
    }

    @Override
    public ImmutableList<Failure> getFailures() {
      return EMPTY_FAILURES;
    }

    @Override
    public boolean equals(Object obj) {
      return obj instanceof OkResult;
    }

    @Override
    public int hashCode() {
      return Objects.hash(OkResult.class);
    }

    @Override
    public String toString() {
      return "RuleResult.ok()";
    }

  }

  /**
   * Class visible for building and chaining reasons
   */
  public static final class FailedResult extends RuleResult {

    private final List<Failure> failures;

    FailedResult() {
      this.failures = new ArrayList<>();
    }

    @Override
    public boolean isValid() {
      return false;
    }

    @Override
    public ImmutableList<Failure> getFailures() {
      return ImmutableList.copyOf(failures);
    }

    /**
     * 
     * @param errorCode
     * @return 
     */
    public FailedResult addFailure(final String errorCode) {
      return this.addFailure(errorCode, ImmutableMap.of());
    }

    /**
     * 
     * @param errorCode
     * @param key
     * @param value
     * @return 
     */
    public FailedResult addFailure(final String errorCode, final String key, final Object value) {
      return this.addFailure(errorCode, ImmutableMap.of(key, value));
    }

    /**
     * 
     * @param errorCode
     * @param key1
     * @param value1
     * @param key2
     * @param value2
     * @return 
     */
    public FailedResult addFailure(final String errorCode, final String key1, final Object value1,
        final String key2, final Object value2) {
      return this.addFailure(errorCode, ImmutableMap.of(key1, value1, key2, value2));
    }

    /**
     * 
     * @param errorCode
     * @param key1
     * @param value1
     * @param key2
     * @param value2
     * @param key3
     * @param value3
     * @return 
     */
    public FailedResult addFailure(final String errorCode, final String key1, final Object value1,
        final String key2, final Object value2, final String key3, final Object value3) {
      return this.addFailure(errorCode, ImmutableMap.of(key1, value1, key2, value2, key3, value3));
    }

    /**
     * 
     * @param errorCode
     * @param parameters
     * @return 
     */
    public FailedResult addFailure(final String errorCode, final Map<String, Object> parameters) {
      this.failures.add(new Failure(errorCode, ImmutableMap.copyOf(parameters)));
      return this;
    }

    void addFailures(final ImmutableList<Failure> failures) {
      this.failures.addAll(failures);
    }

    @Override
    public boolean equals(Object obj) {
      if (obj == null || this.getClass() != obj.getClass()) {
        return false;
      }
      FailedResult other = (FailedResult) obj;
      return Objects.equals(this.failures, other.failures);
    }

    @Override
    public int hashCode() {
      return Objects.hash(FailedResult.class, this.failures);
    }

    @Override
    public String toString() {
      return MoreObjects.toStringHelper("RuleResult.failed()")
          .addValue(this.failures)
          .toString();
    }
  }
}
