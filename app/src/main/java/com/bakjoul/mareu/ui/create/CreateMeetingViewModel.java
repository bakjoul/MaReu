package com.bakjoul.mareu.ui.create;

import android.content.Context;
import android.util.Patterns;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.bakjoul.mareu.data.model.Meeting;
import com.bakjoul.mareu.data.model.Room;
import com.bakjoul.mareu.data.repository.MeetingRepository;
import com.bakjoul.mareu.ui.MeetingViewEvent;
import com.bakjoul.mareu.utils.SingleLiveEvent;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;

@HiltViewModel
public class CreateMeetingViewModel extends ViewModel {

    @NonNull
    private final MeetingRepository meetingRepository;

    private final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd MMMM yyyy", Locale.FRENCH);
    private final DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm", Locale.FRENCH);

    private final MutableLiveData<CreateMeetingViewState> createMeetingViewStateMutableLiveData = new MutableLiveData<>();

    private final SingleLiveEvent<MeetingViewEvent> singleLiveEvent = new SingleLiveEvent<>();

    @Nullable
    private String subject;
    @NonNull
    private final List<String> participants = new ArrayList<>();
    @Nullable
    private Room room;
    @Nullable
    private LocalDate date;
    @Nullable
    private LocalTime start;
    @Nullable
    private LocalTime end;

    @Inject
    public CreateMeetingViewModel(@NonNull MeetingRepository meetingRepository) {
        this.meetingRepository = meetingRepository;

        createMeetingViewStateMutableLiveData.setValue(
                new CreateMeetingViewState(
                        Room.values(),
                        formatDate(date),
                        formatTime(start),
                        formatTime(end),
                        null,
                        null,
                        null,
                        null,
                        null,
                        null
                ));
    }

    public LiveData<CreateMeetingViewState> getViewStateLiveData() {
        return createMeetingViewStateMutableLiveData;
    }

    public SingleLiveEvent<MeetingViewEvent> getSingleLiveEvent() {
        return singleLiveEvent;
    }

    // Met à jour la LiveData quand le champ sujet change
    public void onSubjectChanged(String subject) {
        this.subject = subject;
        CreateMeetingViewState viewState = createMeetingViewStateMutableLiveData.getValue();

        if (!subject.isEmpty() && viewState != null) {
            createMeetingViewStateMutableLiveData.setValue(
                    new CreateMeetingViewState(
                            viewState.getRooms(),
                            viewState.getDate(),
                            viewState.getStart(),
                            viewState.getEnd(),
                            null,
                            viewState.getParticipantsError(),
                            viewState.getRoomError(),
                            viewState.getDateError(),
                            viewState.getStartError(),
                            viewState.getEndError()
                    )
            );
        }
    }

    // Met à jour la LiveData quand le champ participants change
    public void onParticipantsChanged(@NonNull List<String> participantsInput) {
        participants.clear();

        if (!participantsInput.isEmpty())
            participants.addAll(participantsInput);

        CreateMeetingViewState viewState = createMeetingViewStateMutableLiveData.getValue();

        if (!participantsInput.isEmpty() && viewState != null) {
            createMeetingViewStateMutableLiveData.setValue(
                    new CreateMeetingViewState(
                            viewState.getRooms(),
                            viewState.getDate(),
                            viewState.getStart(),
                            viewState.getEnd(),
                            viewState.getSubjectError(),
                            null,
                            viewState.getRoomError(),
                            viewState.getDateError(),
                            viewState.getStartError(),
                            viewState.getEndError()
                    )
            );
        }
    }

    // Met à jour la LiveData quand la salle change
    public void onRoomChanged(@Nullable Room room) {
        this.room = room;
        CreateMeetingViewState viewState = createMeetingViewStateMutableLiveData.getValue();

        if (room != null && viewState != null) {
            createMeetingViewStateMutableLiveData.setValue(
                    new CreateMeetingViewState(
                            viewState.getRooms(),
                            viewState.getDate(),
                            viewState.getStart(),
                            viewState.getEnd(),
                            viewState.getSubjectError(),
                            viewState.getParticipantsError(),
                            null,
                            viewState.getDateError(),
                            viewState.getStartError(),
                            viewState.getEndError()
                    )
            );
        }
    }

