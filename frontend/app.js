const API = 'http://localhost:8080/api';
let token = localStorage.getItem('token');
let role  = localStorage.getItem('role');
let currentTab = 'professors';
 
document.addEventListener('DOMContentLoaded', () => {
  if (token) updateUIAfterLogin();
  loadProfessors();
});

async function login() {
  const username = document.getElementById('login-username').value;
  const password = document.getElementById('login-password').value;
  try {
    const res = await apiPost('/auth/login', { username, password }, false);
    token = res.token; role = res.role;
    localStorage.setItem('token', token);
    localStorage.setItem('role', role);
    localStorage.setItem('username', res.username);
    closeModal();
    updateUIAfterLogin();
    showAlert('Uspešna prijava! Dobrodošli, ' + res.username, 'success');
    loadProfessors();
  } catch(e) { showAlert(e.message, 'error'); }
}
 
async function register() {
  const username = document.getElementById('reg-username').value;
  const email    = document.getElementById('reg-email').value;
  const password = document.getElementById('reg-password').value;
  const regRole  = document.getElementById('reg-role').value;
  try {
    const res = await apiPost('/auth/register',
      { username, email, password, role: regRole }, false);
    token = res.token; role = res.role;
    localStorage.setItem('token', token);
    localStorage.setItem('role', role);
    localStorage.setItem('username', res.username);
    closeModal();
    updateUIAfterLogin();
    showAlert('Registracija uspešna!', 'success');
  } catch(e) { showAlert(e.message, 'error'); }
}
 
function logout() {
  token = role = null;
  localStorage.clear();
  document.getElementById('user-info').classList.add('hidden');
  document.getElementById('auth-buttons').classList.remove('hidden');
  document.querySelectorAll('.admin-only').forEach(
    el => el.classList.add('hidden'));
  showAlert('Odjavili ste se', 'success');
  loadProfessors();
}
 
function updateUIAfterLogin() {
  document.getElementById('user-info').classList.remove('hidden');
  document.getElementById('auth-buttons').classList.add('hidden');
  document.getElementById('username-display').textContent =
    localStorage.getItem('username');
  document.getElementById('role-badge').textContent = role;
  if (role === 'ADMIN') {
    document.querySelectorAll('.admin-only').forEach(
      el => el.classList.remove('hidden'));
  }
}
 
function showTab(tab) {
  currentTab = tab;
  document.getElementById('professors-tab').classList
    .toggle('hidden', tab !== 'professors');
  document.getElementById('subjects-tab').classList
    .toggle('hidden', tab !== 'subjects');
  document.getElementById('tab-professors').classList
    .toggle('active', tab === 'professors');
  document.getElementById('tab-subjects').classList
    .toggle('active', tab === 'subjects');
  if (tab === 'professors') loadProfessors();
  else loadSubjects();
}
 
// CRUD: Profesori
async function loadProfessors() {
  if (!token) {
    document.getElementById('professors-grid').innerHTML =
      '<p style="color:#64748B">Prijavite se da vidite podatke.</p>';
    return;
  }
  try {
    const data = await apiGet('/professors');
    const grid = document.getElementById('professors-grid');
    grid.innerHTML = data.map(p => `
      <div class='card'>
        <div class='card-title'>${p.firstName} ${p.lastName}</div>
        <div class='card-meta'>Email: ${p.email}</div>
        <div class='card-meta'>Departman:
          <span class='tag'>${p.department}</span></div>
        <div class='card-actions admin-only
          ${role !== 'ADMIN' ? 'hidden' : ''}'>
          <button class='btn btn-secondary'
            onclick='editProfessor(${p.id})'>Izmeni</button>
          <button class='btn btn-danger'
            onclick='deleteProfessor(${p.id})'>Obrisi</button>
        </div>
      </div>`).join('');
  } catch(e) { showAlert('Greška pri učitavanju', 'error'); }
}
 
function showProfessorForm(p = null) {
  document.getElementById('professor-modal-title').textContent =
    p ? 'Izmeni Profesora' : 'Dodaj Profesora';
  document.getElementById('prof-id').value        = p?.id || '';
  document.getElementById('prof-firstName').value = p?.firstName || '';
  document.getElementById('prof-lastName').value  = p?.lastName || '';
  document.getElementById('prof-email').value     = p?.email || '';
  document.getElementById('prof-department').value= p?.department || '';
  showModal('professor');
}
 
async function editProfessor(id) {
  const p = await apiGet('/professors/' + id);
  showProfessorForm(p);
}
 
async function saveProfessor() {
  const id = document.getElementById('prof-id').value;
  const dto = {
    firstName:  document.getElementById('prof-firstName').value,
    lastName:   document.getElementById('prof-lastName').value,
    email:      document.getElementById('prof-email').value,
    department: document.getElementById('prof-department').value
  };
  try {
    if (id) await apiPut('/professors/' + id, dto);
    else     await apiPost('/professors', dto);
    closeModal(); loadProfessors();
    showAlert(id ? 'Profesor izmenjen!' : 'Profesor dodat!', 'success');
  } catch(e) { showAlert(e.message, 'error'); }
}
 
