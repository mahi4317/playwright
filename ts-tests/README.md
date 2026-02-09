# Playwright TypeScript Tests

This folder contains a separate Playwright Test project written in TypeScript, mirroring flows from the Java Playwright framework.

## Structure

- `playwright.config.ts` – Playwright Test configuration
- `tsconfig.json` – TypeScript configuration
- `src/pages` – Page Object Model classes
- `src/tests` – Test specs

## Setup

```sh
cd ts-tests
npm install
npx playwright install
```

## Run tests

```sh
cd ts-tests
npx playwright test
```
