package com.bakjoul.mareu.ui.create;

import android.app.Dialog;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.ViewModelProvider;

import com.bakjoul.mareu.R;
import com.bakjoul.mareu.databinding.CreateMeetingDialogBinding;
import com.google.android.material.datepicker.MaterialDatePicker;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class CreateMeetingDialog extends DialogFragment {

    private CreateMeetingDialogBinding b;

    @Override
    public void onStart() {
        super.onStart();
        Dialog dialog = getDialog();
        if (dialog != null) {
            int width = ViewGroup.LayoutParams.MATCH_PARENT;
            int height = ViewGroup.LayoutParams.MATCH_PARENT;
            dialog.getWindow().setLayout(width, height);
            dialog.getWindow().setWindowAnimations(R.style.AppTheme_Slide);
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NORMAL, R.style.AppTheme_FullscreenDialog);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        b = CreateMeetingDialogBinding.inflate(inflater, container, false);
        return b.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        CreateMeetingViewModel viewModel = new ViewModelProvider(this).get(CreateMeetingViewModel.class);

        // Définit l'action du bouton close de la toolbar
        b.dialogToolbar.setNavigationOnClickListener(item -> dismiss());

        // Initialise le champ subject
        initSubjectEditText(viewModel, b.inputSubjectEdit);

        // Initialise le champ participants
        initParticipantsEditText(viewModel, b.inputParticipantsEdit);

        viewModel.getViewStateLiveData().observe(getViewLifecycleOwner(), createMeetingViewState ->
                // Initialise le spinner du choix de la salle
                initRoomMenu(viewModel, createMeetingViewState));

        initDatePicker();
    }

    private void initDatePicker() {
        MaterialDatePicker.Builder<Long> builder = MaterialDatePicker.Builder.datePicker();
        builder.setTitleText("Veuillez choisir une date")
                .setSelection(MaterialDatePicker.todayInUtcMilliseconds());
        MaterialDatePicker<Long> datePicker = builder.build();
        datePicker.addOnPositiveButtonClickListener(selection ->
                b.inputDateEdit.setText(datePicker.getHeaderText()));

        b.inputDateEdit.setOnClickListener(v ->
                datePicker.show(getActivity().getSupportFragmentManager(), "date_picker"));
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        b = null;
    }

    private void initSubjectEditText(CreateMeetingViewModel viewModel, EditText subject) {
        subject.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                viewModel.onSubjectChanged(editable.toString());
            }
        });
    }

    private void initParticipantsEditText(CreateMeetingViewModel viewModel, EditText participants) {
        participants.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                viewModel.onParticipantsChanged(editable.toString());
            }
        });
    }

    private void initRoomMenu(CreateMeetingViewModel viewModel, CreateMeetingViewState createMeetingViewState) {
        CreateMeetingAdapter adapter = new CreateMeetingAdapter(requireContext(), R.layout.create_meeting_spinner_item, createMeetingViewState.getRooms());
        b.autoCompleteTextView.setAdapter(adapter);
        b.autoCompleteTextView.setOnItemClickListener((adapterView, view1, i, l) ->
                viewModel.onRoomChanged(adapter.getItem(i)));
    }

}
