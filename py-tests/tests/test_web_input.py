from playwright.sync_api import Page

from pages.web_page_input import WebPageInput


def test_web_input(page: Page) -> None:
    web_input = WebPageInput(page)

    web_input.open().enter_text("Test").click_search()

    assert web_input.is_heading_present(), "Page heading should be present"

    heading_text = web_input.get_heading_text()
    assert "Sample applications for" in heading_text
