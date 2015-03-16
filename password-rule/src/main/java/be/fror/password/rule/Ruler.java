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

import static be.fror.common.function.MoreCollectors.toImmutableSet;
import static be.fror.common.function.Suppliers.memoize;
import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;
import static com.google.common.base.Preconditions.checkState;

import com.google.common.annotations.VisibleForTesting;
import com.google.common.collect.ImmutableSet;

import java.util.Collection;
import java.util.Random;
import java.util.function.Supplier;

import javax.annotation.concurrent.Immutable;
import javax.annotation.concurrent.NotThreadSafe;
import javax.annotation.concurrent.ThreadSafe;

/**
 *
 * @author Olivier Grégoire &lt;fror@users.noreply.github.com&gt;
 */
@ThreadSafe
@Immutable
public final class Ruler {

  /**
   * Factory method to easily create a new <tt>Ruler</tt> with all the rules given as parameter.
   *
   * @param rules the rules to create the ruler with.
   * @return a new <tt>Ruler</tt> using each <tt>Rule</tt> of <tt>rules</tt>.
   */
  public static Ruler createRuler(Iterable<Rule> rules) {
    return new Builder().addRules(rules).build();
  }

  @VisibleForTesting
  final ImmutableSet<Rule> rules;

  private final Supplier<ImmutableSet<CharacterRule>> characterRules;

  @VisibleForTesting
  Ruler(Builder builder) {
    this.rules = builder.rules.build();
    this.characterRules = memoize(() -> rules.stream()
        .filter((e) -> e instanceof CharacterRule)
        .map((e) -> (CharacterRule) e)
        .collect(toImmutableSet())
    );
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
    final Password pwd = new Password(password);
    final RuleResult.FailedResult failedResult = RuleResult.failed();
    boolean failed = false;
    for (Rule rule : this.rules) {
      final RuleResult result = rule.validate(pwd);
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
   * Generates a password of length <tt>length</tt> using <tt>random</tt>.
   *
   * @param length the length of the password to generate
   * @param random the random number generator to use to generate the password
   * @return A password valid according the provided character rules.
   * @throws IllegalArgumentException if <tt>length &lt;= 0</tt>
   * @throws IllegalStateException if no <tt>CharacterRule</tt> were provided when creating this
   * ruler.
   */
  public String generatePassword(final int length, final Random random) {
    checkArgument(length > 0, "length must greater than 0");
    checkNotNull(random, "random must not be null");
    final Collection<CharacterRule> charRules = this.characterRules.get();
    checkState(charRules.size() > 0, "No CharacterRule were added to this Ruler");
    final StringBuilder allChars = new StringBuilder();
    final StringBuilder builder = new StringBuilder();
    charRules.stream().forEach((rule) -> {
      final String source = rule.getValidCharacters();
      allChars.append(source);
      for (int i = 0, l = rule.getNumberOfCharacters(); i < l; i++) {
        builder.append(source.charAt(random.nextInt(source.length())));
      }
    });
    for (int i = builder.length(); i < length; i++) {
      builder.append(allChars.charAt(random.nextInt(allChars.length())));
    }
    for (int i = 0, len = builder.length() - 1; i < len; i++) {
      // length - 1 because the last call would always be switching the last character with itself.
      int pos = i + random.nextInt(builder.length() - i);
      char c = builder.charAt(pos);
      builder.setCharAt(pos, builder.charAt(i));
      builder.setCharAt(i, c);
    }
    return builder.toString();
  }

  /**
   * The builder class for Ruler.
   *
   * <p>
   * The rules of the <tt>Ruler</tt> are ordered in the order they were added in this Builder.
   *
   * <p>
   * The methods are chained, meaning that it is possible to create a
   * <tt>Ruler</tt> like this:
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
     * Instantiates a new <tt>Rule</tt> builder.
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
     * Creates a new <tt>Ruler</tt> with all the rules that were add to
     * <tt>this</tt>.
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
