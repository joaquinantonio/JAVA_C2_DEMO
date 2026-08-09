export const STATUS_OPTIONS = ['AVAILABLE', 'ASSIGNED', 'MAINTENANCE'];

export const emptyAssetForm = {
  assetTag: '',
  name: '',
  category: '',
  serialNumber: '',
  status: 'AVAILABLE',
  location: '',
  assignedTo: ''
};

export function validateAssetFormStep(formValues, stepToValidate, reviewConfirmed = false) {
  const errors = {};

  if (stepToValidate === 1) {
    if (!formValues.assetTag.trim()) {
      errors.assetTag = 'Asset tag is required.';
    } else if (!/^[A-Z0-9-]+$/.test(formValues.assetTag.trim())) {
      errors.assetTag = 'Use uppercase letters, numbers and hyphens only.';
    }

    if (!formValues.name.trim()) {
      errors.name = 'Asset name is required.';
    } else if (formValues.name.trim().length < 3) {
      errors.name = 'Asset name must be at least 3 characters.';
    }

    if (!formValues.category.trim()) {
      errors.category = 'Category is required.';
    }

    if (!formValues.serialNumber.trim()) {
      errors.serialNumber = 'Serial number is required.';
    }
  }

  if (stepToValidate === 2) {
    if (!formValues.location.trim()) {
      errors.location = 'Location is required.';
    }

    if (!STATUS_OPTIONS.includes(formValues.status)) {
      errors.status = 'Choose a valid status.';
    }

    if (formValues.assignedTo.trim() && !formValues.assignedTo.includes('@')) {
      errors.assignedTo = 'Assigned user should look like an email address.';
    }
  }

  if (stepToValidate === 3 && !reviewConfirmed) {
    errors.review = 'Please confirm that you reviewed the asset details.';
  }

  return errors;
}

export function normalizeAssetFormPayload(formValues) {
  return {
    assetTag: formValues.assetTag.trim(),
    name: formValues.name.trim(),
    category: formValues.category.trim(),
    serialNumber: formValues.serialNumber.trim(),
    status: formValues.status,
    location: formValues.location.trim(),
    assignedTo: formValues.assignedTo.trim() || null
  };
}

export function formatAssetFormLabel(key) {
  return key
    .replace(/([A-Z])/g, ' $1')
    .replace(/^./, (letter) => letter.toUpperCase());
}
