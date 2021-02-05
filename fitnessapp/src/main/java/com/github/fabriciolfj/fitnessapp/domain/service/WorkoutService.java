package com.github.fabriciolfj.fitnessapp.domain.service;

import com.github.fabriciolfj.fitnessapp.domain.entity.Workout;
import com.github.fabriciolfj.fitnessapp.domain.repository.WorkoutRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class WorkoutService {

    private final WorkoutRepository workoutRepository;

    @PreAuthorize("#workout.user == authentication.name and #oauth2.hasScope('fitnessapp')")
    public void saveWorkout(final Workout workout) {
        workoutRepository.save(workout);
    }

    public List<Workout> findWorkouts() {
        return workoutRepository.findAllByUser();
    }

    public void deleteWorkout(final Integer id) {
        workoutRepository.deleteById(id);
    }
}
