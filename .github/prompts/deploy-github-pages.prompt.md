---
name: deploy-github-pages
description: "Generate or update the GitHub Actions workflow to deploy the static landing page to GitHub Pages. Use when: deploying to GitHub Pages, setting up CI/CD for the landing page, publishing the portfolio landing page."
---

Generate a GitHub Actions workflow file at `.github/workflows/deploy-pages.yml` that deploys the static landing page to GitHub Pages.

**Context:** Only the root-level static files (`index.html`, `script.js`, `styles.css`) can be deployed to GitHub Pages — the Spring Boot backend cannot run there.

**Requirements:**
1. Trigger on push to `main` branch
2. Use `actions/configure-pages`, `actions/upload-pages-artifact`, and `actions/deploy-pages`
3. Upload the root directory as the Pages artifact (include `index.html`, `script.js`, `styles.css`)
4. Set the correct `permissions` block (`pages: write`, `id-token: write`)
5. Use the `github-pages` environment

After generating the workflow, also remind the user to:
- Enable GitHub Pages in repository **Settings → Pages → Source: GitHub Actions**
- Optionally update CTA links in `index.html` to point to the live backend URL (if deployed separately)
