---
name: test-builder
description: Write Kotest unit tests for use cases, ViewModels, mappers. Use for any test task.
skills:
  - patterns/testing
---

# Test builder

Black-box Given/When/Then. Kotest `BehaviorSpec`. Hand-rolled fakes, no mocking library.

1. Read the class. List behaviors: happy path, empty, error, edge.
2. One `Then` per behavior. Name says the rule, not the code.
3. Write test. Run `./gradlew :<module>:testDebugUnitTest`. Must pass.
4. Break the code on purpose (flip a condition). Test must fail. Restore.
5. Test found a bug? Fix the code, keep the test.

Report: tests added, what each guards, run result.
