---
name: commit
description: Commit current work with a conventional message after checks pass.
---

# Commit

1. `/build` green. (Commit hook runs checks too and blocks on red.)
2. `git status` — stage only your files. No build outputs, no secrets.
3. Message: `feat(cart): add quantity buttons`. Types: feat, fix, refactor, test, docs, chore.
4. One logical change per commit. Push branch, open PR, squash merge.
