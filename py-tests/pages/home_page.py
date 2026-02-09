from playwright.sync_api import Page


class HomePage:
    def __init__(self, page: Page) -> None:
        self.page = page

    def open(self, url: str) -> "HomePage":
        self.page.goto(url)
        return self

    def get_title(self) -> str:
        return self.page.title()

    def get_url(self) -> str:
        return self.page.url

    def url_contains(self, text: str) -> bool:
        return text in self.get_url()
