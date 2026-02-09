from playwright.sync_api import Page, Locator


class AlertPage:
    def __init__(self, page: Page) -> None:
        self.page = page

    def navigate(self) -> "AlertPage":
        self.page.goto("https://www.qaplayground.com/practice/alert")
        return self

    def _simple_alert_button(self) -> Locator:
        return self.page.locator("button:has-text('Simple Alert')").first

    def _confirm_alert_button(self) -> Locator:
        return self.page.locator("button:has-text('Confirm Alert')").first

    def _prompt_alert_button(self) -> Locator:
        return self.page.locator("button:has-text('Prompt Alert')").first

    def click_simple_alert_and_get_text(self) -> str:
        message = ""

        def handle_dialog(dialog):
            nonlocal message
            message = dialog.message
            dialog.accept()

        self.page.once("dialog", handle_dialog)
        self._simple_alert_button().click()
        self.page.wait_for_timeout(500)
        return message

    def click_confirm_alert_and_accept(self) -> str:
        message = ""

        def handle_dialog(dialog):
            nonlocal message
            message = dialog.message
            dialog.accept()

        self.page.once("dialog", handle_dialog)
        self._confirm_alert_button().click()
        self.page.wait_for_timeout(500)
        return message

    def click_confirm_alert_and_dismiss(self) -> str:
        message = ""

        def handle_dialog(dialog):
            nonlocal message
            message = dialog.message
            dialog.dismiss()

        self.page.once("dialog", handle_dialog)
        self._confirm_alert_button().click()
        self.page.wait_for_timeout(500)
        return message

    def click_prompt_alert_and_enter_name(self, name: str) -> str:
        message = ""

        def handle_dialog(dialog):
            nonlocal message
            message = dialog.message
            dialog.accept(name)

        self.page.once("dialog", handle_dialog)
        self._prompt_alert_button().click()
        self.page.wait_for_timeout(500)
        return message
