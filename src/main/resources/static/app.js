const API_BASE = '/api/ballot-tally';

const statusEl = document.getElementById('status');
const errorEl = document.getElementById('error');
const fieldsEl = document.getElementById('candidate-fields');
const formEl = document.getElementById('ballot-form');
const confirmButton = document.getElementById('confirm-button');

function showError(message) {
  errorEl.textContent = message;
}

function clearError() {
  errorEl.textContent = '';
}

async function fetchJson(url, options) {
  const response = await fetch(url, options);
  if (!response.ok) {
    throw new Error(`Request to ${url} failed with status ${response.status}`);
  }
  if (response.status === 204) {
    return null;
  }
  return response.json();
}

async function refreshStatus() {
  try {
    const data = await fetchJson(`${API_BASE}/status`);
    statusEl.textContent = data.status;
  } catch (err) {
    showError(`Could not load status: ${err.message}`);
  }
}

async function loadCandidates() {
  try {
    const candidates = await fetchJson(`${API_BASE}/candidates`);
    fieldsEl.innerHTML = '';
    candidates.forEach((candidate) => {
      const label = document.createElement('label');
      label.style.display = 'block';
      label.textContent = candidate;

      const input = document.createElement('input');
      input.type = 'number';
      input.min = '0';
      input.name = candidate;
      input.dataset.candidate = candidate;

      label.appendChild(input);
      fieldsEl.appendChild(label);
    });
  } catch (err) {
    fieldsEl.textContent = '';
    showError(`Could not load candidates: ${err.message}`);
  }
}

async function submitBallotCount(event) {
  event.preventDefault();
  clearError();

  const inputs = fieldsEl.querySelectorAll('input[data-candidate]');
  const candidateVotes = [];
  inputs.forEach((input) => {
    if (input.value !== '') {
      candidateVotes.push({
        candidate: input.dataset.candidate,
        votes: Number(input.value),
      });
    }
  });

  try {
    await fetchJson(`${API_BASE}/enter`, {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify({ candidateVotes }),
    });
    await refreshStatus();
  } catch (err) {
    showError(`Could not submit ballot count: ${err.message}`);
  }
}

async function confirmTally() {
  clearError();
  try {
    await fetchJson(`${API_BASE}/submit`, { method: 'POST' });
    await refreshStatus();
  } catch (err) {
    showError(`Could not confirm tally: ${err.message}`);
  }
}

formEl.addEventListener('submit', submitBallotCount);
confirmButton.addEventListener('click', confirmTally);

loadCandidates();
refreshStatus();
