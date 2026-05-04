const RENDER_BACKEND_URL = 'https://your-java-backend.onrender.com'; // Change this after deploying to Render

const API_URL = (window.location.hostname === 'localhost' || window.location.hostname === '127.0.0.1') 
    ? 'http://localhost:8080/api' 
    : RENDER_BACKEND_URL + '/api';

export const login = async (email, password) => {
  const res = await fetch(`${API_URL}/auth/login`, {
    method: 'POST',
    headers: { 'Content-Type': 'application/json' },
    body: JSON.stringify({ email, password }),
  });
  if (!res.ok) throw new Error('Login failed');
  const data = await res.json();
  localStorage.setItem('token', data.token);
  return data;
};

export const getMe = async () => {
  const token = localStorage.getItem('token');
  const res = await fetch(`${API_URL}/auth/me`, {
    headers: { 'Authorization': `Bearer ${token}` },
  });
  if (!res.ok) throw new Error('Failed to fetch user');
  return res.json();
};

export const getEmployees = async ({ status = '', search = '', page = 0, size = 10, sortBy = 'employeeId', sortDir = 'asc' }) => {
  const token = localStorage.getItem('token');
  const queryParams = new URLSearchParams();
  if (status) queryParams.append('status', status);
  if (search) queryParams.append('search', search);
  queryParams.append('page', page);
  queryParams.append('size', size);
  queryParams.append('sortBy', sortBy);
  queryParams.append('sortDir', sortDir);
  
  const url = `${API_URL}/employees?${queryParams.toString()}`;
  const res = await fetch(url, {
    headers: { 'Authorization': `Bearer ${token}` },
  });
  if (!res.ok) throw new Error('Failed to fetch');
  return res.json();
};

export const createEmployee = async (employee) => {
  const token = localStorage.getItem('token');
  const res = await fetch(`${API_URL}/employees`, {
    method: 'POST',
    headers: {
      'Authorization': `Bearer ${token}`,
      'Content-Type': 'application/json',
    },
    body: JSON.stringify(employee),
  });
  if (!res.ok) throw new Error('Failed to create');
  return res.json();
};

export const updateEmployee = async (id, employee) => {
  const token = localStorage.getItem('token');
  const res = await fetch(`${API_URL}/employees/${id}`, {
    method: 'PUT',
    headers: {
      'Authorization': `Bearer ${token}`,
      'Content-Type': 'application/json',
    },
    body: JSON.stringify(employee),
  });
  if (!res.ok) throw new Error('Failed to update');
  return res.json();
};

export const deleteEmployee = async (id) => {
  const token = localStorage.getItem('token');
  const res = await fetch(`${API_URL}/employees/${id}`, {
    method: 'DELETE',
    headers: { 'Authorization': `Bearer ${token}` },
  });
  if (!res.ok) throw new Error('Failed to delete');
};

export const uploadFile = async (file) => {
  const token = localStorage.getItem('token');
  const formData = new FormData();
  formData.append('file', file);
  
  const res = await fetch(`${API_URL}/files/upload`, {
    method: 'POST',
    headers: { 'Authorization': `Bearer ${token}` },
    body: formData,
  });
  if (!res.ok) throw new Error('Failed to upload file');
  return res.text();
};

export const getAuditLogs = async (page = 0, size = 10) => {
  const token = localStorage.getItem('token');
  const res = await fetch(`${API_URL}/employees/audit-logs?page=${page}&size=${size}`, {
    headers: { 'Authorization': `Bearer ${token}` },
  });
  if (!res.ok) throw new Error('Failed to fetch audit logs');
  return res.json();
};
