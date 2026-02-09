from playwright.sync_api import Page

from pages.iframe_page import IframePage


def test_verify_iframe_docs_installation_heading(page: Page) -> None:
    iframe_page = IframePage(page)

    iframe_page.navigate()
    iframe_page.iframe().get_by_role("link", name="Docs").click()

    heading = (
        iframe_page.iframe()
        .get_by_role("heading", name="Installation")
        .inner_text()
    )

    assert heading == "Installation"
