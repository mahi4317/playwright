from playwright.sync_api import Page, expect

from pages.home_page import HomePage


def test_launch_base_url_uses_pom(page: Page) -> None:
    base_url = "https://practice.expandtesting.com/"

    home = HomePage(page).open(base_url)

    assert home.url_contains("expandtesting.com"), f"URL was: {home.get_url()}"

    title = home.get_title() or ""
    lowered = title.lower()
    assert (
        not title
        or "practice" in lowered
        or "expand" in lowered
    ), f"Unexpected title: {title}"
