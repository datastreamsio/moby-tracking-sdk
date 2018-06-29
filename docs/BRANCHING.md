# Branching

## Strict
| Branch | Description |
| ------ | ----------- |
| `master` | No commits go directly onto this branch. It's only updated after a **reviewed** Pull Request. |
| `feat/*` | Feature branches only add new functionality. They should be prepended by feat/ and only commit code dedicated to that feature. The name should be descriptive of the new functionality. |
| `fix/*` | Fix branches only repair broken functionality / bugs and do not add new functionality. They should be prepended by fix/ and descriptive of the bug it's supposed to fix. |
| `refactor/-*` | Not necessarily a feature nor bug fix, refactors branches usually only clean up code afterwards. This could be enhancing an existing feature to improve efficiency, or renaming variables and such. |

## Optional
| Branch | Description |
| ------ | ----------- |
| `*/android-*` | Prefixing a name inside a branch increases clarity. For example, `fix/android-networking-error` clearly states the branch is meant for the Android SDK. This is particularly helpful when one issue occurs on both platforms. |
| `*/ios-*` | See above. Example here: `refactor/ios-battery-optimization.` |

## Typical workflow
> Creating a unit test for the dispatcher on the Android SDK (pseudo code)
```bash
git checkout master # go to master branch
git pull # pull latest changes from origin (github)
git checkout -b feat/android-unittest-dispatcher # creates a new branch named feat/android-unittest-dispatcher
# ...
# coding
# ...
git add <files>
git commit <changes>
git push
# create pull request on GitHub
# assign reviewer
# await review while working on another task
# approved
# merge into master
# remove old branch for clarity on GitHub (locally optional)
```
