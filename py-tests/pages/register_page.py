from playwright.sync_api import Page, Locator


class RegisterPage:
    def __init__(self, page: Page) -> None:
        self.page = page
        self.username_field: Locator = page.get_by_label("Username")
        self.password_field: Locator = page.locator("#password")
        self.confirm_password_field: Locator = page.locator("#confirmPassword")
        self.register_button: Locator = page.get_by_role("button", name="Register")
        self.success_message: Locator = page.locator(
            ".alert-success, .success-message, [role='alert']"
        )
        self.error_message: Locator = page.locator(
            ".alert-danger, .error-message, .alert-error"
        )

    def open(self) -> "RegisterPage":
        self.page.goto("https://practice.expandtesting.com/register")
        return self

    def enter_username(self, username: str) -> "RegisterPage":
        self.username_field.fill(username)
        return self

    def enter_password(self, password: str) -> "RegisterPage":
        self.password_field.fill(password)
        return self

    def enter_confirm_password(self, confirm_password: str) -> "RegisterPage":
        self.confirm_password_field.fill(confirm_password)
        return self

    def click_register(self) -> "RegisterPage":
        self.register_button.click()
        return self

    def is_success_message_visible(self) -> bool:
        return self.success_message.is_visible()

    def is_error_message_visible(self) -> bool:
        return self.error_message.is_visible()

    def get_success_message_text(self) -> str:
        return self.success_message.text_content() or ""

    def get_error_message_text(self) -> str:
        return self.error_message.text_content() or ""