async function deleteProfessor(id) {
  if (!confirm('Brisanje profesora ce obrisati i sve njegove predmete!')) return;
  try {
    await apiDelete('/professors/' + id);
    loadProfessors();
    showAlert('Profesor obrisan (i svi njegovi predmeti)!', 'success');
  } catch(e) { showAlert(e.message, 'error'); }
}
 
// CRUD: Predmeti
async function loadSubjects() {
  if (!token) return;
  try {
    const data = await apiGet('/subjects');
    const grid = document.getElementById('subjects-grid');
    grid.innerHTML = data.map(s => `
      <div class='card'>
        <div class='card-title'>${s.name}</div>
        <div class='card-meta'>Profesor: ${s.professorName}</div>
        <div class='card-meta'>
          <span class='tag'>ESPB: ${s.espb}</span>
          <span class='tag'>Sem: ${s.semester}</span>
        </div>
        ${s.description ? `<div class='card-meta' style='margin-top:8px'>
          ${s.description}</div>` : ''}
        <div class='card-actions
          ${role !== 'ADMIN' ? 'hidden' : ''}'>
          <button class='btn btn-secondary'
            onclick='editSubject(${s.id})'>Izmeni</button>
          <button class='btn btn-danger'
            onclick='deleteSubject(${s.id})'>Obrisi</button>
        </div>
      </div>`).join('');
  } catch(e) { showAlert('Greška pri učitavanju predmeta', 'error'); }
}
 
async function showSubjectForm(s = null) {
  const professors = await apiGet('/professors');
  const sel = document.getElementById('subj-professor');
  sel.innerHTML = '<option value="">Izaberi profesora</option>' +
    professors.map(p =>
      `<option value='${p.id}'>${p.firstName} ${p.lastName}</option>`
    ).join('');
  document.getElementById('subject-modal-title').textContent =
    s ? 'Izmeni Predmet' : 'Dodaj Predmet';
  document.getElementById('subj-id').value          = s?.id || '';
  document.getElementById('subj-name').value        = s?.name || '';
  document.getElementById('subj-description').value = s?.description || '';
  document.getElementById('subj-espb').value        = s?.espb || '';
  document.getElementById('subj-semester').value    = s?.semester || '';
  if (s) sel.value = s.professorId;
  showModal('subject');
}
 
async function editSubject(id) {
  const s = await apiGet('/subjects/' + id);
  showSubjectForm(s);
}
 
async function saveSubject() {
  const id = document.getElementById('subj-id').value;
  const dto = {
    name:        document.getElementById('subj-name').value,
    description: document.getElementById('subj-description').value,
    espb:     +document.getElementById('subj-espb').value,
    semester: +document.getElementById('subj-semester').value,
    professorId: +document.getElementById('subj-professor').value
  };
  try {
    if (id) await apiPut('/subjects/' + id, dto);
    else     await apiPost('/subjects', dto);
    closeModal(); loadSubjects();
    showAlert(id ? 'Predmet izmenjen!' : 'Predmet dodat!', 'success');
  } catch(e) { showAlert(e.message, 'error'); }
}
 
async function deleteSubject(id) {
  if (!confirm('Obrisati predmet?')) return;
  try {
    await apiDelete('/subjects/' + id);
    loadSubjects();
    showAlert('Predmet obrisan!', 'success');
  } catch(e) { showAlert(e.message, 'error'); }
}
 
function headers(auth = true) {
  const h = { 'Content-Type': 'application/json' };
  if (auth && token) h['Authorization'] = 'Bearer ' + token;
  return h;
}
 
async function apiGet(path) {
  const res = await fetch(API + path, { headers: headers() });
  if (!res.ok) throw new Error(await errorMsg(res));
  return res.json();
}
 
async function apiPost(path, body, auth = true) {
  const res = await fetch(API + path, {
    method: 'POST', headers: headers(auth),
    body: JSON.stringify(body) });
  if (!res.ok) throw new Error(await errorMsg(res));
  return res.json();
}
 
async function apiPut(path, body) {
  const res = await fetch(API + path, {
    method: 'PUT', headers: headers(),
    body: JSON.stringify(body) });
  if (!res.ok) throw new Error(await errorMsg(res));
  return res.json();
}
 
async function apiDelete(path) {
  const res = await fetch(API + path,
    { method: 'DELETE', headers: headers() });
  if (!res.ok) throw new Error(await errorMsg(res));
}
 
async function errorMsg(res) {
  try { const j = await res.json(); return j.message || 'Greška'; }
  catch { return 'Greška ' + res.status; }
}
 
function showModal(name) {
  document.querySelectorAll('.modal-overlay')
    .forEach(m => m.classList.add('hidden'));
  document.getElementById(name + '-modal')
    .classList.remove('hidden');
}
 
function closeModal() {
  document.querySelectorAll('.modal-overlay')
    .forEach(m => m.classList.add('hidden'));
}
 
function showAlert(msg, type) {
  const el = document.getElementById('alert');
  el.textContent = msg;
  el.className = 'alert alert-' + type;
  el.classList.remove('hidden');
  setTimeout(() => el.classList.add('hidden'), 4000);
}
 
document.querySelectorAll('.modal-overlay').forEach(overlay => {
  overlay.addEventListener('click', e => {
    if (e.target === overlay) closeModal();
  });
});