# 08. Use-Case Diagram

The following Mermaid diagram models the core use-case interactions for Policyholders, Claims Reviewers, and Administrators within the HealthSure platform:

```mermaid
flowchart TD
    subgraph Actors
        P[Policyholder]
        R[Claims Reviewer]
        A[Administrator]
    end

    subgraph "HealthSure Insurance Portal"
        UC01((Register Account))
        UC02((Sign In / Logout))
        UC03((View Active Policies))
        UC04((Submit New Claim))
        UC05((Upload Documents))
        UC06((Track Claim Status))
        UC07((Respond to Info Requests))

        UC08((Access Review Queue))
        UC09((Examine Supporting Files))
        UC10((Approve Claim))
        UC11((Reject Claim with Reason))
        UC12((Request Additional Info))

        UC13((Manage Users & Roles))
        UC14((Create & Assign Policies))
        UC15((View Global System Metrics))
        UC16((Inspect Immutable Audit Trail))
    end

    P --> UC01
    P --> UC02
    P --> UC03
    P --> UC04
    P --> UC05
    P --> UC06
    P --> UC07

    R --> UC02
    R --> UC08
    R --> UC09
    R --> UC10
    R --> UC11
    R --> UC12

    A --> UC02
    A --> UC13
    A --> UC14
    A --> UC15
    A --> UC16
```

### Actor Use-Case Matrix
- **Policyholder:** Manages self-registration, claim draft submission, file attachment, and lifecycle monitoring.
- **Claims Reviewer:** Evaluates claims in `UNDER_REVIEW`, inspects documents, requests additional diagnostic information, or approves/rejects claims with audited notes.
- **Administrator:** Manages system users, allocates insurance policies, reviews global claim counts and sums, and inspects the audit trail.
