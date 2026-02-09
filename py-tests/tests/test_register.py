import random
import string

from playwright.sync_api import Page

from pages.register_page import RegisterPage


def random_username() -> str:
    return "user_" + "".join(random.choices(string.ascii_lowercase + string.digits, k=8))


def random_password(length: int = 12) -> str:
    chars = string.ascii_letters + string.digits + "!@#$%^&*"
    return "".join(random.choices(chars, k=length))


def mask(value: str) -> str:
    return "*" * len(value)


def test_user_registration(page: Page) -> None:
    register_page = RegisterPage(page)

    username = random_username()
    password = random_password()

    print(f"Registering user: {username}")
    print(f"Using password: {mask(password)}")

    register_page.open().enter_username(username).enter_password(password).enter_confirm_password(password).click_register()

    url = page.url
    assert (
        "register" in url or "welcome" in url or "login" in url
    ), f"Unexpected URL after registration: {url}"
