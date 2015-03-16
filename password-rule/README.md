# password-rule

`password-rule` is a library to validate or generate passwords based on rules.

## Rules

Rules are the core of the library. They define how the password validation must
proceed.

Rules simply implement the `Rule` interface and return a result explaining
whether the validation succeeded or failed. If the result failed, reasons should
be given to explain the cause of the failure.

An example of `Rule` usage:

```java
/**
 * Checks that a password is exactly <tt>"abc"</tt>.
 */
class AbcMatcher implements Rule {
  @Override public RuleResult validate(Password password) {
    if ("abc".equals(password.getPassword())) {
      return RuleResult.ok();
    } else {
      return RuleResult.failed("abc");
    }
  }
}
```

## Ruler

A `Ruler` is a mechanism that allow to group several `Rule`s into one common
API. A `Ruler` is an immutable object whose goal is to aggregate Rules and have
them return a single validation result. This is useful if several rules have to
work together without impacting each other.

For example, a rule has the responsibility to check the length of a password
while other rules have the responsibility to check the content of the password,
making sure that it isn't too easy to guess.

```java
Ruler ruler = Ruler.createRuler(Arrays.asList(
    Rule.lengthIs(8), // rule checking that the length is exactly 8
    Rule.asciiLetter(2) // rule checking that the password contains at least
                        // two ascii letters.
  ));

ruler.validate("abcdefgh"); // Will return RuleResult.ok()
                            // because both rules constraints are met.

ruler.validate("abc"); // Will return RuleResult.failed()
                       // because the length constraint is not met.

ruler.validate("12345678"); // Will return RuleResult.failed()
                            // because there is no ASCII letter in the password.

ruler.validate("123"); // Will return RuleResult.failed()
                       // because both constraints are not met.
```