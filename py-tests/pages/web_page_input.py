from playwright.sync_api import Page, Locator


class WebPageInput:
    def __init__(self, page: Page) -> None:
        self.page = page
        self.input_field: Locator = page.get_by_placeholder("Search an example...")
        self.search_button: Locator = page.get_by_role("button", name="Search")
        self.page_heading: Locator = page.get_by_role(
            "heading", name="Sample applications for"
        )

    def open(self) -> "WebPageInput":
        self.page.goto("https://practice.expandtesting.com/")
        return self

    def enter_text(self, text: str) -> "WebPageInput":
        self.input_field.fill(text)
        return self

    def click_search(self) -> "WebPageInput":
        self.search_button.click()
        return self

    def is_heading_present(self) -> bool:
        try:
            self.page_heading.wait_for(state="visible", timeout=5000)
            return self.page_heading.is_visible()
        except Exception:
            return False

    def get_heading_text(self) -> str:
        return self.page_heading.text_content() or ""
