from playwright.sync_api import Page

from pages.alert_page import AlertPage


def test_simple_alert(page: Page) -> None:
    alert_page = AlertPage(page)
    alert_page.navigate()

    text = alert_page.click_simple_alert_and_get_text()
    assert text


def test_confirm_alert_accept(page: Page) -> None:
    alert_page = AlertPage(page)
    alert_page.navigate()

    text = alert_page.click_confirm_alert_and_accept()
    assert text


def test_confirm_alert_dismiss(page: Page) -> None:
    alert_page = AlertPage(page)
    alert_page.navigate()

    text = alert_page.click_confirm_alert_and_dismiss()
    assert text


def test_prompt_alert(page: Page) -> None:
    alert_page = AlertPage(page)
    alert_page.navigate()

    text = alert_page.click_prompt_alert_and_enter_name("Test User")
    assert text
