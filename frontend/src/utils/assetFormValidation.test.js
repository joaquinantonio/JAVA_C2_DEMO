import { describe, expect, it } from 'vitest';
import {
  emptyAssetForm,
  formatAssetFormLabel,
  normalizeAssetFormPayload,
  validateAssetFormStep
} from './assetFormValidation.js';

describe('assetFormValidation', () => {
  it('returns required field errors for step one', () => {
    const errors = validateAssetFormStep(emptyAssetForm, 1);

    expect(errors.assetTag).toBe('Asset tag is required.');
    expect(errors.name).toBe('Asset name is required.');
    expect(errors.category).toBe('Category is required.');
    expect(errors.serialNumber).toBe('Serial number is required.');
  });

  it('validates location, status and assigned email on step two', () => {
    const errors = validateAssetFormStep(
      {
        ...emptyAssetForm,
        location: '',
        status: 'BROKEN',
        assignedTo: 'not-an-email'
      },
      2
    );

    expect(errors.location).toBe('Location is required.');
    expect(errors.status).toBe('Choose a valid status.');
    expect(errors.assignedTo).toBe('Assigned user should look like an email address.');
  });

  it('normalizes form values before submit', () => {
    const payload = normalizeAssetFormPayload({
      ...emptyAssetForm,
      assetTag: ' LAP-2026-010 ',
      name: ' Test Laptop ',
      category: ' Laptop ',
      serialNumber: ' SN-010 ',
      location: ' HQ ',
      assignedTo: ' '
    });

    expect(payload).toEqual({
      assetTag: 'LAP-2026-010',
      name: 'Test Laptop',
      category: 'Laptop',
      serialNumber: 'SN-010',
      status: 'AVAILABLE',
      location: 'HQ',
      assignedTo: null
    });
  });

  it('formats camelCase labels for review cards', () => {
    expect(formatAssetFormLabel('serialNumber')).toBe('Serial Number');
    expect(formatAssetFormLabel('assignedTo')).toBe('Assigned To');
  });
});