    public void onDisplayDatePickerClicked() {
        singleLiveEvent.setValue(MeetingViewEvent.DISPLAY_CREATE_MEETING_DATE_PICKER);
    }

    public void onDisplayStartTimePickerClicked() {
        singleLiveEvent.setValue(MeetingViewEvent.DISPLAY_CREATE_MEETING_START_PICKER);
    }

    public void onDisplayEndTimePickerClicked() {
        singleLiveEvent.setValue(MeetingViewEvent.DISPLAY_CREATE_MEETING_END_PICKER);
    }

    public void onOverlappingMeetingDetected() {
        singleLiveEvent.setValue(MeetingViewEvent.DISPLAY_OVERLAPPING_MEETING_TOAST);
    }

    private void onMeetingSuccessfullyCreated() {
        singleLiveEvent.setValue(MeetingViewEvent.DISMISS_CREATE_MEETING_DIALOG);
    }

    private void onInvalidMeetingStartTimeSet() {
        singleLiveEvent.setValue(MeetingViewEvent.DISPLAY_MEETING_START_TIME_PASSED_TOAST);
    }

    private void onDurationShort() {
        singleLiveEvent.setValue(MeetingViewEvent.DISPLAY_MINIMUM_MEETING_DURATION_TOAST);
    }

    private void onDurationLong() {
        singleLiveEvent.setValue(MeetingViewEvent.DISPLAY_MAXIMUM_MEETING_DURATION_TOAST);
    }

    public void onDateChanged(int year, int month, int day) {
        date = LocalDate.of(year, month + 1, day);  // On ajoute 1 car les mois commencent à 0

        CreateMeetingViewState viewState = createMeetingViewStateMutableLiveData.getValue();
        if (date != null && viewState != null) {
            createMeetingViewStateMutableLiveData.setValue(
                    new CreateMeetingViewState(
                            viewState.getRooms(),
                            formatDate(date),
                            viewState.getStart(),
                            viewState.getEnd(),
                            viewState.getSubjectError(),
                            viewState.getParticipantsError(),
                            viewState.getRoomError(),
                            null,
                            viewState.getStartError(),
                            viewState.getEndError()
                    )
            );
        }
    }

    public void onStartTimeChanged(int hour, int minute) {
        start = LocalTime.of(hour, minute);
        if (end == null)
            end = start.plusMinutes(CreateMeetingDialogFragment.MEETING_MIN_DURATION);

        CreateMeetingViewState viewState = createMeetingViewStateMutableLiveData.getValue();
        if (viewState != null) {
            createMeetingViewStateMutableLiveData.setValue(
                    new CreateMeetingViewState(
                            viewState.getRooms(),
                            viewState.getDate(),
                            formatTime(start),
                            formatTime(end),
                            viewState.getSubjectError(),
                            viewState.getParticipantsError(),
                            viewState.getRoomError(),
                            viewState.getDateError(),
                            null,
                            viewState.getEndError()
                    )
            );
        }
    }

    public void onEndTimeChanged(int hour, int minute) {
        end = LocalTime.of(hour, minute);

        CreateMeetingViewState viewState = createMeetingViewStateMutableLiveData.getValue();
        if (viewState != null) {
            createMeetingViewStateMutableLiveData.setValue(
                    new CreateMeetingViewState(
                            viewState.getRooms(),
                            viewState.getDate(),
                            viewState.getStart(),
                            formatTime(end),
                            viewState.getSubjectError(),
                            viewState.getParticipantsError(),
                            viewState.getRoomError(),
                            viewState.getDateError(),
                            viewState.getStartError(),
                            null
                    )
            );
        }
    }

    private String formatTime(@Nullable LocalTime time) {
        String formattedTime = null;
        if (time != null)
            formattedTime = time.format(timeFormatter);
        return formattedTime;
    }

    private String formatDate(@Nullable LocalDate date) {
        String formattedDate = null;
        if (date != null)
            formattedDate = date.format(dateFormatter);
        return formattedDate;
    }

