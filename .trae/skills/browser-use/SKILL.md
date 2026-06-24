---
name: "browser-use"
description: "Automates browser interactions using Playwright. Invoke when user needs web scraping, form filling, automated testing, or browser automation tasks."
---

# Browser Use

This skill provides browser automation capabilities using Playwright for web scraping, automated testing, and browser interactions.

## Features

- Web scraping and data extraction
- Automated form filling
- Browser automation testing
- Page navigation and interaction
- Screenshot capturing
- PDF generation

## Usage

To use this skill, you need to:

1. Install Python dependencies:
```bash
pip install browser-use
```

2. Install browser binaries:
```bash
playwright install
```

3. Use in your Python code:
```python
from browser_use import Browser

browser = Browser()
browser.navigate("https://example.com")
browser.fill_form("#search", "query")
browser.click("#submit")
```

## Requirements

- Python 3.8+
- Playwright
- Chrome/Chromium, Firefox, or WebKit browsers

## Documentation

For more details, visit the GitHub repository:
https://github.com/browser-use/browser-use