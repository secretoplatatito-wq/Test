# 🚀 GitHub Pages & Automated APK Build Setup

This repository is now equipped with an automated **GitHub Actions CI/CD pipeline** that compiles Gradle into an Android APK on every push and publishes it to **GitHub Pages (`username.github.io/repo`)** and **GitHub Releases**.

---

## ⚙️ 1-Minute Setup on GitHub

To activate your `.github.io` download site:

1. Open your repository on [GitHub](https://github.com).
2. Go to **Settings** (top navigation bar of your repo).
3. In the left sidebar, click **Pages** (under the "Code and automation" section).
4. Under **Build and deployment**:
   - Change **Source** from *"Deploy from a branch"* to **GitHub Actions**.
5. That's it!

---

## 📲 How It Works

Every time you push code to `main` or `master` (or manually click "Run workflow" under the **Actions** tab):

1. **Gradle Compilation**: GitHub spins up a runner, restores the debug keystore, and executes `./gradlew assembleDebug`.
2. **APK Generation**: Outputs `SuperOSINT.apk` ready for installation on any Android phone.
3. **GitHub Pages Deployment**: Deploys the landing page to your custom GitHub Pages URL:
   ```
   https://<your-username>.github.io/<your-repo-name>/
   ```
   The site features:
   - Direct download button (`./SuperOSINT.apk`)
   - Interactive QR code for mobile camera scanning
   - Installation guide for Android devices
4. **GitHub Releases Mirror**: The APK is also attached as an asset in your repository's Releases:
   ```
   https://github.com/<your-username>/<your-repo-name>/releases/tag/latest
   ```

---

## 🛠️ Testing Locally or Running Manually
To trigger a build without pushing new commits:
1. Go to the **Actions** tab in your GitHub repository.
2. Click **Build Android APK & Deploy GitHub Pages** in the left menu.
3. Click the **Run workflow** dropdown on the right and select **Run workflow**.
