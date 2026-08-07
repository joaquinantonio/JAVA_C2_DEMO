import { describe, expect, it, vi } from 'vitest';
import { render, screen } from '@testing-library/react';
import userEvent from '@testing-library/user-event';
import AssetFormWizard from './AssetFormWizard.jsx';

describe('AssetFormWizard', () => {
  it('shows inline validation errors for empty required fields', async () => {
    const user = userEvent.setup();
    render(<AssetFormWizard onSubmit={vi.fn()} />);

    await user.click(screen.getByRole('button', { name: 'Continue' }));

    expect(screen.getByText('Asset tag is required.')).toBeInTheDocument();
    expect(screen.getByText('Asset name is required.')).toBeInTheDocument();
    expect(screen.getByText('Category is required.')).toBeInTheDocument();
    expect(screen.getByText('Serial number is required.')).toBeInTheDocument();
  });

  it('submits valid form data', async () => {
    const user = userEvent.setup();
    const onSubmit = vi.fn();
    render(<AssetFormWizard onSubmit={onSubmit} />);

    await user.type(screen.getByLabelText('Asset Tag'), 'cam-2026-001');
    await user.type(screen.getByLabelText('Asset Name'), 'Training Camera');
    await user.type(screen.getByLabelText('Category'), 'Camera');
    await user.type(screen.getByLabelText('Serial Number'), 'SN-CAM-001');
    await user.click(screen.getByRole('button', { name: 'Continue' }));

    await user.type(screen.getByLabelText('Location'), 'Training Room B');
    await user.click(screen.getByRole('button', { name: 'Continue' }));

    await user.click(screen.getByLabelText('I have reviewed the asset details and they are ready to submit.'));
    await user.click(screen.getByRole('button', { name: 'Create Asset' }));

    expect(onSubmit).toHaveBeenCalledWith({
      assetTag: 'CAM-2026-001',
      name: 'Training Camera',
      category: 'Camera',
      serialNumber: 'SN-CAM-001',
      status: 'AVAILABLE',
      location: 'Training Room B',
      assignedTo: null
    });
  });
});
