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

import static com.google.common.base.Preconditions.checkNotNull;
import static com.google.common.base.Preconditions.checkState;

import com.google.common.annotations.VisibleForTesting;
import com.google.common.collect.ImmutableSet;

import javax.annotation.concurrent.NotThreadSafe;
import javax.annotation.concurrent.ThreadSafe;

/**
 *
 * @author Olivier Grégoire &lt;fror@users.noreply.github.com&gt;
 */
@ThreadSafe
public final class Ruler {

  /**
   * Creates a new <tt>Ruler</tt> with all the rules given as parameters.
   *
   * @param rules the rules to create the ruler with.
   * @return a new <tt>Ruler</tt> using each <tt>Rule</tt> of <tt>rules</tt>.
   */
  public static Ruler createRuler(Iterable<Rule> rules) {
    return new Builder().addRules(rules).build();
  }

  @VisibleForTesting
  final ImmutableSet<Rule> rules;

  @VisibleForTesting
  Ruler(Builder builder) {
    this.rules = builder.rules.build();
  }

  /**
   * Validates the password according to the defined rules and aggregates their result in a
   * <tt>{@link RuleResult}</tt>.
   *
   * @param password the password to validatePassword
   * @return the result of the validation
   */
  public RuleResult validatePassword(final String password) {
    checkNotNull(password, "password must not be null");
    Password pwd = new Password(password);
    RuleResult.FailedResult failedResult = RuleResult.failed();
    boolean failed = false;
    for (Rule rule : this.rules) {
      RuleResult result = rule.validate(pwd);
      if (!result.isValid()) {
        failed = true;
        failedResult.addFailures(result.getFailures());
      }
    }
    if (failed) {
      return failedResult;
    } else {
      return RuleResult.ok();
    }
  }

  /**
   * The builder class for Ruler.
   *
   * <p>
   * The rules of the <tt>Ruler</tt> are ordered in the order they were added in this Builder.
   *
   * <p>
   * The methods are chained, meaning that it is possible to create a <tt>Ruler</tt> like this:
   *
   * <pre><code>
   * Ruler ruler = new Ruler.Builder()
   *   .addRule(firstRule)
   *   .addRule(secondRule)
   *   .build();
   * </code></pre>
   */
  @NotThreadSafe
  public static class Builder {

    private boolean nonEmpty = false;
    private final ImmutableSet.Builder<Rule> rules = ImmutableSet.builder();

    /**
     * Instanciates a new <tt>Rule</tt> builder.
     */
    public Builder() {
    }

    /**
     * Adds a rule to the <tt>Ruler</tt> being built.
     *
     * <p>
     * If two identical rules are added, only the first is retained and the other one is silently
     * not added.
     *
     * @param rule the <tt>Rule</tt> to add.
     * @return <tt>this</tt>
     */
    public Builder addRule(Rule rule) {
      this.rules.add(rule);
      this.nonEmpty = true;
      return this;
    }

    /**
     * Adds rules to the <tt>Ruler</tt> being built.
     *
     * <p>
     * If two identical rules are added, only the first is retained and the other one is silently
     * not added.
     *
     * @param rules the <tt>Rule</tt> to add.
     * @return <tt>this</tt>
     */
    public Builder addRules(Iterable<Rule> rules) {
      boolean nonEmptyRules = rules.iterator().hasNext();
      this.rules.addAll(rules);
      this.nonEmpty |= nonEmptyRules;
      return this;
    }

    /**
     * Creates a new <tt>Ruler</tt> with all the rules that were add to <tt>this</tt>.
     *
     * @return a new ruler containing the rules
     * @throws IllegalStateException if no rule were added to <tt>this</tt>.
     */
    public Ruler build() {
      checkState(this.nonEmpty, "No rules were added to this builder");
      return new Ruler(this);
    }
  }
}
