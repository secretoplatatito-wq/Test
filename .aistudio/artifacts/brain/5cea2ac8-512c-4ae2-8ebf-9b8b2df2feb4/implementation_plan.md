# Automated GitHub Gradle APK Build & GitHub Pages Download Portal

Automates the build process on GitHub so that every push compiles the project via Gradle into an installable APK, publishes it to GitHub Releases, and deploys a tactical GitHub Pages landing site (`username.github.io/<repo>`) where users can download the APK directly to their Android device.

## User Review & Critical Decisions

> [!IMPORTANT]
> The confirmed choices from the interview phase will be implemented:
> - **Distribution Strategy**: GitHub Releases + a dedicated GitHub Pages download portal (`username.github.io`).
> - **Build Target**: Debug APK (`SuperOSINT-debug.apk`), ensuring it can be installed on any Android device without requiring custom production keystore certificates.
> - **Zero-Config Keystore in CI**: The workflow will automatically decode the project's root `debug.keystore.base64` before compiling, guaranteeing that Gradle finds the debug keystore without missing-file failures.

---

## 1. Overview & Core Concept

Currently, pushing code to GitHub only hosts the source repository. GitHub Pages by itself is a static web hosting service and does not execute Java/Kotlin compilers or Gradle. 

To turn Gradle code into an APK automatically and host it on a `.github.io` link, we establish a two-stage automated pipeline:
1. **GitHub Actions Runner (CI/CD)**: Triggers on code push or manual trigger. Boots an Ubuntu runner with JDK 17, restores the debug signing key, generates `.env` placeholders, runs `gradle assembleDebug`, and exports `SuperOSINT-debug.apk`.
2. **Dual-Channel Distribution**:
   - **GitHub Releases**: Hosts the immutable APK asset tagged with the build timestamp/version.
   - **GitHub Pages Portal (`username.github.io/<repo>`)**: A custom-styled cyber/tactical download landing page with release version metadata, direct "Download APK" button, installation guide (Allow Unknown Sources / Play Protect tips), and a mobile QR code for instant scanning and downloading.

```
┌────────────────────────┐      ┌──────────────────────────┐
│   git push to GitHub   │ ───► │  GitHub Actions Workflow │
└────────────────────────┘      └────────────┬─────────────┘
                                             │
                       ┌─────────────────────┴─────────────────────┐
                       ▼                                           ▼
         ┌───────────────────────────┐               ┌───────────────────────────┐
         │       Gradle Build        │               │   Deploy GitHub Pages     │
         │   `gradle assembleDebug`  │               │   (`username.github.io`)  │
         ├───────────────────────────┤               ├───────────────────────────┤
         │ • Decodes debug.keystore  │               │ • Cyber Tactical Theme    │
         │ • Compiles Kotlin/Compose │               │ • Direct APK Download     │
         │ • Produces app-debug.apk  │               │ • QR Code for Mobile Scan │
         │ • Uploads to GH Releases  │               │ • Install Instructions    │
         └───────────────────────────┘               └───────────────────────────┘
```

---

## 2. User Experience & Visual Design (GitHub Pages Portal)

- **Aesthetic Direction**: Sleek, high-contrast tactical cyber dark theme matching the Super OSINT mobile application (Deep navy `#070B12`, neon cyan `#00E5FF`, tactical amber `#FFB300`).
- **Hero & Download Card**:
  - Prominent badge displaying current build status and version.
  - One-tap "Download SuperOSINT APK" primary button.
  - Dynamic QR code generation allowing users to point their phone camera at the computer screen to start downloading immediately on mobile.
- **Device Installation Guide**:
  - Clear 3-step accordion on how to install third-party APKs on modern Android (Chrome "Open", "Settings -> Allow from this source", "Install").
- **Core Feature Showcase**:
  - Highlights the 7y7g OSINT Framework, Deep Search Matrix, EXIF Forensics, and Gemini AI Analyst capabilities.

---

## 3. Key Technical Decisions & Workflow Architecture

### Decision 1: GitHub Actions for Compilation vs. Static Pages
- *Chosen Approach*: Use a GitHub Actions workflow (`.github/workflows/build-apk.yml`) with the official `actions/setup-java@v4` and `gradle/actions/setup-gradle@v4`.
- *Why*: GitHub Pages cannot compile code. Running Gradle in GitHub Actions provides free cloud computing to compile the Android app into `.apk` on every commit.

### Decision 2: Keystore & Secrets Handling in GitHub CI
- *Chosen Approach*: 
  1. Generate `.env` from `.env.example` in CI so `secrets-gradle-plugin` finds the configuration file.
  2. Decode `debug.keystore.base64` into `debug.keystore` before Gradle executes.
- *Why*: Eliminates the common Gradle failure `debug.keystore not found` when building in clean cloud containers.

### Decision 3: Direct APK Linking from GitHub Pages
- *Chosen Approach*: The workflow copies the built APK into the deployment directory of the Pages site, allowing the GitHub Pages site itself to host the direct download (`https://<username>.github.io/<repo>/SuperOSINT.apk`), while also mirroring the file to GitHub Releases as a permanent fallback.
- *Why*: Users can download the APK directly from the website without having to navigate GitHub's developer UI.

---

## 4. Planned Changes & Deliverables

1. **GitHub Actions Workflow (`.github/workflows/build-apk.yml`)**:
   - Automatic triggers on `push` to `main` / `master`, and `workflow_dispatch` (manual one-click build).
   - JDK 17 environment setup.
   - Gradle caching for fast subsequent builds.
   - Build task execution: `gradle assembleDebug`.
   - Artifact export + Release creation.
   - Automated deployment of the download site to GitHub Pages via `actions/deploy-pages@v4`.

2. **Web Portal Site (`web/index.html` & `web/style.css`)**:
   - Modern, single-file lightweight download portal.
   - Real-time download link pointing to the latest APK artifact.
   - Responsive layout for both desktop and mobile web browsers.
   - Android sideloading tips and safety verification details.

3. **Repository Instructions (`GITHUB_PAGES_SETUP.md`)**:
   - Clear guide for the user on how to enable GitHub Pages in their repository settings (switching Pages source to **GitHub Actions** with 2 clicks).
