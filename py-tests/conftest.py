from __future__ import annotations

import pytest
from playwright.sync_api import Browser, BrowserContext, Page, Playwright, sync_playwright


def _resolve_browser_name(request: pytest.FixtureRequest) -> str:
    browser_option = request.config.getoption("browser", default=None)
    if isinstance(browser_option, (list, tuple)):
        return browser_option[0] if browser_option else "chromium"
    if isinstance(browser_option, str) and browser_option:
        return browser_option
    return "chromium"


def _resolve_headless_mode(request: pytest.FixtureRequest) -> bool:
    headed = request.config.getoption("headed", default=False)
    return not bool(headed)


@pytest.fixture(scope="session")
def playwright_instance():
    with sync_playwright() as playwright:
        yield playwright


@pytest.fixture(scope="session")
def browser(
    request: pytest.FixtureRequest,
    playwright_instance: Playwright,
):
    browser_name = _resolve_browser_name(request)
    headless = _resolve_headless_mode(request)

    browser_type = getattr(playwright_instance, browser_name)
    launched_browser = browser_type.launch(headless=headless)
    yield launched_browser
    launched_browser.close()


@pytest.fixture(scope="function")
def context(browser: Browser):
    browser_context = browser.new_context()
    yield browser_context
    browser_context.close()


@pytest.fixture(scope="function")
def page(context: BrowserContext, request: pytest.FixtureRequest):
    test_page = context.new_page()
    test_page.set_default_timeout(30_000)
    print(f"[PY] Starting test: {request.node.nodeid}")
    yield test_page
    test_page.close()
