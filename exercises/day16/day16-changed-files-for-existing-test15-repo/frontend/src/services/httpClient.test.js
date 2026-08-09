import { describe, expect, it, vi } from 'vitest';
import { apiRequest, buildQueryString } from './httpClient.js';
import { createJsonResponse } from '../test/testUtils.jsx';

describe('httpClient', () => {
  it('builds query strings without empty values', () => {
    const query = buildQueryString({ page: 1, size: 5, sortBy: 'assetTag', direction: 'asc', status: '' });

    expect(query).toBe('page=1&size=5&sortBy=assetTag&direction=asc');
  });

  it('adds bearer token and JSON body when sending a request', async () => {
    const fetchSpy = vi.spyOn(globalThis, 'fetch').mockResolvedValue(createJsonResponse({ ok: true }));

    await apiRequest('/api/v1/assets/A001', {
      method: 'PUT',
      token: 'abc123',
      body: { status: 'ASSIGNED' }
    });

    expect(fetchSpy).toHaveBeenCalledWith('/api/v1/assets/A001', {
      method: 'PUT',
      headers: {
        Authorization: 'Bearer abc123',
        'Content-Type': 'application/json'
      },
      body: JSON.stringify({ status: 'ASSIGNED' })
    });
  });
});
