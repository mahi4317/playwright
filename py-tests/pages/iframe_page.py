from playwright.sync_api import Page, FrameLocator


class IframePage:
    def __init__(self, page: Page) -> None:
        self.page = page

    def navigate(self) -> "IframePage":
        self.page.goto("https://practice-automation.com/iframes/")
        self.page.wait_for_selector("iframe[name='top-iframe']")
        return self

    def iframe(self) -> FrameLocator:
        return self.page.frame_locator("iframe[name='top-iframe']")
