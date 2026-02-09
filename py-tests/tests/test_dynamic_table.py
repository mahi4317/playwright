from playwright.sync_api import Page

from pages.dynamic_table_page import DynamicTablePage


def test_display_rows(page: Page) -> None:
    dynamic_table = DynamicTablePage(page)
    dynamic_table.open()

    data = dynamic_table.get_all_table_data()
    for i, row in enumerate(data):
        print(f"Row {i}: {' | '.join(row)}")

    assert dynamic_table.get_row_count() > 0


def test_verify_chrome_cpu_value(page: Page) -> None:
    dynamic_table = DynamicTablePage(page)
    dynamic_table.open()

    chrome_cpu_from_table = dynamic_table.get_cpu_value_for_process("Chrome")
    cpu_from_label = dynamic_table.extract_cpu_from_label()

    assert (
        chrome_cpu_from_table == cpu_from_label
    ), f"Mismatch: table={chrome_cpu_from_table}, label={cpu_from_label}"
