from playwright.sync_api import Page

from pages.login_page import LoginPage


def test_login(page: Page) -> None:
    login_page = LoginPage(page)

    login_page.open().login("practice", "SuperSecretPassword!")

    assert login_page.is_logged_in(), f"Login was not successful. URL: {page.url}"
