from __future__ import annotations

import pytest
from playwright.sync_api import Page


@pytest.fixture(autouse=True)
def base_page_setup(page: Page, request: pytest.FixtureRequest) -> None:
    """Shared per-test setup using the built-in page fixture.

    Similar to Java's BaseTest/BrowserContextManager: central place to
    configure timeouts, tracing, and simple logging for every test.
    """

    page.set_default_timeout(30_000)
    print(f"[PY] Starting test: {request.node.nodeid}")
