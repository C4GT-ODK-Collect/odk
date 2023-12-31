package org.odk.collect.android.formentry.saving;

import static org.odk.collect.android.formentry.saving.FormSaveViewModel.SaveResult.State.SAVING;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModelProvider;

import org.odk.collect.android.R;
import org.odk.collect.android.injection.DaggerUtils;
import org.odk.collect.material.MaterialProgressDialogFragment;

import javax.inject.Inject;

public class SaveFormProgressDialogFragment extends MaterialProgressDialogFragment {

    @Inject
    FormSaveViewModel.FactoryFactory formSaveViewModelFactoryFactory;

    private FormSaveViewModel viewModel;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        DaggerUtils.getComponent(context).inject(this);

        ViewModelProvider.Factory factory = formSaveViewModelFactoryFactory.create(requireActivity(), null);
        viewModel = new ViewModelProvider(requireActivity(), factory).get(FormSaveViewModel.class);

        setCancelable(false);
        setTitle(getString(R.string.saving_form));

        viewModel.getSaveResult().observe(this, result -> {
            if (result != null && result.getState() == SAVING && result.getMessage() != null) {
                setMessage(getString(R.string.please_wait) + "\n\n" + result.getMessage());
            } else {
                setMessage(getString(R.string.please_wait));
            }
        });
    }

    @Override
    protected OnCancelCallback getOnCancelCallback() {
        return viewModel;
    }
}