    private Boolean areInputsOk() {
        boolean inputsOk = true;

        String subjectError;
        if (subject == null || subject.isEmpty()) {
            subjectError = "Veuillez saisir un sujet";
            inputsOk = false;
        } else
            subjectError = null;

        String participantsError;
        if (participants.isEmpty()) {
            participantsError = "Veuillez saisir au moins un participant";
            inputsOk = false;
        } else
            participantsError = null;

        String roomError;
        if (room == null) {
            roomError = "Veuillez sélectionner une salle";
            inputsOk = false;
        } else
            roomError = null;

        String dateError;
        if (date == null) {
            dateError = "Veuillez sélectionner une date";
            inputsOk = false;
        } else
            dateError = null;

        String startError;
        if (start == null) {
            startError = "Veuillez définir une heure de début";
            inputsOk = false;
        } else
            startError = null;

        String endError;
        if (end == null) {
            endError = "Veuillez définir une heure de fin";
            inputsOk = false;
        } else
            endError = null;

        createMeetingViewStateMutableLiveData.setValue(
                new CreateMeetingViewState(
                        Room.values(),
                        formatDate(date),
                        formatTime(start),
                        formatTime(end),
                        subjectError,
                        participantsError,
                        roomError,
                        dateError,
                        startError,
                        endError
                )
        );

        return inputsOk;
    }

    // Vérifie que la réunion à créer n'en chevauche pas une autre
    private Boolean areRoomAndTimeSlotAvailable() {
        boolean areAvailable = true;
        List<Meeting> meetings = meetingRepository.getMeetingsLiveData().getValue();

        // Si la liste est vide, arrête la vérification
        if (meetings == null)
            return true;

        // Vérifie que l'heure de début saisie n'est pas dans le passé
        if (start != null && date != null && date.isEqual(LocalDate.now()) && start.isBefore(LocalTime.now())) {
            onInvalidMeetingStartTimeSet();
            return false;
        }

        // Parcourt les réunions existantes
        for (Meeting m : meetings) {
            // Si la date et la salle de la réunion en cours d'itération sont égales à celles de la réunion à créer
            if (m.getDate().isEqual(date) && m.getRoom() == room
                    // et que la réunion à créer commence entre l'heure de début incluse et l'heure de fin exclue de celle itérée
                    && (((Objects.requireNonNull(start).equals(m.getStart()) || Objects.requireNonNull(start).isAfter(m.getStart())) && start.isBefore(m.getEnd())
                    // ou que la réunion itérée commence entre l'heure de début incluse et l'heure de fin exclue de celle à créer
                    || ((m.getStart().equals(start) || m.getStart().isAfter(start)) && m.getStart().isBefore(end))))) {
                // alors la salle n'est pas disponible sur le créneau choisi
                areAvailable = false;
                onOverlappingMeetingDetected();
                break;
            }
        }
        return areAvailable;
    }

    @NonNull
    private Boolean isDurationOk() {
        Duration duration = Duration.between(start, end);
        if (duration.getSeconds() < CreateMeetingDialogFragment.MEETING_MIN_DURATION * 60) {
            onDurationShort();
            return false;
        } else if (duration.getSeconds() > CreateMeetingDialogFragment.MEETING_MAX_DURATION * 60) {
            onDurationLong();
            return false;
        }
        return true;
    }

    public void createMeeting() {
        if (areInputsOk() && areRoomAndTimeSlotAvailable() && isDurationOk()) {
            meetingRepository.addMeeting(
                    Objects.requireNonNull(subject),
                    Objects.requireNonNull(date),
                    Objects.requireNonNull(start),
                    Objects.requireNonNull(end),
                    Objects.requireNonNull(room),
                    participants
            );
            onMeetingSuccessfullyCreated();
        }
    }

    public void displayToast(Context context, String message, boolean isShort) {
        int duration = Toast.LENGTH_LONG;
        if (isShort)
            duration = Toast.LENGTH_SHORT;

        Toast toast = Toast.makeText(context, message, duration);
        toast.show();
    }

    public String parseString(String participant) {
        String currentString = "";
        if (participant.length() > 0) {
            currentString = participant.substring(0, participant.length() - 1).replaceAll("(\\s\\u001F(.*?)\\u001F\\s)", "");
        }
        return currentString;
    }

    public boolean isEmailValid(String email) {
        return Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }
}
