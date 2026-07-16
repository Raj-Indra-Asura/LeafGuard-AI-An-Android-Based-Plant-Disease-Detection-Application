# LeafGuard AI Production Release Record

Copy this file to `release-records/vX.Y.Z-release-record.md` for each release. Do not
store passwords, private keys, API tokens, `.env` contents, or keystore files here.

## Release decision

| Field | Value |
|---|---|
| Release version | `vX.Y.Z` |
| Release classification | Course/demo / controlled pilot / public production |
| Distribution | GitHub APK / Google Play / private channel |
| Decision | GO / NO-GO |
| Decision date (UTC) | |
| Release owner | |
| Incident owner | |
| Support contact | |

## Immutable release tuple

| Field | Recorded value |
|---|---|
| Git commit SHA | |
| Git tag | |
| Android application ID | |
| Android version code | |
| Android version name | |
| Keras byte size | |
| Keras SHA-256 | |
| TFLite byte size | |
| TFLite SHA-256 | |
| Labels count | `38` |
| Labels SHA-256 | |
| Backend registry/image | |
| Backend image digest | `sha256:` |
| Backend deployed revision | |
| Production HTTPS URL | |
| APK filename | |
| APK byte size | |
| APK SHA-256 | |
| AAB filename/SHA-256, if used | |
| Signing certificate SHA-256 | |
| Previous backend rollback digest | |
| Previous Android release | |

## Gate evidence

| Gate | Result | Evidence path or URL | Approved by/date |
|---|---|---|---|
| A - Product/legal | PASS / FAIL | | |
| B - Source/dependencies | PASS / FAIL | | |
| C - Model | PASS / FAIL | | |
| D - Backend | PASS / FAIL | | |
| E - Android/device | PASS / FAIL | | |
| F - Signing identity | PASS / FAIL | | |
| G - Final artifact | PASS / FAIL | | |
| H - CI/CD | PASS / FAIL | | |
| I - Publication | PASS / FAIL | | |

## Device matrix

| Device | Android/API | ABI/RAM class | Network mode | APK SHA-256 matched | Result | Evidence |
|---|---|---|---|---|---|---|
| | | | Wi-Fi/mobile/offline | Yes / No | PASS / FAIL | |

## Model acceptance summary

| Metric | Result | Required threshold | Evidence |
|---|---:|---:|---|
| Keras/TFLite parity | | 100% top-class match | |
| Maximum confidence delta | | `<= 0.02` | |
| Independent top-1 accuracy | | Approved release threshold | |
| Lowest per-class recall | | Approved release threshold | |
| OOD/non-leaf rejection | | Approved release threshold | |
| Agricultural expert review | PASS / FAIL | PASS | |

## Backend acceptance summary

| Check | Result | Evidence |
|---|---|---|
| HTTPS and certificate | PASS / FAIL | |
| `use_mock=false`, model loaded, 38 classes | PASS / FAIL | |
| Valid JPEG/PNG inference | PASS / FAIL | |
| Invalid/empty/oversized request rejection | PASS / FAIL | |
| Rate limit, timeout, and concurrency controls | PASS / FAIL | |
| No image/sensitive-data logging | PASS / FAIL | |
| Monitoring and budget alerts | PASS / FAIL | |
| Rollback test | PASS / FAIL | |

## Android acceptance summary

| Check | Result | Evidence |
|---|---|---|
| Unit tests, lint, release build | PASS / FAIL | |
| Connected instrumentation tests | PASS / FAIL | |
| Production HTTPS default | PASS / FAIL | |
| Exact APK clean installation | PASS / FAIL | |
| Cloud prediction | PASS / FAIL | |
| Airplane-mode offline prediction | PASS / FAIL | |
| Same-image cloud/offline comparison | PASS / FAIL | |
| Permissions and failure recovery | PASS / FAIL | |
| History/analytics/library/share/notifications | PASS / FAIL | |
| Accessibility/performance/leak checks | PASS / FAIL | |
| Upgrade or beta migration behavior | PASS / FAIL | |

## Security, privacy, and legal

- [ ] Privacy policy approved and published
- [ ] Terms/acceptable-use statement approved and published
- [ ] Security contact and supported versions published
- [ ] Model license and attribution preserved
- [ ] Dataset/image licenses and citations recorded
- [ ] Diagnostic disclaimer approved in app and release notes
- [ ] Data retention, deletion, backup, and image-upload behavior approved
- [ ] Dependency/container/security findings resolved or accepted
- [ ] Keystore backed up in two encrypted locations
- [ ] No secret or private key appears in source, APK, logs, evidence, or release assets

## Publication verification

| Field | Value |
|---|---|
| Release channel URL | |
| Publication date (UTC) | |
| Downloaded artifact SHA-256 | |
| Downloaded signature verified | Yes / No |
| Clean-device install verified | Yes / No |
| Production cloud smoke test | PASS / FAIL |
| Offline smoke test | PASS / FAIL |
| Staged rollout percentage | |

## Known limitations

- 

## Rollback triggers and actions

| Trigger | Threshold | Action | Owner |
|---|---|---|---|
| Backend errors/timeouts | | Deploy previous image digest | |
| Android crashes/ANRs | | Halt rollout and prepare higher version code | |
| Model/label mismatch | Any | Halt rollout and restore approved model pair | |
| Privacy/security incident | Any confirmed incident | Contain, rotate, notify, investigate | |
| High-confidence error pattern | | Halt/limit affected mode and investigate | |

## Final sign-off

- Product/legal owner: ____________________  Date: __________
- Model/agricultural reviewer: ____________  Date: __________
- Backend/operations owner: ______________  Date: __________
- Android/release owner: __________________  Date: __________
- Final GO/NO-GO approver: _______________  Date: __________
