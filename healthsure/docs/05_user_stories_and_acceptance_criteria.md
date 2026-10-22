# 05. User Stories & Acceptance Criteria

### US-01: User Registration and Secure Login
- **As a** prospective policyholder,
- **I want to** register for an account and log in with my credentials,
- **So that** I can submit claims and access my insurance records securely.
- **Acceptance Criteria:**
  - Password must be hashed using BCrypt before storing in database.
  - Form validation must enforce unique username and valid email.
  - Invalid credentials display user-friendly feedback without exposing internal stack traces.

### US-02: Claim Draft Creation and Immediate Submission
- **As a** policyholder,
- **I want to** select my active insurance policy and submit medical expense details,
- **So that** my claim can be reviewed and processed for reimbursement.
- **Acceptance Criteria:**
  - User can only select active policies registered under their own user account.
  - Claimed amount must be greater than zero and must not exceed remaining policy balance.
  - A unique tracking reference formatted as `CLM-YYYYMMDD-XXXX` must be generated.

### US-03: Supporting Document Attachment
- **As a** policyholder,
- **I want to** upload hospital bills and diagnostic reports,
- **So that** the reviewer can substantiate the validity of my medical procedure.
- **Acceptance Criteria:**
  - Only PDF, PNG, and JPEG files under 10MB are accepted.
  - File is stored securely with UUID-based filename outside static document roots.
  - Direct download links are authorized only for claim owner, reviewer, and admin.

### US-04: Automated Claim Validation
- **As a** reviewer,
- **I want** the system to automatically validate policy dates, balances, and duplicates,
- **So that** only complete and non-duplicate claims enter my adjudication queue.
- **Acceptance Criteria:**
  - Inverted treatment dates or expired policies immediately transition to `VALIDATION_FAILED`.
  - Duplicate claims within 30 days are flagged with actionable descriptions.

### US-05: Reviewer Adjudication (Approve / Reject / Request Info)
- **As a** claims reviewer,
- **I want to** approve, reject, or request additional information on submitted claims,
- **So that** legitimate claims are approved and questionable submissions are properly audited.
- **Acceptance Criteria:**
  - Approving a claim updates policy `usedCoverage` and creates a `ClaimReview` record.
  - Rejecting a claim strictly requires a non-blank mandatory rejection reason.
  - Requesting info transitions state to `ADDITIONAL_INFO_REQUIRED`.
