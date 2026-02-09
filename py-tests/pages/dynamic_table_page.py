from typing import List

from playwright.sync_api import Page, Locator


class DynamicTablePage:
    def __init__(self, page: Page) -> None:
        self.page = page

    def _table_rows(self) -> Locator:
        return self.page.locator("table tbody tr")

    def _table_headers(self) -> Locator:
        return self.page.locator("table thead th")

    def _yellow_label(self) -> Locator:
        return self.page.locator(".bg-warning")

    def open(self) -> "DynamicTablePage":
        self.page.goto("https://practice.expandtesting.com/dynamic-table")
        self.page.wait_for_selector("table")
        return self

    def _get_column_index_by_header(self, header_name: str) -> int:
        headers: List[str] = self._table_headers().all_inner_texts()
        for i, header in enumerate(headers):
            if header.strip().lower() == header_name.strip().lower():
                return i
        return -1

    def get_cpu_value_for_process(self, process_name: str) -> str:
        cpu_col_index = self._get_column_index_by_header("CPU")
        if cpu_col_index == -1:
            raise RuntimeError("CPU column not found in table")

        row = self._table_rows().filter(has_text=process_name).first
        cells = row.locator("td").all_inner_texts()
        return cells[cpu_col_index]

    def get_yellow_label_value(self) -> str:
        return self._yellow_label().inner_text()

    def extract_cpu_from_label(self) -> str:
        label_text = self.get_yellow_label_value()
        import re

        match = re.search(r"\d+(?:\.\d+)?%", label_text)
        return match.group(0) if match else label_text

    def get_all_row_texts(self) -> List[str]:
        return self._table_rows().all_inner_texts()

    def get_all_table_data(self) -> List[List[str]]:
        rows = self._table_rows()
        row_count = rows.count()
        data: List[List[str]] = []
        for i in range(row_count):
            cells = rows.nth(i).locator("td")
            data.append(cells.all_inner_texts())
        return data

    def get_row_count(self) -> int:
        return self._table_rows().count()
