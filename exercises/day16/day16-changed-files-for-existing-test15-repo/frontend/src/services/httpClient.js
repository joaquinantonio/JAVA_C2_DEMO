export async function apiRequest(path, options = {}) {
  const {
    token,
    body,
    headers = {},
    ...fetchOptions
  } = options;

  const requestHeaders = {
    ...headers
  };

  if (token) {
    requestHeaders.Authorization = `Bearer ${token}`;
  }

  const requestOptions = {
    ...fetchOptions,
    headers: requestHeaders
  };

  if (body !== undefined) {
    requestHeaders['Content-Type'] = 'application/json';
    requestOptions.body = JSON.stringify(body);
  }

  const response = await fetch(path, requestOptions);
  return parseJsonResponse(response);
}

export async function parseJsonResponse(response) {
  const contentType = response.headers.get('content-type') ?? '';
  const body = contentType.includes('application/json') ? await response.json() : null;

  if (!response.ok) {
    const message = body?.message || `Request failed with status ${response.status}`;
    const error = new Error(message);
    error.status = response.status;
    error.body = body;
    throw error;
  }

  return body;
}

export function buildQueryString(params) {
  const searchParams = new URLSearchParams();

  Object.entries(params).forEach(([key, value]) => {
    if (value !== undefined && value !== null && value !== '') {
      searchParams.set(key, String(value));
    }
  });

  return searchParams.toString();
}
