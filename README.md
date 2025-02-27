# Research_Assistant

## Overview

This project is an AI-driven Web Content Summarizer & Knowledge Enhancer that extracts key insights from web pages. It also suggests additional topics to help users gain a deeper understanding of the content. Users can select a language from a dropdown, and both the summary and suggestions will be generated in that language.

Additionally, users can take notes on specific webpages and retrieve them whenever they revisit the same page. The project includes a Chrome Extension for seamless browsing integration.

## Features

* AI-Powered Summarization – Generates concise summaries using Gemini AI.
* Smart Topic Suggestions – Recommends additional topics for better understanding.
* Multi-Language Support – Users can select a language from a dropdown.
* Take Notes on Webpages – Users can save notes for specific webpages and view them when revisiting.
* Chrome Extension Integration – Fetch and summarize web pages directly from the browser.

## Prerequisites

### Backend Requirements

* Java 17 or higher
* Maven 3.6+
* Spring Boot

### Frontend Requirements

* HTML, CSS, JavaScript – Simple and lightweight UI.
* Chrome Extension – Extracts webpage content, allows note-taking, and interacts with the backend.

### API Requirements

* Gemini AI API Key
* API URL for Gemini AI

## Installation

### 1. Clone the Repository

git clone https://github.com/VISHNUDHARSHAN/Research_Assistant.git
cd research-assistant

### 2. Backend Setup

1. Navigate to the backend folder: 
    cd backend
2. Install dependencies:
    mvn clean install
3. Configure application.properties with the Gemini AI API details:
    gemini.api.url=YOUR_API_URL
    gemini.api.key=YOUR_API_KEY
4. Run the Spring Boot application:
5. mvn spring-boot:run

### 3. Chrome Extension Setup

1. Navigate to the chrome-extension folder:
2. cd chrome-extension
    Open Chrome and navigate to chrome://extensions/.
3. Enable Developer mode.
4. Click on Load unpacked and select the chrome-extension folder.


## API Details

### Endpoint: /api/research/process
### Method: POST
### Request Body:
{
  "content": "<content>",
  "operation": "<summarize or suggest>",
  "language": "<language>"
}

## Contact

For any inquiries or support, please contact:

### Email: vishnudharshan2003@gmail.com
### GitHub: VISHNUDHARSHAN
