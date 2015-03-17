# password-rule

`password-rule` is a library to validate or generate passwords based on rules.

This project requires JDK 1.8 or higher.

## Rules

Rules are the core of the library. They define how the password validation must
proceed.

Rules simply implement the `Rule` interface and return a result explaining
whether the validation succeeded or failed. If the result failed, error codes
should be given to explain the cause of the failure.

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

### Password validation

For example, a rule has the responsibility to check the length of a password
while other rules have the responsibility to check the content of the password,
making sure that it isn't too easy to guess.

```java
Ruler ruler = Ruler.createFromRules(Arrays.asList(
    Rule.lengthIs(8), // rule checking that the length is exactly 8
    Rule.asciiLetter(2) // rule checking that the password contains at least
                        // two ascii letters.
  ));

ruler.validatePassword("abcdefgh"); // Will return RuleResult.ok()
                                    // because both rules constraints are met.

ruler.validatePassword("abc"); // Will return RuleResult.failed()
                               // because the length constraint is not met.

ruler.validatePassword("12345678"); // Will return RuleResult.failed()
                                    // because there is no ASCII letter in
                                    // the password.

ruler.validatePassword("123"); // Will return RuleResult.failed()
                               // because both constraints are not met.
```

### Password generation

Another responsibility of the `Ruler` is to generate passwords. This can be done
only by using the interface `CharacterRule` (which extends `Rule`).

Most character-based rules defined as static methods in `Rule` already implement
`CharacterRule`, but any custom `CharacterRule` will work so long as the
contract is strictly respected.

```java
Ruler ruler = Ruler.createFromRules(Arrays.asList(
    Rule.lengthIs(8),     // rule checking that the length is exactly 8
    Rule.asciiLetters(2), // rule checking that the password contains at least
                          // two ascii letters ('a' -> 'z' or 'A' -> 'z').
    Rule.asciiDigits(2)   // rule checking that the password contains at least
                          // two ascii digits ('0' -> '9')
  ));

Random random = new Random(); // The random object used to generate passwords.

int passwordLength = 10; // Rule.lengthIs() is not a character rule so it is not
                         // enforced and another length must be provided.

for (int i = 0; i < 10; i++) {
    String password = ruler.generatePassword(passwordLength, random);
    System.out.println(password);
}
```

Here is one example of resulting passwords:

```
VI72yNBeur
31v6TuiTHl
tnaI2G3WX1
HUP3d4cLQf
eX3E28TQrG
TChWeG9ay8
YD8eD1G5C1
tt0vK6dc6M
Zjnc078ceO
5ogx386sRF
```