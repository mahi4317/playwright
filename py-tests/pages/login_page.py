from playwright.sync_api import Page, Locator


class LoginPage:
    def __init__(self, page: Page) -> None:
        self.page = page
        self.username_input: Locator = page.get_by_label("Username")
        self.password_input: Locator = page.get_by_label("Password")
        self.login_button: Locator = page.get_by_role("button", name="Login")
        self.success_heading: Locator = page.get_by_role(
            "heading", name="You logged into a secure area!"
        )

    def open(self) -> "LoginPage":
        self.page.goto("https://practice.expandtesting.com/login")
        return self

    def login(self, username: str, password: str) -> "LoginPage":
        self.username_input.fill(username)
        self.password_input.fill(password)
        self.login_button.click()
        return self

    def is_logged_in(self) -> bool:
        url = self.page.url
        if "/secure" in url:
            return True
        try:
            return self.success_heading.is_visible()
        except Exception:
            return False
