document.addEventListener("DOMContentLoaded", () => {
  chrome.tabs.query({ active: true, currentWindow: true }, function (tabs) {
    const urlKey = `researchNotes_${tabs[0].url}`;
    chrome.storage.local.get([urlKey], function (result) {
      if (result[urlKey]) {
        document.getElementById("notes").value = result[urlKey];
      }
    });
  });
  document
    .getElementById("summarizeBtn")
    .addEventListener("click", () => processRequest("summarize"));
  document
    .getElementById("suggestBtn")
    .addEventListener("click", () => processRequest("suggest"));
  document.getElementById("saveNotesBtn").addEventListener("click", saveNotes);
});

// document.addEventListener("DOMContentLoaded", () => {
//   chrome.storage.local.get(["researchNotes"], function (result) {
//     if (result.researchNotes) {
//       document.getElementById("notes").value = result.researchNotes;
//     }
//   });

//   document.getElementById("saveNotesBtn").addEventListener("click", saveNotes);
// });

async function processRequest(operation) {
  try {
    const [tab] = await chrome.tabs.query({
      active: true,
      currentWindow: true,
    });
    const [{ result }] = await chrome.scripting.executeScript({
      target: { tabId: tab.id },
      function: () => window.getSelection().toString(),
    });

    if (!result) {
      showResult("Please select some text first");
      return;
    }

    const selectedLanguage = document.getElementById("languageSelect").value;

    const response = await fetch("http://localhost:8080/api/research/process", {
      method: "POST",
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify({
        content: result,
        operation: operation,
        language: selectedLanguage,
      }),
    });

    if (!response.ok) {
      throw new Error(`API Error: ${response.status}`);
    }

    const text = await response.text();
    showResult(text.replace(/\n/g, "<br>"), operation);
  } catch (error) {
    showResult("Error: " + error.message);
  }
}

async function saveNotes() {
  chrome.tabs.query({ active: true, currentWindow: true }, function (tabs) {
    const urlKey = `researchNotes_${tabs[0].url}`;
    const notes = document.getElementById("notes").value;
    chrome.storage.local.set({ [urlKey]: notes }, function () {
      alert("Notes saved successfully for this webpage");
    });
  });
}

// async function saveNotes() {
//   const notes = document.getElementById("notes").value;
//   chrome.storage.local.set({ researchNotes: notes }, function () {
//     alert("Notes saved successfully");
//   });
// }

function showResult(content, operation) {
  const resultContainer =
    operation === "summarize" ? "summarizeResults" : "suggestResults";
  document.getElementById(
    resultContainer
  ).innerHTML = `<div class="result-item"><div class="result-content">${content}</div></div>`;
}
